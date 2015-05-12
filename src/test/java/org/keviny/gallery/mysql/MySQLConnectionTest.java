package org.keviny.gallery.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;


public class MySQLConnectionTest {
	
	@Test
	public void testConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
		PreparedStatement stmt = conn.prepareStatement("SHOW databases");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}
		conn.close();
	}
}
