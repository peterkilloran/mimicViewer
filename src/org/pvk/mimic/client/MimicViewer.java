package org.pvk.mimic.client;

import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.VisualizationUtils;

import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart.Type;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class MimicViewer implements EntryPoint {
	SubjectDemographics demographics;
	SubjectDemographicsServiceAsync subjectDemographicsSvc = GWT.create(SubjectDemographicsService.class);
	Vector<PatientNote> patientNotes;
	PatientNoteServiceAsync patientNoteSvc = GWT.create(PatientNoteService.class);
	HashMap<String, PatientNote> hNotes;
	HashMap<String, LabResult> hLabs;
	HashMap<String, HashMap<Integer,LabResult>> hLabResultTimeVector ;
	HashMap<String, Vector<LabResult>> hLabLoincResults ;
	HashMap<String, Vector<ChartEvent>> hChartEvents;
	HashMap<String, LabResult> hLabTypes ;
	ArrayList<String> labLoincCodes ;
	ArrayList<String> labEventTimes ;
	HashMap<Integer, String> hLabEventTimes ;
	Vector<LabResult> labResults ;
	LabResultServiceAsync labResultSvc = GWT.create(LabResultService.class);
	
	ChartEventServiceAsync chartEventSvc = GWT.create(ChartEventService.class);
	
	HashMap <String,Integer> hLabGridColumns ;
  	HashMap <String,Integer> hLabGridRows ;
	
	HashMap<String,String> hCbcPanel;
	HashMap<String,String> hChemPanel;
	HashMap<String,String> hAbgPanel;
	
    private TextBox subjectID = new TextBox();
    private Label maritalStatus = new Label("Marital Status: ");
    private Tree noteTree;
    private Tree labTree; 
    private Tree vitalsTree;
    private RichTextArea note = new RichTextArea();
    private Grid labGrid ;
	Button updateSubjectButton = new Button("update");
   
    private DataView labView;
    private ScrollPanel labTablePanel;
    private DataTable labDataTable;
    private Table labTable;
    private VerticalPanel labVisualPanel;
    private VerticalPanel vitalsVisualPanel;
    private FlowPanel eventTimeLinePanel;
    private Label errorLabel = new Label();
    private SplitLayoutPanel splitPanel;
	private ScrollPanel labPanel;
	private ScrollPanel vitalsPanel;
	private HorizontalPanel banner;
	//private ScrollPanel workPanel ;
	private StackLayoutPanel stack;
	private Date maxVisibleLabDate;
	private Date minVisibleLabDate;
	
	//private AnnotatedTimeLine labTimeLine;
	
	private TimeLineWidget simileTimeLine;

	
	private boolean bLabChartDragStarted ;
	private LayoutPanel selectedLabChartPanel;
	
	private MimicData data;
	private LabSelector labSelector;
    /**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * This is the entry point method.
	 */
	
	private void initializeVariables() {
		data = new MimicData(Integer.parseInt(subjectID.getText()));
	    
		class MyLabSelectionUpdatedHandler implements LabSelectionUpdatedEventHandler {
			public MyLabSelectionUpdatedHandler() {
				System.out.println("create handler");
			}
			@Override
			public void onLabSelectionUpdated(LabSelectionUpdatedEvent event) {
				// TODO Auto-generated method stub
				if (splitPanel.getWidgetIndex(note) > -1) {
					splitPanel.remove(note);
					
				}
				if (splitPanel.getWidgetIndex(vitalsPanel) > -1) {
					splitPanel.remove(vitalsPanel);
					
				}
				if (splitPanel.getWidgetIndex(labPanel) ==-1 ) {
					splitPanel.add(labPanel);
				}
				if (event.getSelectionType().equalsIgnoreCase("selected")) {
					System.out.println("lab selected");
					addLabToVisualiztion(event.getLoincID());
					addLabResultsToGrid(event.getLoincID());
				} else if (event.getSelectionType().equalsIgnoreCase("unselected")) {
					System.out.println("lab selected");
					removeLabFromVisualization(event.getLoincID());
					removeLabResultsFromGrid(event.getLoincID());
				} else {
					System.out.println("unknown selection type");
				}

			}
	    };
	    labSelector = new LabSelector(data);
	    MyLabSelectionUpdatedHandler labSelectionHandler = new MyLabSelectionUpdatedHandler();
	    labSelector.addLabSelectionUpdatedEventHandler(labSelectionHandler);
	    //data.pullLabs();
	   // data.pullPatientDemographics();
/*
		hCbcPanel = new HashMap<String,String>();
		hCbcPanel.put("26464-8", "WBC");
		hCbcPanel.put("20570-8", "HCT");
		hCbcPanel.put("718-7", "HGB");
		hCbcPanel.put("26515-7", "PLT COUNT");
		
		hChemPanel = new HashMap<String,String>();
		hChemPanel.put("2951-2", "SODIUM");
		hChemPanel.put("2823-3", "POTASSIUM");
		hChemPanel.put("2069-3", "CHLORIDE");
		hChemPanel.put("1963-8", "TOTAL CO2");
		hChemPanel.put("3094-0", "UREA N");
		hChemPanel.put("2160-0", "CREAT");
		hChemPanel.put("2345-7", "GLUCOSE");
		hChemPanel.put("17861-6", "CALCIUM");
		hChemPanel.put("19123-9", "MAGNESIUM");
		hChemPanel.put("2777-1", "PHOSPHATE");
		
		hAbgPanel = new HashMap<String,String>();
		hAbgPanel.put("11558-4","pH");
		hAbgPanel.put("11557-6","pCO2");
		hAbgPanel.put("11556-8","pO2");
		hAbgPanel.put("11555-0","BASE XS");
		hAbgPanel.put("1959-6","TOTAL CO2");
		hAbgPanel.put("19991-9","AADO2");
		hAbgPanel.put("32693-4","LACTATE");
		hAbgPanel.put("2947-0","Na+");
		hAbgPanel.put("6298-4","K+");
		hAbgPanel.put("2069-3","CL-");
		hAbgPanel.put("2339-0","GLUCOSE");
		hAbgPanel.put("20570-8","calcHCT");
		hAbgPanel.put("718-7","HGB");
		hAbgPanel.put("20563-3","CARBOXYHB");
		hAbgPanel.put("2614-6","MET HGB");
		hAbgPanel.put("1994-3","freeCa");
		
		//??Ventilator stuff
		hAbgPanel.put("19994-3","O2");
		hAbgPanel.put("3151-8","O2 Flow");
		hAbgPanel.put("20564-1","O2 Sat");
		hAbgPanel.put("20077-4","PEEP");
		hAbgPanel.put("20112-9","TIDAL VOL");

		*/
		demographics = new SubjectDemographics();
		patientNotes = new Vector<PatientNote>();
		hNotes= new HashMap<String, PatientNote>();
		hLabs= new HashMap<String, LabResult>();
		hLabResultTimeVector = new HashMap<String, HashMap<Integer,LabResult>>();
		hLabLoincResults = new HashMap<String, Vector<LabResult>>();
		hChartEvents = new HashMap<String, Vector<ChartEvent>>();
		hLabTypes = new HashMap<String, LabResult>();
		labLoincCodes = new ArrayList<String>();		
		labEventTimes = new ArrayList<String>();
		hLabEventTimes = new HashMap<Integer,String>();
		labResults = new Vector<LabResult>();
		banner = new HorizontalPanel();
		stack =  new StackLayoutPanel(Unit.EM);
    	labDataTable = DataTable.create();    	
    	labTable = new Table();
    	labTablePanel = new ScrollPanel();
  	    labTablePanel.add(labTable);
  	    labPanel = new ScrollPanel();
  	    labGrid = new Grid();
  	    //labPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
  	    labVisualPanel = new VerticalPanel();
		
  	    vitalsVisualPanel = new VerticalPanel();
  	    vitalsPanel = new ScrollPanel();
  	    
		
		
// commenting out the simile widget for now
		/*
		eventTimeLinePanel = new FlowPanel();
		simileTimeLine = new TimeLineWidget(
				"100%",
				"100%",
				new SimileTimeLineRenderer());

		simileTimeLine.load("test-data.xml");
		
		eventTimeLinePanel.add(simileTimeLine);
		*/

		//workPanel = new ScrollPanel();
		splitPanel = new SplitLayoutPanel(5);
		RootLayoutPanel.get().add(splitPanel);
		labTree = new Tree();
		noteTree = new Tree();
		
		vitalsTree = new Tree();
		clearVitalsTree();
		initializeVitalsTree();
		ScrollPanel navVitals = new ScrollPanel();
		navVitals.add(vitalsTree);
		
		// Create a new tree
		clearNoteTree();
		initializeNoteTree();
		//clearLabTree();
		//initializeLabTree();
		ScrollPanel navNotes = new ScrollPanel();
		//ScrollPanel navLabs = new ScrollPanel();
	    navNotes.add(noteTree);
		//navLabs.add(labTree);

    	
	    
		ScrollPanel navLabs = labSelector.getLabTreeScrollPanel();
		stack.add(navNotes, "Notes", false, 2);
		stack.add(navLabs, "Labs", false, 2);
		
		
		stack.add(navVitals, "Vital Signs", false, 2);
		
		//stack.forceLayout();
		
		banner.setSpacing(5);
	    banner.ensureDebugId("cwBannerPanel");
	    banner.add(new Label("Subject ID:"));
	    banner.add(subjectID);
	    banner.add(updateSubjectButton);
	    banner.add(maritalStatus);
	    banner.add(errorLabel);

	    // Create a Split Panel
	    splitPanel.clear();
	    splitPanel.ensureDebugId("cwSplitLayoutPanel");
	    
	    splitPanel.getElement().getStyle()
	        .setProperty("border", "3px solid #e7e7e7");
	    splitPanel.addNorth(banner, 50);
	    splitPanel.addWest(stack, 200);
	    
	    //commenting out the event timeline for sharpC
	    /*
	    splitPanel.addNorth(eventTimeLinePanel, 300); 
	    */
	    splitPanel.addEast(labTablePanel, 850);
	    //splitPanel.add(workPanel);
	    
	    
	    labPanel.setWidth("100%");
	    labVisualPanel.setWidth("100%");
	    
	    //splitPanel.add(note);
	    
	    //workPanel.add(note);
	    //workPanel.add(labPanel);
	    //note.setHeight("500px");
	    note.setWidth("100%");
	    

		updateBanner();
		updateLabs();
		updateVitals();
		//updateLabVisualization();
		labPanel.add(labVisualPanel);
		
		splitPanel.add(labPanel);
		vitalsPanel.setWidth("100%");
		vitalsVisualPanel.setWidth("100%");
		vitalsPanel.add(vitalsVisualPanel);
	    
		
	}
	public void onModuleLoad() {
		
		
		VisualizationUtils.loadVisualizationApi(new Runnable() {
		      public void run() {
		    	initializeVariables();
		    	errorLabel.setText("loading Visualization library");
		      }}, Table.PACKAGE, AnnotatedTimeLine.PACKAGE, LineChart.PACKAGE);
		

	    updateSubjectButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				note.setText("");
				labVisualPanel.clear();
				labTablePanel.clear();
				initializeVariables();
			}
		});
	    
	    subjectID.setText("3011");	    
	    note.setText("no note selected");
	    
	    
	}
	/*
	private void initializeLabTree() {
	    // Add a handler that automatically generates some children
	    labTree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				// TODO Auto-generated method stub
		    	  final TreeItem item = event.getTarget();
		    	  if (item.getChildCount() <= 1) {
		    		  if (item.getChildCount() <= 1) {
		  		        
		    			  if (item.getText().contentEquals("By Test")){
		  		        	item.setState(false,false);
		  		        	for (String key : hLabLoincResults.keySet()) {
		  		        		if (hLabLoincResults.containsKey(key)) {
		  		        			// same loinc can be found for different sources (i.e., loinc for hgb is the same for abg and cbc		  		        			
		  		        			item.addItem(getLabTreeItemPanel(key));		  		        			
		  		        		}
		  		        		
		  		        	}
		  		        	item.setState(true,false);
		  		        	if (splitPanel.getWidgetIndex(note)>-1) {
		  		    		  
		  		    	  } 
		  		    	  
		  		        } else if (item.getText().contentEquals("By Panel")) {
		  		        	item.setState(false,false);
		  		        	TreeItem cbcItem = new TreeItem("CBC");
		  		        	TreeItem chemItem = new TreeItem("Chemistry");
		  		        	TreeItem abgItem = new TreeItem("ABG");
		  		        	TreeItem lftItem = new TreeItem("LFT");
		  		        	
		  		        	for (String key : hCbcPanel.keySet()) {
		  		        		if (hLabLoincResults.containsKey(key)) {
		  		        			// same loinc can be found for different sources (i.e., loinc for hgb is the same for abg and cbc
		  		        			if (hLabLoincResults.get(key).get(0).getCategory().toString().equalsIgnoreCase("HEMATOLOGY")) {
		  		        				cbcItem.addItem(getLabTreeItemPanel(key));		  		        			
		  		        			}
		  		        		}
		  		        	}
		  		        	if (cbcItem.getChildCount()>0) {
		  		        		item.addItem(cbcItem);
		  		        	}
		  		        	
		  		        	for (String key : hChemPanel.keySet()) {
		  		        		if (hLabLoincResults.containsKey(key)) {
		  		        			// differentiate loinc from different sources (i.e., loinc for hgb is the same for abg and cbc
		  		        			if (hLabLoincResults.get(key).get(0).getCategory().toString().equalsIgnoreCase("CHEMISTRY")) {
		  		        				chemItem.addItem(getLabTreeItemPanel(key));		  		        			
		  		        			}
		  		        		}
		  		        	}
		  		        	if (chemItem.getChildCount()>0) {
		  		        		item.addItem(chemItem);
		  		        	}

		  		        	for (String key : hAbgPanel.keySet()) {
		  		        		if (hLabLoincResults.containsKey(key)) {
		  		        			// differentiate loinc from different sources (i.e., loinc for hgb is the same for abg and cbc
		  		        			if (hLabLoincResults.get(key).get(0).getCategory().toString().equalsIgnoreCase("BLOOD GAS")) {
		  		        				abgItem.addItem(getLabTreeItemPanel(key));		  		        					  		        		
		  		        			}
		  		        		}
		  		        	}
		  		        	if (abgItem.getChildCount()>0) {
		  		        		item.addItem(abgItem);
		  		        	}

		  		        	
		  		        	item.setState(true,false);
		  		        	
		  		        }
		    			  splitPanel.remove(note);
		  	    		  splitPanel.remove(labPanel);
		  		    	  errorLabel.setText("updateing lab visualization");
		  		    	  updateLabVisualization();		  		    	  
		  		    	  //splitPanel.add(labVisualPanel);
		  		    	  splitPanel.add(labPanel);
		  		    	  updateLabVisualization();
		  		    	  
		  		    	  errorLabel.setText("lab visualization updated");
		    		  }
		    	  }
		    	  
			}});
	    
		}
*/
	/*
	private HorizontalPanel getLabTreeItemPanel(String loinc) {
  		HorizontalPanel hp = new HorizontalPanel();
  		Label l = new Label(loinc);
  		l.setVisible(false);
  		CheckBox cb = new CheckBox(hLabLoincResults.get(loinc).get(0).getTestName());
  		hp.add(l);
  		hp.add(cb);
  		cb.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				boolean checked = ((CheckBox) event.getSource()).getValue();
				HorizontalPanel parent = (HorizontalPanel)((Widget) event.getSource()).getParent();
				Label child =(Label) parent.getWidget(0);
				if (checked) {
					addLabToVisualiztion(child.getText().toString());
					addLabResultsToGrid(child.getText().toString());
				} else {
					removeLabFromVisualization(child.getText().toString());
					removeLabResultsFromGrid(child.getText().toString());
				}
			}});
  		return hp;
	}
	
	*/
	
	private void updateTimeLineIndex() {
		DataTable labTimeData = DataTable.create();
		labTimeData.insertColumn(0, ColumnType.DATETIME);
		labTimeData.insertColumn(1, ColumnType.NUMBER);
		

		DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss");
		
		for (int i=0;i<labEventTimes.size();i++) {
			labTimeData.addRow();
			java.util.Date d = df.parse(labEventTimes.get(i));
			//Properties props = Properties.create();
	
			//labTimeData.setCell(i, 0, d, d.toString(), props);
			
			labTimeData.setValue(i, 0, d);
			labTimeData.setValue(i, 1, 1);
		}
		
		//eventTimeLinePanel.clear();
		AnnotatedTimeLine.Options options = AnnotatedTimeLine.Options.create();
        options.setThickness(5);
        options.setDisplayLegendDots(true);
		
//		labTimeLine = new AnnotatedTimeLine(labTimeData, options, "100%", "200px");
//		eventTimeLinePanel.add(labTimeLine);
		

		if (eventTimeLinePanel.isVisible()) {
			//labTimeLine.draw(labTimeData);
		}
		
		
		/*
		labTimeLine.addRangeChangeHandler(new RangeChangeHandler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				maxVisibleLabDate= event.getEnd();
				minVisibleLabDate= event.getStart();
				// TODO Auto-generated method stub
				errorLabel.setText("On range change event");
				//if (workPanel.getWidget().equals(note)) {
		    	//	  workPanel.remove(note);	    		  
		    	//  } else {
		    		  //must be working with labPanel
		    	//	  workPanel.remove(labPanel);
		    	//  }
		    	  
		    	//  updateLabVisualization();
		    	//  workPanel.add(labPanel);
		    	  
		    	 if (splitPanel.getWidgetIndex(note)>-1) {
		    		  splitPanel.remove(note);
		    		  splitPanel.add(labPanel);
		    	 } 
		    //*** this is critical to NOT remove the lab panel to use full width for some reason
		    	 	//can't figure out the inconsistent chart sizing
		    	 //at least this makes it consistent
		    	 	splitPanel.remove(labPanel);
		    	 
		    		updateLabVisualization();
		    		splitPanel.add(labPanel);
			
		    	 
			}
			
		});
		*/
	}
	
	private DataTable getVitalsDataTable(String vitalName, boolean bTwoColumns) {
		
		DataTable vitalsData = DataTable.create();						
		vitalsData.insertColumn(0, ColumnType.DATETIME, "Time");
		//labResultData.insertColumn(0, ColumnType.NUMBER, "Time");
		
		vitalsData.insertColumn(1, ColumnType.NUMBER,"value1");
		if (bTwoColumns) {
			vitalsData.insertColumn(2, ColumnType.NUMBER,"value2");			
		}
		//vitalsData.insertColumn(2, ColumnType.NUMBER,"value2");
		vitalsData.addRow();
		int rowIndex = 0;
		//labResultData.setValue(0, 0, df.parse(labEventTimes.get(0)).getTime());
		DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss");
		for (ChartEvent ce : hChartEvents.get(vitalName)) {
			try {
				if (Float.parseFloat(ce.getValue1())!=Float.NaN) {
					vitalsData.addRow();
					java.util.Date d = df.parse(ce.getChartTime());
					vitalsData.setValue(rowIndex, 0, d);
					//labResultData.setValue(rowIndex, 0, df.parse(r.getChartTime()).getTime());
					vitalsData.setValue(rowIndex, 1, Float.parseFloat(ce.getValue1()));
					if (bTwoColumns) {
						vitalsData.setValue(rowIndex, 2, Float.parseFloat(ce.getValue2()));						
					}
					//vitalsData.setValue(rowIndex, 2, Float.parseFloat(ce.getValue2()));
					rowIndex = rowIndex+1;		
				}
			} catch (NumberFormatException e) {
				//do nothing
				
			}
			
		}
		
		return vitalsData;		
	}
	
	private DataTable getLabDataTable(String labKey) {
		
		DataTable labResultData = DataTable.create();						
		labResultData.insertColumn(0, ColumnType.DATETIME, "Time");
		//labResultData.insertColumn(0, ColumnType.NUMBER, "Time");
		
		labResultData.insertColumn(1, ColumnType.NUMBER,hLabLoincResults.get(labKey).get(0).getTestName() );
		labResultData.addRow();
		int rowIndex = 0;
		//labResultData.setValue(0, 0, df.parse(labEventTimes.get(0)).getTime());
		DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss");
		for (LabResult r : hLabLoincResults.get(labKey)) {
			try {
				if (Float.parseFloat(r.getValue())!=Float.NaN) {
					labResultData.addRow();
					java.util.Date d = df.parse(r.getChartTime());
					labResultData.setValue(rowIndex, 0, d);
					//labResultData.setValue(rowIndex, 0, df.parse(r.getChartTime()).getTime());
					labResultData.setValue(rowIndex, 1, Float.parseFloat(r.getValue()));
					rowIndex = rowIndex+1;		
				}
			} catch (NumberFormatException e) {
				//do nothing
				
			}
			
		}
		
		return labResultData;		
	}
	
	private Options getLabChartOptions(String labKey) {
		Options opt = Options.create();
		Options hAxisOptions = Options.create();
		Options viewWindowOptions = Options.create();
		Options legendOptions = Options.create();
		Options vAxisOptions = Options.create();
		//Options chartAreaOptions = Options.create();
		
		//Options seriesOptions = Options.create();
		//Options thisSeries = Options.create();
		//thisSeries.set("pointSize", "2")
		
		opt.setType(Type.LINE);

		legendOptions.set("position", "none");
		
		viewWindowOptions.set("max", maxVisibleLabDate);
		viewWindowOptions.set("min", minVisibleLabDate);
		
		hAxisOptions.set("viewWindow", viewWindowOptions);
		hAxisOptions.set("maxValue", maxVisibleLabDate);
		hAxisOptions.set("minValue", minVisibleLabDate);					
		hAxisOptions.set("viewWindowMode", "explicit");
		
		//vAxisOptions.set("title", hLabLoincResults.get(labKey).get(0).getTestName());
		Options vAxisGridlineOptions = Options.create();
		vAxisGridlineOptions.set("count", "2");
		vAxisOptions.set("gridlines", vAxisGridlineOptions);
		
		//chartAreaOptions.set("left", "100");
		
		//opt.set("chartArea", chartAreaOptions);
		opt.set("vAxis", vAxisOptions);
		opt.set("hAxis", hAxisOptions);
		opt.set("legend", legendOptions);
		opt.set("pointSize", "2");
		opt.set("lineWidth", "1");
		opt.set("fontSize", "9");
		return opt;
	}
	
private LayoutPanel getVitalsChartLayoutPanel(String vitalsName, boolean bTwoValues){
		//using the same chart options as labs for now...
		final LineChart vitalsLineChart = new LineChart(getVitalsDataTable(vitalsName, bTwoValues), getLabChartOptions(vitalsName));
		vitalsLineChart.setTitle(vitalsName);
		vitalsLineChart.setWidth("100%");
		vitalsLineChart.setHeight("50px");

		//FocusPanel chartFocusPanel = new FocusPanel();
		//chartFocusPanel.setWidth("100%");
		
		//chartFocusPanel.add(labLineChart);
		
		Button b = new Button(vitalsName);
		Label l = new Label(vitalsName);
		l.setVisible(false);
		final LayoutPanel lp = new LayoutPanel();
		lp.add(l);
		lp.setHeight("50px");
		lp.setWidth("100%");
		lp.add(b);
		lp.add(vitalsLineChart);
		
		//lp.add(chartFocusPanel);
		lp.setWidgetLeftWidth(b, 0, Style.Unit.PX, 100, Style.Unit.PX);
		//lp.setWidgetLeftWidth(chartFocusPanel, 100, Style.Unit.PX, 100, Style.Unit.PCT);
		lp.setWidgetLeftWidth(vitalsLineChart, 100, Style.Unit.PX, 100, Style.Unit.PCT);
						
		b.addMouseDownHandler(new MouseDownHandler(){

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				
				//errorLabel.setText("mouse down: "  + event.getSource().toString());
				bLabChartDragStarted=true;
				selectedLabChartPanel = lp;
											
			}});
		
		b.addMouseUpHandler(new MouseUpHandler(){

			@Override
			public void onMouseUp(MouseUpEvent event) {
				// TODO Auto-generated method stub
				//errorLabel.setText("onMouseUp: " + selectedLabChart.getTitle());
				Widget dropTarget = null;
				for (Widget w: vitalsVisualPanel) {
					int dropY = event.getClientY();
					int targetTopY = w.getAbsoluteTop();
					int targetBotY = targetTopY + w.getOffsetHeight();
					if((dropY > targetTopY) && (dropY < targetBotY)) {
						dropTarget = w;
					}
				}
				
				if (dropTarget != null) {
					//need to know the source lab... using existing chart title
					
					//FocusPanel focPan = (FocusPanel)selectedLabChartPanel.getWidget(2);
					//LineChart lc = (LineChart)focPan.getWidget();
					
					//FocusPanel focPan = (FocusPanel)selectedLabChartPanel.getWidget(2);
					LineChart lc = (LineChart)selectedLabChartPanel.getWidget(2);
					boolean bBPHack = lc.getTitle().equalsIgnoreCase("Arterial Blood Pressure");
					//a bit sloppy... using the title as hLoinc key
					LayoutPanel newLayoutPanel = getVitalsChartLayoutPanel(lc.getTitle(), bBPHack);
					int index = vitalsVisualPanel.getWidgetIndex(dropTarget);
					vitalsVisualPanel.remove(selectedLabChartPanel);
					splitPanel.forceLayout();
					vitalsVisualPanel.insert(newLayoutPanel, index);
					
				}

			}});

		return lp;
		
	}
	
	private LayoutPanel getLabChartLayoutPanel(String labKey){
		
		final LineChart labLineChart = new LineChart(getLabDataTable(labKey), getLabChartOptions(labKey));
		labLineChart.setTitle(hLabLoincResults.get(labKey).get(0).getLoincCode());
		labLineChart.setWidth("100%");
		labLineChart.setHeight("50px");

		//FocusPanel chartFocusPanel = new FocusPanel();
		//chartFocusPanel.setWidth("100%");
		
		//chartFocusPanel.add(labLineChart);
		
		Button b = new Button(hLabLoincResults.get(labKey).get(0).getTestName());
		Label l = new Label(labKey);
		l.setVisible(false);
		final LayoutPanel lp = new LayoutPanel();
		lp.add(l);
		lp.setHeight("50px");
		lp.setWidth("100%");
		lp.add(b);
		lp.add(labLineChart);
		//lp.add(chartFocusPanel);
		lp.setWidgetLeftWidth(b, 0, Style.Unit.PX, 100, Style.Unit.PX);
		//lp.setWidgetLeftWidth(chartFocusPanel, 100, Style.Unit.PX, 100, Style.Unit.PCT);
		lp.setWidgetLeftWidth(labLineChart, 100, Style.Unit.PX, 100, Style.Unit.PCT);
						
		b.addMouseDownHandler(new MouseDownHandler(){

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				
				//errorLabel.setText("mouse down: "  + event.getSource().toString());
				bLabChartDragStarted=true;
				selectedLabChartPanel = lp;
											
			}});
		
		b.addMouseUpHandler(new MouseUpHandler(){

			@Override
			public void onMouseUp(MouseUpEvent event) {
				// TODO Auto-generated method stub
				//errorLabel.setText("onMouseUp: " + selectedLabChart.getTitle());
				Widget dropTarget = null;
				for (Widget w: labVisualPanel) {
					int dropY = event.getClientY();
					int targetTopY = w.getAbsoluteTop();
					int targetBotY = targetTopY + w.getOffsetHeight();
					if((dropY > targetTopY) && (dropY < targetBotY)) {
						dropTarget = w;
					}
				}
				
				if (dropTarget != null) {
					//need to know the source lab... using existing chart title
					
					//FocusPanel focPan = (FocusPanel)selectedLabChartPanel.getWidget(2);
					//LineChart lc = (LineChart)focPan.getWidget();
					
					//FocusPanel focPan = (FocusPanel)selectedLabChartPanel.getWidget(2);
					LineChart lc = (LineChart)selectedLabChartPanel.getWidget(2);
					
					//a bit sloppy... using the title as hLoinc key
					LayoutPanel newLayoutPanel = getLabChartLayoutPanel(lc.getTitle());
					int index = labVisualPanel.getWidgetIndex(dropTarget);
					labVisualPanel.remove(selectedLabChartPanel);
					splitPanel.forceLayout();
					labVisualPanel.insert(newLayoutPanel, index);
					
				}

			}});

		return lp;
		
	}
	
	private void removeLabFromVisualization (String loinc) {
		for (int i = 0; i<labVisualPanel.getWidgetCount(); i++) {
			Label targetLabel = (Label) ((ComplexPanel) labVisualPanel.getWidget(i)).getWidget(0);
			if (targetLabel.getText().toString().equals(loinc)) {
				labVisualPanel.remove(i);
				return;
			}
		}
	}
	
	private void addVitalsToVisualiztion(String vitalName, boolean twoVariables) {
				splitPanel.forceLayout();
				System.out.println(vitalsVisualPanel.getWidgetCount());
				vitalsVisualPanel.add(getVitalsChartLayoutPanel(vitalName, twoVariables));
				
				splitPanel.forceLayout();
	}
	
	
	private void addLabToVisualiztion(String loinc) {
		try {
			if ((Float.parseFloat(hLabLoincResults.get(loinc).get(0).getValue())!=Float.NaN) ) {
				splitPanel.forceLayout();
				System.out.println(labVisualPanel.getWidgetCount());
				labVisualPanel.add(getLabChartLayoutPanel(loinc));
				
				splitPanel.forceLayout();
				
			}
		} catch (NumberFormatException e) {
			// result values aren't numbers, so can't use line chart
			e.printStackTrace();
		}
	}
	/*
	private void updateLabVisualization() {
		
		ArrayList <String> loinc= new ArrayList<String>();
		
		for (int i=0; i<labVisualPanel.getWidgetCount(); i++ ) {
			LayoutPanel parent = (LayoutPanel)labVisualPanel.getWidget(i);
			Label child =(Label) parent.getWidget(0);
			loinc.add(child.getText());
		}
		labVisualPanel.clear();
		splitPanel.forceLayout();
		for (String key : loinc) {
			//for (String key : hLabLoincResults.keySet()) {
			
			try {
				if ((Float.parseFloat(hLabLoincResults.get(key).get(0).getValue())!=Float.NaN) ){
					
					labVisualPanel.add(getLabChartLayoutPanel(key));
					
				}
			} catch (NumberFormatException e) {
				// result values aren't numbers, so can't use line chart
			} catch (JavaScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
        labPanel.setWidth("100%");
        labVisualPanel.setWidth("100%");
	}
	
	*/
	private void updateVitals() {
		class MyChartEventsUpdatedHandler implements ChartEventsUpdatedEventHandler {
			@Override
			public void onChartEventsUpdated(ChartEventsUpdatedEvent event) {
				System.out.print("chart events updated:");
				System.out.println(String.valueOf(event.getChartEvents().size()));
				errorLabel.setText("Processing chart events");

				hChartEvents.put(event.getChartEventType(), event.getChartEvents());
			}
		}
		//set up handler and start the data pull
		MyChartEventsUpdatedHandler chartEventsUpdatedEventHandler = new MyChartEventsUpdatedHandler();
		data.addChartEventsUpdatedEventHandler(chartEventsUpdatedEventHandler);
		data.pullArterialBloodPressure();
		data.pullHeartRate();
		data.pullTemperature();
		data.pullSpO2();
		data.pullRespiratoryRate();
		
	}
	
	private void updateLabs() {
		
	    //int id = Integer.parseInt(subjectID.getText());
	    //final MimicData md = new MimicData(id);
	    //setup asynchronous call back function
		class MyLabResultsUpdatedHandler implements LabResultsUpdatedEventHandler {
			@Override
			public void onLabResultsUpdated(LabResultsUpdatedEvent event) {
				System.out.print("labs updated:");
				System.out.println(String.valueOf(event.getvLabResults().size()));
				errorLabel.setText("Processing Lab results");
	        	  
	        	  labTablePanel.remove(labGrid);
	        	  labGrid.clear();
	        	  labTablePanel.clear();
			  	
	        	  
	          	hLabGridColumns = new HashMap <String,Integer>();
	          	hLabGridRows = new HashMap <String,Integer>();
	          	
	          	labEventTimes = data.getLabEventTimes();
	          	labLoincCodes =data.getLabLoincCodes();
	          	hLabLoincResults = data.gethLabLoincResults();
	          	
	          	minVisibleLabDate = data.getMinLabDate();
	          	maxVisibleLabDate = data.getMaxLabDate();
	          	
			  	labGrid = new Grid();
			  	
	          	labTablePanel.add(labGrid);
	          	labGrid.setWidth("100%");
	          	labGrid.setHeight("100%");
	          	labGrid.setBorderWidth(1);
	          	errorLabel.setText("Lab results processed");
	          	
	          	for (int i = 0; i<stack.getWidgetCount();i++) {
	          		Widget w = stack.getWidget(i);
	          		if (w.getTitle().equalsIgnoreCase("Lab Results")) {
	          			//LabSelector selector = new LabSelector(data);
	          			stack.remove(i);
	          			w = labSelector.getLabTreeScrollPanel();
	          			stack.insert(w, "Lab Results", 2, i);
	          			break;
	          		}
	          	}
			}
		}
		//set up handler and start the data pull
		MyLabResultsUpdatedHandler LabResultsUpdateHandler = new MyLabResultsUpdatedHandler();
		data.addLabResultsUpdatedEventHandler(LabResultsUpdateHandler);
		data.pullLabs();
		
		
		

	}
	
	private void addLabResultsToGrid(String lonic) {
		
		ArrayList<String> columnsToAdd = new ArrayList<String>();
		hLabGridRows.put(lonic, hLabGridRows.size()+1);
		for (LabResult result : hLabLoincResults.get(lonic)) {
			if (!hLabGridColumns.containsKey(result.getChartTime())) {
				columnsToAdd.add(result.getChartTime());
			}
		}
		columnsToAdd.addAll(hLabGridColumns.keySet());
		
		//labGrid.resizeColumns(hLabGridColumns.size()+columnsToAdd.size());
		labGrid.resizeColumns(columnsToAdd.size()+1);

		Collections.sort(columnsToAdd);
		hLabGridColumns.clear();
		labGrid.clear();
		for (int i=0; i<columnsToAdd.size();i++) {
			hLabGridColumns.put(columnsToAdd.get(i), i+1);
		}
		//will need to completely redraw the table....
		//first add column labels
		
		//if (labGrid.getRowCount()<1) {
		//	labGrid.resizeRows(1);
		//}
		labGrid.resize(hLabGridRows.size()+1, hLabGridColumns.size()+1);
		for (String s : hLabGridColumns.keySet()) {
			labGrid.setWidget(0, hLabGridColumns.get(s), new Label(s));
		}
		
		for (String key : hLabGridRows.keySet()) {
			
			//int rowIndex = labGrid.getRowCount();
	  		//labGrid.resizeRows(rowIndex+1);
	  		labGrid.setWidget(hLabGridRows.get(key), 0, new Label(hLabLoincResults.get(key).get(0).getTestName()));
	  		//hLabGridRows.put(lonic, rowIndex);
			for (LabResult result : hLabLoincResults.get(key)) {
				labGrid.setWidget(hLabGridRows.get(key), hLabGridColumns.get(result.getChartTime()), new Label(result.getValue()));
				
	      	}
		}
		/*
		int rowIndex = labGrid.getRowCount();
  		labGrid.resizeRows(rowIndex+1);
  		labGrid.setWidget(rowIndex, 0, new Label(hLabLoincResults.get(lonic).get(0).getTestName()));
  		hLabGridRows.put(lonic, rowIndex);
		for (LabResult result : hLabLoincResults.get(lonic)) {
			labGrid.setWidget(rowIndex, hLabGridColumns.get(result.getChartTime()), new Label(result.getValue()));
			
      	}
      	*/
	}
	private void removeLabResultsFromGrid(String lonic) {
		ArrayList<String> newColumns = new ArrayList<String>();
		int delRowIndex = hLabGridRows.get(lonic);
		hLabGridRows.remove(lonic);
		for (String loinc : hLabGridRows.keySet()) {
			if (hLabGridRows.get(loinc) > delRowIndex) {
				hLabGridRows.put(loinc, hLabGridRows.get(loinc)-1);
			}
			for (LabResult result : hLabLoincResults.get(loinc)) {
				if (!newColumns.contains(result.getChartTime())) {
					newColumns.add(result.getChartTime());
				}
			}
		}
		
		Collections.sort(newColumns);
		hLabGridColumns.clear();
		labGrid.clear();
		for (int i=0;i<newColumns.size();i++){
			hLabGridColumns.put(newColumns.get(i), i+1);
		}
		//labGrid.removeRow(hLabGridRows.get(lonic));
		labGrid.resize(hLabGridRows.size()+1, hLabGridColumns.size()+1);
		for (String s : hLabGridColumns.keySet()) {
			labGrid.setWidget(0, hLabGridColumns.get(s), new Label(s));
		}
		
		for (String key : hLabGridRows.keySet()) {
			
			//int rowIndex = labGrid.getRowCount();
	  		//labGrid.resizeRows(rowIndex+1);
	  		labGrid.setWidget(hLabGridRows.get(key), 0, new Label(hLabLoincResults.get(key).get(0).getTestName()));
	  		//hLabGridRows.put(lonic, rowIndex);
			for (LabResult result : hLabLoincResults.get(key)) {
				labGrid.setWidget(hLabGridRows.get(key), hLabGridColumns.get(result.getChartTime()), new Label(result.getValue()));
				
	      	}
		}
		
		
	}
	
	private void updateBanner() {

	    int id = Integer.parseInt(subjectID.getText());
	    MimicData md = new MimicData(id);
	    //set up call back function
		class MyDemographicsUpdatedHandler implements DemographicsUpdatedEventHandler {
			@Override
			public void onDemographicsUpdated(DemographicsUpdatedEvent event) {

 		    	subjectID.setText(String.valueOf(event.getDemographics().getSubjectID()));
 		    	maritalStatus.setText("Marital Status: " + event.getDemographics().getMaritalStatusDescription());
			}
		}
		//set up handler and start the data pull
		MyDemographicsUpdatedHandler DemographicsUpdateHandler = new MyDemographicsUpdatedHandler();
		md.addDemographicsUpdatedEventHandler(DemographicsUpdateHandler);
		md.pullPatientDemographics();

	}
	
	private void clearNoteTree() {
		noteTree.clear();
	    TreeItem noteRoot = new TreeItem("Notes");
	    noteRoot.addItem("");
	    //root.addItem(noteRoot);
	    noteTree.addItem(noteRoot);
	}
	
	private void clearVitalsTree() {
		vitalsTree.clear();
	    TreeItem vitalsRoot = new TreeItem("Vital Signs");
	    vitalsRoot.addItem("");
	    //root.addItem(noteRoot);
	    vitalsTree.addItem(vitalsRoot);
	}
	
	private void initializeNoteTree() {
	    // Add a handler that automatically generates some children
	    noteTree.addOpenHandler(new OpenHandler<TreeItem>() {
	      public void onOpen(OpenEvent<TreeItem> event) {
	        final TreeItem item = event.getTarget();
	        if (item.getChildCount() <= 1) {
		        if (item.getText().contentEquals("Notes")){
		        	item.setState(false,false);
		        	// Initialize the service proxy
			          if (patientNoteSvc == null) {
			        	  patientNoteSvc = GWT.create(PatientNoteService.class);
			          }
	
			          // Set up the callback object.
			          AsyncCallback< Vector<PatientNote> > callback = new AsyncCallback< Vector<PatientNote> >() {
			            public void onFailure(Throwable caught) {
			              // TODO: Do something with errors.
			            }
	
			            public void onSuccess(Vector<PatientNote> result) {
			            	item.setState(false,false);
			            	patientNotes = result;
			            	HashMap<String, TreeItem> hNoteCategories = new HashMap<String, TreeItem>();
			            	for (int i=0;i<result.size();i++) {
			            		if (!hNoteCategories.containsKey(result.get(i).getCategoryDescription())) {
			            			TreeItem addItem = item.addItem(result.get(i).getCategoryDescription());
			            			hNoteCategories.put(result.get(i).getCategoryDescription(), addItem);
			            			
			            		}
			            	}
			            	
			            	for (int i=0;i<result.size();i++) {
			            		String desc = result.get(i).getCategoryDescription();
			            		TreeItem targetItem = hNoteCategories.get(desc);
			            		String uniqueTitle = result.get(i).getChartTime()+"-"+String.valueOf(i);
			            		targetItem.addItem(uniqueTitle);
			            		hNotes.put(uniqueTitle, result.get(i));
			            	}
			            	item.setState(true,false);
			            }
			          };
		        
		          // Make the call to the note service.
		          int id = Integer.parseInt(subjectID.getText());
		          patientNoteSvc.getNotes(id, callback);
		        }
	        	
	        }
	        
	      }
	    });

	    noteTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				// TODO Auto-generated method stub
				 TreeItem selectedItem = event.getSelectedItem();
				 if (hNotes.containsKey(selectedItem.getText())) {
					 note.setText(hNotes.get(selectedItem.getText()).getText());
					 //splitPanel.clear();
					 
					 //workPanel.remove(labPanel);
					 if (splitPanel.getWidgetIndex(labPanel) > -1 ) {
						 splitPanel.remove(labPanel);
					 }
					 if (splitPanel.getWidgetIndex(vitalsPanel) > -1 ) {
						 splitPanel.remove(vitalsPanel);
					 }
					 //splitPanel.remove(note);
					 //note.setWidth("100%");
					 //note.setHeight("100%");
					 if (splitPanel.getWidgetIndex(note)==-1) {
						 splitPanel.add(note);
					 }
					note.setWidth("100%");
					note.setHeight("100%");
					 //}

					 /*
					 if (workPanel.getWidget() == null) {
						 workPanel.add(note);
					 }
					 */
				 }
			}
		    });
	}
	
	private HorizontalPanel getVitalsTreeItemPanel(String vitalName) {
  		HorizontalPanel hp = new HorizontalPanel();
  		Label l = new Label(vitalName);
  		l.setVisible(false);
  		CheckBox cb = new CheckBox();
  		hp.add(l);
  		hp.add(cb);
  		cb.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				boolean checked = ((CheckBox) event.getSource()).getValue();
				HorizontalPanel parent = (HorizontalPanel) ((Widget) event.getSource()).getParent();
				Label child =(Label) parent.getWidget(0);
				//fire events
				System.out.println("checkbox clicked");
			}});


  		return hp;
	}
	
	private void initializeVitalsTree() {
	    // Add a handler that automatically generates some children
	    vitalsTree.addOpenHandler(new OpenHandler<TreeItem>() {
	      public void onOpen(OpenEvent<TreeItem> event) {
	        final TreeItem item = event.getTarget();
	        if (item.getChildCount() <= 1) {
		        if (item.getText().contentEquals("Vital Signs")){
		        	item.setState(false,false);
		        	item.addItem(getVitalsTreeItemPanel("Arterial Blood Pressure"));
		        	item.addItem(getVitalsTreeItemPanel("Heart Rate"));	
		        	item.addItem(getVitalsTreeItemPanel("Temperature C"));
		        	item.addItem(getVitalsTreeItemPanel("SpO2"));
		        	item.addItem(getVitalsTreeItemPanel("Respiratory Rate"));
		        }
	        	
	        }
	        
	      }
	    });

	    vitalsTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				// TODO Auto-generated method stub
				if (splitPanel.getWidgetIndex(note) > -1) {
					splitPanel.remove(note);	
				}

				if (splitPanel.getWidgetIndex(labPanel) > -1 ) {
					splitPanel.remove(labPanel);
				}
				
				if (splitPanel.getWidgetIndex(vitalsPanel) == -1 ) {
					splitPanel.add(vitalsPanel);
				}
				
				 TreeItem selectedItem = event.getSelectedItem();
				 System.out.println(selectedItem.getText());
				 if (hChartEvents.containsKey(selectedItem.getText())) {
					 String key = selectedItem.getText();
					 	boolean bBPHack = key.equalsIgnoreCase("Arterial Blood Pressure");
						addVitalsToVisualiztion(key, bBPHack);
				 }
					 
			}

	    });
	}
	/*
	private void clearLabTree() {
		labTree.clear();
	    TreeItem labRoot = new TreeItem("Labs");
	    TreeItem panelRoot = new TreeItem("By Panel");
	    panelRoot.addItem(new TreeItem());
	    
	    labRoot.addItem(panelRoot);
	    TreeItem item = new TreeItem("By Test");
	    item.addItem(new TreeItem(""));
	    labRoot.addItem(item);
	    labRoot.addItem(new TreeItem("By Problem"));
	    labRoot.addItem(new TreeItem("By Time"));
	    
	    labTree.addItem(labRoot);
	}
	*/
}
