#!/bin/bash

echo "Running CI Script 'ci-testIntegration' on branch: $1"
# for CI server, don't want daemon
# more info here: https://docs.gradle.org/3.3/userguide/gradle_daemon.html#when_should_i_not_use_the_gradle_daemon
./gradlew testIntegration --no-daemon --stacktrace -PmavenUser=$2 -PmavenPassword=$3
