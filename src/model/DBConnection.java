package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection getActiveConnection (){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			//Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/face_hub","root","root");
			//Connection con =DriverManager.getConnection("jdbc:mysql://127.9.145.2:3306/facehub","adminMH5qECT","41Tyj6kaqzjd");
			Connection con= DriverManager.getConnection("jdbc:mysql://127.9.145.2:3306/facehub?user=adminMH5qECT&password=41Tyj6kaqzjd"); 

			return con ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
}
