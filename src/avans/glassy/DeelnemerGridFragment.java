package avans.glassy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class DeelnemerGridFragment extends Fragment {
	private GridView deelnemersGrid;
	private DeelnemerAdapter adapter;
	private View view;
	private OnDeelnamerSelectedListener listener;
	
	public interface OnDeelnamerSelectedListener {
		public void onDeelnemerClicked(int pos);
	}
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  
	    this.view = inflater.inflate(R.layout.fragment_deelnemersgrid,
	        container, false);     
	    	    
	    deelnemersGrid = (GridView) view.findViewById(R.id.deelnemerGrid);
        return view;
	  }
	  
	  @Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			if (activity instanceof OnDeelnamerSelectedListener) {
				listener = (OnDeelnamerSelectedListener) activity;
			} else {
				throw new ClassCastException(
						activity.toString()
								+ " must implement DeelnemerGridFragment.OnDeelnamerSelectedListener");
			}
		}
	  
	    public void setData(ArrayList<Deelnemer> deelnemers){		
	    	adapter = new DeelnemerAdapter(this.getActivity(), R.layout.fragment_deelnemersgrid,
	    			deelnemers);
	    	deelnemersGrid.setAdapter(adapter);
	    	deelnemersGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					listener.onDeelnemerClicked(position);
				}
			});
	    	
	 	   String aantalDeelnemers = String.valueOf(adapter.getCount());
		   TextView deelnemerView =  (TextView) view.findViewById(R.id.deelnemerView);
		   deelnemerView.setText(aantalDeelnemers);
	    	}

}
