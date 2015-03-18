#!/usr/local/bin/perl

$baseDir = "/usr/local/projects/javaxml/foobar/books/";
$filename = "books.txt";
$bookFile = $baseDir . $filename;

# First open the file
open(FILE, $bookFile) || die "Could not open $bookFile.\n";

# Let browser know what is coming
print "Content-type: text/plain\n\n";

# Print out XML header and root element
print "<?xml version=\"1.0\"?>\n";
print "<books>\n";

# Print out books
while (<FILE>) {
  print "$_";
}


# Close root element
print "</books>\n";

close(FILE);
