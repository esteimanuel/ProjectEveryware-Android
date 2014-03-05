package avans.glassy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class MijnWijkActivity extends Activity implements
	YouTubePlayer.OnInitializedListener{
	 
public static final String API_KEY = "AIzaSyDPmdqhVEdYTJSHxol_FKg2PtPEPg3Xfcw";
public static final String VIDEO_ID = "UXssI5tMc0A";

private YouTubePlayerFragment youTubePlayerFragment;
private static final int RQS_ErrorDialog = 1;

	private DeelnemerGridFragment deelnemerGridFragment;
	private ArrayList<Deelnemer> deelnemers;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mijn_wijk);    
        
        FragmentManager fm = getFragmentManager();        

        youTubePlayerFragment = (YouTubePlayerFragment) fm
        		.findFragmentById(R.id.youtubeplayerfragment);
        youTubePlayerFragment.initialize(API_KEY, this);
        
		deelnemers = new ArrayList<Deelnemer>();
		deelnemers.add(new Deelnemer("Bart", "Schut", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Pieter", "School", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Tim", "Slot", R.drawable.profile3));
		
        deelnemerGridFragment = (DeelnemerGridFragment) fm
				.findFragmentById(R.id.DeelnemersGridFragment);
		// give fragment the data to show
        deelnemerGridFragment.setData(deelnemers);
    }
	


@Override
public void onInitializationFailure(Provider provider,
 YouTubeInitializationResult result) {

if (result.isUserRecoverableError()) {
 result.getErrorDialog(this, RQS_ErrorDialog).show(); 
}
}

@Override
public void onInitializationSuccess(Provider provider, YouTubePlayer player,
 boolean wasRestored) {
	 
if (!wasRestored) {
      player.cueVideo(VIDEO_ID);
    }
}


}
