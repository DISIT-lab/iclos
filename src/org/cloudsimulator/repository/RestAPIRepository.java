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

package org.cloudsimulator.repository;

import javax.inject.Named;

import java.io.Serializable;

@Named("restAPIRepository")
public final class RestAPIRepository implements Serializable {

    private static final long serialVersionUID = -8608443121158545601L;

    public static final String KB_DATA_CENTER = "/IcaroKB/api/dataCenter/";
    public static final String KB_BUSINESS_CONFIGURATION = "/IcaroKB/api/businessConfiguration/";
    public static final String KB_SERVICE_METRIC = "/IcaroKB/api/serviceMetric/";
    public static final String KB_STATUS = "/IcaroKB/api/status";
    public static final String KB_APPLICATION_TYPE = "/IcaroKB/api/applicationType/";
    public static final String KB_BUSINESS_CONFIGURATION_CHECK = "/IcaroKB/api/businessConfigurationCheck";
    public static final String KB_QUERY = "/IcaroKB/sparql?query=";
    public static final String SM_CONFIGURATOR_CONFIGURATION = "/IcaroSM/api/configurator/configuration";
    public static final String SM_MONITOR_XML = "/IcaroSM/api/monitor/xml";
    public static final String SM_MONITOR_INFO = "/IcaroSM/api/monitor/info";
    public static final String SM_MONITOR_IMG = "/IcaroSM/api/monitor/img";

    public String getKbDataCenter() {
        return KB_DATA_CENTER;
    }

    public String getKbBusinessConfiguration() {
        return KB_BUSINESS_CONFIGURATION;
    }

    public String getKbServiceMetric() {
        return KB_SERVICE_METRIC;
    }

    public String getKbStatus() {
        return KB_STATUS;
    }

    public String getKbApplicationType() {
        return KB_APPLICATION_TYPE;
    }

    public String getKbBusinessConfigurationCheck() {
        return KB_BUSINESS_CONFIGURATION_CHECK;
    }

    public String getKbQuery() {
        return KB_QUERY;
    }

    public String getSmConfiguratorConfiguration() {
        return SM_CONFIGURATOR_CONFIGURATION;
    }

    public String getSmMonitorXml() {
        return SM_MONITOR_XML;
    }

    public String getSmMonitorImg() {
        return SM_MONITOR_IMG;
    }

}
