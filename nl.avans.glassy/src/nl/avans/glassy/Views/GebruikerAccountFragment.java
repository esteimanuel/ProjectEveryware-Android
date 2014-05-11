package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class GebruikerAccountFragment extends Fragment implements OnClickListener {
	
	private ToggleFunctionsOnClick listener;
	
	public interface ToggleFunctionsOnClick {
		
		public void toggleFunctions();
	}
				
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.account_fragment, container, false);
		view.findViewById(R.id.accountfuncties).setOnClickListener(this);		
				
		return view;
	}

	@Override
	public void onClick(View v) {
		
		listener.toggleFunctions();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if(activity instanceof ToggleFunctionsOnClick) {
			
			listener = (ToggleFunctionsOnClick) activity;
			
		} else {
			
			throw new ClassCastException(activity.toString() + " should implement GebruikerAccountFragment.ToggleFunctionsOnClick");
		}
	}
	
}
