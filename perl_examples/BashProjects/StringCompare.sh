#!/bin/bash

#Declare string S1
S1="Bash"
#Declare string S2
S2="Scripting"

# no -eq for bash
if [ $S1 = $S2 ]; then
	echo "Both Strings are equal"
else 
	echo "Strings are NOT equal"
fi 
#Bash String Comparisons - values are NOT equal

#Declare string S1
S1="Bash"
#Declare string S2
S2="Bash"
if [ $S1 = $S2 ]; then
	echo "Both Strings are equal"
else 
	echo "Strings are NOT equal"
fi 
#Bash String Comparisons - values are equal