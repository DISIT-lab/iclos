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

package org.cloudsimulator.simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.domain.ontology.GroupVirtualMachine;

@Named("fakeSCE")
@ConversationScoped
public class FakeSCE implements Serializable {

    private static final long serialVersionUID = 2603158706683045358L;
    private BusinessConfiguration businessConfiguration;
    private List<GroupVirtualMachine> groupVirtualMachineForSimulatorList;

    private String ipOfKB;
    private String createdXmlBusinessConfiguration;
    private boolean createdXml;
    private String errorMessage;
    private String alertMessage;
    private String sendDone;

    private boolean viewVirtualMachineForm;
    private boolean viewBusinessConfigurationForm;

    public FakeSCE() {
        super();

        this.viewVirtualMachineForm = false;
        this.viewBusinessConfigurationForm = false;

        this.businessConfiguration = new BusinessConfiguration();
        this.groupVirtualMachineForSimulatorList = new ArrayList<GroupVirtualMachine>();

        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        this.ipOfKB = request.getServerName() + ":" + request.getServerPort();
    }

    public void createVirtualMachineOnDataCenter() {
        /*
         * for (GroupVirtualMachine groupVirtual : this.groupVirtualMachineList)
         * { if (groupVirtual != null) { HostMachine currentHostMachine = new
         * HostMachine(); for (HostMachine hostMachine :
         * this.dataCenterController.getDataCenter().getHostMachineList()) { if
         * (hostMachine.getUri().equals(groupVirtual.getArePartOf())) {
         * currentHostMachine = hostMachine; } }
         * 
         * for (int j = 0; j < groupVirtual.getNumberVirtual(); j++) { String
         * suffixVirtualMachine = "" +
         * this.groupVirtualMachineList.indexOf(groupVirtual) + (j +
         * currentHostMachine.getVirtualMachineList().size() + 1);
         * 
         * this.mapLocalNetworkController = new HashMap<String,
         * LocalNetworkController>();
         * 
         * for (LocalNetwork localNetwork :
         * this.dataCenterController.getDataCenter().getLocalNetworkList()) {
         * controlLocalNetwork(localNetwork); }
         * 
         * List<NetworkAdapter> hasNetworkAdapterVirtualMachineList = new
         * ArrayList<NetworkAdapter>();
         * 
         * for (LocalNetwork localNetwork : groupVirtual.getLocalNetworkList())
         * { if (localNetwork != null) { controlLocalNetwork(localNetwork);
         * hasNetworkAdapterVirtualMachineList.add(new
         * NetworkAdapter(mapLocalNetworkController
         * .get(localNetwork.getLocalUri()).getAddress(), localNetwork
         * .getUri())); } }
         * 
         * String hostMachine = groupVirtual.getArePartOf(); String
         * suffixHostMachine; if (hostMachine.indexOf('_') == -1) {
         * suffixHostMachine =
         * hostMachine.substring(hostMachine.lastIndexOf(':') + 1); } else {
         * suffixHostMachine = hostMachine.substring(hostMachine.indexOf('_') +
         * 1); }
         * 
         * 
         * List<VirtualStorage> hasVirtualStorageList = new
         * ArrayList<VirtualStorage>();
         * 
         * for (VirtualStorage virtualStorage :
         * groupVirtual.getHasVirtualStorageList()) { hasVirtualStorageList.add(
         * new
         * VirtualStorage(this.dataCenterController.getDataCenter().getLocalUri
         * () + "_" + suffixHostMachine + "_VM" + suffixVirtualMachine + "_VS" +
         * groupVirtual.getHasVirtualStorageList().indexOf(virtualStorage),
         * virtualStorage.getHasName(), virtualStorage.getHasIdentifier(),
         * virtualStorage.getHasDiskSize(), virtualStorage.getIsStoredOn())); }
         * 
         * virtualMachineList.add(new
         * VirtualMachine(this.dataCenterController.getDataCenter
         * ().getLocalUri() + "_" + suffixHostMachine + "_VM" +
         * suffixVirtualMachine, groupVirtual.getHasPrefixName() +
         * suffixVirtualMachine, groupVirtual.getHasPrefixIdentifier() +
         * suffixVirtualMachine, groupVirtual.getHasCPUCount(),
         * groupVirtual.getHasCPUSpeedReservation(),
         * groupVirtual.getHasCPUSpeedLimit(), groupVirtual.getHasMemorySize(),
         * groupVirtual.getHasMemoryReservation(),
         * groupVirtual.getHasMemoryLimit(),
         * groupVirtual.getHasExternalIPAddress(),
         * groupVirtual.getHasMonitoringIPAddress(), hasVirtualStorageList,
         * hasNetworkAdapterVirtualMachineList,
         * groupVirtual.getHasMonitorInfoList(), groupVirtual.getHasOS(),
         * hostMachine, groupVirtual.getIsInDomain(),
         * groupVirtual.getIsStoredOn(), groupVirtual.getHasAuthUserName(),
         * groupVirtual.getHasAuthUserPassword(),
         * groupVirtual.getHasMonitorState())); } } }
         * 
         * return "businessConfiguration";
         */

        invertViewBusinessConfigurationForm();
        invertViewVirtualMachineForm();

    }

    public void invertViewVirtualMachineForm() {
        this.viewVirtualMachineForm = !this.viewVirtualMachineForm;
    }

    public void invertViewBusinessConfigurationForm() {
        this.viewBusinessConfigurationForm = !this.viewBusinessConfigurationForm;
    }

    public BusinessConfiguration getBusinessConfiguration() {
        return this.businessConfiguration;
    }

    public void setBusinessConfiguration(
            BusinessConfiguration businessConfiguration) {
        this.businessConfiguration = businessConfiguration;
    }

    public List<GroupVirtualMachine> getGroupVirtualMachineList() {
        return this.groupVirtualMachineForSimulatorList;
    }

    public void setGroupVirtualMachineList(
            List<GroupVirtualMachine> groupVirtualMachineList) {
        this.groupVirtualMachineForSimulatorList = groupVirtualMachineList;
    }

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

    public String getCreatedXmlBusinessConfiguration() {
        return this.createdXmlBusinessConfiguration;
    }

    public void setCreatedXmlBusinessConfiguration(
            String createdXmlBusinessConfiguration) {
        this.createdXmlBusinessConfiguration = createdXmlBusinessConfiguration;
    }

    public boolean isCreatedXml() {
        return this.createdXml;
    }

    public void setCreatedXml(boolean createdXml) {
        this.createdXml = createdXml;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAlertMessage() {
        return this.alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getSendDone() {
        return this.sendDone;
    }

    public void setSendDone(String sendDone) {
        this.sendDone = sendDone;
    }

    public boolean isViewVirtualMachineForm() {
        return this.viewVirtualMachineForm;
    }

    public void setViewVirtualMachineForm(boolean viewVirtualMachineForm) {
        this.viewVirtualMachineForm = viewVirtualMachineForm;
    }

    public boolean isViewBusinessConfigurationForm() {
        return this.viewBusinessConfigurationForm;
    }

    public void setViewBusinessConfigurationForm(
            boolean viewBusinessConfigurationForm) {
        this.viewBusinessConfigurationForm = viewBusinessConfigurationForm;
    }

}
