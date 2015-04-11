<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsp="http://www.apache.org/1999/XSP/Core"
  xmlns:JavaXML="http://www.oreilly.com/catalog/javaxml/"
>
  <xsl:template match="xsp:page">
    <xsp:page>
      <xsl:copy>
        <xsl:apply-templates select="@*"/>
      </xsl:copy>

      <xsp:structure>
        <xsp:include>java.util.Date</xsp:include>
        <xsp:include>java.text.SimpleDateFormat</xsp:include>
      </xsp:structure>

      <xsp:logic>
        private String getDraftDate() {
          return (new SimpleDateFormat("MM/dd/yyyy"))
            .format(new Date());
        }

        private String getTitle(int chapterNum, String chapterTitle) {
          return "Chapter " + chapterNum + ": " + chapterTitle;
        }
      </xsp:logic>

      <xsl:apply-templates/>
    </xsp:page>
  </xsl:template>

  <!-- Create formatted title -->
  <xsl:template match="JavaXML:draftTitle">
    <xsp:expr>getTitle(<xsl:value-of select="@chapterNum" />,
                       "<xsl:value-of select="@chapterTitle" />")
    </xsp:expr> - <xsp:expr>getDraftDate()</xsp:expr>
  </xsl:template>

  <xsl:template match="@*|*|text()|processing-instruction()">
    <xsl:copy>
      <xsl:apply-templates 
           select="@*|*|text()|processing-instruction()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>