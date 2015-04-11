<?xml version="1.0"?> 
<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">

<body>

<h2>Main Page for <xsl:value-of select="//user/name"/></h2>
<form name="mainpage" action="" method="post" onsubmit="return false;">
<input type="hidden" name="appvgrpid" value=""/>
<center>
<table border="0" cellspacing="0" cellpadding="3" width="95%">
<xsl:if test="//user/role[text()='Tool:LogViewer']">
   <tr>
   <td align="right"><b><input type="button" class="frm2" value=" View Log " width="10" onclick="mainpage.action='/ViewLog';mainpage.submit()"/></b></td>
   <td>&#160;</td>
   <td>View the tool's log file.</td>
   </tr>
</xsl:if>
<xsl:if test="//user/role[text()='Tool:Admin']">
   <tr>
   <td align="right"><b><input type="button" class="frm2" value=" Manage Approval Groups " width="10" onclick="mainpage.action='/GetAppvGroupsForm';mainpage.submit()"/></b></td>
   <td>&#160;</td>
   <td>Add or remove Approval Groups.</td>
   </tr>
   <tr>
   <td align="right"><b><input type="button" class="frm2" value=" Manage Tool Admins " width="10" onclick="mainpage.action='/GetToolAdmins';mainpage.submit()"/></b></td>
   <td>&#160;</td>
   <td>Add or remove tool administrators, which includes those who can manage Approval Groups.</td>
   </tr>
</xsl:if>
<xsl:if test="//user/role[text()='Cert:Publisher']">
   <tr>
   <td align="right"><b><input type="button" class="frm2" value=" Publish Certificates " width="10" onclick="mainpage.action='/PublishCerts';mainpage.submit()"/></b></td>
   <td>&#160;</td>
   <td>Publish selected certificates from a certificate database to the authentication and authorization databases.</td>
   </tr>
</xsl:if>
<xsl:if test="//user/role[text()='Profiles:Viewer'] or //user/role[contains(.,':Approver')]">
   <tr>
   <xsl:choose>
   <xsl:when test="//user/role[text()='Profiles:Manager'] or //user/role[contains(.,':Approver')]">
      <td align="right"><b><input type="button" class="frm2" value=" Manage Profiles " width="10" onclick="mainpage.action='/GetProfiles';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>Manage entries and associated roles in the Authentication and Authorization databases.</td>
   </xsl:when>
   <xsl:otherwise>
      <td align="right"><b><input type="button" class="frm2" value=" View Profiles " width="10" onclick="mainpage.action='/GetProfiles';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>View entries and associated roles in the Authentication and Authorization databases.</td>
   </xsl:otherwise>
   </xsl:choose>
   </tr>
</xsl:if>

<xsl:if test="//user/role[text()='Profiles:Manager']">
   <tr>
     <td align="right"><b><input type="button" class="frm2" value=" Search Database " width="10" onclick="mainpage.action='/SearchCerts';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>Search Database Certificates by Owner, Company, Approver</td>
   </tr>
</xsl:if>

<xsl:if test="//user/role[text()='Enrollments:Viewer'] or //user/role[contains(.,':Approver')]">
   <tr>
   <xsl:choose>
   <xsl:when test="//user/role[contains(.,':Approver')]">
      <td align="right"><b><input type="button" class="frm2" value=" Manage Enrollments " width="10" onclick="mainpage.action='/GetEnrollments';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>Manage entries in the Enrollments database.</td>
   </xsl:when>
   <xsl:otherwise>
      <td align="right"><b><input type="button" class="frm2" value=" View Enrollments " width="10" onclick="mainpage.action='/GetEnrollments';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>View entries in the Enrollments database.</td>
   </xsl:otherwise>
   </xsl:choose>
   </tr>
</xsl:if>
<xsl:if test="//user/role[contains(.,':Approver')]">
   <tr>
   <td align="right"><b><input type="button" class="frm2" value=" Approve Enrollments " width="10" onclick="mainpage.action='/GetEnrollmentForm';mainpage.submit()"/></b></td>
   <td>&#160;</td>
   <td>Pre-approve enrollments for partners associated with an Approval Group.</td>
   </tr>
</xsl:if>
<xsl:for-each select="//appvgroup">
   <xsl:sort/>
   <xsl:variable name="appvgrpid" select="@id"/>
   <xsl:variable name="appvgrpname" select="name"/>
   <xsl:if test="//user/role[text()=concat('AppvGrps:',$appvgrpid,':Manager')]">
      <tr>
      <td align="right"><b><input type="button" class="frm2" value=" Manage {$appvgrpname} Info " width="10" onclick="mainpage.action='/GetAppvGroupInfo';mainpage.appvgrpid.value='{$appvgrpid}';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>Manage the information for the <xsl:value-of select="$appvgrpname"/> Approval Group.</td>
      </tr>
      <tr>
      <td align="right"><b><input type="button" class="frm2" value=" Manage {$appvgrpname} Members " width="10" onclick="mainpage.action='/GetAppvGroupMembers';mainpage.appvgrpid.value='{$appvgrpid}';mainpage.submit()"/></b></td>
      <td>&#160;</td>
      <td>Add or remove members of the <xsl:value-of select="$appvgrpname"/> Approval Group.</td>
      </tr>
   </xsl:if>
</xsl:for-each>
</table>
</center>
</form>

</body>

</html>

