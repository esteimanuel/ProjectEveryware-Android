package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Deelnemer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class BuddyProfielActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddyprofiel_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		Bundle b = getIntent().getExtras();
		Deelnemer buddy = b.getParcelable("nl.avans.glassy.Models.Deelnemer");
		Log.d("BuddyProfielActivity",
				buddy.getVoornaam() + " " + buddy.getAchternaam());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
