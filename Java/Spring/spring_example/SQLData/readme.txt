Building a complete RESTful API in Java using JAX-RS and Jersey.
Consuming these services using jQuery

Start the mysqld service:

su
# service mysqld start

Set Up:

1. Create a MySQL database name "eac".
2. Execute eac.sql to create and populate the "Person" table:

        mysql spring -uroot < spring.sql

3. Import the Dynamic Web Project in Eclipse.
4. Locate eac.properties and make sure the JDBC connection string matches your database configuration
5. Run the project.
6. Test with SquirrelSQL using the following JDBC settings:

URL: jdbc:mysql://localhost:3306/spring
User: root


