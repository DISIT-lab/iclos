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

import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;

public class HostMachineSimulator extends Simulator implements Runnable,
        Serializable {

    private static final long serialVersionUID = 4141434183633063152L;
    private List<VirtualMachineSimulator> virtualMachineSimulatorList;

    public HostMachineSimulator(final HostMachine hostMachine) {
        super(hostMachine.getUri(), hostMachine.getHasCPUCount()
                * hostMachine.getHasCPUSpeed(), hostMachine.getHasMemorySize(),
                hostMachine.getStorage());

        if (!hostMachine.getVirtualMachineList().isEmpty()) {
            virtualMachineSimulatorList = new ArrayList<VirtualMachineSimulator>();
            for (VirtualMachine virtualMachine : hostMachine
                    .getVirtualMachineList()) {
                virtualMachineSimulatorList.add(new VirtualMachineSimulator(
                        virtualMachine));
            }
        }
    }

    @Override
    public void run() {
        super.getLogger().info("Start: " + this.getUriSimulatedEntity());
        this.setStarted(true);

        initSubThreads();

        while (this.isStarted()) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                super.getLogger().error(e.getMessage(), e);
            }
            updateWorkLoad();

        }

        super.getLogger().info("Stop : " + this.getUriSimulatedEntity());
        return;
    }

    public void stopHostMachineSimulator() {
        if (this.virtualMachineSimulatorList != null) {
            for (VirtualMachineSimulator virtualMachineSimulator : this.virtualMachineSimulatorList) {
                virtualMachineSimulator.stopVirtualMachineSimulator();
            }
        }
        this.setStarted(false);
    }

    @Override
    public void updateWorkLoad() {

        setCpuMhzActuallyUsed(0f);
        setMemoryActuallyUsed(0f);
        setStorageActuallyUsed(0f);

        if (this.virtualMachineSimulatorList != null) {
            for (VirtualMachineSimulator virtualMachineSimulator : this.virtualMachineSimulatorList) {
                if (virtualMachineSimulator != null) {
                    setCpuMhzActuallyUsed(getCpuMhzActuallyUsed()
                            + virtualMachineSimulator.getCpuMhzActuallyUsed());
                    setMemoryActuallyUsed(getMemoryActuallyUsed()
                            + virtualMachineSimulator.getMemoryActuallyUsed());
                    setStorageActuallyUsed(getStorageActuallyUsed()
                            + virtualMachineSimulator.getStorageActuallyUsed());
                }
            }
        } else {
            setCpuMhzActuallyUsed(getCpuMhz() * 0.10f);
            setMemoryActuallyUsed(getMemory() * 0.10f);
            setStorageActuallyUsed(getStorage() * 0.10f);
        }

    }

    @Override
    public void initSubThreads() {
        Thread virtualMachineSimulatorThread;

        if (this.virtualMachineSimulatorList != null) {
            for (VirtualMachineSimulator virtualMachineSimulator : this.virtualMachineSimulatorList) {
                virtualMachineSimulatorThread = new Thread(
                        virtualMachineSimulator,
                        "virtualMachineSimulator#"
                                + this.virtualMachineSimulatorList
                                        .indexOf(virtualMachineSimulator));
                virtualMachineSimulatorThread.start();
            }
        }
    }

    public List<VirtualMachineSimulator> getVirtualMachineSimulatorList() {
        return this.virtualMachineSimulatorList;
    }

}
