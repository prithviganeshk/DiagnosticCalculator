package com.example.diagnosticcalculator;

public class Test {
private String testName;
private double sensitivity;
private double specificity;
private int rating;

public Test(String testName, double sensitivity, double specificity, int rating) {
	this.testName = testName;
	this.sensitivity = sensitivity;
	this.specificity = specificity;
	this.rating = rating;
}
@Override
public String toString() {
	return this.testName;
}
public double getSensitivity() {
	return sensitivity;
}

public int getRating() {
	return rating;
}


public double getSpecificity() {
	return specificity;
}


}
