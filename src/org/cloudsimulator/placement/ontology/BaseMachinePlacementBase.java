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

import org.cloudsimulator.domain.ontology.Machine;
import org.jblas.DoubleMatrix;
import org.primefaces.model.chart.LineChartModel;

public abstract class BaseMachinePlacementBase implements BaseMachinePlacement,Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6446843186890550317L;
    protected float cpu_limit;
    protected float memory_limit;
    protected Machine machine;
    protected DoubleMatrix cpu_workload;
    protected DoubleMatrix memory_workload;
    protected double cpu_avg_usage;
    protected double memory_avg_usage;
    protected double overall_avg_usage;
    protected double cpu_max_usage;
    private double memory_max_usage;
    protected LineChartModel lineModelCpu;
    protected LineChartModel lineModelMemory;
    protected DoubleMatrix assigned_cpu_matrix;
    protected DoubleMatrix assigned_memory_matrix;
    protected LineChartModel lineModelCpuArea;
    protected LineChartModel lineModelMemoryArea;
    public BaseMachinePlacementBase() {
        super();
    }
    

    protected abstract void buildAssignedMatrix();

    
    public abstract void buildStatistics();

    protected void calculateAvgUsage() {
        this.cpu_avg_usage = this.cpu_workload.mean() / this.cpu_limit;
        this.memory_avg_usage = this.memory_workload.mean() / this.memory_limit;
        
        this.overall_avg_usage = (this.cpu_avg_usage + this.memory_avg_usage) / 2;
    }
    
    void calculateCpuWorkload() {
        this.cpu_workload = this.assigned_cpu_matrix.columnSums();
    }

    protected void calculateMaxUsage() {
        this.setCpu_max_usage(this.cpu_workload.max());
        this.setMemory_max_usage(this.memory_workload.max());               
    }

    void calculateMemoryWorkload() {
        this.memory_workload = this.assigned_memory_matrix.columnSums();
    }

    protected Float computeCpuThroughtput(){
        return machine.getCpuThroughtput();
    }

    protected Float computeMemorySize(){
        return machine.getHasMemorySize();
    }

    public double getCpu_avg_usage() {
        return cpu_avg_usage;
    }

    public float getCpu_limit() {
        return cpu_limit;
    }

    public double getCpu_max_usage() {
        return cpu_max_usage;
    }

    public DoubleMatrix getCpu_workload() {
        return cpu_workload;
    }

    public LineChartModel getLineModelCpu() {
        return lineModelCpu;
    }

    public LineChartModel getLineModelCpuArea() {
        return lineModelCpuArea;
    }

    public LineChartModel getLineModelMemory() {
        return lineModelMemory;
    }

    public LineChartModel getLineModelMemoryArea() {
        return lineModelMemoryArea;
    }

    public Machine getMachine() {
       return this.machine;
    }

    public double getMemory_avg_usage() {
        return memory_avg_usage;
    }

    public float getMemory_limit() {
        return memory_limit;
    }

    public double getMemory_max_usage() {
        return memory_max_usage;
    }

    public DoubleMatrix getMemory_workload() {
        return memory_workload;
    }

    public double getOverall_avg_usage() {
        return overall_avg_usage;
    }

    public void setCpu_avg_usage(double cpu_avg_usage) {
        this.cpu_avg_usage = cpu_avg_usage;
    }

    public void setCpu_max_usage(double cpu_max_usage) {
        this.cpu_max_usage = cpu_max_usage;
    }


    public void setLineModelCpu(LineChartModel lineModelCpu) {
        this.lineModelCpu = lineModelCpu;
    }

    public void setLineModelCpuArea(LineChartModel lineModelCpuArea) {
        this.lineModelCpuArea = lineModelCpuArea;
    }


    public void setLineModelMemory(LineChartModel lineModelMemory) {
        this.lineModelMemory = lineModelMemory;
    }


    public void setLineModelMemoryArea(LineChartModel lineModelMemoryArea) {
        this.lineModelMemoryArea = lineModelMemoryArea;
    }


    public void setMachine(Machine machine) {
       this.machine = machine;
        
    }


    public void setMemory_avg_usage(double memory_avg_usage) {
        this.memory_avg_usage = memory_avg_usage;
    }


    public void setMemory_max_usage(double memory_max_usage) {
        this.memory_max_usage = memory_max_usage;
    }


    public void setOverall_avg_usage(double overall_avg_usage) {
        this.overall_avg_usage = overall_avg_usage;
    }

}