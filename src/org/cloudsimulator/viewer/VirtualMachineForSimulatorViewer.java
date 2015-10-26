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

import org.cloudsimulator.domain.ontology.GroupVirtualMachine;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.simulator.FakeSCE;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("virtualMachineForSimulatorViewer")
@ConversationScoped
public class VirtualMachineForSimulatorViewer implements Serializable {

    private static final long serialVersionUID = 2736989737825705221L;
    @Inject
    private FakeSCE fakeSCE;
    private boolean buttonNext;
    private String entityToAddMessage;
    private transient HtmlPanelGroup panelBodyVirtualMachinePrincipal;
    private List<HtmlPanelGroup> panelVirtualMachineBodyList;
    private int indexVirtualMachine;
    private int countVirtualMachine;
    private List<Integer> indexVMVirtualStorageList;
    private List<Integer> countVMVirtualStorageList;

    public VirtualMachineForSimulatorViewer() {
        super();
        this.panelVirtualMachineBodyList = new ArrayList<HtmlPanelGroup>();

        this.indexVMVirtualStorageList = new ArrayList<Integer>();
        this.countVMVirtualStorageList = new ArrayList<Integer>();

        controlForButtonNext();
    }

    public String virtualMachineViewClear() {
        panelBodyVirtualMachinePrincipal.getChildren().clear();
        return "virtualMachine";
    }

    public void addPanelVirtualMachine() {
        this.fakeSCE.getGroupVirtualMachineList().add(indexVirtualMachine,
                new GroupVirtualMachine());
        this.panelVirtualMachineBodyList.add(indexVirtualMachine,
                new HtmlPanelGroup());
        this.indexVMVirtualStorageList.add(indexVirtualMachine, 0);
        this.countVMVirtualStorageList.add(indexVirtualMachine, 0);
        CCUtility
                .setFirstPanel(indexVirtualMachine,
                        panelBodyVirtualMachinePrincipal,
                        "groupVirtualMachineForSimulator",
                        "virtualMachineForSimulator");
        this.indexVirtualMachine++;
        this.countVirtualMachine++;
        controlForButtonNext();
    }

    public void addPanelVirtualStorageVirtualMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexVirtualStorage = indexVMVirtualStorageList
                .get(indexPanelParent);
        this.panelVirtualMachineBodyList.set(indexPanelParent, htmlPanelParent);
        this.fakeSCE.getGroupVirtualMachineList().get(indexPanelParent)
                .getVirtualStorageList()
                .add(indexVirtualStorage, new VirtualStorage());
        CCUtility
                .setSecondPanel(indexVirtualStorage, indexPanelParent,
                        htmlPanelParent, "virtualStorage",
                        "groupVirtualMachineForSimulator",
                        "virtualMachineForSimulator");
        this.indexVMVirtualStorageList.set(indexPanelParent,
                indexVirtualStorage + 1);
        this.countVMVirtualStorageList.set(indexPanelParent,
                this.countVMVirtualStorageList.get(indexPanelParent) + 1);
        controlForButtonNext();
    }

    public void removePanelVirtualMachineForSimulator(
            final String idPanelToRemove, final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBodyVirtualMachinePrincipal
                .findComponent(idPanelToRemove);
        panelBodyVirtualMachinePrincipal.getChildren().remove(uiPanelToRemove);
        this.panelVirtualMachineBodyList.set(indexPanelToRemove, null);
        this.fakeSCE.getGroupVirtualMachineList().set(indexPanelToRemove, null);
        this.indexVMVirtualStorageList.set(indexPanelToRemove, null);
        this.countVirtualMachine--;
        controlForButtonNext();
    }

    public void removePanelVirtualStorageVirtualMachineForSimulator(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.panelVirtualMachineBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.fakeSCE.getGroupVirtualMachineList().get(indexPanelParent)
                        .getVirtualStorageList().set(indexPanelToRemove, null);
                this.countVMVirtualStorageList
                        .set(indexPanelParent, this.countVMVirtualStorageList
                                .get(indexPanelParent) - 1);
            }

        }
        controlForButtonNext();
    }

    public boolean isButtonNext() {
        return this.buttonNext;
    }

    public void setButtonNext(final boolean buttonNext) {
        this.buttonNext = buttonNext;
    }

    public HtmlPanelGroup getPanelBodyVirtualMachinePrincipal() {
        return this.panelBodyVirtualMachinePrincipal;
    }

    public void setPanelBodyVirtualMachinePrincipal(
            final HtmlPanelGroup panelBodyVirtualMachinePrincipal) {
        this.panelBodyVirtualMachinePrincipal = panelBodyVirtualMachinePrincipal;
    }

    public List<HtmlPanelGroup> getPanelVirtualMachineBodyList() {
        return this.panelVirtualMachineBodyList;
    }

    public void setPanelVirtualMachineBodyList(
            final List<HtmlPanelGroup> panelVirtualMachineBodyList) {
        this.panelVirtualMachineBodyList = panelVirtualMachineBodyList;
    }

    public String getEntityToAddMessage() {
        return entityToAddMessage;
    }

    private void controlForButtonNext() {
        this.buttonNext = false;
        this.entityToAddMessage = "";
        if (this.countVirtualMachine >= 1) {
            for (int i = 0; i < this.countVMVirtualStorageList.size(); i++) {
                if (this.countVMVirtualStorageList.get(i) < 1
                        && this.fakeSCE.getGroupVirtualMachineList().get(i) != null) {
                    this.entityToAddMessage = "Add to each virtualMachine at least one virtualStorage. ";
                    return;
                }
            }
        } else {
            this.entityToAddMessage = "Add at least one virtualMachine to BusinessConfiguration";
            return;
        }

        this.buttonNext = true;
    }
}
