package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Locks;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class DetailMapFragment extends Fragment {	
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private String wijkID = "1";
	private ProgressBar mPbar = null;
	private WebView webView = null;
	private Button closeb;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.detailmap_fragment, container, false);	
		webView = (WebView) rootView.findViewById(R.id.map_detail);
		webView.setVisibility(View.GONE);
		closeb = (Button) rootView.findViewById(R.id.map_view_button);
		closeb.setOnClickListener(
				new OnClickListener(){

					@Override
					public void onClick(View v) {
						Locks.mapfull = false;
						getActivity().finish();
					}

				});
		setupWebViewClient();
		return rootView;
	}
	
	public void loadMap(String url)
	{
		
		webView.loadUrl(url);
	}
	
	public void setupWebViewClient() {
		// create own custom webviewclient
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		
		WebViewClient customWebViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				webView.setVisibility(View.VISIBLE);
			}
		};		
		webView.setWebViewClient(customWebViewClient);
	}
	

}
