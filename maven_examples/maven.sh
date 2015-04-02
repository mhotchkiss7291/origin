#!/bin/bash

mvn archetype:generate \
-DgroupId=org.mhotchkiss.eac \
-DartifactId=./bin/mvn_eac \
-DarchetypeArtifactId=maven-archetype-quickstart \
-DinteractiveMode=false
