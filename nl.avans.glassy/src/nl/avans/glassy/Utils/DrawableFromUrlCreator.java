package nl.avans.glassy.Utils;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DrawableFromUrlCreator extends AsyncTask<String, Void, Drawable> {
	
	public Drawable createDrawableFromUrl(URL url) {
		
		try
        {
            InputStream is = (InputStream) url.getContent();
            Drawable retval = Drawable.createFromStream(is, "src name");
            return retval;
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            return null;
        }
	}
	
	public Drawable createDrawableFromUrlString(String url) {
		
		try {
			
			return createDrawableFromUrl(new URL(url));
			
		} catch(Exception e) {
			
			e.printStackTrace();
			return null;
		}
			
	}

	@Override
	protected Drawable doInBackground(String... url) {
		
		return createDrawableFromUrlString(url[0]);
	}

}
