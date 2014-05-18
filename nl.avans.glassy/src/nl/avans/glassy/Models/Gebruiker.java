package nl.avans.glassy.Models;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Gebruiker {
	
	private static String API_CONTROLLER = "account/";
	
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
	
	public static void profielWijzigen(Context context) {
		
		SharedPreferences preferences = context.getSharedPreferences("GLASSY", 0);
		JSONObject account = null;
		String[] params = { "EMPTY" };
		
		try {
			
			account = new JSONObject(preferences.getString("ACCOUNT", null));
			
			params = new String[]{
				"PUT",
				API_CONTROLLER,
				"{ _token:" + preferences.getString("token", null) + ", gebruiker: " + account.getString("gebruiker") + "}"
			};
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		new ApiCommunicator(context) {
			
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

}
