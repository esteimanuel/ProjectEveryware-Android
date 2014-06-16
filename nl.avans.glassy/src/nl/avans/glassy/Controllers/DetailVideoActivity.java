package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Locks;
import nl.avans.glassy.Views.DetailGoededoelenFragment;
import nl.avans.glassy.Views.DetailVideoFragment;
import nl.avans.glassy.Views.FaqFragment;
import nl.avans.glassy.Views.WijkDeelnemersFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DetailVideoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailvideo_activity);
		Intent intent = getIntent();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new DetailVideoFragment()).commit();
		}
		setVideoId();
	}

	private void setVideoId() {		
		Intent intent = getIntent();
		DetailVideoFragment fragment = (DetailVideoFragment) getSupportFragmentManager().findFragmentById(R.id.detailvideofragment);
		fragment.setVideo(intent.getStringExtra("url"));
	}
	

}
