package org.eac.critic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CriticDAO {

	public List<Critic> findAll() {
		List<Critic> list = new ArrayList<Critic>();
		Connection c = null;
		String sql = "SELECT * FROM critic ORDER BY name";
		try {
			c = ConnectionHelper.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				list.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return list;
	}

	public List<Critic> findByName(String name) {
		List<Critic> list = new ArrayList<Critic>();
		Connection c = null;
		String sql = "SELECT * FROM critic as e " + "WHERE UPPER(name) LIKE ? "
				+ "ORDER BY name";
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "%" + name.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return list;
	}

	public Critic findById(int id) {
		String sql = "SELECT * FROM critic WHERE id = ?";
		Critic critic = null;
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				critic = processRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critic;
	}

	public Critic save(Critic critic) {
		return critic.getId() > 0 ? update(critic) : create(critic);
	}

	public Critic create(Critic critic) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement(
					"INSERT INTO critic (name, email, expertise, description) VALUES (?, ?, ?, ?)",
					new String[] { "ID" });
			ps.setString(1, critic.getName());
			ps.setString(2, critic.getEmail());
			ps.setString(3, critic.getExpertise());
			ps.setString(4, critic.getDescription());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			// Update the id in the returned object. This is important as this
			// value must be returned to the client.
			int id = rs.getInt(1);
			critic.setId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critic;
	}

	public Critic update(Critic critic) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE critic SET name=?, email=?, expertise=?, description=? WHERE id=?");
			ps.setString(1, critic.getName());
			ps.setString(2, critic.getEmail());
			ps.setString(3, critic.getExpertise());
			ps.setString(4, critic.getDescription());
			ps.setInt(5, critic.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critic;
	}

	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("DELETE FROM critic WHERE id=?");
			ps.setInt(1, id);
			int count = ps.executeUpdate();
			return count == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	}

	protected Critic processRow(ResultSet rs) throws SQLException {
		Critic critic = new Critic();
		critic.setId(rs.getInt("id"));
		critic.setName(rs.getString("name"));
		critic.setEmail(rs.getString("email"));
		critic.setExpertise(rs.getString("expertise"));
		critic.setDescription(rs.getString("description"));
		return critic;
	}

}
