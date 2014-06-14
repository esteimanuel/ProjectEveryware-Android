package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Utils.DrawableFromUrlCreator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class ProfielBewerkenFragment extends Fragment {
	
	private ProfielBewerkingManager manager;
	private ContactTijdenManager tijdmanager;
	
	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		
		if(activity instanceof ProfielBewerkingManager) {
			
			manager = (ProfielBewerkingManager) activity;
			
		} else {
			
			throw new ClassCastException();
		}
		
		if(activity instanceof ContactTijdenManager) {
			
			tijdmanager = (ContactTijdenManager) activity;
			
		} else {
			
			throw new ClassCastException();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.fragment_profielbewerken, container, false);
				
		view.findViewById(R.id.profiel_wijzigen).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.profielWijzigen();					
				}
			});
			
		view.findViewById(R.id.profiel_foto).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
		
					manager.avatarWijzigen();					
				}
			});
		
		((Switch) view.findViewById(R.id.buddy_functies)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			 			
	 			@Override
	 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	 				
					manager.toggleBuddyGegevens(isChecked);
				}
		 	});
		
		view.findViewById(R.id.contacttijd_van).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				tijdmanager.setContacttijdVan(v);				
			}
		});

		view.findViewById(R.id.contacttijd_tot).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				tijdmanager.setContacttijdTot(v);				
			}
		});
		
		veldenInvullen(view);
		
		return view;
	}
	
	private void veldenInvullen(View view) {
		
		SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("GLASSY", 0);
		
		try {
			
			JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
			
			final ImageButton foto_button = ((ImageButton) view.findViewById(R.id.profiel_foto));
			 			
 			new DrawableFromUrlCreator(){
 				
 				@Override
 				protected void onPostExecute(Drawable image) {
 					
 					if(image != null) foto_button.setImageDrawable(image);
 				}
 			}.execute(Gebruiker.getStringUitGebruikerJson(account, "foto_link"));
		
			// naam gegevens invullen
			if(gebruiker.getString("voornaam") != null && !gebruiker.getString("voornaam").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_voornaam)).setText(gebruiker.getString("voornaam"));			
			}

			if(gebruiker.getString("tussenvoegsel") != null && !gebruiker.getString("tussenvoegsel").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_tussenvoegsel)).setText(gebruiker.getString("tussenvoegsel"));			
			}
			
			if(gebruiker.getString("achternaam") != null && !gebruiker.getString("achternaam").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_achternaam)).setText(gebruiker.getString("achternaam"));			
			}
			
			// locatie gegevens invullen
			if(gebruiker.getString("huisnummer") != null && !gebruiker.getString("huisnummer").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_huisnummer)).setText(gebruiker.getString("huisnummer"));			
			}

			if(gebruiker.getString("huisnummer_toevoeging") != null && !gebruiker.getString("huisnummer_toevoeging").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_huisnummer)).setText(gebruiker.getString("huisnummer"));			
			}
			
			postcodeInvullen(view);
			buddyVeldenInvullen(view);

		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public interface ProfielBewerkingManager {
		
		public void profielWijzigen();
		
		public void avatarWijzigen();
		
		public void toggleBuddyGegevens(boolean zichtbaar);
	}
	
	public interface ContactTijdenManager {
		
		public void setContacttijdVan(View v);
		
		public void setContacttijdTot(View v);
	}
	
	// logic hier is lelijk maar heb geen beter oplossing...
	private void buddyVeldenInvullen(View view) throws JSONException {
		
		final View finalized = view;
		SharedPreferences preferences = getActivity().getSharedPreferences("GLASSY", 0);
		JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
		JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
		
		String[] params = new String[] {
			"GET",
			"buddy/getByGebruikerId?gebruiker_id=" + gebruiker.getString("gebruiker_id")
		};
		
		new ApiCommunicator(null) {
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
				if(result == null) return; // don't even bother
				
				try {
					
					((TextView) finalized.findViewById(R.id.buddy_contacttel)).setText(result.getString("contact_tel"));
					((TextView) finalized.findViewById(R.id.buddy_contactmail)).setText(result.getString("contact_email"));
					((TextView) finalized.findViewById(R.id.contacttijd_tot_tijd)).setText(result.getString("tijd_tot"));
					((TextView) finalized.findViewById(R.id.contacttijd_van_tijd)).setText(result.getString("tijd_vanaf"));					
					
				} catch(Exception e) { }
			}
			
		}.execute(params); 	
		
	}
	
	private void postcodeInvullen(View view) throws JSONException {
		
		final View finalized = view;
		SharedPreferences preferences = getActivity().getSharedPreferences("GLASSY", 0);
		JSONObject account = new JSONObject(preferences.getString("ACCOUNT", null));
		JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
		
		String[] params = new String[] {
			"GET",
			"postcode?id=" + gebruiker.getString("postcode_id")
		};
		
		new ApiCommunicator(null) {
			
			@Override
			protected void onPostExecute(JSONObject result) {
				
				if(result == null) return; // don't even bother
				
				try {
					
					((EditText) finalized.findViewById(R.id.profiel_postcode)).setText(result.getString("postcode"));				
					
				} catch(Exception e) { }
			}
			
		}.execute(params); 	
		
	}
}
