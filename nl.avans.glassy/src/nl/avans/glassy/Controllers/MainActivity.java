package nl.avans.glassy.Controllers;

import nl.avans.glassy.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

public class MainActivity extends FragmentActivity {
	
	private Session.StatusCallback callback = new SessionStatusCallback();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
    	Session session = Session.getActiveSession();
    	if(session == null) {
    		
    		if(savedInstanceState != null) {
    			
    			session = Session.restoreSession(this, null, callback, savedInstanceState);
    			
    		} if(session == null) {
    			
    			session = new Session(this);
    		}
    		
    		Session.setActiveSession(session);
    		if(session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
    			
    			session.openForRead(new Session.OpenRequest(this).setCallback(callback));
    		}
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onStart() {
    	
        super.onStart();
        Session.getActiveSession().addCallback(callback);
    }

    @Override
    public void onStop() {
    	
        super.onStop();
        Session.getActiveSession().removeCallback(callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
    private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			Log.i("SessionStatusCallback", session.toString());
			Log.i("SessionStatusCallback", state.toString());
			
			if(exception != null) {
			
				exception.printStackTrace();
			}
		}
    	
    }
    
}
