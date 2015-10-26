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
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.injection.InjectedConfiguration;
import org.cloudsimulator.persistence.BusinessConfigurationDAO;
import org.cloudsimulator.persistence.DataCenterDAO;

@Named("analyzeWorkLoadController")
@ConversationScoped
public class AnalyzeWorkLoadController implements Serializable {

    private static final long serialVersionUID = 980391635704686574L;
    private boolean ipInserted;
    @Inject
    @InjectedConfiguration(key = "icaroKB.addressIP", defaultValue = "localhost:8080")
    private String ipOfKB;
    private String dataCenterChoice;
    private transient DataCenterDAO dataCenterDAO;
    private transient BusinessConfigurationDAO businessConfigurationDAO;
    private DataCenter dataCenter;
    private Map<BusinessConfiguration, List<String>> mapVirtualMachineToBusinessConfiguration;

    // Constructor----------------------------------------------------------------------------------

    public AnalyzeWorkLoadController() {
        super();
        this.dataCenter = new DataCenter();
        this.dataCenterDAO = new DataCenterDAO();
        this.businessConfigurationDAO = new BusinessConfigurationDAO();
        this.mapVirtualMachineToBusinessConfiguration = new HashMap<BusinessConfiguration, List<String>>();
    }

    // Method--------------------------------------------------------------------------------------

    public void loadDataCenter() {
        this.dataCenter = dataCenterDAO.fetchDataCenterByURI(this.ipOfKB,
                this.dataCenterChoice);
        for (HostMachine hostMachine : this.dataCenter.getHostMachineList()) {
            BusinessConfiguration businessConfiguration = null;
            for (VirtualMachine virtualMachine : hostMachine
                    .getVirtualMachineList()) {
                List<String> uriBusinessConfigurationList = this.businessConfigurationDAO
                        .getURIBusinessConfigurationListByVirtualMachine(
                                this.ipOfKB, virtualMachine.getUri());
                for (String uriBusinessConfiguration : uriBusinessConfigurationList) {
                    if (businessConfiguration == null
                            || !uriBusinessConfiguration
                                    .equals(businessConfiguration.getUri())) {
                        businessConfiguration = this.businessConfigurationDAO
                                .fetchBusinessConfigurationByURI(this.ipOfKB,
                                        uriBusinessConfiguration);
                    }
                    if (!this.mapVirtualMachineToBusinessConfiguration
                            .containsKey(businessConfiguration)) {
                        this.mapVirtualMachineToBusinessConfiguration.put(
                                businessConfiguration, new ArrayList<String>());
                    }
                    this.mapVirtualMachineToBusinessConfiguration.get(
                            businessConfiguration).add(virtualMachine.getUri());
                }
            }
        }

    }

    public void invertIpInserted() {
        this.ipInserted = !ipInserted;
    }

    // Getters&setters-------------------------------------------------------------------------

    public List<String> getUriDataCenterList() {
        return this.dataCenterDAO.getURIDataCenterListByKB(this.ipOfKB);
    }

    public boolean isIpInserted() {
        return this.ipInserted;
    }

    public void setIpInserted(boolean ipInserted) {
        this.ipInserted = ipInserted;
    }

    public String getDataCenterChoice() {
        return this.dataCenterChoice;
    }

    public void setDataCenterChoice(final String dataCenterChoice) {
        this.dataCenterChoice = dataCenterChoice;
        loadDataCenter();
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

    public Map<BusinessConfiguration, List<String>> getMapVirtualMachineToBusinessConfiguration() {
        return mapVirtualMachineToBusinessConfiguration;
    }

}
