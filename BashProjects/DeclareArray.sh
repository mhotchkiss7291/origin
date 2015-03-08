#!/bin/bash

#Declare array with 4 elements
ARRAY=( 'Debian Linux' 'Redhat Linux' Ubuntu Linux )

# get number of elements in the array
ELEMENTS=${#ARRAY[@]}

# echo each element in array 
# for loop

# Very clever output. 4 elements in array named ARRAY:
# ("Debian Linux", "Redhat Linux", "Ubuntu", "Linux")
# with varied syntax of strings with or without '' 

# Output is 4 elements
for (( i=0; i < $ELEMENTS; i++ )); do
    echo ${ARRAY[${i}]}
done 