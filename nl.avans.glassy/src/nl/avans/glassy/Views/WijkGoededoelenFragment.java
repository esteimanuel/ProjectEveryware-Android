package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Actie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WijkGoededoelenFragment extends Fragment{
	
	private String myInfo = "test info dit moet aangepast worden.";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);		
		Bundle bundle = this.getArguments();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkgoededoelen_fragment, container, false);
		TextView text = (TextView) rootView.findViewById(R.id.goededoelenInfo);		
		text.setText(myInfo);
		return rootView;
	}

}
