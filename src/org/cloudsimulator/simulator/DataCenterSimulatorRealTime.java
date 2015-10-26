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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.controller.DataCenterController;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.injection.InjectedConfiguration;

@Named("dataCenterSimulator")
@ConversationScoped
public class DataCenterSimulatorRealTime implements Serializable {

    private static final long serialVersionUID = 811424898285158551L;
    private Integer refreshTime;
    private Integer extendedRefreshTime;
    private Integer currentHostMachineSimulator;

    private boolean simulationStarted;
    private List<HostMachineSimulator> hostMachineSimulatorList;
    private DataCenterMetricSender dataCenterMetricSender;
    private DataCenterSimulatorMonitor dataCenterSimulatorMonitor;

    @Inject
    private DataCenterController dataCenterController;
    @Inject
    @InjectedConfiguration(key = "icaroKB.addressIP", defaultValue = "localhost:8080")
    private String ipOfKB;

    @PostConstruct
    public void init() {
        this.refreshTime = 5;
        this.extendedRefreshTime = 1200;

        initHostMachineSimulatorList();

    }

    public void initHostMachineSimulatorList() {
        hostMachineSimulatorList = new ArrayList<HostMachineSimulator>();
        for (HostMachine hostMachine : this.dataCenterController
                .getDataCenter().getHostMachineList()) {
            hostMachineSimulatorList.add(new HostMachineSimulator(hostMachine));
        }

        this.dataCenterMetricSender = new DataCenterMetricSender(this);
        this.dataCenterSimulatorMonitor = new DataCenterSimulatorMonitor(
                hostMachineSimulatorList);
    }

    public void startSimulation() {
        Thread hostMachineSimulatorThread;

        for (HostMachineSimulator hostMachineSimulator : this.hostMachineSimulatorList) {
            hostMachineSimulatorThread = new Thread(hostMachineSimulator,
                    "hostMachineSimulator#"
                            + this.hostMachineSimulatorList
                                    .indexOf(hostMachineSimulator));
            hostMachineSimulatorThread.start();

        }

        startMonitor();

        this.simulationStarted = true;
    }

    public void stopSimulation() {
        for (HostMachineSimulator hostMachineSimulator : this.hostMachineSimulatorList) {
            hostMachineSimulator.stopHostMachineSimulator();
        }
        this.simulationStarted = false;
    }

    public void startSendToKB() {
        Thread metricSenderThread;
        metricSenderThread = new Thread(this.dataCenterMetricSender,
                "MetricSender");
        metricSenderThread.start();
    }

    public void stopSendToKB() {
        this.dataCenterMetricSender.stopSender();
    }

    public void startMonitor() {
        Thread simulatorMonitorThread;
        simulatorMonitorThread = new Thread(this.dataCenterSimulatorMonitor,
                "Monitor");
        simulatorMonitorThread.start();
    }

    public void stopmonitor() {
        this.dataCenterSimulatorMonitor.stopMonitor();
    }

    public String viewVirtualMachineChart(
            final Integer indexHostMachineSimulator) {
        this.currentHostMachineSimulator = indexHostMachineSimulator;
        return "dataCenterSimulationRealTimeVirtualMachine.xhtml";
    }

    public void extendRestoreRefreshTime() {
        final Integer tempRefreshTime = this.refreshTime;
        this.refreshTime = this.extendedRefreshTime;
        this.extendedRefreshTime = tempRefreshTime;
    }

    public Integer getRefreshTime() {
        return this.refreshTime;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Integer getCurrentHostMachineSimulator() {
        return this.currentHostMachineSimulator;
    }

    public void setCurrentHostMachineSimulator(
            Integer currentHostMachineSimulator) {
        this.currentHostMachineSimulator = currentHostMachineSimulator;
    }

    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    public List<HostMachineSimulator> getHostMachineSimulatorList() {
        return this.hostMachineSimulatorList;
    }

    public void setHostMachineSimulatorList(
            List<HostMachineSimulator> hostMachineSimulatorList) {
        this.hostMachineSimulatorList = hostMachineSimulatorList;
    }

    public DataCenterMetricSender getDataCenterMetricSender() {
        return dataCenterMetricSender;
    }

    public DataCenterSimulatorMonitor getDataCenterSimulatorMonitor() {
        return this.dataCenterSimulatorMonitor;
    }

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

}
