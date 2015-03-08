#!/bin/bash

# Declare array
declare -a ARRAY

# A command expects the first three file descriptors to be available. 
# The first, fd 0 (standard input, stdin), is for reading. 
# The other two (fd 1, stdout and fd 2, stderr) are for writing.

# There is a stdin, stdout, and a stderr associated with each command. 

# ls 2>&1 

# means temporarily connecting the stderr of the ls command 
# to the same "resource" as the shell's stdout.

# By convention, a command reads its input from fd 0 (stdin), 
# prints normal output to fd 1 (stdout), and error ouput to fd 2 (stderr). 
# If one of those three fd's is not open, you may encounter problems:

# Link filedescriptor 10 with stdin
exec 10<&0

# stdin replaced with a file supplied as a first argument
exec < $1

let count=0
while read LINE; do
    ARRAY[$count]=$LINE
    ((count++))
done

echo Number of elements: ${#ARRAY[@]}

# echo array's content
echo ${ARRAY[@]}

# restore stdin from filedescriptor 10
# and close filedescriptor 10
exec 0<&10 10<&-