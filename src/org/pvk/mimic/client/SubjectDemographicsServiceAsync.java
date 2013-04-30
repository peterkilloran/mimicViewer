package org.pvk.mimic.client;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.pvk.mimic.client.SubjectDemographics;

public interface SubjectDemographicsServiceAsync {
	//fix StockPrice
	void getDemographics(int subjectID, AsyncCallback<SubjectDemographics> callback);
}
