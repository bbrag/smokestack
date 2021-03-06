/**
 * 
 */
package com.github.smokestack.jdbc.ex04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author bbrag *
 */

public class DeleteOp{
	public static void main(String[] args) throws Exception {
		Connection conn = null;
		Statement stat = null;
		try{
			Class.forName("org.hsqldb.jdbcDriver" );
			conn = DriverManager.getConnection("jdbc:hsqldb:mem:inmemory");
	        stat = conn.createStatement();
	        stat.execute("create table message(id long primary key, message varchar(255))");
	        //stat = conn.createStatement(); //Nice. Our framework catches this error!
			String sql = "DELETE FROM message";
			int delete = stat.executeUpdate(sql);
			if(delete == 0){
				System.out.println("Deletion Successful!");
			}
		}
		catch (SQLException se){
			//do nothing
		}
		finally{
			if(stat != null){
				stat.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}
}
