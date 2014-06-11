package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WijkGoededoelenFragment extends Fragment{

	private String myInfo = "laden...";
	private String title;
	private String description;
	private String message;
	private wijkgoededoelenListener myGoededoelenListener;
	private View clickableLayout;
	private TextView goededoelenInfo;
	private ProgressBar status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkgoededoelen_fragment, container, false);
		goededoelenInfo = (TextView) rootView.findViewById(R.id.goededoelenInfo);	
		status = (ProgressBar) rootView.findViewById(R.id.goededoelen_view_progress);
		clickableLayout = rootView.findViewById(R.id.goededoelenLayout);	    
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

	public void updateText(String title, String description, String message)
	{
		goededoelenInfo.setText(description);
		this.title = title;
		this.description = description;
		this.message = message;		
	}

	public void updateStatus(int progressPercentage)
	{
		status.setProgress(progressPercentage);
	}

	private void setlisteners()
	{

		clickableLayout.setOnClickListener(
				new OnClickListener(){

					@Override
					public void onClick(View v) {
						myGoededoelenListener.onTouchGoededoelen(title, description, message);
					}
				});
	}

	//Interface that the activity implements to listen to events
	public interface wijkgoededoelenListener {		
		public void onTouchGoededoelen(String title, String description, String message);
	}
}
