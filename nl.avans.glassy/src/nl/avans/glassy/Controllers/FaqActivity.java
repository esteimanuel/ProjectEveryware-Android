package nl.avans.glassy.Controllers;

import java.util.ArrayList;

import nl.avans.glassy.R;
import nl.avans.glassy.Models.FaqInfo;
import nl.avans.glassy.Threads.Faq;
import nl.avans.glassy.Threads.Faq.faqListener;
import nl.avans.glassy.Views.FaqFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class FaqActivity extends FragmentActivity implements faqListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FaqFragment()).commit();
		}
		loadFaqInfo();
	}

	private void loadFaqInfo() {
		if(FaqInfo.answers.isEmpty() && FaqInfo.questions.isEmpty())
		{
		Context context = getApplicationContext();
		Faq.loadFaq(context, this);
		} else {
			FaqFragment fragment = (FaqFragment) getFragmentManager()
					.findFragmentById(R.id.faqfragment);
			fragment.updateText(FaqInfo.questions, FaqInfo.answers);
		}
	}

	@Override
	public void onFaqLoaded(ArrayList<String> questions,
			ArrayList<String> answers) {
		FaqFragment fragment = (FaqFragment) getFragmentManager()
				.findFragmentById(R.id.faqfragment);
		fragment.updateText(questions, answers);
	}
	
	

}
