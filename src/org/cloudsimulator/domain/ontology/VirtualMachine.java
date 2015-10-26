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

import org.cloudsimulator.placement.TestCasePatternWrapper;

public class VirtualMachine implements Serializable, Machine {

    private static final long serialVersionUID = 8185991965903498016L;
    private static final String PATHURI = "urn:cloudicaro:VirtualMachine:";
    public static String getPathUri() {
        return PATHURI;
    }
    private String localUri;
    private String hasName;
    private String hasIdentifier;
    private Integer hasCPUCount;
    private Float hasCPUSpeedReservation;
    private Float hasCPUSpeedLimit;
    private Float hasMemoryReservation;
    private Float hasMemoryLimit;
    private Float hasMemorySize;
    private String hasExternalIPAddress;
    private String hasMonitorIPAddress;
    private List<VirtualStorage> hasVirtualStorageList;
    private List<NetworkAdapter> hasNetworkAdapterList;
    private List<MonitorInfo> hasMonitorInfoList;
    private List<IcaroService> icaroServiceList;
    private String hasOS;
    private String isPartOf;
    private String isStoredOn;
    private String isInDomain;
    private String hasAuthUserName;
    private String hasAuthUserPassword;
    private String hasMonitorState;
    
    // Constructor----------------------------------------------------------------------------------

    private TestCasePatternWrapper testCasePatternWrapper;

    public VirtualMachine() {
        this.hasVirtualStorageList = new ArrayList<VirtualStorage>();
        this.hasNetworkAdapterList = new ArrayList<NetworkAdapter>();
        this.hasMonitorInfoList = new ArrayList<MonitorInfo>();
        this.icaroServiceList = new ArrayList<IcaroService>();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public VirtualMachine(final String localUri, final String hasName,
            final String hasIdentifier, final Integer hasCPUCount,
            final Float hasCPUSpeedReservation, final Float hasCPUSpeedLimit,
            final Float hasMemorySize, final Float hasMemoryReservation,
            final Float hasMemoryLimit, final String hasExternalIPAddress,
            final List<VirtualStorage> hasVirtualStorageList,
            final List<NetworkAdapter> hasNetworkAdapterList,
            final List<MonitorInfo> hasMonitorInfoList, final String hasOS,
            final String isPartOf, final String isInDomain,
            final String isStoredOn, final String hasAuthUserName,
            final String hasAuthUserPassword, final String hasMonitorState) {
        super();
        this.localUri = localUri;
        this.hasName = hasName;
        this.hasIdentifier = hasIdentifier;
        this.hasCPUCount = hasCPUCount;
        this.hasCPUSpeedReservation = hasCPUSpeedReservation;
        this.hasCPUSpeedLimit = hasCPUSpeedLimit;
        this.hasMemorySize = hasMemorySize;
        this.hasMemoryReservation = hasMemoryReservation;
        this.hasMemoryLimit = hasMemoryLimit;
        this.hasExternalIPAddress = hasExternalIPAddress;
        this.hasMonitorIPAddress = hasNetworkAdapterList.get(0)
                .getHasIPAddress();
        this.hasVirtualStorageList = hasVirtualStorageList;
        this.hasNetworkAdapterList = hasNetworkAdapterList;
        this.hasMonitorInfoList = hasMonitorInfoList;
        this.hasOS = hasOS;
        this.isPartOf = isPartOf;
        this.isStoredOn = isStoredOn;
        this.isInDomain = isInDomain;
        this.hasAuthUserName = hasAuthUserName;
        this.hasAuthUserPassword = hasAuthUserPassword;
        this.hasMonitorState = hasMonitorState;
    }

    @Override
    public Float getCpuThroughtput() {        
        return this.getHasCPUSpeedLimit() * this.getHasCPUCount();
    }

    public String getHasAuthUserName() {
        return this.hasAuthUserName;
    }

    public String getHasAuthUserPassword() {
        return this.hasAuthUserPassword;
    }

    public Integer getHasCPUCount() {
        return this.hasCPUCount;
    }

    public Float getHasCPUSpeedLimit() {
        return hasCPUSpeedLimit;
    }

    public Float getHasCPUSpeedReservation() {
        return hasCPUSpeedReservation;
    }

    public String getHasExternalIPAddress() {
        return this.hasExternalIPAddress;
    }

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public Float getHasMemoryLimit() {
        return hasMemoryLimit;
    }

    public Float getHasMemoryReservation() {
        return hasMemoryReservation;
    }

    public Float getHasMemorySize() {
        return hasMemorySize;
    }

    public List<MonitorInfo> getHasMonitorInfoList() {
        return this.hasMonitorInfoList;
    }

    public String getHasMonitorIPAddress() {
        return this.hasMonitorIPAddress;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public String getHasName() {
        return this.hasName;
    }

    public List<NetworkAdapter> getHasNetworkAdapterList() {
        return this.hasNetworkAdapterList;
    }

    public String getHasOS() {
        return this.hasOS;
    }

    public List<VirtualStorage> getHasVirtualStorageList() {
        return this.hasVirtualStorageList;
    }

    public List<IcaroService> getIcaroServiceList() {
        return this.icaroServiceList;
    }

    public String getIsInDomain() {
        return this.isInDomain;
    }

    public String getIsPartOf() {
        return this.isPartOf;
    }

    public String getIsStoredOn() {
        return this.isStoredOn;
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public float getReservedConsumptionCPU() {
        return (float)this.hasCPUCount * this.hasCPUSpeedReservation;
    }

    public Float getStorage() {
        Float storage = 0F;
        for (final VirtualStorage virtualStorage : this.hasVirtualStorageList) {
            if (virtualStorage != null) {
                storage += virtualStorage.getHasDiskSize();
            }
        }
        return storage;
    }

    public TestCasePatternWrapper getTestCasePatternWrapper() {
        return testCasePatternWrapper;
    }

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setHasAuthUserName(String hasAuthUserName) {
        this.hasAuthUserName = hasAuthUserName;
    }

    public void setHasAuthUserPassword(String hasAuthUserPassword) {
        this.hasAuthUserPassword = hasAuthUserPassword;
    }

    public void setHasCPUCount(Integer hasCPUCount) {
        this.hasCPUCount = hasCPUCount;
    }

    public void setHasCPUSpeedLimit(Float hasCPUSpeedLimit) {
        this.hasCPUSpeedLimit = hasCPUSpeedLimit;
    }

    public void setHasCPUSpeedReservation(Float hasCPUSpeedReservation) {
        this.hasCPUSpeedReservation = hasCPUSpeedReservation;
    }

    public void setHasExternalIPAddress(String hasExternalIPAddress) {
        this.hasExternalIPAddress = hasExternalIPAddress;
    }

    public void setHasIdentifier(String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public void setHasMemoryLimit(Float hasMemoryLimit) {
        this.hasMemoryLimit = hasMemoryLimit;
    }

    public void setHasMemoryReservation(Float hasMemoryReservation) {
        this.hasMemoryReservation = hasMemoryReservation;
    }

    public void setHasMemorySize(Float hasMemorySize) {
        this.hasMemorySize = hasMemorySize;
    }

    public void setHasMonitorInfoList(List<MonitorInfo> hasMonitorInfoList) {
        this.hasMonitorInfoList = hasMonitorInfoList;
    }

    public void setHasMonitorIPAddress(String hasMonitorIPAddress) {
        this.hasMonitorIPAddress = hasMonitorIPAddress;
    }

    public void setHasMonitorState(String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public void setHasName(String hasName) {
        this.hasName = hasName;
    }

    public void setHasNetworkAdapterList(
            List<NetworkAdapter> hasNetworkAdapterList) {
        this.hasNetworkAdapterList = hasNetworkAdapterList;
    }

    public void setHasOS(String hasOS) {
        this.hasOS = hasOS;
    }

    public void setHasVirtualStorageList(
            List<VirtualStorage> hasVirtualStorageList) {
        this.hasVirtualStorageList = hasVirtualStorageList;
    }

    public void setIcaroServiceList(List<IcaroService> icaroServiceList) {
        this.icaroServiceList = icaroServiceList;
    }

    public void setIsInDomain(String isInDomain) {
        this.isInDomain = isInDomain;
    }

    public void setIsPartOf(String isPartOf) {
        this.isPartOf = isPartOf;
    }

    public void setIsStoredOn(String isStoredOn) {
        this.isStoredOn = isStoredOn;
    }
    
    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public void setTestCasePatternWrapper(TestCasePatternWrapper testCasePatternWrapper) {
        this.testCasePatternWrapper = testCasePatternWrapper;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }
    
}
