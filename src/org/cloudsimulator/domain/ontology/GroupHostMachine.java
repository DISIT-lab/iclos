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

public class GroupHostMachine implements Serializable {

    private static final long serialVersionUID = -4413943818067825073L;
    private Integer numberHost;
    private String hasOS;
    private List<LocalNetwork> localNetworkList;
    private List<MonitorInfo> monitorInfoList;
    private List<LocalStorage> localStorageList;
    private List<String> sharedStorageList;
    private Integer hasCPUCount;
    private Float hasCPUSpeed;
    private String hasCPUType;
    private String hasPrefixIdentifier;
    private Float hasMemorySize;
    private String hasPrefixName;
    private String hasAuthUserName;
    private String hasAuthUserPassword;
    private Float hasCapacity;
    private String hasMonitorState;
    private String isInDomain;

    // Constructor----------------------------------------------------------------------------------

    public GroupHostMachine() {
        super();
        this.localNetworkList = new ArrayList<LocalNetwork>();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        this.localStorageList = new ArrayList<LocalStorage>();
        this.sharedStorageList = new ArrayList<String>();
        initGroupHost();
    }

    // Init----------------------------------------------------------------------------------------

    private void initGroupHost() {
        this.numberHost = 10;
        this.hasOS = "redhat";
        this.hasCPUCount = 32;
        this.hasCPUSpeed = 2500F;
        this.hasCPUType = "Intel Xeon";
        this.hasPrefixIdentifier = "HSTT";
        this.hasMemorySize = 128F;
        this.hasPrefixName = "hostTest";
        this.hasAuthUserName = "root";
        this.hasAuthUserPassword = "xxxx";
        this.hasCapacity = 100F;
        this.hasMonitorState = "enabled";
        this.isInDomain = "DC01";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public Integer getNumberHost() {
        return this.numberHost;
    }

    public void setNumberHost(final Integer numberHost) {
        this.numberHost = numberHost;
    }

    public String getHasOS() {
        return this.hasOS;
    }

    public void setHasOS(final String hasOS) {
        this.hasOS = hasOS;
    }

    public List<LocalNetwork> getLocalNetworkList() {
        return this.localNetworkList;
    }

    public void setLocalNetworkList(final List<LocalNetwork> localNetworkList) {
        this.localNetworkList = localNetworkList;
    }

    public List<MonitorInfo> getMonitorInfoList() {
        return this.monitorInfoList;
    }

    public void setMonitorInfoList(List<MonitorInfo> monitorInfoList) {
        this.monitorInfoList = monitorInfoList;
    }

    public List<LocalStorage> getLocalStorageList() {
        return this.localStorageList;
    }

    public void setLocalStorageList(List<LocalStorage> localStorageList) {
        this.localStorageList = localStorageList;
    }

    public List<String> getSharedStorageList() {
        return this.sharedStorageList;
    }

    public void setSharedStorageList(List<String> sharedStorageList) {
        this.sharedStorageList = sharedStorageList;
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

    public void setHasCPUSpeed(Float hasCPUSpeed) {
        this.hasCPUSpeed = hasCPUSpeed;
    }

    public String getHasCPUType() {
        return this.hasCPUType;
    }

    public void setHasCPUType(String hasCPUType) {
        this.hasCPUType = hasCPUType;
    }

    public String getHasPrefixIdentifier() {
        return this.hasPrefixIdentifier;
    }

    public void setHasPrefixIdentifier(final String hasPrefixIdentifier) {
        this.hasPrefixIdentifier = hasPrefixIdentifier;
    }

    public Float getHasMemorySize() {
        return this.hasMemorySize;
    }

    public void setHasMemorySize(final Float hasMemorySize) {
        this.hasMemorySize = hasMemorySize;
    }

    public String getHasPrefixName() {
        return this.hasPrefixName;
    }

    public void setHasPrefixName(final String hasPrefixName) {
        this.hasPrefixName = hasPrefixName;
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

}
