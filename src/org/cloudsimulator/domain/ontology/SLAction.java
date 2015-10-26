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

public class SLAction implements Serializable {

    private static final long serialVersionUID = -1580916860096740486L;
    private String hasName;
    private String callUri;

    // Constructor----------------------------------------------------------------------------------

    public SLAction() {
        super();
        initSLAction();
    }

    // Init----------------------------------------------------------------------------------------

    private void initSLAction() {
        this.hasName = "Send an Email";
        this.callUri = "http://www.example.com";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getCallUri() {
        return this.callUri;
    }

    public void setCallUri(String callUri) {
        this.callUri = callUri;
    }

}
