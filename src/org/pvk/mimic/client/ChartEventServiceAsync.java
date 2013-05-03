package org.pvk.mimic.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChartEventServiceAsync {
	void getChartEvents(int subjectID, int ChartEventType, AsyncCallback< Vector<ChartEvent> > callback);
}
