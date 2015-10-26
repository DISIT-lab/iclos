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

import java.util.ArrayList;

import org.cloudsimulator.domain.ontology.VirtualMachine;

public class PlacementResultExt {
    private int id;
    private float reservedOsHostResource;
    private float hostMaxRisk;
    private float maxOverprovisioningTime;
    private int n_runs;
    private int n_simulation;
    private int originalMachines;
    private int usedForSimulationMachines;   
    private long iterationTime;
    private String euristicMethod;
    private String euristicCoeffBuilderMethod;
    private int usedHost;
    protected float cpu_avg_usage;
    protected float memory_avg_usage;
    protected float overall_avg_usage;    
    private float cpu_in_range;
    private float memory_in_range;
    private float overall_in_range;
    
    
    public PlacementResultExt(int id, float reservedOsHostResource,
            float hostMaxRisk, float maxOverprovisioningTime, int n_runs,
            int n_simulation, int originalMachines,
            int usedForSimulationMachines, long iterationTime,
            String euristicMethod, String euristicCoeffBuilderMethod,
            int usedHost, float cpu_avg_usage, float memory_avg_usage,
            float overall_avg_usage, float cpu_in_range, float memory_in_range,
            float overall_in_range) {
        super();
        this.id = id;
        this.reservedOsHostResource = reservedOsHostResource;
        this.hostMaxRisk = hostMaxRisk;
        this.maxOverprovisioningTime = maxOverprovisioningTime;
        this.n_runs = n_runs;
        this.n_simulation = n_simulation;
        this.originalMachines = originalMachines;
        this.usedForSimulationMachines = usedForSimulationMachines;
        this.iterationTime = iterationTime;
        this.euristicMethod = euristicMethod;
        this.euristicCoeffBuilderMethod = euristicCoeffBuilderMethod;
        this.usedHost = usedHost;
        this.cpu_avg_usage = cpu_avg_usage;
        this.memory_avg_usage = memory_avg_usage;
        this.overall_avg_usage = overall_avg_usage;
        this.cpu_in_range = cpu_in_range;
        this.memory_in_range = memory_in_range;
        this.overall_in_range = overall_in_range;
    }
    public float getCpu_avg_usage() {
        return cpu_avg_usage;
    }
    public float getCpu_in_range() {
        return cpu_in_range;
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
    public int getId() {
        return id;
    }
    public long getIterationTime() {
        return iterationTime;
    }
    public float getMaxOverprovisioningTime() {
        return maxOverprovisioningTime;
    }
    public float getMemory_avg_usage() {
        return memory_avg_usage;
    }
    public float getMemory_in_range() {
        return memory_in_range;
    }
    public int getN_runs() {
        return n_runs;
    }
    public int getN_simulation() {
        return n_simulation;
    }
    public int getOriginalMachines() {
        return originalMachines;
    }
    public float getOverall_avg_usage() {
        return overall_avg_usage;
    }
    public float getOverall_in_range() {
        return overall_in_range;
    }
    public float getReservedOsHostResource() {
        return reservedOsHostResource;
    }
    public int getUsedForSimulationMachines() {
        return usedForSimulationMachines;
    }
    public int getUsedHost() {
        return usedHost;
    }
    public void setCpu_avg_usage(float cpu_avg_usage) {
        this.cpu_avg_usage = cpu_avg_usage;
    }
    public void setCpu_in_range(float cpu_in_range) {
        this.cpu_in_range = cpu_in_range;
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
    public void setId(int id) {
        this.id = id;
    }
    public void setIterationTime(long iterationTime) {
        this.iterationTime = iterationTime;
    }
    public void setMaxOverprovisioningTime(float maxOverprovisioningTime) {
        this.maxOverprovisioningTime = maxOverprovisioningTime;
    }
    public void setMemory_avg_usage(float memory_avg_usage) {
        this.memory_avg_usage = memory_avg_usage;
    }
    public void setMemory_in_range(float memory_in_range) {
        this.memory_in_range = memory_in_range;
    }
    public void setN_runs(int n_runs) {
        this.n_runs = n_runs;
    }
    public void setN_simulation(int n_simulation) {
        this.n_simulation = n_simulation;
    }
    public void setOriginalMachines(int originalMachines) {
        this.originalMachines = originalMachines;
    }
    public void setOverall_avg_usage(float overall_avg_usage) {
        this.overall_avg_usage = overall_avg_usage;
    }
    public void setOverall_in_range(float overall_in_range) {
        this.overall_in_range = overall_in_range;
    }
    public void setReservedOsHostResource(float reservedOsHostResource) {
        this.reservedOsHostResource = reservedOsHostResource;
    }
    public void setUsedForSimulationMachines(int usedForSimulationMachines) {
        this.usedForSimulationMachines = usedForSimulationMachines;
    }
    public void setUsedHost(int usedHost) {
        this.usedHost = usedHost;
    }
    
    
    

}
