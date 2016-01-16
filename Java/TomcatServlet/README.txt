How to set up a servlet deployed in Tomcat locally in Eclipse

1. Download Tomcat and unpack it in a handy director
2. Start Eclipse and install the WTP packages via the Eclipse Update Manager: 
	http://download.eclipse.org/releases/mars
3. Open Eclipse and configure the Tomcat Server: 
	Preferences -> Server -> Runtime Environments
	Add -> Apache Tomcat v7.0 -> Next
	Navigate to unpacked Tomcat directory
	JRE: Select local JDK
	Finish
4. Open Server perspective: Window -> Other.. -> Server -> Servers
	Start/Stop Server and recognize startup/shutdown of Tomcat
5. Create Dynamic Web project: New -> Dynamic Web project
6. Create DAO and Servlet classes under appropriate packages
7. Deploy the servlet: 
	Right click on the Servlet class
	Run As.. -> Run on Server
8. See the servlet in the browser and wait 5 seconds and re-run to see 
the counter work



