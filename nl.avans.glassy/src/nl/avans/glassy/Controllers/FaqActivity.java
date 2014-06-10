package nl.avans.glassy.Controllers;

import java.util.ArrayList;

import nl.avans.glassy.R;
import nl.avans.glassy.Threads.Faq;
import nl.avans.glassy.Threads.Faq.faqListener;
import nl.avans.glassy.Views.FaqFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
