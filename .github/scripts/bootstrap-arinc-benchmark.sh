#!/usr/bin/env bash

set -euo pipefail

base_directory=${1:?Usage: bootstrap-arinc-benchmark.sh BASE_DIRECTORY}
benchmark_source=boogie-arinc/src/jmh/java/org/mitre/tdp/boogie/arinc/OneshotRecordParserBenchmark.java
benchmark_harness=boogie-arinc/jmh.gradle.kts
base_benchmark_source=${base_directory}/${benchmark_source}
base_benchmark_harness=${base_directory}/${benchmark_harness}
base_build=${base_directory}/boogie-arinc/build.gradle.kts

mkdir -p "$(dirname "${base_benchmark_source}")"
cp "${benchmark_source}" "${base_benchmark_source}"
cp "${benchmark_harness}" "${base_benchmark_harness}"

if ! grep -Fq 'registerJmhTask("jmhLidoNav"' "${base_build}" \
    && ! grep -Fq 'apply(from = "jmh.gradle.kts")' "${base_build}"; then
  printf '\napply(from = "jmh.gradle.kts")\n' >> "${base_build}"
fi
