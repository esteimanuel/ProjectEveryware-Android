package nl.avans.glassy.Views;

import java.util.ArrayList;

import nl.avans.glassy.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WijkFaqFragment extends Fragment{
	private TextView faqContent;
    private wijkFaqListener myfaqListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);		
		Bundle bundle = this.getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkfaq_fragment, container, false);
		faqContent = (TextView) rootView.findViewById(R.id.faqContent);
		faqContent.setText("laden...");
		setlisteners();
		return rootView;
	}
	
	public void updateText(ArrayList<String> questions,	ArrayList<String> answers) {
		
		if(questions.size() > 2)
		{
			String textviewinfo = "";
			for(int i = 0; i < 3; i++)
			{
				String info = null;
				info = questions.get(i) + "\n";
				textviewinfo = textviewinfo + info;				
			}
			faqContent.setText(textviewinfo);		
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//koppel de activity aan de listener
		try {
			myfaqListener = (wijkFaqListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement wijkfaqListenerr");
		}
	}
	
	private void setlisteners()
	{
		faqContent.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {				
				myfaqListener.onTouchFaq();
				return true;
			}
		});
	}
	
	//Interface that the activity implements to listen to events
	public interface wijkFaqListener {		
		public void onTouchFaq();
	}
}
