package org.pvk.mimic.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

public class MimicData implements HasHandlers {

	private HandlerManager handlerManager;
	private int ID;
	private SubjectDemographics demographics; 
	private SubjectDemographicsServiceAsync subjectDemographicsSvc;
	private PatientNoteServiceAsync patientNoteSvc;
	private LabResultServiceAsync labResultSvc;
	public LabResultServiceAsync getLabResultSvc() {
		return labResultSvc;
	}

	public void setLabResultSvc(LabResultServiceAsync labResultSvc) {
		this.labResultSvc = labResultSvc;
	}

	private Vector<PatientNote> patientNotes;
	private HashMap<String, PatientNote> hNotes;
	private HashMap<String, LabResult> hLabs;
	private HashMap<String, HashMap<Integer,LabResult>> hLabResultTimeVector ;
	private HashMap<String, Vector<LabResult>> hLabLoincResults ;
	private HashMap<String, LabResult> hLabTypes ;
	private ArrayList<String> labLoincCodes ;
	private ArrayList<String> labEventTimes ;
	
	public HashMap<String, Vector<LabResult>> gethLabLoincResults() {
		return hLabLoincResults;
	}

	public void sethLabLoincResults(
			HashMap<String, Vector<LabResult>> hLabLoincResults) {
		this.hLabLoincResults = hLabLoincResults;
	}

	public ArrayList<String> getLabLoincCodes() {
		return labLoincCodes;
	}

	public void setLabLoincCodes(ArrayList<String> labLoincCodes) {
		this.labLoincCodes = labLoincCodes;
	}

	public ArrayList<String> getLabEventTimes() {
		return labEventTimes;
	}

	public void setLabEventTimes(ArrayList<String> labEventTimes) {
		this.labEventTimes = labEventTimes;
	}

	private HashMap<Integer, String> hLabEventTimes ;

	private Vector<LabResult> labResults ;

	private Date minLabDate;
	private Date maxLabDate;

	public MimicData (int ptID) {
		this.ID=ptID;		
		subjectDemographicsSvc = GWT.create(SubjectDemographicsService.class);
		patientNoteSvc = GWT.create(PatientNoteService.class);
		labResultSvc = GWT.create(LabResultService.class);
		handlerManager = new HandlerManager(this);
	}

	public void pullLabs() {
		// Set up the callback object.
		AsyncCallback< Vector<LabResult> > callback = new AsyncCallback< Vector<LabResult> >() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Vector<LabResult> result) {
				labResults = result;
				labEventTimes = new ArrayList<String>();
				labLoincCodes = new ArrayList<String>();
				hLabLoincResults = new HashMap<String, Vector<LabResult>>();
				for (int i=0;i<result.size();i++) {

					if (!labEventTimes.contains(result.get(i).getChartTime())) {
						labEventTimes.add(result.get(i).getChartTime());

					} 

					if (!labLoincCodes.contains(result.get(i).getLoincCode())) {
						labLoincCodes.add(result.get(i).getLoincCode());
					} 

					if (hLabLoincResults.containsKey(result.get(i).getLoincCode())) {
						Vector<LabResult> hTimeItems = hLabLoincResults.get(result.get(i).getLoincCode());
						hTimeItems.add(result.get(i));
						hLabLoincResults.put(result.get(i).getLoincCode(), hTimeItems);
					} else {
						Vector<LabResult> hTimeItems = new Vector<LabResult>();
						hTimeItems.add(result.get(i));
						hLabLoincResults.put(result.get(i).getLoincCode(), hTimeItems);
					}
				}
				Collections.sort(labEventTimes);
				DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss");
				setMinLabDate(df.parse(labEventTimes.get(0)));
				setMaxLabDate(df.parse(labEventTimes.get(labEventTimes.size()-1)));
				LabResultsUpdatedEvent e = new LabResultsUpdatedEvent(labResults);	        	  
				fireEvent(e);



			}};
			// Make the call to the lab service.

			labResultSvc.getLabResults(this.ID, callback);
	}

	public void pullPatientDemographics () {
		demographics = new SubjectDemographics();
		AsyncCallback<SubjectDemographics> callback = new AsyncCallback<SubjectDemographics>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(SubjectDemographics result) {
				demographics = result;
				DemographicsUpdatedEvent e = new DemographicsUpdatedEvent(demographics);	        	  
				fireEvent(e);
			}
		};

		subjectDemographicsSvc.getDemographics(this.ID, callback);

	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// TODO Auto-generated method stub
		handlerManager.fireEvent(event);
	}
	
	public HandlerRegistration addDemographicsUpdatedEventHandler(
			DemographicsUpdatedEventHandler handler) {
		return handlerManager.addHandler(DemographicsUpdatedEvent.TYPE, handler);
	}
	
	public HandlerRegistration addLabResultsUpdatedEventHandler(
			LabResultsUpdatedEventHandler handler) {
		return handlerManager.addHandler(LabResultsUpdatedEvent.TYPE, handler);
	}

	public Date getMinLabDate() {
		return minLabDate;
	}

	public void setMinLabDate(Date minLabDate) {
		this.minLabDate = minLabDate;
	}

	public Date getMaxLabDate() {
		return maxLabDate;
	}

	public void setMaxLabDate(Date maxLabDate) {
		this.maxLabDate = maxLabDate;
	}
}
