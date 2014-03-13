package avans.glassy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class MijnWijkActivity extends Activity implements
	YouTubePlayer.OnInitializedListener,
	DeelnemerGridFragment.OnDeelnamerSelectedListener{
	
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
		deelnemers.add(new Deelnemer("Esté", "Tigele", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Gijs", "van der Venne", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Leon", "van Tuijl", R.drawable.profile3));
		deelnemers.add(new Deelnemer("Mathijs", "van den Worm", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Niek", "Willems", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Remi", "Sloot", R.drawable.profile3));
		deelnemers.add(new Deelnemer("Stefan", "Vervoort", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Tom", "Voigt", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Bart", "Schut", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Pieter", "School", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Tim", "Slot", R.drawable.profile3));
		deelnemers.add(new Deelnemer("Esté", "Tigele", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Gijs", "van der Venne", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Leon", "van Tuijl", R.drawable.profile3));
		deelnemers.add(new Deelnemer("Mathijs", "van den Worm", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Niek", "Willems", R.drawable.profile2));
		deelnemers.add(new Deelnemer("Remi", "Sloot", R.drawable.profile3));
		deelnemers.add(new Deelnemer("Stefan", "Vervoort", R.drawable.profile1));
		deelnemers.add(new Deelnemer("Tom", "Voigt", R.drawable.profile2));	
		
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

	@Override
	public void onDeelnemerClicked(int pos) {
	Intent intent = new Intent(getApplicationContext(), ProfielOverlayActivity.class);
    //Create Parcelable object
	ParcableDeelnemer parcelableDeelnemer= new ParcableDeelnemer(deelnemers.get(pos));
 
    //Store Parcelable object in Intent
    intent.putExtra("deelnemer", parcelableDeelnemer);
 
    //Start next activity
    startActivity(intent);
	}


}
