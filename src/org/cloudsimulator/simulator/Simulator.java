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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Simulator {

    private static int indexGlobalSimulator = 0;
    private int currentIndex;
    private String uriSimulatedEntity;
    private boolean started;
    private Float cpuMhz;
    private Float cpuMhzActuallyUsed;
    private Float memory;
    private Float memoryActuallyUsed;
    private Float storage;
    private Float storageActuallyUsed;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Simulator.class);

    public Simulator(String uriSimulatedEntity, Float cpuMhz, Float memory,
            Float storage) {
        super();
        this.uriSimulatedEntity = uriSimulatedEntity;
        this.cpuMhz = cpuMhz;
        this.memory = memory;
        this.storage = storage;
        this.currentIndex = indexGlobalSimulator;
        incrementIndexGlobalSimulator();
    }

    public Simulator() {
        super();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(final int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public abstract void updateWorkLoad();

    public abstract void initSubThreads();

    private static void incrementIndexGlobalSimulator() {
        indexGlobalSimulator++;
    }

    public String getUriSimulatedEntity() {
        return uriSimulatedEntity;
    }

    public void setUriSimulatedEntity(final String uriSimulatedEntity) {
        this.uriSimulatedEntity = uriSimulatedEntity;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(final boolean started) {
        this.started = started;
    }

    public Float getCpuMhz() {
        return this.cpuMhz;
    }

    public void setCpuMhz(Float cpuMhz) {
        this.cpuMhz = cpuMhz;
    }

    public Float getCpuMhzActuallyUsed() {
        return this.cpuMhzActuallyUsed;
    }

    public void setCpuMhzActuallyUsed(Float cpuMhzActuallyUsed) {
        this.cpuMhzActuallyUsed = cpuMhzActuallyUsed;
    }

    public Float getMemory() {
        return this.memory;
    }

    public void setMemory(Float memory) {
        this.memory = memory;
    }

    public Float getMemoryActuallyUsed() {
        return this.memoryActuallyUsed;
    }

    public void setMemoryActuallyUsed(Float memoryActuallyUsed) {
        this.memoryActuallyUsed = memoryActuallyUsed;
    }

    public Float getStorage() {
        return this.storage;
    }

    public void setStorage(Float storage) {
        this.storage = storage;
    }

    public Float getStorageActuallyUsed() {
        return this.storageActuallyUsed;
    }

    public void setStorageActuallyUsed(Float storageActuallyUsed) {
        this.storageActuallyUsed = storageActuallyUsed;
    }

    public Logger getLogger() {
        return LOGGER;
    }

}
