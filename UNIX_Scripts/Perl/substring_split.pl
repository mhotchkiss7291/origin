#use strict;
use warnings;

# Define the string
$a = "Welcome to Perl!\n";

# "Welcome"
print substr( $a, 0, 7 ) . "\n";

# " to Perl"
print substr( $a, 7 );

# Redefine the string
$a = "Welcome to Perl!\n";
# Negative index starts from the end of the string
# and counts backwards, then forward
print substr( $a, -6, 5 ) . "\n";

# Redefine the string
$a = "Welcome to Java!\n";
print $a;
# Substitute "Perl" for "Java" in the orginal string
substr( $a, 11, 4 ) = "Perl";
print $a;

# remove " to " from string
substr( $a, 7, 3 ) = "";
print $a;

# Prepend "Hello. " to the string
substr( $a, 0, 0 ) = "Hello. ";
print $a;

# Redefine the string
$a = "Hello. Welcome Perl!\n";
# Parse the string with spaces as tokens
# and create array with elements
@a = split(/ /, $a );
# Print array 
print @a;
# Print length of array
print $#a . "\n";
# Print array elements
print $a[0] . "\n";
print $a[1] . "\n";
print $a[2] . "\n";

# Redefine the string
$a = "Hello. Welcome Perl!\n";
# Print array 
print $a;
# Split the string at spaces but
# only the first space instance
@a = split(/ /, $a, 2 );
# Print array 
print @a;

# Redefine the array
@a = ( "Hello.", "Welcome", "Perl!\n" );
# Concatenate the array with spaces
# as separators
$a = join( ' ', @a );
print $a;

# Insert " and " between each element
# in the new string bprint $#a1 . "\n";
$b = join( ' and ', @a );
print $b;

# Concatenate the array witn no spaces
# in the new string
$c = join( '', @a );
print $c;

