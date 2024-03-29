package nl.avans.glassy.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewHelveticaThin extends TextView {
	private String font = "Fonts/HelveticaNeue_Thin.ttf";

	public TextViewHelveticaThin(Context context) {
		super(context);
		Typeface face = Typeface.createFromAsset(context.getAssets(), font); 
		this.setTypeface(face); 
	}

	public TextViewHelveticaThin(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface face = Typeface.createFromAsset(context.getAssets(), font); 
		this.setTypeface(face); 
	}

	public TextViewHelveticaThin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface face = Typeface.createFromAsset(context.getAssets(), font); 
		this.setTypeface(face); 
	}

	protected void onDraw (Canvas canvas) {
		super.onDraw(canvas);
	}
}
