package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class WijkDeelnemersFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkdeelnemers_fragments, container, false);

		GridView gridview = (GridView) rootView.findViewById(R.id.deelnemerGrid);
		gridview.setAdapter(new nl.avans.glassy.Models.ImageAdapter(getActivity()));

		int deelnemers = gridview.getAdapter().getCount();
		TextView deelnemerView = (TextView) rootView.findViewById(R.id.deelnemerCount);
		deelnemerView.setText(String.valueOf(deelnemers));
		TextView procentCount = (TextView) rootView.findViewById(R.id.procentCount);
		int procent = 500/deelnemers;
		procentCount.setText(String.valueOf(procent));
		return rootView;
	}
}