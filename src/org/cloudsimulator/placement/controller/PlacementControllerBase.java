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
import java.util.List;

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.AddedHostDetail;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;
import org.cloudsimulator.placement.ontology.BaseMachinePlacementBase;
import org.cloudsimulator.placement.ontology.BaseMachinePlacementContainer;
import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;

public abstract class PlacementControllerBase implements PlacementControllerInterface {

    private int completedSimulation;
    private String simulationResult;
    protected String monitorString;
    private boolean simulationSuccess;
    protected float hostMaxRisk;
    protected float maxOverprovisioningTime;
    protected float reservedOsHostResource;
    protected int days;
    protected int n_runs;
    protected int DAYSAMPLE;
    protected PlacementResult placementResult;
    protected DataCenter dataCenter;
    protected long simulationTime;
    private long computeStatisticTime;
    protected AddedHostDetail addedHostDetail;
    protected List<VirtualMachine> virtualMachines;

    public PlacementControllerBase(DataCenter dataCenter,float hostMaxRisk,
            float maxOverprovisioningTime, float reservedOsHostResource,
            int days, int n_runs, int daySample,AddedHostDetail addedHostDetail, List<VirtualMachine> virtualMachines) {
        super();
        this.dataCenter = dataCenter;
        this.hostMaxRisk = hostMaxRisk;
        this.maxOverprovisioningTime = maxOverprovisioningTime;
        this.reservedOsHostResource = reservedOsHostResource;
        this.days = days;
        this.n_runs = n_runs;
        DAYSAMPLE = daySample;
        this.addedHostDetail = addedHostDetail;
        this.virtualMachines = virtualMachines;
    }

    protected HostMachinePlacement addNewHost(ArrayList<HostMachinePlacement> availableHost,
            ArrayList<HostMachinePlacement> addedHost) {
        HostMachine added_host = new HostMachine(
                ((HostMachine) availableHost.get(0).getMachine()).getLocalUri(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasOS(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasNetworkAdapterList(), 
                ((HostMachine) availableHost.get(0).getMachine()).getHasMonitorInfoList(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasLocalStorageList(),
                ((HostMachine) availableHost.get(0).getMachine()).getUseSharedStorageList(),
                addedHostDetail.getAddedHostCpuCount(),
                addedHostDetail.getAddedHostCpuFreq(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasCPUType(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasIdentifier(),
                addedHostDetail.getAddedHostMemSize(),
                availableHost.get(0).getMachine().getHasName() + "_"+ addedHost.size()+1 +"_Added",
                ((HostMachine) availableHost.get(0).getMachine()).getHasAuthUserName(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasAuthUserPassword(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasCapacity(),
                ((HostMachine) availableHost.get(0).getMachine()).getHasMonitorState(),
                ((HostMachine) availableHost.get(0).getMachine()).getIsInDomain());
        //aggiungo host fittizi e continuo
        HostMachinePlacement hmp = new HostMachinePlacement(DAYSAMPLE,
                added_host,
                this.hostMaxRisk, 
                this.maxOverprovisioningTime,
                this.reservedOsHostResource);                    
        addedHost.add(hmp);
        hmp.Init(this.days * DAYSAMPLE);
        return hmp;
    }

    @Override
    public abstract PlacementResult allocateVm(
            ArrayList<VmResourceValueContainer> vmResourceValueContainers,
            HeuristicInterface ec, HeuristicCoefficientBuilder coeffBuilder,
            boolean stopSimulationIfNotFitting, boolean showOutput) throws Exception;

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#computeIterationStatistics(java.util.ArrayList)
     */
    @Override
    public PlacementResult computeIterationStatistics(PlacementResult placementResult) {
        
        long startTime = System.currentTimeMillis();
        
            
            BaseMachinePlacementContainer temp = null;
            if(placementResult.getUsedHost().size() > 0)
                temp = placementResult.getUsedHost().get(0);
            else if(placementResult.getAddedHost().size() > 0)
                temp = placementResult.getAddedHost().get(0);                
            
            for (HostMachinePlacement hostMachinePlacement : placementResult.getUsedHost()) {
                hostMachinePlacement.buildStatistics();
            }
            
            if(temp != null){
                ArrayList<HostMachinePlacement> machines = new ArrayList<HostMachinePlacement>();
                machines.addAll(placementResult.getUsedHost());
                placementResult.setDataCenterMachinePlacement( 
                new DataCenterMachinePlacement(DAYSAMPLE, this.dataCenter,temp.getReservedResource(),machines));            
            }
            
            placementResult.getDataCenterMachinePlacement().buildChartModel();
        
        long stopTime = System.currentTimeMillis();
        computeStatisticTime = stopTime - startTime;
        
        return placementResult;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#getCompletedSimulation()
     */
    @Override
    public int getCompletedSimulation() {
        return completedSimulation;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#getComputeStatisticTime()
     */
    @Override
    public long getComputeStatisticTime() {
        return computeStatisticTime;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#getMonitorString()
     */
    @Override
    public String getMonitorString() {
        return monitorString;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#getSimulationResult()
     */
    @Override
    public String getSimulationResult() {
        return simulationResult;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#getSimulationTime()
     */
    @Override
    public long getSimulationTime() {
        return simulationTime;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#isSimulationSuccess()
     */
    @Override
    public boolean isSimulationSuccess() {
        return simulationSuccess;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#setCompletedSimulation(int)
     */
    @Override
    public void setCompletedSimulation(int completedSimulation) {
        this.completedSimulation = completedSimulation;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#setMonitorString(java.lang.String)
     */
    @Override
    public void setMonitorString(String monitorString) {
        this.monitorString = monitorString;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#setSimulationResult(java.lang.String)
     */
    @Override
    public void setSimulationResult(String simulationResult) {
        this.simulationResult = simulationResult;
    }

    /* (non-Javadoc)
     * @see org.cloudsimulator.placement.PlacementControllerInterface#setSimulationSuccess(boolean)
     */
    @Override
    public void setSimulationSuccess(boolean simulationSuccess) {
        this.simulationSuccess = simulationSuccess;
    }

    protected void updateProgress(int iteration, int totalToAlloc, String hasName,
            int remain) {
                this.setMonitorString(this.getMonitorString() + "-> Allocating vm: <strong>" + hasName + "</strong>, "+ remain +" " + (remain>1?"vms":"vm") +"left.  <br />");
                //(totalToAlloc - remain) = already allocated;
                this.setCompletedSimulation(((totalToAlloc-remain) + (totalToAlloc * iteration)) * (100 / (totalToAlloc * n_runs)));
            }
    
    protected void showMessage(String message) {
                this.setMonitorString(this.getMonitorString() + "-> "+ message +"  <br />");                                
            }

}