#!/bin/bash

set -e

if [[ -z "$THREAD_COUNT" ]]; then
  echo "ERROR: please specify THREAD_COUNT"
  exit 1
fi

result=$(grep "Finished: $THREAD_COUNT" ./docker-jmeter/tests/siren-server/jmeter.log)

if [[ -n "$result" ]]; then
  echo
  echo "SUMMARY RESULT:"
  echo
  echo "$result"

  echo "#############################" >> history.log
  echo "TEST_ENV: $TEST_ENV" >> history.log
  echo "THREAD_COUNT: $THREAD_COUNT" >> history.log
  echo "$result" >> history.log
  echo "#############################" >> history.log
fi

# grep -v 200 docker-jmeter/tests/siren-server/siren-server.jtl
