#!/bin/bash

#Define bash global variable
#This variable is global and can be used anywhere in this bash script
VAR="global variable"

function bash {

	#Define bash local variable
	#This variable is local to bash function only

	local VAR="local variable"
	echo $VAR
}

# print global
echo $VAR

# function bash redefines it and prints it
bash

# Note the bash global variable did not change
# "local" is bash reserved word
# just repeating above
echo $VAR
