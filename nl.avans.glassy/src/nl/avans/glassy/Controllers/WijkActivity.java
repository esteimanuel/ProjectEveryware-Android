package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Interfaces.PagerAdapter;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Threads.ActieManager;
import nl.avans.glassy.Views.WijkDetailsFragment.OnSpecialButtonPressListener;
import nl.avans.glassy.Views.WijkFaqFragment.wijkFaqListener;
import nl.avans.glassy.Views.WijkGoededoelenFragment.wijkgoededoelenListener;
import nl.avans.glassy.Views.WijkMapFragment.webClientListener;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WijkActivity extends AccountFunctieActivity implements
		webClientListener, wijkgoededoelenListener, wijkFaqListener, OnSpecialButtonPressListener {
	private ActieManager mActieManager;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wijkcollection_activity);

		findViewById(R.id.functies).setVisibility(View.GONE);

		mActieManager = ActieManager.getInstance();
		mActieManager.startInitialization(this);

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */

	// Implementations of the ontouchlistener from wijkMapFragment
	@Override
	public void onTouchMap(String URL) {
		//TODO FIX
		Intent myIntent = new Intent(this, DetailMapActivity.class);
		myIntent.putExtra("url", URL);
		this.startActivity(myIntent);
	}
	@Override
	public void onTouchGoededoelen(String infofull) {
		Intent myIntent = new Intent(this, DetailGoededoelenActivity.class);
		myIntent.putExtra("info", infofull);
		this.startActivity(myIntent);
	}

	public PagerAdapter getViewPagerAdapter() {
		return mPagerAdapter;
	}
	
	@Override
	public void onTouchFaq() {
		Intent myIntent = new Intent(this, FaqActivity.class);
		this.startActivity(myIntent);
		
	}

	@Override
	public void volgendeActieStapUitvoeren() {

		WijkFragment huidigeWijk = ((WijkFragment) mPagerAdapter.getItem(mPager.getCurrentItem()));

		SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);

		try {

			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));

			if(gebruiker.getString("actie_id") != null) {

				Gebruiker.aanmeldenBijWijk(getApplicationContext(), account.getString("token"), Integer.toString(huidigeWijk.getWijkId()));

			} else if( gebruiker.getBoolean("borg_betaald") != true) {

//				gebruiker.BetaalBorg();

			} else if( gebruiker.getInt("pakket_id") < 0) {

//				gebruiker.KiesProviderPakket();
			}

		} catch(NullPointerException nullpointer) {

			// TODO aanmelden
			nullpointer.printStackTrace();

		} catch(Exception e) {

			e.printStackTrace();
		}
	}
}
