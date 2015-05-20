package org.eac.opinion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OpinionDAO {

	public List<Opinion> findAll() {
		List<Opinion> list = new ArrayList<Opinion>();
		Connection c = null;
		String sql = "SELECT * FROM opinion ORDER BY name";
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

	public List<Opinion> findByName(String name) {
		List<Opinion> list = new ArrayList<Opinion>();
		Connection c = null;
		String sql = "SELECT * FROM opinion as e " + "WHERE UPPER(name) LIKE ? "
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

	public Opinion findById(int id) {
		String sql = "SELECT * FROM opinion WHERE id = ?";
		Opinion opinion = null;
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				opinion = processRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return opinion;
	}

	public Opinion save(Opinion opinion) {
		return opinion.getId() > 0 ? update(opinion) : create(opinion);
	}

	public Opinion create(Opinion opinion) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement(
					//"INSERT INTO opinion (name, email, expertise, description) VALUES (?, ?, ?, ?)",
					"INSERT INTO opinion (critic, description, expertise) VALUES (?, ?, ?)",
					new String[] { "ID" });
			ps.setString(1, opinion.getCritic());
			ps.setString(4, opinion.getDescription());
			ps.setString(3, opinion.getExpertise());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			// Update the id in the returned object. This is important as this
			// value must be returned to the client.
			int id = rs.getInt(1);
			opinion.setId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return opinion;
	}

	public Opinion update(Opinion opinion) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE opinion SET critic=?, description=?, expertise=? WHERE id=?");
			ps.setString(1, opinion.getCritic());
			ps.setString(2, opinion.getDescription());
			ps.setString(3, opinion.getExpertise());
			ps.setInt(4, opinion.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return opinion;
	}

	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("DELETE FROM opinion WHERE id=?");
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

	protected Opinion processRow(ResultSet rs) throws SQLException {
		Opinion opinion = new Opinion();
		opinion.setId(rs.getInt("id"));
		opinion.setCritic(rs.getString("name"));
		opinion.setDescription(rs.getString("description"));
		opinion.setExpertise(rs.getString("expertise"));
		return opinion;
	}

}
