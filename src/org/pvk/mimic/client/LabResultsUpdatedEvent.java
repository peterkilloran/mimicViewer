package org.pvk.mimic.client;
import java.util.Vector;

import com.google.gwt.event.shared.GwtEvent;

public class LabResultsUpdatedEvent extends GwtEvent<LabResultsUpdatedEventHandler>{
	public static Type<LabResultsUpdatedEventHandler> TYPE = new Type<LabResultsUpdatedEventHandler>();
	private Vector<LabResult> vLabResults;
	
	public LabResultsUpdatedEvent( Vector<LabResult> vNewResults) {
		this.setvLabResults(vNewResults);
	}
	public Type<LabResultsUpdatedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	protected void dispatch(LabResultsUpdatedEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onLabResultsUpdated(this);
		
	}
	public Vector<LabResult> getvLabResults() {
		return vLabResults;
	}
	public void setvLabResults(Vector<LabResult> vLabResults) {
		this.vLabResults = vLabResults;
	}

}
