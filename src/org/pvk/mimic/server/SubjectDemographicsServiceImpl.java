package org.pvk.mimic.server;

import org.pvk.mimic.client.SubjectDemographicsService;
import org.pvk.mimic.client.SubjectDemographics;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.sql.*;
import java.util.Properties;

public class SubjectDemographicsServiceImpl extends RemoteServiceServlet implements SubjectDemographicsService {

  public SubjectDemographics getDemographics(int subjectID)   {
	  
	SubjectDemographics demographics = new SubjectDemographics();

	  
	Statement stmt = null;
	ResultSet rst = null;
	  
	try {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/MIMIC2";
		Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","Pejkpptc88k");
        Connection conn = DriverManager.getConnection(url, props);
        String strSQL = "SELECT * FROM mimic2v26.demographic_detail WHERE subject_ID = " + String.valueOf(subjectID) + ";";
        stmt = conn.createStatement();
        rst = stmt.executeQuery(strSQL);
        while (rst.next()) {
        	if (rst.getString("marital_status_descr") != null) {
        		demographics.setMaritalStatusDescription(rst.getString("marital_status_descr"));
        	} else {
        		demographics.setMaritalStatusDescription("Not Specified");
        	}
        	demographics.setSubjectID(subjectID);
        }
        rst.close();
        stmt.close();
        conn.close();
        
	} catch (SQLException e) {

	} catch (ClassNotFoundException e){
	  	
	}


    return demographics;
  }

}




