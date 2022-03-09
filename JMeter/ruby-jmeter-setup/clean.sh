#!/bin/bash

# Clean up before new run

set -e

rm -f ruby-jmeter.jmx
rm -f *.log
rm -rf ./docker-jmeter/tests
