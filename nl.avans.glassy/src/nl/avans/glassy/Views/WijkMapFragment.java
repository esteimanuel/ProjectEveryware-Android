package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class WijkMapFragment extends Fragment {
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private int wijkId;
	private ProgressBar mPbar = null;
	private RelativeLayout mapLayout;
	private WebView webView;
	private webClientListener mywebListener;
	private Boolean listenerset = false;

	// private Actie thisActie;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment,	container, false);
		mapLayout = (RelativeLayout) rootView.findViewById(R.id.mapLayout);
		webView = (WebView) rootView.findViewById(R.id.web_view);
		mPbar = (ProgressBar) rootView.findViewById(R.id.web_view_progress);
		// Set the right size programmatically + various settings for what is
		// allowed
		webviewSetup();
		connectWebViewClient();
		
		// Set WebView URL
		webView.loadUrl(URL + wijkId);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// koppel de activity aan de listener
		try {
			mywebListener = (webClientListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement webClientListener");
		}
	}


	public void webviewSetup() {
		webView.setVisibility(View.GONE);
		// Enable different settings
		// Alert: If the App doesn't need JavaScript setJavaScriptEnabled should
		// be false.
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

	}

	public void connectWebViewClient() {
		// create own custom webviewclient
		
		WebViewClient customWebViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				webView.setVisibility(View.VISIBLE);
				mPbar.setVisibility(View.GONE);
				if (!listenerset) {
					setlisteners();
					listenerset = true;
				}
			}
		};		
		webView.setWebViewClient(customWebViewClient);
	}

	private void setlisteners() {		
		mapLayout.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						mywebListener.onTouchMap(URL + wijkId);
					}
				});
		//overwriten to disable events		
		webView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
			public boolean onTouch(View v, MotionEvent event) {
		    	mywebListener.onTouchMap(URL + wijkId);
				return true;
			}
		});
	}

	// Interface that the activity implements to listen to events
	public interface webClientListener {
		public void onTouchMap(String URL);
	}

	public void setWijkid(int wijkId) {
		this.wijkId = wijkId;
	}
}
