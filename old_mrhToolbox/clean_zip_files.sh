#!/bin/sh
                          
for zip_file in $(find . -print | grep "\.zip")
do
  echo deleting $zip_file 
  rm $zip_file
done

for errorlog_file in $(find . -print | grep "ErrorLog")
do
  echo deleting $errorlog_file 
  rm $errorlog_file 
done
