package org.pvk.mimic.client;

import com.google.gwt.event.shared.GwtEvent;

public class DemographicsUpdatedEvent extends GwtEvent<DemographicsUpdatedEventHandler> {
	
	
	public static Type<DemographicsUpdatedEventHandler> TYPE = new Type<DemographicsUpdatedEventHandler>();
	
	private SubjectDemographics demographics;
	
	public SubjectDemographics getDemographics() {
		return demographics;
	}

	public void setDemographics(SubjectDemographics demographics) {
		this.demographics = demographics;
	}
	

	public DemographicsUpdatedEvent(SubjectDemographics newDemographics) {
		this.demographics= newDemographics;
	}
	
	@Override
	public Type<DemographicsUpdatedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(DemographicsUpdatedEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onDemographicsUpdated(this);
		
	}



}
