package net.sourceforge.smokestack.jdbc.ex02;

import java.sql.*;

public class PSBatch{
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement pst = null;
		try{
	        Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:mem:inmemory");
	        Statement stat = conn.createStatement();
	        stat.execute("create table message(id long primary key, message varchar(255))");
			stat.close();
			conn.setAutoCommit(false);
			String sql = "INSERT into Message VALUES(?,?)";
			pst = conn.prepareStatement(sql);
			pst.setLong(1,3);
			pst.setString(2,"Message 3");
			pst.addBatch();
			pst.setLong(1,4);
			pst.setString(2,"Message 4");
			pst.addBatch();
			int count[] = pst.executeBatch();
			conn.commit();
		}
		finally{
			pst.close();
			conn.close();
		}
	}
}
