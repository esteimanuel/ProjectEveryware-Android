package nl.avans.glassy.Models;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.util.Log;

public class Gebruiker {
	
	private static String API_CONTROLLER = "account/";
	
	private String email;
	private int facebookid;
	private String firstname;
	private String lastname;
	private String zipcode; // post code
	private String city;
	private String streetname;
	private int streetnumber;
	
	private Gebruiker(String email) {
		
		this.email = email;
	}
	
	private Gebruiker(String email, int facebookid) {
		
		this(email);
		this.facebookid = facebookid;
	}
	
	// properties
	public String Email() {
		
		return email;
	}
	
	public int Facebookid() {
		
		return this.facebookid;
	}
	
	public String Firstname(String firstname) {
		
		if(firstname != null) {
			
			this.firstname = firstname;
		}
		
		return this.firstname;
	}
	
	public String Lastname(String lastname) {
		
		if(lastname != null) {
			
			this.lastname = lastname;
		}
		
		return this.lastname;
	}
	
	public String Zipcode(String zipcode) {
		
		if(zipcode != null) {
			
			this.zipcode = zipcode;
		}
		
		return this.zipcode;
	}
	
	public String City(String city) {
		
		return this.city;
	}
	
	public String Streetname(String streetname) {
		
		return this.streetname;
	}
	
	public int Streetnumber(int streetnumber) {
		
		return this.streetnumber;
	}
	
	public String Street() {
		
		return this.streetname + " " + this.streetnumber;
	}
	
	// api fuctions
	public static void login(String email, String password) {
		
		String[] params = {
				"GET",
				API_CONTROLLER + "login?email="+ email +"&wachtwoord=" + password
		};
		
		new ApiCommunicator(){
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
//				Log.i("user.login", result.toString());
			}
			
		}.execute(params);
	}
	
	public static void register(String email, String password) {
		
		String[] params = {
				"GET",
				API_CONTROLLER + "register",
				"{email:"+ email +", wachtwoord:"+ password +"}"
		};
		
		new ApiCommunicator(){
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
				Log.i("user.register", result.toString());
			}
			
		}.execute(params);
	}
	
	public static void facebookLogin(int facebookid) {
		
		
	}

}
