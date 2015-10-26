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
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;

import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;

@Named("placementIDCtrl")
@ConversationScoped
public class VmPlacementIterationDetail implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -7676251215990053009L;
//    @ManagedProperty("#{param.id}") non vanno conil cdi named
    private String id;    
    @Inject
    protected DataCenterController dataCenterController;
    private PlacementResult selectedIteration;
    private DataCenterMachinePlacement machinePlacement;
    
    
    
    private boolean showHostOverallChart = true;
    private boolean showDataCenterOverallChart = true;
    
    public VmPlacementIterationDetail() {
    
    }
    
//    @PostConstruct
//    public void postInit(){
//        String id_ = null;
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        Map<String, String> requestParameters = externalContext.getRequestParameterMap();
//        if (requestParameters.containsKey("id")) {
//           setId(requestParameters.get("id"));
//        } else {
//           throw new WebServiceException("No item id in request parameters");
//        }
//    }
    
    public void init(){        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameters = externalContext.getRequestParameterMap();
        if (requestParameters.containsKey("id")) {
           setId(requestParameters.get("id"));
        
            int id = Integer.parseInt(getId());
            
            for (PlacementResult result : dataCenterController.getDataCenterVirtualMachinePlacementSimulator().getSimulationResultWrapper().getPlacementResults()) {
            
                if(result.getId() == id){
                    this.setSelectedIteration(result);
                    this.setMachinePlacement(result.getDataCenterMachinePlacement());
                    break;
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

    public DataCenterMachinePlacement getMachinePlacement() {
        return machinePlacement;
    }

    public void setMachinePlacement(DataCenterMachinePlacement machinePlacement) {
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
    
    public void toggleDataCenterChartView(){
        this.showDataCenterOverallChart = ! this.showDataCenterOverallChart;
    }
    
    public boolean isShowDataCenterOverallChart() {
        return showDataCenterOverallChart;
    }
    
    public void setShowDataCenterOverallChart(boolean showDataCenterOverallChart) {
        this.showDataCenterOverallChart = showDataCenterOverallChart;
    }
    
    public String showHostDetail(int id,String name){
        
        return "dataCenterVirtualMachinePlacementHostDetail?faces-redirect=true&includeViewParams=true&id="+id+"&hostName="+name;
    }
}
