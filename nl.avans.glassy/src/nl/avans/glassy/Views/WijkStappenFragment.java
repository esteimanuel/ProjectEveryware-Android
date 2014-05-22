package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WijkStappenFragment extends Fragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);		
		Bundle bundle = this.getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkstappen_fragment, container, false);
		return rootView;
	}

}
