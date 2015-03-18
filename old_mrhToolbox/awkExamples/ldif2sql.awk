# This script converts an LDAP record with SAMS
# specific data and converts it to a SQL statement
# so that equivalent data may be loaded into Oracle
BEGIN {
  sawRecord=0
}

/^dn:/ {
   employeenumber=""
   postaladdress=""
   cn=""
   seaprincipalid=""
   givenname=""
   sn=""
   personaltitle=""
   generationqualifier=""
   givenname=""
   initials=""
   facsimiletelephonenumber=""
   telephonenumber=""
   mail=""
   title=""
   l=""
   st=""
   postalcode=""
   samsianacountrycode=""
   o=""
   c=""
   preferredlanguage=""
   uid=""
   samssecurityquestion=""
   samssecurityanswer=""
   samsdatelastlogin=""
   samscreateprincipalid=""
   samseditingprincipalid=""
   inetuserstatus=""
   createtimestamp=""
   modifytimestamp=""
   nextStreetLine=1
   street[1]="" 
   street[2]="" 
   street[3]=""
   dn=removeAttributeName($0)
   sawRecord=1
}

/^employeenumber:/ {
   employeenumber=removeAttributeName($0)
}

/^postaladdress:/ {
   postaladdress=removeAttributeName($0)
}

/^cn:/ {
   cn=removeAttributeName($0)
}

/^seaprincipalid:/ {
   seaprincipalid=removeAttributeName($0)
}

/^givenname:/ {
   givenname=removeAttributeName($0)
}

/^sn:/ {
   sn=removeAttributeName($0)
}

/^personaltitle:/ {
   personaltitle=removeAttributeName($0)
}

/^generationqualifier:/ {
   generationqualifier=removeAttributeName($0)
}

/^street:/ {
   street[nextStreetLine]=removeAttributeName($0)
   nextStreetLine++
}

/^givenname:/ {
   givenname=removeAttributeName($0)
}

/^initials:/ {
   initials=removeAttributeName($0)
}

/^facsimiletelephonenumber:/ {
   facsimiletelephonenumber=removeAttributeName($0)
}

/^telephonenumber:/ {
   telephonenumber=removeAttributeName($0)
}

/^mail:/ {
   mail=removeAttributeName($0)
}

/^title:/ {
   title=removeAttributeName($0)
}

/^l:/ {
   l=removeAttributeName($0)
}

/^st:/ {
   st=removeAttributeName($0)
}

/^postalcode:/ {
   postalcode=removeAttributeName($0)
}

/^samsianacountrycode:/ {
   samsianacountrycode=removeAttributeName($0)
}

/^o:/ {
   o=removeAttributeName($0)
}

/^c:/ {
   c=removeAttributeName($0)
}

/^preferredlanguage:/ {
   preferredlanguage=removeAttributeName($0)
}

/^uid:/ {
   uid=removeAttributeName($0)
}

/^samssecurityquestion:/ {
   samssecurityquestion=removeAttributeName($0)
}

/^samssecurityanswer:/ {
   samssecurityanswer=removeAttributeName($0)
}

/^samsdatelastlogin:/ {
   samsdatelastlogin=removeAttributeName($0)
}

/^samscreateprincipalid:/ {
   samscreateprincipalid=removeAttributeName($0)
}

/^samseditingprincipalid:/ {
   samseditingprincipalid=removeAttributeName($0)
}

/^inetuserstatus:/ {
   inetuserstatus=removeAttributeName($0)
}

/^createtimestamp:/ {
   createtimestamp=removeAttributeName($0)
}

/^modifytimestamp:/ {
   modifytimestamp=removeAttributeName($0)
}

function removeAttributeName(inputString) {
  firstColon=index(inputString, ":") 
  firstData = firstColon + 2 
  return substr(inputString, firstData)
}

/^$/{
  if (sawRecord>0) {
    sawRecord=0
    print "INSERT INTO ECR_REGISTRY_OWNER.ECR_CONTACT"
    print " ( PRINCIPALID,       NAME_PREFIX,        FIRST_NAME,         MIDDLE_NAME,"
    print "   LAST_NAME,         ADDRESS1,           ADDRESS2,           ADDRESS3,"
    print "   PHONE_NUMBER,      FAX_NUMBER,         EMAIL,              JOB_TITLE,"
    print "   CITY_TOWN,         STATE_PROVINCE,     POSTAL_CODE,        COUNTRY,"
    print "   COMPANY,           CREATEPRINCIPALID,  EDITINGPRINCIPALID, DATECREATED,"
    print "   DATELASTMODIFIED,  ISO_COUNTRY_CODE,   ISO_LANGUAGE_CODE )"
    print "VALUES"
    printf("  ( \"%s\" , \"%s\" ,\"%s\" ,\"%s\", \n", seaprincipalid,personaltitle,givenname, initials)
    printf("    \"%s\" , \"%s\" ,\"%s\" ,\"%s\", \n", sn,street[1],street[2], street[3])
    printf("    \"%s\" , \"%s\" ,\"%s\" ,\"%s\", \n", telephonenumber,facsimiletelephonenumber,mail, title)
    printf("    \"%s\" , \"%s\" ,\"%s\" ,\"%s\", \n", l,st,postalcode, samsianacountrycode)
    printf("    \"%s\" , \"%s\" ,\"%s\" ,\n    to_date(\"%s\",'CCYYMMDDHHMMSSZ'), \n", o,samscreateprincipalid,samseditingprincipalid, createtimestamp)
    printf("    to_date(\"%s\",'CCYYMMDDHHMMSSZ') , \"%s\" ,\"%s\" ) ; \n", modifytimestamp,c,preferredlanguage)
    print "INSERT INTO SECURITY_REGISTRY_OWNER.SCR_SECURITY"
    print "     ( PRINCIPALID,        USERNAME,       PASSWORD,"
    print "       SECURITYQUESTION,   SECURITYANSWER, CREATEPRINCIPALID,"
    print "       EDITINGPRINCIPALID, DATECREATED,    DATELASTMODIFIED )"
    print "VALUES"
    printf("  ( \"%s\" , \"%s\" ,\"%s\", \n", seaprincipalid,uid,"CANNEDPASSWORD")
    printf("    \"%s\" , \"%s\" ,\"%s\", \n", samssecurityquestion,samssecurityanswer,samscreateprincipalid)
    printf("    \"%s\" , to_date(\"%s\",'CCYYMMDDHHMMSSZ') ,to_date(\"%s\",'CCYYMMDDHHMMSSZ')); \n", samseditingprincipalid,createtimestamp,modifytimestamp)
  }
}

