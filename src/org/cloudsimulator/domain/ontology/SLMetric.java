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

public class SLMetric implements Serializable {

    private static final long serialVersionUID = -1546071925267199847L;
    private String hasName;
    private Integer hasMetricValue;
    private String hasSymbol;
    private String hasMetricUnit;
    private String dependsOn;

    // Constructor----------------------------------------------------------------------------------

    public SLMetric() {
        super();
        initSLMetric();
    }

    // Init----------------------------------------------------------------------------------------

    private void initSLMetric() {
        this.hasName = "avgCPUPerc";
        this.hasMetricValue = 30;
        this.hasSymbol = "less";
        this.hasMetricUnit = "percentual";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public Integer getHasMetricValue() {
        return this.hasMetricValue;
    }

    public void setHasMetricValue(final Integer hasMetricValue) {
        this.hasMetricValue = hasMetricValue;
    }

    public String getHasSymbol() {
        return this.hasSymbol;
    }

    public void setHasSymbol(final String hasSymbol) {
        this.hasSymbol = hasSymbol;
    }

    public String getHasMetricUnit() {
        return this.hasMetricUnit;
    }

    public void setHasMetricUnit(final String hasMetricUnit) {
        this.hasMetricUnit = hasMetricUnit;
    }

    public String getDependsOn() {
        return this.dependsOn;
    }

    public void setDependsOn(final String dependsOn) {
        this.dependsOn = dependsOn;
    }

}
