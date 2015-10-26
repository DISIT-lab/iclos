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

public class SharedStorageVolume implements Serializable {

    private static final long serialVersionUID = -5778961165824535025L;
    private static final String PATHURI = "urn:cloudicaro:SharedStorageVolume:";
    private String localUri;
    private String hasName;
    private String hasIdentifier;
    private Integer hasDiskSize;

    // Constructor----------------------------------------------------------------------------------

    public SharedStorageVolume() {
        super();
        initSharedStorage();
    }

    public SharedStorageVolume(final String localUri, final String hasName,
            String hasIdentifier, Integer hasDiskSize) {
        super();
        this.localUri = localUri;
        this.hasName = hasName;
        this.hasIdentifier = hasIdentifier;
        this.hasDiskSize = hasDiskSize;
    }

    // Init----------------------------------------------------------------------------------------

    private void initSharedStorage() {
        this.localUri = "SharStorage01";
        this.hasName = "sharedSto01";
        this.hasIdentifier = "SS01";
        this.hasDiskSize = 5000;
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public static String getPathuri() {
        return PATHURI;
    }

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

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

    public Integer getHasDiskSize() {
        return this.hasDiskSize;
    }

    public void setHasDiskSize(Integer hasDiskSize) {
        this.hasDiskSize = hasDiskSize;
    }

}