package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
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

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class WijkFragment extends Fragment implements
		YouTubePlayer.OnInitializedListener {
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	// Fragments
	private WijkDetailsFragment wijkDetails;
	private YouTubePlayerSupportFragment youtubePlayerFragment;

	// Misc
	public static final String API_KEY = "AIzaSyDPmdqhVEdYTJSHxol_FKg2PtPEPg3Xfcw";
	public static final String VIDEO_ID = "vkgRqz2lLAs";

	private static final int RQS_ErrorDialog = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijk_fragment, container, false);

		int height = getDeviceSize();

		Log.d("height", Integer.toString(height));

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

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
		youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
		youtubePlayerFragment.initialize(API_KEY, this);
		fragmentTransaction.replace(R.id.youtube, youtubePlayerFragment,
				"youtubePlayer");

		fragmentTransaction.commit();

		return rootView;
	}

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult result) {

		if (result.isUserRecoverableError()) {
			result.getErrorDialog(getActivity(), RQS_ErrorDialog).show();
		}
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {

		if (!wasRestored) {
			player.cueVideo(VIDEO_ID);
		}
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
