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

public class User implements Serializable {

    private static final long serialVersionUID = -8018635186893200465L;
    private static final String PATHURI = "urn:cloudicaro:User:";
    private String localUri;
    private String nameFoaf;
    private String mboxFoaf;

    // Constructor----------------------------------------------------------------------------------

    public User() {
        super();
        initUser();
    }

    // Init----------------------------------------------------------------------------------------

    private void initUser() {
        this.localUri = "admin";
        this.nameFoaf = "Administrator";
        this.mboxFoaf = "admin@icaro.it";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getNameFoaf() {
        return this.nameFoaf;
    }

    public void setNameFoaf(final String nameFoaf) {
        this.nameFoaf = nameFoaf;
    }

    public String getMboxFoaf() {
        return this.mboxFoaf;
    }

    public void setMboxFoaf(final String mboxFoaf) {
        this.mboxFoaf = mboxFoaf;
    }

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }
}
