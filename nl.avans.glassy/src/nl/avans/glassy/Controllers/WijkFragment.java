package nl.avans.glassy.Controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;

import nl.avans.glassy.R;
import nl.avans.glassy.Interfaces.ScrollViewListener;
import nl.avans.glassy.Models.Actie;
import nl.avans.glassy.Models.Faq;
import nl.avans.glassy.Models.Faq.faqListener;
import nl.avans.glassy.Models.GoedeDoelen.goededoelenListener;
import nl.avans.glassy.Views.WijkDeelnemersFragment;
import nl.avans.glassy.Views.WijkDetailsFragment;
import nl.avans.glassy.Views.WijkFaqFragment;
import nl.avans.glassy.Views.WijkGoededoelenFragment;
import nl.avans.glassy.Views.WijkMapFragment;
import nl.avans.glassy.Views.WijkStappenFragment;
import nl.avans.glassy.Views.WijkVideoFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WijkFragment extends Fragment implements ScrollViewListener, faqListener, goededoelenListener {

	private float oldAlpha = 1.00f;
	private ImageView background;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	// Fragments
	private WijkDetailsFragment wijkDetails;
	private WijkVideoFragment wijkVideoFragment;
	private WijkMapFragment wijkMapFragment;
	private WijkDeelnemersFragment wijkDeelnemersFragment;
	private WijkGoededoelenFragment wijkGoededoelenFragment;
	private Actie wijkActie;
	private WijkStappenFragment wijkStappenFragment;
	private WijkFaqFragment wijkFaqFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijk_fragment, container, false);

		ObservableScrollView scrollView = (ObservableScrollView) rootView
				.findViewById(R.id.scrollPanel);
		scrollView.setScrollViewListener(this);

		background = (ImageView) rootView.findViewById(R.id.backgroundImage);

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
		wijkDetails.setArguments(bundle);
		fragmentTransaction.replace(R.id.details, wijkDetails, "wijkDetails");

		// New youtubePlayer SupportFragment
		wijkVideoFragment = new WijkVideoFragment();
		wijkVideoFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.youtube, wijkVideoFragment,
				"youtubePlayer");

		// New map Fragment
		wijkMapFragment = new WijkMapFragment();
		wijkMapFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.map, wijkMapFragment, "wijkMap");

		// New goededoelen Fragment
		wijkGoededoelenFragment = new WijkGoededoelenFragment();
		wijkGoededoelenFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.goededoelen, wijkGoededoelenFragment, "wijkgoededoelen");
		
		// New faq Fragment
		wijkFaqFragment = new WijkFaqFragment();
		wijkFaqFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.faq, wijkFaqFragment, "wijkfaq");

		// New stappen Fragment
		wijkStappenFragment = new WijkStappenFragment();
		wijkStappenFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.stappen, wijkStappenFragment, "wijkstappen");
		
		

		
		// New WijkDeelnemers Fragment
		// wijkDeelnemersFragment = new WijkDeelnemersFragment();
		// fragmentTransaction.replace(R.id.deelnemers, wijkDeelnemersFragment,
		// "wijkDeelnemers");
		startLoadingFragmentInfo();
		fragmentTransaction.commit();
		
		wijkActie = (Actie) bundle.getParcelable("ActieObject");
				
		return rootView;
	}

	private void startLoadingFragmentInfo() {
		Faq.loadFaq(getActivity().getApplicationContext(), this);
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
			height = height - result - 50;
		}

		return height;
	}

	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// Log.d("scolling", "Scrolling");

		float neededBlur = 1.00f;
		if (y < 50) {
			// ScrollView on top
			neededBlur = 1.00f;
		} else if (y >= 50 && y <= 200) {
			// ScrollView in fading zone
			neededBlur -= ((y - 50.00f) / 150.00f);
		} else {
			// ScrollView scrolling past fading zone
			neededBlur = 0.00f;
		}

		// Log.d("neededBlur", Double.toString(neededAlpha));

		if (neededBlur != oldAlpha) {
			// background.setImageBitmap();
		}

		oldAlpha = neededBlur;
	}
	
	public Actie getActie() {
		
		return wijkActie;
	}

	@Override
	public void onFaqLoaded(ArrayList<String> questions, ArrayList<String> answers) {
		wijkFaqFragment.updateText(questions, answers);		
	}

	@Override
	public void onGoededoelenLoaded(String goededoel, int status) {
		wijkGoededoelenFragment.updateText(goededoel, status);		
	}
}
