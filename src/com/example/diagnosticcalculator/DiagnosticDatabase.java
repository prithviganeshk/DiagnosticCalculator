package com.example.diagnosticcalculator;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiagnosticDatabase extends SQLiteOpenHelper {

	public DiagnosticDatabase(Context applicationcontext) {
		super(applicationcontext, "ChopDiagnostics", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("FUNCTION", "Inside create Database");
		String query = "CREATE TABLE ChopDiagnostics(diseasename TEXT, hint TEXT, "
				+ "testname TEXT, sensitivity REAL, specificity REAL, cost REAL,PRIMARY KEY(diseasename,testname))";
		db.execSQL(query);
		Log.d("DATABASE LOG", "table ChopDiagnostics created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS ChopDiagnostics";
		db.execSQL(query);
		onCreate(db);

	}
	
	public int numOfElements(){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount= db.rawQuery("select count(*) from ChopDiagnostics'", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		return count;

	}
	
	public void deleteEntries(){
	//	String query = "DELETE * FROM ChopDiagnostics";
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("ChopDiagnostics",null,null);
	}

	public void insertDiagnostics(
			HashMap<Objectdisease, ArrayList<Objecttest>> myData) {
		Log.v("FUNCTION", "Inside Insert");
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		ArrayList<Objecttest> testArray = new ArrayList<Objecttest>();

		for (Objectdisease dis : myData.keySet()) {
			testArray = myData.get(dis);
			Log.v("Disease INSERTED", dis.disease);
			for (int i = 0; i < testArray.size(); i++) {
				Log.v("TEST IN INSERT", testArray.get(i).testname);
				values.put("diseasename", dis.disease);
				values.put("hint", dis.hint);
				values.put("testname", testArray.get(i).testname);
				values.put("sensitivity", testArray.get(i).sensitivity);
				values.put("specificity", testArray.get(i).specificity);
				values.put("cost", testArray.get(i).cost);
		//		if (testArray.get(i).testname)
				database.insert("ChopDiagnostics", null, values);

			}
			testArray = new ArrayList<Objecttest>();
		}

		Log.v("FUNCTION", "AFTER FOR");

	}
/*	public boolean checkTuple(String disease, String test){
		String query = "SELECT DISTINCT diseasename from ChopDiagnostics";
	}*/

	public ArrayList<String> getDisease() {
		Log.v("FUNCTION", "inside getdisease");
		String query = "SELECT DISTINCT diseasename from ChopDiagnostics ORDER BY diseasename";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> result = new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				String disease = cursor.getString(0);
				result.add(disease);
			} while (cursor.moveToNext());
		}
		return result;
	}

	public ArrayList<String> getTest(String disease) {
		Log.v("FUNCTION", "inside get test");
		String query = "SELECT DISTINCT testname from ChopDiagnostics WHERE diseasename = '"
				+ disease + "' ORDER BY testname";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> result = new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				String test = cursor.getString(0);
				result.add(test);
				Log.v("TEST", test);
			} while (cursor.moveToNext());
		}
		return result;

	}

	public double getSensitivity(String testname, String disease) {
		Log.v("FUNCTION", "inside get SENSITIVITY");
		String query = "SELECT DISTINCT sensitivity from ChopDiagnostics WHERE testname = '"
				+ testname + "'" + " AND diseasename = '" + disease + "'";
		;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		double result = -1;
		double sensitivity;

		if (cursor.moveToFirst()) {
			do {
				sensitivity = Double.parseDouble(cursor.getString(0));
				result = sensitivity;
				Log.v("sensitivity database", Double.toString(result));
			} while (cursor.moveToNext());
		}
		return result;

	}

	public double getSpecificity(String test_name, String disease) {
		// TODO Auto-generated method stub
		Log.v("FUNCTION", "inside get SPECIFICITY");
		String query = "SELECT DISTINCT specificity from ChopDiagnostics WHERE testname = '"
				+ test_name + "'" + " AND diseasename = '" + disease + "'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		double result = -1;
		double specificity;

		if (cursor.moveToFirst()) {
			do {
				specificity = Double.parseDouble(cursor.getString(0));
				result = specificity;
				Log.v("specificity database", Double.toString(result));
			} while (cursor.moveToNext());
		}
		return result;
	}

	public String getHint(String disease) {
		Log.v("FUNCTION", "inside getHINT");
		String query = "SELECT DISTINCT hint from ChopDiagnostics WHERE diseasename= '"
				+ disease + "'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		String hint = null;

		if (cursor.moveToFirst()) {
			do {
				hint = cursor.getString(0);
				Log.v("HINT database", hint);
			} while (cursor.moveToNext());
		}
		return hint;
	}

	public int getCost(String test_name, String disease) {
		// TODO Auto-generated method stub
		Log.v("FUNCTION", "inside get COST");
		String query = "SELECT DISTINCT cost from ChopDiagnostics WHERE testname = '"
				+ test_name + "'" + " AND diseasename = '" + disease + "'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		int cost = -1;

		if (cursor.moveToFirst()) {
			do {
				cost = Integer.parseInt(cursor.getString(0));

				Log.v("specificity database", Integer.toString(cost));
			} while (cursor.moveToNext());
		}
		return cost;
	}

}
