#!/usr/local/bin/perl

$baseDir = "/usr/local/projects/javaxml/foobar/books/";
$filename = "books.txt";
$bookFile = $baseDir . $filename;

# Get the user's input
use CGI;
$query = new CGI;

$title = $query->param('title');
$author = $query->param('author');
$subject = $query->param('subject');
$publisher = $query->param('publisher');
$isbn = $query->param('isbn');
$price = $query->param('price');
$numPages = $query->param('numPages');
$description = $query->param('description');

# Save the book to a file in XML
if (open(FILE, ">>" . $bookFile)) {
  print FILE "<book subject=\"" . $subject . "\">\n";
  print FILE " <title><![CDATA[" . $title . "]]></title>\n";
  print FILE " <author><![CDATA[" . $author . "]]></author>\n";
  print FILE " <publisher><![CDATA[" . $publisher . "]]></publisher>\n";
  print FILE " <numPages>" . $numPages . "</numPages>\n";
  print FILE " <saleDetails>\n";
  print FILE "  <isbn>" . $isbn . "</isbn>\n";
  print FILE "  <price>" . $price . "</price>\n";
  print FILE " </saleDetails>\n";
  print FILE " <description><![CDATA[" . $description . "]]></description>\n";
  print FILE "</book>\n\n"; 

  # Give the user a confirmation
  print <<"EOF";
Content-type: text/html

  <html>
   <head>
    <title>Foobar Public Library: Confirmation</title>
   </head>
   <body>
    <h1 align="center">Book Added</h1>
    <p align="center">
     Thank you.  The book you submitted has been added to the Library.
    </p>
   </body>
  </html>
EOF

} else {
  print <<"EOF";
Content-type: text/html

  <html>
   <head>
    <title>Foobar Public Library: Error</title>
   </head>
   <body>
    <h1 align="center">Error in Adding Book</h1>
    <p align="center">
     We're sorry.  The book you submitted has <i>not</i> been added to the Library.
    </p>
   </body>
  </html>
EOF
}
close (FILE);

