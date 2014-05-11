package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WijkDetailsFragment extends Fragment {
//	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkdetails_fragment, container, false);
//		view = rootView;
		return rootView;
	}

//	private void fragmentInitialization() {
//		TextView wijkName = (TextView) view.findViewById(R.id.wijkTitel);
//		TextView deelnemersCount = (TextView) view
//				.findViewById(R.id.deelnemersCount);
//		TextView targetPercentage = (TextView) view
//				.findViewById(R.id.percentage);
//
//	}
}
