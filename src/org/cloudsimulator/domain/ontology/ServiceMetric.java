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

public class ServiceMetric implements Serializable {

    private static final long serialVersionUID = 6002955060800531453L;
    private String atTime;
    private String hasMetricName;
    private Float hasMetricValue;
    private String hasMetricUnit;
    private String dependsOn;

    // Constructor----------------------------------------------------------------------------------

    public ServiceMetric(final String atTime, final String hasMetricName,
            final Float hasMetricValue, final String hasMetricUnit,
            final String dependsOn) {
        super();
        this.atTime = atTime;
        this.hasMetricName = hasMetricName;
        this.hasMetricValue = hasMetricValue;
        this.hasMetricUnit = hasMetricUnit;
        this.dependsOn = dependsOn;
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public ServiceMetric() {
    }

    public String getAtTime() {
        return this.atTime;
    }

    public void setAtTime(final String atTime) {
        this.atTime = atTime;
    }

    public String getHasMetricName() {
        return this.hasMetricName;
    }

    public void setHasMetricName(final String hasMetricName) {
        this.hasMetricName = hasMetricName;
    }

    public Float getHasMetricValue() {
        return this.hasMetricValue;
    }

    public void setHasMetricValue(final Float hasMetricValue) {
        this.hasMetricValue = hasMetricValue;
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
