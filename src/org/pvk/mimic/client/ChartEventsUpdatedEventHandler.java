package org.pvk.mimic.client;

import com.google.gwt.event.shared.EventHandler;

public interface ChartEventsUpdatedEventHandler extends EventHandler {
	void onChartEventsUpdated(ChartEventsUpdatedEvent event);
}
