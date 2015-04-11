use strict;
use warnings;
$a = 0;

while ( $a != 3 ) {
	$a++;
	print "Counting up to $a...\n";
}

until ( $a == 0 ) {
	$a--;
	print "Counting down to $a...\n";
}
