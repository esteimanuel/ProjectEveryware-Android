package avans.glassy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class DeelnemerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deelnemer);
		
		GridView gridview = (GridView) findViewById(R.id.deelnemerGrid);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(DeelnemerActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	   String deelnemers = String.valueOf(gridview.getAdapter().getCount());
	   TextView deelnemerView =  (TextView) findViewById(R.id.deelnemerView);
	   deelnemerView.setText(deelnemers);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}


