#!/bin/sh

# Author: Mark Hotchkiss
# Tue Sep 16 10:17:03 MDT 2008

# This script is designed to provide a distribution of a release from a Perforce
# depot that only contains files that have been edited or added between two changelists
# or labels. It also generates a file "delete_files.sh" that when executed will delete
# any files that were deleted from the two changelists or labels that should be run
# on an existing user's workspace. It is to be treated as a kind of overlay to an
# existing local workspace to be updated with any files that have changed.

# Example: Suppose I want to get a distribution of //depot/myproject/... between 
# changelist 17 and labeled changelist "release_1_1". I would first delete my
# local workspace entirely. I would then issue the command:
# 
#  ./p4_incremental_release //depot/myproject/... 17 release_1_1
# 
# A p4 sync would be performed that would copy only files that were edited and
# added between the two changelists (labels). The administrator or user
# would then probably zip or tar up the top level directory and the delete_files.sh
# script and copy it to the location of the user's old release (changelist 17), 
# unpack it, overwriting any files that had changed and adding any new files that
# were added. And the script "delete_files.sh" generated would be examined
# for accurace in the relative path names and run to delete any files that
# should be deleted.
 

echo
if [ $# -ne 3 ]; then
	echo 1>&2 Usage: $0 depot_filespec changelist_old changelist_new
	echo 
	echo 1>&2 Example: $0 //depot/myproject/... 1002 1033
	exit 127
fi

FILESPEC=$1
CHANGELIST_OLD=$2
CHANGELIST_NEW=$3

echo
echo
echo "Syncing changed files from $FILESPEC changelist" $CHANGELIST_OLD"and changelist" $CHANGELIST_NEW "...." 
echo
echo

p4 diff2 -q $FILESPEC@$CHANGELIST_OLD $FILESPEC@$CHANGELIST_NEW | grep content | sed 's/^==== //' | sed 's/#.*//' | p4 -x- sync -f

echo
echo
echo "Syncing added files from $FILESPEC changelist" $CHANGELIST_OLD"and changelist" $CHANGELIST_NEW "...." 
echo
echo

p4 diff2 -q $FILESPEC@$CHANGELIST_OLD $FILESPEC@$CHANGELIST_NEW | grep "==== <none> -" | sed 's/^==== <none> - //' | sed 's/#.*//' | p4 -x- sync -f

echo
echo
echo "Generating delete script delete_file.sh for changelist" $1 "to changelist" $2 "...."
echo
echo

p4 diff2 -q $FILESPEC@$CHANGELIST_OLD $FILESPEC@$CHANGELIST_NEW | grep "<none> ===$" | sed 's/^==== \/\/depot\//rm /' | sed 's/#.*//' > delete_files.sh
chmod 755 delete_files.sh

echo "Examine delete_files.sh for accuracy and then run if correct" 
echo
echo


