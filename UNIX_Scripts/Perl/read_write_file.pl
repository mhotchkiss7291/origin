#use strict;
use warnings;

# open an existing file
open( LOGFILE, "log.txt" ) or die "I couldn't get at log.txt";

$file_contents = <LOGFILE>;
print "File contents :\n$file_contents";
for $line (<LOGFILE>) {
	print $line;
}
close LOGFILE;

# Open file and blow away contents of overwrite.txt 
# if it exists and you have permissions
open( OVERWRITE, "> overwrite.txt" ) or die "$! error trying to overwrite";
print OVERWRITE "This is the new content.\n";

# Open and set to APPEND data to a file named append.txt
open( APPEND, ">> append.txt" ) or die "$! error trying to append";
print APPEND "We're adding to the end here.\n", "And here too.\n";
