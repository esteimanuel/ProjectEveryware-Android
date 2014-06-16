package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailStappenFragment extends Fragment {
	private TextView infoTextView;
	private Button closeb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//TODO waardes veranderen. bij findbyviews
		View rootView = inflater.inflate(R.layout.detailstappen_fragment, container, false);		
		infoTextView = (TextView) rootView.findViewById(R.id.stappen_detail_info);
		closeb = (Button) rootView.findViewById(R.id.stappen_view_button);
		closeb.setOnClickListener(
				new OnClickListener(){

					@Override
					public void onClick(View v) {
						getActivity().finish();
					}

				});
		return rootView;
	}

	public void setText(String text)
	{
		infoTextView.setText(text);
	}

	public void setInfo(String p1, String p2, String p3, String p4, String p5) {
		String info = null;
		info = "<p>" + getResources().getString(R.string.stap1) + " = <b>" + p1 + "%</b> </p>" + 
				"<p>" + getResources().getString(R.string.stap2) + " = <b>" + p2 + "%</b> </p>" + 
				"<p>" + getResources().getString(R.string.stap3) + " = <b>" + p3 + "%</b> </p>" + 
				"<p>" + getResources().getString(R.string.stap4) + " = <b>" + p4 + "%</b> </p>" + 
				"<p>" + getResources().getString(R.string.stap5) + " = <b>" + p5 + "%</b> </p>";		
		infoTextView.setText(Html.fromHtml(info));	
	}

}
