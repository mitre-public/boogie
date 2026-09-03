#!/usr/bin/env bash

set -euo pipefail

if [[ $# -ne 4 ]]; then
  echo "Usage: $0 BASE_FULL.json BASE_NAV.json CURRENT_FULL.json CURRENT_NAV.json" >&2
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
    --slurpfile currentFull "$3" \
    --slurpfile currentNav "$4" '
      def benchmark($documents; $label):
        if ($documents | length) != 1
            or ($documents[0] | type) != "array"
            or ($documents[0] | length) != 1
        then error($label + " must contain exactly one JMH result")
        else $documents[0][0]
        end;

      def identity($result):
        $result.params.dataset + "/" + $result.params.specSet;

      def configuration($result):
        $result
        | {
            benchmark,
            mode,
            threads,
            forks,
            warmupIterations,
            warmupTime,
            warmupBatchSize,
            measurementIterations,
            measurementTime,
            measurementBatchSize,
            jmhVersion
          };

      def metric_check($identity; $name; $unit; $limit; $base; $current):
        if $base.scoreUnit != $unit or $current.scoreUnit != $unit
        then error($identity + "/" + $name + " expected " + $unit)
        elif ($base.score | type) != "number"
            or ($base.scoreError | type) != "number"
            or ($current.score | type) != "number"
            or ($current.scoreError | type) != "number"
            or $base.score <= 0
            or $current.score <= 0
            or $base.scoreError < 0
            or $current.scoreError < 0
        then error($identity + "/" + $name + " contains an invalid score")
        else
          ($current.score / $base.score) as $pointRatio
          | (($current.score - $current.scoreError) / ($base.score + $base.scoreError)) as $confidenceRatio
          | {
              identity: $identity,
              metric: $name,
              unit: $unit,
              baseScore: $base.score,
              baseError: $base.scoreError,
              currentScore: $current.score,
              currentError: $current.scoreError,
              pointRatio: $pointRatio,
              confidenceRatio: $confidenceRatio,
              limit: $limit,
              status: (
                if $confidenceRatio > (1 + $limit) then "REGRESSION"
                elif $pointRatio > (1 + $limit) then "WARN"
                else "PASS"
                end
              )
            }
        end;

      def compare($base; $current):
        if identity($base) != identity($current)
        then error("Cannot compare " + identity($base) + " with " + identity($current))
        elif configuration($base) != configuration($current)
        then error("Cannot compare different JMH configurations for " + identity($base))
        else
          [
            metric_check(
              identity($base);
              "Time";
              "s/op";
              $timeLimit;
              $base.primaryMetric;
              $current.primaryMetric
            ),
            metric_check(
              identity($base);
              "Allocation";
              "B/op";
              $allocationLimit;
              $base.secondaryMetrics["gc.alloc.rate.norm"];
              $current.secondaryMetrics["gc.alloc.rate.norm"]
            )
          ]
        end;

      compare(
        benchmark($baseFull; "base full");
        benchmark($currentFull; "current full")
      )
      + compare(
        benchmark($baseNav; "base navigation");
        benchmark($currentNav; "current navigation")
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

    "### Weekly ARINC performance comparison",
    "",
    "REGRESSION means the lower bound of the current result is more than the threshold above the upper bound of the base.",
    "Performance findings are advisory and do not fail the workflow.",
    "",
    "| Benchmark | Metric | Base | Current | Point change | Confirmed lower change | Threshold | Result |",
    "|---|---|---:|---:|---:|---:|---:|---|",
    (.[] |
      "| \(.identity) | \(.metric) | \(score(.baseScore; .unit)) ± \(score(.baseError; .unit)) \(.unit) | \(score(.currentScore; .unit)) ± \(score(.currentError; .unit)) \(.unit) | \(signed_percent(.pointRatio)) | \(signed_percent(.confidenceRatio)) | \(.limit * 100)% | \(.status) |"
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
    | "::warning title=JMH \(.status) - \(.identity) \(.metric)::Point change \(percent_change(.pointRatio))%; confidence-bound change \(percent_change(.confidenceRatio))%; threshold \(.limit * 100)%"
  ' <<< "${checks}"
fi
