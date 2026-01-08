package org.example.Datebase;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DBConnection {
   public static DataSource getDataSource(){
       MysqlDataSource dataSource = new MysqlDataSource();
       dataSource.setUrl("jdbc:mysql://localhost:3306");
       dataSource.setUser("root");
       dataSource.setPassword("root");
       return dataSource;
   }
}
