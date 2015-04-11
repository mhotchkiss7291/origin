#use strict;
use warnings;

%days_in_summer = ( "July" => 31, "August" => 31, "September" => 30 );

# 30, of course.
print $days_in_summer{"September"} . "\n";

# It's a leap year.
$days_in_summer{"February"} = 29;

# @month_list is now ('July', 'September', 'August') !
@month_list = keys %days_in_summer;

# Order is by value, not key
print @month_list;
