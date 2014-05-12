package nl.avans.glassy.Views;

import nl.avans.glassy.R;
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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class WijkMapFragment extends Fragment {
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private String wijkID = "1";
	private int mapHeight;

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);

		DisplayMetrics display = this.getResources().getDisplayMetrics();
		mapHeight = display.heightPixels / 2;
		Log.d("mapHeight","mapHeight: " + Integer.toString(mapHeight));

		// Create new WebView object.
		webView = new WebView(getActivity());
		Log.d("webView", "WebView Object created");

		// Prevent the WebView from opening in a external browser
		WebViewClient customWebViewClient = new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);

				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {

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
		RelativeLayout.LayoutParams webViewLayout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, mapHeight);
		webViewLayout.addRule(RelativeLayout.BELOW, R.id.mapTitel);
		webView.setLayoutParams(webViewLayout);
		Log.d("webView", "webView layout set");

		float scale = getResources().getDisplayMetrics().density;
		int dpAsPixels = (int) (5 * scale + 0.5f);
		webView.setPadding(dpAsPixels, 0, dpAsPixels, 0);
		Log.d("webView", "webView padding set");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment,
				container, false);

		RelativeLayout layout = (RelativeLayout) rootView
				.findViewById(R.id.container);

		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		layout.addView(webView);
		Log.d("webView", "webView added to layout");
		
		// Set WebView URL
		webView.loadUrl(URL + wijkID);
		Log.d("webView", "webView loadURL called");
		return rootView;
	}
}
