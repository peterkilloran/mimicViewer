package org.pvk.mimic.client;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
public interface PatientNoteServiceAsync {
	void getNotes(int subjectID, AsyncCallback< Vector<PatientNote> > callback);
}
