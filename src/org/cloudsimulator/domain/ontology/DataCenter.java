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
import java.util.Iterator;
import java.util.List;

/**
 * Questa, come le altre classi presenti nel package org.cloudsimulator.domain,
 * rappresenta un'entit� che si trova all'interno della KB (Knowledge Base) di
 * ICARO. In particolare questa classe rappresenta l'entit� DataCenter: una
 * delle due entit� principali della KB (l'altra � BusinessConfiguration), in
 * quanto queste ultime devono essere necessariamente presenti nei due file XML
 * che si inviano alla KB per aggiungere informazioni. Per tale motivo, in
 * questa classe non sono presenti solamente campi che rappresentano le
 * propriet� dell'entit� correlata, ma anche i campi relativi alle entit� che si
 * trovano all'interno del file XML insieme all'entit� DataCenter.
 * 
 * @author Claudio Badii
 * 
 */
public class DataCenter implements Serializable, Machine {

    private static final long serialVersionUID = 5661641907328757000L;
    public static final String PATHURI = "urn:cloudicaro:DataCenter:";
    private String localUri;
    private String hasName;
    private String hasIdentifier;
    private List<HostMachine> hostMachineList;
    private List<ExternalStorage> externalStorageList;
    private List<Firewall> firewallList;
    private List<Router> routerList;
    private List<LocalNetwork> localNetworkList;
    private List<VirtualMachine> virtualMachineList;
    

    // Constructor----------------------------------------------------------------------------------

    public DataCenter() {
        super();
        this.hostMachineList = new ArrayList<HostMachine>();
        this.externalStorageList = new ArrayList<ExternalStorage>();
        this.firewallList = new ArrayList<Firewall>();
        this.routerList = new ArrayList<Router>();
        this.localNetworkList = new ArrayList<LocalNetwork>();
        initDataCenter();
    }

    public DataCenter(final String localUri, final String hasName,
            final String hasIdentifier) {
        super();
        this.localUri = localUri;
        this.hasName = hasName;
        this.hasIdentifier = hasIdentifier;
        this.hostMachineList = new ArrayList<HostMachine>();
        this.externalStorageList = new ArrayList<ExternalStorage>();
        this.firewallList = new ArrayList<Firewall>();
        this.routerList = new ArrayList<Router>();
        this.localNetworkList = new ArrayList<LocalNetwork>();
    }

    // Init----------------------------------------------------------------------------------------

    private void initDataCenter() {
        this.hasName = "Data Center Test-012";
        this.hasIdentifier = "DTC012";
        this.localUri = "TEST-012";
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getUri() {
        return PATHURI + this.localUri;
    }

    public void setUri(final String uri) {
        this.localUri = uri.replace(PATHURI, "");
    }

    public String getLocalUri() {
        return this.localUri;
    }

    public void setLocalUri(final String localUri) {
        this.localUri = localUri;
    }

    public String getHasName() {
        return this.hasName;
    }

    public void setHasName(final String hasName) {
        this.hasName = hasName;
    }

    public String getHasIdentifier() {
        return this.hasIdentifier;
    }

    public void setHasIdentifier(final String hasIdentifier) {
        this.hasIdentifier = hasIdentifier;
    }

    public List<HostMachine> getHostMachineList() {
        return this.hostMachineList;
    }

    public void setHostMachineList(final List<HostMachine> hostMachineList) {
        this.hostMachineList = hostMachineList;
    }
    
    public List<VirtualMachine> getVirtualMachineList() {
        this.virtualMachineList = new ArrayList<VirtualMachine>();
        for(HostMachine host : this.getHostMachineList()){
            this.virtualMachineList.addAll(host.getVirtualMachineList());
        }
        return this.virtualMachineList;
    }

    public void setVirtualMachineList(final List<VirtualMachine> virtualMachineList) {
        this.virtualMachineList = virtualMachineList;
    }


    public List<ExternalStorage> getExternalStorageList() {
        return this.externalStorageList;
    }

    public void setExternalStorageList(
            final List<ExternalStorage> externalStorageList) {
        this.externalStorageList = externalStorageList;
    }

    public List<Firewall> getFirewallList() {
        return this.firewallList;
    }

    public void setFirewallList(final List<Firewall> firewallList) {
        this.firewallList = firewallList;
    }

    public List<Router> getRouterList() {
        return this.routerList;
    }

    public void setRouterList(final List<Router> routerList) {
        this.routerList = routerList;
    }

    public List<LocalNetwork> getLocalNetworkList() {
        return this.localNetworkList;
    }

    public void setLocalNetworkList(final List<LocalNetwork> localNetworkList) {
        this.localNetworkList = localNetworkList;
    }

    public Float getHasMemorySize() {
        Float memSize = 0.0f;        
        for (HostMachine hostMachine : hostMachineList) {
            memSize += hostMachine.getHasMemorySize();
        }        
        return memSize;
    }

    public Float getCpuThroughtput(){
        Float cpu = 0.0f;        
        for (HostMachine hostMachine : hostMachineList) {
            cpu += hostMachine.getHasCPUSpeed() * hostMachine.getHasCPUCount();
        }        
        return cpu;
    }

}
