package nl.avans.glassy.Models;

import java.util.ArrayList;

import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Views.WijkMapFragment.webClientListener;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Faq {

	private static String API_CONTROLLER = "faq";
	
	public Faq()
	{
		
	}
	
	public static void loadFaq(Context context, Activity activity) {

		final faqListener myListener;		
		try {		
			myListener = (faqListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BoardListListener");
		}
		
		String[] params = {
				"GET",
				API_CONTROLLER
		};


		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {
				
					JSONArray faqarray = result.getJSONArray("entries");
					ArrayList<String> questions = new ArrayList<String>();
					ArrayList<String> answers = new ArrayList<String>();
					
					for(int i=0;i<faqarray.length(); i++){
						JSONObject faqinfo = faqarray.getJSONObject(i); 
	                    questions.add(faqinfo.getString("question"));
	                    answers.add(faqinfo.getString("answer"));	                    	                    
	                }
					
					myListener.onFaqLoaded(questions, answers);

				} catch(Exception e) {

					e.printStackTrace(); // log it
				}
			}		}.execute(params);
	}
	
	public interface faqListener {
		public void onFaqLoaded(ArrayList<String> questions, ArrayList<String> answers);
	}
}
