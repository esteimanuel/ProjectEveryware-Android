package nl.avans.glassy.Models;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;

public class GoedeDoelen {

	//TODO api goededoelen
	private static String API_CONTROLLER = "goededoelen";
	
	public static void loadGoededoelen(Context context, Object activity, int id) {

		final goededoelenListener myListener;		
		try {		
			myListener = (goededoelenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement goededoelenListener");
		}
		
		String[] params = {
				"GET",
				API_CONTROLLER
		};


		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {
				    //TODO goede api location en kijken welke data ik terug krijg
					String text = "Als 90% van de buurt mee doet dan krijgt de hele wijk 1 jaar gratis internet.";
					int status = 50;					
					myListener.onGoededoelenLoaded(text, status);

				} catch(Exception e) {

					e.printStackTrace(); // log it
				}
			}		}.execute(params);
	}
	
	public interface goededoelenListener {
		public void onGoededoelenLoaded(String goededoel, int status);
	}
}
