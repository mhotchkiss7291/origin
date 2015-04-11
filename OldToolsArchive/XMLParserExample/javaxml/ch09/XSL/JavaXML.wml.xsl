<?xml version="1.0"?>

<xsl:stylesheet version="1.0"                 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"                
                xmlns:JavaXML="http://www.oreilly.com/catalog/javaxml/"
                exclude-result-prefixes="JavaXML"
>

 <xsl:template match="JavaXML:Book">
  <xsl:processing-instruction name="cocoon-format">
   type="text/wml"
  </xsl:processing-instruction>

  <wml>
   <card id="index" title="{JavaXML:Title}">
    <p align="center">
     <i><xsl:value-of select="JavaXML:Title" /></i><br />
     <a href="#contents">Contents</a><br/>
     <a href="#copyright">Copyright</a><br/>
    </p>
   </card>
 
   <xsl:apply-templates select="JavaXML:Contents" />

   <card id="copyright" title="Copyright">
    <p align="center">
     Copyright 2000, O&apos;Reilly &amp; Associates
    </p>
   </card>
  </wml>
 </xsl:template>

 <xsl:template match="JavaXML:Contents">
  <card id="contents" title="Contents">
   <p align="center">
    <i>Contents</i><br />
    <xsl:for-each select="JavaXML:Chapter">
     <xsl:number value="position()" format="1: " />
     <xsl:value-of select="JavaXML:Heading" /><br />
    </xsl:for-each>
   </p>
  </card>
 </xsl:template>

</xsl:stylesheet>