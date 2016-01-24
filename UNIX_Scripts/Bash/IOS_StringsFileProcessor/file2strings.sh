#!/bin/bash

if [ $# -ne 1 ]; then
        echo 1>&2 Usage: $0 MyStringsFile.strings
        echo
        exit 127
fi

# Declare array
declare -a ARRAY

# Link filedescriptor 10 with stdin
exec 10<&0

# stdin replaced with a file supplied as a first argument
# Enter the name of the file being processed
exec < $1

# Read each line into the array and count them
let COUNT=0
while read LINE; do
    ARRAY[$COUNT]=$LINE
    ((COUNT++))
done

#echo Number of elements: ${#ARRAY[@]}
echo COUNT $COUNT

ELEMENTS=${#ARRAY[@]}

for (( i=0; i < $ELEMENTS; i++ )); do

        string=${ARRAY[${i}]}

        # Does the line contain a double quote?
        if [[ $string == *"\""* ]]
        then

                # Does the line NOT contain an askterisk?
                if [[ $string != *"*/"* ]]

                then

                        # Can't remove // string because it is used in URLs!!
                        # echo $string | sed 's/;//' | sed -e 's/\/\/.*$/\/\//' >> \
                        # $1.clean | sed 's/#.*//'

                        # Strip the semicolon and append to a same named file with
                        # extension .clean
                        echo $string | sed 's/;//' >> $1.clean | sed 's/#.*//'
                fi
        fi
done
