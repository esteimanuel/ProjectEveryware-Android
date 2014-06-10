package nl.avans.glassy.Views;

import java.util.ArrayList;

import nl.avans.glassy.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class FaqFragment extends Fragment {
	private TextView faqinfo;
	private ImageButton closeb;


	public FaqFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.faq_fragment, container, false);
		faqinfo = (TextView) rootView.findViewById(R.id.faqInfo);
		closeb = (ImageButton) rootView.findViewById(R.id.faq_view_button);
		closeb.setOnClickListener(
				new OnClickListener(){

					@Override
					public void onClick(View v) {
						getActivity().finish();
					}

				});
		return rootView;
	}

	public void updateText(ArrayList<String> questions,	ArrayList<String> answers) {
	
		//TODO netjes zetten
		if(questions.size() == answers.size())
		{
			String textviewinfo = "";
			for(int i = 0; i < questions.size(); i++)
			{
				String info = null;
				info = questions.get(i) + "\n" + answers.get(i) + "\n\n";
				textviewinfo = textviewinfo + info;				
			}
			
			faqinfo.setText(textviewinfo);				
		}
	}
	


}