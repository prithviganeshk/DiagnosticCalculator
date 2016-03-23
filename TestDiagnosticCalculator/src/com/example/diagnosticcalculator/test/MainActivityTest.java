package com.example.diagnosticcalculator.test;
import org.junit.Test;

import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.diagnosticcalculator.Calculator;
import com.example.diagnosticcalculator.MainActivity;
import com.example.diagnosticcalculator.ResultActivity;
import com.robotium.solo.Solo;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	public Solo solo;
	public MainActivityTest() {
		super("com.example.diagnosticcalculator", MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUp() throws Exception {

		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	@Test
	public void testPostTestPPV() {
		Calculator c = new Calculator();
		assertEquals(0.0,c.postTestPPV(0, 0, 0.5));
		assertEquals(1.1/100,c.postTestPPV(0.93, 0.57, 0.005),0.001);
		assertEquals(35.0/100,c.postTestPPV(0.93, 0.57, 0.2),0.001);		
	}
	
	@Test
	public void testPostTestNPV() {
		Calculator c = new Calculator();
		assertEquals(0.0,c.postTestNPV(0, 0, 0.5));
		assertEquals(99.9/100,c.postTestNPV(0.93, 0.57, 0.005),0.001);
		assertEquals(97.0/100,c.postTestNPV(0.93, 0.57, 0.2),0.001);
		
	}

	@Test
	public void testHintButton() {
		solo.waitForActivity("MainActivity");
		solo.clickOnButton(0);
		assertTrue(solo.searchText("children ages 3 months to 36"));
		solo.finishOpenedActivities();

	}
	
	@Test
	public void testDollar() {
		solo.waitForActivity("MainActivity");
		MainActivity mainactivity = getActivity();
		final RatingBar dollars = (RatingBar) mainactivity.findViewById(com.example.diagnosticcalculator.R.id.ratingBar1);		
		solo.clickOnView(dollars);	//Click on the dollars symbol
		solo.waitForText("$10-$100");

	}
	
	@Test
	public void testDropDownDisease0() {
		Solo solo = new Solo(getInstrumentation(), getActivity());
		assertTrue(solo.searchText("Occult"));
		solo.pressSpinnerItem(0, 0);
		assertTrue(solo.isSpinnerTextSelected(0, "Occult Bacteremia"));
	}
	
	@Test
	public void testDropDownDisease1() {
		solo.waitForActivity("MainActivity");
		assertTrue(solo.searchText("Occult"));
		solo.pressSpinnerItem(0, 1); 	//Selecting Urinary Tract Infection
		assertTrue(solo.isSpinnerTextSelected(0, "Urinary Tract Infection"));		
	}

	@Test
	public void testDropDownTest0() {
		solo.waitForActivity("MainActivity");
		assertTrue(solo.searchText("CRP > 4.4"));
		solo.pressSpinnerItem(1, 0); 	//Selecting Urinary Tract Infection
		assertTrue(solo.isSpinnerTextSelected(1, "CRP > 4.4 mg/dL"));		
	}

	@Test
	public void testDropDownTest2() {
		solo.waitForActivity("MainActivity");
		assertTrue(solo.searchText("CRP > 4.4"));
		solo.pressSpinnerItem(0, 2); 	//Selecting Appendicitis
		assertTrue(solo.isSpinnerTextSelected(0, "Appendicitis"));
		solo.pressSpinnerItem(1, 2); 	//Selecting Abdominal UltraSound		
		assertTrue(solo.isSpinnerTextSelected(1, "Abdominal UltraSound"));				
	}

	@Test
	public void testDropDownTest2_Rating() {
		solo.waitForActivity("MainActivity");
		assertTrue(solo.searchText("CRP > 4.4"));
		solo.pressSpinnerItem(0, 2); 	//Selecting Appendicitis
		assertTrue(solo.isSpinnerTextSelected(0, "Appendicitis"));
		solo.pressSpinnerItem(1, 2); 	//Selecting Abdominal UltraSound		
		assertTrue(solo.isSpinnerTextSelected(1, "Abdominal UltraSound"));				
		
		MainActivity mainactivity = getActivity();
		final RatingBar dollars = (RatingBar) mainactivity.findViewById(com.example.diagnosticcalculator.R.id.ratingBar1);		
		solo.clickOnView(dollars);	//Click on the dollars symbol
		solo.waitForText("$101-$500");
								
		solo.clickOnButton(0);	// Click on hint
		solo.searchText("Children with fever > 38");
		solo.clickOnScreen(100, 100);
	}
	
	
	@Test
	public void testResultsButton(){
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ResultActivity.class.getName(), null, false);

		solo.clickOnButton(1);		
		getInstrumentation().waitForIdleSync();
		ResultActivity resultactivity = (ResultActivity) activityMonitor
				.waitForActivityWithTimeout(10000);
		assertTrue(solo.searchText("Slide the bar to change"));		
	}
	
	@Test
	public void testHintsResultsPage(){
		solo.clickOnButton(1);
		solo.waitForActivity(ResultActivity.class);
		
		solo.clickOnButton(0);	// Click on hint
		solo.searchText("3 months to 36 months with fever");
		solo.clickOnScreen(100, 100);
		
	}

	@Test
	public void testDollarResultsPage(){
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ResultActivity.class.getName(), null, false);
		solo.clickOnButton(1);		
		getInstrumentation().waitForIdleSync();

		ResultActivity ra = (ResultActivity) activityMonitor
				.waitForActivityWithTimeout(10000);
		assertTrue(solo.searchText("Slide the bar to change"));
		
		final RatingBar dollars = (RatingBar) ra.findViewById(com.example.diagnosticcalculator.R.id.ratingBar2);		
		solo.clickOnView(dollars);	//Click on the dollars symbol
		getInstrumentation().waitForIdleSync();
		solo.searchText("$10-$100");

	}

	@Test
	public void testDisease2ResultsPage(){
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ResultActivity.class.getName(), null, false);

		solo.pressSpinnerItem(0, 0); 	//Selecting Occult Bacteremia
		assertTrue(solo.isSpinnerTextSelected(0, "Occult Bacteremia"));
		solo.pressSpinnerItem(1, 1); 	//Selecting Procalcitonin..
		assertTrue(solo.isSpinnerTextSelected(1, "Procalcitonin > 0.3 ng/mL"));
		
		solo.clickOnButton(1);		
		getInstrumentation().waitForIdleSync();

		ResultActivity ra = (ResultActivity) activityMonitor
				.waitForActivityWithTimeout(10000);
				
		final RatingBar dollars = (RatingBar) ra.findViewById(com.example.diagnosticcalculator.R.id.ratingBar2);		
		solo.clickOnView(dollars);	//Click on the dollars symbol
		solo.waitForText("$101-$500");

		solo.clickOnButton(0); //Click on hint
		assertTrue(solo.searchText("3 months to 36 months with fever"));
	}

	@Test
	public void testSeekBar(){
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ResultActivity.class.getName(), null, false);

		solo.pressSpinnerItem(0, 0); 	//Selecting Occult Bacteremia
		assertTrue(solo.isSpinnerTextSelected(0, "Occult Bacteremia"));
		solo.pressSpinnerItem(1, 1); 	//Selecting Procalcitonin..
		assertTrue(solo.isSpinnerTextSelected(1, "Procalcitonin > 0.3 ng/mL"));
		
		solo.clickOnButton(1);		
		getInstrumentation().waitForIdleSync();

		ResultActivity ra = (ResultActivity) activityMonitor
				.waitForActivityWithTimeout(10000);
		
		solo.setProgressBar(0, 50);
		assertTrue(solo.searchText("Pre-Test Probability is 0.5"));		
		assertTrue(solo.searchText("Positive : 0.72"));
		assertTrue(solo.searchText("Negative : 0.90"));
		
	}
	
	@Override
	public void tearDown(){
		solo.finishOpenedActivities();
	}
}
