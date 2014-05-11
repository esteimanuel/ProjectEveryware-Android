package nl.avans.glassy.Controllers;

import java.util.Arrays;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Views.GebruikerAccountFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public abstract class AuthActivity extends FragmentActivity implements GebruikerAccountFragment.ToggleFunctionsOnClick {

	private Session.StatusCallback callback = new SessionStatusCallback();

	@Override
	public void toggleFunctions() {
		
		View toToggle = findViewById(R.id.preAuthFuncties);
		
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

			if (exception != null) {

				exception.printStackTrace();
			}
			
			if (state.toString().equals("OPENED")) {
				
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser user, Response response) {
						
						SharedPreferences sp = getApplicationContext().getSharedPreferences("GLASSY", 0);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("ACCOUNT", user.toString());
						
						editor.commit();
					}
					
				}).executeAsync();
			}
		}

	}
	
	@Override
	protected void onStart() {
		
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
	
	protected void initFacebookLogin(Bundle savedInstanceState) {
		
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
	
	protected void initApiLogin() {
		
		Button switchButton = (Button) findViewById(R.id.logRegSwitch);
		switchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Button source = (Button) v;
				String sourceText = source.getText().toString();
				if(sourceText.toLowerCase().equals("register")) {

					source.setText(R.string.login);

					((Button) findViewById(R.id.executeAuth)).setText(R.string.register);
					findViewById(R.id.password_repeat).setVisibility(View.VISIBLE);
					
				} else {

					source.setText(R.string.register);

					((Button) findViewById(R.id.executeAuth)).setText(R.string.login);
					findViewById(R.id.password_repeat).setVisibility(View.GONE);
				}
			}
		});
		
		Button loginButton = (Button) findViewById(R.id.executeAuth);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Button source = (Button) v;
				String sourceText = source.getText().toString();
				
				if(sourceText.toLowerCase().equals("log in")) {
					
					Gebruiker.login(
						getApplicationContext(),
						((EditText) findViewById(R.id.email)).getText().toString(), 
						((EditText) findViewById(R.id.password)).getText().toString()
					);
					
				} else {

					EditText email = (EditText) findViewById(R.id.email);
					EditText password = (EditText) findViewById(R.id.password);
					EditText password_repeat = (EditText) findViewById(R.id.password_repeat);
					
					if(!password.getText().toString().equals(password_repeat.getText().toString())) {
						
						// TODO melding geven aan gebruiker
					}
					
					Gebruiker.register(
						getApplicationContext(),
						email.getText().toString(), 
						password.getText().toString()
					);
				}			
			}
		});
	}
	
}
