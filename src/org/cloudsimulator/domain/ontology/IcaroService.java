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

public class IcaroService implements Serializable {

    private static final long serialVersionUID = -2599033459660131563L;
    private static final String PATHURI = "urn:cloudicaro:";
    private String localUri;
    private String typeRdf;
    private String hasName;
    private String hasIdentifier;
    private String runsOnVm;
    private String supportsLanguage;
    private String hasMonitorIPAddress;
    private String hasProcessName;
    private String hasAuthUserName;
    private String hasAuthUserPassword;
    private List<Integer> usesTcpPortList;
    private List<Integer> usesUdpPortList;
    private List<MonitorInfo> monitorInfoList;
    private String hasMonitorState;

    // Constructor----------------------------------------------------------------------------------

    public IcaroService() {
        super();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        this.usesTcpPortList = new ArrayList<Integer>();
        this.usesUdpPortList = new ArrayList<Integer>();
        initIcaroService();
    }

    // Init----------------------------------------------------------------------------------------

    private void initIcaroService() {
        this.hasName = "IcaroServiceTest";
        this.hasIdentifier = "ICT";
        this.supportsLanguage = "java";
        this.hasMonitorIPAddress = "192.168.0.254";
        this.hasProcessName = "ServiceTest";
        this.hasAuthUserName = "root";
        this.hasAuthUserPassword = "xxxx";
        this.hasMonitorState = "enabled";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getUri() {
        return PATHURI + this.typeRdf + this.localUri;
    }

    public void setUri(final String uri) {
        String typeLocalUri = uri.replace(PATHURI, "");
        this.typeRdf = typeLocalUri.substring(0, typeLocalUri.indexOf(':'));
        this.localUri = typeLocalUri.substring(typeLocalUri.indexOf(':') + 1);
    }

    public String getTypeRdf() {
        return this.typeRdf;
    }

    public void setTypeRdf(final String typeRdf) {
        this.typeRdf = typeRdf;
    }

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public void setHasIdentifier(final String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public String getRunsOnVm() {
        return this.runsOnVm;
    }

    public void setRunsOnVm(final String runsOnVm) {
        this.runsOnVm = runsOnVm;
    }

    public String getSupportsLanguage() {
        return this.supportsLanguage;
    }

    public void setSupportsLanguage(final String supportsLanguage) {
        this.supportsLanguage = supportsLanguage;
    }

    public String getHasMonitorIPAddress() {
        return this.hasMonitorIPAddress;
    }

    public void setHasMonitorIPAddress(final String hasMonitorIPAddress) {
        this.hasMonitorIPAddress = hasMonitorIPAddress;
    }

    public String getHasProcessName() {
        return this.hasProcessName;
    }

    public void setHasProcessName(final String hasProcessName) {
        this.hasProcessName = hasProcessName;
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

    public List<Integer> getUsesTcpPortList() {
        return this.usesTcpPortList;
    }

    public void setUsesTcpPortList(final List<Integer> usesTcpPortList) {
        this.usesTcpPortList = usesTcpPortList;
    }

    public List<Integer> getUsesUdpPortList() {
        return this.usesUdpPortList;
    }

    public void setUsesUdpPortList(final List<Integer> usesUdpPortList) {
        this.usesUdpPortList = usesUdpPortList;
    }

    public List<MonitorInfo> getMonitorInfoList() {
        return this.monitorInfoList;
    }

    public void setMonitorInfoList(List<MonitorInfo> monitorInfoList) {
        this.monitorInfoList = monitorInfoList;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public static String getPathUri() {
        return PATHURI;
    }

}
