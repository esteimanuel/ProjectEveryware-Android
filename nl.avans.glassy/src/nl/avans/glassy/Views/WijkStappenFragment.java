package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class WijkStappenFragment extends Fragment{

	private ProgressBar status1;
	private ProgressBar status2;
	private ProgressBar status3;
	private ProgressBar status4;
	private ProgressBar status5;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkstappen_fragment, container, false);
		status1 = (ProgressBar) rootView.findViewById(R.id.stapprogress1);
		status2 = (ProgressBar) rootView.findViewById(R.id.stapprogress2);
		status3 = (ProgressBar) rootView.findViewById(R.id.stapprogress3);
		status4 = (ProgressBar) rootView.findViewById(R.id.stapprogress4);
		status5 = (ProgressBar) rootView.findViewById(R.id.stapprogress5);
		return rootView;
	}
	
	public void updateStatus(int stap1, int stap2, int stap3, int stap4, int stap5)
	{		
				status1.setProgress(stap1);
				status2.setProgress(stap2);
				status3.setProgress(stap3);
				status4.setProgress(stap4);
				status5.setProgress(stap5);				
	}

}
