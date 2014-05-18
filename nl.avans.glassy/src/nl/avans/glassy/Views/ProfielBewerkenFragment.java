package nl.avans.glassy.Views;

import nl.avans.glassy.R;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class ProfielBewerkenFragment extends Fragment {
	
	private ProfielBewerkingManager manager;
	
	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		
		if(activity instanceof ProfielBewerkingManager) {
			
			manager = (ProfielBewerkingManager) activity;
			
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
		
		veldenInvullen(view);
		
		return view;
	}
	
	private void veldenInvullen(View view) {
		
		SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("GLASSY", 0);
		
		try {
			
			JSONObject account = new JSONObject(sp.getString("ACCOUNT", null));
			JSONObject gebruiker = new JSONObject(account.getString("gebruiker"));
		
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
			JSONObject postcode = new JSONObject(gebruiker.getString("postcode"));
			
			if(postcode.getString("postcode") != null && !postcode.getString("postcode").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_postcode)).setText(postcode.getString("postcode"));			
			}
			
			if(postcode.getString("plaats") != null && !postcode.getString("plaats").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_stad)).setText(postcode.getString("plaats"));			
			}
		
			if(postcode.getString("straat") != null && !postcode.getString("straat").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_straat)).setText(postcode.getString("straat"));			
			}

			if(gebruiker.getString("huisnummer") != null && !gebruiker.getString("huisnummer").equals("null")) {
				
				((EditText) view.findViewById(R.id.profiel_huisnummer)).setText(gebruiker.getString("huisnummer"));			
			}

		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public interface ProfielBewerkingManager {
		
		public void profielWijzigen();
		
		public void avatarWijzigen();
	}
}
