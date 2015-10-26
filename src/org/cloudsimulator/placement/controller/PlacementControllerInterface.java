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

import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;

public interface PlacementControllerInterface {

    public abstract PlacementResult computeIterationStatistics(
            PlacementResult placementResult);

    public abstract int getCompletedSimulation();

    public abstract long getComputeStatisticTime();

    public abstract String getMonitorString();

    public abstract String getSimulationResult();

    public abstract long getSimulationTime();

    public abstract boolean isSimulationSuccess();

    public abstract void setCompletedSimulation(int completedSimulation);

    public abstract void setMonitorString(String monitorString);

    public abstract void setSimulationResult(String simulationResult);

    public abstract void setSimulationSuccess(boolean simulationSuccess);
    
    public abstract PlacementResult allocateVm(            
            ArrayList<VmResourceValueContainer> vmResourceValueContainers, 
            HeuristicInterface ec, HeuristicCoefficientBuilder coeffBuilder,
            boolean stopSimulationIfNotFitting, boolean showOutput) throws Exception;

}