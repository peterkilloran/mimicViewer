package org.pvk.mimic.client;

import java.util.Vector;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chartEvents")

public interface ChartEventService extends RemoteService {
	Vector<ChartEvent> getChartEvents(int subjectID, int ChartEventType);
}
