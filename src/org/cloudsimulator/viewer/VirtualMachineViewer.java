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

package org.cloudsimulator.viewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.controller.VirtualMachineController;
import org.cloudsimulator.domain.ontology.GroupVirtualMachine;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("virtualMachineViewer")
@ConversationScoped
public class VirtualMachineViewer implements Serializable {

    private static final long serialVersionUID = 4061013389077743840L;
    @Inject
    private VirtualMachineController virtualMachineController;
    private boolean buttonNext;
    private String entityToAddMessage;
    private transient HtmlPanelGroup panelBodyVirtualMachinePrincipal;
    private List<HtmlPanelGroup> virtualMachinePanelBodyList;
    private int indexVirtualMachine;
    private int countVirtualMachine;
    private List<Integer> indexVMMonitorInfoList;
    private List<Integer> indexVMLocalNetworkList;
    private List<Integer> countVMLocalNetworkList;
    private List<Integer> indexVMVirtualStorageList;
    private List<Integer> countVMVirtualStorageList;

    public VirtualMachineViewer() {
        super();
        this.virtualMachinePanelBodyList = new ArrayList<HtmlPanelGroup>();

        this.indexVMMonitorInfoList = new ArrayList<Integer>();
        this.indexVMLocalNetworkList = new ArrayList<Integer>();
        this.countVMLocalNetworkList = new ArrayList<Integer>();
        this.indexVMVirtualStorageList = new ArrayList<Integer>();
        this.countVMVirtualStorageList = new ArrayList<Integer>();

        controlForButtonNext();
    }

    public String virtualMachineViewClear() {
        panelBodyVirtualMachinePrincipal.getChildren().clear();
        return "virtualMachine";
    }

    public void addPanelGroupVirtualMachine() {
        this.virtualMachineController.getGroupVirtualMachineList().add(
                indexVirtualMachine, new GroupVirtualMachine());
        this.virtualMachinePanelBodyList.add(indexVirtualMachine,
                new HtmlPanelGroup());
        this.indexVMVirtualStorageList.add(indexVirtualMachine, 0);
        this.countVMVirtualStorageList.add(indexVirtualMachine, 0);
        this.indexVMLocalNetworkList.add(indexVirtualMachine, 0);
        this.countVMLocalNetworkList.add(indexVirtualMachine, 0);
        this.indexVMMonitorInfoList.add(indexVirtualMachine, 0);
        CCUtility.setFirstPanel(indexVirtualMachine,
                panelBodyVirtualMachinePrincipal, "groupVirtualMachine",
                "virtualMachine");
        this.indexVirtualMachine++;
        this.countVirtualMachine++;
        controlForButtonNext();
    }

    public void addPanelMonitorInfoGroupVirtualMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexMonitorInfo = indexVMMonitorInfoList
                .get(indexPanelParent);
        this.virtualMachinePanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.virtualMachineController.getGroupVirtualMachineList()
                .get(indexPanelParent).getMonitorInfoList()
                .add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "groupVirtualMachine",
                "virtualMachine");
        this.indexVMMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelLocalNetworkGroupVirtualMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalNetwork = indexVMLocalNetworkList
                .get(indexPanelParent);
        this.virtualMachinePanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.virtualMachineController.getGroupVirtualMachineList()
                .get(indexPanelParent).getLocalNetworkList()
                .add(indexLocalNetwork, new LocalNetwork());
        CCUtility.setSecondPanel(indexLocalNetwork, indexPanelParent,
                htmlPanelParent, "localNetwork", "groupVirtualMachine",
                "virtualMachine");
        this.indexVMLocalNetworkList.set(indexPanelParent,
                indexLocalNetwork + 1);
        this.countVMLocalNetworkList.set(indexPanelParent,
                this.countVMLocalNetworkList.get(indexPanelParent) + 1);
        controlForButtonNext();
    }

    public void addPanelVirtualStorageGroupVirtualMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexVirtualStorage = indexVMVirtualStorageList
                .get(indexPanelParent);
        this.virtualMachinePanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.virtualMachineController.getGroupVirtualMachineList()
                .get(indexPanelParent).getVirtualStorageList()
                .add(indexVirtualStorage, new VirtualStorage());
        CCUtility.setSecondPanel(indexVirtualStorage, indexPanelParent,
                htmlPanelParent, "virtualStorage", "groupVirtualMachine",
                "virtualMachine");
        this.indexVMVirtualStorageList.set(indexPanelParent,
                indexVirtualStorage + 1);
        this.countVMVirtualStorageList.set(indexPanelParent,
                this.countVMVirtualStorageList.get(indexPanelParent) + 1);
        controlForButtonNext();
    }

    public void removePanelGroupVirtualMachine(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBodyVirtualMachinePrincipal
                .findComponent(idPanelToRemove);
        panelBodyVirtualMachinePrincipal.getChildren().remove(uiPanelToRemove);
        this.virtualMachinePanelBodyList.set(indexPanelToRemove, null);
        this.virtualMachineController.getGroupVirtualMachineList().set(
                indexPanelToRemove, null);
        this.indexVMVirtualStorageList.set(indexPanelToRemove, null);
        this.indexVMLocalNetworkList.set(indexPanelToRemove, null);
        this.indexVMMonitorInfoList.set(indexPanelToRemove, null);
        this.countVirtualMachine--;
        controlForButtonNext();
    }

    public void removePanelMonitorInfoGroupVirtualMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.virtualMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.virtualMachineController.getGroupVirtualMachineList()
                        .get(indexPanelParent).getMonitorInfoList()
                        .set(indexPanelToRemove, null);
            }

        }
    }

    public void removePanelLocalNetworkGroupVirtualMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.virtualMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.virtualMachineController.getGroupVirtualMachineList()
                        .get(indexPanelParent).getLocalNetworkList()
                        .set(indexPanelToRemove, null);
                this.countVMLocalNetworkList.set(indexPanelParent,
                        this.countVMLocalNetworkList.get(indexPanelParent) - 1);
            }

        }
        controlForButtonNext();
    }

    public void removePanelVirtualStorageGroupVirtualMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.virtualMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.virtualMachineController.getGroupVirtualMachineList()
                        .get(indexPanelParent).getVirtualStorageList()
                        .set(indexPanelToRemove, null);
                this.countVMVirtualStorageList
                        .set(indexPanelParent, this.countVMVirtualStorageList
                                .get(indexPanelParent) - 1);
            }

        }
        controlForButtonNext();
    }

    private void controlForButtonNext() {
        this.buttonNext = false;
        this.entityToAddMessage = "";
        if (this.countVirtualMachine >= 1) {
            for (int i = 0; i < this.countVMVirtualStorageList.size(); i++) {
                if (this.countVMVirtualStorageList.get(i) < 1
                        && this.virtualMachineController
                                .getGroupVirtualMachineList().get(i) != null) {
                    this.entityToAddMessage = "Add to each virtualMachine at least one virtualStorage. ";
                    return;
                }
            }
            for (int i = 0; i < this.countVMLocalNetworkList.size(); i++) {
                if (this.countVMLocalNetworkList.get(i) < 1
                        && this.virtualMachineController
                                .getGroupVirtualMachineList().get(i) != null) {
                    this.entityToAddMessage = "Add to each virtualMachine at least one localNetwork. ";
                    return;
                }
            }
        } else {
            this.entityToAddMessage = "Add at least one virtualMachine to BusinessConfiguration";
            return;
        }

        this.buttonNext = true;
    }

    public boolean isButtonNext() {
        return this.buttonNext;
    }

    public void setButtonNext(final boolean buttonNext) {
        this.buttonNext = buttonNext;
    }

    public String getEntityToAddMessage() {
        return entityToAddMessage;
    }

    public HtmlPanelGroup getPanelBodyVirtualMachinePrincipal() {
        return this.panelBodyVirtualMachinePrincipal;
    }

    public void setPanelBodyVirtualMachinePrincipal(
            final HtmlPanelGroup panelBodyVirtualMachinePrincipal) {
        this.panelBodyVirtualMachinePrincipal = panelBodyVirtualMachinePrincipal;
    }

    public List<HtmlPanelGroup> getPanelVirtualMachineBodyList() {
        return this.virtualMachinePanelBodyList;
    }

    public void setPanelVirtualMachineBodyList(
            final List<HtmlPanelGroup> panelVirtualMachineBodyList) {
        this.virtualMachinePanelBodyList = panelVirtualMachineBodyList;
    }

}
