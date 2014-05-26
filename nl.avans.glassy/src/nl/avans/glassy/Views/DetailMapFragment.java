package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class DetailMapFragment extends Fragment {	
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private String wijkID = "1";
	private ProgressBar mPbar = null;
	private WebView webView = null;
	
	
	public  DetailMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detailmap_fragment, container, false);
		webviewSetup(rootView);
		return rootView;
	}
	
	public void webviewSetup(View rootView)
	{
		//laden
	}
	

}
