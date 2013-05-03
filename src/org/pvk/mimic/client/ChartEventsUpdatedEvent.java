package org.pvk.mimic.client;

import java.util.Vector;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class ChartEventsUpdatedEvent extends GwtEvent<ChartEventsUpdatedEventHandler>{
	public static Type<ChartEventsUpdatedEventHandler> TYPE = new Type<ChartEventsUpdatedEventHandler>();
	private Vector<ChartEvent> chartEvents;
	private String chartEventType;
	
	
	public ChartEventsUpdatedEvent( Vector<ChartEvent> vNewEvents) {
		this.setChartEvents(vNewEvents);
	}
	@Override
	public Type<ChartEventsUpdatedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(ChartEventsUpdatedEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onChartEventsUpdated(this);
	}
	public Vector<ChartEvent> getChartEvents() {
		return chartEvents;
	}
	public void setChartEvents(Vector<ChartEvent> vChartEvents) {
		this.chartEvents = vChartEvents;
	}
	public String getChartEventType() {
		return chartEventType;
	}
	public void setChartEventType(String chartEventType) {
		this.chartEventType = chartEventType;
	}

}
