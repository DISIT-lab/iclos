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
import java.util.List;

public class Firewall implements Serializable {

    private static final long serialVersionUID = -606362555170881137L;
    private static final String PATHURI = "urn:cloudicaro:HostMachine:";
    private String localUri;
    private List<NetworkAdapter> hasNetworkAdapterList;
    private List<MonitorInfo> hasMonitorInfoList;
    private String hasIdentifier;
    private String hasName;
    private String hasMonitorState;
    private String hasModelName;
    private String hasMonitorIPAddress;

    // Constructor----------------------------------------------------------------------------------

    public Firewall(final String localUri,
            final List<NetworkAdapter> networkAdapterList,
            final List<MonitorInfo> hasMonitorInfoList,
            final String hasIdentifier, final String hasName,
            final String hasMonitorState, final String hasModelName) {
        super();
        this.localUri = localUri;
        this.hasNetworkAdapterList = networkAdapterList;
        this.hasMonitorInfoList = hasMonitorInfoList;
        this.hasIdentifier = hasIdentifier;
        this.hasName = hasName;
        this.hasMonitorState = hasMonitorState;
        this.hasModelName = hasModelName;
        this.hasMonitorIPAddress = this.hasNetworkAdapterList.get(0)
                .getHasIPAddress();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
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

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public void setHasIdentifier(final String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public String getHasModelName() {
        return this.hasModelName;
    }

    public void setHasModelName(final String hasModelName) {
        this.hasModelName = hasModelName;
    }

    public String getHasMonitorIPAddress() {
        return this.hasMonitorIPAddress;
    }

    public void setHasMonitorIPAddress(String hasMonitorIPAddress) {
        this.hasMonitorIPAddress = hasMonitorIPAddress;
    }

}
