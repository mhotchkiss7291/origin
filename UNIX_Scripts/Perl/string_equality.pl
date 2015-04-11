#use strict;
use warnings;

# Argument "yes" isn't numeric in numeric eq (==)
$yes_no = "no";
if ( $yes_no == "yes" ) {
	print "You said yes not no!\n";
}

$a = 5;

if ( $a == "5" ) { 
	print "Numeric equality!\n"; 
}

if ( $a eq "5" ) { 
	print "String equality!\n"; 
}
