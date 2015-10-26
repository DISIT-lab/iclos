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

public class SLObjective implements Serializable {

    private static final long serialVersionUID = 7088602095262415485L;
    private List<SLAction> slActionList;
    private List<SLMetric> slMetricList;

    // Constructor----------------------------------------------------------------------------------

    public SLObjective() {
        this.slActionList = new ArrayList<SLAction>();
        this.slMetricList = new ArrayList<SLMetric>();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public List<SLAction> getSlActionList() {
        return this.slActionList;
    }

    public void setSlActionList(List<SLAction> slActionList) {
        this.slActionList = slActionList;
    }

    public List<SLMetric> getSlMetricList() {
        return this.slMetricList;
    }

    public void setSlMetricList(List<SLMetric> slMetricList) {
        this.slMetricList = slMetricList;
    }

}
