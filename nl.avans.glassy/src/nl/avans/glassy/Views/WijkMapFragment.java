package nl.avans.glassy.Views;


import nl.avans.glassy.R;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class WijkMapFragment extends Fragment  {

	private String url = "https://www.google.nl/maps/place/%27%27s-Hertogenbosch%27/@51.7003464,5.3086228,15z/data=!4m2!3m1!1s0x47c6ee37ad3edd37:0x400de5a8d1e7c50?hl=en";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment, container, false);
		WebView webView = null;
		if(url != null)
		{
			// Set WebView URL
			webView = (WebView) rootView.findViewById(R.id.webview);
			DisplayMetrics display = this.getResources().getDisplayMetrics();
			final int height = display.heightPixels;
			webView.getLayoutParams().height = (height/2);
			webView.loadUrl(url);

			// Prevent it from opening in a external browser
			webView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					}
			});

			// Enable Javascript
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);
		}

		return rootView;
	}




}
