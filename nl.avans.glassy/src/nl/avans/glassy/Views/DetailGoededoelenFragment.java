package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailGoededoelenFragment extends Fragment {
	private TextView info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detailgoededoelen_fragment, container, false);		
		info = (TextView) rootView.findViewById(R.id.Goededoelen_detail_info);		
		return rootView;
	}
	
	public void setText(String text)
	{
		info.setText(text);
	}
	
	
}
