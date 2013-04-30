package org.pvk.mimic.client;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("patientNotes")
public interface PatientNoteService extends RemoteService {
	 Vector<PatientNote> getNotes(int subjectID);
}
