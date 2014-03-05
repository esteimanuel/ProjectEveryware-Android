package avans.glassy;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {
	
	Button done;
	EditText name;
	EditText password;
	
	String matchingName = "admin";
	String matchingPassword = "admin";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		addListenerOnButton();
	}
	
	private void addListenerOnButton() {
		done = (Button) findViewById(R.id.buttonDone);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				standardLoginValidate();
			}

		});

	}
	private void standardLoginValidate() {
		name   = (EditText)findViewById(R.id.editName);
		password  = (EditText)findViewById(R.id.editPassword);
		
		String currentName = name.getText().toString();
		String currentPassword = password.getText().toString();
		if(currentName.equals(matchingName) && currentPassword.equals(matchingPassword))
		{
		Intent myIntent = new Intent(LoginActivity.this, Main.class);				
		LoginActivity.this.startActivity(myIntent);
		}
		else {
			Toast.makeText(getApplicationContext(), "Foute username/wachtwoord, probeer admin/admin eens", Toast.LENGTH_LONG).show();
		}
	}
	

}
