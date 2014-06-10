package nl.avans.glassy.Views;

import nl.avans.glassy.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class WijkDeelnemersFragment extends Fragment {
	private ViewGroup rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkdeelnemers_fragments, container, false);

		GridView gridview = (GridView) rootView
				.findViewById(R.id.deelnemerGrid);
		gridview.setAdapter(new nl.avans.glassy.Models.ImageAdapter(
				getActivity()));

		int deelnemers = 0;
		TextView deelnemerView = (TextView) rootView
				.findViewById(R.id.deelnemerCount);
		deelnemerView.setText(String.valueOf(deelnemers));
		return rootView;
	}

	public void setDeelnemersCount(int length) {
		TextView deelnemerCount = (TextView) rootView
				.findViewById(R.id.deelnemerCount);
		deelnemerCount.setText("" + length);
	}
}