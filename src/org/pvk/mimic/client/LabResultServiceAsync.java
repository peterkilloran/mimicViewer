package org.pvk.mimic.client;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LabResultServiceAsync {
	void getLabResults(int subjectID, AsyncCallback< Vector<LabResult> > callback);
}
