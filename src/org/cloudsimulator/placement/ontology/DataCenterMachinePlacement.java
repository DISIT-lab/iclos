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

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.placement.PlacementResult;
import org.jblas.DoubleMatrix;


public class DataCenterMachinePlacement extends BaseMachinePlacementContainer implements Serializable {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -787260678161559339L;
    private ArrayList<HostMachinePlacement> assignedMachines;
    private PlacementResult assignedTo;
    
    public DataCenterMachinePlacement(int n_sample,DataCenter dataCenter, float reservedResource, ArrayList<HostMachinePlacement> assignedMachines) {
        super(n_sample,reservedResource);
        this.machine = dataCenter;
        this.reservedResource = reservedResource;
        this.assignedMachines = assignedMachines;

        for (HostMachinePlacement hostMachinePlacement : assignedMachines) {
            this.available_cpu += hostMachinePlacement.getAvailable_cpu();
            this.available_memory += hostMachinePlacement.getAvailable_memory();
            this.cpu_limit += hostMachinePlacement.getCpu_limit();
            this.memory_limit += hostMachinePlacement.getMemory_limit();
            
            hostMachinePlacement.setAssignedTo(this);
        }        
        
        
    }

    public void assignBasePlacementMachine(HostMachinePlacement poweredOnHost) {
        this.assignedMachines.add(poweredOnHost);          
        poweredOnHost.setAssignedTo(this);
    }

    protected void buildAssignedMatrix() {
        
//        int n_vm = 0;
//        for (HostMachinePlacement machine : assignedMachines) {
//            n_vm += machine.getAssignedMachineSize();
//        }
//        
        assigned_cpu_matrix = DoubleMatrix.zeros(this.assignedMachines.size(),this.n_sample);
        assigned_memory_matrix = DoubleMatrix.zeros(this.assignedMachines.size(),this.n_sample);
        
        int i=0;
        for (BaseMachinePlacementBase machine : assignedMachines) {                       
            DoubleMatrix temp = machine.getCpu_workload();
            assigned_cpu_matrix.putRow(i, temp);
            
            temp  = machine.getMemory_workload();
            assigned_memory_matrix.putRow(i,temp);                                                
            i++;
        }
    }

    protected void calculateOver() {
        DoubleMatrix cpu_val = DoubleMatrix.zeros(assignedMachines.size());
        DoubleMatrix mem_val = DoubleMatrix.zeros(assignedMachines.size());
        DoubleMatrix overall_val = DoubleMatrix.zeros(assignedMachines.size());
       
        for (int i = 0; i < assignedMachines.size(); i++) {
            cpu_val.put(i, assignedMachines.get(i).getCpu_in_range());
            mem_val.put(i, assignedMachines.get(i).getMemory_in_range());
            overall_val.put(i, assignedMachines.get(i).getOverall_in_range());            
        }
        setCpu_in_range((float) cpu_val.mean());
        setOverall_in_range((float) overall_val.mean());
        setMemory_in_range((float) mem_val.mean());
                        
    }

    @Override
    protected Float computeCpuThroughtput() {
        Float cpu = 0.0f;        
        for (BaseMachinePlacementBase hostMachine : assignedMachines) {
            cpu += ((HostMachine) hostMachine.getMachine()).getHasCPUSpeed() * ((HostMachine) hostMachine.getMachine()).getHasCPUCount();
        }        
        return cpu;
    }

    @Override
    protected Float computeMemorySize() {
        Float memSize = 0.0f;        
        for (BaseMachinePlacementBase hostMachine : assignedMachines) {
            memSize += hostMachine.getMachine().getHasMemorySize();
        }        
        return memSize;
    }
    
    public ArrayList<HostMachinePlacement> getAssignedMachines() {
        return assignedMachines;
    }

    public int getAssignedMachineSize() {
        return getAssignedMachines().size();
    }

    public void setAssignedMachines(ArrayList<HostMachinePlacement> assignedMachines) {
        this.assignedMachines = assignedMachines;
    }

    public PlacementResult getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(PlacementResult assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    protected void calculateTestRatio() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected ArrayList<String> getAssignedMachineNames() {
        ArrayList<String> names = new ArrayList<String>();
        for(HostMachinePlacement vm : this.assignedMachines){
            names.add(vm.getMachine().getHasName());
        }
        return names;
    }

}
