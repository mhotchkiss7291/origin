use strict;
use warnings;

print "What is your name?\n";
my $name = readline STDIN;
chomp($name);
print "$name\n";

my $secretword = "llama";

if ( $name eq "Mark" ) {
	print "Mark is here\n";
	print "What is the secret word?\n";

	# local variable declared
	my $guess = readline STDIN;
	chomp($guess);
	while ( $guess ne $secretword ) {
		print "Wrong. Try again.\n";

		# global variable declared
		$guess = readline STDIN;
		chomp($guess);
	}
	print "Right\n";
}
