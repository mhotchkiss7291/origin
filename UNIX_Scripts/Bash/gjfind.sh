#!/bin/bash

echo
if [ $# -ne 1 ]; then
	echo 1>&2 Usage: $0 package.class
	echo 
	echo 1>&2 Example: $0 org.mrh.MyClass
	exit 127
fi

CLASS=$1
#echo $CLASS
#echo "find . -type f -name '*.jar' -print0 |  xargs -0 -I '{}' sh -c 'jar tf {} | grep $CLASS &&  echo {}'"

find . -type f -name '*.jar' -print0 |  xargs -0 -I '{}' sh -c 'jar tf {} | grep '$CLASS' &&  echo {}'
