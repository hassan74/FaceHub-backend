package services;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import model.UserModel;

import org.apache.catalina.mbeans.UserMBean;

@Path("/")
public class Services {
	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,
			@FormParam("email") String email, @FormParam("pass") String pass ,@FormParam("phone")String phoneNumber,
			@FormParam("address")String address) {
		UserModel user =new UserModel();
		int status=user.addNewUser(name, email, pass ,phoneNumber,address);
		JSONObject json = new JSONObject();
		//json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("status", status);
		return json.toString();
		//return json.toJSONString();
		
	}
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email, @FormParam("pass") String pass) {
		UserModel user =new UserModel();
		int id =user.login(email, pass);
		JSONObject json=new JSONObject();
		json.put("id", id);
		json.put("email", email);
		json.put("pass",pass);
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
