package nl.avans.glassy.Controllers.GCM;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class GoogleServicesActivity extends FragmentActivity {

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if(checkPlayServices()) {
			
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
			String regid = getRegistrationId(getApplicationContext());
		}
	}
	
	protected void onResume() {
		
		super.onResume();
		checkPlayServices();
	}
	
	private boolean checkPlayServices() {
		
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if(resultCode != ConnectionResult.SUCCESS) {
			
			if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
				
			} else {
				
				Log.i("GOOGLE PLAY SERVICES", "This device is not supported.");
				finish();
			}
			
			return false;
		}
		
		return true;
	}
	
	private String getRegistrationId(Context context) {
		
		final SharedPreferences prefs = context.getSharedPreferences("GLASSY", Context.MODE_PRIVATE);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		
		if(registrationId.isEmpty()) {
			
			return "";
		}
		
		return registrationId;		
	}

}
