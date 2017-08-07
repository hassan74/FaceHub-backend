package services;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Item;
import model.ReportModel;
import model.UserModel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.Base64.Decoder;

import org.glassfish.jersey.server.*;

import sun.misc.BASE64Decoder;

@Path("/")
public class Services {
	//1 missing 2 found
	@POST 
	@Path("/missing")
	@Produces(MediaType.TEXT_PLAIN)
	public String report_missing(@FormParam("img") String img ,@FormParam ("id")String user_id) throws IOException
	{
		int userId=Integer.parseInt(user_id);
//----- insert report in reports table (photoID,userID ,reportType)
		ReportModel reportModel = new ReportModel();
		int photoId= reportModel.addReport(userId ,1 ,"");
		if(photoId!=0)
		{
//---save photo in missing folders and--------------------------
			BufferedImage image = null;
			BASE64Decoder decoder = new BASE64Decoder();
			byte [] imageByte=decoder.decodeBuffer(img);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
//---- write the image to a file
			File outputfile = new File("E:/FCI/missing/"+photoId+".png");
			ImageIO.write(image, "png", outputfile);
			/*
			Process p = Runtime.getRuntime().exec("python E:/workspace/test1.py "+photoId+" "+1);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int[] ret = new Integer(in.readLine()).intValue();
			for(int i=0 ;i<ret.size ;i++)
			{
			addMatch(photoId ,ret[i]);
			}
			
			*/
//--------------- run python code ----------------------

//----------------- read result from file & Add match in table matches --------------
		Scanner scanner = new Scanner(new File("E:/FCI/result.txt"));
		while (scanner.hasNextInt())
		{
			int matchID=scanner.nextInt();
			//System.out.println(matchID);
			reportModel.addMatches(photoId, matchID);
		}

		JSONObject json = new JSONObject();
		json.put("status", "true");
		return json.toJSONString() ;
		}
		else
		{
		JSONObject json = new JSONObject();
		json.put("status", "false");
		return json.toJSONString() ;
		}
	}
	@POST 
	@Path("/found")
	@Produces(MediaType.TEXT_PLAIN)
	public String report_found(@FormParam("img") String img ,@FormParam ("id")String user_id ,@FormParam("info") String info) throws IOException
	{
		int userId=Integer.parseInt(user_id);
//---- insert report in reports table (photoID,userID ,reportType)
		ReportModel reportModel = new ReportModel();
		int photoId= reportModel.addReport(userId ,2 ,info);
		if(photoId!=0)
		{
//---- save photo in missing folders and--------------------------
			BufferedImage image = null;
			BASE64Decoder decoder = new BASE64Decoder();
			byte [] imageByte=decoder.decodeBuffer(img);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
//---- write the image to a file
			File outputfile = new File("E:/FCI/found/"+photoId+".png");
			ImageIO.write(image, "png", outputfile);
			/*
			Process p = Runtime.getRuntime().exec("python E:/workspace/test1.py "+photoId+" "+1);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int[] ret = new Integer(in.readLine()).intValue();
			for(int i=0 ;i<ret.size ;i++)
			{
			addMatch(photoId ,ret[i]);
			}
			
			*/
//--------------- run python code ----------------------
			

//----------------- read result from file & Add match in table matches --------------
			Scanner scanner = new Scanner(new File("E:/FCI/result.txt"));
			while (scanner.hasNextInt())
			{
				int matchID=scanner.nextInt();
				System.out.println(matchID);
				reportModel.addMatches(photoId, matchID);
			}
						
			JSONObject json = new JSONObject();
			json.put("status", "true");
			return json.toJSONString();
		}
	    JSONObject json = new JSONObject();
		json.put("status", "false");
		return json.toJSONString() ;

	}
	@POST 
	@Path("/getInfo")
	@Produces(MediaType.TEXT_PLAIN)
	public String getInfo(@FormParam ("id")String user_id) throws IOException
	{
		
		int userId=Integer.parseInt(user_id);
		ReportModel reportModel =new ReportModel();
		ArrayList<Item>userReports=reportModel.getMatcheReport(userId);
	    
		JSONArray jsonArray=new JSONArray();
		for(Item i :userReports)
		{
			JSONObject json = new JSONObject();
			json.put("img1", i.getImg1());
			json.put("img2", i.getImg2());
			json.put("info",i.getInfo());
			jsonArray.add(json);
			
		}
		System.out.println(jsonArray.toJSONString());
		return jsonArray.toJSONString();


	}
	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,
			@FormParam("email") String email, 
			@FormParam("pass") String pass ,
			@FormParam("phone")String phoneNumber,
			@FormParam("address")String address) {
			//@Consumes("application/x-www-form-urlencoded")
 		UserModel user =new UserModel();
		int status=user.addNewUser(name, email, pass ,phoneNumber,address);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("status", status);
 		return json.toString();
		
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email, @FormParam("pass") String pass) {
		UserModel user =new UserModel();
		int id =user.login(email, pass);
	    JSONObject json=new JSONObject();
		if(id!=0)
		{
		json.put("id", id);
		json.put("email", email);
		json.put("pass",pass);
		json.put("status", "1");
		}
		else
		{
			json.put("status", "0");
			json.put("id", "");
			json.put("email", "");
			json.put("pass","");
		}
		return json.toString();
		//return json.toJSONString();
		
	}
	@GET
	@Path("/test")
	
	@Produces(MediaType.TEXT_HTML)
	public String test() {
		//UserModel user =new UserModel();
		//user.addNewUser(name, email, pass);
		//JSONObject json = new JSONObject();
		//json.put("id", user.getId());
//		json.put("name", user.getName());
//		json.put("email", user.getEmail());
//		json.put("pass", user.getPass());
		return "<h1>Hello </h1>";
		//return json.toJSONString();
		
	}

}
 