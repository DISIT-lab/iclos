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

public class SLAgreement implements Serializable {

    private static final long serialVersionUID = 4679211473716588125L;
    private static final String PATHURI = "urn:cloudicaro:ServiceLevelAgreement:";
    private List<SLObjective> slObjectiveList;
    private String hasStartTime;
    private String hasEndTime;

    // Constructor----------------------------------------------------------------------------------

    public SLAgreement() {
        super();
        this.slObjectiveList = new ArrayList<SLObjective>();
        initSLAgreement();
    }

    // Init----------------------------------------------------------------------------------------

    private void initSLAgreement() {
        this.hasStartTime = "2014-01-01T00:00";
        this.hasEndTime = "2014-12-31T23:59";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getHasStartTime() {
        return this.hasStartTime;
    }

    public List<SLObjective> getSlObjectiveList() {
        return this.slObjectiveList;
    }

    public void setSlObjectiveList(List<SLObjective> slObjectiveList) {
        this.slObjectiveList = slObjectiveList;
    }

    public void setHasStartTime(final String hasStartTime) {
        this.hasStartTime = hasStartTime;
    }

    public String getHasEndTime() {
        return this.hasEndTime;
    }

    public void setHasEndTime(final String hasEndTime) {
        this.hasEndTime = hasEndTime;
    }

    public String getPathUri() {
        return PATHURI;
    }

}
