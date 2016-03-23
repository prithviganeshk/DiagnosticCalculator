package com.example.diagnosticcalculator.test;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.diagnosticcalculator.Calculator;
import com.example.diagnosticcalculator.MainActivity;

public class CalcTest extends ActivityInstrumentationTestCase2<MainActivity> {
	public CalcTest(){
		super("com.example.diagnosticcalculator",MainActivity.class);
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
}
