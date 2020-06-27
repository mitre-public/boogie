#!/bin/bash
echo "Running CI Script 'buildAndTest' using CI branch: $bamboo_planRepository_branchName"
./gradlew build
