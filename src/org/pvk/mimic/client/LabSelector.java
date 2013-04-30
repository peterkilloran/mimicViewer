package org.pvk.mimic.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class LabSelector {

	private Tree labTree;
	private MimicData data;
	private ScrollPanel panel;
	private MimicDataUtils utils;
	
	public LabSelector(MimicData mimicDataObject) {
		this.labTree = new Tree();
		this.data=mimicDataObject;
		this.utils = new MimicDataUtils();
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
		initializeLabTree();
	}

	public ScrollPanel getLabTreeScrollPanel () {
		this.panel = new ScrollPanel();
		this.panel.setTitle("Lab Results");
		initializeLabTree();
		this.panel.add(labTree);
		return panel;
	}
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
							for (String key : data.gethLabLoincResults().keySet()) {
								if (data.gethLabLoincResults().containsKey(key)) {
									// same loinc can be found for different sources (i.e., loinc for hgb is the same for abg and cbc		  		        			
									item.addItem(getLabTreeItemPanel(key));		  		        			
								}

							}
							item.setState(true,false);


						} else if (item.getText().contentEquals("By Panel")) {
							item.setState(false,false);
							TreeItem cbcItem = new TreeItem("CBC");
							TreeItem chemItem = new TreeItem("Chemistry");
							TreeItem abgItem = new TreeItem("ABG");
							TreeItem lftItem = new TreeItem("LFT");

							for (String key : utils.gethCbcPanel().keySet()) {
								if (data.gethLabLoincResults().containsKey(key)) {
									// same loinc can be found for different sources (i.e., loinc for hgb is the same for abg and cbc
									if (data.gethLabLoincResults().get(key).get(0).getCategory().toString().equalsIgnoreCase("HEMATOLOGY")) {
										cbcItem.addItem(getLabTreeItemPanel(key));		  		        			
									}
								}
							}
							if (cbcItem.getChildCount()>0) {
								item.addItem(cbcItem);
							}

							for (String key : utils.gethChemPanel().keySet()) {
								if (data.gethLabLoincResults().containsKey(key)) {
									// differentiate loinc from different sources (i.e., loinc for hgb is the same for abg and cbc
									if (data.gethLabLoincResults().get(key).get(0).getCategory().toString().equalsIgnoreCase("CHEMISTRY")) {
										chemItem.addItem(getLabTreeItemPanel(key));		  		        			
									}
								}
							}
							if (chemItem.getChildCount()>0) {
								item.addItem(chemItem);
							}

							for (String key : utils.gethAbgPanel().keySet()) {
								if (data.gethLabLoincResults().containsKey(key)) {
									// differentiate loinc from different sources (i.e., loinc for hgb is the same for abg and cbc
									if (data.gethLabLoincResults().get(key).get(0).getCategory().toString().equalsIgnoreCase("BLOOD GAS")) {
										abgItem.addItem(getLabTreeItemPanel(key));		  		        					  		        		
									}
								}
							}
							if (abgItem.getChildCount()>0) {
								item.addItem(abgItem);
							}


							item.setState(true,false);

						}
					}
				}

			}

		});

	}
	private HorizontalPanel getLabTreeItemPanel(String loinc) {
  		HorizontalPanel hp = new HorizontalPanel();
  		Label l = new Label(loinc);
  		l.setVisible(false);
  		CheckBox cb = new CheckBox(data.gethLabLoincResults().get(loinc).get(0).getTestName());
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
				/*
				if (checked) {
					addLabToVisualiztion(child.getText().toString());
					addLabResultsToGrid(child.getText().toString());
				} else {
					removeLabFromVisualization(child.getText().toString());
					removeLabResultsFromGrid(child.getText().toString());
				}
				*/
			}});
  		return hp;
	}
}
