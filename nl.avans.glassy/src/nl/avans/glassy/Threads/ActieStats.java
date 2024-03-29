package nl.avans.glassy.Threads;

import nl.avans.glassy.Utils.ApiCommunicator;

import org.json.JSONObject;

import android.content.Context;

public class ActieStats {

	private static String API_CONTROLLER = "actie/stats";
	
	public ActieStats()
	{
		
	}
	
	public static void loadStats(Context context, Object activity, int actieId) {

		final actieStatsListener myListener;		
		try {		
			myListener = (actieStatsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement actieStatsListener");
		}
		
		String[] params = {
				"GET",
				API_CONTROLLER + "?id=" + actieId
		};
		
		new ApiCommunicator(context){

			@Override
			protected void onPostExecute(JSONObject result) {

				try {
					if(result != null && result.length() != 0)
					{
					int participants = result.getInt("participants");
					int houses = result.getInt("houses");
					int target = result.getInt("target");
					int totalPartPerc = result.getInt("totalPartPerc");
					int targetPartPerc = result.getInt("targetPartPerc");
					int paidTargetPerc = result.getInt("paidTargetPerc");
					//TODO opletten! "providerSelecPerc" stond zo in database, typ fout
					int providerSelectPerc = result.getInt("providerSelecPerc");
					int goedeDoelPartPerc = result.getInt("goedeDoelPartPerc");
					myListener.onActieStatsLoaded(participants, houses, target, totalPartPerc, targetPartPerc, paidTargetPerc, providerSelectPerc, goedeDoelPartPerc);
					} else
					{
						myListener.onActieStatsLoaded(0, 0, 0, 0, 0, 0, 0, 0);
					}

					
				} catch(Exception e) {

					e.printStackTrace(); // log it
				}
			}		}.execute(params);
	}
	
	public interface actieStatsListener {
		public void onActieStatsLoaded(int participants, int houses, int target, int totalPartPerc, int targetPartPerc, int paidTargetPerc, int providerSelectPerc, int goedeDoelPartPerc);
	}
}
