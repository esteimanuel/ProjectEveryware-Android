package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

@SuppressLint("ValidFragment")
public class DetailVideoFragment extends Fragment implements
YouTubePlayer.OnInitializedListener {

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private YouTubePlayerSupportFragment youtubePlayerFragment;

	// Misc
	public static final String API_KEY = "AIzaSyDPmdqhVEdYTJSHxol_FKg2PtPEPg3Xfcw";
	private String VIDEO_ID;
	private FragmentActivity myContext;

	private static final int RQS_ErrorDialog = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.detailvideo_fragment, container, false);		
		
		fragmentInitialization();

		return rootView;
	}
	
	private void fragmentInitialization(){

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		// New youtubePlayer SupportFragment
		youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();		
		youtubePlayerFragment.initialize(API_KEY, this);
		fragmentTransaction.replace(R.id.detailyoutube, youtubePlayerFragment,
				"youtubePlayer");

		fragmentTransaction.commit();

	}


	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult result) {
		
		if (result.isUserRecoverableError()) {
			result.getErrorDialog(getActivity(), RQS_ErrorDialog).show();
		}
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			if(VIDEO_ID != null)
			{
				player.cueVideo(VIDEO_ID);
			} else
			{
			}
		}
	}
	
	public void setVideo(String VIDEO_ID)
	{
		this.VIDEO_ID = VIDEO_ID;
	}


}
