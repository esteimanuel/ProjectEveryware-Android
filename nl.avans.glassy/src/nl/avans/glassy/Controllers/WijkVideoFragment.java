package nl.avans.glassy.Controllers;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayer.Provider;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WijkVideoFragment extends Fragment implements
		YouTubePlayer.OnInitializedListener {

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private YouTubePlayerSupportFragment youtubePlayerFragment;

	// Misc
	public static final String API_KEY = "AIzaSyDPmdqhVEdYTJSHxol_FKg2PtPEPg3Xfcw";
	public static final String VIDEO_ID = "vkgRqz2lLAs";

	private static final int RQS_ErrorDialog = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkvideo_fragment, container, false);
		
		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

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
}
