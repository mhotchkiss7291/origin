#use strict;
use warnings;

for $i ( 1, 2, 3, 4, 5 ) {
	print "$i in array\n";
}

# .. operator is a range
# define array using range
@one_to_ten = ( 1 .. 10 );
$top_limit  = 25;

# loop 1 .. 10, then 15, then 20 to 25
for $i ( @one_to_ten, 15, 20 .. $top_limit ) {
	print "$i in range loop\n";
}

# Assign array value to variable $marx and loop thru array
for $marx ( 'Groucho', 'Harpo', 'Zeppo', 'Karl' ) {
	print "$marx is my favorite Marx brother.\n";
}