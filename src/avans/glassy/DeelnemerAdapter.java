package avans.glassy;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class DeelnemerAdapter extends ArrayAdapter<Deelnemer> {
	private ArrayList<Deelnemer> data;
	
	public DeelnemerAdapter(Context context, int layoutResourceId,
			ArrayList<Deelnemer> data) {	
    	super(context, layoutResourceId, data);
    	this.data = data;
    }

    public int getCount() {
        return data.size();
    }

    public Deelnemer getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }
        
        Deelnemer deelnemer =(Deelnemer) data.get(position);
        imageView.setImageResource(deelnemer.getProfielFoto());
        return imageView;
    }
}