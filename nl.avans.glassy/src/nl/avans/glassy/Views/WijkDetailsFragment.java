package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Actie;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WijkDetailsFragment extends Fragment {

	private Actie thisActie;
	private ViewGroup rootView;
	private OnSpecialButtonPressListener listener;

	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnSpecialButtonPressListener) {

			listener = (OnSpecialButtonPressListener) activity;

		} else {

			throw new ClassCastException();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.wijkdetails_fragment,
				container, false);

		rootView.findViewById(R.id.ikDoeMeeButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						listener.volgendeActieStapUitvoeren();
					}
				});

		init();
		return rootView;
	}

	private void init() {
		Button ikDoeMeeButton = (Button) rootView
				.findViewById(R.id.ikDoeMeeButton);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"Fonts/HelveticaNeue_Lt.ttf");
		ikDoeMeeButton.setTypeface(font);
	}

	public Actie getActie() {

		return thisActie;
	}

	public interface OnSpecialButtonPressListener {

		public void volgendeActieStapUitvoeren();
	}

	public void setWijkNaam(String wijkNaam) {
		TextView wijkTitel = (TextView) rootView.findViewById(R.id.wijkTitel);
		wijkTitel.setText(wijkNaam);
		wijkTitel.setVisibility(View.VISIBLE);
	}

	public void setDeelnemersCount(int length, int percentage) {
		TextView deelnemersCount = (TextView) rootView
				.findViewById(R.id.deelnemersCount);
		deelnemersCount.setText(Integer.toString(length));
		TextView deelnemersPercentage = (TextView) rootView
				.findViewById(R.id.percentage);
		deelnemersPercentage.setText(Integer.toString(percentage));
		
		rootView.findViewById(R.id.wijkDetails).setVisibility(View.VISIBLE);
	}
}
