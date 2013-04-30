package org.pvk.mimic.client;
import java.io.Serializable;

public class LabResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String chartTime;
	private String sValue;
	private String flag;
	private String valueUnits;
	private String testName;
	private String fluid;
	private String category;
	private String loincCode;
	private String loincDescription;
	
	public void setChartTime(String chartTime) {
		this.chartTime = chartTime;
	}
	public String getChartTime() {
		return chartTime;
	}
	public void setValue(String value) {
		this.sValue = value;
	}
	public String getValue() {
		return sValue;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFlag() {
		return flag;
	}
	public void setValueUnits(String valueUnits) {
		this.valueUnits = valueUnits;
	}
	public String getValueUnits() {
		return valueUnits;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestName() {
		return testName;
	}
	public void setFluid(String fluid) {
		this.fluid = fluid;
	}
	public String getFluid() {
		return fluid;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}
	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}
	public String getLoincCode() {
		return loincCode;
	}
	public void setLoincDescription(String loincDescription) {
		this.loincDescription = loincDescription;
	}
	public String getLoincDescription() {
		return loincDescription;
	}
	
	
	
}
