package nl.avans.glassy.Controllers;

import java.util.ArrayList;

import org.json.JSONObject;

import nl.avans.glassy.R;
import nl.avans.glassy.R.id;
import nl.avans.glassy.R.layout;
import nl.avans.glassy.R.menu;
import nl.avans.glassy.Models.Faq;
import nl.avans.glassy.Models.Faq.faqListener;
import nl.avans.glassy.Utils.ApiCommunicator;
import nl.avans.glassy.Views.FaqFragment;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class FaqActivity extends FragmentActivity implements faqListener {
	private Faq faq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new FaqFragment()).commit();
		}
		loadFaqInfo();
	}

	private void loadFaqInfo()
	{
		Context context = getApplicationContext();
		Faq.loadFaq(context, this);	
	}

	@Override
	public void onFaqLoaded(ArrayList<String> questions, ArrayList<String> answers) {
		FaqFragment fragment = (FaqFragment) getFragmentManager().findFragmentById(R.id.faqfragment);
		fragment.updateText(questions, answers);		
	}


}
