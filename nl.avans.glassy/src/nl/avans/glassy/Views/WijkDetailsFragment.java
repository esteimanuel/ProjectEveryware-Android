package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Actie;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WijkDetailsFragment extends Fragment {

	private Actie thisActie;
	private ViewGroup rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getArguments();
		thisActie = (Actie) bundle.getParcelable("ActieObject");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.wijkdetails_fragment, container, false);
		init();
		return rootView;
	}

	private void init() {
		TextView wijkTitel = (TextView) rootView.findViewById(R.id.wijkTitel);
		wijkTitel.setText(thisActie.getWijk_naam());
		
		Button ikDoeMeeButton = (Button) rootView.findViewById(R.id.ikDoeMeeButton);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Fonts/HelveticaNeue_Lt.ttf");
		ikDoeMeeButton.setTypeface(font);
	}
}
