package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WijkDetailsFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Probably will be needed when filling with real
		// data
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkdetails_fragment, container, false);
		return rootView;
	}
}
