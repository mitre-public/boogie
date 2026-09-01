#!/usr/bin/env bash

set -euo pipefail

if [[ $# -ne 4 ]]; then
  echo "Usage: $0 BASE_FULL.json BASE_NAV.json CANDIDATE_FULL.json CANDIDATE_NAV.json" >&2
  exit 2
fi

for result_file in "$@"; do
  if [[ ! -r "${result_file}" ]]; then
    echo "Missing JMH result file: ${result_file}" >&2
    exit 2
  fi
done

time_regression_limit=${JMH_TIME_REGRESSION_LIMIT:-0.10}
allocation_regression_limit=${JMH_ALLOCATION_REGRESSION_LIMIT:-0.01}

checks=$(
  jq --compact-output --null-input \
    --argjson timeLimit "${time_regression_limit}" \
    --argjson allocationLimit "${allocation_regression_limit}" \
    --slurpfile baseFull "$1" \
    --slurpfile baseNav "$2" \
    --slurpfile candidateFull "$3" \
    --slurpfile candidateNav "$4" '
      def benchmark($documents; $label):
        if ($documents | length) != 1
            or ($documents[0] | type) != "array"
            or ($documents[0] | length) != 1
        then error($label + " must contain exactly one JMH result")
        else $documents[0][0]
        end;

      def identity($result):
        $result.params.dataset + "/" + $result.params.specSet;

      def metric_check($identity; $name; $unit; $limit; $base; $candidate):
        if $base.scoreUnit != $unit or $candidate.scoreUnit != $unit
        then error($identity + "/" + $name + " expected " + $unit)
        elif ($base.score | type) != "number"
            or ($base.scoreError | type) != "number"
            or ($candidate.score | type) != "number"
            or ($candidate.scoreError | type) != "number"
            or $base.score <= 0
            or $candidate.score <= 0
            or $base.scoreError < 0
            or $candidate.scoreError < 0
        then error($identity + "/" + $name + " contains an invalid score")
        else
          ($candidate.score / $base.score) as $pointRatio
          | (($candidate.score - $candidate.scoreError) / ($base.score + $base.scoreError)) as $confidenceRatio
          | {
              identity: $identity,
              metric: $name,
              unit: $unit,
              baseScore: $base.score,
              baseError: $base.scoreError,
              candidateScore: $candidate.score,
              candidateError: $candidate.scoreError,
              pointRatio: $pointRatio,
              confidenceRatio: $confidenceRatio,
              limit: $limit,
              status: (
                if $confidenceRatio > (1 + $limit) then "FAIL"
                elif $pointRatio > (1 + $limit) then "WARN"
                else "PASS"
                end
              )
            }
        end;

      def compare($base; $candidate):
        if identity($base) != identity($candidate)
        then error("Cannot compare " + identity($base) + " with " + identity($candidate))
        else
          [
            metric_check(
              identity($base);
              "Time";
              "s/op";
              $timeLimit;
              $base.primaryMetric;
              $candidate.primaryMetric
            ),
            metric_check(
              identity($base);
              "Allocation";
              "B/op";
              $allocationLimit;
              $base.secondaryMetrics["gc.alloc.rate.norm"];
              $candidate.secondaryMetrics["gc.alloc.rate.norm"]
            )
          ]
        end;

      compare(
        benchmark($baseFull; "base full");
        benchmark($candidateFull; "candidate full")
      )
      + compare(
        benchmark($baseNav; "base navigation");
        benchmark($candidateNav; "candidate navigation")
      )
    '
)

report=$(
  jq --raw-output '
    def percent_change($ratio): (($ratio - 1) * 100);
    def round_to_two: (. * 100 | round) / 100;
    def round_to_four: (. * 10000 | round) / 10000;
    def signed_percent($ratio):
      (percent_change($ratio) | round_to_two) as $percent
      | if $percent >= 0 then "+\($percent)%" else "\($percent)%" end;
    def score($value; $unit):
      if $unit == "s/op" then ($value | round_to_four)
      else ($value | round)
      end;

    "### ARINC performance regression gate",
    "",
    "A result fails only when the lower bound of the candidate is more than the budget above the upper bound of the base.",
    "",
    "| Benchmark | Metric | Base | Candidate | Point change | Confirmed lower change | Budget | Result |",
    "|---|---|---:|---:|---:|---:|---:|---|",
    (.[] |
      "| \(.identity) | \(.metric) | \(score(.baseScore; .unit)) ± \(score(.baseError; .unit)) \(.unit) | \(score(.candidateScore; .unit)) ± \(score(.candidateError; .unit)) \(.unit) | \(signed_percent(.pointRatio)) | \(signed_percent(.confidenceRatio)) | \(.limit * 100)% | \(.status) |"
    )
  ' <<< "${checks}"
)

printf '%s\n' "${report}"
if [[ -n "${GITHUB_STEP_SUMMARY:-}" ]]; then
  printf '%s\n' "${report}" >> "${GITHUB_STEP_SUMMARY}"
fi

if [[ "${GITHUB_ACTIONS:-false}" == "true" ]]; then
  jq --raw-output '
    def percent_change($ratio): ((($ratio - 1) * 10000 | round) / 100);
    .[]
    | select(.status != "PASS")
    | (if .status == "FAIL" then "error" else "warning" end) as $level
    | "::\($level) title=JMH \(.status) - \(.identity) \(.metric)::Point change \(percent_change(.pointRatio))%; confidence-bound change \(percent_change(.confidenceRatio))%; budget \(.limit * 100)%"
  ' <<< "${checks}"
fi

if ! jq --exit-status 'all(.[]; .status != "FAIL")' <<< "${checks}" > /dev/null; then
  echo "A confirmed JMH performance regression exceeded its budget." >&2
  exit 1
fi
