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

package org.cloudsimulator.placement;

import java.io.Serializable;
import java.util.ArrayList;

import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;

public class SimulationResultWrapper implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -8126321433846295088L;
    
    private String euristicMethod;
    private String euristicCoeffBuilderMethod;
    private ArrayList<PlacementResult> placementResults;
    private ArrayList<VirtualMachine> originalMachines;
    private ArrayList<VirtualMachine> usedForSimulationMachines;
    private String testCaseMachineAssignament;
    private String testCaseName;
    private float reservedOsHostResource;
    private float hostMaxRisk;
    private float maxOverprovisioningTime;
    private int n_runs;
    private int n_simulation;
    
    
    
    
    public SimulationResultWrapper(String euristicMethod,
            String euristicCoeffBuilderMethod,
            ArrayList<PlacementResult> placementResults,
            ArrayList<VirtualMachine> originalMachines,
            ArrayList<VirtualMachine> usedForSimulationMachines, String testAssignament,String testCaseName,float reservedOsHostResource
            ,float hostMaxRisk,float maxOverprovisioningTime,int n_runs,int n_simulation) {

        this.euristicMethod = euristicMethod;
        this.euristicCoeffBuilderMethod = euristicCoeffBuilderMethod;
        this.placementResults = placementResults;
        this.testCaseMachineAssignament = testAssignament;
        this.testCaseName = testCaseName;
        this.setReservedOsHostResource(reservedOsHostResource);
        this.setHostMaxRisk(hostMaxRisk);
        this.setMaxOverprovisioningTime(maxOverprovisioningTime);
        this.setN_runs(n_runs);
        this.setN_simulation(n_simulation);
        
        this.usedForSimulationMachines = new ArrayList<VirtualMachine>(usedForSimulationMachines);
//        for (VirtualMachine virtualMachinePlacement : usedForSimulationMachines) {
//            usedForSimulationMachines.add(virtualMachinePlacement);
//        }
        this.originalMachines = new ArrayList<VirtualMachine>(originalMachines);
//        for (VirtualMachine virtualMachine : originalMachines) {
//            this.originalMachines.add(virtualMachine);
//        }
    }
    
    public String getEuristicCoeffBuilderMethod() {
        return euristicCoeffBuilderMethod;
    }
    public String getEuristicMethod() {
        return euristicMethod;
    }
    public float getHostMaxRisk() {
        return hostMaxRisk;
    }
    public float getMaxOverprovisioningTime() {
        return maxOverprovisioningTime;
    }
    public int getN_runs() {
        return n_runs;
    }
    public int getN_simulation() {
        return n_simulation;
    }
    public ArrayList<VirtualMachine> getOriginalMachines() {
        return originalMachines;
    }
    public ArrayList<PlacementResult> getPlacementResults() {
        return placementResults;
    }
    public float getReservedOsHostResource() {
        return reservedOsHostResource;
    }
    public String getTestCaseMachineAssignament() {
        return testCaseMachineAssignament;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public ArrayList<VirtualMachine> getUsedForSimulationMachines() {
        return usedForSimulationMachines;
    }

    public void setEuristicCoeffBuilderMethod(String euristicCoeffBuilderMethod) {
        this.euristicCoeffBuilderMethod = euristicCoeffBuilderMethod;
    }

    public void setEuristicMethod(String euristicMethod) {
        this.euristicMethod = euristicMethod;
    }

    public void setHostMaxRisk(float hostMaxRisk) {
        this.hostMaxRisk = hostMaxRisk;
    }

    public void setMaxOverprovisioningTime(float maxOverprovisioningTime) {
        this.maxOverprovisioningTime = maxOverprovisioningTime;
    }

    public void setN_runs(int n_runs) {
        this.n_runs = n_runs;
    }

    public void setN_simulation(int n_simulation) {
        this.n_simulation = n_simulation;
    }

    public void setOriginalMachines(ArrayList<VirtualMachine> originalMachines) {
        this.originalMachines = originalMachines;
    }

    public void setPlacementResults(ArrayList<PlacementResult> placementResults) {
        this.placementResults = placementResults;
    }

    public void setReservedOsHostResource(float reservedOsHostResource) {
        this.reservedOsHostResource = reservedOsHostResource;
    }

    public void setTestCaseMachineAssignament(String testCaseMachineAssignament) {
        this.testCaseMachineAssignament = testCaseMachineAssignament;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public void setUsedForSimulationMachines(
            ArrayList<VirtualMachine> usedForSimulationMachines) {
        this.usedForSimulationMachines = usedForSimulationMachines;
    }

}
