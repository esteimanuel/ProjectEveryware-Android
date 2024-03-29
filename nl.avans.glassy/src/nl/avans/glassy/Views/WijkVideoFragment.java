package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Controllers.WijkActivity;
import nl.avans.glassy.Views.WijkGoededoelenFragment.wijkgoededoelenListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class WijkVideoFragment extends Fragment implements
		YouTubePlayer.OnInitializedListener{

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private YouTubePlayerSupportFragment youtubePlayerFragment;

	// Misc
	public static final String API_KEY = "AIzaSyDPmdqhVEdYTJSHxol_FKg2PtPEPg3Xfcw";
	private String VIDEO_ID;
	private videoFullscreenListener myListener;

	private static final int RQS_ErrorDialog = 1;
	private Boolean pressed = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkvideo_fragment, container, false);		
		
		fragmentInitialization();

		return rootView;
	}

	private void fragmentInitialization(){

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		// New youtubePlayer SupportFragment
		youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();		
		youtubePlayerFragment.initialize(API_KEY, this);
		fragmentTransaction.replace(R.id.youtube, youtubePlayerFragment,
				"youtubePlayer");

		fragmentTransaction.commit();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//koppel de activity aan de listener
		try {
			myListener = (videoFullscreenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement videoFullscreenListener");
		}
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
			final YouTubePlayer player, boolean wasRestored) {

		if (!wasRestored) {
			player.cueVideo(VIDEO_ID);
			
			player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
			player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener(){
				@Override
				public void onFullscreen(boolean arg0) {  
					if(arg0)
					{
						player.pause();					
						myListener.onVideoFullscreen(VIDEO_ID);	
					}
				}});
			
		}
	}

	public void setVideoId(String VIDEO_ID) {
		this.VIDEO_ID = VIDEO_ID;		
	}

	public interface videoFullscreenListener {		
		public void onVideoFullscreen(String VIDEO_ID);
	}
}