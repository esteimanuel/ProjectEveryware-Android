package nl.avans.glassy.Controllers;

import java.util.Arrays;

import nl.avans.glassy.R;
import nl.avans.glassy.Views.GebruikerAccountFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class WijkActivity extends FragmentActivity implements GebruikerAccountFragment.ToggleFunctionsOnClick {
	
	static Handler handler = new  Handler() {
		@Override
		public void handleMessage(Message msg) {
			// get the bundle and extract data by key
			Bundle b = msg.getData();
		}
	};
	private Session.StatusCallback callback = new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wijkcollection_activity);

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		
		initFacebookLogin(savedInstanceState);
		initApiLogin();
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
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 5;

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
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new WijkFragment();
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Do async task for wijk objects
		// create a new thread
		Thread background = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < NUM_PAGES; i++) {
					try {
						Message msg = new Message();
						Bundle b = new Bundle();
						b.putString("My Key", "My Value: " + String.valueOf(i));
						msg.setData(b);
						// send message to the handler with the current message
						// handler
						handler.sendMessage(msg);
					} catch (Exception e) {
						Log.v("Error", e.toString());
					}
				}
			}
		});

		background.start();
		
		// adding facebook auth callback
		Session.getActiveSession().addCallback(callback);
	}
	
	// -- auth dingen
	@Override
	public void toggleFunctions() {
		
		View toToggle = findViewById(R.id.account_functies);
		
		if(toToggle.getVisibility() == View.GONE) {
			
			toToggle.setVisibility(View.VISIBLE);
			
		} else {
			
			toToggle.setVisibility(View.GONE);
		}
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			Log.i("SessionStatusCallback", session.toString());
			Log.i("SessionStatusCallback", state.toString());

			if (exception != null) {

				exception.printStackTrace();
			}
			
			if (state.toString().equals("OPENED")) {
				
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						
						Log.i("GraphUser", user.toString());
					}
					
				}).executeAsync();
			}
		}

	}

	@Override
	public void onStop() {

		super.onStop();
		Session.getActiveSession().removeCallback(callback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
	
	private void initFacebookLogin(Bundle savedInstanceState) {
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		LoginButton facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);
		facebookLogin.setReadPermissions(Arrays.asList("basic_info", "email", "user_photos", "user_videos"));

		Session session = Session.getActiveSession();
		if (session == null) {

			if (savedInstanceState != null) {

				session = Session.restoreSession(this, null, callback,
						savedInstanceState);
			}
			
			if (session == null) {

				session = new Session(this);
			}

			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

				session.openForRead(new Session.OpenRequest(this)
						.setCallback(callback));
			}
		}
	}
	
	private void initApiLogin() {
		
		Button registerButton = (Button) findViewById(R.id.register);
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

//				findViewById(R.id.skip).setVisibility(View.VISIBLE);
				
			}
		});
		
		Button loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

//				findViewById(R.id.skip).setVisibility(View.VISIBLE);
				
			}
		});
	}

}
