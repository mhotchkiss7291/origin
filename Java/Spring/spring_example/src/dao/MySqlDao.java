package dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import dao.mapper.PersonRowMapper;
import domainmodel.Person;


// Not working yet. Still having problems with defining
// the database before transactions. Derby does this
// injection differently than MySQL and I haven't
// figured out how to include the MySQL database moniker

public class MySqlDao implements iDao {
	private DataSource dataSource;

	public void setDataSource(DataSource ds) {
		dataSource = ds;
	}

	public void create(String firstName, String lastName) {
		JdbcTemplate insert = new JdbcTemplate(dataSource);
		insert.update("INSERT INTO PERSON (FIRSTNAME, LASTNAME) VALUES(?,?)",
				new Object[] { firstName, lastName });
	}

	public List<Person> select(String firstname, String lastname) {
		JdbcTemplate select = new JdbcTemplate(dataSource);
		return select
				.query("select  FIRSTNAME, LASTNAME from PERSON where FIRSTNAME = ? AND LASTNAME= ?",
						new Object[] { firstname, lastname },
						new PersonRowMapper());
	}

	public List<Person> selectAll() {
		JdbcTemplate select = new JdbcTemplate(dataSource);
		return select.query("select FIRSTNAME, LASTNAME from PERSON",
				new PersonRowMapper());
	}

	public void deleteAll() {
		JdbcTemplate delete = new JdbcTemplate(dataSource);
		delete.update("DELETE from PERSON");
	}

	public void delete(String firstName, String lastName) {
		JdbcTemplate delete = new JdbcTemplate(dataSource);
		delete.update("DELETE from PERSON where FIRSTNAME= ? AND LASTNAME = ?",
				new Object[] { firstName, lastName });
	}

}
