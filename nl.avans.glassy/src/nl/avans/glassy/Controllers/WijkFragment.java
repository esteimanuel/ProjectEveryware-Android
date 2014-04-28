package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WijkFragment extends Fragment {
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijk_fragment, container, false);

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		WijkDetailsFragment wijkDetails = new WijkDetailsFragment();
		fragmentTransaction.add(R.id.wijkContainer, wijkDetails);
		fragmentTransaction.commit();

		return rootView;
	}
}
