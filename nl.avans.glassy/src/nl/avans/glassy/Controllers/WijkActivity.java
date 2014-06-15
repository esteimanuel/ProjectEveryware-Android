package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Interfaces.PagerAdapter;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Models.Providers;
import nl.avans.glassy.Threads.ActieManager;
import nl.avans.glassy.Views.ProfielBewerkenFragment;
import nl.avans.glassy.Views.WijkDetailsFragment.OnSpecialButtonPressListener;
import nl.avans.glassy.Views.WijkFaqFragment.wijkFaqListener;
import nl.avans.glassy.Views.WijkGoededoelenFragment.wijkgoededoelenListener;
import nl.avans.glassy.Views.WijkMapFragment.webClientListener;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

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
		
		mPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {

				super.onPageSelected(position);
				WijkFragment wf = (WijkFragment) mPagerAdapter.getItem(position);
				wf.evalActieButton();
				wf.evalWijkNaam();
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
	protected void onStart() {
		
		super.onStart();
		Providers.updateProviders(getApplicationContext());
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
	public void onTouchGoededoelen(String title, String description, String message) {
		Intent myIntent = new Intent(this, DetailGoededoelenActivity.class);
		myIntent.putExtra("title", title);
		myIntent.putExtra("description", description);
		myIntent.putExtra("message", message);
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

		Log.d("volgende actie", "click");
		
		try {

			JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
			
			String token = account.getString("token");
			if(!Gebruiker.zitInActie(getApplicationContext())) {

				doeAanmelden(huidigeWijk, token);

			} else if(!Gebruiker.heeftBetaald(getApplicationContext())) {
				
				doeBetalingStap(huidigeWijk, token);
	
			} else if(!Gebruiker.heeftProviderGekozen(getApplicationContext())) {
				
				doeProviderKiezenStap(huidigeWijk, token);
			}

		} catch(NullPointerException nullpointer) {

			Builder dialog = new AlertDialog.Builder(this)
			   .setTitle("Inloggen");		
			
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			final View view = inflater.inflate(R.layout.dialog_login, null);
			
			dialog = dialog.setMessage("Je bent nog niet ingelogd! Om deel te kunnen nemen aan een wijk moet je ingelogd zijn.")
			   		  .setView(view)
			   		  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
				@Override
				public void onClick(DialogInterface iDialog, int which) {
					
					Gebruiker.login(
							getApplicationContext(), 
							((EditText) view.findViewById(R.id.dialog_email)).getText().toString(), 
							((EditText) view.findViewById(R.id.dialog_wachtwoord)).getText().toString()
						);
				}
			});
			
			dialog.show();
			
			nullpointer.printStackTrace();

		} catch(Exception e) {

			e.printStackTrace();
		}
	}
	
	private void doeAanmelden(WijkFragment huidigewijk, String token) {
		
		Gebruiker.aanmeldenBijWijk(getApplicationContext(), token, huidigewijk);
		return;
	}
	
	private void doeBetalingStap(WijkFragment huidigewijk, String token) {
		
		final String token_finallized = token;
		final WijkFragment wijk_finallized = huidigewijk;
		
		boolean gegevensNodig = !Gebruiker.heeftGegevensIngevuld(getApplicationContext());

		Builder dialog = new AlertDialog.Builder(this)
					   .setTitle(R.string.inschrijven);							   
		
		if(gegevensNodig) {
			
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.dialog_info, null);
			
			final View finalizedView = ProfielBewerkenFragment.dialogInvullen(getApplicationContext(), view);	
			
			dialog = dialog.setMessage(getResources().getString(R.string.extra_info_vereist))
				   		  .setView(finalizedView)
				   		  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					try {
						
						SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);
						JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
						JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
						
						for(Integer key : dialogProfielPairs.keySet()) {
							
							String value = ((EditText) finalizedView.findViewById(key)).getText().toString();
							
							if(!value.isEmpty()) gebruiker.put(dialogProfielPairs.get(key), value);
						}
						
						account.put("gebruiker", gebruiker.toString());
						
						SharedPreferences.Editor edit = preferences.edit();
						edit.putString("ACCOUNT", account.toString());
						edit.commit();
												
						Gebruiker.profielWijzigen(getApplicationContext());
						
						String postcode = ((EditText) finalizedView.findViewById(R.id.dialog_postcode)).getText().toString();			
						if(!postcode.isEmpty()) Gebruiker.postcodeWijzigen(getApplicationContext(), postcode.toUpperCase());
						
					} catch(Exception e) { e.printStackTrace(); }
					
					return;
				}
			});
			   						
		} else {

			  dialog = dialog.setMessage(R.string.inschrijven_uitleg)
					  		 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Gebruiker.betaalBorg(getApplicationContext(), token_finallized);
						wijk_finallized.evalActieButton();
						return;
					}
				});
		}
		
		AlertDialog alert = dialog.create();
		alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alert.show();
	}
	
	/** 
	 * dialog voor het afhandelen van het kiezen van een provider
	 * @param huidigewijk
	 * @param token
	 */
	private void doeProviderKiezenStap(WijkFragment huidigewijk, String token) {

		final String token_finallized = token;
		final WijkFragment wijk_finallized = huidigewijk;
		
		String[] providers = new String[] {};
		JSONArray providersJsonArray = new JSONArray();
		
		try {
			
			SharedPreferences preferences = getApplicationContext().getSharedPreferences("GLASSY", 0);
			JSONObject providersJson = new JSONObject(preferences.getString("PROVIDERS", null));
			providersJsonArray = providersJson.getJSONArray("entries");

			providers = new String[providersJsonArray.length()];
			
			for(int i = 0; i < providersJsonArray.length(); i++) {
				
				providers[i] = providersJsonArray.getJSONObject(i).getString("naam");
			}
			
		} catch(Exception e) { e.printStackTrace(); }
		
		final String[] finalizedProviders = providers;
		final JSONArray finalizedJsonArray = providersJsonArray;
		
		new AlertDialog.Builder(this)
		   .setTitle("Provider kiezen")
		   .setItems(finalizedProviders, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					
					SharedPreferences sp = getApplicationContext().getSharedPreferences("GLASSY", 0);
					SharedPreferences.Editor editor = sp.edit();
	
					JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
					JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
					
					gebruiker.put("provider_id", finalizedJsonArray.getJSONObject(which).getInt("provider_id"));
					account.put("gebruiker", gebruiker.toString());
	
					editor.putString("ACCOUNT", account.toString());
	
					editor.commit();
					
					Gebruiker.profielWijzigen(getApplicationContext());
				
				} catch(Exception e) { e.printStackTrace();	}
				
				wijk_finallized.evalActieButton();
				return;
			}
		}).show();
	}


	@Override
	public void gaNaarMijnWijk() {
		
		int pagerItems = mPagerAdapter.getCount();
		
		int wijkIndex = -1;
		for(int i = 0; i < pagerItems; i++) {
			
			WijkFragment wf = (WijkFragment) mPagerAdapter.getItem(i);
			if(Gebruiker.zitInWelkeActie(getApplicationContext()) == wf.getActieId()) {
				
				wijkIndex = i;
				break;
			}
		}
		
		if(wijkIndex != -1) {
			
			mPager.setCurrentItem(wijkIndex);
			
		} else {
			
			new AlertDialog.Builder(this)
						   .setTitle("Ooops")
						   .setMessage("Je bent nog niet aangemeld bij een wijkactie!")
						   .show();
		}
	}
	
	@Override 
	protected void evalHuidigeWijk() {
		
		WijkFragment huidigeWijk = ((WijkFragment) mPagerAdapter.getItem(mPager.getCurrentItem()));
		huidigeWijk.evalActieButton();
		huidigeWijk.evalWijkNaam();
	}
}
