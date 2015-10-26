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

public class GroupServiceMetric implements Serializable {

    private static final long serialVersionUID = 9101296946720809931L;
    private String dependsOn;
    private String dateFrom;
    private String dateTo;
    private Integer numberServiceMetric;
    private Integer minValue;
    private Integer maxValue;
    private Float finalValue;
    private String metricName;
    private String metricUnit;

    // GettersAndSetters----------------------------------------------------------------------------

    public String getDependsOn() {
        return this.dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getDateFrom() {
        return this.dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return this.dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getNumberServiceMetric() {
        return this.numberServiceMetric;
    }

    public void setNumberServiceMetric(Integer numberServiceMetric) {
        this.numberServiceMetric = numberServiceMetric;
    }

    public Integer getMinValue() {
        return this.minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public Float getFinalValue() {
        return this.finalValue;
    }

    public void setFinalValue(Float finalValue) {
        this.finalValue = finalValue;
    }

    public String getMetricName() {
        return this.metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricUnit() {
        return this.metricUnit;
    }

    public void setMetricUnit(String metricUnit) {
        this.metricUnit = metricUnit;
    }

}
