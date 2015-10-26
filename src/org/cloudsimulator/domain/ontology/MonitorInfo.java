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

public class MonitorInfo implements Serializable {

    private static final long serialVersionUID = -1766529633001976440L;
    private String hasMetricName;
    private String hasArguments;
    private Integer hasWarningValue;
    private Integer hasCriticalValue;
    private Integer hasMaxCheckAttempts;
    private Integer hasCheckInterval;
    private String hasCheckMode;
    private String hasMonitorState;

    // Constructor----------------------------------------------------------------------------------

    public MonitorInfo() {
        super();
        initMonitorInfo();
    }

    public MonitorInfo(final String hasMetricName, final String hasArguments,
            final Integer hasWarningValue, final Integer hasCriticalValue,
            final Integer hasMaxCheckAttempts, final Integer hasCheckInterval,
            final String hasCheckMode, final String hasMonitorState) {
        super();
        this.hasMetricName = hasMetricName;
        this.hasArguments = hasArguments;
        this.hasWarningValue = hasWarningValue;
        this.hasCriticalValue = hasCriticalValue;
        this.hasMaxCheckAttempts = hasMaxCheckAttempts;
        this.hasCheckInterval = hasCheckInterval;
        this.hasCheckMode = hasCheckMode;
        this.hasMonitorState = hasMonitorState;
    }

    // Init----------------------------------------------------------------------------------------

    private void initMonitorInfo() {
        this.hasMetricName = "avgCPU";
        this.hasArguments = "nessuno";
        this.hasWarningValue = 85;
        this.hasCriticalValue = 70;
        this.hasMaxCheckAttempts = 20;
        this.hasCheckInterval = 12;
        this.hasCheckMode = "passive";
        this.hasMonitorState = "enabled";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getHasMetricName() {
        return this.hasMetricName;
    }

    public void setHasMetricName(final String hasMetricName) {
        this.hasMetricName = hasMetricName;
    }

    public String getHasArguments() {
        return this.hasArguments;
    }

    public void setHasArguments(final String hasArguments) {
        this.hasArguments = hasArguments;
    }

    public Integer getHasWarningValue() {
        return this.hasWarningValue;
    }

    public void setHasWarningValue(final Integer hasWarningValue) {
        this.hasWarningValue = hasWarningValue;
    }

    public Integer getHasCriticalValue() {
        return this.hasCriticalValue;
    }

    public void setHasCriticalValue(final Integer hasCriticalValue) {
        this.hasCriticalValue = hasCriticalValue;
    }

    public Integer getHasMaxCheckAttempts() {
        return this.hasMaxCheckAttempts;
    }

    public void setHasMaxCheckAttempts(final Integer hasMaxCheckAttempts) {
        this.hasMaxCheckAttempts = hasMaxCheckAttempts;
    }

    public Integer getHasCheckInterval() {
        return this.hasCheckInterval;
    }

    public void setHasCheckInterval(final Integer hasCheckInterval) {
        this.hasCheckInterval = hasCheckInterval;
    }

    public String getHasCheckMode() {
        return this.hasCheckMode;
    }

    public void setHasCheckMode(final String hasCheckMode) {
        this.hasCheckMode = hasCheckMode;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

}
