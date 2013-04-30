package org.pvk.mimic.client;
import java.io.Serializable;

public class SubjectDemographics implements Serializable{
	  private int subjectID;
	  private String maritalStatusDescription;
	  
	  public SubjectDemographics() {}
	  public SubjectDemographics(int subjectID, String maritalStatusDescription) {
		  this.subjectID=subjectID;
		  this.maritalStatusDescription=maritalStatusDescription;
	  }
	  
	  public int getSubjectID () {
		    return this.subjectID;
		  }

	  public String getMaritalStatusDescription() {
		    return this.maritalStatusDescription;
		  }

	  public void setSubjectID (int subjectID) {
		  this.subjectID=subjectID;
	  }

	  public void setMaritalStatusDescription (String maritalStatusDescription) {
		  this.maritalStatusDescription=maritalStatusDescription;
	  }

}
