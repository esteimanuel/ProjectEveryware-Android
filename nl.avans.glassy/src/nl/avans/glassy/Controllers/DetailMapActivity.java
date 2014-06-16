package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Locks;
import nl.avans.glassy.Views.DetailGoededoelenFragment;
import nl.avans.glassy.Views.DetailMapFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DetailMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailmap_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new DetailMapFragment()).commit();
		}
		loadMap();
	}

	private void loadMap() {
		Intent intent = getIntent();
		DetailMapFragment fragment = (DetailMapFragment) getFragmentManager().findFragmentById(R.id.detailmapfragment);
		fragment.loadMap(intent.getStringExtra("url"));
	}
	
	@Override
	public void onStop () {
		Locks.mapfull = false;
		super.onStop();
	}
}
