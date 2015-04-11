Regis Authentication and LDAP Data Retrieval Package

Author: Mark Hotchkiss
Date: 2/6/01

Perform the following steps to install and use Regis authentication.

1. Copy the ITLoginAccessJaWS2.0_v1.0.jar file to your normal jar
file directory and set your CLASSPATH variable to find it. 

2. Compile the Java files and place the class files in a servlet
directory. You will of course need to have appropriate servlet.jar
files already installed as well as the jndi1_2_1.zip and ldap1_2_2.zip.
Be aware that the JNDI downloads from java.sun.com require that you 
download the jndi components one at a time (what a pain).

3. Copy the libpassword.so shared library file to a specified directory
and note the path to it. For purposes of this document, we will
assume it is installed in /opt/netscape/server4/docs/jbservlet/lib/libpassword.so.
Set permissions to the directory to 755 and 644 for the file.

4. Find the "start" script for the web server instance you are installing.
For this document we will use /opt/netscape/server4/https-buster/start. Open
the file and add the path to the shared library to the LD_LIBRARY_PATH variable
entry like so:

LD_LIBRARY_PATH=${SERVER_ROOT}/bin/${PRODUCT_NAME}/lib:
/opt/netscape/server4/docs/jbservlet/lib:${LD_LIBRARY_PATH};
export LD_LIBRARY_PATH

5. Append to just below this environment variable a new variable that points
to the Regis database URL to be consulted during authentication like so:

	ES_SERVER_HOST=sigprod.central; export ES_SERVER_HOST

6. Restart the web server with the start script you just edited.

7. Hit the "Enter" servlet web page and enter your SunID and Regis password,
press Submit.

You should receive a list of the basic employee information if the password 
is correct, and "Failed" if it is wrong.

That is all.
