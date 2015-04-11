package org.mrh.spring.maven;

import java.util.List;

import org.mrh.spring.maven.dao.DerbyDao;
//import org.mrh.spring.maven.dao.MySqlDao;
import org.mrh.spring.maven.domainmodel.Person;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public final class Main {

	private Main() {
	};

	public static void main(String[] args) {

		DerbyDao dao = new DerbyDao();
		//MySqlDao dao = new MySqlDao();

		// Initialize the datasource, could /should be done of Spring
		// configuration
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		//dataSource.setDriverClassName("org.gjt.mm.mysql.Driver");

		dataSource.setUrl("jdbc:derby:c:\\temp\\database\\test01;create=true");
		// dataSource.setUrl("jdbc:derby:/home/mhotchkiss/derby/database/test01;create=true");
		// dataSource.setUrl("jdbc:mysql://localhost:3306/spring");
		// dataSource.setUrl("jdbc:mysql://localhost:3306");
		// dataSource.setUsername("root");
		// dataSource.setUsername("");
		// dataSource.setPassword("");

		// Inject the datasource into the dao
		dao.setDataSource(dataSource);

		dao.create("Lars", "Vogel");
		dao.create("Jim", "Knopf");
		dao.create("Lars", "Man");
		dao.create("Spider", "Man");

		System.out.println("Now select and list all persons");
		List<Person> list = dao.selectAll();

		for (Person myPerson : list) {
			System.out.print(myPerson.getFirstName() + " ");
			System.out.println(myPerson.getLastName());
		}

		System.out
				.println("Now select and list all persons with have the firstname Lars and lastname Vogel");
		list = dao.select("Lars", "Vogel");
		for (Person myPerson : list) {
			System.out.print(myPerson.getFirstName() + " ");
			System.out.println(myPerson.getLastName());
		}

		// Clean-up
		dao.deleteAll();
	}
}
