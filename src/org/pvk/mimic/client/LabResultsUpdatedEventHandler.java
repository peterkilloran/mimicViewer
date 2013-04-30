package org.pvk.mimic.client;
import com.google.gwt.event.shared.EventHandler;
public interface LabResultsUpdatedEventHandler extends EventHandler {
	void onLabResultsUpdated(LabResultsUpdatedEvent event);
}
