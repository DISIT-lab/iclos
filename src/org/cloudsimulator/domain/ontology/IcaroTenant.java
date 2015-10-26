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

public class IcaroTenant implements Serializable {

    private static final long serialVersionUID = 6230376874500531195L;

    private static final String PATHURI = "urn:cloudicaro:";

    private String localUri;
    private String hasName;
    private String hasIdentifier;
    private String hasMonitorState;
    private String isTenantOf;
    private List<MonitorInfo> monitorInfoList;
    private List<SLAgreement> slAgreementList;
    private List<User> creatorList;

    // Constructor----------------------------------------------------------------------------------

    public IcaroTenant() {
        super();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        this.slAgreementList = new ArrayList<SLAgreement>();
        this.creatorList = new ArrayList<User>();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(String hasName) {
        this.hasName = hasName;
    }

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public void setHasIdentifier(String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public String getIsTenantOf() {
        return this.isTenantOf;
    }

    public void setIsTenantOf(String isTenantOf) {
        this.isTenantOf = isTenantOf;
    }

    public List<MonitorInfo> getMonitorInfoList() {
        return this.monitorInfoList;
    }

    public void setMonitorInfoList(List<MonitorInfo> monitorInfoList) {
        this.monitorInfoList = monitorInfoList;
    }

    public List<SLAgreement> getSlAgreementList() {
        return this.slAgreementList;
    }

    public void setSlAgreementList(List<SLAgreement> slAgreementList) {
        this.slAgreementList = slAgreementList;
    }

    public List<User> getCreatorList() {
        return this.creatorList;
    }

    public void setCreatorList(List<User> creatorList) {
        this.creatorList = creatorList;
    }

    public static String getPathUri() {
        return PATHURI;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

}
