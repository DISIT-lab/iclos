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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.placement.AddedHostDetail;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.PlacementResultExt;
import org.cloudsimulator.placement.SimulationResultWrapper;
import org.cloudsimulator.placement.TestCaseMachineAssignament;
import org.cloudsimulator.placement.TestCasePatternWrapper;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.controller.PlacementControllerBinCentric;
import org.cloudsimulator.placement.controller.PlacementControllerItemCentric;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientFactory;
import org.cloudsimulator.placement.heuristic.HeuristicControllerFFD;
import org.cloudsimulator.placement.heuristic.HeuristicControllerFactory;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;
import org.cloudsimulator.utility.RatioNumberContainer;
import org.jboss.weld.security.GetAccessibleCopyOfMember;

@Named("placementSimulatorTestRunner")
public class PlacementSimulatorTestRunner extends DataCenterVirtualMachinePlacementSimulator {
         
   
    /**
     * 
     */
    private static final long serialVersionUID = 9148281207430115230L;
    
    private ArrayList<RatioNumberContainer> vmScaleFactor;
    private String autoExportResultPath = "/home/debian/Desktop/placement_results";
    private String dataCenterSizes = "50,100";
    private boolean exportResultObject = false;

    public PlacementSimulatorTestRunner() {
        super();      
        this.vmScaleFactor = new ArrayList<RatioNumberContainer>();
        
        vmScaleFactor.add(new RatioNumberContainer(50, 10));
        vmScaleFactor.add(new RatioNumberContainer(100, 20));
        vmScaleFactor.add(new RatioNumberContainer(200, 20));
        vmScaleFactor.add(new RatioNumberContainer(250, 20));
        vmScaleFactor.add(new RatioNumberContainer(300, 20));
        vmScaleFactor.add(new RatioNumberContainer(350, 10));
        
//        this.n_runs = 10;
//        this.n_simulation = 100;
        
    }


    public ArrayList<RatioNumberContainer> getVmScaleFactor() {
        return vmScaleFactor;
    }


    @Override
    public void run() {        
        
        /*      USO SEMPRE GLI STESSI COEFFICIENTI      */
        HeuristicCoefficientBuilder coeffBuilder = HeuristicCoefficientFactory
                .getInstance(getSelectedEuristicCoefficientBuilder());
        
        simulationEnded = false;
        ArrayList<String> heuristicControllers = new ArrayList<String>();
        heuristicControllers.add(HeuristicControllerFactory.DOT_PRODUCT);
        heuristicControllers.add(HeuristicControllerFactory.DOT_PRODUCT_GRASP);
        heuristicControllers.add(HeuristicControllerFactory.L2_NORM);
        heuristicControllers.add(HeuristicControllerFactory.L2_NORM_GRASP);
        heuristicControllers.add(HeuristicControllerFactory.FFD_PRODUCT);
        heuristicControllers.add(HeuristicControllerFactory.FFD_SUM);
        
        AddedHostDetail addedHostDetail = new AddedHostDetail(addedHostCpuCount, addedHostCpuFreq, addedHostMemSize);
        
        ArrayList<Integer> dim_to_simulate = new ArrayList<Integer>();
        
        String[] sizes = getDataCenterSizes().split(",");
        for (int i = 0; i < sizes.length; i++) {
            dim_to_simulate.add(Integer.parseInt(sizes[i]));                
        }        
        if(dim_to_simulate.size() == 0){
            dim_to_simulate.add(50);    
        }
        
        ArrayList<String> toRunTestCase = new ArrayList<String>();
        
        if(selectedTestCase == null || selectedTestCase.isEmpty()){
            toRunTestCase.addAll(availableTestCases);
        }else
        {
            toRunTestCase.add(getSelectedTestCase());
        }
        
        /* 
         * SET VM PARAMS
         */
        float vmCPUSpeedReservation = 800f;
        float vmCPUSpeedLimit = 2000f;
        float vmMemorySize = 6f;
        float vmMemoryReservation = 1f;
        float vmHasMemoryLimit = 2f;
        
        /*
         * INVECE DI CREARE UN DATACENTER CREO QUELLO CHE MI SERVE IN MEMORIA
         * PERRCHÈ HO TROPPI VINCOLI NEL CASO DI GRANDI DC
         * E NON RIESCO A CREARE PIÙ DC O PIÙ BC ALLO STESSO TEMPO
         */
        
        
        /*      CARICO UN CERTO DATA CENTER CON RELATIVA BC
        
        DataCenterDAO dataCenterDAO = new DataCenterDAO();
        String dataCenterChoice="urn:cloudicaro:DataCenter:" + currentDC;
        this.dataCenter = dataCenterDAO.fetchDataCenterByURI(this.ipOfKB,dataCenterChoice);
        this.dataCenterController.setDataCenter(this.dataCenter);
        
        */
        
        
        /*
         * CREATE A FAKE DC INSTEAD TO CREATE A REAL BC
         */
        DataCenter dataCenter = new DataCenter("Testing", "Testing", "Testing");
        NetworkAdapter fakeNetworkAdapter = new NetworkAdapter("192.168.0.1","192.168.0.1");
        ArrayList<NetworkAdapter> fakeNetworkAdapters = new ArrayList<NetworkAdapter>();
        fakeNetworkAdapters.add(fakeNetworkAdapter);
        HostMachine fakeHost = new HostMachine(
                dataCenter.getLocalUri() + "_HM1",
                "Linux",
                fakeNetworkAdapters,
                new ArrayList<MonitorInfo>(),
                new ArrayList<LocalStorage>(),
                new ArrayList<String>(),
                addedHostCpuCount,
                addedHostCpuFreq,
                "Intel Xeon",
                dataCenter.getLocalUri() + "_HM1",
                addedHostMemSize,
                "",
                "", 
                "",
                10000f,
                "",
                "");
        
        dataCenter.getHostMachineList().add(fakeHost);        
        
        ArrayList<VirtualMachine> virtualMachineList = null;
        
        /*
         * MUST RUN EACH TEST FOR ALL THE DC SIZE
         */
        for (Integer dim : dim_to_simulate) {
            int n_VM = dim;
        
            virtualMachineList = new ArrayList<VirtualMachine>();            
            float[] scalingFactor = buildScalingFactorArray(n_VM);
            
            /* build fake vms */            
            for (int j = 0; j < n_VM; j++) {
                String suffixVirtualMachine = dataCenter.getHasName() + "_VM" + (j+1);
                
                float currentScale = scalingFactor[j];
    
                virtualMachineList.add(new VirtualMachine(
                        suffixVirtualMachine,
                        "",
                        suffixVirtualMachine,
                        1,
                        vmCPUSpeedReservation * currentScale,
                        vmCPUSpeedLimit * currentScale,
                        vmMemorySize * currentScale,
                        vmMemoryReservation * currentScale, 
                        vmHasMemoryLimit * currentScale,
                        "",
                        new ArrayList<VirtualStorage>(),
                        fakeNetworkAdapters,
                        new ArrayList<MonitorInfo>(),
                        "Linux",
                        fakeHost.getHasName(),
                        "", "","","",""));               
            }
            
            long seed = System.nanoTime();
            Collections.shuffle(virtualMachineList, new Random(seed));
            
            fakeHost.setVirtualMachineList(virtualMachineList);
            
            /*
             * MUST KEEP THIS HERE ELSE THE COLLECTION WILL NOT BE CORRECTLY INITIALIZED
             */
            setDataCenter(dataCenter);
            /*
             * MUST KEEP THIS HERE ELSE THE COLLECTION WILL NOT BE CORRECTLY INITIALIZED
             */
            
            String currentDC = "TEST-" + dim.toString();
            
            this.setIterationStatus(String.format("DC %s", currentDC));   
            
            /*
             * MUST RUN ALL AVAILABLE TEST CASE RATION FOR EACH DC
             */
            for (String testCase : toRunTestCase) {
                
                if(testCase.equals("")) continue;
                
                ArrayList<SimulationResultWrapper> simulationResultWrappers = new ArrayList<SimulationResultWrapper>();
            
                this.setIterationStatus(String.format("DC %s......Test case is %s", currentDC,testCase)); 
                     
                String resPath = this.autoExportResultPath + "/" + currentDC + "/" + testCase;
                    
                /*      IN BASE AL TEST CASE, ASSEGNO I PATTERN         */
                this.selectedTestCase = testCase;
                
                StringBuffer testAssignamentString = new StringBuffer();
                for (TestCaseMachineAssignament assignament : testCaseMachineAssignament) {
                    testAssignamentString.append(String.format("", assignament.getTestCase().getName(),assignament.getMachinesWithPattern()));
                }
                
                runHeuristic(coeffBuilder, heuristicControllers,
                        addedHostDetail, dataCenter, currentDC, testCase,
                        simulationResultWrappers, testAssignamentString);                     
                
                if(simulationResultWrappers.size() > 0){
                    exportSimulationResults(currentDC,testCase,simulationResultWrappers);
                }
                PlacementResult.setIncremental(0);
            }
        }
        
        simulationEnded = true;
        this.dataCenterController.setPlacementSimulatorTestRunnerStarted(false);
        this.refreshTime = 1200;
    }


    protected void runHeuristic(
            HeuristicCoefficientBuilder coeffBuilder,
            ArrayList<String> heuristicControllers,
            AddedHostDetail addedHostDetail, DataCenter dataCenter,
            String currentDC, String testCase,
            ArrayList<SimulationResultWrapper> simulationResultWrappers,
            StringBuffer testAssignamentString) {
        for (String ecName : heuristicControllers) {
        
            ArrayList<PlacementResult> placementResults = new ArrayList<PlacementResult>();
            
            this.setIterationStatus(String.format("DC %s...Test case is %s...Heuristic controller %s",
                    currentDC,testCase,ecName));                                                       
            
            /*
             * ASSIGN DIFFERENT PATTERN FOR EACH ITERATION
             */
            assignPatternBasedOnTestCase();
            
            runIteration(addedHostDetail,
                    dataCenter, currentDC, testCase, ecName,
                    placementResults);              
            
             if(placementResults.size() > 0){
                 SimulationResultWrapper simulationResultWrapper = new SimulationResultWrapper(ecName, coeffBuilder.getMethodName(),
                 placementResults, this.originalVirtualMachineList, getVirtualMachineList(),testAssignamentString.toString(),
                 selectedTestCase,reservedOsHostResource,hostMaxRisk,maxOverprovisioningTime,n_runs,n_simulation);    
                 
                 if(isExportResultObject()){
                     Path resultPath = Paths.get(this.autoExportResultPath,currentDC,testCase);                             
                     try {
                         exportPlacementResultObject(resultPath,simulationResultWrapper.getEuristicMethod(),simulationResultWrapper);
                     }catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 simulationResultWrappers.add(simulationResultWrapper);
             }
        }
    }


    protected void runIteration(            
            AddedHostDetail addedHostDetail, DataCenter dataCenter,
            String currentDC, String testCase, String ecName,
            ArrayList<PlacementResult> placementResults) {
        
        /*      USO SEMPRE GLI STESSI COEFFICIENTI      */
        HeuristicCoefficientBuilder coeffBuilder = HeuristicCoefficientFactory
                .getInstance(HeuristicCoefficientFactory.EXP_AVG_DEMAND);
        for (int i = 0; i < n_simulation; i++) {
            
            this.setIterationStatus(String.format("DC %s...Test case is %s..."
                    + "Heuristic controller %s...Iteration %d",
                    currentDC,testCase,ecName,i+1));                                                       
                                                            
            ArrayList<VmResourceValueContainer> vmResourceValueContainers = pullResourceValue();
            
            HeuristicInterface ec = HeuristicControllerFactory
                    .getInstance(ecName);                       
            
            simulationEnded = false;
            
            if(ec instanceof HeuristicControllerFFD){
                placementController = new PlacementControllerItemCentric(dataCenter, hostMaxRisk, 
                        maxOverprovisioningTime, 
                        reservedOsHostResource, 
                        days, n_runs, DAYSAMPLE,addedHostDetail,getVirtualMachineList());
            }else{
                placementController = new PlacementControllerBinCentric(dataCenter, hostMaxRisk, 
                        maxOverprovisioningTime, 
                        reservedOsHostResource, 
                        days, n_runs, DAYSAMPLE,addedHostDetail,getVirtualMachineList());
            }    
            
            PlacementResult currentPlacementResult = null;
            try {
                currentPlacementResult = placementController.allocateVm( 
                        vmResourceValueContainers, ec, coeffBuilder, stopSimulationIfNotFitting,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if(currentPlacementResult != null){
                placementController.computeIterationStatistics(currentPlacementResult);                           
                placementResults.add(currentPlacementResult);
            }
            
            if(i < n_simulation -1){
                //dopo ho un'altra simulazione, devo riassegnare i pattern in maniera casuale
                patternAssignamentWrapper.setVirtualMachinePatternMap(new Hashtable<String, TestCasePatternWrapper>());
                assignPatternToVMs();
            }
            
            
         }
    }
    
    protected float[] buildScalingFactorArray(int n_VM) {
        /* Build scaling factor list based on vm vmScaleFactor lists */        
        float[] scalingFactor = new float[n_VM];
        for (int i = 0; i < scalingFactor.length; i++) {
            scalingFactor[i] = 1.0f;
        }
        
        int offset = 0;
        for (RatioNumberContainer ratioNumberContainer : vmScaleFactor) {
            int n_ratio = (int) Math.floor(((float)ratioNumberContainer.getRatio() / 100.0f) * n_VM);
            //non controllo in fase di inserimento che la percentuale sia 100, quindi lo gestisco qui
            if( offset + n_ratio > n_VM)
                n_ratio = n_VM - offset;
            
            if( n_ratio <= 0 )
                break;
            
            for(int z = 0; z < n_ratio; z++){                     
                scalingFactor[offset+z] = (float)ratioNumberContainer.getScale() / 100.0f;                        
            }                    
            offset += n_ratio;
        }
        return scalingFactor;
    }
    
    
    public void setVmScaleFactor(ArrayList<RatioNumberContainer> vmScaleFactor) {
        this.vmScaleFactor = vmScaleFactor;
    }


    protected void exportSimulationResults(String dcName,String testCase, ArrayList<SimulationResultWrapper> simulationResultWrappers) {
        
        
        
        Path resultPath = Paths.get(this.autoExportResultPath,dcName);
        
        if(! Files.exists(resultPath)){
            File resultFile = new File(resultPath.toString());
            resultFile.mkdirs();
        }
        
        ArrayList<PlacementResultExt> resultExts = new ArrayList<PlacementResultExt>();
                
        Path resultFilePath = Paths.get(resultPath.toString(),testCase+".csv");        
        
        CSVFormat csvFormat = CSVFormat.EXCEL.withDelimiter(';');
                
        Object[] header = {"Id",
                "Reserved OS resource",
                "Max risk",
                "Max overprov time",
                "N runs for GRASP",
                "N simulation",
                "N VM",
                "N VM used for keep test consistency",
                "Iteration time",
                "Euristic method",
                "Euristic coefficient",
                "N Used host",
                "CPU Avg",
                "Mem Avg",
                "Overall Avg",
                "Cpu in range",
                "Mem in range",
                "Overall in range"};
        
        
        FileWriter buffer;
        try {
            buffer = new FileWriter(resultFilePath.toString());
            CSVPrinter csvPrinter = new CSVPrinter(buffer, csvFormat);
        
            csvPrinter.printRecord(header);
            for (SimulationResultWrapper simulationResultWrapper : simulationResultWrappers) {
            
                for (PlacementResult result : simulationResultWrapper.getPlacementResults()) {
                    List record = new ArrayList();
                    record.add(result.getId());
                    record.add(simulationResultWrapper.getReservedOsHostResource());
                    record.add(simulationResultWrapper.getHostMaxRisk());
                    record.add(simulationResultWrapper.getMaxOverprovisioningTime());
                    record.add(simulationResultWrapper.getN_runs());
                    record.add(simulationResultWrapper.getN_simulation());
                    record.add(simulationResultWrapper.getOriginalMachines().size());
                    record.add(simulationResultWrapper.getUsedForSimulationMachines().size());
                    record.add(result.getIterationTime());
                    record.add(simulationResultWrapper.getEuristicMethod());
                    record.add(simulationResultWrapper.getEuristicCoeffBuilderMethod());
                    record.add(result.getUsedHost().size());
                    record.add((float)result.getDataCenterMachinePlacement().getCpu_avg_usage());
                    record.add((float)result.getDataCenterMachinePlacement().getMemory_avg_usage());
                    record.add((float)result.getDataCenterMachinePlacement().getOverall_avg_usage());
                    record.add((float)result.getDataCenterMachinePlacement().getCpu_in_range());
                    record.add((float)result.getDataCenterMachinePlacement().getMemory_in_range());
                    record.add((float)result.getDataCenterMachinePlacement().getOverall_in_range());
                    
                    csvPrinter.printRecord(record);
                    csvPrinter.flush();
                   
        //            resultExts.add(new PlacementResultExt(result.getId(),
        //                    simulationResultWrapper.getReservedOsHostResource(),
        //                    simulationResultWrapper.getHostMaxRisk(),
        //                    simulationResultWrapper.getMaxOverprovisioningTime(),
        //                    simulationResultWrapper.getN_runs(),
        //                    simulationResultWrapper.getN_simulation(),
        //                    simulationResultWrapper.getOriginalMachines().size(),
        //                    simulationResultWrapper.getUsedForSimulationMachines().size(),
        //                    result.getIterationTime(),
        //                    simulationResultWrapper.getEuristicMethod(),
        //                    simulationResultWrapper.getEuristicCoeffBuilderMethod(),
        //                    result.getUsedHost().size(),
        //                    (float)result.getDataCenterMachinePlacement().getCpu_avg_usage(),
        //                    (float)result.getDataCenterMachinePlacement().getMemory_avg_usage(),
        //                    (float)result.getDataCenterMachinePlacement().getOverall_avg_usage(),
        //                    (float)result.getDataCenterMachinePlacement().getCpu_in_range(),
        //                    (float)result.getDataCenterMachinePlacement().getMemory_in_range(),
        //                    (float)result.getDataCenterMachinePlacement().getOverall_in_range()));
                }
            }
            csvPrinter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
                    
                
//            for (PlacementResultExt res : resultExts) {
//                csvPrinter.printRecords(res);    
//            }
//            
                            
    }

    public void exportPlacementResultObject(Path resultPath,String fileName,SimulationResultWrapper toSerialize) throws IOException{

        if(! Files.exists(resultPath)){
            File resultFile = new File(resultPath.toString());
            resultFile.mkdirs();
        }
        
        /* WRITE SERIALIZED FILE ON DISK */
        File f = new File(resultPath + "/" + fileName + ".ser");
        
        
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(toSerialize);
        oos.close();

    }

    public String getDataCenterSizes() {
        return dataCenterSizes;
    }


    public void setDataCenterSizes(String dataCenterSizes) {
        this.dataCenterSizes = dataCenterSizes;
    }


    public boolean isExportResultObject() {
        return exportResultObject;
    }


    public void setExportResultObject(boolean exportResultObject) {
        this.exportResultObject = exportResultObject;
    }    
}