package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.R.id;
import nl.avans.glassy.R.layout;
import nl.avans.glassy.R.menu;
import nl.avans.glassy.Views.DetailGoededoelenFragment;
import nl.avans.glassy.Views.DetailStappenFragment;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class DetailStappenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailstappen_activity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new DetailStappenFragment()).commit();
		}

		setText();
	}

	public void setText()
	{
		Intent intent = getIntent();
		DetailStappenFragment fragment = (DetailStappenFragment) getFragmentManager().findFragmentById(R.id.detailStappenfragment);
		fragment.setInfo(intent.getStringExtra("1"), intent.getStringExtra("2"), intent.getStringExtra("3"), intent.getStringExtra("4"), intent.getStringExtra("5"));
	}

	
}
