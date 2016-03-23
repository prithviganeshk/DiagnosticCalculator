package com.example.diagnosticcalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GetDatabase<testObject> extends AsyncTask<Context, Void, Void> {
	public DiagnosticDatabase database;
	@Override
	protected Void doInBackground(Context... params) {

		String line;
		StringBuffer buffer = new StringBuffer();
		InputStream inputstream;
		HttpURLConnection urlConnection = null;
		URL url;
		String jsonResponse = null;
		BufferedReader bufferedreader = null;
		try {
			url = new URL(
					"https://fling.seas.upenn.edu/~vaibhavn/cgi-bin/diagCalculator/index.php/api");
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			Log.v("connection", "set request success");
			urlConnection.connect();
			Log.v("connection", "connect success");
			inputstream = urlConnection.getInputStream();
			bufferedreader = new BufferedReader(new InputStreamReader(
					inputstream));
			if (inputstream == null)
				return null;

			while ((line = bufferedreader.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}

			if (buffer.length() == 0)
				return null;

			jsonResponse = buffer.toString();
			if (jsonResponse == null)
				Log.v("JSON", "it is null");
			else
				Log.v("JSON", jsonResponse);

		} catch (IOException e) {
			e.printStackTrace();
			Log.v("EXCEPTION", "DID I MAKE EXCEPTION IN IO?");
			jsonResponse = null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		DiagnosticDatabase database = new DiagnosticDatabase(params[0]);
		database.deleteEntries();
		try {
			database.insertDiagnostics(parseJSON(jsonResponse));
	/*		ArrayList<String> disease_list = new ArrayList<String>();
			disease_list = database.getDisease();

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(params[0],
					android.R.layout.simple_spinner_item, disease_list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s_disease.setAdapter(dataAdapter);
			*/

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	protected HashMap<Objectdisease, ArrayList<Objecttest>> parseJSON(
			String jsonResponse) throws JSONException {

		HashMap<Objectdisease, ArrayList<Objecttest>> myData = new HashMap<Objectdisease, ArrayList<Objecttest>>();

		String disease;
		String hint;
		String testname;
		double sensitivity = 0;
		double specificity = 0;
		double cost = 0;
		JSONArray data = new JSONArray(jsonResponse);
		JSONObject test;
		ArrayList<Objecttest> testList = new ArrayList<Objecttest>();

		for (int i = 0; i < data.length(); i++) {
			JSONObject value = (JSONObject) data.getJSONObject(i);
			// getting the disease name and hint from the current JSON object in
			// the JSON array
			disease = value.getString("disease_name");
			hint = value.getString("hint");

			Objectdisease tempDisease = new Objectdisease();
			tempDisease.disease = disease;
			tempDisease.hint = hint;
			int j = 0;
			Log.v("DISEASE:", disease);

			while (value.has(Integer.toString(j))) {

				test = value.getJSONObject(Integer.toString(j));
				testname = test.getString("name");
				Log.v("TEST:", testname);
				sensitivity = Double.parseDouble(test.getString("sensitivity"));
				specificity = Double.parseDouble(test.getString("specificity"));
				cost = Double.parseDouble(test.getString("cost"));
				Objecttest tempTest = new Objecttest();
				tempTest.testname = testname;
				tempTest.sensitivity = sensitivity;
				tempTest.specificity = specificity;
				tempTest.cost = cost;
				testList.add(tempTest);

				j++;
			}

			myData.put(tempDisease, testList);
			testList = new ArrayList<Objecttest>();
		}
		return myData;
	}

	
}