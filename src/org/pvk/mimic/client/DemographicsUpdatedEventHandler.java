package org.pvk.mimic.client;
import com.google.gwt.event.shared.EventHandler;

public interface DemographicsUpdatedEventHandler extends EventHandler {
	void onDemographicsUpdated(DemographicsUpdatedEvent event);
}
