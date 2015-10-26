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

package org.cloudsimulator.placement.ontology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.cloudsimulator.domain.ontology.HostMachine;
import org.jblas.DoubleMatrix;
import org.jblas.ranges.IntervalRange;
import org.jblas.ranges.Range;
import org.jblas.ranges.RangeUtils;


public class HostMachinePlacement extends BaseMachinePlacementContainer implements Serializable {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -1836138634051513431L;
    
    protected float maxOverProvisioningTime;
    protected float maxRisk;
    protected double normalized_available_cpu;
    protected double normalized_available_memory;
    protected DoubleMatrix inverse_normalized_resource_matrix;
    protected DoubleMatrix normalized_available_resource_matrix;
    protected DoubleMatrix residual_resource;
    
    private DataCenterMachinePlacement assignedTo;

    private ArrayList<VirtualMachinePlacement> assignedMachines;

    //la prima chiave contiene il nome del test case, la seconda contiene il ruolo ed il conteggio  
    private HashMap<String, HashMap<String, Integer>> testCaseCounter;
    
    public HashMap<String, HashMap<String, Integer>> getTestCaseCounter() {
        return testCaseCounter;
    }

    public void setTestCaseCounter(
            HashMap<String, HashMap<String, Integer>> testCaseCounter) {
        this.testCaseCounter = testCaseCounter;
    }

    public HostMachinePlacement() {
        super();
    }

    public HostMachinePlacement(int n_sample, HostMachine hostMachine, float maxRisk,
            float maxOverProvisioningTime, float reservedResource) {
        super(n_sample,reservedResource);
        this.machine = hostMachine;
        this.maxRisk = maxRisk;
        this.maxOverProvisioningTime = maxOverProvisioningTime;
        testCaseCounter = new HashMap<String, HashMap<String,Integer>>();
        
    }

    public void assignBasePlacementMachine(VirtualMachinePlacement machine_toAlloc) {
        this.assignedMachines.add(machine_toAlloc);
        machine_toAlloc.setAssignedTo(this);
        this.residual_resource.subi(((VirtualMachinePlacement)machine_toAlloc).getNormalizedResourceDemandWrtBin());        
    }

    protected void buildAssignedMatrix() {
        int n_interval = this.n_sample * 2;                        
        assigned_cpu_matrix = DoubleMatrix.zeros(this.assignedMachines.size(),this.n_sample);
        assigned_memory_matrix = DoubleMatrix.zeros(this.assignedMachines.size(),this.n_sample);
        
        int i=0;
        for (VirtualMachinePlacement machine : getAssignedMachines()) {
            Range range = new IntervalRange(0, n_interval/2);            
            DoubleMatrix temp = ((VirtualMachinePlacement) machine).getNot_normalized_demand().getColumns(range);
            assigned_cpu_matrix.putRow(i, temp);
            
            range = new IntervalRange(n_interval/2, (n_interval));
            temp  = ((VirtualMachinePlacement) machine).getNot_normalized_demand().getColumns(range);
            assigned_memory_matrix.putRow(i,temp);                                    
            
            i++;
        }
    }

    protected void calculateOver() {
        
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
        
        DoubleMatrix test = this.assigned_cpu_matrix.columnSums();
        DoubleMatrix temp = test.lt(this.available_cpu);
        this.cpu_in_range = (float) RangeUtils.find(temp).length() / this.n_sample;                        
        
        test = this.assigned_memory_matrix.columnSums();
        temp = test.lt(this.available_memory);
        this.memory_in_range = (float) RangeUtils.find(temp).length() / this.n_sample;                                                      
        
        this.overall_in_range = (float) (cpu_in_range + memory_in_range) / 2;
        
    }

    public ArrayList<VirtualMachinePlacement> getAssignedMachines() {
        return assignedMachines;
    }

    public DoubleMatrix getInverse_normalized_resource_matrix() {
        return inverse_normalized_resource_matrix;
    }

    public float getMaxOverProvisioningTime() {
        return maxOverProvisioningTime;
    }

    public float getMaxRisk() {
        return maxRisk;
    }
    
    public DoubleMatrix getNormalized_available_resource_matrix() {
        return normalized_available_resource_matrix;
    }

    public DoubleMatrix getResidual_resource() {
        return residual_resource;
    }

    public void Init(int n_time_interval) {
        this.assignedMachines = new ArrayList<VirtualMachinePlacement>();
        
        //questo è il valore che la macchina ha a disposizione per le macchine virtuali
        this.available_cpu =getMachine().getCpuThroughtput() * (1-this.reservedResource);
        this.available_memory =(float) this.machine.getHasMemorySize() * (1-this.reservedResource);
        
        //questo è il valore limite oltre il quale si sta facendo overprovisionoing
        if(this.maxRisk > 0){
            float offset_cpu = (getMachine().getCpuThroughtput() - this.available_cpu) * this.maxRisk;            
            this.cpu_limit = this.available_cpu + offset_cpu; //quando la cpu è tra available_cpu e cpu_limit sto facendo overprovisioning
            
            float offset_memory = (this.machine.getHasMemorySize() - this.available_memory) * this.maxRisk;            
            this.memory_limit = this.available_memory + offset_memory; //quando la memoria è tra available_mem e mem_limit sto facendo overprovisioning
            
        }else{
            //questo caso sarebbe uguale tranne che per gli estremi invertiti.Lo scompongo solo per chiarezza
            //float offset_cpu = (getMachine().getCpuThroughtput() - this.available_cpu) * this.maxRisk;
            float offset_cpu =  this.available_cpu * this.maxRisk;
            this.cpu_limit = this.available_cpu + offset_cpu; //quando la cpu è tra available_cpu e cpu_limit sto facendo overprovisioning
            
            float temp = cpu_limit;
            this.cpu_limit = this.available_cpu;
            this.available_cpu = temp;
            
//            float offset_memory = (this.machine.getHasMemorySize() - this.available_memory) * this.maxRisk;
            float offset_memory = this.available_memory * this.maxRisk;
            this.memory_limit = this.available_memory + offset_memory; //quando la memoria è tra available_mem e mem_limit sto facendo overprovisioning
            
            temp = memory_limit;
            this.memory_limit = this.available_memory;
            this.available_memory = temp;
            
        }
        
        this.residual_resource = new DoubleMatrix().ones(1,n_time_interval * 2);
        this.normalized_available_resource_matrix = new DoubleMatrix().ones(1,n_time_interval * 2);
        
        
        this.normalized_available_cpu = (double) this.available_cpu/this.cpu_limit;
        this.normalized_available_memory = (double) this.available_memory/this.memory_limit;
        
        for(int i=0;i<n_time_interval;i++){
            this.normalized_available_resource_matrix.put(i,this.normalized_available_cpu);
        }
        for(int i=n_time_interval;i<n_time_interval * 2;i++){
            this.normalized_available_resource_matrix.put(i,this.normalized_available_memory);
        }
        
        this.inverse_normalized_resource_matrix = new DoubleMatrix().ones(1,n_time_interval * 2).sub(this.normalized_available_resource_matrix);
    }

    protected DoubleMatrix initHostLimit() {
        int n_interval = this.getResidual_resource().columns;
        
        //Inizializzo i vettori limit dell'host
        DoubleMatrix limit_host_resource = new DoubleMatrix(1,n_interval);
        for(int i=0; i < n_interval / 2; i++)
            limit_host_resource.put(i, this.getCpu_limit());                
        for(int i= n_interval / 2 ;i < n_interval; i++)
            limit_host_resource.put(i, this.getMemory_limit());
        return limit_host_resource;
    }

    public void normalizeUsageWrtBin(
            ArrayList<VirtualMachinePlacement> toBeAllocs) {
        /*      devo inizializzare i vettori normalizzati delle vm dividendo 
         *      i valori richiesti originariamente con il limit dell'host        
         */
        
        DoubleMatrix limit_host_resource = initHostLimit();
                
        for(VirtualMachinePlacement vm : toBeAllocs){                                               
            //Adesso devo normalizzare le risorse richieste rispetto ad i parametri limit dell'host
            vm.setNormalizedResourceDemandWrtBin(vm.getNot_normalized_demand().div(limit_host_resource));                        
        }
        
    }

    public void normalizeUsageWrtBin(VirtualMachinePlacement currentVm) {
        
        DoubleMatrix limit_host_resource = initHostLimit();
                                                                  
        currentVm.setNormalizedResourceDemandWrtBin(currentVm.getNot_normalized_demand().div(limit_host_resource));                                       
    }
    
    public void setAssignedMachines(
            ArrayList<VirtualMachinePlacement> assignedMachines) {
        this.assignedMachines = assignedMachines;
    }
    
    public void setMaxRisk(float maxRisk) {
        this.maxRisk = maxRisk;
    }

    public DataCenterMachinePlacement getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(DataCenterMachinePlacement assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    protected void calculateTestRatio() {                     
        for (VirtualMachinePlacement virtualMachinePlacement : assignedMachines) {
            String testCaseName = virtualMachinePlacement.getTestCasePatternWrapper().getTestName();
            String machineRole = virtualMachinePlacement.getTestCasePatternWrapper().getType();
            
            if(testCaseName !=null && machineRole !=null){
                if(testCaseName.length() > 0 && machineRole.length() > 0){
                    if(! testCaseCounter.containsKey(testCaseName)){                
                        HashMap<String, Integer> actual = new HashMap<String, Integer>();
                        actual.put(machineRole, 1);                
                        testCaseCounter.put(testCaseName, actual);                
                    }else{
                        HashMap<String, Integer> actual = testCaseCounter.get(testCaseName);
                        
                        if(! actual.containsKey(machineRole)){
                            actual.put(machineRole, 1);
                        }else{
                            actual.put(machineRole, (actual.get(machineRole)+1));
                        }
                        
                    }                                    
                }
            }
        }                
    }   
    
    public String getTestRatioString(){
        StringBuffer res = new StringBuffer();
        for (Entry<String, HashMap<String, Integer>> testCase : testCaseCounter.entrySet()) {
            res.append(testCase.getKey() + "<br>");
            for (Entry<String, Integer> role : testCase.getValue().entrySet()) {
                res.append("   " + role.getKey() + ": " + role.getValue() + "<br>");
            }
        }
        return res.toString();
    }

    @Override
    protected ArrayList<String> getAssignedMachineNames() {
        ArrayList<String> names = new ArrayList<String>();
        for(VirtualMachinePlacement vm : this.assignedMachines){
            names.add(vm.getMachine().getHasName());
        }
        return names;
    }
}
