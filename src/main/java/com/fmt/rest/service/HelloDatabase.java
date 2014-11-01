package com.fmt.rest.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

 //see: http://www.ibm.com/developerworks/web/library/wa-aj-tomcat/index.html
//java.lang.ClassNotFoundException: com.sun.jersey.spi.container.servlet.ServletContainer
//java.lang.ClassNotFoundException: com.sun.jersey.spi.container.servlet.ServletContainer
@Path("/db")
public class HelloDatabase {
	  private Connection connect = null;
	  private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	  
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String dbQuery() {
		String foo= "unfound";
		try {
			final String dbForName= "org.postgresql.Driver";
			final String dbPrefix= "postgres";
			Class.forName(dbForName);

			final String username= System.getenv().get("DBUSERNAME");
			final String password= System.getenv().get("DBPASSWORD");
			final String port= System.getenv().get("DBPORT");
			final String url= System.getenv().get("DBURL");
			final String dbUrl= System.getenv().get("DATABASE_URL");
			final String dbName= System.getenv().get("DBNAME");


			// Setup the connection with the DB
			connection= DriverManager.getConnection(String.format("jdbc:%s", dbUrl));

			  // Statements allow to issue SQL queries to the database
			  statement = connect.createStatement();
			  // Result set get the result of the SQL query
			  resultSet = statement
			      .executeQuery("select * from keyvalue");
			  
			  //writeResultSet(resultSet);
			  while (resultSet.next()) {
				  foo= resultSet.getString("name");
			  }
			  resultSet.close();
			  statement.close();
			  connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			foo= e.getMessage();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			foo= e.getMessage();
		}
		return foo;
	}
}   
