package nl.avans.glassy.Models;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
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
	public static void login(Context context, String email, String password) {
		
		String[] params = {
				"GET",
				API_CONTROLLER + "login?email="+ email +"&wachtwoord=" + password
		};
		
		new ApiCommunicator(context){
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
				try {
					
					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("ACCOUNT", result.get("account").toString()); // if account is null exception will be caught
					
					editor.commit();
					
				} catch(Exception e) {
					
					e.printStackTrace(); // log it
				}
			}
		}.execute(params);
	}
	
	public static void register(Context context, String email, String password) {
		
		String[] params = {
				"POST",
				API_CONTROLLER + "register",
				"{email:"+ email +", wachtwoord:"+ password +"}"
		};
		
		new ApiCommunicator(context){
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
				try {
					
					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("ACCOUNT", result.get("account").toString()); // if account is null exception will be caught
					
					editor.commit();
				
				} catch(Exception e) {
					
					e.printStackTrace();
				}
			}
			
		}.execute(params);
		
	}
	
	public static void facebookLogin(int facebookid) {
		
		// TODO facebook api validation
	}

}
