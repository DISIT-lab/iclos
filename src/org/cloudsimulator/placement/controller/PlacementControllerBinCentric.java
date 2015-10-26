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
import java.util.Iterator;
import java.util.List;

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.AddedHostDetail;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.heuristic.GraspDotProductHeuristic;
import org.cloudsimulator.placement.heuristic.GraspL2NormHeuristic;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicGeometric;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;
import org.jblas.DoubleMatrix;
import org.jblas.ranges.RangeUtils;

public class PlacementControllerBinCentric extends PlacementControllerBase {
    
    public PlacementControllerBinCentric(DataCenter dataCenter,float hostMaxRisk,
            float maxOverprovisioningTime, float reservedOsHostResource,
            int days, int n_runs, int daySample,AddedHostDetail addedHostDetail, List<VirtualMachine> virtualMachines) {

        super(dataCenter,hostMaxRisk,maxOverprovisioningTime,reservedOsHostResource,days,n_runs,daySample, addedHostDetail,virtualMachines);
    }
    
    public PlacementResult allocateVm(            
            ArrayList<VmResourceValueContainer> vmResourceValueContainers, 
            HeuristicInterface ec, HeuristicCoefficientBuilder coeffBuilder,
            boolean stopSimulationIfNotFitting, boolean showOutput) throws Exception{
      
        if( !(ec instanceof HeuristicGeometric)) {
            throw new Exception("Wrong heuristic controller type");
        }
        
        if(!(ec instanceof GraspDotProductHeuristic) && 
                !(ec instanceof GraspL2NormHeuristic) )
            n_runs = 1;
        
        long startTime = System.currentTimeMillis();
        
        if(showOutput) this.monitorString += " <br /> Starting new simulation... <br />";
        
        setSimulationSuccess(false);
        ArrayList<PlacementResult> iterationResults = new ArrayList<PlacementResult>();
        int totalToAllocPerIteration = virtualMachines.size();
                
                
        boolean result = true;
        
        this.setSimulationResult("");        
        
        int incrementalId = PlacementResult.getIncremental(); 
                
        for(int iteration = 0; iteration < n_runs; iteration++){
            this.setCompletedSimulation(0);
            this.setMonitorString("");
            
            long iterationTime = System.currentTimeMillis();
            
            ArrayList<HostMachinePlacement> availableHost = new ArrayList<HostMachinePlacement>();
            ArrayList<HostMachinePlacement> addedHost = new ArrayList<HostMachinePlacement>();
            ArrayList<HostMachinePlacement> usedHost = new ArrayList<HostMachinePlacement>();
            
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
            
            HostMachinePlacement openedBin = openNewBin(openedBinIndex,availableHost,usedHost);
            openedBinIndex++;
            //devo normalizzare i valori di utilizzo della vm sulla base del valore limit
            //dell'host aperto. Ovviamente devo farlo solo per le macchine ancora da piazzare        
            openedBin.normalizeUsageWrtBin(toBeAllocs);
            
            while(!toBeAllocs.isEmpty()){
                        
                ArrayList<VirtualMachinePlacement> vmFittingBin = selectFittingOpenedBin(openedBin,toBeAllocs);
                
                if(vmFittingBin.size() == 0 && openedBin.getAssignedMachines().size() == 0){
                    // se sono qui vuol dire che esistono ona o più macchine virtuali che sono più grandi 
                    //dell'host che dovrebbe contenerle. Interrompo la simulazione e lo notifico all'utente:
                    String mes = "FATAL - There are one ore more VMs that use more resource than the container available limit. Please review your BC.";
                    showMessage(mes);
                    setSimulationResult(mes);
                    return null;
                }
                
                while(vmFittingBin.size() > 0){                               
                    /*      Assegno la VM all'host migliore e la rimuovo dalla lista di quelle da assegnare        */
                    VirtualMachinePlacement vm_toAlloc = ((HeuristicGeometric) ec).chooseBestVm(vmFittingBin,openedBin);
                    openedBin.assignBasePlacementMachine(vm_toAlloc);                
                    toBeAllocs.remove(vm_toAlloc);
                    
                    if(showOutput) updateProgress(iteration,totalToAllocPerIteration ,vm_toAlloc.getMachine().getHasName(),toBeAllocs.size());                
                    
                    //try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
                    
                    /*      continuo a guardare se ci sono macchine che entrano nel mio host        */
                    vmFittingBin = selectFittingOpenedBin(openedBin,toBeAllocs);                
                }
                
                if(toBeAllocs.size() > 0){
                    if(openedBinIndex < availableHost.size() - 1){
                        openedBinIndex++;
                        openedBin = openNewBin(openedBinIndex,availableHost,usedHost);
                        openedBin.normalizeUsageWrtBin(toBeAllocs);
                    }else{
                        //ho finito gli host disponibili
                        if(stopSimulationIfNotFitting)
                        {
                            result = false;
                            break;
                        }
                        else{
                            openedBin = addNewHost(availableHost,addedHost);                        
                            usedHost.add(openedBin);
                            openedBin.normalizeUsageWrtBin(toBeAllocs);
                        }
                    }
                }
                
            }
            
            long stopTime = System.currentTimeMillis();
            iterationTime = stopTime - iterationTime;
            
            if(result == false){
                this.setSimulationResult("Simulation ended, VMs can't fit available host");
                setSimulationSuccess(false);
                break;//esco anche da iterare nuovamente
            }else {
                if(addedHost.size() > 0){        
                    this.setSimulationResult("Simulation ended. You need to add " + addedHost.size() + " more hosts to place all the VMs of the business configuration.") ;
                }else{
                    this.setSimulationResult("Simulation ended. VMs can fit the host. Check the result for more details.");
                }
                if(showOutput) this.setMonitorString(this.getMonitorString() + " <br /> Iteration complete, saving results... <br />");
                setSimulationSuccess(true);
                iterationResults.add(
                        new PlacementResult(usedHost, 
                                addedHost,availableHost,
                                iterationTime));
            }
        }
        
        long stopTime = System.currentTimeMillis();
        simulationTime = stopTime - startTime;
        
        PlacementResult best = null;
        best = iterationResults.get(0);
        for (PlacementResult placementResult : iterationResults) {
            if(best.getUsedHost().size() > placementResult.getUsedHost().size())
                best = placementResult;
        }
        placementResult = best;
        
        /*
         * IN QUESTO MODO ASSEGNO L'ID IN MANIERA PROGRESSIVA ANCHE NEL CASO DEI GRASP
         */
        placementResult.setId(incrementalId + 1);
        PlacementResult.setIncremental(incrementalId + 1);
        
        
        return placementResult;
    }

    protected HostMachinePlacement openNewBin(int openedBinIndex, ArrayList<HostMachinePlacement> availableHost,
            ArrayList<HostMachinePlacement> usedHost) {
        HostMachinePlacement hm = availableHost.get(openedBinIndex);
        usedHost.add(hm);
        return hm;
    }

    protected ArrayList<VirtualMachinePlacement> selectFittingOpenedBin(HostMachinePlacement openedBin, ArrayList<VirtualMachinePlacement> toBeAllocs) {
        
        
        ArrayList<VirtualMachinePlacement> fitting_vm = new ArrayList<VirtualMachinePlacement>();        
        
        if(toBeAllocs.size() == 0)
            return fitting_vm;
        /*
         * Filtro le vm. Ho due possibilità:
         * 1.   La vm assegnata all'host non provoca overprovisioning
         * 2.   La vm assegnata all'host provoca overprovisioning
         * Nel caso uno la ritorno come se fitta, nel caso due devo controllare
         * se il tempo totale in cui faccio overprovisioning è minore di quello
         * che ho impostato come range
        */
        int remain = toBeAllocs.size();
        int n_el = openedBin.getResidual_resource().columns;
        int n_el_overprov_per_res = (int) Math.floor(openedBin.getMaxOverProvisioningTime() * n_el/2);
        
        //Create host residual. Tante righe quante sono le macchine virtuali da allocare
        DoubleMatrix host_residual_matrix = openedBin.getResidual_resource().repmat(remain, 1);                
        
        DoubleMatrix requestedResource_matrix = VirtualMachinePlacement.buildRequestedResourceMatrix(toBeAllocs);
        
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
        
        
        boolean[] fitting_vm_index = new boolean[toBeAllocs.size()];               
        for(int i=0; i < try_fit.rows; i++){
            
            if(RangeUtils.find(try_fit.getRow(i).gt(0)).length() < n_el){
                //Per almeno un istante la vm non fitta
                fitting_vm_index[i] = false;                
            }else{
                DoubleMatrix temp = try_fit.getRow(i).gt(openedBin.getInverse_normalized_resource_matrix());
                int in_range = RangeUtils.find(temp).length();
                if( in_range == n_el){
                    //è tutto dentro il limite, sono a posto
                    fitting_vm_index[i] = true;       
                }else{
                    
                    //qualcosa è fuori, devo controllare il tempo di overprovisioning                    
                    
                    DoubleMatrix appo = temp.getRange(0, n_el/2);                    
                    int cpu_over = appo.length - RangeUtils.find(appo).length();
                    
                    appo = temp.getRange(n_el/2,n_el);
                    int mem_over = appo.length - RangeUtils.find(appo).length();
                    
                    if(cpu_over <= n_el_overprov_per_res && mem_over <= n_el_overprov_per_res)
                        fitting_vm_index[i] = true;    
                    else
                        fitting_vm_index[i] = false;    
                }
            }
        }        
        for(int i=0;i<fitting_vm_index.length;i++){
            if(fitting_vm_index[i])
                fitting_vm.add((VirtualMachinePlacement) toBeAllocs.get(i));            
        }
        return fitting_vm;
    }
   
}
