<?xml version="1.0"?>
<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">

<body>

<script language="javascript">
<![CDATA[
function submitsearch(form) {
   var digits = '0123456789';
   var max = form.elements['search.max'].value;
   for (var i = 0 ; i < max.length ; i++) {
      if (digits.indexOf(max.charAt(i)) == -1){
         alert('Invalid maximum number of entries: '+max+'\n'+
               'Must be a number.');
         return;
      }
   }
   form.submit();
}
]]>
</script>

<h2>Search Certificates: Search Criteria</h2>

<p>Use this form to search for certificates by owner, company, approver.</p>

<p>
Choose a search method, then enter a string appropriate for the method.
</p>

<form name="searchform" method="post" action="/SearchCerts">
<input type="hidden" name="dosearch" value="true"/>
<center>
<table border="2" cellspacing="0" cellpadding="5">
<tr>
<td>
<table border="0" cellspacing="0" cellpadding="3">

<tr>
<td align="right" valign="center"><b>Search By:</b></td>
<td align="left" valign="center">
  <select name="search.by" size="30"/>
		<option selected>"Owner"
		<option>Owner</option>
		<option>Company</option>
		<option>Approver</option>
  </select>
</td>
</tr>

<tr>
<td align="right" valign="center"><b>Search For:</b></td>
<td align="left" valign="center"><input type="text" name="search.for" value="" size="30"/></td>
</tr>

<tr>
<td colspan="2" align="center"><b><input type="button" name="search" value=" Email Me a Report " class="frm2" onclick="submitsearch(searchform)"/></b></td>
</tr>
</table>
</td>
</tr>
</table>
</center>
</form>

</body>

</html>

