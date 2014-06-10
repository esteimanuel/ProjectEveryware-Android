package nl.avans.glassy.Threads;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;

public class GoedeDoelen {

	//TODO api goededoelen
	private static String API_CONTROLLER = "goededoel";
	
	public static void loadGoededoelen(Context context, Object activity, int actieId) {

		final goededoelenListener myListener;		
		try {		
			myListener = (goededoelenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement goededoelenListener");
		}
		//TODO wat als er geen goededoel is.
		String[] params = {
				"GET",
				API_CONTROLLER + "?id=" + actieId
		};


		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {
				    //TODO status eruit, die word ergens anders ingeladen.
					String title = result.getString("title");
					String description = result.getString("description");
					String message = result.getString("message");
					myListener.onGoededoelenLoaded(title ,description, message);

				} catch(Exception e) {

					e.printStackTrace(); // log it
				}
			}		}.execute(params);
	}
	
	public interface goededoelenListener {
		public void onGoededoelenLoaded(String title, String description, String message);
	}
}
