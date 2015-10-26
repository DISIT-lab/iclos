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
import java.util.List;

import org.cloudsimulator.domain.ontology.Machine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.TestCasePatternWrapper;
import org.jblas.DoubleMatrix;
import org.jblas.ranges.IntervalRange;
import org.jblas.ranges.Range;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

public class VirtualMachinePlacement extends BaseMachinePlacementBase implements Serializable{
    
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5963893979806631671L;

    public static DoubleMatrix buildRequestedResourceMatrix(ArrayList<VirtualMachinePlacement> toBeAllocs) {
              
        DoubleMatrix requestedResource_matrix = DoubleMatrix.zeros(toBeAllocs.size(),
                toBeAllocs.get(0).getNormalizedResourceDemandWrtBin().columns);
                
        for(int i=0;i < toBeAllocs.size();i++){                        
            requestedResource_matrix.putRow(i,toBeAllocs.get(i).getNormalizedResourceDemandWrtBin());            
        }
        return requestedResource_matrix;
    }
    private HostMachinePlacement assignedTo;
    
    protected List<Float> cpu_vals_list;    
    protected List<Float> mem_vals_list;
    
    private double FFDWeight = 0;
    /*
     * Contiene i valori originali tra 0 ed 1 del pattern assegnato alla macchina 
     */
    protected DoubleMatrix originalRequired;
    /*
     * Contiene i valori originali del pattern moltiplicati per le risorse della VM 
     */
    private DoubleMatrix not_normalized_demand;
    
    /*
     * Contiene i valori originali del pattern moltiplicati per le risorse della VM 
     * e divisi per le risorse presenti nel bin
     */
    private DoubleMatrix normalizedResourceDemandWrtBin;

    private TestCasePatternWrapper testCasePatternWrapper;;
            
    public VirtualMachinePlacement(VirtualMachine virtualMachine,
            List<Float> cpu_vals_list, List<Float> mem_vals_list,TestCasePatternWrapper testCasePatternWrapper) {
        super();
        this.machine = virtualMachine;
        this.cpu_vals_list = cpu_vals_list;
        this.mem_vals_list = mem_vals_list;
        this.cpu_limit = this.machine.getCpuThroughtput();
        this.memory_limit = this.machine.getHasMemorySize();
        this.setTestCasePatternWrapper(testCasePatternWrapper);
        
        ArrayList<Float> temp = new ArrayList<Float>();
        temp.addAll(cpu_vals_list);
        temp.addAll(mem_vals_list);
        
        this.originalRequired = new DoubleMatrix(1,temp.size());
        
        for(int i=0;i < temp.size();i++){
            this.originalRequired.put(i, (double) temp.get(i) / 100);
        }
        
        
        double vm_max_cpu = (double) this.getMachine().getCpuThroughtput();
        double vm_max_memory = (double) this.getMachine().getHasMemorySize();     
        
        this.not_normalized_demand = new DoubleMatrix(1,this.getOriginalRequired().columns);
        for(int i=0;i<this.getOriginalRequired().columns / 2;i++)
            
            not_normalized_demand.put(i, vm_max_cpu);                
        for(int i=this.getOriginalRequired().columns / 2;i<this.getOriginalRequired().columns;i++)
            not_normalized_demand.put(i, vm_max_memory);           
        
        not_normalized_demand.muli(this.getOriginalRequired());
        
        buildStatistics();
    }

    @Override
    protected void buildAssignedMatrix() {
        int n_sample = getNot_normalized_demand().columns / 2;
        int n_interval = n_sample * 2;                        
        assigned_cpu_matrix = DoubleMatrix.zeros(1,n_sample);
        assigned_memory_matrix = DoubleMatrix.zeros(1,n_sample);
        
        int i=0;
        
        Range range = new IntervalRange(0, n_interval/2);            
        DoubleMatrix temp = getNot_normalized_demand().getColumns(range);
        assigned_cpu_matrix.putRow(i, temp);
        
        range = new IntervalRange(n_interval/2, (n_interval));
        temp  = getNot_normalized_demand().getColumns(range);
        assigned_memory_matrix.putRow(i,temp);                                                                   
        
    }

    public void buildChartModel() {
        if(this.assigned_cpu_matrix == null || this.assigned_memory_matrix == null)
            buildStatistics();
        
        buildChartModelDetailed();
        buildChartModelOverall();                
    }

    public void buildChartModelDetailed() {
                
        
        int n_sample = getNot_normalized_demand().columns / 2;
        
        lineModelCpuArea = new LineChartModel();       
        lineModelCpuArea.setStacked(true);        
        lineModelCpuArea.setZoom(true);        
        lineModelCpuArea.setTitle("CPU Workload");
        lineModelCpuArea.setExtender("VmPlacementChartStackedWorkloadExtender");
        
        lineModelMemoryArea =  new LineChartModel(); 
        lineModelMemoryArea.setStacked(true);
        lineModelMemoryArea.setZoom(true);
        lineModelMemoryArea.setTitle("MEMORY Workload");
        lineModelMemoryArea.setExtender("VmPlacementChartStackedWorkloadExtender");
        
//        LineChartSeries lineSeriesCpuLimit = new LineChartSeries();
//        LineChartSeries lineSeriesCpuAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesCpuReserved = new LineChartSeries();        
//        LineChartSeries lineSeriesMemoryLimit = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryReserved = new LineChartSeries();
//        
//        lineSeriesCpuLimit.setLabel("limit");
//        lineSeriesCpuLimit.setMarkerStyle("dash");
//        lineSeriesCpuAvailable.setLabel("available");
//        lineSeriesCpuAvailable.setMarkerStyle("dash");
//        lineSeriesCpuReserved.setLabel("reserved");
//        lineSeriesCpuReserved.setMarkerStyle("dash");
//        lineSeriesMemoryLimit.setLabel("limit");
//        lineSeriesMemoryLimit.setMarkerStyle("dash");
//        lineSeriesMemoryAvailable.setLabel("available");
//        lineSeriesMemoryAvailable.setMarkerStyle("dash");
//        lineSeriesMemoryReserved.setLabel("reserved");
//        lineSeriesMemoryReserved.setMarkerStyle("dash");
               
        
        int n_interval = n_sample * 2;                
        
        LineChartSeries lineSeriesCpu = null;
        LineChartSeries lineSeriesMemory = null;
        
        int y_min_cpu = (int) this.cpu_limit;
        int y_min_mem = (int) this.memory_limit;
        
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
            lineModelCpuArea.addSeries(lineSeriesCpu);
            lineModelMemoryArea.addSeries(lineSeriesMemory);            
        }
        
//        for(int i=0;i<n_interval/2;i++){            
//            /*  Set CPU limit   */
//            lineSeriesCpuLimit.set(i, this.cpu_limit);
//            lineSeriesCpuAvailable.set(i, this.available_cpu);
//            lineSeriesCpuReserved.set(i, computeCpuThroughtput() * (1-this.getReservedResource()));
//            /*  Set MEMORY limit   */
//            lineSeriesMemoryLimit.set(i, this.memory_limit);
//            lineSeriesMemoryAvailable.set(i, this.available_memory);
//            lineSeriesMemoryReserved.set(i, (float) getMachine().getHasMemorySize() * (1-this.getReservedResource()));
//        }
        
//        lineSeriesCpuLimit.setDisableStack(true);
//        lineModelCpuArea.addSeries(lineSeriesCpuLimit);
//        lineSeriesCpuAvailable.setDisableStack(true);
//        lineModelCpuArea.addSeries(lineSeriesCpuAvailable);
//        lineSeriesCpuReserved.setDisableStack(true);        
//        lineModelCpuArea.addSeries(lineSeriesCpuReserved);
//        
//        lineSeriesMemoryLimit.setDisableStack(true);
//        lineModelMemoryArea.addSeries(lineSeriesMemoryLimit);
//        lineSeriesMemoryAvailable.setDisableStack(true);
//        lineModelMemoryArea.addSeries(lineSeriesMemoryAvailable);
//        lineSeriesMemoryReserved.setDisableStack(true);
//        lineModelMemoryArea.addSeries(lineSeriesMemoryReserved);
        
        
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

    public void buildChartModelOverall() {
        
        int n_sample = getNot_normalized_demand().columns / 2;
        
        lineModelCpu = new LineChartModel(); 
        lineModelCpu.setZoom(true);
        lineModelCpu.setTitle("CPU Workload");        
        lineModelMemory =  new LineChartModel(); 
        lineModelMemory.setZoom(true);
        lineModelMemory.setTitle("MEMORY Workload");
        
        LineChartSeries lineSeriesCpu = new LineChartSeries();
//        LineChartSeries lineSeriesCpuLimit = new LineChartSeries();
//        LineChartSeries lineSeriesCpuAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesCpuReserved = new LineChartSeries();
        LineChartSeries lineSeriesMemory = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryLimit = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryAvailable = new LineChartSeries();
//        LineChartSeries lineSeriesMemoryReserved = new LineChartSeries();
//        
        lineSeriesCpu.setLabel("workload");        
        lineSeriesMemory.setLabel("workload");        
//        lineSeriesCpuLimit.setLabel("limit");
//        lineSeriesCpuLimit.setMarkerStyle("dash");
//        lineSeriesCpuAvailable.setLabel("available");
//        lineSeriesCpuAvailable.setMarkerStyle("dash");
//        lineSeriesCpuReserved.setLabel("reserved");
//        lineSeriesCpuReserved.setMarkerStyle("dash");
//        lineSeriesMemoryLimit.setLabel("limit");
//        lineSeriesMemoryLimit.setMarkerStyle("dash");
//        lineSeriesMemoryAvailable.setLabel("available");
//        lineSeriesMemoryAvailable.setMarkerStyle("dash");
//        lineSeriesMemoryReserved.setLabel("reserved");
//        lineSeriesMemoryReserved.setMarkerStyle("dash");
        
        lineSeriesCpu.setShowMarker(false);
        lineSeriesMemory.setShowMarker(false);
        
        int n_interval = n_sample * 2;                
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
            
//            /*  Set CPU limit   */
//            lineSeriesCpuLimit.set(i, this.cpu_limit);
//            lineSeriesCpuAvailable.set(i, this.available_cpu);
//            lineSeriesCpuReserved.set(i, computeCpuThroughtput() * (1-this.getReservedResource()));
//            /*  Set MEMORY limit   */
//            lineSeriesMemoryLimit.set(i, this.memory_limit);
//            lineSeriesMemoryAvailable.set(i, this.available_memory);
//            lineSeriesMemoryReserved.set(i, (float) computeMemorySize() * (1-this.getReservedResource()));
        }
        
        lineModelCpu.addSeries(lineSeriesCpu);
//        lineModelCpu.addSeries(lineSeriesCpuLimit);
//        lineModelCpu.addSeries(lineSeriesCpuAvailable);
//        lineModelCpu.addSeries(lineSeriesCpuReserved);
        lineModelMemory.addSeries(lineSeriesMemory);
//        lineModelMemory.addSeries(lineSeriesMemoryLimit);
//        lineModelMemory.addSeries(lineSeriesMemoryAvailable);
//        lineModelMemory.addSeries(lineSeriesMemoryReserved);
        
        
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
    }

//    private double[] to1Dmatrix() {
//        ArrayList<Float> temp = new ArrayList<Float>();
//        temp.addAll(this.getCpu_vals_list());
//        temp.addAll(this.getMem_vals_list());
//        
//        double[] doubleArray = new double[temp.size()];
//        
//        for(int i=0;i < temp.size();i++){
//            doubleArray[i] = (double) temp.get(i);
//        }
//        
//        return doubleArray;
//    }

    public HostMachinePlacement getAssignedTo() {
        return assignedTo;
    }

    public List<Float> getCpu_vals_list() {
        return cpu_vals_list;
    }

    public double getFFDWeight() {
        return FFDWeight;
    }

    public Machine getMachine() {
        return this.machine;
    }
    
    public List<Float> getMem_vals_list() {
        return mem_vals_list;
    }

    public DoubleMatrix getNormalizedResourceDemandWrtBin() {
        return normalizedResourceDemandWrtBin;
    }

    public DoubleMatrix getNot_normalized_demand() {
        return not_normalized_demand;
    }

    public DoubleMatrix getOriginalRequired() {
        return originalRequired;
    }

    public TestCasePatternWrapper getTestCasePatternWrapper() {
        return testCasePatternWrapper;
    }

    public void setAssignedTo(HostMachinePlacement assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setCpu_vals_list(List<Float> cpu_vals_list) {
        this.cpu_vals_list = cpu_vals_list;
    }

    public void setFFDWeight(double fFDWeight) {
        FFDWeight = fFDWeight;
    }

    public void setMachine(Machine machine) {
        this.machine = (VirtualMachine) machine;        
    }

    public void setMem_vals_list(List<Float> mem_vals_list) {
        this.mem_vals_list = mem_vals_list;
    }

    public void setNormalizedResourceDemandWrtBin(DoubleMatrix normalizedResourceDemandWrtOpenedBin) {
        this.normalizedResourceDemandWrtBin = normalizedResourceDemandWrtOpenedBin;
    }

    public void setOriginalRequired(DoubleMatrix originalRequired) {
        this.originalRequired = originalRequired;
    }

    public void setTestCasePatternWrapper(TestCasePatternWrapper testCasePatternWrapper) {
        this.testCasePatternWrapper = testCasePatternWrapper;
    }

}