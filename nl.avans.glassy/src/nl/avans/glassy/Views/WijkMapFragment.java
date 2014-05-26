package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class WijkMapFragment extends Fragment {
	private final String URL = "http://glassy-web.avans-project.nl/?wijk=";
	private int mapHeight;
	private int mapWidth;
	private ProgressBar mPbar = null;
	private WebView webView;
	private webClientListener mywebListener;
	private Boolean listenerset = false;

	// private Actie thisActie;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO: Not needed yet. Just a try to get the map to load faster.
		super.onCreate(savedInstanceState);

		// Bundle bundle = this.getArguments();
		// thisActie = (Actie) bundle.getParcelable("ActieObject");

		// get required size based on screen size
		DisplayMetrics display = this.getResources().getDisplayMetrics();
		mapHeight = display.heightPixels / 2;
		mapWidth = display.widthPixels;

		// Set the right size programmatically + various settings for what is
		// allowed
		webviewSetup();
		connectWebViewClient();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment,
				container, false);
		RelativeLayout layout = (RelativeLayout) rootView
				.findViewById(R.id.container);

		layout.addView(webView);
		createProgressSpinner(rootView);

		// Set WebView URL
		webView.loadUrl(URL + 0);
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

	public void createProgressSpinner(View view) {
		// Find progressbar
		mPbar = (ProgressBar) view.findViewById(R.id.web_view_progress);
		// set the size
		RelativeLayout.LayoutParams webViewLayout = new RelativeLayout.LayoutParams(
				mapWidth, mapHeight);
		webViewLayout.addRule(RelativeLayout.BELOW, R.id.mapTitel);
		mPbar.setLayoutParams(webViewLayout);
	}

	public void webviewSetup() {
		// Create new WebView object.
		webView = new WebView(getActivity());
		webView.setVisibility(View.GONE);

		// Enable different settings
		// Alert: If the App doesn't need JavaScript setJavaScriptEnabled should
		// be false.
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

		// Create layout, padding and other settings.
		RelativeLayout.LayoutParams webViewLayout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, mapHeight);
		webViewLayout.addRule(RelativeLayout.BELOW, R.id.mapTitel);
		webView.setLayoutParams(webViewLayout);

		float scale = getResources().getDisplayMetrics().density;
		int dpAsPixels = (int) (5 * scale + 0.5f);
		webView.setPadding(dpAsPixels, 0, dpAsPixels, 0);
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
		webView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mywebListener.onTouchMap(URL + 0);
				return true;
			}
		});
	}

	// Interface that the activity implements to listen to events
	public interface webClientListener {
		public void onTouchMap(String URL);
	}
}
