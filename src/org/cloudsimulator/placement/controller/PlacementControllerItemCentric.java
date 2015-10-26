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

package org.cloudsimulator.placement.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.AddedHostDetail;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.WeightedVmComparator;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicControllerFFD;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;
import org.jblas.DoubleMatrix;
import org.jblas.ranges.RangeUtils;

public class PlacementControllerItemCentric extends PlacementControllerBase {
    
    public PlacementControllerItemCentric(DataCenter dataCenter,float hostMaxRisk,
            float maxOverprovisioningTime, float reservedOsHostResource,
            int days, int n_runs, int daySample, AddedHostDetail addedHostDetail, List<VirtualMachine> virtualMachines) {

        super(dataCenter,hostMaxRisk,maxOverprovisioningTime,reservedOsHostResource,days,n_runs,daySample,addedHostDetail,virtualMachines);
    }
    
    public PlacementResult allocateVm(            
            ArrayList<VmResourceValueContainer> vmResourceValueContainers, 
            HeuristicInterface ec, HeuristicCoefficientBuilder coeffBuilder,
            boolean stopSimulationIfNotFitting, boolean showOutput) throws Exception{
      
        
        if( !(ec instanceof HeuristicControllerFFD)) {
            throw new Exception("Wrong heuristic controller type");
        }
        
        long startTime = System.currentTimeMillis();
        
        if(showOutput) this.monitorString += " <br /> Starting new simulation... <br />";
        
        setSimulationSuccess(false);
        int totalToAllocPerIteration = virtualMachines.size();
                
                
        boolean result = true;
        this.setCompletedSimulation(0);
        this.setSimulationResult("");
        this.setMonitorString("");
        
                
        long iterationTime = System.currentTimeMillis();
        
        ArrayList<HostMachinePlacement> availableHost = new ArrayList<HostMachinePlacement>();
        ArrayList<HostMachinePlacement> addedHost = new ArrayList<HostMachinePlacement>();
        ArrayList<HostMachinePlacement> usedHost = new ArrayList<HostMachinePlacement>();
        ArrayList<HostMachinePlacement> openedBins = new ArrayList<HostMachinePlacement>();
        
        ArrayList<VirtualMachinePlacement> toBeAllocs = new ArrayList<VirtualMachinePlacement>();
        for (int i=0; i < totalToAllocPerIteration; i++) {            
            VirtualMachine virtualMachine = virtualMachines.get(i);
            VmResourceValueContainer rc = vmResourceValueContainers.get(i);
            
            VirtualMachinePlacement vmp = new VirtualMachinePlacement(virtualMachine, rc.getCpu_value(), rc.getMem_value(),virtualMachine.getTestCasePatternWrapper());            
            toBeAllocs.add(vmp);            
        }
        
        //Init the euristic controller and other needed params
        ec.Init(toBeAllocs,coeffBuilder); 
        
        for (HostMachine hostMachine : dataCenter.getHostMachineList()) {   
            HostMachinePlacement hmp = new HostMachinePlacement(DAYSAMPLE,
                    hostMachine,
                    this.hostMaxRisk, 
                    this.maxOverprovisioningTime,
                    this.reservedOsHostResource);
            availableHost.add(hmp);
            hmp.Init(this.days * DAYSAMPLE);
        }
        
                
        int openedBinIndex = 0;
        int iteration = 1;
        
        //ORDINARE LE MACCHINE VIRTUALI SULLA BASE DELLE RICHIESTE
        //in not_normalized_demand ho le risorse utilizzate dalla vm ma non normalizzate
        //per calcolare il peso di ogni vm devo applicare l'euristica FFDProd e FFDSum
        toBeAllocs = ((HeuristicControllerFFD)ec).computeWeight(toBeAllocs);
        
        Collections.sort(toBeAllocs, Collections.reverseOrder(new WeightedVmComparator()));
               
        openNewBin(openedBinIndex,openedBins,availableHost,usedHost);        
        
        int vmIndex = 0;
        int hostIndex = 0;
        boolean currentVmPlaced = false;
        HostMachinePlacement currentBin = null;

        
        //CICLARE SUGLI OGGETTI        
        for (vmIndex = 0; vmIndex < toBeAllocs.size(); vmIndex++) {
            VirtualMachinePlacement currentItem = toBeAllocs.get(vmIndex);
            currentVmPlaced = false;
                                                       
            //PIAZZARE L'OGGETTO NEL PRIMO BIN DOVE ENTRA
            for (hostIndex = 0; hostIndex < openedBins.size(); hostIndex++) {
                currentBin = openedBins.get(hostIndex);
                currentBin.normalizeUsageWrtBin(currentItem);
                
                if(checkFittingCurrentBin(currentBin, currentItem)){
                    currentVmPlaced = true;
                    currentBin.assignBasePlacementMachine(currentItem);
                    
                    if(showOutput) updateProgress(iteration,totalToAllocPerIteration ,currentItem.getMachine().getHasName(),toBeAllocs.size());                
                    
                    break; // go to next item
                }                                                
            }
            if(!currentVmPlaced){
                if(vmIndex < toBeAllocs.size()){
                    if(openedBinIndex < availableHost.size() - 1){
                        openedBinIndex++;
                        //non entrava nei bin che avevo, ne apro un altro
                        currentBin = openNewBin(openedBinIndex,openedBins,availableHost,usedHost);
                    }else{
                        //ho finito gli host disponibili
                        if(stopSimulationIfNotFitting)
                        {
                            result = false;
                            break;
                        }
                        else{
                            currentBin = addNewHost(availableHost,addedHost);                        
                            usedHost.add(currentBin);                            
                        }
                    }
                    openedBins.add(currentBin);
                    if(checkFittingCurrentBin(currentBin, currentItem)){
                        currentVmPlaced = true;
                        currentBin.assignBasePlacementMachine(currentItem);
                        
                        if(showOutput) updateProgress(iteration,totalToAllocPerIteration ,currentItem.getMachine().getHasName(),toBeAllocs.size());                
                    }else{
                        if(currentBin.getAssignedMachines().size() == 0){
                            // se sono qui vuol dire che esistono ona o più macchine virtuali che sono più grandi 
                            //dell'host che dovrebbe contenerle. Interrompo la simulazione e lo notifico all'utente:
                            String mes = "FATAL - There are one ore more VMs that use more resource than the container available limit. Please review your BC.";
                            showMessage(mes);
                            setSimulationResult(mes);
                            return null;
                        }                        
                    }
                    
                }                                  
            }  
//            if(!currentVmPlaced){
//                //la macchina non entra nemmeno nel nuovo host!
//                result = false;
//                break;
//            }
        }
        
        if(vmIndex == toBeAllocs.size() - 1){
            result = true;
        }
        
        long stopTime = System.currentTimeMillis();
        iterationTime = stopTime - iterationTime;
        
        if(result == false){
            this.setSimulationResult("Simulation ended, VMs can't fit available host");
            setSimulationSuccess(false);            
        }else {
            if(addedHost.size() > 0){        
                this.setSimulationResult("Simulation ended. You need to add " + addedHost.size() + " more hosts to place all the VMs of the business configuration.") ;
            }else{
                this.setSimulationResult("Simulation ended. VMs can fit the host. Check the result for more details.");
            }
            if(showOutput) this.setMonitorString(this.getMonitorString() + " <br /> Iteration complete, saving results... <br />");
            setSimulationSuccess(true);
            this.placementResult = 
                    new PlacementResult(usedHost, 
                            addedHost,availableHost,
                            iterationTime);
        }
        
               
        simulationTime = stopTime - startTime;
        
        return placementResult;
    }

    protected boolean checkFittingCurrentBin(HostMachinePlacement currentBin,
            VirtualMachinePlacement currentItem) {
        
        
        /*
         * Filtro le vm. Ho due possibilità:
         * 1.   La vm assegnata all'host non provoca overprovisioning
         * 2.   La vm assegnata all'host provoca overprovisioning
         * Nel caso uno la ritorno come se fitta, nel caso due devo controllare
         * se il tempo totale in cui faccio overprovisioning è minore di quello
         * che ho impostato come range
        */
        int n_el = currentBin.getResidual_resource().columns;
        int n_el_overprov_per_res = (int) Math.floor(currentBin.getMaxOverProvisioningTime() * n_el/2);
        
        //Create host residual. Tante righe quante sono le macchine virtuali da allocare
        DoubleMatrix host_residual_matrix = currentBin.getResidual_resource();             
        
        DoubleMatrix requestedResource_matrix = currentItem.getNormalizedResourceDemandWrtBin();
        
        DoubleMatrix try_fit = host_residual_matrix.sub(requestedResource_matrix);        
        
        /* 
         * Rem := host_residual - vm_demand
         * 
         * 
         * ---------------- <--- 1 
         * |Host reserved |
         * |--------------| <--- (limit)
         * |              |
         * |              | <--- x (AvailableResource)
         * |              |            
         * | Rem > (1-x)  |
         * |              |
         * |     OK       |
         * |--------------| <--- (1 - x)
         * | Rem < (1-x)  |
         * |OverProv check|
         * |______________| 0
         *   Non fitting
         */
        
                          
        
        if(RangeUtils.find(try_fit.gt(0)).length() < n_el){
            //Per almeno un istante la vm non fitta
            return false;                
        }else{
            DoubleMatrix temp = try_fit.gt(currentBin.getInverse_normalized_resource_matrix());
            int in_range = RangeUtils.find(temp).length();
            if( in_range == n_el){
                //è tutto dentro il limite, sono a posto
                return true;       
            }else{
                
                //qualcosa è fuori, devo controllare il tempo di overprovisioning                    
                
                DoubleMatrix appo = temp.getRange(0, n_el/2);                    
                int cpu_over = appo.length - RangeUtils.find(appo).length();
                
                appo = temp.getRange(n_el/2,n_el);
                int mem_over = appo.length - RangeUtils.find(appo).length();
                
                if(cpu_over <= n_el_overprov_per_res && mem_over <= n_el_overprov_per_res)
                    return true;    
                else
                    return false;    
            }
        }
    }

    protected HostMachinePlacement openNewBin(int openedBinIndex, ArrayList<HostMachinePlacement> openedBins,
            ArrayList<HostMachinePlacement> availableHost, ArrayList<HostMachinePlacement> usedHost) {
        HostMachinePlacement hm = availableHost.get(openedBinIndex);
        usedHost.add(hm);
        openedBins.add(hm);
        return hm;
    }
   
}
