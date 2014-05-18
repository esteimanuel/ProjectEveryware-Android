package nl.avans.glassy.Controllers;

import java.util.ArrayList;
import java.util.List;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Actie;
import nl.avans.glassy.Threads.ActieManager;
import nl.avans.glassy.Views.WijkMapFragment.webClientListener;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WijkActivity extends AuthActivity implements webClientListener {

	private OnSharedPreferenceChangeListener spListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {

			if (key.equals("ACCOUNT")) {

				String account = sharedPreferences.getString(key, null);
				Log.i("SP ACCOUNT", account);

				if (account != null) {

					try {

						JSONObject accountAsJson = new JSONObject(account);

						updateAccount(accountAsJson.getString("token")); // them
																			// nested
																			// blocks...

					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wijkcollection_activity);

		getApplicationContext().getSharedPreferences("GLASSY", 0)
				.registerOnSharedPreferenceChangeListener(spListener);

		findViewById(R.id.preAuthFuncties).setVisibility(View.GONE);

		initFacebookLogin(savedInstanceState);
		initApiLogin();
		ActieManager.getInstance().init(this);

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
	private class PagerAdapter extends FragmentStatePagerAdapter {
		private List<WijkFragment> actieList;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			actieList = new ArrayList<WijkFragment>();
		}

		@Override
		public Fragment getItem(int position) {
			Log.d("stuff", Integer.toString(position));
			return actieList.get(position);
		}

		@Override
		public int getCount() {
			return actieList.size();
		}

		public void addFragmentToAdapter(WijkFragment theFragment) {
			actieList.add(theFragment);
			notifyDataSetChanged();
		}
	}

	protected void updateAccount(String name) {

		((TextView) findViewById(R.id.gebruikersnaam)).setText(name);
		findViewById(R.id.preAuthFuncties).setVisibility(View.GONE);
	}

	// Implementations of the ontouchlistener from wijkMapFragment
	@Override
	public void onTouchMap(String URL) {
		// TODO deze wordt 3 keer aangeroepen.
		Intent myIntent = new Intent(this, MapDetailActivity.class);
		myIntent.putExtra("url", URL);
		this.startActivity(myIntent);
	}

	public void addFragment(Actie actie) {
		WijkFragment tempFragment = new WijkFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("ActieObject", actie);
		tempFragment.setArguments(bundle);
		mPagerAdapter.addFragmentToAdapter(tempFragment);
	}
}
