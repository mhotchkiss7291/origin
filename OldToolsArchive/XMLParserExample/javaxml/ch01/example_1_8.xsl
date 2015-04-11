<?xml version="1.0"?>
<xsl:transform
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsp="http://www.apache.org/1999/XSP/Core"
>

  <xsl:template match="xsp:page">
    <xsp:page language="java">

      <xsp:structure>
        <xsp:include>java.lang.*</xsp:include>
      </xsp:structure>

      <xsp:logic>
        private static int counter = 0;

        private synchronized int currentCount() {
          return ++counter;
        }
      </xsp:logic>

      <xsp:content>
      </xsp:content>
    </xsp:page>
  </xsl:template>

  <xsl:template match="counter">
    <xsp:expr>currentCount()</xsp:expr>
  </xsl:template>

  <!-- Transcribe everthing else verbatim -->
  <xsl:template match="*|@*|comment()|pi()|text()">
    <xsl:copy>
      <xsl:apply-templates />
    </xsl:copy>
  </xsl:template>
</xsl:transform>