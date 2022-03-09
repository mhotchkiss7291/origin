#!/bin/bash

set -e

if [[ -z "$THREAD_COUNT" ]]; then
  export THREAD_COUNT=300
fi
echo "THREAD_COUNT: $THREAD_COUNT"

if [[ -z "$TEST_ENV" ]]; then
  export TEST_ENV=qa8
fi
echo "TEST_ENV: $TEST_ENV"

./clean.sh
./build_jmeter.sh
./get_summary.sh
