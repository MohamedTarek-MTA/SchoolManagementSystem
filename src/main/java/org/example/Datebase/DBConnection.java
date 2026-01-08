package org.example.Datebase;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DBConnection {

    private static final String HOST_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_URL   = "jdbc:mysql://localhost:3306/SchoolDB";

    public static DataSource getBootstrapDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl(HOST_URL);
        ds.setUser("root");
        ds.setPassword("root");
        return ds;
    }

    public static DataSource getAppDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl(DB_URL);
        ds.setUser("root");
        ds.setPassword("root");
        return ds;
    }
}
