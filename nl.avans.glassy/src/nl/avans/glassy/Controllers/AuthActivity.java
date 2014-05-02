package nl.avans.glassy.Controllers;

import java.util.Arrays;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Gebruiker;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
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

public class AuthActivity extends FragmentActivity {

	private Session.StatusCallback callback = new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Gebruiker.login("meep@meep.nl", "meep");
		Gebruiker.register("meep@meep.nl", "meep");

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

		Button joinButton = (Button) findViewById(R.id.skip);
		joinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AuthActivity.this, WijkActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart() {

		super.onStart();
		Session.getActiveSession().addCallback(callback);
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

}
