package nl.avans.glassy.Models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import nl.avans.glassy.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Deelnemer> deelnemers;

	public ImageAdapter(Context c, ArrayList<Deelnemer> deelnemers) {
		mContext = c;
		this.deelnemers = deelnemers;
	}

	public int getCount() {
		return deelnemers.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(92, 92));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (ImageView) convertView;
		}
		Deelnemer deelnemer = deelnemers.get(position);

		imageView.setImageResource(R.drawable.profile1);
		if (deelnemer.getFotoLink() != "null") {
			new DownloadImageTask(imageView).execute(deelnemer.getFotoLink());
		}
		return imageView;
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
				options.inSampleSize = 8;
				Bitmap preview_bitmap = BitmapFactory.decodeStream(input, null,
						options);
				preview_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
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