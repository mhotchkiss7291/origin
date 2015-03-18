<?xml version="1.0"?>

<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
>

  <xsl:template match="page">
    <xsl:processing-instruction name="cocoon-format">
      type="text/html"
    </xsl:processing-instruction>
    <html>
      <head>
        <title><xsl:value-of select="title"/></title>
      </head>
      <body>
        <xsl:apply-templates select="*[not(self::title)]" />
      </body>
    </html>
  </xsl:template>

  <xsl:template match="p">
    <p align="center">
      <xsl:apply-templates />
    </p>
  </xsl:template>

</xsl:stylesheet>