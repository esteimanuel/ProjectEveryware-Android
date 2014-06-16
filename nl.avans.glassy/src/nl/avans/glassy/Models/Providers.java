package nl.avans.glassy.Models;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class Providers {

	public static void updateProviders(Context context) {
		
		String[] params = {
				"GET",
				"provider"
		};
		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {

					SharedPreferences sp = getContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();

					editor.putString("PROVIDERS", result.toString());

					editor.commit();

				} catch(Exception e) {

					e.printStackTrace();
				}
			}

		}.execute(params);
		
	}

}
