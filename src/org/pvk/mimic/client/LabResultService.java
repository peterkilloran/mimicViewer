package org.pvk.mimic.client;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("labResults")
public interface LabResultService extends RemoteService {
	
	Vector<LabResult> getLabResults(int subjectID);
}
