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

public class BusinessConfiguration implements Serializable {

    private static final long serialVersionUID = -8719814482906835399L;
    private static final String PATHURI = "urn:cloudicaro:BusinessConfiguration:";
    private String hasName;
    private String hasIdentifier;
    private String localUri;
    private String hasContractId;
    private List<User> creatorList;
    private List<IcaroApplication> icaroApplicationList;
    private List<IcaroTenant> icaroTenantList;
    private List<SLAgreement> slAgreementList;

    // Constructor----------------------------------------------------------------------------------

    public BusinessConfiguration() {
        super();
        this.creatorList = new ArrayList<User>();
        this.icaroApplicationList = new ArrayList<IcaroApplication>();
        this.icaroTenantList = new ArrayList<IcaroTenant>();
        this.slAgreementList = new ArrayList<SLAgreement>();
        initBusinessConfiguration();
    }

    // Init----------------------------------------------------------------------------------------

    private void initBusinessConfiguration() {
        this.hasName = "Business Configuration Test-007";
        this.hasIdentifier = "BC007";
        this.localUri = "TEST-007";
        this.hasContractId = "C123456789";

    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }

    public String getHasName() {
        return hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getHasIdentifier() {
        return hasIdentifier;
    }

    public void setHasIdentifier(final String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getHasContractId() {
        return this.hasContractId;
    }

    public void setHasContractId(String hasContractId) {
        this.hasContractId = hasContractId;
    }

    public List<User> getCreatorList() {
        return this.creatorList;
    }

    public void setCreatorList(List<User> creatorList) {
        this.creatorList = creatorList;
    }

    public List<IcaroApplication> getIcaroApplicationList() {
        return this.icaroApplicationList;
    }

    public void setIcaroApplicationList(
            final List<IcaroApplication> icaroApplicationList) {
        this.icaroApplicationList = icaroApplicationList;
    }

    public List<IcaroTenant> getIcaroTenantList() {
        return this.icaroTenantList;
    }

    public void setIcaroTenantList(final List<IcaroTenant> icaroTenantList) {
        this.icaroTenantList = icaroTenantList;
    }

    public List<SLAgreement> getSlAgreementList() {
        return this.slAgreementList;
    }

    public void setSlAgreementList(List<SLAgreement> slAgreementList) {
        this.slAgreementList = slAgreementList;
    }

}
