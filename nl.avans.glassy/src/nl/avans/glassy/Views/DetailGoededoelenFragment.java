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

public class DetailGoededoelenFragment extends Fragment {
	private TextView infoTextView;
	private Button closeb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detailgoededoelen_fragment, container, false);		
		infoTextView = (TextView) rootView.findViewById(R.id.Goededoelen_detail_info);
		closeb = (Button) rootView.findViewById(R.id.goededoelen_view_button);
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

	public void setInfo(String title, String description, String message) {
		String info = null;
		info = "<p> <b>" + title + "</b> <br />" + message + "</p>";		
		infoTextView.setText(Html.fromHtml(info));	
	}


}
