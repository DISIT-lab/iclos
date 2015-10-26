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

public class GroupRouter implements Serializable {

    private static final long serialVersionUID = 8667899485122498825L;
    private Integer numberRouter;
    private List<LocalNetwork> localNetworkList;
    private List<MonitorInfo> monitorInfoList;
    private String hasPrefixIdentifier;
    private String hasPrefixName;
    private String hasMonitorState;
    private String hasModelName;

    // Constructor----------------------------------------------------------------------------------

    public GroupRouter() {
        super();
        this.localNetworkList = new ArrayList<LocalNetwork>();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        initRouter();
    }

    // Init----------------------------------------------------------------------------------------

    private void initRouter() {
        this.numberRouter = 12;
        this.hasPrefixIdentifier = "RT";
        this.hasPrefixName = "RouterTest";
        this.hasMonitorState = "enabled";
        this.hasModelName = "NetGear";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public Integer getNumberRouter() {
        return this.numberRouter;
    }

    public void setNumberRouter(final Integer numberRouter) {
        this.numberRouter = numberRouter;
    }

    public List<LocalNetwork> getLocalNetworkList() {
        return this.localNetworkList;
    }

    public void setLocalNetworkList(final List<LocalNetwork> localNetworkList) {
        this.localNetworkList = localNetworkList;
    }

    public List<MonitorInfo> getMonitorInfoList() {
        return this.monitorInfoList;
    }

    public void setMonitorInfoList(List<MonitorInfo> monitorInfoList) {
        this.monitorInfoList = monitorInfoList;
    }

    public String getHasPrefixIdentifier() {
        return this.hasPrefixIdentifier;
    }

    public void setHasPrefixIdentifier(final String hasPrefixIdentifier) {
        this.hasPrefixIdentifier = hasPrefixIdentifier;
    }

    public String getHasPrefixName() {
        return this.hasPrefixName;
    }

    public void setHasPrefixName(final String hasPrefixName) {
        this.hasPrefixName = hasPrefixName;
    }

    public String getHasMonitorState() {
        return this.hasMonitorState;
    }

    public void setHasMonitorState(final String hasMonitorState) {
        this.hasMonitorState = hasMonitorState;
    }

    public String getHasModelName() {
        return this.hasModelName;
    }

    public void setHasModelName(final String hasModelName) {
        this.hasModelName = hasModelName;
    }

}
