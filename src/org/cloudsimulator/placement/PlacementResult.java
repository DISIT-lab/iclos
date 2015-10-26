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

import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;

public class PlacementResult implements Serializable{
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -553670336870779647L;
    private static int incremental = 0;    
    public static int getIncremental() {
        return incremental;
    }
    public static void setIncremental(int incremental) {
        PlacementResult.incremental = incremental;
    }    
    private int id;
    private ArrayList<HostMachinePlacement> usedHost;
    private ArrayList<HostMachinePlacement> addedHost;    
    private ArrayList<HostMachinePlacement> availableHost;
    
    private DataCenterMachinePlacement dataCenterMachinePlacement;
    
    private long iterationTime;

    public PlacementResult(){}

    public PlacementResult(ArrayList<HostMachinePlacement> usedHost,
            ArrayList<HostMachinePlacement> addedHost, ArrayList<HostMachinePlacement> availableHost,long iterationTime) {
        super();
        this.usedHost = usedHost;
        this.addedHost = addedHost;
        this.availableHost = availableHost;
        this.setId(++incremental);
        this.iterationTime = iterationTime;
    }

    public ArrayList<HostMachinePlacement> getAddedHost() {
        return addedHost;
    }

    public ArrayList<HostMachinePlacement> getAvailableHost() {
        return availableHost;
    }
    
    public DataCenterMachinePlacement getDataCenterMachinePlacement() {
        return dataCenterMachinePlacement;
    }
    
    public int getId() {
        return id;
    }

    public long getIterationTime() {
        return iterationTime;
    }

    public ArrayList<HostMachinePlacement> getUsedHost() {
        return usedHost;
    }

    public void setAvailableHost(ArrayList<HostMachinePlacement> availableHost) {
        this.availableHost = availableHost;
    }

    public void setDataCenterMachinePlacement(DataCenterMachinePlacement dataCenterMachinePlacement) {
        this.dataCenterMachinePlacement = dataCenterMachinePlacement;
        this.dataCenterMachinePlacement.setAssignedTo(this);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIterationTime(long iterationTime) {
        this.iterationTime = iterationTime;
    }

    private String toPerc(double val) {
        return String.format("%.1f",val * 100);
    }        
    
    public String toString(){
        return "Iteration " + this.getId()
                + " HOST: " + this.dataCenterMachinePlacement.getAssignedMachineSize()
                + " CPU_AVG: " + toPerc(this.dataCenterMachinePlacement.getCpu_avg_usage())
                + "% MEM_AVG: " + toPerc(this.dataCenterMachinePlacement.getMemory_avg_usage())
                + "% Overall_AVG: " + toPerc(this.dataCenterMachinePlacement.getOverall_avg_usage()) + "%";
                
        
    }

    public int totalNeededMachines(){        
        return usedHost.size() + addedHost.size();
    }

}
