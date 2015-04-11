<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
>

  <xsl:template match="books">
   <html>
    <head>
     <title>Tech Books - Your Computer Bookstore</title>
    </head>
    <body background="http://newInstance.com/javaxml/techbooks/images/background.gif" 
          link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
     <h1 align="center">
      <font face="Arial" color="#00659C">
       &lt;techbooks.com&gt;
      </font>
     </h1>
     <p align="center">
      <i><b>Your source on the Web for computing and technical books.</b></i>
     </p>
     <p align="center">
      <b><font size="4" color="#00659C">
       <u>New Listings</u>
      </font></b>
     </p>
     <table border="0" cellpadding="5" cellspacing="5">
      <tr>
       <td valign="top" align="center" nowrap="nowrap" width="115">
        <p align="center">
         <font color="#FFFFFF"><b>
          <a href="http://newInstance.com/javaxml/techbooks/">Home</a>
         </b></font>
        </p>
        <p align="center">
         <font color="#FFFFFF"><b>
          <a href="http://newInstance.com/javaxml/techbooks/current.html">Current Listings</a>
         </b></font>
        </p>
        <p align="center">
         <b><font color="#FFFFFF">
          <i>New Listings</i>
         </font></b>
        </p>
        <p align="center">
         <font color="#FFFFFF"><b>
          <a href="http://newInstance.com/javaxml/techbooks/contact.html">Contact Us</a>
         </b></font>
        </p>
       </td>
       <td valign="top" align="left">
        <table border="0" cellpadding="5" cellspacing="5">
         <tr>
          <td width="450" align="left" valign="top">
           <p>
            <b>
             Welcome to <font face="courier">techbooks.com</font>, 
             your source on the Web for computing and technical books.
             Our newest offerings are listed on the left.  To purchase any of these fine books,
             simply click on the &quot;Buy this Book!&quot; link, and you will be taken to
             the shopping cart for our store.  Enjoy!
            </b>
           </p>
           <p>
            <b>
             You should also check out our current listings, information about the
             store, and you can call us with your questions.  Use the links on the
             menu to the left to access this information.  Thanks for shopping!
            </b>
           </p>
          </td>
          <td align="left">

          <!-- Handle creation of content for each new *computer* book -->
          <xsl:apply-templates select="book[@subject='Computers']" />

          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
    </body>
   </html>
  </xsl:template>

  <!-- Only books with the subject 'Computers' will get here -->
  <xsl:template match="book">
   <table border="0" cellspacing="1" bgcolor="#000000">
    <tr>
     <td>
      <table border="0" cellpadding="3" cellspacing="0">
       <tr>
        <td width="100%" bgcolor="#00659C" nowrap="nowrap" align="center">
         <b><font color="#FFFFFF">
          <xsl:value-of select="title" />
         </font></b>
        </td>
       </tr>
       <tr>
        <td width="100%" align="center" nowrap="nowrap" bgcolor="#FFFFFF">
         <font color="#000000"><b>
          Author: <xsl:value-of select="author" /><br />
          Publisher: <xsl:value-of select="publisher" /><br />
          Pages: <xsl:value-of select="numPages" /><br />
          Price: <xsl:value-of select="saleDetails/price" /><br />
          <br />          
         </b></font>
         <xsl:element name="a">
          <xsl:attribute name="href">http://newInstance.com/javaxml/techbooks/buy.xsp?isbn=<xsl:value-of select="saleDetails/isbn" />
          </xsl:attribute>
          <font color="#00659C">Buy the Book!</font>
         </xsl:element>
        </td>
       </tr>
      </table>
     </td>
    </tr>
   </table>
   <br />
  </xsl:template>

</xsl:stylesheet>