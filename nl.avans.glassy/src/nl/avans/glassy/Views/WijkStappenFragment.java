package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Views.WijkGoededoelenFragment.wijkgoededoelenListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

public class WijkStappenFragment extends Fragment{

	private ProgressBar status1;
	private ProgressBar status2;
	private ProgressBar status3;
	private ProgressBar status4;
	private ProgressBar status5;
	private String procent1;
	private String procent3;
	private String procent2;
	private String procent4;
	private String procent5;
	private wijkstappenListener mystappenListener;
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
		
		rootView.setOnClickListener(
				new OnClickListener(){

					@Override
					public void onClick(View v) {
						mystappenListener.onTouchstappen(procent1, procent2, procent3, procent4, procent5);
					}
				});
		return rootView;
	}

	public void updateStatus(int stap1, int stap2, int stap3, int stap4, int stap5)
	{		
		procent1 = Integer.toString(stap1);
		procent2 = Integer.toString(stap2);
		procent3 = Integer.toString(stap3);
		procent4 = Integer.toString(stap4);
		procent5 = Integer.toString(stap5);

		status1.setProgress(stap1);
		status2.setProgress(stap2);
		status3.setProgress(stap3);
		status4.setProgress(stap4);
		status5.setProgress(stap5);				
	}

	public interface wijkstappenListener {		
		public void onTouchstappen(String p1, String p2, String p3, String p4, String p5);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//koppel de activity aan de listener
		try {
			mystappenListener = (wijkstappenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement wijkstappenListener");
		}
	}

}
