package org.pvk.mimic.server;

import org.pvk.mimic.client.PatientNote;
import org.pvk.mimic.client.PatientNoteService;
import org.pvk.mimic.client.SubjectDemographics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PatientNoteServiceImpl extends RemoteServiceServlet implements
		PatientNoteService {

	@Override
	public Vector<PatientNote> getNotes(int subjectID) {
		
		Vector<PatientNote> notes = new Vector<PatientNote>();
				  
		Statement stmt = null;
		ResultSet rst = null;
		  
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/MIMIC2";
			Properties props = new Properties();
	        props.setProperty("user","postgres");
	        props.setProperty("password","Pejkpptc88k");
	        Connection conn = DriverManager.getConnection(url, props);
	        String strSQL = "SELECT * FROM mimic2v26.noteevents WHERE subject_ID = " + String.valueOf(subjectID) + ";";
	        stmt = conn.createStatement();
	        rst = stmt.executeQuery(strSQL);
	        while (rst.next()) {
	        	PatientNote note = new PatientNote();
	        	
	        	if (rst.getString("charttime") != null) {
	        		note.setChartTime(rst.getString("charttime"));
	        	} else {
	        		note.setChartTime("unspecified time");
	        	}
	        	
	        	if (rst.getString("category") != null) {
	        		note.setCategoryDescription(rst.getString("category"));
	        	} else {
	        		note.setCategoryDescription("unspecified");
	        	}
	        	
	        	if (rst.getString("title") != null) {
	        		note.setTitle(rst.getString("title"));
	        	} else {
	        		note.setTitle("no title");
	        	}

	        	if (rst.getString("text") != null && (rst.getString("text").length()>0) && (!rst.getString("text").equals("\n\n"))) {
	        		String theNote = rst.getString("text");
	        		note.setText(rst.getString("text"));
	        	} else {
	        		note.setText("no text found");
	        	}
	        	
	        	note.setSubjectID(subjectID);
	        	notes.add(note);
	        }
	        rst.close();
	        stmt.close();
	        conn.close();
	        
		} catch (SQLException e) {

		} catch (ClassNotFoundException e){
		  	
		}


	    return notes;
	}

}
