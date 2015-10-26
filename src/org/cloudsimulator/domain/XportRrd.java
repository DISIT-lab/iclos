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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XportRrd {

    private Map<Integer, List<Float>> dataMap;
    private Map<String, Integer> metaMap;
    private List<String> columnsLegend;

    public XportRrd() {
        this.dataMap = new LinkedHashMap<Integer, List<Float>>();
        this.metaMap = new HashMap<String, Integer>();
        this.columnsLegend = new ArrayList<String>();
    }

    public Map<Integer, List<Float>> getDataMap() {
        return this.dataMap;
    }

    public void setDataMap(Map<Integer, List<Float>> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, Integer> getMetaMap() {
        return this.metaMap;
    }

    public void setMetaMap(Map<String, Integer> metaMap) {
        this.metaMap = metaMap;
    }

    public List<String> getColumnsLegend() {
        return this.columnsLegend;
    }

    public void setColumnsLegend(List<String> columnsLegend) {
        this.columnsLegend = columnsLegend;
    }

}
