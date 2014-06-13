package nl.avans.glassy.Controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.Deelnemer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuddyProfielActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddyprofiel_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		Bundle b = getIntent().getExtras();
		Deelnemer buddy = b.getParcelable("nl.avans.glassy.Models.Deelnemer");
		setData(buddy);
	}

	private void setData(Deelnemer buddy) {
		TextView buddyNaam = (TextView) findViewById(R.id.buddyNaam);
		String naam = "";
		if (buddy.getVoornaam() != null) {
			naam += buddy.getVoornaam();
		}

		if (buddy.getTussenvoegsel() != null) {
			naam += " " + buddy.getTussenvoegsel();
		}
		if (buddy.getAchternaam() != null) {
			naam += " " + buddy.getAchternaam();
		}
		buddyNaam.setText(naam);

		TextView buddyTijd = (TextView) findViewById(R.id.buddyTijd);
		if (buddy.getBuddy().getTijdVanaf() != null
				&& buddy.getBuddy().getTijdTot() != null) {
			String tijd = "Beschikbaar: " + buddy.getBuddy().getTijdVanaf()
					+ " Tot " + buddy.getBuddy().getTijdTot();
			buddyTijd.setText(tijd);
			buddyTijd.setVisibility((View.VISIBLE));
		}

		TextView buddyLocatie = (TextView) findViewById(R.id.buddyLocatie);
		if (buddy.getBuddy().getLocatie() != null) {
			buddyLocatie.setText(buddy.getBuddy().getLocatie());
			buddyLocatie.setVisibility((View.VISIBLE));
		}

		TextView buddyTel = (TextView) findViewById(R.id.buddyTel);
		if (buddy.getBuddy().getContactTel() != null) {
			buddyTel.setText("Telefoon: " + buddy.getBuddy().getContactTel());
			buddyTel.setVisibility((View.VISIBLE));
		}

		TextView buddyMail = (TextView) findViewById(R.id.buddyMail);
		if (buddy.getBuddy().getContactEmail() != null) {
			buddyMail.setText("Email: " + buddy.getBuddy().getContactEmail());
			buddyMail.setVisibility((View.VISIBLE));
		}

		if (buddy.getFotoLink() != null) {
			new DownloadImageTask((ImageView) findViewById(R.id.profielFoto))
					.execute(buddy.getFotoLink());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
			Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), config);
			Canvas canvas = new Canvas(convertedBitmap);
			Paint paint = new Paint();
			canvas.drawBitmap(bitmap, 0, 0, paint);
			return convertedBitmap;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			URL imageUrl = null;
			try {
				imageUrl = new URL(urldisplay);
			} catch (MalformedURLException e1) {
				Log.d("DownloadImageTask", "Cannot parse URL");
				e1.printStackTrace();
			}
			Bitmap mIcon11 = null;
			try {
				URLConnection connection = imageUrl.openConnection();
				InputStream input = connection.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize = 8;
				Bitmap preview_bitmap = BitmapFactory.decodeStream(input, null,
						options);
				preview_bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
				mIcon11 = BitmapFactory.decodeStream(new ByteArrayInputStream(
						out.toByteArray()));
				preview_bitmap.recycle();
				mIcon11 = convert(mIcon11, Bitmap.Config.RGB_565);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				bmImage.setImageBitmap(result);
				bmImage = null;
			}
		}
	}
}
