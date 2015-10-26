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

public class LocalNetwork implements Serializable {

    private static final long serialVersionUID = -1804942774554263425L;
    private static final String PATHURI = "urn:cloudicaro:LocalNetwork:";
    private String hasName;
    private String hasIdentifier;
    private String localUri;
    private String hasIPAddress;
    private String hasSubNetworkMask;
    private List<String> assignedIP;

    // Constructor----------------------------------------------------------------------------------

    public LocalNetwork() {
        super();
        this.assignedIP = new ArrayList<String>();
        initLocalNetwork();
    }

    // Init----------------------------------------------------------------------------------------

    private void initLocalNetwork() {
        this.hasName = "LocalNetworkTest";
        this.hasIdentifier = "LNT";
        this.localUri = "LNet01";
        this.hasIPAddress = "192.168.0.0";
        this.hasSubNetworkMask = "255.255.255.0";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
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

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getHasIPAddress() {
        return this.hasIPAddress;
    }

    public void setHasIPAddress(String hasIPAddress) {
        this.hasIPAddress = hasIPAddress;
    }

    public String getHasSubNetworkMask() {
        return this.hasSubNetworkMask;
    }

    public void setHasSubNetworkMask(String hasSubNetworkMask) {
        this.hasSubNetworkMask = hasSubNetworkMask;
    }

    public List<String> getAssignedIP() {
        return this.assignedIP;
    }

    public void setAssignedIP(List<String> assignedIP) {
        this.assignedIP = assignedIP;
    }

}
