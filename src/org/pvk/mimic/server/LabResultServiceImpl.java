package org.pvk.mimic.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import org.pvk.mimic.client.LabResult;
import org.pvk.mimic.client.LabResultService;
import org.pvk.mimic.client.PatientNote;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LabResultServiceImpl extends RemoteServiceServlet implements
		LabResultService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Vector<LabResult> getLabResults(int subjectID) {
		// TODO Auto-generated method stub
		Vector<LabResult> labResults = new Vector<LabResult>();
		Statement stmt = null;
		ResultSet rst = null;
		  
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/MIMIC2";
			Properties props = new Properties();
	        props.setProperty("user","postgres");
	        props.setProperty("password","Pejkpptc88k");
	        Connection conn = DriverManager.getConnection(url, props);
	        String strSQL = "SELECT labevents.*, d_labitems.* " +
	        	"FROM mimic2v26.d_labitems, mimic2v26.labevents " +
	        	"WHERE labevents.itemid = d_labitems.itemid " +
	        	"AND subject_id = " + String.valueOf(subjectID) +
	        	"ORDER BY labevents.charttime ASC;";

	        stmt = conn.createStatement();
	        rst = stmt.executeQuery(strSQL);
	        while (rst.next()) {
	        	LabResult labResult = new LabResult();
	        	
	        	if (rst.getString("charttime") != null) {
	        		labResult.setChartTime(rst.getString("charttime"));
	        	} else {
	        		labResult.setChartTime("unspecified time");
	        	}
	        	
	        	if (rst.getString("category") != null) {
	        		labResult.setCategory(rst.getString("category"));
	        	} else {
	        		labResult.setCategory("unspecified");
	        	}
	        	
	        	if (rst.getString("value") != null) {
	        		labResult.setValue(rst.getString("value"));
	        	} else {
	        		labResult.setValue("");
	        	}

	        	if (rst.getString("valueuom") != null) {
	        		labResult.setValueUnits(rst.getString("valueuom"));
	        	} else {
	        		labResult.setValueUnits("");
	        	}
	        	
	        	if (rst.getString("test_name") != null) {
	        		labResult.setTestName(rst.getString("test_name"));
	        	} else {
	        		labResult.setTestName("");
	        	}
	        	
	        	if (rst.getString("fluid") != null) {
	        		labResult.setFluid(rst.getString("fluid"));
	        	} else {
	        		labResult.setFluid("unspecified fluid");
	        	}
	        	
	        	if (rst.getString("loinc_code") != null) {
	        		labResult.setLoincCode(rst.getString("loinc_code"));
	        	} else {
	        		labResult.setLoincCode("unspecified loinc code");
	        	}
	        	
	        	if (rst.getString("loinc_description") != null) {
	        		labResult.setLoincDescription(rst.getString("loinc_description"));
	        	} else {
	        		labResult.setLoincDescription("unspecified loinc description");
	        	}
	        	
	        	//labResult.setSubjectID(subjectID);
	        	labResults.add(labResult);
	        }
	        rst.close();
	        stmt.close();
	        conn.close();
	        
		} catch (SQLException e) {

		} catch (ClassNotFoundException e){
		  	
		}
		
		return labResults;
	}

}
