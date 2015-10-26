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

public class GroupExternalStorage implements Serializable {

    private static final long serialVersionUID = -8684816557430183493L;
    private Integer numberExternalStorage;
    private List<LocalNetwork> localNetworkList;
    private List<SharedStorageVolume> sharedStorageVolumeList;
    private List<MonitorInfo> monitorInfoList;
    private String hasPrefixIdentifier;
    private String hasPrefixName;
    private String hasMonitorState;
    private String hasModelName;

    // Constructor----------------------------------------------------------------------------------

    public GroupExternalStorage() {
        super();
        this.localNetworkList = new ArrayList<LocalNetwork>();
        this.sharedStorageVolumeList = new ArrayList<SharedStorageVolume>();
        this.monitorInfoList = new ArrayList<MonitorInfo>();
        initExternalStorage();
    }

    // Init----------------------------------------------------------------------------------------

    private void initExternalStorage() {
        this.numberExternalStorage = 15;
        this.hasPrefixIdentifier = "EST";
        this.hasPrefixName = "ExtStoTest";
        this.hasMonitorState = "enabled";
        this.hasModelName = "Seagate";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public Integer getNumberExternalStorage() {
        return this.numberExternalStorage;
    }

    public void setNumberExternalStorage(final Integer numberExternalStorage) {
        this.numberExternalStorage = numberExternalStorage;
    }

    public List<LocalNetwork> getLocalNetworkList() {
        return this.localNetworkList;
    }

    public void setLocalNetworkList(final List<LocalNetwork> localNetworkList) {
        this.localNetworkList = localNetworkList;
    }

    public List<SharedStorageVolume> getSharedStorageVolumeList() {
        return this.sharedStorageVolumeList;
    }

    public void setSharedStorageVolumeList(
            List<SharedStorageVolume> sharedStorageVolumeList) {
        this.sharedStorageVolumeList = sharedStorageVolumeList;
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
