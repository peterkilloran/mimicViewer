package org.pvk.mimic.client;

import java.util.HashMap;

public class MimicDataUtils {
	private  HashMap<String,String> hCbcPanel;
	private  HashMap<String,String> hChemPanel;
	private  HashMap<String,String> hAbgPanel;
	
	
	public MimicDataUtils () {
		Initialize();
	}
	private void Initialize () {
		hCbcPanel = new HashMap<String,String>();
		hCbcPanel.put("26464-8", "WBC");
		hCbcPanel.put("20570-8", "HCT");
		hCbcPanel.put("718-7", "HGB");
		hCbcPanel.put("26515-7", "PLT COUNT");

		
		hChemPanel = new HashMap<String,String>();
		hChemPanel.put("2951-2", "SODIUM");
		hChemPanel.put("2823-3", "POTASSIUM");
		hChemPanel.put("2069-3", "CHLORIDE");
		hChemPanel.put("1963-8", "TOTAL CO2");
		hChemPanel.put("3094-0", "UREA N");
		hChemPanel.put("2160-0", "CREAT");
		hChemPanel.put("2345-7", "GLUCOSE");
		hChemPanel.put("17861-6", "CALCIUM");
		hChemPanel.put("19123-9", "MAGNESIUM");
		hChemPanel.put("2777-1", "PHOSPHATE");

		hAbgPanel = new HashMap<String,String>();
		hAbgPanel.put("11558-4","pH");
		hAbgPanel.put("11557-6","pCO2");
		hAbgPanel.put("11556-8","pO2");
		hAbgPanel.put("11555-0","BASE XS");
		hAbgPanel.put("1959-6","TOTAL CO2");
		hAbgPanel.put("19991-9","AADO2");
		hAbgPanel.put("32693-4","LACTATE");
		hAbgPanel.put("2947-0","Na+");
		hAbgPanel.put("6298-4","K+");
		hAbgPanel.put("2069-3","CL-");
		hAbgPanel.put("2339-0","GLUCOSE");
		hAbgPanel.put("20570-8","calcHCT");
		hAbgPanel.put("718-7","HGB");
		hAbgPanel.put("20563-3","CARBOXYHB");
		hAbgPanel.put("2614-6","MET HGB");
		hAbgPanel.put("1994-3","freeCa");
		
		//??Ventilator stuff
		hAbgPanel.put("19994-3","O2");
		hAbgPanel.put("3151-8","O2 Flow");
		hAbgPanel.put("20564-1","O2 Sat");
		hAbgPanel.put("20077-4","PEEP");
		hAbgPanel.put("20112-9","TIDAL VOL");

	}
	public  HashMap<String, String> gethCbcPanel() {
		return hCbcPanel;
	}

	public  HashMap<String, String> gethChemPanel() {
		return hChemPanel;
	}

	public  HashMap<String, String> gethAbgPanel() {
		return hAbgPanel;
	}
		
}
