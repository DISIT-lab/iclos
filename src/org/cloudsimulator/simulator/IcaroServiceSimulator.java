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
import java.util.Random;

import org.cloudsimulator.domain.ontology.IcaroService;
import org.uncommons.maths.random.GaussianGenerator;

public class IcaroServiceSimulator extends Simulator implements Runnable,
        Serializable {

    private static final long serialVersionUID = 5109460453490016479L;
    private transient GaussianGenerator gaussianGeneratorCPU;
    private transient GaussianGenerator gaussianGeneratorMemory;
    private transient GaussianGenerator gaussianGeneratorStorage;
    private Random random = new Random();

    public IcaroServiceSimulator(final IcaroService icaroService) {
        super(icaroService.getUri(), 0F, 0F, 0F);
        this.gaussianGeneratorCPU = new GaussianGenerator(10, 3, random);
        this.gaussianGeneratorMemory = new GaussianGenerator(10, 3, random);
        this.gaussianGeneratorStorage = new GaussianGenerator(10, 3, random);
    }

    @Override
    public void run() {
        super.getLogger().info("Start: " + this.getUriSimulatedEntity());
        this.setStarted(true);

        while (this.isStarted()) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                super.getLogger().error(e.getMessage(), e);
            }

            updateWorkLoad();

        }

        super.getLogger().info("Stop : " + this.getUriSimulatedEntity());
        return;
    }

    public void stopIcaroServiceSimulator() {
        this.setStarted(false);
    }

    @Override
    public void updateWorkLoad() {

        setCpuMhzActuallyUsed(0f);
        setMemoryActuallyUsed(0f);
        setStorageActuallyUsed(0f);

        setCpuMhzActuallyUsed(getCpuMhzActuallyUsed()
                + gaussianGeneratorCPU.nextValue().floatValue() / 100);
        setMemoryActuallyUsed(getMemoryActuallyUsed()
                + gaussianGeneratorMemory.nextValue().floatValue() / 100);
        setStorageActuallyUsed(getStorageActuallyUsed()
                + gaussianGeneratorStorage.nextValue().floatValue() / 100);

    }

    @Override
    public void initSubThreads() {
        // Not called
    }

}