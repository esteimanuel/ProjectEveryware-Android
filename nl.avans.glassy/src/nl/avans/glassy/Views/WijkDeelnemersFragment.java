package nl.avans.glassy.Views;

import java.util.ArrayList;

import nl.avans.glassy.R;
import nl.avans.glassy.Controllers.BuddyProfielActivity;
import nl.avans.glassy.Models.Deelnemer;
import nl.avans.glassy.Models.ImageAdapter;
import nl.avans.glassy.Utils.ExtendingGridView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class WijkDeelnemersFragment extends Fragment {
	private ViewGroup rootView;
	private ExtendingGridView gridview;
	private ImageAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(
				R.layout.wijkdeelnemers_fragments, container, false);

		gridview = (ExtendingGridView) rootView
				.findViewById(R.id.deelnemerGrid);

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

	public void addDeelnemers(ArrayList<Deelnemer> deelnemersArray) {
		mAdapter = new ImageAdapter(getActivity(), deelnemersArray);
		gridview.setAdapter(mAdapter);
		gridview.setExpanded(true);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Deelnemer deelnemer = mAdapter.getDeelnemer(position);
				if (deelnemer != null) {
				    Intent intent = new Intent(getActivity(), BuddyProfielActivity.class);
				    //String message = editText.getText().toString();
				    //intent.putExtra(EXTRA_MESSAGE, message);
				    startActivity(intent);
				}
			}
		});
	}
}