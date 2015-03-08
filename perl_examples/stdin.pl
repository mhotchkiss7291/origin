use strict;
use warnings;

# Taking stdin
print "What is your name?\n";

#Variables
#scalar - single value ($)
#array - holds multiple values (@)
#hash - holds multiple values, but differently (%)

# my denotes a Global scalar value - constant?
my $name = readline STDIN;

# chomp removes the newline
chomp($name);

print "$name\n";
