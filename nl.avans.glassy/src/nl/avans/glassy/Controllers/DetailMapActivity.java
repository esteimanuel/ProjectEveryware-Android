package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Views.DetailMapFragment;
import android.app.Activity;
import android.os.Bundle;

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
