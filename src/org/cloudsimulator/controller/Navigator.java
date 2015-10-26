/* Icaro Cloud Simulator (ICLOS).
   Copyright (C) 2015 DISIT Lab http://www.disit.org - University of Florence

   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. */

package org.cloudsimulator.controller;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.simulator.DataCenterSimulatorRealTime;

@Named("navigator")
@ApplicationScoped
public class Navigator {

    private boolean analyzeMetrics;
    private boolean home;
    private boolean cloneStructure;
    private boolean collectData;
    private boolean configuration;
    private boolean dataCenter;
    private boolean dataCenterSimulationChoice;
    private boolean dataCenterSimulationFaster;
    private boolean dataCenterSimulationRealTime;
    private boolean dataCenterVirtualMachinePlacementChoice;
    private boolean dataCenterVirtualMachinePlacement;
    private boolean dataCenterVirtualMachinePlacementDetail;
    private boolean vmPlacementTestRunner;    
    private boolean dataCenterChoice;
    private boolean businessConfiguration;
    private boolean serviceMetric;
    private boolean virtualMachine;
    private boolean visualizeXMLDataCenter;
    private boolean visualizeXMLBusinessConfiguration;
    private boolean visualizeXMLServiceMetric;
    private boolean dataCenterTestBuilderPlacementController;
    private String title;
    private String originalViewId;
    @Inject
    private Conversation conversation;
    @Inject
    private DataCenterController dataCenterController;
    @Inject
    private DataCenterSimulatorRealTime dataCenterControllerSimulator;
    private boolean dataCenterVirtualMachinePlacementAnalyzeResult;
  
    public void reset() {
        this.analyzeMetrics = false;
        this.home = false;
        this.cloneStructure = false;
        this.collectData = false;
        this.configuration = false;
        this.dataCenter = false;
        this.dataCenterSimulationChoice = false;
        this.dataCenterSimulationFaster = false;
        this.dataCenterSimulationRealTime = false;
        this.dataCenterChoice = false;
        this.businessConfiguration = false;
        this.serviceMetric = false;
        this.virtualMachine = false;
        this.visualizeXMLDataCenter = false;
        this.visualizeXMLBusinessConfiguration = false;
        this.visualizeXMLServiceMetric = false;
        this.title = "";
        this.dataCenterVirtualMachinePlacement = false;
        this.dataCenterVirtualMachinePlacementChoice = false;
        this.setDataCenterVirtualMachinePlacementDetail(false);
        this.dataCenterVirtualMachinePlacementAnalyzeResult = false;
        this.vmPlacementTestRunner = false;
    }

    public void goToAnalyzeMetrics() {
        reset();
        this.analyzeMetrics = true;
        this.title = "Analyze Metrics of <strong>"
                + dataCenterController.getDataCenterChoice().replace(
                        DataCenter.PATHURI, "") + "</strong>";
    }

    public void goToCloneStructure() {
        reset();
        this.cloneStructure = true;
        this.title = "Clone Structure";
    }

    public void goToCollectData() {
        reset();
        this.collectData = true;
        this.title = "Collect Data";
    }

    public void goToConfiguration() {
        reset();
        this.configuration = true;
        this.title = "Configuration Tools";
    }

    public void goToHome() {
        reset();
        this.home = true;
        this.title = "";
    }

    public void goToDataCenter() {
        reset();
        this.dataCenter = true;
        this.title = "Create a Data Center";
    }

    public void goToDataCenterSimulationChoice() {
        reset();
        this.dataCenterSimulationChoice = true;
        this.title = "";
    }

    public void goToDataCenterSimulationFaster() {
        reset();
        this.dataCenterSimulationFaster = true;
        this.title = "Simulate Data Center <strong>"
                + dataCenterController.getDataCenterChoice().replace(
                        DataCenter.PATHURI, "") + "</strong> Faster";
    }
    
    public void goToDataCenterVirtualMachinePlacement() {
        reset();
        this.dataCenterVirtualMachinePlacement = true;
        this.title = "Data Center virtual machines placement <strong>"
                + dataCenterController.getDataCenterChoice().replace(
                        DataCenter.PATHURI, "") + "</strong>";
    }
    
    public void goToVmPlacementTestRunner() {
        reset();
        this.vmPlacementTestRunner = true;
        this.title = "Placement test runner";
    }
    
    public void goToDataCenterVirtualMachinePlacementChoice() {
        reset();
        this.dataCenterVirtualMachinePlacementChoice = true;
        this.title = "Data Center virtual machines placement ";
    }

    public void goToDataCenterVirtualMachinePlacementDetail() {
        reset();
        this.setDataCenterVirtualMachinePlacementDetail(true);
        this.title = "Virtual machines placement detail";
    }
    
    public void goToDataCenterVirtualMachinePlacementAnalyzeResult() {
        reset();
        this.dataCenterVirtualMachinePlacementAnalyzeResult = true;
        this.title = "Simulation results";
    }
    
    public void goToDataCenterSimulationRealTime() {
        reset();
        this.dataCenterSimulationRealTime = true;
        this.title = "Simulate Data Center <strong>"
                + dataCenterController.getDataCenterChoice().replace(
                        DataCenter.PATHURI, "") + "</strong> Real Time";
    }

    public void goToDataCenterChoice() {
        reset();
        this.dataCenterChoice = true;
        this.title = "Choose dataCenter";
    }

    public void goToBusinessConfiguration() {
        reset();
        this.businessConfiguration = true;
        this.title = "Create a BusinessConfiguration";
    }

    public void goToServiceMetric() {
        reset();
        this.serviceMetric = true;
        this.title = "Create ServiceMetrics";
    }

    public void goToVirtualMachine() {
        reset();
        this.virtualMachine = true;
        this.title = "Create VirtualMachine on DataCenter <strong>"
                + dataCenterController.getDataCenterChoice().replace(
                        DataCenter.PATHURI, "") + "</strong>";
    }

    public void goToVisualizeXMLDataCenter() {
        reset();
        this.visualizeXMLDataCenter = true;
        this.title = "Visualize XML DC";
    }

    public void goToVisualizeXMLBusinessConfiguration() {
        reset();
        this.visualizeXMLBusinessConfiguration = true;
        this.title = "Visualize XML BC";
    }

    public void goToVisualizeXMLServiceMetric() {
        reset();
        this.visualizeXMLServiceMetric = true;
        this.title = "Visualize XML SM";
    }
    
    public void goToDataCenterTestBuilderPlacementController(){
        reset();
        this.dataCenterTestBuilderPlacementController = true;
        this.title = "Available test case for vms allocation";
    }

    public void goToRightPage() {
        String viewId = this.originalViewId;
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ViewHandler viewHandler = app.getViewHandler();
        UIViewRoot root = viewHandler.createView(context, viewId);
        context.setViewRoot(root);
        if ("/datacenter/dataCenterSimulationRealTime.xhtml".equals(viewId)) {
            this.dataCenterControllerSimulator.initHostMachineSimulatorList();
        }
        if ("/datacenter/dataCenterVirtualMachinePlacement.xhtml".equals(viewId)) {
            this.dataCenterController.getDataCenterVirtualMachinePlacementSimulator().setDataCenter(dataCenterController.getDataCenter());
        }
        if ("/datacenter/vmPlacementTestRunner.xhtml".equals(viewId)) {
            this.dataCenterController.getPlacementSimulatorTestRunner().setDataCenter(dataCenterController.getDataCenter());            
        }
    }

    public void beginConversation() {
        if (conversation.isTransient()) {
            conversation.setTimeout(86400000);
            conversation.begin();
        }
    }

    public void endConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    public boolean isAnalyzeMetrics() {
        return this.analyzeMetrics;
    }

    public void setAnalyzeMetrics(final boolean analyzeMetrics) {
        this.analyzeMetrics = analyzeMetrics;
    }

    public boolean isHome() {
        return this.home;
    }

    public void setHome(final boolean home) {
        this.home = home;
    }

    public boolean isCloneStructure() {
        return cloneStructure;
    }

    public void setCloneStructure(boolean cloneStructure) {
        this.cloneStructure = cloneStructure;
    }

    public boolean isCollectData() {
        return this.collectData;
    }

    public void setCollectData(boolean collectData) {
        this.collectData = collectData;
    }

    public boolean isConfiguration() {
        return configuration;
    }

    public void setConfiguration(boolean configuration) {
        this.configuration = configuration;
    }

    public boolean isDataCenter() {
        return this.dataCenter;
    }

    public void setDataCenter(final boolean dataCenter) {
        this.dataCenter = dataCenter;
    }

    public boolean isDataCenterSimulationChoice() {
        return this.dataCenterSimulationChoice;
    }

    public void setDataCenterSimulationChoice(boolean dataCenterSimulationChoice) {
        this.dataCenterSimulationChoice = dataCenterSimulationChoice;
    }

    public boolean isDataCenterSimulationFaster() {
        return this.dataCenterSimulationFaster;
    }

    public void setDataCenterSimulationFaster(
            final boolean dataCenterSimulationFaster) {
        this.dataCenterSimulationFaster = dataCenterSimulationFaster;
    }
    
    public boolean isDataCenterVirtualMachinePlacement() {
        return this.dataCenterVirtualMachinePlacement;
    }

    public void setDataCenterVirtualMachinePlacement(
            final boolean dataCenterVirtualMachinePlacement) {
        this.dataCenterVirtualMachinePlacement = dataCenterVirtualMachinePlacement;
    }

    public boolean isDataCenterSimulationRealTime() {
        return this.dataCenterSimulationRealTime;
    }

    public void setDataCenterSimulationRealTime(
            boolean dataCenterSimulationRealTime) {
        this.dataCenterSimulationRealTime = dataCenterSimulationRealTime;
    }

    public boolean isDataCenterChoice() {
        return this.dataCenterChoice;
    }

    public void setDataCenterChoice(final boolean dataCenterChoice) {
        this.dataCenterChoice = dataCenterChoice;
    }

    public boolean isBusinessConfiguration() {
        return this.businessConfiguration;
    }

    public void setBusinessConfiguration(final boolean businessConfiguration) {
        this.businessConfiguration = businessConfiguration;
    }

    public boolean isServiceMetric() {
        return this.serviceMetric;
    }

    public void setServiceMetric(boolean serviceMetric) {
        this.serviceMetric = serviceMetric;
    }

    public boolean isVirtualMachine() {
        return this.virtualMachine;
    }

    public void setVirtualMachine(final boolean virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public boolean isVisualizeXMLDataCenter() {
        return this.visualizeXMLDataCenter;
    }

    public void setVisualizeXMLDataCenter(final boolean visualizeXMLDataCenter) {
        this.visualizeXMLDataCenter = visualizeXMLDataCenter;
    }

    public boolean isVisualizeXMLBusinessConfiguration() {
        return this.visualizeXMLBusinessConfiguration;
    }

    public void setVisualizeXMLBusinessConfiguration(
            final boolean visualizeXMLBusinessConfiguration) {
        this.visualizeXMLBusinessConfiguration = visualizeXMLBusinessConfiguration;
    }

    public boolean isVisualizeXMLServiceMetric() {
        return this.visualizeXMLServiceMetric;
    }

    public void setVisualizeXMLServiceMetric(boolean visualizeXMLServiceMetric) {
        this.visualizeXMLServiceMetric = visualizeXMLServiceMetric;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getOriginalViewId() {
        return this.originalViewId;
    }

    public void setOriginalViewId(final String originalViewId) {
        this.originalViewId = originalViewId;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(final Conversation conversation) {
        this.conversation = conversation;
    }

    public boolean isDataCenterVirtualMachinePlacementDetail() {
        return dataCenterVirtualMachinePlacementDetail;
    }

    public void setDataCenterVirtualMachinePlacementDetail(
            boolean dataCenterVirtualMachinePlacementDetail) {
        this.dataCenterVirtualMachinePlacementDetail = dataCenterVirtualMachinePlacementDetail;
    }

    public boolean isDataCenterVirtualMachinePlacementChoice() {
        return dataCenterVirtualMachinePlacementChoice;
    }

    public void setDataCenterVirtualMachinePlacementChoice(
            boolean dataCenterVirtualMachinePlacementChoice) {
        this.dataCenterVirtualMachinePlacementChoice = dataCenterVirtualMachinePlacementChoice;
    }

    public boolean isDataCenterTestBuilderPlacementController() {
        return dataCenterTestBuilderPlacementController;
    }

    public void setDataCenterTestBuilderPlacementController(
            boolean dataCenterTestBuilderPlacementController) {
        this.dataCenterTestBuilderPlacementController = dataCenterTestBuilderPlacementController;
    }

}
