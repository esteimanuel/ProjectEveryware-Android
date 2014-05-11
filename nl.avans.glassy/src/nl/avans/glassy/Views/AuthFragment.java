package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View) inflater.inflate(R.layout.auth_fragment, container, false);
				
		return view;
	}
}
