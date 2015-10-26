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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.ontology.BaseMachinePlacementBase;
import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;

@Named("placementIHCtrl")
@ConversationScoped
public class VmPlacementHostDetail implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4230643908784835288L;
    //@ManagedProperty("#{param.id}")
    private String id;    
    //@ManagedProperty("#{param.hostName}")
    private String hostName;    
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Inject
    protected DataCenterController dataCenterController;
    private PlacementResult selectedIteration;
    private HostMachinePlacement machinePlacement;
    private DataCenterMachinePlacement dc;
    
//    private VirtualMachinePlacement vmPlacement;
    
    
    
    private boolean showHostOverallChart = true;
    
    public VmPlacementHostDetail() {
    
    }
    
    public void init(){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameters = externalContext.getRequestParameterMap();
        if (requestParameters.containsKey("id") && requestParameters.containsKey("hostName")) {
            setId(requestParameters.get("id"));
            setHostName(requestParameters.get("hostName"));
         
            int id = Integer.parseInt(getId());        
            
            for (PlacementResult result : dataCenterController.getDataCenterVirtualMachinePlacementSimulator().getSimulationResultWrapper().getPlacementResults()) {
            
                if(result.getId() == id){
                    this.setSelectedIteration(result);
                    setDc(result.getDataCenterMachinePlacement());
                    for (HostMachinePlacement host : result.getDataCenterMachinePlacement().getAssignedMachines()) {
                        if(host.getMachine().getHasName().equals(getHostName())){                        
                            machinePlacement = host;
                            machinePlacement.buildChartModel();
                            break;
                        }
                    }
                    
                }
                    
            }
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlacementResult getSelectedIteration() {
        return selectedIteration;
    }

    public void setSelectedIteration(PlacementResult selectedIteration) {
        this.selectedIteration = selectedIteration;
    }

    public HostMachinePlacement getMachinePlacement() {
        return machinePlacement;
    }

    public void setMachinePlacement(HostMachinePlacement machinePlacement) {
        this.machinePlacement = machinePlacement;
    }
    
    public void toggleChartView(){
        this.showHostOverallChart = ! this.showHostOverallChart;
    }
    
    public boolean isShowHostOverallChart() {
        return showHostOverallChart;
    }
    
    public void setShowHostOverallChart(boolean showHostOverallChart) {
        this.showHostOverallChart = showHostOverallChart;
    }
        
    public String showVmDetail(int id,String hostName,String vmName){
        return "dataCenterVirtualMachinePlacementVmDetail?faces-redirect=true&includeViewParams=true&id="+id+"&hostName="+hostName+"&vmName="+vmName;
//        for (VirtualMachinePlacement virtualMachinePlacement : machinePlacement.getAssignedMachines()) {
//            if(virtualMachinePlacement.getMachine().getHasName().equals(name)){
//                vmPlacement = virtualMachinePlacement;
//                vmPlacement.buildChartModel();
//                break;
//            }
//        }
    }

    public DataCenterMachinePlacement getDc() {
        return dc;
    }

    public void setDc(DataCenterMachinePlacement dc) {
        this.dc = dc;
    }

//    public VirtualMachinePlacement getVmPlacement() {
//        return vmPlacement;
//    }
//
//    public void setVmPlacement(VirtualMachinePlacement vmPlacement) {
//        this.vmPlacement = vmPlacement;
//    }
}
