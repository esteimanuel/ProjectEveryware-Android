package nl.avans.glassy.Views;


import nl.avans.glassy.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class WijkMapFragment extends Fragment  {

	private String url = "https://www.insidegamer.nl/";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.wijkmap_fragment, container, false);

		if(url != null)
		{
			WebView webView = (WebView) rootView.findViewById(R.id.webview);
			WebViewClient client = new WebViewClient();
			
			webView.setWebViewClient(client);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			
			webView.loadUrl(url);
		}

		return rootView;
	}

	private class wijkWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}	


	}
	

}
