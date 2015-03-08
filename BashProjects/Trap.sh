#!/bin/bash

# bash trap command
trap bashtrap INT

# bash clear screen command
clear;

# bash trap function is executed when CTRL-C is pressed:
# bash prints message => Executing bash trap subroutine !
bashtrap() {
    echo "CTRL+C Detected !...executing bash trap !"
}

# for loop from 1/10 to 10/10
for a in `seq 1 10`; do
    echo "$a/10 to Exit." 
    sleep 1;
done

echo "Exit Bash Trap Example!!!" 

#Run in a normal bash command window:
#1/10 to Exit.
#2/10 to Exit.
#3/10 to Exit.
#^CCTRL+C Detected !...executing bash trap !
#4/10 to Exit.
#5/10 to Exit.
#6/10 to Exit.
#7/10 to Exit.
#^CCTRL+C Detected !...executing bash trap !
#8/10 to Exit.
#9/10 to Exit.
#^CCTRL+C Detected !...executing bash trap !
#10/10 to Exit.
#Exit Bash Trap Example!!!
