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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.controller.DataCenterController;
import org.cloudsimulator.controller.VirtualMachineController;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.persistence.DataCenterDAO;
import org.cloudsimulator.viewer.compositecomponent.InputText;
import org.cloudsimulator.viewer.compositecomponent.InputTextSelectOneMenu;
import org.cloudsimulator.viewer.compositecomponent.InputTextSelectOneMenuListener;

@Named("dynamicAttributesRepository")
public class DynamicAttributesRepository {

    @Inject
    private DataCenterController dataCenterController;
    @Inject
    private VirtualMachineController virtualMachineController;
    private List<String> virtualMachineList;
    private List<String> hostVirtualList;
    private List<String> hostMachineList;
    private List<String> storageList;
    private String ipOfKB;

    @PostConstruct
    public void init() {
        this.virtualMachineList = this.virtualMachineController
                .createVirtualMachineList();
        this.hostVirtualList = this.virtualMachineController
                .createHostVirtualList();
        this.hostMachineList = this.dataCenterController
                .createHostMachineList();
        this.storageList = this.virtualMachineController.getStorageList();
        this.ipOfKB = this.dataCenterController.getIpOfKB();
    }

    public List<InputTextSelectOneMenu> getIcaroServiceListInputSelectOneMenuDynamic() {
        List<InputText> listVirtualMachine = new ArrayList<InputText>();
        for (String currentString : this.virtualMachineList) {
            listVirtualMachine.add(new InputText(currentString, currentString));
        }

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("runsOnVM",
                        "runsOnVm", listVirtualMachine)));
    }

    // TODO Controllare perché ho messo la lista delle virtualMachine al posto
    // della lista delle IcaroApplication
    public List<InputTextSelectOneMenu> getIcaroTenantListInputSelectOneMenuDynamic() {
        List<InputText> listIcaroApplication = new ArrayList<InputText>();
        for (String currentString : this.virtualMachineList) {
            listIcaroApplication
                    .add(new InputText(currentString, currentString));
        }

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("isTenantOf",
                        "isTenantOf", listIcaroApplication)));
    }

    public List<InputTextSelectOneMenuListener> getGroupVirtualMachineListInputSelectOneMenuDynamicListener() {
        List<InputText> listHostMachine = new ArrayList<InputText>();
        for (String currentString : this.hostMachineList) {
            listHostMachine.add(new InputText(currentString, currentString));
        }

        return new ArrayList<InputTextSelectOneMenuListener>(
                Arrays.asList(new InputTextSelectOneMenuListener("arePartOf",
                        "arePartOf", listHostMachine, "changeStorageList")));
    }

    public List<InputTextSelectOneMenu> getGroupVirtualMachineListInputSelectOneMenuDynamic() {
        List<InputText> listStorage = new ArrayList<InputText>();
        for (String currentString : this.storageList) {
            listStorage.add(new InputText(currentString, currentString));
        }

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("isStoredOn",
                        "isStoredOn", listStorage)));
    }

    public List<InputTextSelectOneMenu> getSlMetricListInputSelectOneMenuDynamic() {
        List<InputText> listHostVirtual = new ArrayList<InputText>();
        for (String currentString : this.hostVirtualList) {
            listHostVirtual.add(new InputText(currentString, currentString));
        }

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("dependsOn",
                        "dependsOn", listHostVirtual)));
    }

    public List<InputTextSelectOneMenu> getDataCenterChoiceSelectOneMenu() {
        List<InputText> listDataCenterOnKB = new ArrayList<InputText>();
        DataCenterDAO dataCenterDAO = new DataCenterDAO();
        for (DataCenter dataCenter : dataCenterDAO
                .getDataCenterListByKB(this.ipOfKB)) {
            listDataCenterOnKB.add(new InputText("URI: "
                    + dataCenter.getHasIdentifier() + " ID: "
                    + dataCenter.getHasName(), dataCenter.getUri()));
        }
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("choose", "",
                        listDataCenterOnKB)));
    }

}
