package org.cloudsimulator.placement.ontology;
import java.io.Serializable;
import java.util.ArrayList;

import org.jblas.DoubleMatrix;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

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


public abstract class BaseMachinePlacementContainer extends BaseMachinePlacementBase implements Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = -158343622313164028L;
    protected int n_sample;
    protected float available_cpu;
    protected float available_memory;

    protected float cpu_in_range;
    protected float memory_in_range;
    protected float overall_in_range;
    protected float reservedResource;
    
    public BaseMachinePlacementContainer() {}
    
    public BaseMachinePlacementContainer(int n_sample,float reservedResource) {
        this.n_sample = n_sample;
        this.reservedResource = reservedResource;
    }
    
    public void buildChartModel() {
        if(this.assigned_cpu_matrix == null || this.assigned_memory_matrix == null)
            buildStatistics();
        
        buildChartModelDetailed();
        buildChartModelOverall();                
    }

    public void buildChartModelDetailed() {
                
        
        lineModelCpuArea = new LineChartModel();       
        lineModelCpuArea.setStacked(true);        
        lineModelCpuArea.setZoom(true);        
        lineModelCpuArea.setTitle("CPU Workload");
        lineModelCpuArea.setExtender("VmPlacementChartStackedWorkloadExtender");
        lineModelCpuArea.setLegendPosition("s");
        
        lineModelMemoryArea =  new LineChartModel(); 
        lineModelMemoryArea.setStacked(true);
        lineModelMemoryArea.setZoom(true);
        lineModelMemoryArea.setTitle("MEMORY Workload");
        lineModelMemoryArea.setExtender("VmPlacementChartStackedWorkloadExtender");
        lineModelMemoryArea.setLegendPosition("s");
        
        LineChartSeries lineSeriesCpuLimit = new LineChartSeries();
        LineChartSeries lineSeriesCpuAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesCpuReserved = new LineChartSeries();        
        LineChartSeries lineSeriesMemoryLimit = new LineChartSeries();
        LineChartSeries lineSeriesMemoryAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryReserved = new LineChartSeries();
        
        lineSeriesCpuLimit.setLabel("limit");
        lineSeriesCpuLimit.setMarkerStyle("dash");
        
        
        lineSeriesCpuAvailable.setLabel("available");
        lineSeriesCpuAvailable.setMarkerStyle("dash");
//        lineSeriesCpuReserved.setLabel("reserved");
//        lineSeriesCpuReserved.setMarkerStyle("dash");
        lineSeriesMemoryLimit.setLabel("limit");
        lineSeriesMemoryLimit.setMarkerStyle("dash");
        lineSeriesMemoryAvailable.setLabel("available");
        lineSeriesMemoryAvailable.setMarkerStyle("dash");
//        lineSeriesMemoryReserved.setLabel("reserved");
//        lineSeriesMemoryReserved.setMarkerStyle("dash");
               
        
        int n_interval = this.n_sample * 2;                
        
        LineChartSeries lineSeriesCpu = null;
        LineChartSeries lineSeriesMemory = null;
        
        int y_min_cpu = (int) this.cpu_limit;
        int y_min_mem = (int) this.memory_limit;
        
        
        
        for(int i=0;i<n_interval/2;i++){            
            /*  Set CPU limit   */
            lineSeriesCpuLimit.set(i, this.cpu_limit);
            lineSeriesCpuAvailable.set(i, this.available_cpu);
//            lineSeriesCpuReserved.set(i, computeCpuThroughtput() * (1-this.getReservedResource()));
            /*  Set MEMORY limit   */
            lineSeriesMemoryLimit.set(i, this.memory_limit);
            lineSeriesMemoryAvailable.set(i, this.available_memory);
//            lineSeriesMemoryReserved.set(i, (float) getMachine().getHasMemorySize() * (1-this.getReservedResource()));
        }
        
        lineSeriesCpuLimit.setDisableStack(true);
        lineModelCpuArea.addSeries(lineSeriesCpuLimit);
        lineSeriesCpuAvailable.setDisableStack(true);
        lineModelCpuArea.addSeries(lineSeriesCpuAvailable);
//        lineModelCpuArea.setSeriesColors("d9534f,5cb85c");

        //        lineSeriesCpuReserved.setDisableStack(true);        
//        lineModelCpuArea.addSeries(lineSeriesCpuReserved);
//        
        lineSeriesMemoryLimit.setDisableStack(true);
        lineModelMemoryArea.addSeries(lineSeriesMemoryLimit);
        lineSeriesMemoryAvailable.setDisableStack(true);
        lineModelMemoryArea.addSeries(lineSeriesMemoryAvailable);
//        lineModelMemoryArea.setSeriesColors("d9534f,5cb85c");

        //        lineSeriesMemoryReserved.setDisableStack(true);
//        lineModelMemoryArea.addSeries(lineSeriesMemoryReserved);
        
        ArrayList<String> assignedMachineNames = getAssignedMachineNames();
        for (int j=0;j<assigned_cpu_matrix.rows;j++) {
            
            lineSeriesCpu = new LineChartSeries();
            lineSeriesMemory = new LineChartSeries();
            
            lineSeriesCpu.setShowMarker(false);
            lineSeriesCpu.setFill(true);           
            lineSeriesMemory.setShowMarker(false);
            lineSeriesMemory.setFill(true);
            
            //Range range = new IntervalRange(0, n_interval/2);            
            //DoubleMatrix tempCpu = vm.getNot_normalized_demand().getColumns(range);
            DoubleMatrix tempCpu = assigned_cpu_matrix.getRow(j);
                        
            //range = new IntervalRange(n_interval/2, (n_interval));
            //DoubleMatrix tempMemory  = vm.getNot_normalized_demand().getColumns(range);         
            DoubleMatrix tempMemory  = assigned_memory_matrix.getRow(j);         
            
            for(int i=0;i<n_interval/2;i++){            
                float el = (float) tempCpu.get(i);
                lineSeriesCpu.set(i,el);
                if(el<y_min_cpu) y_min_cpu = (int) el;
                
                el = (float) tempMemory.get(i);            
                lineSeriesMemory.set(i,el);
                if(el<y_min_mem) y_min_mem = (int) el;                
            }
            
            if(j==0){
                lineSeriesCpu.setDisableStack(true);
                lineSeriesMemory.setDisableStack(true);
            }
                
            if(j<assignedMachineNames.size()){
                String vmName = assignedMachineNames.get(j);
                lineSeriesCpu.setLabel(vmName);
                lineSeriesMemory.setLabel(vmName);
            }
            lineModelCpuArea.addSeries(lineSeriesCpu);
            lineModelMemoryArea.addSeries(lineSeriesMemory);            
        }
        
        Axis yAxis,xAxis;
        
        //lineModelCpuArea.setLegendPosition("e");        
        xAxis = lineModelMemoryArea.getAxis(AxisType.X);
        yAxis = lineModelCpuArea.getAxis(AxisType.Y);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        xAxis.setMax(lineSeriesCpu.getData().size());
        yAxis.setMin(y_min_cpu);
        yAxis.setMax(computeCpuThroughtput());
        lineModelCpuArea.getAxes().put(AxisType.X, xAxis);
        
        //lineModelMemoryArea.setLegendPosition("e");        
        yAxis = lineModelMemoryArea.getAxis(AxisType.Y);
        xAxis = lineModelMemoryArea.getAxis(AxisType.X);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        xAxis.setMin(0);
        xAxis.setMax(lineSeriesMemory.getData().size());
        yAxis.setMin(y_min_mem);        
        yAxis.setMax(computeMemorySize());
               
    }

    protected abstract ArrayList<String> getAssignedMachineNames();    

    public void buildChartModelOverall() {
        
        lineModelCpu = new LineChartModel(); 
        lineModelCpu.setZoom(true);
        lineModelCpu.setTitle("CPU Workload");       
        lineModelCpu.setLegendPosition("ne");
        
        lineModelMemory =  new LineChartModel(); 
        lineModelMemory.setZoom(true);
        lineModelMemory.setTitle("MEMORY Workload");
        lineModelMemory.setLegendPosition("ne");
        
        
        LineChartSeries lineSeriesCpu = new LineChartSeries();
        LineChartSeries lineSeriesCpuLimit = new LineChartSeries();
        LineChartSeries lineSeriesCpuAvailable = new LineChartSeries();
        //LineChartSeries lineSeriesCpuReserved = new LineChartSeries();
        LineChartSeries lineSeriesMemory = new LineChartSeries();
        LineChartSeries lineSeriesMemoryLimit = new LineChartSeries();
        LineChartSeries lineSeriesMemoryAvailable = new LineChartSeries();
        //LineChartSeries lineSeriesMemoryReserved = new LineChartSeries();
        
        lineSeriesCpu.setLabel("workload");        
        lineSeriesMemory.setLabel("workload");        
        lineSeriesCpuLimit.setLabel("limit");
        lineSeriesCpuLimit.setMarkerStyle("dash");
        lineSeriesCpuAvailable.setLabel("available");
        lineSeriesCpuAvailable.setMarkerStyle("dash");
//        lineSeriesCpuReserved.setLabel("reserved");
//        lineSeriesCpuReserved.setMarkerStyle("dash");
        lineSeriesMemoryLimit.setLabel("limit");
        lineSeriesMemoryLimit.setMarkerStyle("dash");
        lineSeriesMemoryAvailable.setLabel("available");
        lineSeriesMemoryAvailable.setMarkerStyle("dash");
//        lineSeriesMemoryReserved.setLabel("reserved");
//        lineSeriesMemoryReserved.setMarkerStyle("dash");
        
        lineSeriesCpu.setShowMarker(false);
        lineSeriesMemory.setShowMarker(false);
        
        int n_interval = this.n_sample * 2;                
        /*DoubleMatrix cpu_usage = DoubleMatrix.zeros(this.assignedVm.size(),this.residual_resource.columns/2);
        DoubleMatrix memory_usage = DoubleMatrix.zeros(this.assignedVm.size(),this.residual_resource.columns/2);
        
        int i=0;
        for (VirtualMachinePlacement vm : assignedVm) {
            Range range = new IntervalRange(0, n_interval/2);            
            DoubleMatrix temp = vm.getNot_normalized_demand().getColumns(range);
            cpu_usage.putRow(i, temp);
            
            range = new IntervalRange(n_interval/2, (n_interval));
            temp  = vm.getNot_normalized_demand().getColumns(range);
            memory_usage.putRow(i,temp );                                    
            
            i++;
        }*/
        
        DoubleMatrix total_cpu_usage = assigned_cpu_matrix.columnSums();
        DoubleMatrix total_memory_usage = assigned_memory_matrix.columnSums();
        
        int y_min_cpu = (int) (total_cpu_usage.min())-1;
        int y_min_memory = (int) (total_memory_usage.min())-1;;
        
        for(int i=0;i<n_interval/2;i++){            
            float el = (float) total_cpu_usage.get(i);
            lineSeriesCpu.set(i,el);
            
            el = (float) total_memory_usage.get(i);            
            lineSeriesMemory.set(i,el);
            
            /*  Set CPU limit   */
            lineSeriesCpuLimit.set(i, this.cpu_limit);
            lineSeriesCpuAvailable.set(i, this.available_cpu);
//            lineSeriesCpuReserved.set(i, computeCpuThroughtput() * (1-this.getReservedResource()));
            /*  Set MEMORY limit   */
            lineSeriesMemoryLimit.set(i, this.memory_limit);
            lineSeriesMemoryAvailable.set(i, this.available_memory);
//            lineSeriesMemoryReserved.set(i, (float) computeMemorySize() * (1-this.getReservedResource()));
        }
        
        lineModelCpu.addSeries(lineSeriesCpuLimit);
        lineModelCpu.addSeries(lineSeriesCpuAvailable);
//        lineModelCpu.addSeries(lineSeriesCpuReserved);        
        lineModelCpu.addSeries(lineSeriesCpu);
        lineModelCpu.setSeriesColors("d9534f,5cb85c,337ab7");
        
        lineModelMemory.addSeries(lineSeriesMemoryLimit);
        lineModelMemory.addSeries(lineSeriesMemoryAvailable);
//        lineModelMemory.addSeries(lineSeriesMemoryReserved);        
        lineModelMemory.addSeries(lineSeriesMemory);
        lineModelMemory.setSeriesColors("d9534f,5cb85c,337ab7");
        
        Axis yAxis,xAxis;
        
        //lineModelCpu.setLegendPosition("e");        
        yAxis = lineModelCpu.getAxis(AxisType.Y);
        xAxis = lineModelCpu.getAxis(AxisType.X);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        xAxis.setMin(0);
        xAxis.setMax(lineSeriesCpu.getData().size());
        yAxis.setMin(y_min_cpu);
        yAxis.setMax(computeCpuThroughtput());
        
        //lineModelMemory.setLegendPosition("e");
        //lineModelMemory.setStacked(true);
        yAxis = lineModelMemory.getAxis(AxisType.Y);
        xAxis = lineModelMemory.getAxis(AxisType.X);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        xAxis.setMin(0);
        xAxis.setMax(lineSeriesMemory.getData().size());
        yAxis.setMin(y_min_memory);        
        yAxis.setMax(computeMemorySize());
        
    }

    public void buildStatistics() {
        buildAssignedMatrix();
        calculateCpuWorkload();
        calculateMemoryWorkload();
        calculateAvgUsage();
        calculateMaxUsage();
        calculateOver();        
        calculateTestRatio();
    }
    
    protected abstract void calculateOver();
    
    protected abstract void calculateTestRatio();

    public float getAvailable_cpu() {
        return available_cpu;
    }

    public float getAvailable_memory() {
        return available_memory;
    }

    public float getCpu_in_range() {
        return cpu_in_range;
    }

    public float getMemory_in_range() {
        return memory_in_range;
    }    

    public float getOverall_in_range() {
        return overall_in_range;
    }

    public float getReservedResource() {
        return reservedResource;
    }

    public void setAvailable_cpu(float available_cpu) {
        this.available_cpu = available_cpu;
    }

    public void setAvailable_memory(float available_memory) {
        this.available_memory = available_memory;
    }

    public void setCpu_in_range(float cpu_in_range) {
        this.cpu_in_range = cpu_in_range;
    }

    public void setMemory_in_range(float memory_in_range) {
        this.memory_in_range = memory_in_range;
    }

    public void setOverall_in_range(float overall_in_range) {
        this.overall_in_range = overall_in_range;
    }          
    
    
}