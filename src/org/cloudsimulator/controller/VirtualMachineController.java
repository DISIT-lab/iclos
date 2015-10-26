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

package org.cloudsimulator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.domain.ontology.GroupVirtualMachine;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.exception.LocalNetworkDuplicateException;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;
import org.cloudsimulator.utility.RatioNumberContainer;
import org.cloudsimulator.utility.Utility;

@Named("virtualMachineController")
@ConversationScoped
public class VirtualMachineController implements Serializable {

    private static final long serialVersionUID = 9013846275869212621L;
    private List<GroupVirtualMachine> groupVirtualMachineList;
    private List<VirtualMachine> virtualMachineList;
    private List<String> storageList;
    @Inject
    private DataCenterController dataCenterController;
    private Map<String, LocalNetworkController> mapLocalNetworkController;
    private List<RatioNumberContainer> vmScaleFactor;
    private Integer currentRatio=100; 
    private Integer currentScale=100;

    public VirtualMachineController() {
        super();
        this.groupVirtualMachineList = new ArrayList<GroupVirtualMachine>();
        this.virtualMachineList = new ArrayList<VirtualMachine>();
        this.storageList = new ArrayList<String>();
        this.setVmScaleFactor(new ArrayList<RatioNumberContainer>());
        
        vmScaleFactor.add(new RatioNumberContainer(50, 10));
        vmScaleFactor.add(new RatioNumberContainer(100, 20));
        vmScaleFactor.add(new RatioNumberContainer(200, 20));
        vmScaleFactor.add(new RatioNumberContainer(250, 20));
        vmScaleFactor.add(new RatioNumberContainer(300, 20));
        vmScaleFactor.add(new RatioNumberContainer(350, 10));
        
    }

    public List<String> createVirtualMachineList() {
        List<String> virtualMachineListString = new ArrayList<String>();
        for (VirtualMachine virtualMachine : this.virtualMachineList) {
            if (virtualMachine != null) {
                virtualMachineListString.add(virtualMachine.getUri());
            }
        }

        return virtualMachineListString;
    }
    
    public void addVmScaleFactor(){
        this.vmScaleFactor.add(new RatioNumberContainer(this.getCurrentScale(),this.getCurrentRatio()));
    }

    public List<String> createHostVirtualList() {
        List<String> hostVirtualListString = new ArrayList<String>();
        for (VirtualMachine virtualMachine : this.virtualMachineList) {
            if (virtualMachine != null) {
                if (!hostVirtualListString.contains(virtualMachine
                        .getIsPartOf())) {
                    hostVirtualListString.add(virtualMachine.getIsPartOf());
                }

                hostVirtualListString.add(virtualMachine.getUri());
            }
        }

        return hostVirtualListString;
    }

    public void changeStorageList(ValueChangeEvent ev) {
        List<String> storageListString = new ArrayList<String>();
        for (HostMachine hostMachine : this.dataCenterController
                .getDataCenter().getHostMachineList()) {
            if (hostMachine.getUri().equals(ev.getNewValue().toString())) {
                for (LocalStorage localStorage : hostMachine
                        .getHasLocalStorageList()) {
                    storageListString.add(localStorage.getUri());
                }
                storageListString.addAll(hostMachine.getUseSharedStorageList());
                break;
            }
        }
        this.storageList = storageListString;
    }

    public String createVirtualMachineOnDataCenter()
            throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {
                        
        this.virtualMachineList = new ArrayList<VirtualMachine>();

        
        for (GroupVirtualMachine groupVirtual : this.groupVirtualMachineList) {
            if (groupVirtual != null) {
                
                /* Build scaling factor list based on vm vmScaleFactor lists */
                int n_el = groupVirtual.getNumberVirtual();
                float[] scalingFactor = new float[n_el];
                for (int i = 0; i < scalingFactor.length; i++) {
                    scalingFactor[i] = 1.0f;
                }
                
                int offset = 0;
                for (RatioNumberContainer ratioNumberContainer : vmScaleFactor) {
                    int n_ratio = (int) Math.floor(((float)ratioNumberContainer.getRatio() / 100.0f) * groupVirtual.getNumberVirtual());
                    //non controllo in fase di inserimento che la percentuale sia 100, quindi lo gestisco qui
                    if( offset + n_ratio > n_el)
                        n_ratio = n_el - offset;
                    
                    if( n_ratio <= 0 )
                        break;
                    
                    for(int z = 0; z < n_ratio; z++){                     
                        scalingFactor[offset+z] = (float)ratioNumberContainer.getScale() / 100.0f;                        
                    }                    
                    offset += n_ratio;
                }
                
                HostMachine currentHostMachine = null;
                for (HostMachine hostMachine : this.dataCenterController
                        .getDataCenter().getHostMachineList()) {
                    if (hostMachine.getUri()
                            .equals(groupVirtual.getArePartOf())) {
                        currentHostMachine = hostMachine;
                    }
                }

                for (int j = 0; j < groupVirtual.getNumberVirtual(); j++) {
                    String suffixVirtualMachine = ""
                            + this.groupVirtualMachineList
                                    .indexOf(groupVirtual) + (j + 1);

                    this.mapLocalNetworkController = new HashMap<String, LocalNetworkController>();

                    for (LocalNetwork localNetwork : this.dataCenterController
                            .getDataCenter().getLocalNetworkList()) {
                        controlLocalNetwork(localNetwork);
                    }

                    List<NetworkAdapter> hasNetworkAdapterVirtualMachineList = new ArrayList<NetworkAdapter>();

                    for (LocalNetwork localNetwork : groupVirtual
                            .getLocalNetworkList()) {
                        if (localNetwork != null) {
                            controlLocalNetwork(localNetwork);
                            hasNetworkAdapterVirtualMachineList
                                    .add(new NetworkAdapter(
                                            mapLocalNetworkController.get(
                                                    localNetwork.getLocalUri())
                                                    .getAddress(), localNetwork
                                                    .getUri()));
                        }
                    }

                    String hostMachine = groupVirtual.getArePartOf();
                    String suffixHostMachine;
                    if (hostMachine.indexOf('_') == -1) {
                        suffixHostMachine = hostMachine.substring(hostMachine
                                .lastIndexOf(':') + 1);
                    } else {
                        suffixHostMachine = hostMachine.substring(hostMachine
                                .indexOf('_') + 1);
                    }

                    List<VirtualStorage> hasVirtualStorageList = new ArrayList<VirtualStorage>();

                    for (VirtualStorage virtualStorage : groupVirtual
                            .getVirtualStorageList()) {
                        if (virtualStorage != null) {
                            hasVirtualStorageList.add(new VirtualStorage(
                                    this.dataCenterController.getDataCenter()
                                            .getLocalUri()
                                            + "_"
                                            + suffixHostMachine
                                            + "_VM"
                                            + suffixVirtualMachine
                                            + "_VS"
                                            + groupVirtual
                                                    .getVirtualStorageList()
                                                    .indexOf(virtualStorage),
                                    virtualStorage.getHasName(), virtualStorage
                                            .getHasIdentifier(), virtualStorage
                                            .getHasDiskSize(), virtualStorage
                                            .getIsStoredOn()));
                        }
                    }

                    float currentScale = scalingFactor[j];
                    virtualMachineList.add(new VirtualMachine(
                            this.dataCenterController.getDataCenter()
                                    .getLocalUri()
                                    + "_"
                                    + suffixHostMachine
                                    + "_VM" + suffixVirtualMachine,
                            groupVirtual.getHasPrefixName()
                                    + suffixVirtualMachine
                                    + Utility.getTimeStampForWriteDirectory(),
                            groupVirtual.getHasPrefixIdentifier()
                                    + suffixVirtualMachine, groupVirtual
                                    .getHasCPUCount(), groupVirtual
                                    .getHasCPUSpeedReservation() * currentScale, groupVirtual
                                    .getHasCPUSpeedLimit() * currentScale, groupVirtual
                                    .getHasMemorySize() * currentScale, groupVirtual
                                    .getHasMemoryReservation() * currentScale, groupVirtual
                                    .getHasMemoryLimit() * currentScale, groupVirtual
                                    .getHasExternalIPAddress(),
                            hasVirtualStorageList,
                            hasNetworkAdapterVirtualMachineList, groupVirtual
                                    .getMonitorInfoList(), groupVirtual
                                    .getHasOS(), hostMachine, groupVirtual
                                    .getIsInDomain(), groupVirtual
                                    .getIsStoredOn(), groupVirtual
                                    .getHasAuthUserName(), groupVirtual
                                    .getHasAuthUserPassword(), groupVirtual
                                    .getHasMonitorState()));
                }

                currentHostMachine.setVirtualMachineList(virtualMachineList);
            }
        }
        return "businessConfiguration";
    }

    private void controlLocalNetwork(final LocalNetwork localNetwork)
            throws LocalNetworkDuplicateException {
        if (localNetwork != null) {
            if (!mapLocalNetworkController.containsKey(localNetwork
                    .getLocalUri())) {
                mapLocalNetworkController.put(localNetwork.getLocalUri(),
                        new LocalNetworkController(localNetwork));
            } else {
                if (!mapLocalNetworkController.get(localNetwork.getLocalUri())
                        .getLocalNetwork().getHasIPAddress()
                        .equals(localNetwork.getHasIPAddress())
                        || !mapLocalNetworkController
                                .get(localNetwork.getLocalUri())
                                .getLocalNetwork().getHasSubNetworkMask()
                                .equals(localNetwork.getHasSubNetworkMask())) {
                    throw new LocalNetworkDuplicateException(
                            mapLocalNetworkController.get(
                                    localNetwork.getLocalUri())
                                    .getLocalNetwork(), localNetwork);
                }
            }
        }
    }

    public List<GroupVirtualMachine> getGroupVirtualMachineList() {
        return this.groupVirtualMachineList;
    }

    public void setGroupVirtualMachineList(
            final List<GroupVirtualMachine> groupVirtualMachineList) {
        this.groupVirtualMachineList = groupVirtualMachineList;
    }

    public List<VirtualMachine> getVirtualMachineList() {
        return this.virtualMachineList;
    }

    public void setVirtualMachineList(
            final List<VirtualMachine> virtualMachineList) {
        this.virtualMachineList = virtualMachineList;
    }

    public List<String> getStorageList() {
        return this.storageList;
    }

    public void setStorageList(List<String> storageList) {
        this.storageList = storageList;
    }

    public List<RatioNumberContainer> getVmScaleFactor() {
        return vmScaleFactor;
    }

    public void setVmScaleFactor(List<RatioNumberContainer> vmScaleFactor) {
        this.vmScaleFactor = vmScaleFactor;
    }

    public Integer getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(Integer currentRatio) {
        this.currentRatio = currentRatio;
    }

    public Integer getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(Integer currentScale) {
        this.currentScale = currentScale;
    }

}
