package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PostAuthFragment extends Fragment {

	private AccountLinkManager manager;
	
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		
		if(activity instanceof AccountLinkManager) {
			
			manager = (AccountLinkManager) activity;
			
		} else {
			
			throw new ClassCastException();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.fragment_postauthfuncties, container, false);
				
		view.findViewById(R.id.profiel_link)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.gaNaarProfiel();					
				}
			});
		
		view.findViewById(R.id.mijn_wijk_link)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.gaNaarMijnWijk();					
				}
			});
		
		view.findViewById(R.id.instellingen_link)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.gaNaarInstellingen();					
				}
			});
		
		return view;
	}
	
	public interface AccountLinkManager {
		
		public void gaNaarProfiel();
		
		public void gaNaarMijnWijk();
		
		public void gaNaarInstellingen();
	}
	
}
