package nl.avans.glassy.Interfaces;

import java.util.ArrayList;
import java.util.List;

import nl.avans.glassy.Controllers.WijkFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
	private List<WijkFragment> actieList;

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		actieList = new ArrayList<WijkFragment>();
	}

	@Override
	public Fragment getItem(int position) {
		return actieList.get(position);
	}

	@Override
	public int getCount() {
		return actieList.size();
	}

	public void addFragmentToAdapter(WijkFragment theFragment) {
		actieList.add(theFragment);
		notifyDataSetChanged();
	}
}
