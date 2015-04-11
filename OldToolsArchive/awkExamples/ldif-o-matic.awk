#Usage: nawk -f ldif-o-matic.ldif < input_file
# This script breaks a very large file into
# smaller chunks. It takes standard input, counts "dn"
# records until RECORDSPERFILE is exceeded,
# then writes it out into smaller files with a 
# name similar to "ldifgroup2.ldif" and then
# increments the file name with each output
BEGIN {
   RECORDSPERFILE=500000
   CURRENTFILE=1
   BASENAME="./ldifgroup"
   RECORDSSOFAR=0
   currentFileName=sprintf("%s%d.ldif",BASENAME, CURRENTFILE) ;
}

/^dn:/ {
   if (RECORDSSOFAR>RECORDSPERFILE) {
      RECORDSSOFAR=0
      CURRENTFILE++ ;
      currentFileName=sprintf("%s%d.ldif",BASENAME, CURRENTFILE) ;
   } else {
      RECORDSSOFAR++
   }
    print >> currentFileName
    next
   
}

{
    print >> currentFileName
}
