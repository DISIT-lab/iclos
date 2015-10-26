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

public class IcaroApplication implements Serializable {

    private static final long serialVersionUID = 201443228210631325L;

    private static final String PATHURI = "urn:cloudicaro:";

    private String localUri;
    private String typeRdf;
    private String hasName;
    private String hasIdentifier;
    private Float hasCapacity;
    private List<IcaroService> icaroServiceList;
    private List<SLAgreement> slAgreementList;
    private List<User> creatorList;

    // Constructor----------------------------------------------------------------------------------

    public IcaroApplication() {
        super();
        this.icaroServiceList = new ArrayList<IcaroService>();
        this.slAgreementList = new ArrayList<SLAgreement>();
        this.creatorList = new ArrayList<User>();
        initIcaroApplication();
    }

    // Init----------------------------------------------------------------------------------------

    private void initIcaroApplication() {
        this.hasName = "IcaroApplicatonTest";
        this.hasIdentifier = "IAPP";
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

    public Float getHasCapacity() {
        return this.hasCapacity;
    }

    public void setHasCapacity(final Float hasCapacity) {
        this.hasCapacity = hasCapacity;
    }

    public List<IcaroService> getIcaroServiceList() {
        return this.icaroServiceList;
    }

    public void setIcaroServiceList(final List<IcaroService> icaroServiceList) {
        this.icaroServiceList = icaroServiceList;
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

    public void setCreatorList(final List<User> creatorList) {
        this.creatorList = creatorList;
    }

    public static String getPathUri() {
        return PATHURI;
    }
}
