package nl.avans.glassy.Threads;

import java.util.ArrayList;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class Faq {

	private static String API_CONTROLLER = "faq";

	public Faq()
	{	
	}

	public static void loadFaq(Context context, Object activity) {

		final faqListener myListener;		
		try {		
			myListener = (faqListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement faqListener");
		}

		String[] params = {
				"GET",
				API_CONTROLLER 
		};		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {

					ArrayList<String> questions = new ArrayList<String>();
					ArrayList<String> answers = new ArrayList<String>();
					if(result != null && result.length() != 0)
					{
						JSONArray faqarray = result.getJSONArray("entries");
						for(int i=0;i<faqarray.length(); i++){
							JSONObject faqinfo = faqarray.getJSONObject(i); 
							questions.add(faqinfo.getString("question"));
							answers.add(faqinfo.getString("answer"));	                    	                    
						}
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
