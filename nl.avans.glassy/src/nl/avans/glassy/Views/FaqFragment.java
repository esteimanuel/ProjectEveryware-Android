package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FaqFragment extends Fragment {
	private TextView faqinfo;

	public FaqFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.faq_fragment, container, false);
		faqinfo = (TextView) rootView.findViewById(R.id.faqInfo);
		return rootView;
	}

	public void updateText(String uRL) {
		faqinfo.setText(uRL);
	}


}