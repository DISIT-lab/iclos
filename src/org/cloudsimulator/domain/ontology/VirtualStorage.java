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

public class VirtualStorage extends LocalStorage implements Serializable {

    private static final long serialVersionUID = -5778961165824535025L;
    private static final String PATHURI = "urn:cloudicaro:VirtualStorage:";
    private String isStoredOn;

    // Constructor----------------------------------------------------------------------------------

    public VirtualStorage(String localUri, String hasName, String hasIdentifier, Float hasDiskSize, String isStoredOn) {
        super(localUri, hasName, hasIdentifier, hasDiskSize);
        this.isStoredOn = isStoredOn;
    }

    public VirtualStorage() {
        super();
        initVirtualStorage();
    }

    // Init----------------------------------------------------------------------------------------

    private void initVirtualStorage() {
        setHasName("virtStor01");
        setHasIdentifier("VS01");
        setHasDiskSize(7000F);
    }

    // GettersAndSetters----------------------------------------------------------------------------

    
    public static String getPathuri() {
        return PATHURI;
    }
    @Override
    public String getUri() {
        return PATHURI + super.getLocalUri();
    }

    @Override
    public void setUri(final String uri) {
        super.setUri(uri.replace(PATHURI, ""));
    }

    public String getIsStoredOn() {
        return this.isStoredOn;
    }

    public void setIsStoredOn(String isStoredOn) {
        this.isStoredOn = isStoredOn;
    }

}
