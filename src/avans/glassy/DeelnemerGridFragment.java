package avans.glassy;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class DeelnemerGridFragment extends Fragment {
	private GridView deelnemersGrid;
	private DeelnemerAdapter adapter;
	private View view;
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  
	    this.view = inflater.inflate(R.layout.fragment_deelnemersgrid,
	        container, false);     
	    	    
	    deelnemersGrid = (GridView) view.findViewById(R.id.deelnemerGrid);
        return view;
	  }
	  
	    public void setData(ArrayList<Deelnemer> deelnemers){		
	    	adapter = new DeelnemerAdapter(this.getActivity(), R.layout.fragment_deelnemersgrid,
	    			deelnemers);
	    	deelnemersGrid.setAdapter(adapter);
	    	
	 	   String aantalDeelnemers = String.valueOf(adapter.getCount());
		   TextView deelnemerView =  (TextView) view.findViewById(R.id.deelnemerView);
		   deelnemerView.setText(aantalDeelnemers);
	    	}

}
