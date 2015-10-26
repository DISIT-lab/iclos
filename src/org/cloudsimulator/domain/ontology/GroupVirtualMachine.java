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

public class GroupVirtualMachine implements Serializable {

    private static final long serialVersionUID = 1104382418986417959L;
    private Integer numberVirtual;
    private String hasPrefixName;
    private String hasPrefixIdentifier;
    private Integer hasCPUCount;
    private Float hasCPUSpeedReservation;
    private Float hasCPUSpeedLimit;
    private Float hasMemoryReservation;
    private Float hasMemoryLimit;
    private Float hasMemorySize;
    private String hasExternalIPAddress;
    private List<VirtualStorage> virtualStorageList;
    private List<LocalNetwork> localNetworkList;
    private List<MonitorInfo> monitorInfoList;
    private String hasOS;
    private String isInDomain;
    private String isStoredOn;
    private String hasAuthUserName;
    private String hasAuthUserPassword;
    private String hasMonitorState;
    private String arePartOf;

    // Constructor----------------------------------------------------------------------------------

    public GroupVirtualMachine() {
        super();
        this.localNetworkList = new ArrayList<LocalNetwork>();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        this.virtualStorageList = new ArrayList<VirtualStorage>();
        initVirtualMachine();
    }

    // Init----------------------------------------------------------------------------------------

    private void initVirtualMachine() {
        this.numberVirtual = 5;
        this.hasOS = "ubuntu";
        this.hasCPUCount = 1;
        this.hasCPUSpeedReservation = 800F;
        this.hasCPUSpeedLimit = 2000F;
        this.hasMemoryReservation = 1F;
        this.hasMemoryLimit = 2F;
        this.hasPrefixIdentifier = "VM";
        this.hasMemorySize = 6F;
        this.hasExternalIPAddress = "25.26.78.25";
        this.hasPrefixName = "VirtMach";
        this.hasAuthUserName = "root";
        this.hasAuthUserPassword = "xxxx";
        this.hasMonitorState = "enabled";
        this.isInDomain = "DC01";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public Integer getNumberVirtual() {
        return this.numberVirtual;
    }

    public void setNumberVirtual(final Integer numberVirtual) {
        this.numberVirtual = numberVirtual;
    }

    public String getHasPrefixName() {
        return this.hasPrefixName;
    }

    public void setHasPrefixName(final String hasPrefixName) {
        this.hasPrefixName = hasPrefixName;
    }

    public String getHasPrefixIdentifier() {
        return this.hasPrefixIdentifier;
    }

    public void setHasPrefixIdentifier(final String hasPrefixIdentifier) {
        this.hasPrefixIdentifier = hasPrefixIdentifier;
    }

    public Integer getHasCPUCount() {
        return this.hasCPUCount;
    }

    public void setHasCPUCount(Integer hasCPUCount) {
        this.hasCPUCount = hasCPUCount;
    }

    public Float getHasCPUSpeedReservation() {
        return hasCPUSpeedReservation;
    }

    public void setHasCPUSpeedReservation(Float hasCPUSpeedReservation) {
        this.hasCPUSpeedReservation = hasCPUSpeedReservation;
    }

    public Float getHasCPUSpeedLimit() {
        return hasCPUSpeedLimit;
    }

    public void setHasCPUSpeedLimit(Float hasCPUSpeedLimit) {
        this.hasCPUSpeedLimit = hasCPUSpeedLimit;
    }

    public Float getHasMemoryReservation() {
        return hasMemoryReservation;
    }

    public void setHasMemoryReservation(Float hasMemoryReservation) {
        this.hasMemoryReservation = hasMemoryReservation;
    }

    public Float getHasMemoryLimit() {
        return hasMemoryLimit;
    }

    public void setHasMemoryLimit(Float hasMemoryLimit) {
        this.hasMemoryLimit = hasMemoryLimit;
    }

    public Float getHasMemorySize() {
        return hasMemorySize;
    }

    public void setHasMemorySize(Float hasMemorySize) {
        this.hasMemorySize = hasMemorySize;
    }

    public String getHasExternalIPAddress() {
        return this.hasExternalIPAddress;
    }

    public void setHasExternalIPAddress(String hasExternalIPAddress) {
        this.hasExternalIPAddress = hasExternalIPAddress;
    }

    public List<LocalNetwork> getLocalNetworkList() {
        return this.localNetworkList;
    }

    public void setLocalNetworkList(final List<LocalNetwork> localNetworkList) {
        this.localNetworkList = localNetworkList;
    }

    public List<VirtualStorage> getVirtualStorageList() {
        return this.virtualStorageList;
    }

    public void setVirtualStorageList(List<VirtualStorage> virtualStorageList) {
        this.virtualStorageList = virtualStorageList;
    }

    public List<MonitorInfo> getMonitorInfoList() {
        return this.monitorInfoList;
    }

    public void setMonitorInfoList(List<MonitorInfo> monitorInfoList) {
        this.monitorInfoList = monitorInfoList;
    }

    public String getHasOS() {
        return this.hasOS;
    }

    public void setHasOS(final String hasOS) {
        this.hasOS = hasOS;
    }

    public String getIsInDomain() {
        return this.isInDomain;
    }

    public void setIsInDomain(final String isInDomain) {
        this.isInDomain = isInDomain;
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

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public String getArePartOf() {
        return this.arePartOf;
    }

    public void setArePartOf(final String arePartOf) {
        this.arePartOf = arePartOf;
    }

    public String getIsStoredOn() {
        return this.isStoredOn;
    }

    public void setIsStoredOn(String isStoredOn) {
        this.isStoredOn = isStoredOn;
    }

}
