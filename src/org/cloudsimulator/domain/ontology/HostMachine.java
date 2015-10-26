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

package org.cloudsimulator.domain.ontology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HostMachine implements Serializable, Machine {

    private static final long serialVersionUID = -3628820129061361024L;
    private static final String PATHURI = "urn:cloudicaro:HostMachine:";
    private String localUri;
    private String hasOS;
    private List<VirtualMachine> virtualMachineList;
    private List<NetworkAdapter> hasNetworkAdapterList;
    private List<MonitorInfo> hasMonitorInfoList;
    private List<LocalStorage> hasLocalStorageList;
    private List<String> useSharedStorageList;
    private Integer hasCPUCount;
    private Float hasCPUSpeed;
    private String hasCPUType;
    private String hasIdentifier;
    private Float hasMemorySize;
    private String hasName;
    private String hasAuthUserName;
    private String hasAuthUserPassword;
    private Float hasCapacity;
    private String hasMonitorIPAddress;
    private String hasMonitorState;
    private String isInDomain;
    private float hasResidualCPU;
    private float hasResidualMemory;

    // Constructor----------------------------------------------------------------------------------

    public HostMachine(final String localUri, final String hasOS,
            final List<NetworkAdapter> hasNetworkAdapterList,
            final List<MonitorInfo> hasMonitorInfoList,
            final List<LocalStorage> hasLocalStorageList,
            final List<String> useSharedStorageList, final Integer hasCPUCount,
            final Float hasCPUSpeed, final String hasCPUType,
            final String hasIdentifier, final Float hasMemorySize,
            final String hasName, final String hasAuthUserName,
            final String hasAuthUserPassword, final Float hasCapacity,
            final String hasMonitorState, final String isInDomain) {
        super();
        this.localUri = localUri;
        this.hasOS = hasOS;
        this.hasNetworkAdapterList = hasNetworkAdapterList;
        this.hasMonitorInfoList = hasMonitorInfoList;
        this.hasLocalStorageList = hasLocalStorageList;
        this.useSharedStorageList = useSharedStorageList;
        this.hasCPUCount = hasCPUCount;
        this.hasCPUSpeed = hasCPUSpeed;
        this.hasCPUType = hasCPUType;
        this.hasIdentifier = hasIdentifier;
        this.hasMemorySize = hasMemorySize;
        this.hasName = hasName;
        this.hasAuthUserName = hasAuthUserName;
        this.hasAuthUserPassword = hasAuthUserPassword;
        this.hasCapacity = hasCapacity;
        this.hasMonitorIPAddress = hasNetworkAdapterList.get(0)
                .getHasIPAddress();
        this.hasMonitorState = hasMonitorState;
        this.isInDomain = isInDomain;
        this.virtualMachineList = new ArrayList<VirtualMachine>();
    }

    public HostMachine() {
        super();
        this.virtualMachineList = new ArrayList<VirtualMachine>();
        this.hasNetworkAdapterList = new ArrayList<NetworkAdapter>();
        this.hasMonitorInfoList = new ArrayList<MonitorInfo>();
        this.hasLocalStorageList = new ArrayList<LocalStorage>();
        this.useSharedStorageList = new ArrayList<String>();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getHasOS() {
        return this.hasOS;
    }

    public void setHasOS(final String hasOS) {
        this.hasOS = hasOS;
    }

    public List<VirtualMachine> getVirtualMachineList() {
        return this.virtualMachineList;
    }

    public void setVirtualMachineList(
            final List<VirtualMachine> virtualMachineList) {
        this.virtualMachineList = virtualMachineList;
    }

    public List<NetworkAdapter> getHasNetworkAdapterList() {
        return this.hasNetworkAdapterList;
    }

    public void setHasNetworkAdapterList(
            final List<NetworkAdapter> hasNetworkAdapterList) {
        this.hasNetworkAdapterList = hasNetworkAdapterList;
    }

    public List<MonitorInfo> getHasMonitorInfoList() {
        return this.hasMonitorInfoList;
    }

    public void setHasMonitorInfoList(final List<MonitorInfo> hasMonitorInfoList) {
        this.hasMonitorInfoList = hasMonitorInfoList;
    }

    public List<LocalStorage> getHasLocalStorageList() {
        return this.hasLocalStorageList;
    }

    public void setHasLocalStorageList(List<LocalStorage> hasLocalStorageList) {
        this.hasLocalStorageList = hasLocalStorageList;
    }

    public List<String> getUseSharedStorageList() {
        return this.useSharedStorageList;
    }

    public void setUseSharedStorageList(List<String> useSharedStorageList) {
        this.useSharedStorageList = useSharedStorageList;
    }

    public Integer getHasCPUCount() {
        return this.hasCPUCount;
    }

    public void setHasCPUCount(final Integer hasCPUCount) {
        this.hasCPUCount = hasCPUCount;
    }

    public Float getHasCPUSpeed() {
        return this.hasCPUSpeed;
    }

    public void setHasCPUSpeed(final Float hasCPUSpeed) {
        this.hasCPUSpeed = hasCPUSpeed;
    }

    public String getHasCPUType() {
        return this.hasCPUType;
    }

    public void setHasCPUType(final String hasCPUType) {
        this.hasCPUType = hasCPUType;
    }

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public void setHasIdentifier(final String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    @Override
    public Float getHasMemorySize() {
        return this.hasMemorySize;
    }

    public void setHasMemorySize(final Float hasMemorySize) {
        this.hasMemorySize = hasMemorySize;
    }

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getHasAuthUserName() {
        return this.hasAuthUserName;
    }

    public void setHasAuthUserName(final String hasAuthUserName) {
        this.hasAuthUserName = hasAuthUserName;
    }

    public String getHasAuthUserPassword() {
        return this.hasAuthUserPassword;
    }

    public void setHasAuthUserPassword(final String hasAuthUserPassword) {
        this.hasAuthUserPassword = hasAuthUserPassword;
    }

    public Float getHasCapacity() {
        return this.hasCapacity;
    }

    public void setHasCapacity(final Float hasCapacity) {
        this.hasCapacity = hasCapacity;
    }

    public String getHasMonitorIPAddress() {
        return this.hasMonitorIPAddress;
    }

    public void setHasMonitorIPAddress(String hasMonitorIPAddress) {
        this.hasMonitorIPAddress = hasMonitorIPAddress;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public String getIsInDomain() {
        return this.isInDomain;
    }

    public void setIsInDomain(final String isInDomain) {
        this.isInDomain = isInDomain;
    }

    public Float getStorage() {
        Float storage = 0F;
        for (final LocalStorage localStorage : this.hasLocalStorageList) {
            if (localStorage != null) {
                storage += localStorage.getHasDiskSize();
            }
        }
        return storage;
    }

    public void initResidualCapacity(float reservedOsHostResource) {
        this.setHasResidualCPU(((float) this.hasCPUCount * this.hasCPUSpeed)*(1-reservedOsHostResource));
        this.setHasResidualMemory((float) this.hasMemorySize * (1-reservedOsHostResource));        
    }

    public float getHasResidualCPU() {
        return hasResidualCPU;
    }

    public void setHasResidualCPU(float hasResidualCPU) {
        this.hasResidualCPU = hasResidualCPU;
    }

    public float getHasResidualMemory() {
        return hasResidualMemory;
    }

    public void setHasResidualMemory(float hasResidualMemory) {
        this.hasResidualMemory = hasResidualMemory;
    }

    @Override
    public Float getCpuThroughtput() {
        
        return this.hasCPUSpeed * this.hasCPUCount;
    }

}
