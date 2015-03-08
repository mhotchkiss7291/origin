use strict;
use warnings;

##################################
# Validation of strings on stdin
#
print "What is your name?\n";

# Possibly declared earlier in program
# Notice the annoying \n required
# for this simple string comparison.
my $name = readline STDIN;

if ( $name eq "Mark\n" ) {
	print "Mark is here\n";
}
elsif ( $name eq "Groucho\n" ) {
	print "Groucho is here\n";
}
else {
	print "Don't know you.\n";
}
