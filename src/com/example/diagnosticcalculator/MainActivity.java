//new revision
package com.example.diagnosticcalculator;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	final Context context = this;
	private Spinner s_disease;
	private Spinner s_test;
	public static String disease;
	private double sensitivity;
	private double specificity;
	private Button dummy;
	public static String preTestHint;
	public static RatingBar rating;
	public final static String EXTRA_MESSAGE = "com.example.diagnosticcalculator.MESSAGE";
	private Button hintButton;
	public static String test_name;
	public static int rate;
	Calculator calc = new Calculator();
	DiagnosticDatabase database = new DiagnosticDatabase(this);
	Toast rateToast;
	Toast internetConnection;
	GetDatabase<?> getData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dummy = (Button) findViewById(R.id.button1);
		hintButton = (Button) findViewById(R.id.button2);
		rating = (RatingBar) findViewById(R.id.ratingBar1);
		rating.setIsIndicator(true);
		s_disease = (Spinner) findViewById(R.id.spinner1);
		s_test = (Spinner) findViewById(R.id.spinner2);
		// Add spinner data
		if (isConnected()) {
			GetDatabase<?> getData = new GetDatabase<Object>();
/*				internetConnection = Toast.makeText(getApplicationContext(),
					"loading data...", Toast.LENGTH_SHORT);
			internetConnection.show();
*/				getData.execute(this);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		ArrayList<String> disease_list = new ArrayList<String>();
		disease_list = database.getDisease();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, disease_list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_disease.setAdapter(dataAdapter);

		addDiseaseListener();
		addTestListener();

		hintButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						context);
				alertDialog.setTitle("Hint:");
				alertDialog.setMessage(preTestHint);
				alertDialog.create().show();

			}
		});

		dummy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ResultActivity.class);
				double[] values = new double[2];
				values[0] = sensitivity;
				values[1] = specificity;
				intent.putExtra(EXTRA_MESSAGE, values);
				startActivity(intent);
			}
		});

		rating.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(rateToast!=null){
					rateToast.cancel();
				}
				rateToast = Toast.makeText(getApplicationContext(),
						getRatingRange(rating.getRating()),
//						database.getCost(test_name, disease),
						Toast.LENGTH_SHORT);
				rateToast.show();
				
				rating.setFocusable(true);
				return true;
			}
		});
	}

	public void addTestListener() {
		s_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				test_name = (String) parent.getItemAtPosition(pos);
				sensitivity = (database.getSensitivity(test_name, disease)) / 100.00;
				specificity = (database.getSpecificity(test_name, disease)) / 100.00;
				rate = database.getCost(test_name, disease);
				preTestHint = database.getHint(disease);
				rating.setRating(rate);
				Log.v("in main activity sens", Double.toString(sensitivity));
				Log.v("in main activity specificity",
						Double.toString(specificity));

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	public void addDiseaseListener() {

		s_disease.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				disease = String.valueOf(s_disease.getSelectedItem());
				ArrayList<String> testName = database.getTest(disease);
				ArrayAdapter<String> testAdapter = new ArrayAdapter<String>(
						MainActivity.this,
						android.R.layout.simple_spinner_item, testName);
				testAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				testAdapter.notifyDataSetChanged();
				s_test.setAdapter(testAdapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.sync: {
			if (isConnected()) {
				GetDatabase<?> getData = new GetDatabase<Object>();
/*				internetConnection = Toast.makeText(getApplicationContext(),
						"loading data...", Toast.LENGTH_SHORT);
				internetConnection.show();
*/				getData.execute(this);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Fill the disease_list
				ArrayList<String> disease_list = new ArrayList<String>();
				disease_list = database.getDisease();

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, disease_list);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				s_disease.setAdapter(dataAdapter);
				internetConnection = Toast.makeText(getApplicationContext(),
				"Sync finished", Toast.LENGTH_SHORT);
				internetConnection.show();

			} else {
				internetConnection = Toast.makeText(getApplicationContext(),
						"No internet connection available", Toast.LENGTH_SHORT);
				internetConnection.show();
			}
			break;
		}

		}

		return super.onOptionsItemSelected(item);
	}

	private boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}
	
	public final String getRatingRange (float rate ){
		if (rate==1) return "$10-$100";
		else if (rate==2) return "$101-$500";
		else if (rate==3) return "$501-$1000";
		else return "more than $1000";
		
	}
}