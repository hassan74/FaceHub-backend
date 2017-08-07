package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.mysql.jdbc.Statement;

public class UserModel {

	private String name;
	private String address ;
	private String email;
	private String pass;
	private int id;

	
	public String getPass(){
		return pass;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	
	public int addNewUser (String name ,String email ,String pass ,String phoneNumber ,String address)
	{
		try {	
			String sql="Insert into userdata (name,phoneNumber,address,email ,pass) VALUES  (?,?,?,?,?)";
			Connection conn =DBConnection.getActiveConnection();
			PreparedStatement stmt ;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, phoneNumber);
			stmt.setString(3, address);
			stmt.setString(4, email);
			stmt.setString(5, pass);
			int status = stmt.executeUpdate();
			conn.close();
			stmt.close();
			this.setEmail(email);
			this.setName(name);
			this.setPass(pass);
			return status ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0 ;
		
	}
	public int login (String email,String pass)
	{
		int id=0 ;
		String sql ="SELECT userID ,name FROM userdata where email='"+email+"' and pass='"+pass+"';";
		try {
			Connection connection =DBConnection.getActiveConnection();
			PreparedStatement preparedStatement =connection.prepareStatement(sql);
			ResultSet resultSet= preparedStatement.executeQuery();
			while(resultSet.next())
			{
				 id =resultSet.getInt("userID");
				 String name=resultSet.getString("name");
				
			}
			connection.close();
			preparedStatement.close();
			resultSet.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

}
