Process for deleting Safeword records based on a current LDAP
database:

1. Move to the LDAP directory to retrieve the files.
This must be done from buster for permission reasons.
Copy the file do a local directory. They are very large.
Once transfered, get off of buster.

	cd /net/sirww.central/home/cdr/downstream_load/OUTBOX
	cp gen_worker_active_v1.dat ~/dBaseCleanup
	cp gen_worker_onleave_v1.dat ~/dBaseCleanup

2. Concatinate the two files together and sort them by 
first character. Creates ldap.dat

	cat gen_worker_active_v1.dat gen_worker_onleave_v1.dat | sort > ldap.dat

3. Run runLDAPList (java LDAPList) which takes ldap.dat as input and
creates ldapout.

	runLDAPList (creates ldapout)

4. Log in to alcatraz and copy today's export from Safeword to swlist in
the /swbak directory

	cd /swbak
	cp <today's export file> swlist

5. Open swlist in a text editor and remove the first two lines of the file.
You may need to remove the old swout file.

	rm swout
	vi swlist (remove lines)

6. Run runSWList which creates swout. This takes a while.

	./runSWList (creates swout)

7. Transfer swout back to your working directory where the ldapout file
already is.

	ftp> put swout

8. Back on your main workstation, sort the ldapout and swout files to
ensure proper order. This may not be necessary for the ldapout file but
I do it anyway.

	sort -k 1.3 swout > tempfile  (sorts on the third character)
	mv tempfile swout
	sort ldapout > tempfile
	mv tempfile ldapout

9. This is the big step!. Run runSWCheck (java SWCheck) which 
takes ldapout and swout as input files and outputs "checkout" 
and "questionable"

	runSWCheck 

10. Examine "questionable" file for anomolies and note them. You may
find bogus records or bad user IDs that need to be repaired as well
as trasitioned contractors or temporary employees.

11. Run runExceptions (java Exceptions) which takes "checkout" as input
and creates "finalout".

	runExceptions (creates finalout)

12. Transfer "finalout" to alcatraz:/usr/safeword/deletejava

	ftp> get finalout

13. Run runDelete which will delete each user in the Safeword database.

14. Archive the finalout file into the deleteArchive and date and name
it appropriately (deleteArchive/deleted.020901)

	mv finalout deleteArchive/deleted.020901

15. Clean up any large files in the work directory and send questionable
file to Bryan.

