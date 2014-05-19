package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Views.DetailGoededoelenFragment;
import nl.avans.glassy.Views.DetailMapFragment;
import nl.avans.glassy.Views.FaqFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DetailGoededoelenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailgoededoelen_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new DetailGoededoelenFragment()).commit();
		}
	}
	
	public void setText()
	{
		Intent intent = getIntent();
		DetailGoededoelenFragment fragment = (DetailGoededoelenFragment) getFragmentManager().findFragmentById(R.id.detailGoededoelenfragment);
		fragment.setText(intent.getStringExtra("info"));
	}
}
