package nl.avans.glassy.Utils;

import java.util.Timer;
import java.util.TimerTask;

import nl.avans.glassy.Threads.ActieManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation {
	/*
	 * An object that contains the ThreadPool singleton.
	 */
	private static ActieManager sActieManager;

	// Constants for indicating the state of the download
	static final int LOCATION_FAILED = -1;
	static final int LOCATION_OBTAINED = 0;
	
	LocationManager lm;
	Location locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	public MyLocation() {
		sActieManager = ActieManager.getInstance();
	}

	public boolean getLocation(Context context) {
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			return false;
		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		if (gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				/* do what you need to do */
				GetLastLocation();
			}
		};

		ActieManager.mHandler.postDelayed(runnable, 5000);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationResult = location;
			handleState(LOCATION_OBTAINED);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationResult = location;
			handleState(LOCATION_OBTAINED);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void GetLastLocation() {
		lm.removeUpdates(locationListenerGps);
		lm.removeUpdates(locationListenerNetwork);

		Location net_loc = null, gps_loc = null;
		if (gps_enabled)
			gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (network_enabled)
			net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// if there are both values use the latest one
		if (gps_loc != null && net_loc != null) {
			if (gps_loc.getTime() > net_loc.getTime()) {
				locationResult = gps_loc;
				handleState(LOCATION_OBTAINED);
			} else {
				locationResult = net_loc;
				handleState(LOCATION_OBTAINED);
			}
			return;
		} else if (gps_loc != null) {
			locationResult = gps_loc;
			handleState(LOCATION_OBTAINED);
			return;
		} else if (net_loc != null) {
			locationResult = net_loc;
			handleState(LOCATION_OBTAINED);
			return;
		}
		locationResult = null;
		handleState(LOCATION_FAILED);
	}

	// Delegates handling the current state of the task to the PhotoManager
	// object
	private void handleState(int state) {
		if (sActieManager != null) {
			Log.d("ActieManager", "MyLocation handleState called");

			// Passes the state to the ThreadPool object.
			sActieManager.handleLocationResult(locationResult, state);
			sActieManager = null;
		}
	}
}