package com.example.diagnosticcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends ActionBarActivity {

	public SeekBar slider;
	public TextView display_pretest;
	public TextView display_disease;
	public TextView display_test;
	public TextView display_expense;
	private double sensitivity;
	private double specificity;
	private double preTestProb;
	private Calculator calc;
	public TextView ppv;
	public TextView npv;
	private Button hintButton2;
	private String preTestHint2;
	private RatingBar rating2;
	final Context context = this;
	Toast rateToast;
	public double progressChanged = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent i = getIntent();
		hintButton2 = (Button) findViewById(R.id.button3);
		double[] values = i.getDoubleArrayExtra(MainActivity.EXTRA_MESSAGE);
		sensitivity = values[0];
		specificity = values[1];
		rating2 = (RatingBar) findViewById(R.id.ratingBar2);
		rating2.setIsIndicator(true);
		rating2.setRating(MainActivity.rate);
		display_pretest = (TextView) findViewById(R.id.textView1);
		display_disease = (TextView) findViewById(R.id.textView5);
		display_test = (TextView) findViewById(R.id.textView2);
		display_expense = (TextView) findViewById(R.id.textView6);
		ppv = (TextView) findViewById(R.id.textView3);
		npv = (TextView) findViewById(R.id.textView4);
		ppv.setVisibility(View.GONE);
		npv.setVisibility(View.GONE);
		display_pretest.setTextColor(Color.GRAY);
		display_pretest.setTypeface(null, Typeface.ITALIC);
		display_pretest.setTextSize(14);
		display_disease.setText("Disease: " + MainActivity.disease);
		display_test.setText("Diagnostic Test: " + MainActivity.test_name);

		rating2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				rateToast = Toast.makeText(getApplicationContext(),
						getRatingRange(rating2.getRating()),
						Toast.LENGTH_SHORT);
				rateToast.show();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						rateToast.cancel();
					}
				}, 300);
				rating2.setFocusable(true);
				return true;
			}
		});

		slider = (SeekBar) findViewById(R.id.seekBar1);
		calc = new Calculator();
		addSliderListener();

		hintButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						context);
				alertDialog.setTitle("Hint:");
				alertDialog.setMessage(MainActivity.preTestHint);
				alertDialog.create().show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
	
	public final String getRatingRange (float rate ){
		if (rate==1) return "$10-$100";
		else if (rate==2) return "$101-$500";
		else if (rate==3) return "$501-$1000";
		else return "more than $1000";
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addSliderListener() {
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				ppv.setVisibility(View.VISIBLE);
				npv.setVisibility(View.VISIBLE);
				display_pretest.setVisibility(View.VISIBLE);
				display_pretest.setTextColor(Color.BLACK);
				display_pretest.setTypeface(null, Typeface.NORMAL);
				display_pretest.setTextSize(16);
				ppv.setTextColor(Color.BLACK);
				npv.setTextColor(Color.BLACK);
				progressChanged = (double) progress / 100.00;
				display_pretest.setText("Pre-Test Probability is "
						+ String.valueOf(progressChanged));

				String inp = Double.toString((double) (progressChanged));
				try {
					preTestProb = Double.parseDouble(inp);
				} catch (Exception e) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Invalid Input", Toast.LENGTH_LONG);
					t.show();
					return;
				}
				double fPPV = calc.postTestPPV(sensitivity, specificity,
						preTestProb);
				double fNPV = calc.postTestNPV(sensitivity, specificity,
						preTestProb);
				double PPV = fPPV * 100;
				double NPV = (1-fNPV) * 100;
				String ppvString = String.format("%.2f", PPV);
				String npvString = String.format("%.2f", NPV);
				ppvString = "Positive : " + ppvString + "% ";
				npvString = "Negative : " + npvString + "% ";
				ppv.setText(ppvString);
				npv.setText(npvString);
				if (PPV >= 80) {
					ppv.setTextColor(Color.GREEN);
				}
				if (NPV >= 80) {
					npv.setTextColor(Color.GREEN);
				}

			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {

			}
			
		});

	}
}
