package nl.avans.glassy.Controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Views.AccountFunctiesFragment;
import nl.avans.glassy.Views.AccountFunctiesFragment.ToggleFunctiesManager;
import nl.avans.glassy.Views.AuthFragment;
import nl.avans.glassy.Views.AuthFragment.AuthManager;
import nl.avans.glassy.Views.PostAuthFragment;
import nl.avans.glassy.Views.PostAuthFragment.AccountLinkManager;
import nl.avans.glassy.Views.ProfielBewerkenFragment;
import nl.avans.glassy.Views.ProfielBewerkenFragment.ContactTijdenManager;
import nl.avans.glassy.Views.ProfielBewerkenFragment.ProfielBewerkingManager;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public abstract class AccountFunctieActivity extends FragmentActivity implements ContactTijdenManager, ToggleFunctiesManager, AuthManager, AccountLinkManager, ProfielBewerkingManager {

	private StatusCallback fssc = new FacebookSessionStatusCallback();
	private SharedPreferenceStalker sps = new SharedPreferenceStalker();
	private Boolean toggled = false;

	private Map<Integer, String> basicProfielPairs = new HashMap<Integer, String>() { 
		{
			put(R.id.profiel_voornaam, "voornaam");
			put(R.id.profiel_tussenvoegsel, "tussenvoegsel");
			put(R.id.profiel_achternaam, "achternaam");
			put(R.id.profiel_huisnummer, "huisnummer");
			put(R.id.profiel_nummertoevoeging, "huisnummer_toevoeging");
		}; 
	};

	final public static Map<Integer, String> dialogProfielPairs = new HashMap<Integer, String>() { 
		{
			put(R.id.dialog_voornaam, "voornaam");
			put(R.id.dialog_tussenvoegsels, "tussenvoegsel");
			put(R.id.dialog_achternaam, "achternaam");
			put(R.id.dialog_huisnummer, "huisnummer");
			put(R.id.dialog_nummertoevoeging, "huisnummer_toevoeging");
		}; 
	};

	@Override
	protected void onCreate(Bundle savedInstance) {

		super.onCreate(savedInstance);
		getApplicationContext().getSharedPreferences("GLASSY", 0).registerOnSharedPreferenceChangeListener(sps);
	}

	@Override
	public void setContacttijdVan(View v) {

		final View theView = v;
		final String[] tijd = ((TextView) findViewById(R.id.contacttijd_van_tijd)).getText().toString().split(":");

		new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				String tijd = "" + hourOfDay;
				tijd += ":" + String.format("%02d", minute) + ":00";

				((TextView) theView.findViewById(R.id.contacttijd_van_tijd)).setText(tijd);
			}
		}, Integer.parseInt(tijd[0]), Integer.parseInt(tijd[1]), true).show();

	}

	@Override
	public void setContacttijdTot(View v) {

		final View theView = v;
		final String[] tijd = ((TextView) findViewById(R.id.contacttijd_tot_tijd)).getText().toString().split(":");

		new TimePickerDialog(this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				String tijd = "" + hourOfDay;
				tijd += ":" + String.format("%02d", minute) + ":00";

				((TextView) theView.findViewById(R.id.contacttijd_tot_tijd)).setText(tijd);
			}
		}, Integer.parseInt(tijd[0]), Integer.parseInt(tijd[1]), true).show();
	}

	@Override
	public void profielWijzigen() {

		SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);

		try {

			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));

			for(Integer key : basicProfielPairs.keySet()) {

				String value = ((EditText) findViewById(key)).getText().toString();

				if(!value.isEmpty()) gebruiker.put(basicProfielPairs.get(key), value);
			}
			account.put("gebruiker", gebruiker.toString());

			SharedPreferences.Editor edit = preferences.edit();
			edit.putString("ACCOUNT", account.toString());
			edit.commit();

			Gebruiker.profielWijzigen(getApplicationContext());

			String postcode = ((EditText) findViewById(R.id.profiel_postcode)).getText().toString();			
			if(!postcode.isEmpty()) Gebruiker.postcodeWijzigen(getApplicationContext(), postcode.toUpperCase());

			buddyGegevensWijzigen(preferences, ((Switch) findViewById(R.id.buddy_functies)).isChecked());

		} catch(Exception e) {

			e.printStackTrace();
		}
	}

	private void buddyGegevensWijzigen(SharedPreferences preferences, boolean isBuddy) {

		if(!isBuddy) {

			// buddy gegevens verwijderen
			return;
		}

		try {

			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));

			if(gebruiker.has("buddy")) {

				//TODO update buddy gegevens
				return;

			} else {

				//TODO nieuwe buddy aanmaken
			}

		} catch(Exception e) { e.printStackTrace(); }		

	}

	@Override
	public void avatarWijzigen() {
		// TODO Auto-generated method stub
		// doen we niet meer...
	}

	@Override
	public void gaNaarProfiel() {

		AccountFunctiesFragment.getInstance().veranderFragment(new ProfielBewerkenFragment());

	}

	@Override 
	public void uitloggen() {

		try {

			SharedPreferences sp = getApplicationContext().getSharedPreferences("GLASSY", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("ACCOUNT", null); 

			editor.commit();

			facebookLogout();

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
		ImageButton button = (ImageButton) findViewById(R.id.user_util);
		if(view.getVisibility() == View.GONE) {
			toggled = true;
			button.setImageDrawable(getResources().getDrawable( R.drawable.ic_action_cancel));			
			view.setVisibility(View.VISIBLE);

		} else {
			toggled = false;
			button.setImageDrawable(getResources().getDrawable( R.drawable.ic_action_search));
			view.setVisibility(View.GONE);
		}
	}

	@Override
	public void toggleBuddyGegevens(boolean zichtbaar) {

		if(zichtbaar) {

			findViewById(R.id.buddy_contacttel).setVisibility(View.VISIBLE);
			findViewById(R.id.buddy_contactmail).setVisibility(View.VISIBLE);
			findViewById(R.id.contacttijden).setVisibility(View.VISIBLE);

		} else {

			findViewById(R.id.buddy_contacttel).setVisibility(View.GONE);
			findViewById(R.id.buddy_contactmail).setVisibility(View.GONE);
			findViewById(R.id.contacttijden).setVisibility(View.GONE);
		}
	}

	@Override
	public void search() {
		if(toggled)
		{
			toggleFuncties();
		} else
		{
			inputRequestDialog();
		}
	}

	private void findWijk(String value1) {
		String value = value1.replace(" ", "");
		if(value.length() == 6 && Character.isDigit(value.charAt(0)))
		//TODO
		{
			//postcode 
		}
		else
		{
			//naam
		}
					
	}

	private void inputRequestDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Zoek een wijk");
		alert.setMessage("vul een postcode of naam in");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				findWijk(value);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
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

		try {

			SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);

			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));

			SharedPreferences.Editor edit = preferences.edit();
			edit.putString("ACCOUNT", account.toString());
			edit.commit();

		} catch(Exception e) {	}

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

			if(key.equals("ACCOUNT")) {

				String account = sharedPreferences.getString(key, null);

				if(account != null) {

					try {

						JSONObject accountAsJson = new JSONObject(account);						
						JSONObject gebruiker = new JSONObject(accountAsJson.getString("gebruiker"));
						Log.i("lege naam", gebruiker.getString("voornaam").getClass().toString());

						String gebruikersnaam = "";

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

							updateAccount(getResources().getString(R.string.anonieme_gebruiker));

						} else {

							updateAccount(gebruikersnaam);
						}

					} catch(Exception e) {

						e.printStackTrace();
					}
				}

				evalHuidigeWijk();
			}
		}		
	}	

	protected void updateAccount(String name) {

		((TextView) findViewById(R.id.gebruikersnaam)).setText(name);
		findViewById(R.id.functies).setVisibility(View.GONE);

		AccountFunctiesFragment.getInstance().veranderFragment(new PostAuthFragment());
		AccountFunctiesFragment.getInstance().setIngelogd(true);
	}

	private void facebookLogout() {

		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
	}

	protected abstract void evalHuidigeWijk();
}
