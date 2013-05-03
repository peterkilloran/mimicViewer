package org.pvk.mimic.client;

import com.google.gwt.event.shared.GwtEvent;

public class LabSelectionUpdatedEvent extends GwtEvent<LabSelectionUpdatedEventHandler> {
	
	public static Type<LabSelectionUpdatedEventHandler> TYPE = new Type<LabSelectionUpdatedEventHandler>();

	private String LoincID;
	private String SelectionType;
	
	
	public LabSelectionUpdatedEvent(String loinc) {
		this.setLoincID(loinc);
	}
	
	@Override	
	public Type<LabSelectionUpdatedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(LabSelectionUpdatedEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onLabSelectionUpdated(this);
	}

	public String getLoincID() {
		return LoincID;
	}

	public void setLoincID(String loincID) {
		LoincID = loincID;
	}

	public String getSelectionType() {
		return SelectionType;
	}

	public void setSelectionType(String selectionType) {
		SelectionType = selectionType;
	}

}
