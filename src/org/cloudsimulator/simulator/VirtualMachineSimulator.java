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

import org.cloudsimulator.domain.ontology.IcaroService;
import org.cloudsimulator.domain.ontology.VirtualMachine;

public class VirtualMachineSimulator extends Simulator implements Runnable,
        Serializable {

    private static final long serialVersionUID = -5073623327560881498L;
    private List<IcaroServiceSimulator> icaroServiceSimulatorList;

    public VirtualMachineSimulator(final VirtualMachine virtualMachine) {
        super(virtualMachine.getUri(), virtualMachine.getHasCPUCount()
                * virtualMachine.getHasCPUSpeedLimit(), virtualMachine
                .getHasMemorySize(), virtualMachine.getStorage());

        if (!virtualMachine.getIcaroServiceList().isEmpty()) {
            icaroServiceSimulatorList = new ArrayList<IcaroServiceSimulator>();
            for (IcaroService icaroService : virtualMachine
                    .getIcaroServiceList()) {
                icaroServiceSimulatorList.add(new IcaroServiceSimulator(
                        icaroService));
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                super.getLogger().error(e.getMessage(), e);
            }

            updateWorkLoad();

        }

        super.getLogger().info("Stop : " + this.getUriSimulatedEntity());
        return;
    }

    public void stopVirtualMachineSimulator() {
        if (this.icaroServiceSimulatorList != null) {
            for (IcaroServiceSimulator icaroServiceSimulator : this.icaroServiceSimulatorList) {
                icaroServiceSimulator.stopIcaroServiceSimulator();
            }
        }

        this.setStarted(false);
    }

    @Override
    public void updateWorkLoad() {

        setCpuMhzActuallyUsed(0f);
        setMemoryActuallyUsed(0f);
        setStorageActuallyUsed(0f);

        if (this.icaroServiceSimulatorList != null) {
            for (IcaroServiceSimulator icaroServiceSimulator : this.icaroServiceSimulatorList) {
                if (icaroServiceSimulator != null) {
                    setCpuMhzActuallyUsed(getCpuMhzActuallyUsed()
                            + icaroServiceSimulator.getCpuMhzActuallyUsed()
                            * getCpuMhz());
                    setMemoryActuallyUsed(getMemoryActuallyUsed()
                            + icaroServiceSimulator.getMemoryActuallyUsed()
                            * getMemory());
                    setStorageActuallyUsed(getStorageActuallyUsed()
                            + icaroServiceSimulator.getStorageActuallyUsed()
                            * getStorage());
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
        Thread icaroServiceSimulatorThread;

        if (this.icaroServiceSimulatorList != null) {
            for (IcaroServiceSimulator icaroServiceSimulator : this.icaroServiceSimulatorList) {
                icaroServiceSimulatorThread = new Thread(icaroServiceSimulator,
                        "icaroServiceSimulator#"
                                + this.icaroServiceSimulatorList
                                        .indexOf(icaroServiceSimulator));
                icaroServiceSimulatorThread.start();
            }
        }
    }

    public List<IcaroServiceSimulator> getIcaroServiceSimulatorList() {
        return this.icaroServiceSimulatorList;
    }

}
