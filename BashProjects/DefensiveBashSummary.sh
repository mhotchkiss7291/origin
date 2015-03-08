#!/bin/bash

#Immutable global variables

#Try to keep globals to minimum
#UPPER_CASE naming
#read only declaration
#Use globals to replace cryptic $0, $1, etc.
#Globals I always use in my programs:

# Run with args: myProgname myProgdir Args
readonly PROGNAME=$(basename $0)
readonly PROGDIR=$(readlink -m $(dirname $0))
readonly ARGS="$@"

echo $PROGNAME
echo $PROGDIR
echo $ARGS

#Output: myProgname myProgdir Args

#Everything is local
#All variable should be local.

change_owner_of_file() {
    local filename=$1
    local user=$2
    local group=$3

    chown $user:$group $filename
}

change_owner_of_files() {
    local user=$1; shift
    local group=$1; shift
    local files=$@
    local i

    for i in $files
    do
        chown $user:$group $i
    done
}

# main()

# Help keep all variables local
# Intuitive for functional programming
# The only global command in the code is: main

main() {
    local files="/tmp/a /tmp/b"
    local i

    for i in $files
    do
        change_owner_of_file kfir users $i
    done
}


#Everything is a function

#The only code that is running globally is:
#Global declarations that are immutable.
#main
#Keep code clean
#procedures become descriptive

main() {
    local files=$(ls /tmp | grep pid | grep -v daemon)
}

temporary_files() {
    local dir=$1

    ls $dir \
        | grep pid \
        | grep -v daemon
}

main() {
    local files=$(temporary_files /tmp)
}

# Second example is much better. Finding files is the 
# problem of temporary_files() and not of main()’s. 
# This code is also testable, by unit testing of temporary_files().
# If you try to test the first example, you will mish mash 
# finding temporary files with main algorithm.

test_temporary_files() {
    local dir=/tmp

    touch $dir/a-pid1232.tmp
    touch $dir/a-pid1232-daemon.tmp

    returns "$dir/a-pid1232.tmp" temporary_files $dir

    touch $dir/b-pid1534.tmp

    returns "$dir/a-pid1232.tmp $dir/b-pid1534.tmp" temporary_files $dir
}

#As we see, this test does not concern main().


#Debugging functions

#Run program with -x flag:

#  bash -x my_prog.sh

# debug just a small section of code using set -x and set +x, which will 
# print debug info just for the current code wrapped with set -x … set +x.

temporary_files() {
    local dir=$1

    set -x
    ls $dir \
        | grep pid \
        | grep -v daemon
    set +x
}

#Printing function name and its arguments:

temporary_files() {
    echo $FUNCNAME $@
    local dir=$1

    ls $dir \
        | grep pid \
        | grep -v daemon
}

# So calling the function:
# temporary_files /tmp
# will print to the standard output:

temporary_files /tmp


#Code clarity

#What this code do?

#main() {
#    local dir=/tmp

#    [[ -z $dir ]] \
#        && do_something

#    [[ -n $dir ]] \
#        && do_something

#    [[ -f $dir ]] \
#        && do_something

#    [[ -d $dir ]] \
#        && do_something
#}

#Let your code speak:

#is_empty() {
#    local var=$1

#    [[ -z $var ]]
#}

#is_not_empty() {
#    local var=$1

#    [[ -n $var ]]
#}

#is_file() {
#    local file=$1

#    [[ -f $file ]]
#}

#is_dir() {
#    local dir=$1

#    [[ -d $dir ]]
#}

#main() {
#    local dir=/tmp
#
#    is_empty $dir \
#        && do_something...
#
#    is_not_empty $dir \
#        && do_something...
#
#    is_file $dir \
#        && do_something...
#
#    is_dir $dir \
#        && do_something...
#}
