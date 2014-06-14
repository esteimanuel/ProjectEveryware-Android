package nl.avans.glassy.Models;

import nl.avans.glassy.Controllers.WijkFragment;
import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.model.GraphUser;

public class Gebruiker {

	private static String API_CONTROLLER = "account/";

	// api fuctions
	public static void login(Context context, String email, String password) {

		String[] params = {
				"GET",
				API_CONTROLLER + "login?email="+ email +"&wachtwoord=" + password
		};

		Log.i("URL", params[1]);
		
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

		Log.i("URL", params[1]);
		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {
					
					Log.i("RESULT", result.toString());

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

	public static void facebookLogin(Context context, GraphUser user) {

		String[] params = {
			"GET",
			API_CONTROLLER + "loginFacebook?fid=" + user.getId() + "&femail=" + user.getProperty("email").toString()
		};

		Log.i("URL", params[1]);

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

	public static void profielWijzigen(Context context) {

		SharedPreferences preferences = context.getSharedPreferences("GLASSY", 0);
		JSONObject account = null;
		String[] params = { "EMPTY" };

		try {

			account = new JSONObject(preferences.getString("ACCOUNT", null));

			params = new String[]{
				"PUT",
				"gebruiker",
				"{ _token:" + account.getString("token") + ", _gebruiker: " + account.getString("gebruiker") + "}"
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

					JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
					account.put("gebruiker", result.get("model").toString());

					editor.putString("ACCOUNT", account.toString());

					editor.commit();

				} catch(Exception e) {

					e.printStackTrace();
				}
			}

		}.execute(params);
	}

	public static void postcodeWijzigen(Context context, String postcode) {
		
		String[] params = {
			"GET",
			"postcode/findWithPostcode?postcode=" + postcode
		};		
		
		Log.d("link", params[1]);
		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {
				
				try {
					
					Log.d("postcode gevonden", result.toString());

					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();

					JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
					JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
					gebruiker.put("postcode_id", result.getString("postcode_id"));
					account.put("gebruiker", gebruiker.toString());

					editor.putString("ACCOUNT", account.toString());

					editor.commit();
					
					Gebruiker.profielWijzigen(getContext());

				} catch(Exception e) {

					e.printStackTrace();
				}
			}

		}.execute(params);		
	}

	public static void aanmeldenBijWijk(Context context, String token, WijkFragment wijk) {

		String[] params = {
				"PUT",
				"gebruiker",
				"{ _token:" + token + ", actie_id:" + wijk.getActieId() + "}"
		};
		
		final WijkFragment ditislelijk = wijk;

		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {

					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();

					JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
					account.put("gebruiker", result.get("model").toString());

					editor.putString("ACCOUNT", account.toString());

					editor.commit();
					
					ditislelijk.evalActieButton();
					ditislelijk.evalWijkNaam();

				} catch(Exception e) {

					e.printStackTrace();
				}
			}

		}.execute(params);
	}
	
	public static void betaalBorg(Context context, String token) {
		
		String[] params = {
				"PUT",
				"gebruiker",
				"{ _token:" + token + ", borg_betaald:" + true + "}"
		};
		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {

					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();

					JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
					account.put("gebruiker", result.get("model").toString());

					editor.putString("ACCOUNT", account.toString());

					editor.commit();

				} catch(Exception e) {

					e.printStackTrace();
				}
			}

		}.execute(params);
	}

	public static String getStringUitGebruikerJson(JSONObject gebruiker, String key) {

		try {

			return gebruiker.getString(key);

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public static Boolean zitInActie(Context context) {
		
		Boolean retval = false;
		
		try {
			
			JSONObject gebruiker = getGebruikerUitContext(context);
			
			if(!gebruiker.getString("actie_id").equals("null")) {
				
				retval = true;
			}
		
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return retval;
	}
	
	public static int zitInWelkeActie(Context context) {
		
		int retval = -2;
		
		try {

			JSONObject gebruiker = getGebruikerUitContext(context);
			
			if(!gebruiker.getString("actie_id").equals("null")) {
				
				retval = gebruiker.getInt("actie_id");
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return retval;		
	}
	
	public static boolean heeftGegevensIngevuld(Context context) {
		
		String[] gegevens = {"voornaam", "achternaam", "postcode_id"};
		
		boolean retval = true;
		
		try {
			
			JSONObject gebruiker = getGebruikerUitContext(context);

			for(String gegeven : gegevens) {	

				retval = !(gebruiker.getString(gegeven) == null);
				
				if(!retval) break;
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			retval = false;
		}
		
		return retval;
	}
	
	public static boolean heeftBetaald(Context context) {
		
		Boolean retval = false;
		
		try {
			
			JSONObject gebruiker = getGebruikerUitContext(context);
			
			retval = gebruiker.getString("borg_betaald").toLowerCase().equals("true");
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		return retval;
	}
	
	private static JSONObject getGebruikerUitContext(Context context) throws JSONException {
		
		SharedPreferences preferences = context.getSharedPreferences("GLASSY", 0);
		JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
		return new JSONObject(account.getString("gebruiker"));
	}
	
	public static boolean heeftProviderGekozen(Context context) {
		
		return false;
	}
}