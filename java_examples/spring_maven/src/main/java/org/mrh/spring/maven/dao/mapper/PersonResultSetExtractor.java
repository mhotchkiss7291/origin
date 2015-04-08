package org.mrh.spring.maven.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mrh.spring.maven.domainmodel.Person;
import org.springframework.jdbc.core.ResultSetExtractor;

public class PersonResultSetExtractor implements ResultSetExtractor {

	public Object extractData(ResultSet rs) throws SQLException {
		Person person = new Person();
		person.setFirstName(rs.getString(1));
		person.setLastName(rs.getString(2));
		return person;
	}

}