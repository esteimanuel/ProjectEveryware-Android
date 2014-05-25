package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WijkGoededoelenFragment extends Fragment{
	
	private String myInfo = "Als er meer dan 70% van de wijk meedoet dan zal iedereen in de wijk 50% korting bij alle providers krijgen." +
			"\ndus waar wacht je nog op ? doe ook mee en overtuig gelijk de buren";
	private wijkgoededoelenListener myGoededoelenListener;
	private View clickableLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);		
	//	Bundle bundle = this.getArguments();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkgoededoelen_fragment, container, false);
		TextView text = (TextView) rootView.findViewById(R.id.goededoelenInfo);		
	    clickableLayout = rootView.findViewById(R.id.goededoelenLayout);
		text.setText(myInfo);
		setlisteners();
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//koppel de activity aan de listener
		try {
			myGoededoelenListener = (wijkgoededoelenListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement goedeDoelenListenerr");
		}
	}

	
	private void setlisteners()
	{
		clickableLayout.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {				
				myGoededoelenListener.onTouchGoededoelen(myInfo);
				return true;
			}
		});
	}
	
	//Interface that the activity implements to listen to events
	public interface wijkgoededoelenListener {		
		public void onTouchGoededoelen(String infofull);
	}
}
