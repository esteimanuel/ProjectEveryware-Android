package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AuthFragment extends Fragment {

	private AuthManager manager;
	
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		if(activity instanceof AuthManager) {
			
			manager = (AuthManager) activity;
			
		} else {
			
			throw new ClassCastException();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.fragment_auth, container, false);
		
		view.findViewById(R.id.executeAuth)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.auth();
				}
			});
	
		view.findViewById(R.id.logRegSwitch)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	
					manager.logRegSwap();
				}
			});
				
		return view;
	}

	public interface AuthManager {
		
		public void logRegSwap();
		
		public void auth();
	}
	
}
