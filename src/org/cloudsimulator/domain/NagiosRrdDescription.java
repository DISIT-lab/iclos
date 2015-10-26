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

package org.cloudsimulator.domain;

import java.util.HashMap;
import java.util.Map;

public class NagiosRrdDescription {

    private Map<String, String> dataSourceMap;
    private Map<String, String> rrdMap;
    private Map<String, String> nagiosMetaMap;
    private Map<String, String> xmlMap;

    public NagiosRrdDescription() {
        this.dataSourceMap = new HashMap<String, String>();
        this.rrdMap = new HashMap<String, String>();
        this.nagiosMetaMap = new HashMap<String, String>();
        this.xmlMap = new HashMap<String, String>();
    }

    public Map<String, String> getDataSourceMap() {
        return this.dataSourceMap;
    }

    public void setDataSourceMap(Map<String, String> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public Map<String, String> getRrdMap() {
        return this.rrdMap;
    }

    public void setRrdMap(Map<String, String> rrdMap) {
        this.rrdMap = rrdMap;
    }

    public Map<String, String> getNagiosMetaMap() {
        return this.nagiosMetaMap;
    }

    public void setNagiosMetaMap(Map<String, String> nagiosMetaMap) {
        this.nagiosMetaMap = nagiosMetaMap;
    }

    public Map<String, String> getXmlMap() {
        return this.xmlMap;
    }

    public void setXmlMap(Map<String, String> xmlMap) {
        this.xmlMap = xmlMap;
    }

}
