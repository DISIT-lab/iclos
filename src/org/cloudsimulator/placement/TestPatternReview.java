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

import java.util.List;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

public class TestPatternReview {
    private int id;
    private String name;
    private String role;
    private List<String> path;
    private LineChartModel lineModelCpu;
    private LineChartModel lineModelMemory;
    protected List<Float> cpu_vals_list;    
    protected List<Float> mem_vals_list;
    
    public TestPatternReview(int id,String name, String role, List<String> path,
            List<Float> cpu_vals_list, List<Float> mem_vals_list) {
        super();
        this.id = id;
        this.name = name;
        this.role = role;
        this.path = path;
        this.cpu_vals_list = cpu_vals_list;
        this.mem_vals_list = mem_vals_list;
    }
    
    public void buildChart(){
        int n_sample = cpu_vals_list.size();
        
        lineModelCpu = new LineChartModel(); 
        lineModelCpu.setZoom(true);
        lineModelCpu.setTitle("CPU Workload");        
        lineModelMemory =  new LineChartModel(); 
        lineModelMemory.setZoom(true);
        lineModelMemory.setTitle("MEMORY Workload");
//        lineModelMemory.setExtender("VmPlacementChartLineWorkloadExtender");
//        lineModelCpu.setExtender("VmPlacementChartLineWorkloadExtender");
        
        LineChartSeries lineSeriesCpu = new LineChartSeries();
        LineChartSeries lineSeriesMemory = new LineChartSeries();

        lineSeriesCpu.setLabel("workload");        
        lineSeriesMemory.setLabel("workload");        
        
        lineSeriesCpu.setShowMarker(false);
        lineSeriesMemory.setShowMarker(false);
        
        
        for(int i=0;i<n_sample;i++){            
            float el = (float) cpu_vals_list.get(i);
            lineSeriesCpu.set(i,el);
            
            el = (float) mem_vals_list.get(i);            
            lineSeriesMemory.set(i,el);
            
        }
        
        lineModelCpu.addSeries(lineSeriesCpu);
        lineModelMemory.addSeries(lineSeriesMemory);
        
        
        Axis yAxis,xAxis;
        
        yAxis = lineModelCpu.getAxis(AxisType.Y);
        xAxis = lineModelCpu.getAxis(AxisType.X);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        yAxis.setMax(100);
        xAxis.setMin(0);
        xAxis.setMax(lineSeriesCpu.getData().size());
        
        yAxis = lineModelMemory.getAxis(AxisType.Y);
        xAxis = lineModelMemory.getAxis(AxisType.X);
        xAxis.setLabel("Time sample");
        yAxis.setLabel("Usage");
        yAxis.setMax(100);
        xAxis.setMin(0);
        xAxis.setMax(lineSeriesMemory.getData().size());
    }
    
    public List<Float> getCpu_vals_list() {
        return cpu_vals_list;
    }

    public LineChartModel getLineModelCpu() {
        return lineModelCpu;
    }       
    
    public LineChartModel getLineModelMemory() {
        return lineModelMemory;
    }
    public List<Float> getMem_vals_list() {
        return mem_vals_list;
    }
    public String getName() {
        return name;
    }
    public List<String> getPath() {
        return path;
    }
    public String getRole() {
        return role;
    }
    public void setCpu_vals_list(List<Float> cpu_vals_list) {
        this.cpu_vals_list = cpu_vals_list;
    }
    public void setLineModelCpu(LineChartModel lineModelCpu) {
        this.lineModelCpu = lineModelCpu;
    }
    public void setLineModelMemory(LineChartModel lineModelMemory) {
        this.lineModelMemory = lineModelMemory;
    }
    public void setMem_vals_list(List<Float> mem_vals_list) {
        this.mem_vals_list = mem_vals_list;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPath(List<String> path) {
        this.path = path;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
