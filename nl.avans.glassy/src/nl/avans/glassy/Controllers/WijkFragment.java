package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Views.GebruikerAccountFragment;
import nl.avans.glassy.Views.WijkMapFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WijkFragment extends Fragment {
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	// Fragments
	private WijkDetailsFragment wijkDetails;
	private WijkVideoFragment wijkVideoFragment;
	private WijkMapFragment wijkMapFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijk_fragment, container, false);

		int height = getDeviceSize();

		Log.d("height", Integer.toString(height));

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		
//		GebruikerAccountFragment headerFragment = new GebruikerAccountFragment();
//		fragmentTransaction.replace(R.id.gebruikerFuncties, headerFragment, "gebruikerFuncties");		

		// New wijkDetails SupportFragment
		FrameLayout wijkDetailsPlaceholder = (FrameLayout) rootView
				.findViewById(R.id.details);
		LayoutParams params = (LinearLayout.LayoutParams) wijkDetailsPlaceholder
				.getLayoutParams();
		params.height = height;
		wijkDetailsPlaceholder.setLayoutParams(params);

		wijkDetails = new WijkDetailsFragment();
		fragmentTransaction.replace(R.id.details, wijkDetails, "wijkDetails");

		// New youtubePlayer SupportFragment
		wijkVideoFragment = new WijkVideoFragment();
		fragmentTransaction.replace(R.id.youtube, wijkVideoFragment, "youtubePlayer");
		
		// New Mapwebview SupportFragment
		wijkMapFragment = new WijkMapFragment();
		fragmentTransaction.replace(R.id.map, wijkMapFragment, "wijkMap");

		fragmentTransaction.commit();

		return rootView;
	}

	

	// Get usable device height (not counting statusbar ect.)
	// Used for setting height of first fragment
	// TODO: detect API version since this code is API 13+
	private int getDeviceSize() {
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;

		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
			height = height - result - 10;
		}

		return height;
	}
}
