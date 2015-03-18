<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:JavaXML="http://www.oreilly.com/catalog/javaxml/"
                version="1.0"
>

  <xsl:template match="JavaXML:Book">
    <html>
      <head>
        <title><xsl:value-of select="JavaXML:Title" /></title>
      </head>
      <body>
        <xsl:apply-templates select="*[not(self::JavaXML:Title)]" />
      </body>
    </html>
  </xsl:template>

  <xsl:template match="JavaXML:Contents">
    <center>
     <h2>Table of Contents</h2>
    </center>
    <hr />
    <ul>
     <xsl:for-each select="JavaXML:Chapter">
      <xsl:choose>
       <xsl:when test="@focus='Java'">
        <li><xsl:value-of select="JavaXML:Heading" /> (Java Focus)</li>
       </xsl:when>
       <xsl:otherwise>
        <li><xsl:value-of select="JavaXML:Heading" /> (XML Focus)</li>
       </xsl:otherwise>
      </xsl:choose>
     </xsl:for-each>
    </ul>
  </xsl:template>

  <xsl:template match="JavaXML:References">
   <p>
    <center><h3>Useful References</h3></center>
    <ol>
     <xsl:for-each select="JavaXML:Reference">
      <li>
       <xsl:element name="a">
        <xsl:attribute name="href">
         <xsl:value-of select="JavaXML:Url" />
        </xsl:attribute>
        <xsl:value-of select="JavaXML:Name" />
       </xsl:element>
      </li>
     </xsl:for-each>
    </ol>
   </p>
  </xsl:template>

  <xsl:template match="JavaXML:Copyright">
    <xsl:copy-of select="*" />
  </xsl:template>

</xsl:stylesheet>