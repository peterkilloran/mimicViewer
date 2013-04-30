package org.pvk.mimic.client;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("subjectDemographics")
public interface SubjectDemographicsService extends RemoteService {

  SubjectDemographics getDemographics(int subjectID);
}



