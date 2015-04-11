use strict;
use warnings;

##################################

#If then statements
# if (condidition is met)
# 	do(this action)
#

print "Enter a number\n";
my $num = readline STDIN;
chomp($num);

# if else; notice "elsif"
if ( $num < 10 ) {
	print "It's less than 10\n";
}
elsif ( $num > 10 ) {
	print "It's greater than 10\n";
}
else {
	print "It's equal to 10\n";
}

