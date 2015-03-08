use strict;
use warnings;

$a = 3.14159;
{
	local $a = 3;
	print "In block, \$a = $a\n";
	print "In block, \$::a = $::a\n";
}
print "Outside block, \$a = $a\n";
print "Outside block, \$::a = $::a\n";

# This outputs
# In block, $a = 3
# In block, $::a = 3
# Outside block, $a = 3.14159
# Outside block, $::a = 3.14159
