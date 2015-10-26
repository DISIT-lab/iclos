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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
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
import javax.servlet.http.Part;
import javax.xml.ws.WebServiceException;

import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.SimulationResultWrapper;
import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;

@Named("placementARCtrl")
@ConversationScoped
public class VmPlacementAnalyzeResult implements Serializable{
    
            
    /**
     * 
     */
    private static final long serialVersionUID = 6671959147583177652L;
    
    @Inject
    protected DataCenterController dataCenterController;
    private SimulationResultWrapper simulationResultWrapper;
    private boolean loadingIteration = false;
    private Part uploadediterationResult;
    
    public VmPlacementAnalyzeResult() {
    
    }
    
    public SimulationResultWrapper getSimulationResultWrapper() {
        return simulationResultWrapper;
    }
    
    public Part getUploadediterationResult() {
        return uploadediterationResult;
    }
    
    public void hideLoadIterationForm(){
        this.setLoadingIteration(false);
    }
    
    public void init(){        
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        Map<String, String> requestParameters = externalContext.getRequestParameterMap();
        if(dataCenterController.getDataCenterVirtualMachinePlacementSimulator().getSimulationResultWrapper() != null)
            setSimulationResultWrapper(dataCenterController.getDataCenterVirtualMachinePlacementSimulator().getSimulationResultWrapper());
        
    }
    
    public boolean isLoadingIteration() {
        return loadingIteration;
    }
    
    public void loadIteration() throws IOException, ClassNotFoundException{
        if(uploadediterationResult != null){
            InputStream inputStream = uploadediterationResult.getInputStream();                
            ObjectInputStream ois = new ObjectInputStream(inputStream);

            SimulationResultWrapper result = (SimulationResultWrapper) ois.readObject();
//            result.getDataCenterMachinePlacement().buildChartModel();
//            if(this.simulationResults == null)
//                this.simulationResults = new ArrayList<PlacementResult>();
//            this.simulationResults.add(result);
//            setSelectedPlacementResult(result);
            setSimulationResultWrapper(result);
        }
        this.setLoadingIteration(false);        
    }
    
    protected void prepareDownload(String fileName, String tempFileName,
            Serializable toSerialize) throws IOException,
            FileNotFoundException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();               
        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.        
        /* WRITE SERIALIZED FILE ON DISK */

        File f = File.createTempFile(tempFileName,".ser" , null);
        String path = f.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toSerialize);
        oos.close();
        /*                              */


        File written = new File(path);
        OutputStream output = ec.getResponseOutputStream();

        ec.setResponseContentType("application/octet-stream"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseContentLength((int)written.length()); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        Files.copy(written.toPath(), output);

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
    
    public void saveIteration() throws IOException{
        String fileName = "icaroPlacementIterationResult.ser";
        String tempFileName = "icaroPlacIter";
        SimulationResultWrapper toSerialize = this.simulationResultWrapper;

        prepareDownload(fileName, tempFileName, toSerialize);
    }
        
    public void setLoadingIteration(boolean loadingIteration) {
        this.loadingIteration = loadingIteration;
    }   
    
    
    public void setSimulationResultWrapper(SimulationResultWrapper simulationResultWrapper) {
        this.simulationResultWrapper = simulationResultWrapper;
        this.dataCenterController.getDataCenterVirtualMachinePlacementSimulator().setSimulationResultWrapper(simulationResultWrapper);
        for (PlacementResult placementResult : this.simulationResultWrapper.getPlacementResults()) {
            
//            for (HostMachinePlacement hostMachinePlacement : placementResult.getUsedHost()) {
//                hostMachinePlacement.buildStatistics();
//            }
            
            placementResult.getDataCenterMachinePlacement().buildChartModel();
        }
    }

    public void setUploadediterationResult(Part uploadediterationResult) {
        this.uploadediterationResult = uploadediterationResult;
    }

    public String showIterationDetail(String id){
        
        return "dataCenterVirtualMachinePlacementIterationDetail?faces-redirect=true&includeViewParams=true&id="+id;
    }

    public void showLoadIterationForm(){
        this.setLoadingIteration(true);
    }

   
}
