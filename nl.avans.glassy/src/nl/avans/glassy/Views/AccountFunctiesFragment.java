package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AccountFunctiesFragment extends Fragment {
	
	private ToggleFunctiesManager toggleManager;
	private boolean ingelogd = false;
	private static AccountFunctiesFragment instance;

	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		if(activity instanceof ToggleFunctiesManager) {
			
			toggleManager = (ToggleFunctiesManager) activity;
			
		} else {
		
			throw new ClassCastException();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.fragment_accountfuncties, container, false);
		
		veranderFragment(new AuthFragment());
		
		view.findViewById(R.id.accountfuncties)
			.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				toggleManager.toggleFuncties();
			}
		});	
		
		view.findViewById(R.id.user_util)
			.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					toggleManager.showFAQ();
				}
			});
				
		return view;
	}
	
	public void setIngelogd(boolean b) {
		
		this.ingelogd = b;
	}
	
	public void veranderFragment(Fragment nieuweFragment) {
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.functies, nieuweFragment);
		
		if(ingelogd) { // TODO veranderen naar ingelogd
			
			transaction.addToBackStack(null);
		}
		
		transaction.commit();
	}
	
	public static AccountFunctiesFragment getInstance() {
		
		if(instance == null) {
			
			new AccountFunctiesFragment();
		}
		
		return instance;
	}
	
	public interface ToggleFunctiesManager {
		
		public void toggleFuncties();
		
		public void showFAQ();
	}
	
}
