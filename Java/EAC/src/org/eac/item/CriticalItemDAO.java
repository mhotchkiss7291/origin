package org.eac.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eac.connection.ConnectionHelper;

public class CriticalItemDAO {

	public List<CriticalItem> findAll() {
		List<CriticalItem> list = new ArrayList<CriticalItem>();
		Connection c = null;
		String sql = "SELECT * FROM critical_item ORDER BY name";
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

	public List<CriticalItem> findByName(String name) {
		List<CriticalItem> list = new ArrayList<CriticalItem>();
		Connection c = null;
		String sql = "SELECT * FROM critical_item as e "
				+ "WHERE UPPER(name) LIKE ? " + "ORDER BY name";
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

	public CriticalItem findById(int id) {
		String sql = "SELECT * FROM critical_item WHERE id = ?";
		CriticalItem critical_item = null;
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				critical_item = processRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critical_item;
	}

	public CriticalItem save(CriticalItem critical_item) {
		return critical_item.getId() > 0 ? update(critical_item)
				: create(critical_item);
	}

	public CriticalItem create(CriticalItem critical_item) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement(
					"INSERT INTO critical_item (expertise, name, description) VALUES (?, ?, ?)",
					new String[] { "ID" });
			ps.setString(1, critical_item.getExpertise());
			ps.setString(2, critical_item.getName());
			ps.setString(3, critical_item.getDescription());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			// Update the id in the returned object. This is important as this
			// value must be returned to the client.
			int id = rs.getInt(1);
			critical_item.setId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critical_item;
	}

	public CriticalItem update(CriticalItem critical_item) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE critical_item SET expertise=?, name=? description=? WHERE id=?");
			ps.setString(1, critical_item.getName());
			ps.setString(2, critical_item.getExpertise());
			ps.setString(3, critical_item.getDescription());
			ps.setInt(4, critical_item.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return critical_item;
	}

	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("DELETE FROM critical_item WHERE id=?");
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

	protected CriticalItem processRow(ResultSet rs) throws SQLException {
		CriticalItem critical_item = new CriticalItem();
		critical_item.setId(rs.getInt("id"));
		critical_item.setExpertise(rs.getString("expertise"));
		critical_item.setName(rs.getString("name"));
		critical_item.setDescription(rs.getString("description"));
		return critical_item;
	}

}
