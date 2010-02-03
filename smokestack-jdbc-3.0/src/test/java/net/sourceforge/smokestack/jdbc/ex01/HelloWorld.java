/*
 * Copyright 2004-2010 H2 Group. Multiple-Licensed under the H2 License, Version
 * 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html). Initial Developer: H2 Group
 */
package net.sourceforge.smokestack.jdbc.ex01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A very simple class that shows how to load the driver, create a database,
 * create a table, and insert some data.
 */
public class HelloWorld {

    /**
     * Called when ran from command line.
     *
     * @param args ignored
     */
    public static void main(String... args) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:inmemory");
        Statement stat = conn.createStatement();

        // this line would initialize the database
        // from the SQL script file 'init.sql'
        // stat.execute("runscript from 'init.sql'");

        stat.execute("create table message(id int primary key, message varchar(255))");
        stat.execute("insert into message values(1, 'Hello World')");
        ResultSet rs;
        rs = stat.executeQuery("select * from message");
        while (rs.next()) {
            System.out.println(rs.getString("message"));
        }
        rs.close();
        stat.close();
        conn.close();
    }

}
