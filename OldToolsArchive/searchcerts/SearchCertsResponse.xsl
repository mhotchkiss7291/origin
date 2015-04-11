<?xml version="1.0"?>
<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">

<body>

<script language="javascript">
<![CDATA[
var enrollments = new Array();

var monthName = new Array('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
function getDateString(msecs) {
   if (msecs == 0) {
      return 'UNKNOWN';
   }
   var d = new Date(msecs);
   return d.getDate() + '&nbsp;' + monthName[d.getMonth()] + '&nbsp;' + (1900+d.getYear()) + '<br/>' +
          fixedDigits(d.getHours()) + ':' + fixedDigits(d.getMinutes()) + ':' + fixedDigits(d.getSeconds());
}

function fixedDigits(value) {
   return (value < 10 ? '0' + value : value);
}

function trim(s) {
   var i = 0;
   var j = s.length;
   for (; (i < j) && (s.charAt(i) <= ' ') ; i++) ;
   for (; (i < j) && (s.charAt(j-1) <= ' ') ; j--) ;
   return s.substring(i,j);
}

function make_enrollments(form) {
   if (form.appvgrpid.selectedIndex == 0) {
      alert('You must select an Approval Group under\n'+
            'which to pre-approve enrollments.');
      return;
   }
   var noneChecked = true;
   var element = null;
   for (var i = 0 ; i < form.elements.length ; i++) {
      element = form.elements[i];
      if (element.type == 'checkbox') {
         if (element.checked) {
            noneChecked = false;
            form.enrollments.value += '\n' + enrollments[element.value];
         }
      }
   }
   if (noneChecked) {
      alert('You must check one or more enrollments first.');
      return;
   }
   form.submit();
}

function remove_pins(form) {
   var noneChecked = true;
   for (var i = 0 ; i < form.elements.length ; i++) {
      element = form.elements[i];
      if (element.type == 'checkbox') {
         if (element.checked) {
            noneChecked = false;
            break;
         }
      }
   }
   if (noneChecked) {
      alert('You must check one or more enrollments first.');
      return;
   }
   form.action = "/GetEnrollments";
   form.submit();
}
]]>
</script>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
<td align="left">
<h2>Search Certificates: </h2>
</td>
<td align="right">
<form method="post" name="goback" action="/SearchCerts">
<b><input type="button" name="searchagain" value=" Do Another Search " onclick="goback.submit()" class="frm2"/></b>
</form>
</td>
</tr>
</table>

<h2> 
A report has been emailed to you. If the email is empty, there were no records found that matched your criteria.
</h2>

</body>

</html>
