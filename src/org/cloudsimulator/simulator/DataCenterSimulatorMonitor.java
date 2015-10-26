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

package org.cloudsimulator.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenterSimulatorMonitor implements Runnable, Serializable {

    private static final long serialVersionUID = -7470778714908734574L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataCenterSimulatorMonitor.class);
    private boolean started;
    private static final Integer SAMPLETOVISUALIZE = 40;
    private Integer monitorTime;
    private List<CartesianChartModel> cartesianChartModelCPUList;
    private List<CartesianChartModel> cartesianChartModelMemoryList;
    private List<CartesianChartModel> cartesianChartModelStorageList;
    private List<List<CartesianChartModel>> cartesianChartModelCPUListList;
    private List<List<CartesianChartModel>> cartesianChartModelMemoryListList;
    private List<List<CartesianChartModel>> cartesianChartModelStorageListList;
    private List<HostMachineSimulator> hostMachineSimulatorList;

    public DataCenterSimulatorMonitor(
            final List<HostMachineSimulator> hostMachineSimulatorList) {
        this.monitorTime = 5;

        this.cartesianChartModelCPUList = new ArrayList<CartesianChartModel>();
        this.cartesianChartModelMemoryList = new ArrayList<CartesianChartModel>();
        this.cartesianChartModelStorageList = new ArrayList<CartesianChartModel>();

        this.cartesianChartModelCPUListList = new ArrayList<List<CartesianChartModel>>();
        this.cartesianChartModelMemoryListList = new ArrayList<List<CartesianChartModel>>();
        this.cartesianChartModelStorageListList = new ArrayList<List<CartesianChartModel>>();

        this.hostMachineSimulatorList = hostMachineSimulatorList;
    }

    @Override
    public void run() {
        LOGGER.info("Start: DataCenterSimulationMonitor");
        this.started = true;

        while (this.started) {

            try {
                Thread.sleep(this.monitorTime * 1000L);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }

            updateAllCartesianChartModel();

        }

        LOGGER.info("Stop : DataCenterSimulationMonitor");
        return;

    }

    private void updateAllCartesianChartModel() {
        updateCartesianChartModelCPU();
        updateCartesianChartModelMemory();
        updateCartesianChartModelStorage();
    }

    private void updateCartesianChartModelCPU() {
        Integer indexHostMachineSimulator;

        for (HostMachineSimulator hostMachineSimulator : this.hostMachineSimulatorList) {
            indexHostMachineSimulator = this.hostMachineSimulatorList
                    .indexOf(hostMachineSimulator);
            if (this.cartesianChartModelCPUList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelCPUList.add(new CartesianChartModel());
            }

            ChartSeries chartSeries;

            if (this.cartesianChartModelCPUList.get(indexHostMachineSimulator)
                    .getSeries().isEmpty()) {
                chartSeries = new ChartSeries();
                for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                    chartSeries.set(j, 0);
                }
            } else {
                chartSeries = this.cartesianChartModelCPUList
                        .get(indexHostMachineSimulator).getSeries().get(0);
            }

            for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                chartSeries.set(j, chartSeries.getData().get(j + 1));
            }

            chartSeries.set(SAMPLETOVISUALIZE - 1,
                    hostMachineSimulator.getCpuMhzActuallyUsed());
            CartesianChartModel cartesianChartModel = new CartesianChartModel();
            cartesianChartModel.setTitle("CPU (Mhz)");
            cartesianChartModel.addSeries(chartSeries);
            this.cartesianChartModelCPUList.set(indexHostMachineSimulator,
                    cartesianChartModel);

            if (this.cartesianChartModelCPUListList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelCPUListList
                        .add(new ArrayList<CartesianChartModel>());
            }

            Integer indexVirtualMachineSimulator;
            if (hostMachineSimulator.getVirtualMachineSimulatorList() != null) {
                for (VirtualMachineSimulator virtualMachineSimulator : hostMachineSimulator
                        .getVirtualMachineSimulatorList()) {
                    indexVirtualMachineSimulator = hostMachineSimulator
                            .getVirtualMachineSimulatorList().indexOf(
                                    virtualMachineSimulator);

                    if (this.cartesianChartModelCPUListList.get(
                            indexHostMachineSimulator).size() == indexVirtualMachineSimulator) {
                        this.cartesianChartModelCPUListList.get(
                                indexHostMachineSimulator).add(
                                new CartesianChartModel());
                    }

                    ChartSeries chartSeriesVirtual;

                    if (this.cartesianChartModelCPUListList
                            .get(indexHostMachineSimulator)
                            .get(indexVirtualMachineSimulator).getSeries()
                            .isEmpty()) {
                        chartSeriesVirtual = new ChartSeries();
                        for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                            chartSeriesVirtual.set(j, 0);
                        }
                    } else {
                        chartSeriesVirtual = this.cartesianChartModelCPUListList
                                .get(indexHostMachineSimulator)
                                .get(indexVirtualMachineSimulator).getSeries()
                                .get(0);
                    }

                    for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                        chartSeriesVirtual.set(j, chartSeriesVirtual.getData()
                                .get(j + 1));
                    }

                    chartSeriesVirtual.set(SAMPLETOVISUALIZE - 1,
                            virtualMachineSimulator.getCpuMhzActuallyUsed());
                    CartesianChartModel cartesianChartModelVirtual = new CartesianChartModel();
                    cartesianChartModelVirtual.setTitle("CPU (Mhz)");
                    cartesianChartModelVirtual.addSeries(chartSeriesVirtual);
                    this.cartesianChartModelCPUListList.get(
                            indexHostMachineSimulator).set(
                            indexVirtualMachineSimulator,
                            cartesianChartModelVirtual);

                }
            }
        }
    }

    private void updateCartesianChartModelMemory() {
        Integer indexHostMachineSimulator;

        for (HostMachineSimulator hostMachineSimulator : this.hostMachineSimulatorList) {
            indexHostMachineSimulator = this.hostMachineSimulatorList
                    .indexOf(hostMachineSimulator);
            if (this.cartesianChartModelMemoryList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelMemoryList
                        .add(new CartesianChartModel());
            }

            ChartSeries chartSeries;

            if (this.cartesianChartModelMemoryList
                    .get(indexHostMachineSimulator).getSeries().isEmpty()) {
                chartSeries = new ChartSeries();
                for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                    chartSeries.set(j, 0);
                }
            } else {
                chartSeries = this.cartesianChartModelMemoryList
                        .get(indexHostMachineSimulator).getSeries().get(0);
            }

            for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                chartSeries.set(j, chartSeries.getData().get(j + 1));
            }

            chartSeries.set(SAMPLETOVISUALIZE - 1,
                    hostMachineSimulator.getMemoryActuallyUsed());
            CartesianChartModel cartesianChartModel = new CartesianChartModel();
            cartesianChartModel.setTitle("Memory (GB)");
            cartesianChartModel.addSeries(chartSeries);
            this.cartesianChartModelMemoryList.set(indexHostMachineSimulator,
                    cartesianChartModel);

            if (this.cartesianChartModelMemoryListList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelMemoryListList
                        .add(new ArrayList<CartesianChartModel>());
            }

            Integer indexVirtualMachineSimulator;
            if (hostMachineSimulator.getVirtualMachineSimulatorList() != null) {
                for (VirtualMachineSimulator virtualMachineSimulator : hostMachineSimulator
                        .getVirtualMachineSimulatorList()) {
                    indexVirtualMachineSimulator = hostMachineSimulator
                            .getVirtualMachineSimulatorList().indexOf(
                                    virtualMachineSimulator);

                    if (this.cartesianChartModelMemoryListList.get(
                            indexHostMachineSimulator).size() == indexVirtualMachineSimulator) {
                        this.cartesianChartModelMemoryListList.get(
                                indexHostMachineSimulator).add(
                                new CartesianChartModel());
                    }

                    ChartSeries chartSeriesVirtual;

                    if (this.cartesianChartModelMemoryListList
                            .get(indexHostMachineSimulator)
                            .get(indexVirtualMachineSimulator).getSeries()
                            .isEmpty()) {
                        chartSeriesVirtual = new ChartSeries();
                        for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                            chartSeriesVirtual.set(j, 0);
                        }
                    } else {
                        chartSeriesVirtual = this.cartesianChartModelMemoryListList
                                .get(indexHostMachineSimulator)
                                .get(indexVirtualMachineSimulator).getSeries()
                                .get(0);
                    }

                    for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                        chartSeriesVirtual.set(j, chartSeriesVirtual.getData()
                                .get(j + 1));
                    }

                    chartSeriesVirtual.set(SAMPLETOVISUALIZE - 1,
                            virtualMachineSimulator.getMemoryActuallyUsed());
                    CartesianChartModel cartesianChartModelVirtual = new CartesianChartModel();
                    cartesianChartModelVirtual.setTitle("Memory (GB)");
                    cartesianChartModelVirtual.addSeries(chartSeriesVirtual);
                    this.cartesianChartModelMemoryListList.get(
                            indexHostMachineSimulator).set(
                            indexVirtualMachineSimulator,
                            cartesianChartModelVirtual);

                }
            }
        }
    }

    private void updateCartesianChartModelStorage() {
        Integer indexHostMachineSimulator;

        for (HostMachineSimulator hostMachineSimulator : this.hostMachineSimulatorList) {
            indexHostMachineSimulator = this.hostMachineSimulatorList
                    .indexOf(hostMachineSimulator);
            if (this.cartesianChartModelStorageList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelStorageList
                        .add(new CartesianChartModel());
            }

            ChartSeries chartSeries;

            if (this.cartesianChartModelStorageList
                    .get(indexHostMachineSimulator).getSeries().isEmpty()) {
                chartSeries = new ChartSeries();
                for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                    chartSeries.set(j, 0);
                }
            } else {
                chartSeries = this.cartesianChartModelStorageList
                        .get(indexHostMachineSimulator).getSeries().get(0);
            }

            for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                chartSeries.set(j, chartSeries.getData().get(j + 1));
            }

            chartSeries.set(SAMPLETOVISUALIZE - 1,
                    hostMachineSimulator.getStorageActuallyUsed());
            CartesianChartModel cartesianChartModel = new CartesianChartModel();
            cartesianChartModel.setTitle("Storage (GB)");
            cartesianChartModel.addSeries(chartSeries);
            this.cartesianChartModelStorageList.set(indexHostMachineSimulator,
                    cartesianChartModel);

            if (this.cartesianChartModelStorageListList.size() == indexHostMachineSimulator) {
                this.cartesianChartModelStorageListList
                        .add(new ArrayList<CartesianChartModel>());
            }

            Integer indexVirtualMachineSimulator;
            if (hostMachineSimulator.getVirtualMachineSimulatorList() != null) {
                for (VirtualMachineSimulator virtualMachineSimulator : hostMachineSimulator
                        .getVirtualMachineSimulatorList()) {
                    indexVirtualMachineSimulator = hostMachineSimulator
                            .getVirtualMachineSimulatorList().indexOf(
                                    virtualMachineSimulator);

                    if (this.cartesianChartModelStorageListList.get(
                            indexHostMachineSimulator).size() == indexVirtualMachineSimulator) {
                        this.cartesianChartModelStorageListList.get(
                                indexHostMachineSimulator).add(
                                new CartesianChartModel());
                    }

                    ChartSeries chartSeriesVirtual;

                    if (this.cartesianChartModelStorageListList
                            .get(indexHostMachineSimulator)
                            .get(indexVirtualMachineSimulator).getSeries()
                            .isEmpty()) {
                        chartSeriesVirtual = new ChartSeries();
                        for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                            chartSeriesVirtual.set(j, 0);
                        }
                    } else {
                        chartSeriesVirtual = this.cartesianChartModelStorageListList
                                .get(indexHostMachineSimulator)
                                .get(indexVirtualMachineSimulator).getSeries()
                                .get(0);
                    }

                    for (int j = 0; j < (SAMPLETOVISUALIZE - 1); j++) {
                        chartSeriesVirtual.set(j, chartSeriesVirtual.getData()
                                .get(j + 1));
                    }

                    chartSeriesVirtual.set(SAMPLETOVISUALIZE - 1,
                            virtualMachineSimulator.getCpuMhzActuallyUsed());
                    CartesianChartModel cartesianChartModelVirtual = new CartesianChartModel();
                    cartesianChartModelVirtual.setTitle("Storage (GB)");
                    cartesianChartModelVirtual.addSeries(chartSeriesVirtual);
                    this.cartesianChartModelStorageListList.get(
                            indexHostMachineSimulator).set(
                            indexVirtualMachineSimulator,
                            cartesianChartModelVirtual);

                }
            }
        }
    }

    public void stopMonitor() {
        this.started = false;
    }

    public Integer getSampletovisualize() {
        return SAMPLETOVISUALIZE;
    }

    public List<CartesianChartModel> getCartesianChartModelCPUList() {
        return this.cartesianChartModelCPUList;
    }

    public void setCartesianChartModelCPUList(
            List<CartesianChartModel> cartesianChartModelCPUList) {
        this.cartesianChartModelCPUList = cartesianChartModelCPUList;
    }

    public List<List<CartesianChartModel>> getCartesianChartModelCPUListList() {
        return this.cartesianChartModelCPUListList;
    }

    public void setCartesianChartModelCPUListList(
            List<List<CartesianChartModel>> cartesianChartModelCPUListList) {
        this.cartesianChartModelCPUListList = cartesianChartModelCPUListList;
    }

    public List<CartesianChartModel> getCartesianChartModelMemoryList() {
        return this.cartesianChartModelMemoryList;
    }

    public void setCartesianChartModelMemoryList(
            List<CartesianChartModel> cartesianChartModelMemoryList) {
        this.cartesianChartModelMemoryList = cartesianChartModelMemoryList;
    }

    public List<List<CartesianChartModel>> getCartesianChartModelMemoryListList() {
        return this.cartesianChartModelMemoryListList;
    }

    public void setCartesianChartModelMemoryListList(
            List<List<CartesianChartModel>> cartesianChartModelMemoryListList) {
        this.cartesianChartModelMemoryListList = cartesianChartModelMemoryListList;
    }

    public List<CartesianChartModel> getCartesianChartModelStorageList() {
        return this.cartesianChartModelStorageList;
    }

    public void setCartesianChartModelStorageList(
            List<CartesianChartModel> cartesianChartModelStorageList) {
        this.cartesianChartModelStorageList = cartesianChartModelStorageList;
    }

    public List<List<CartesianChartModel>> getCartesianChartModelStorageListList() {
        return this.cartesianChartModelStorageListList;
    }

    public void setCartesianChartModelStorageListList(
            List<List<CartesianChartModel>> cartesianChartModelStorageListList) {
        this.cartesianChartModelStorageListList = cartesianChartModelStorageListList;
    }

    public List<HostMachineSimulator> getHostMachineSimulatorList() {
        return this.hostMachineSimulatorList;
    }

    public void setHostMachineSimulatorList(
            List<HostMachineSimulator> hostMachineSimulatorList) {
        this.hostMachineSimulatorList = hostMachineSimulatorList;
    }

}
