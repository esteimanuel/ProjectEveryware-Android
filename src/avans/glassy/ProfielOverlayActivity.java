package avans.glassy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfielOverlayActivity extends Activity {
    TextView descTxt;
    ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiel_overlay);
		
		findViewsById();
		 
        Intent intent = getIntent();
 
        ParcableDeelnemer parcelableDeelnemer = (ParcableDeelnemer) intent
                .getParcelableExtra("deelnemer");
        Deelnemer laptop = parcelableDeelnemer.getDeelnemer();
        display(laptop);
		//Toast.makeText(MijnWijkActivity.this, deelnemers.get(pos).getVoornaam() + " " + deelnemers.get(pos).getAchternaam(), Toast.LENGTH_SHORT).show();
	}
	
    private void findViewsById() {
        descTxt = (TextView) findViewById(R.id.desc);
        imageView = (ImageView) findViewById(R.id.icon);
    }
 
    private void display(Deelnemer deelnemer) {
        String desc = deelnemer.getVoornaam() + " " + deelnemer.getAchternaam();
        descTxt.setText(desc);
        imageView.setImageResource(deelnemer.getProfielFoto());
    }
}
