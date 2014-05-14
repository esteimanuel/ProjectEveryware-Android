package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class WijkMapFragment extends Fragment {
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private String wijkID = "1";
	private int mapHeight;
	private int mapWidth;
	private ProgressBar mPbar = null;
	private WebView webView;
	private webClientListener mywebListener;
	private Boolean listenerset = false;
	

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);
		
		//get required size based on screen size
		DisplayMetrics display = this.getResources().getDisplayMetrics();
		mapHeight = display.heightPixels / 2;
		mapWidth = display.widthPixels;
		Log.d("mapHeight","mapHeight: " + Integer.toString(mapHeight));

		// Create new WebView object.
		webView = new WebView(getActivity());
		webView.setVisibility(View.GONE);
		Log.d("webView", "WebView Object created");

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
				if(!listenerset)
				{
					setlisteners();
					listenerset = true;
				}
			}
		};
		webView.setWebViewClient(customWebViewClient);
		Log.d("webView", "Custom WebViewClient Object created");

		// Enable different settings
		// Alert: If the App doesn't need JavaScript setJavaScriptEnabled should
		// be false.
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		Log.d("webView", "Settings are set");

		
		// Create layout, padding and other settings.
		RelativeLayout.LayoutParams webViewLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, mapHeight);
		webViewLayout.addRule(RelativeLayout.BELOW, R.id.mapTitel);
		webView.setLayoutParams(webViewLayout);
		Log.d("webView", "webView layout set");

		float scale = getResources().getDisplayMetrics().density;
		int dpAsPixels = (int) (5 * scale + 0.5f);
		webView.setPadding(dpAsPixels, 0, dpAsPixels, 0);
		Log.d("webView", "webView padding set");

		//add onTouch map listener
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment, container, false);
		RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.container);		

		layout.addView(webView);
		Log.d("webView", "webView added to layout");
		
		createProgressSpinner(rootView);
		
		// Set WebView URL
		webView.loadUrl(URL + wijkID);
		Log.d("webView", "webView loadURL called");
		return rootView;
	}
	
	private void setlisteners()
	{
		webView.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mywebListener.onTouchMap(URL + wijkID);
				return true;
			}
		}); 
	}
	
	public interface webClientListener {
		public void onTouchMap(String URL);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mywebListener = (webClientListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BoardListListener");
		}
	}
	
	public void createProgressSpinner(View view)
	{
		mPbar = (ProgressBar) view.findViewById(R.id.web_view_progress);
		RelativeLayout.LayoutParams webViewLayout = new RelativeLayout.LayoutParams(mapWidth, mapHeight);
		webViewLayout.addRule(RelativeLayout.BELOW, R.id.mapTitel);
		mPbar.setLayoutParams(webViewLayout);
		Log.d("webView", "webView layout set");

	}
}
