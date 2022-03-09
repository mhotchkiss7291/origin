#!/bin/bash

set -e

# Generate test plan file from Ruby code - ruby-jmeter.jmx
ruby siren_test.rb

# Move JMX file to docker-jmeter tests file
mkdir -p ./docker-jmeter/tests/siren-server
mv ruby-jmeter.jmx ./docker-jmeter/tests/siren-server/ruby-jmeter.jmx

# Run siren.sh
cd ./docker-jmeter
./siren.sh
