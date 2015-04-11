#use strict;
use warnings;

@months = ( "July", "August", "September" );
print $months[0] . "\n";    # This prints "July".
print $months[1] . "\n";
print $months[2] . "\n";
$months[2] = "Smarch";      # We just renamed September!
print $months[2] . "\n";

# Print size of the array
# This prints 2.
print $#months . "\n";  

# We don't have an @autumn_months, so this is -1.
$a1 = $#autumn_months; 
print $#a1 . "\n";

# Reset the size of the array
# Now @months only contains "July".
$#months = 0;
print @months;