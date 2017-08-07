package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.glassfish.jersey.message.internal.MsgTraceEvent;

import sun.misc.BASE64Decoder;

import com.mysql.jdbc.Statement;

public class ReportModel {

	public int addReport(int userId ,int reportType ,String info)
	{
		int photo_id=0;
		try {	
			String sql="Insert into reports (userId,reportType ,info) VALUES  (?,?,?)";
			Connection conn =DBConnection.getActiveConnection();
			PreparedStatement stmt ;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userId);
			stmt.setInt(2, reportType);
			stmt.setString(3, info);
			int status = stmt.executeUpdate();
			
			sql="select last_insert_id() as last_id from reports";
			stmt =conn.prepareStatement(sql);
			ResultSet resultSet= stmt.executeQuery();
			while(resultSet.next())
			{
				 photo_id =resultSet.getInt("last_id");
				
			}
			conn.close();
			stmt.close();
			return photo_id ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0 ;
	}
	
	public void addMatches (int searchId , int foundId)
	{
		try {	
			String sql="Insert into matches (searchID,foundID) VALUES  (?,?)";
			
			Connection conn =DBConnection.getActiveConnection();
			PreparedStatement stmt ;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, searchId);
			stmt.setInt(2, foundId);
			int status = stmt.executeUpdate();
			conn.close();
			stmt.close();
			//return status ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//add report
	}
	public String getImgType (int imgId)
	{
	try {	
			String sql="SELECT reportType FROM reports where photoId="+imgId;
			int reportType=0;
			Connection conn =DBConnection.getActiveConnection();
			PreparedStatement stmt ;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet= stmt.executeQuery();
			while(resultSet.next())
			{
				 reportType =resultSet.getInt("reportType");
			}
			conn.close();
			stmt.close();
			if(reportType==1)
			{
				return "missing";
			}
			else if(reportType ==2)
			{
				return "found";
			}
			else 
			{
				return "";
			}
				
			//return status ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return "";
	}
	public ArrayList<Item> getMatcheReport (int userID) throws IOException
	{
		try {	
			String sql="SELECT * FROM correctmatch where userid ="+userID;
			
			Connection conn =DBConnection.getActiveConnection();
			PreparedStatement stmt ;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet= stmt.executeQuery();
			ArrayList<Item>userReports=new ArrayList<Item>();
			while(resultSet.next())
			{
				String info=resultSet.getString("info");
				int searchID=resultSet.getInt("searchID");
				int matchID=resultSet.getInt("matchID");
				String searchIdType=getImgType(searchID);
				String matchIdType=getImgType(matchID);
				//---- read the image from the file
				BufferedImage img1 = null;
				BufferedImage img2 = null;
				img1 = ImageIO.read(new File("E:/FCI/"+searchIdType+"/"+searchID+".png"));
				img2 = ImageIO.read(new File("E:/FCI/"+matchIdType+"/"+matchID+".png"));
				System.out.print("E:/FCI/"+searchIdType+"/"+searchID+".png");
				System.out.print("E:/FCI/"+matchIdType+"/"+matchID+".png");
				//String s = img1.toString();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( img1, "png", baos );
				baos.flush();
				byte[] imageInByte1 = baos.toByteArray();
				baos.close();
				
				ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
				ImageIO.write( img2, "png", baos2 );
				baos.flush();
				byte[] imageInByte2 = baos2.toByteArray();
				baos2.close();
				
				String imgString1=Base64.getEncoder().encodeToString(imageInByte1);
				String imgString2=Base64.getEncoder().encodeToString(imageInByte2);
				Item item=new Item(info, imgString1, imgString2);
				userReports.add(item);
				
			}
			conn.close();
			stmt.close();
			return userReports;
			//return status ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
