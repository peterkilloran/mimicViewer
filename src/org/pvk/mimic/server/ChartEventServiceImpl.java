package org.pvk.mimic.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import org.pvk.mimic.client.ChartEvent;
import org.pvk.mimic.client.ChartEventService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ChartEventServiceImpl extends RemoteServiceServlet implements ChartEventService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Vector<ChartEvent> getChartEvents(int subjectID, int ChartEventType) {
		// TODO Auto-generated method stub
		Vector<ChartEvent> chartEvents = new Vector<ChartEvent>();
		Statement stmt = null;
		ResultSet rst = null;
		  
		try {
			//Class.forName("org.postgresql.Driver");
			//String url = "jdbc:postgresql://localhost:5432/MIMIC2";
			//Properties props = new Properties();
	        //props.setProperty("user","postgres");
	        //props.setProperty("password","Pejkpptc88k");
	        
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://139.52.155.236:5432/MIMIC2";
			Properties props = new Properties();
	        props.setProperty("user","mimic-role");
	        props.setProperty("password","gerhard2000");
			
			Connection conn = DriverManager.getConnection(url, props);
	        String strSQL = "SELECT chartevents.* " +
	        	"FROM mimic2v26.chartevents " +
	        	"WHERE chartevents.itemid = " + ChartEventType +
	        	" AND chartevents.subject_id = " + String.valueOf(subjectID) +
	        	" ORDER BY chartevents.charttime ASC;";

	        stmt = conn.createStatement();
	        rst = stmt.executeQuery(strSQL);
	        while (rst.next()) {
	        	ChartEvent chartEvent = new ChartEvent();

	        	if (rst.getString("charttime") != null) {
	        		chartEvent.setChartTime(rst.getString("charttime"));
	        	} else {
	        		chartEvent.setChartTime("unspecified time");
	        	}
	        	
	        	if (rst.getString("realtime") != null) {
	        		chartEvent.setRealTime(rst.getString("realtime"));
	        	} else {
	        		chartEvent.setRealTime("unspecified time");
	        	}
	        	
	        	if (rst.getString("value1") != null) {
	        		chartEvent.setValue1(rst.getString("value1"));
	        	} else {
	        		chartEvent.setValue1("");
	        	}

	        	if (rst.getString("value2") != null) {
	        		chartEvent.setValue2(rst.getString("value2"));
	        	} else {
	        		chartEvent.setValue2("");
	        	}

	        	if (rst.getString("value1uom") != null) {
	        		chartEvent.setUom1(rst.getString("value1uom"));
	        	} else {
	        		chartEvent.setUom1("");
	        	}
	        	
	        	if (rst.getString("value2uom") != null) {
	        		chartEvent.setUom2(rst.getString("value2uom"));
	        	} else {
	        		chartEvent.setUom2("");
	        	}
	        	
	        	if (rst.getString("resultstatus") != null) {
	        		chartEvent.setResultStatus(rst.getString("resultstatus"));
	        	} else {
	        		chartEvent.setResultStatus("");
	        	}
	        	
	        	chartEvents.add(chartEvent);
	        	
	        }
	        rst.close();
	        stmt.close();
	        conn.close();
	        
		} catch (SQLException e) {

		} catch (ClassNotFoundException e) {
		  	
		}
		
		return chartEvents;
	}

}
