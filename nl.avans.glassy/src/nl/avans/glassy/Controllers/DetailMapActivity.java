package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.R.id;
import nl.avans.glassy.R.layout;
import nl.avans.glassy.R.menu;
import nl.avans.glassy.Views.DetailMapFragment;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class DetailMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailmap_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new DetailMapFragment()).commit();
		}
	}
}
