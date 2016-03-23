package com.example.diagnosticcalculator;

public class Calculator {
	private double TP;
	private double FP;
	private double TN;
	private double FN;
	final int total = 10000;

	public void calculate(double sens, double spec, double pre) {
		double totalDiseasePos = (pre * total);
		double totalDiseaseNeg = total - totalDiseasePos;
		TP = (sens * totalDiseasePos);
		FN = totalDiseasePos - TP;
		TN = (spec * totalDiseaseNeg);
		FP = totalDiseaseNeg - TN;

	}

	public double postTestPPV(double sens, double spec, double pre) {
		calculate(sens, spec, pre);
		return (TP / (TP + FP));

	}

	public double postTestNPV(double sens, double spec, double pre) {
		calculate(sens, spec, pre);
		return (TN / (TN + FN));

	}

	
	
}
