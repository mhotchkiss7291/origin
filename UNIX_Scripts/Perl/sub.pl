#use strict;
use warnings;

sub boo {
	print "Boo!\n";
}

# Call sub
boo();

# @_ must be the argument
# defined as an array
# and then squared
sub multiply {
	my (@ops) = @_;
	return $ops[0] * $ops[1];
}

# Use sub multiply 
for $i ( 1 .. 10 ) {
	print "$i squared is ", multiply( $i, $i ), "\n";
}