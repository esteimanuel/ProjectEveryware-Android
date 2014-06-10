package nl.avans.glassy.Controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Gebruiker;
import nl.avans.glassy.Threads.ActieManager;
import nl.avans.glassy.Threads.ActieStats;
import nl.avans.glassy.Threads.ActieStats.actieStatsListener;
import nl.avans.glassy.Threads.ActieTask;
import nl.avans.glassy.Threads.Faq;
import nl.avans.glassy.Threads.Faq.faqListener;
import nl.avans.glassy.Threads.GoedeDoelen;
import nl.avans.glassy.Threads.GoedeDoelen.goededoelenListener;
import nl.avans.glassy.Views.WijkDeelnemersFragment;
import nl.avans.glassy.Views.WijkDetailsFragment;
import nl.avans.glassy.Views.WijkFaqFragment;
import nl.avans.glassy.Views.WijkGoededoelenFragment;
import nl.avans.glassy.Views.WijkMapFragment;
import nl.avans.glassy.Views.WijkStappenFragment;
import nl.avans.glassy.Views.WijkVideoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WijkFragment extends Fragment implements faqListener,
		goededoelenListener, actieStatsListener {
	private ViewGroup rootView;
	private ActieManager sActieManager;
	private ActieTask mDownloadThread;

	private int wijkId;
	private int actieId = 0;
	private int target;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	// Fragments
	private WijkDetailsFragment wijkDetails;
	private WijkVideoFragment wijkVideoFragment;
	private WijkMapFragment wijkMapFragment;
	private WijkDeelnemersFragment wijkDeelnemersFragment;
	private WijkGoededoelenFragment wijkGoededoelenFragment;
	private WijkStappenFragment wijkStappenFragment;
	private WijkFaqFragment wijkFaqFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getArguments();

		wijkId = bundle.getInt("wijk_id");

		int actie_id = -1;
		try {
			actie_id = bundle.getInt("actie_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		actieId = actie_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.wijk_fragment,
				container, false);

		// Starts filling this fragment.
		ActieManager.startFragmentInitialization(this);

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		// New wijkDetails SupportFragment
		FrameLayout wijkDetailsPlaceholder = (FrameLayout) rootView
				.findViewById(R.id.details);
		LayoutParams params = (LinearLayout.LayoutParams) wijkDetailsPlaceholder
				.getLayoutParams();
		params.height = getDeviceSize();
		wijkDetailsPlaceholder.setLayoutParams(params);

		// New WijkDetails Fragment
		wijkDetails = new WijkDetailsFragment();
		fragmentTransaction.replace(R.id.details, wijkDetails, "wijkDetails");

		// New youtubePlayer SupportFragment
		wijkVideoFragment = new WijkVideoFragment();
		fragmentTransaction.replace(R.id.youtube, wijkVideoFragment,
				"youtubePlayer");

		// New Webview Fragment
		wijkMapFragment = new WijkMapFragment();
		wijkMapFragment.setWijkid(wijkId);
		fragmentTransaction.replace(R.id.map, wijkMapFragment, "wijkMap");

		fragmentTransaction.commit();
		return rootView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the wijkId associated with this Fragment
	 * 
	 * @return a int
	 */
	public int getWijkId() {
		return wijkId;
	}

	public int getActieId() {
		return actieId;
	}

	/**
	 * Sets the detail information downloaded by the ActieManager
	 * 
	 * @return a int
	 */
	public void setDetail(JSONObject results) {
		String wijkNaam = "";
		try {
			wijkNaam = results.getString("wijk_naam");
			target = Integer.parseInt(results.getString("target"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		wijkDetails.setWijkNaam(wijkNaam);
	}

	public void setActieData(JSONObject result) {
		try {
			JSONArray media = result.getJSONArray("media");
			for (int i = 0; i < media.length(); i++) {
				JSONObject object = (JSONObject) media.get(i);
				String type = object.get("type").toString();
				if (type.equals("image")) {
					String url = null;

					url = object.get("url").toString();
					new DownloadImageTask(
							(ImageView) rootView
									.findViewById(R.id.backgroundImage))
							.execute(url);
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		// New Webview Fragment
		wijkGoededoelenFragment = new WijkGoededoelenFragment();
		fragmentTransaction.replace(R.id.goededoelen, wijkGoededoelenFragment,
				"wijkgoededoelen");

		// New faq Fragment
		wijkFaqFragment = new WijkFaqFragment();
		fragmentTransaction.replace(R.id.faq, wijkFaqFragment, "wijkfaq");

		// New stappen Fragment
		wijkStappenFragment = new WijkStappenFragment();
		fragmentTransaction.replace(R.id.stappen, wijkStappenFragment,
				"wijkstappen");

		// New WijkDeelnemers Fragment
		wijkDeelnemersFragment = new WijkDeelnemersFragment();
		fragmentTransaction.replace(R.id.deelnemers, wijkDeelnemersFragment,
				"wijkDeelnemers");		

		startLoadingInfo();
		fragmentTransaction.commit();
		ActieManager.startDeelnemersInitialization(this);
		rootView.findViewById(R.id.actieSpecefiek).setVisibility(View.VISIBLE);
	}

	private void startLoadingInfo() {
		Faq.loadFaq(getActivity().getApplicationContext(), this);
		GoedeDoelen.loadGoededoelen(getActivity().getApplicationContext(), this, wijkId);
		ActieStats.loadFaq(getActivity().getApplicationContext(), this, wijkId);
	}

	public void setDeelnemers(JSONArray result) {
		int percentage = (int) (((float) result.length() / (float) target) * 100);
		wijkDetails.setDeelnemersCount(result.length(), percentage);
		wijkDeelnemersFragment.setDeelnemersCount(result.length());
	}	
	

	// Get usable device height (not counting statusbar ect.)
	// Used for setting height of wijkDetails
	private int getDeviceSize() {
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;

		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
			height = height - result - 70;
		}

		return height;
	}

	public void onStart() {

		super.onStart();
		evalActieButton();
	}

	public void onResume() {

		super.onResume();
		evalActieButton();
	}

	public void evalActieButton() {

		Button actieButton = (Button) getView().findViewById(
				R.id.ikDoeMeeButton);

		if (getActieId() <= 0) {

			actieButton.setText("Geen actie? Wel Glasvezel!");

		} else if (Gebruiker.zitInWelkeActie(getActivity()
				.getApplicationContext()) == getActieId()) {

			if (Gebruiker.heeftBetaald(getActivity().getApplicationContext())) {

				actieButton.setText("Provider voorkeur opgeven");

			} else {

				actieButton.setText("Betalen pannekoek!");
			}

		} else if (Gebruiker.zitInActie(getActivity().getApplicationContext())) {

			if (Gebruiker
					.zitInWelkeActie(getActivity().getApplicationContext()) != getActieId()) {

				actieButton.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onFaqLoaded(ArrayList<String> questions,
			ArrayList<String> answers) {
		wijkFaqFragment.updateText(questions, answers);
	}

	@Override
	public void onGoededoelenLoaded(String title, String description,
			String message) {
		wijkGoededoelenFragment.updateText(title, description, message);
	}
	
	@Override
	public void onActieStatsLoaded(int participants, int houses, int target,
			int totalPartPerc, int targetPartPerc, int paidTargetPerc,
			int providerSelectPerc, int goedeDoelPartPerc) {
		// TODO Auto-generated method stub
		wijkStappenFragment.updateStatus(totalPartPerc, paidTargetPerc, providerSelectPerc, 0, 0);
		wijkGoededoelenFragment.updateStatus(goedeDoelPartPerc);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			URL imageUrl = null;
			try {
				imageUrl = new URL(urldisplay);
			} catch (MalformedURLException e1) {
				Log.d("DownloadImageTask", "Cannot parse URL");
				e1.printStackTrace();
			}
			Bitmap mIcon11 = null;
			try {
				URLConnection connection = imageUrl.openConnection();
				InputStream input = connection.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				Bitmap preview_bitmap = BitmapFactory.decodeStream(input, null,
						options);
				preview_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				mIcon11 = BitmapFactory.decodeStream(new ByteArrayInputStream(
						out.toByteArray()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null)
				bmImage.setImageBitmap(result);
		}
	}

	
}
