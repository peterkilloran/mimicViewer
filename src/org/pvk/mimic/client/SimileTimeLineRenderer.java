package org.pvk.mimic.client;

import java.util.ArrayList;

import com.netthreads.gwt.simile.timeline.client.BandInfo;
import com.netthreads.gwt.simile.timeline.client.BandOptions;
import com.netthreads.gwt.simile.timeline.client.DateTime;
import com.netthreads.gwt.simile.timeline.client.EventSource;
import com.netthreads.gwt.simile.timeline.client.HotZoneBandOptions;
import com.netthreads.gwt.simile.timeline.client.ITimeLineRender;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;

public class SimileTimeLineRenderer implements ITimeLineRender {

	@Override
	public void render(TimeLineWidget widget) {
		// TODO Auto-generated method stub
        ArrayList bandInfos = widget.getBandInfos();
        ArrayList bandHotZones = widget.getBandHotZones();
        ArrayList bandDecorators = widget.getBandDecorators();
        EventSource eventSource = widget.getEventSource();
        
        //HotZoneBandOptions hotZone = HotZoneBandOptions.create();
        //hotZone.setStart("2006");
        //hotZone.setEnd("2007");
        //hotZone.setMagnify(5);
        //hotZone.setUnit(DateTime.YEAR());
        //hotZone.setMultiple(2);
        //bandHotZones.add(hotZone);
        
        BandOptions topOpts = BandOptions.create();
        topOpts.setWidth("10%");
        topOpts.setIntervalUnit(DateTime.YEAR());
        topOpts.setIntervalPixels(200);
        topOpts.setShowEventText(false);
        //topOpts.setTheme(theme);
        topOpts.setDate("2006");
        //topOpts.setZones(bandHotZones);
        
        BandInfo top = BandInfo.create(topOpts);
        
        top.setDecorators(bandDecorators);
        bandInfos.add(top);
        
        BandOptions bottomOpts = BandOptions.create();
        bottomOpts.setWidth("10%");
        bottomOpts.setTrackHeight(1.3f);
        bottomOpts.setTrackGap(0.1f);
        bottomOpts.setIntervalUnit(DateTime.MONTH());
        bottomOpts.setIntervalPixels(50);
        bottomOpts.setShowEventText(true);
        //bottomOpts.setTheme(theme);
        bottomOpts.setEventSource(eventSource);
        bottomOpts.setDate("2006");
        bottomOpts.setZones(bandHotZones);
        bottomOpts.setTimeZone(0);

        BandInfo bottom = BandInfo.createHotZone(bottomOpts);
        bottom.setDecorators(bandDecorators);
        bandInfos.add(bottom);

        bottom.setSyncWith(0);
        bottom.setHighlight(true);
        

        BandOptions bottom2Opts = BandOptions.create();
        bottom2Opts.setWidth("10%");
        bottom2Opts.setTrackHeight(1.3f);
        bottom2Opts.setTrackGap(0.1f);
        bottom2Opts.setIntervalUnit(DateTime.MONTH());
        bottom2Opts.setIntervalPixels(50);
        bottom2Opts.setShowEventText(true);
        //bottomOpts.setTheme(theme);
        bottom2Opts.setEventSource(eventSource);
        bottom2Opts.setDate("2006");
        bottom2Opts.setZones(bandHotZones);
        bottom2Opts.setTimeZone(0);

        BandInfo bottom2 = BandInfo.createHotZone(bottom2Opts);
        bottom2.setDecorators(bandDecorators);
        bandInfos.add(bottom2);

        bottom2.setSyncWith(0);
        bottom2.setHighlight(true);
        
       
	}

}
