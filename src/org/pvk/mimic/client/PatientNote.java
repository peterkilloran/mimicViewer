package org.pvk.mimic.client;
import java.io.Serializable;

public class PatientNote implements Serializable {
	private int subjectID;
	private int intID;
	private String categoryDescription;
	private String title;
	private String text;
	private String chartTime;
	
	public PatientNote() {}
	
	public void setIntID(int intID) {
		this.intID=intID;
	}
	
	public void setSubjectID(int subjectID) {
		this.subjectID=subjectID;
	}
	
	
	public void setCategoryDescription(String description) {
		this.categoryDescription=description;
	}
	
	public void setTitle(String title) {
		this.title=title;
	}

	public void setText(String text) {
		this.text=text;
	}

	public void setChartTime(String chartTime) {
		this.chartTime=chartTime;
	}
	
	public int getIntID () {
		return this.intID;
	}
	
	public int getSubjectID () {
		return this.subjectID;
	}
	
	public String getCategoryDescription () {
		return this.categoryDescription;
	}
	
	public String getTitle() {
		return this.title;		
	}
	public String getText() {
		return this.text;
	}
	public String getChartTime() {
		return this.chartTime;
	}

}
