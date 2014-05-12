package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import nl.avans.glassy.Interfaces.ScrollViewListener;
import nl.avans.glassy.Views.WijkDeelnemersFragment;
import nl.avans.glassy.Views.WijkDetailsFragment;
import nl.avans.glassy.Views.WijkMapFragment;
import nl.avans.glassy.Views.WijkVideoFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WijkFragment extends Fragment implements ScrollViewListener {
	private float oldAlpha = 1.00f;
	private ImageView background;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	// Fragments
	private WijkDetailsFragment wijkDetails;
	private WijkVideoFragment wijkVideoFragment;
	private WijkMapFragment wijkMapFragment;
	private WijkDeelnemersFragment wijkDeelnemersFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijk_fragment, container, false);

		ObservableScrollView scrollView = (ObservableScrollView) rootView
				.findViewById(R.id.scrollPanel);
		scrollView.setScrollViewListener(this);
		
		background = (ImageView) rootView.findViewById(R.id.backgroundImage);
		
		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		// New wijkDetails SupportFragment
		FrameLayout wijkDetailsPlaceholder = (FrameLayout) rootView
				.findViewById(R.id.details);
		LayoutParams params = (LinearLayout.LayoutParams) wijkDetailsPlaceholder
				.getLayoutParams();
		params.height = getDeviceSize();
		wijkDetailsPlaceholder.setLayoutParams(params);

		// New WijkDetails Fragment
		wijkDetails = new WijkDetailsFragment();
		fragmentTransaction.replace(R.id.details, wijkDetails, "wijkDetails");

		// New youtubePlayer SupportFragment
		wijkVideoFragment = new WijkVideoFragment();
		fragmentTransaction.replace(R.id.youtube, wijkVideoFragment,
				"youtubePlayer");

		// New Webview Fragment
		wijkMapFragment = new WijkMapFragment();
		fragmentTransaction.replace(R.id.map, wijkMapFragment, "wijkMap");
		
		// New WijkDeelnemers Fragment
//		wijkDeelnemersFragment = new WijkDeelnemersFragment();
//		fragmentTransaction.replace(R.id.deelnemers, wijkDeelnemersFragment, "wijkDeelnemers");

		fragmentTransaction.commit();

		return rootView;
	}

	// Get usable device height (not counting statusbar ect.)
	// Used for setting height of wijkDetails
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
			height = height - result - 50;
		}

		return height;
	}

	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// Log.d("scolling", "Scrolling");

		float neededBlur = 1.00f;
		if (y < 50) {
			// ScrollView on top
			neededBlur = 1.00f;
		} else if (y >= 50 && y <= 200) {
			// ScrollView in fading zone
			neededBlur -= ((y - 50.00f) / 150.00f);
		} else {
			// ScrollView scrolling past fading zone
			neededBlur = 0.00f;
		}

		// Log.d("neededBlur", Double.toString(neededAlpha));

		if (neededBlur != oldAlpha) {
		//	background.setImageBitmap();
		}

		oldAlpha = neededBlur;
	}
}
