package nl.avans.glassy.Controllers;

import java.util.Arrays;

import nl.avans.glassy.R;
import nl.avans.glassy.Controllers.GCM.GoogleServicesActivity;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Utils.DrawableFromUrlCreator;
import nl.avans.glassy.Views.AccountFunctiesFragment;
import nl.avans.glassy.Views.AccountFunctiesFragment.ToggleFunctiesManager;
import nl.avans.glassy.Views.AuthFragment;
import nl.avans.glassy.Views.AuthFragment.AuthManager;
import nl.avans.glassy.Views.PostAuthFragment;
import nl.avans.glassy.Views.PostAuthFragment.AccountLinkManager;
import nl.avans.glassy.Views.ProfielBewerkenFragment;
import nl.avans.glassy.Views.ProfielBewerkenFragment.ProfielBewerkingManager;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public abstract class AccountFunctieActivity extends GoogleServicesActivity implements ToggleFunctiesManager, AuthManager, AccountLinkManager, ProfielBewerkingManager {

	private StatusCallback fssc = new FacebookSessionStatusCallback();
	private SharedPreferenceStalker sps = new SharedPreferenceStalker();
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		
		super.onCreate(savedInstance);
		getApplicationContext().getSharedPreferences("GLASSY", 0).registerOnSharedPreferenceChangeListener(sps);
	}
	
	@Override
	public void profielWijzigen() {

		SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);
		
		try {
			
			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));

			gebruiker.put("voornaam", ((EditText) findViewById(R.id.profiel_voornaam)).getText().toString());
			gebruiker.put("tussenvoegsel", ((EditText) findViewById(R.id.profiel_tussenvoegsel)).getText().toString());
			gebruiker.put("achternaam", ((EditText) findViewById(R.id.profiel_achternaam)).getText().toString());
			
			account.put("gebruiker", gebruiker.toString());
			
			SharedPreferences.Editor edit = preferences.edit();
			edit.putString("ACCOUNT", account.toString());
			edit.commit();
			
			Gebruiker.profielWijzigen(getApplicationContext());
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void avatarWijzigen() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void toggleBuddyGegevens(boolean zichtbaar) {
		
		if(zichtbaar) {
			
			findViewById(R.id.buddy_contactemail).setVisibility(View.VISIBLE);
			findViewById(R.id.buddy_contacttel).setVisibility(View.VISIBLE);
			
		} else {

			findViewById(R.id.buddy_contactemail).setVisibility(View.GONE);
			findViewById(R.id.buddy_contacttel).setVisibility(View.GONE);
		}
	}

	@Override
	public void gaNaarProfiel() {

		AccountFunctiesFragment.getInstance().veranderFragment(new ProfielBewerkenFragment());
	}

	@Override
	public void gaNaarMijnWijk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gaNaarInstellingen() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void uitloggen() {
				
		try {
			
			SharedPreferences sp = getApplicationContext().getSharedPreferences("GLASSY", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("ACCOUNT", null); 
			
			editor.commit();
			
			Log.i("uitloggen", sp.getString("ACCOUNT", "niks"));
			
		} catch(Exception e) {
			
			e.printStackTrace(); // log it
		}

		AccountFunctiesFragment.getInstance().setIngelogd(false);
		AccountFunctiesFragment.getInstance().veranderFragment(new AuthFragment());
	}

	@Override
	public void logRegSwap() {
		
		String action = ((Button) findViewById(R.id.logRegSwitch)).getText().toString();

		if(action.equals(getResources().getString(R.string.registreer))) {
			
			findViewById(R.id.password_repeat).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.logRegSwitch)).setText(R.string.login);
			((Button) findViewById(R.id.executeAuth)).setText(R.string.registreer);
			
		} else {
			
			findViewById(R.id.password_repeat).setVisibility(View.GONE);
			((Button) findViewById(R.id.logRegSwitch)).setText(R.string.registreer);
			((Button) findViewById(R.id.executeAuth)).setText(R.string.login);
		}
	}

	@Override
	public void auth() {
		
		String action = ((Button) findViewById(R.id.executeAuth)).getText().toString();
		
		if(action.equals(getResources().getString(R.string.registreer))) {
			
			if(!((EditText) findViewById(R.id.password)).getText().toString().equals(((EditText) findViewById(R.id.password_repeat)).getText().toString())) {
				
				// TODO melding naar gebruiker!
				return;
			}
			
			Gebruiker.register(
					getApplicationContext(),
					((EditText) findViewById(R.id.email)).getText().toString(), 
					((EditText) findViewById(R.id.password)).getText().toString()
				);
			
		} else {
			
			Gebruiker.login(
				getApplicationContext(),
				((EditText) findViewById(R.id.email)).getText().toString(), 
				((EditText) findViewById(R.id.password)).getText().toString()
			);
		}
	}

	@Override
	public void toggleFuncties() {

		View view = findViewById(R.id.functies);
		
		if(view.getVisibility() == View.GONE) {
			
			view.setVisibility(View.VISIBLE);
			
		} else {

			view.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void showFAQ() {
		
		Intent intent = new Intent(this, FaqActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Facebook login functions
	 * @author Mathijs van den Worm
	 */	
	private class FacebookSessionStatusCallback implements StatusCallback {

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
						
						Log.i("facebook user", user.toString());
						
						Gebruiker.facebookLogin(getApplicationContext(), user);
					}
					
				}).executeAsync();
			}
		}
	}
	
	protected void initFacebookLogin(Bundle savedInstanceState) {
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		
		LoginButton facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);
		facebookLogin.setReadPermissions(Arrays.asList("basic_info", "email", "user_photos", "user_videos"));

		Session session = Session.getActiveSession();
		if (session == null) {

			if (savedInstanceState != null) {

				session = Session.restoreSession(this, null, fssc,
						savedInstanceState);
			}
			
			if (session == null) {

				session = new Session(this);
			}

			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {

				session.openForRead(new Session.OpenRequest(this)
						.setCallback(fssc));
			}
		}
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		
		if(Session.getActiveSession() != null) {
			
			Session.getActiveSession().addCallback(fssc);
		}
	}

	@Override
	public void onStop() {

		super.onStop();
		
		if(Session.getActiveSession() != null) {
			
			Session.getActiveSession().removeCallback(fssc);
		}
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
	
	/**
	 * Account stalker 
	 */
	private class SharedPreferenceStalker implements OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {

			Log.i("sharedpreferencestalker", "change! " + key);

			String imageSource = "";
			
			if(key.equals("ACCOUNT")) {
				
				String account = sharedPreferences.getString("ACCOUNT", null);
				Log.i("ACCOUNT", account);
				
				if(account != null) {
					
					try {
						
						JSONObject accountAsJson = new JSONObject(account);						
						JSONObject gebruiker = new JSONObject(accountAsJson.getString("gebruiker"));
					
						String gebruikersnaam = "";	
						imageSource = Gebruiker.getStringUitGebruikerJson(accountAsJson, "foto_link");

						
						if(gebruiker.getString("voornaam") != null && !gebruiker.getString("voornaam").equals("null")) {
							
							gebruikersnaam += gebruiker.getString("voornaam") + " ";
						}
						
						if(gebruiker.getString("tussenvoegsel") != null && !gebruiker.getString("tussenvoegsel").equals("null")) {

							gebruikersnaam += gebruiker.getString("tussenvoegsel") + " ";
						}

						if(gebruiker.getString("achternaam") != null && !gebruiker.getString("achternaam").equals("null")) {

							gebruikersnaam += gebruiker.getString("achternaam");
						}

						if(gebruikersnaam.equals("")) {
							
							updateAccount(true, getResources().getString(R.string.anonieme_gebruiker), imageSource);
							
						} else {
							
							updateAccount(true, gebruikersnaam, imageSource);
						}
						
					} catch(Exception e) {
						
						e.printStackTrace();
						// TODO melding naar gebruiker!
					}
					
				} else {
					
					updateAccount(false, getResources().getString(R.string.anonieme_gebruiker), imageSource);
				}
			}
		}		
	}	

	protected void updateAccount(boolean ingelogd, String name, String imageSource) {
		
		((TextView) findViewById(R.id.gebruikersnaam)).setText(name);
		
		if(imageSource == null || imageSource.equals("") || imageSource.equals("null")) {
		
			((ImageView) findViewById(R.id.gebruikersfoto)).setImageResource(R.drawable.default_avatar);
			
		} else {
			
			new DrawableFromUrlCreator(){
				
				@Override
				protected void onPostExecute(Drawable image) {
					
					((ImageView) findViewById(R.id.gebruikersfoto)).setImageDrawable(image);
				}
			}.execute(imageSource);
		}
		
		if(ingelogd) {
			
			findViewById(R.id.functies).setVisibility(View.GONE);
	
			AccountFunctiesFragment.getInstance().veranderFragment(new PostAuthFragment());
			AccountFunctiesFragment.getInstance().setIngelogd(true);
		}
	}
}
