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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.controller.DataCenterController;
import org.cloudsimulator.domain.ontology.GroupExternalStorage;
import org.cloudsimulator.domain.ontology.GroupFirewall;
import org.cloudsimulator.domain.ontology.GroupHostMachine;
import org.cloudsimulator.domain.ontology.GroupRouter;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.SharedStorageVolume;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("dataCenterViewer")
@ConversationScoped
public class DataCenterViewer implements Serializable {

    private static final long serialVersionUID = -9031086232165340616L;
    @Inject
    private DataCenterController dataCenterController;
    private boolean buttonCreateXML;
    private String entityToAddMessage;
    private transient HtmlPanelGroup panelDataCenterBody;
    private List<HtmlPanelGroup> groupHostMachinePanelBodyList;
    private int indexHostMachine;
    private int countHostMachine;
    private List<Integer> indexHMMonitorInfoList;
    private List<Integer> indexHMLocalNetworkList;
    private List<Integer> countHMLocalNetworkList;
    private List<Integer> indexHMLocalStorageList;
    private List<Integer> countHMLocalStorageList;
    private List<Integer> indexHMSharedStorageList;
    private List<HtmlPanelGroup> groupExternalStoragePanelBodyList;
    private int indexExternalStorage;
    private List<Integer> indexESMonitorInfoList;
    private List<Integer> indexESLocalNetworkList;
    private List<Integer> countESLocalNetworkList;
    private List<Integer> indexESSharedStorageList;
    private List<HtmlPanelGroup> groupRouterPanelBodyList;
    private int indexRouter;
    private List<Integer> indexRMonitorInfoList;
    private List<Integer> indexRLocalNetworkList;
    private List<Integer> countRLocalNetworkList;
    private List<HtmlPanelGroup> groupFirewallPanelBodyList;
    private int indexFirewall;
    private List<Integer> indexFMonitorInfoList;
    private List<Integer> indexFLocalNetworkList;
    private List<Integer> countFLocalNetworkList;

    public DataCenterViewer() {
        super();
        this.groupHostMachinePanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.groupExternalStoragePanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.groupFirewallPanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.groupRouterPanelBodyList = new ArrayList<HtmlPanelGroup>();

        this.indexHMMonitorInfoList = new ArrayList<Integer>();
        this.indexHMLocalNetworkList = new ArrayList<Integer>();
        this.countHMLocalNetworkList = new ArrayList<Integer>();
        this.indexHMLocalStorageList = new ArrayList<Integer>();
        this.countHMLocalStorageList = new ArrayList<Integer>();
        this.indexHMSharedStorageList = new ArrayList<Integer>();
        this.indexESMonitorInfoList = new ArrayList<Integer>();
        this.indexESLocalNetworkList = new ArrayList<Integer>();
        this.countESLocalNetworkList = new ArrayList<Integer>();
        this.indexESSharedStorageList = new ArrayList<Integer>();
        this.indexRMonitorInfoList = new ArrayList<Integer>();
        this.indexRLocalNetworkList = new ArrayList<Integer>();
        this.countRLocalNetworkList = new ArrayList<Integer>();
        this.indexFMonitorInfoList = new ArrayList<Integer>();
        this.indexFLocalNetworkList = new ArrayList<Integer>();
        this.countFLocalNetworkList = new ArrayList<Integer>();
        controlForButtonCreateXML();
    }

    public void addPanelGroupExternalStorage() {
        this.dataCenterController.getGroupExternalStorageList().add(
                indexExternalStorage, new GroupExternalStorage());
        this.groupExternalStoragePanelBodyList.add(indexExternalStorage,
                new HtmlPanelGroup());
        this.indexESMonitorInfoList.add(indexExternalStorage, 0);
        this.indexESLocalNetworkList.add(indexExternalStorage, 0);
        this.countESLocalNetworkList.add(indexExternalStorage, 0);
        this.indexESSharedStorageList.add(indexExternalStorage, 0);
        CCUtility.setFirstPanel(this.indexExternalStorage, panelDataCenterBody,
                "groupExternalStorage", "dataCenter");
        this.indexExternalStorage++;
        controlForButtonCreateXML();
    }

    public void addPanelGroupFirewall() {
        this.dataCenterController.getGroupFirewallList().add(indexFirewall,
                new GroupFirewall());
        this.groupFirewallPanelBodyList
                .add(indexFirewall, new HtmlPanelGroup());
        this.indexFMonitorInfoList.add(indexFirewall, 0);
        this.indexFLocalNetworkList.add(indexFirewall, 0);
        this.countFLocalNetworkList.add(indexFirewall, 0);
        CCUtility.setFirstPanel(this.indexFirewall, panelDataCenterBody,
                "groupFirewall", "dataCenter");
        this.indexFirewall++;
        controlForButtonCreateXML();
    }

    public void addPanelGroupHostMachine() {
        this.dataCenterController.getGroupHostMachineList().add(
                indexHostMachine, new GroupHostMachine());
        this.groupHostMachinePanelBodyList.add(indexHostMachine,
                new HtmlPanelGroup());
        this.indexHMLocalStorageList.add(indexHostMachine, 0);
        this.countHMLocalStorageList.add(indexHostMachine, 0);
        this.indexHMMonitorInfoList.add(indexHostMachine, 0);
        this.indexHMLocalNetworkList.add(indexHostMachine, 0);
        this.countHMLocalNetworkList.add(indexHostMachine, 0);
        this.indexHMSharedStorageList.add(indexHostMachine, 0);
        CCUtility.setFirstPanel(this.indexHostMachine, panelDataCenterBody,
                "groupHostMachine", "dataCenter");
        this.indexHostMachine++;
        this.countHostMachine++;
        controlForButtonCreateXML();

    }

    public void addPanelGroupRouter() {
        this.dataCenterController.getGroupRouterList().add(indexRouter,
                new GroupRouter());
        this.groupRouterPanelBodyList.add(indexRouter, new HtmlPanelGroup());
        this.indexRMonitorInfoList.add(indexRouter, 0);
        this.indexRLocalNetworkList.add(indexRouter, 0);
        this.countRLocalNetworkList.add(indexRouter, 0);
        CCUtility.setFirstPanel(this.indexRouter, panelDataCenterBody,
                "groupRouter", "dataCenter");
        this.indexRouter++;
        controlForButtonCreateXML();
    }

    public void addPanelLocalNetworkGroupExternalStorage(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalNetwork = indexESLocalNetworkList
                .get(indexPanelParent);
        this.groupExternalStoragePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupExternalStorageList()
                .get(indexPanelParent).getLocalNetworkList()
                .add(indexLocalNetwork, new LocalNetwork());
        CCUtility.setSecondPanel(indexLocalNetwork, indexPanelParent,
                htmlPanelParent, "localNetwork", "groupExternalStorage",
                "dataCenter");
        this.indexESLocalNetworkList.set(indexPanelParent,
                indexLocalNetwork + 1);
        this.countESLocalNetworkList.set(indexPanelParent,
                this.countESLocalNetworkList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelLocalNetworkGroupFirewall(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalNetwork = indexFLocalNetworkList
                .get(indexPanelParent);
        this.groupFirewallPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.dataCenterController.getGroupFirewallList().get(indexPanelParent)
                .getLocalNetworkList()
                .add(indexLocalNetwork, new LocalNetwork());
        CCUtility.setSecondPanel(indexLocalNetwork, indexPanelParent,
                htmlPanelParent, "localNetwork", "groupFirewall", "dataCenter");
        this.indexFLocalNetworkList
                .set(indexPanelParent, indexLocalNetwork + 1);
        this.countFLocalNetworkList.set(indexPanelParent,
                this.countFLocalNetworkList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelLocalNetworkGroupHostMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalNetwork = indexHMLocalNetworkList
                .get(indexPanelParent);
        this.groupHostMachinePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupHostMachineList()
                .get(indexPanelParent).getLocalNetworkList()
                .add(indexLocalNetwork, new LocalNetwork());
        CCUtility.setSecondPanel(indexLocalNetwork, indexPanelParent,
                htmlPanelParent, "localNetwork", "groupHostMachine",
                "dataCenter");
        this.indexHMLocalNetworkList.set(indexPanelParent,
                indexLocalNetwork + 1);
        this.countHMLocalNetworkList.set(indexPanelParent,
                this.countHMLocalNetworkList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelLocalNetworkGroupRouter(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalNetwork = indexRLocalNetworkList
                .get(indexPanelParent);
        this.groupRouterPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.dataCenterController.getGroupRouterList().get(indexPanelParent)
                .getLocalNetworkList()
                .add(indexLocalNetwork, new LocalNetwork());
        CCUtility.setSecondPanel(indexLocalNetwork, indexPanelParent,
                htmlPanelParent, "localNetwork", "groupRouter", "dataCenter");
        this.indexRLocalNetworkList
                .set(indexPanelParent, indexLocalNetwork + 1);
        this.countRLocalNetworkList.set(indexPanelParent,
                this.countRLocalNetworkList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelLocalStorageGroupHostMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexLocalStorage = indexHMLocalStorageList
                .get(indexPanelParent);
        this.groupHostMachinePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupHostMachineList()
                .get(indexPanelParent).getLocalStorageList()
                .add(indexLocalStorage, new LocalStorage());
        CCUtility.setSecondPanel(indexLocalStorage, indexPanelParent,
                htmlPanelParent, "localStorage", "groupHostMachine",
                "dataCenter");
        this.indexHMLocalStorageList.set(indexPanelParent,
                indexLocalStorage + 1);
        this.countHMLocalStorageList.set(indexPanelParent,
                this.countHMLocalStorageList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelMonitorInfoGroupExternalStorage(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexMonitorInfo = indexESMonitorInfoList
                .get(indexPanelParent);
        this.groupExternalStoragePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupExternalStorageList()
                .get(indexPanelParent).getMonitorInfoList()
                .add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "groupExternalStorage",
                "dataCenter");
        this.indexESMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelMonitorInfoGroupFirewall(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexMonitorInfo = indexFMonitorInfoList
                .get(indexPanelParent);
        this.groupFirewallPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.dataCenterController.getGroupFirewallList().get(indexPanelParent)
                .getMonitorInfoList().add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "groupFirewall", "dataCenter");
        this.indexFMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelMonitorInfoGroupHostMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexMonitorInfo = indexHMMonitorInfoList
                .get(indexPanelParent);
        this.groupHostMachinePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupHostMachineList()
                .get(indexPanelParent).getMonitorInfoList()
                .add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "groupHostMachine",
                "dataCenter");
        this.indexHMMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelMonitorInfoGroupRouter(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexMonitorInfo = indexRMonitorInfoList
                .get(indexPanelParent);
        this.groupRouterPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.dataCenterController.getGroupRouterList().get(indexPanelParent)
                .getMonitorInfoList().add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "groupRouter", "dataCenter");
        this.indexRMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelSharedStorageGroupExternalStorage(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexSharedStorage = indexESSharedStorageList
                .get(indexPanelParent);
        this.groupExternalStoragePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupExternalStorageList()
                .get(indexPanelParent).getSharedStorageVolumeList()
                .add(indexSharedStorage, new SharedStorageVolume());
        CCUtility.setSecondPanel(indexSharedStorage, indexPanelParent,
                htmlPanelParent, "sharedStorageVolume", "groupExternalStorage",
                "dataCenter");
        this.indexESSharedStorageList.set(indexPanelParent,
                indexSharedStorage + 1);
    }

    public void addInputTextSharedStorageGroupHostMachine(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final int indexSharedStorage = indexHMSharedStorageList
                .get(indexPanelParent);
        this.groupHostMachinePanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.dataCenterController.getGroupHostMachineList()
                .get(indexPanelParent).getSharedStorageList()
                .add(indexSharedStorage, "");
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = "ccInputTextSharedStorage" + indexSharedStorage;
        mapValueExpression.put("preAddon", CCUtility.createValueExpression(
                "urn:cloudicaro:SharedStorageVolume:", String.class));
        mapValueExpression.put("placeholder", CCUtility.createValueExpression(
                "Insert localUri of SharedStorage used by this host ",
                String.class));
        mapValueExpression.put("value", CCUtility.createValueExpression(
                "#{dataCenterController.groupHostMachineList.get("
                        + indexPanelParent + ").sharedStorageList["
                        + indexSharedStorage + "]}", Object.class));
        CCUtility.includeCompositeComponent(htmlPanelParent, "inputComponent",
                "inputTextBS.xhtml", idCC, mapValueExpression);
        this.indexHMSharedStorageList.set(indexPanelParent,
                indexSharedStorage + 1);
    }

    public void removePanelGroupExternalStorage(final String panelToRemove,
            final Integer indexPanelToRemove) {
        final UIComponent uiPanelToRemove = this.panelDataCenterBody
                .findComponent(panelToRemove);
        this.panelDataCenterBody.getChildren().remove(uiPanelToRemove);
        this.groupExternalStoragePanelBodyList.set(indexPanelToRemove, null);
        this.dataCenterController.getGroupExternalStorageList().set(
                indexPanelToRemove, null);
        this.indexESLocalNetworkList.set(indexPanelToRemove, null);
        this.indexESMonitorInfoList.set(indexPanelToRemove, null);
        controlForButtonCreateXML();
    }

    public void removePanelGroupFirewall(final String panelToRemove,
            final Integer indexPanelToRemove) {
        final UIComponent uiPanelToRemove = this.panelDataCenterBody
                .findComponent(panelToRemove);
        this.panelDataCenterBody.getChildren().remove(uiPanelToRemove);
        this.groupFirewallPanelBodyList.set(indexPanelToRemove, null);
        this.dataCenterController.getGroupFirewallList().set(
                indexPanelToRemove, null);
        this.indexFLocalNetworkList.set(indexPanelToRemove, null);
        this.indexFMonitorInfoList.set(indexPanelToRemove, null);
        controlForButtonCreateXML();
    }

    public void removePanelGroupHostMachine(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        final UIComponent uiPanelToRemove = this.panelDataCenterBody
                .findComponent(idPanelToRemove);
        this.panelDataCenterBody.getChildren().remove(uiPanelToRemove);
        this.groupHostMachinePanelBodyList.set(indexPanelToRemove, null);
        this.dataCenterController.getGroupHostMachineList().set(
                indexPanelToRemove, null);
        this.indexHMLocalNetworkList.set(indexPanelToRemove, null);
        this.indexHMMonitorInfoList.set(indexPanelToRemove, null);
        this.countHostMachine--;
        controlForButtonCreateXML();
    }

    public void removePanelGroupRouter(final String panelToRemove,
            final Integer indexPanelToRemove) {
        final UIComponent uiPanelToRemove = this.panelDataCenterBody
                .findComponent(panelToRemove);
        this.panelDataCenterBody.getChildren().remove(uiPanelToRemove);
        this.groupRouterPanelBodyList.set(indexPanelToRemove, null);
        this.dataCenterController.getGroupRouterList().set(indexPanelToRemove,
                null);
        this.indexRLocalNetworkList.set(indexPanelToRemove, null);
        this.indexRMonitorInfoList.set(indexPanelToRemove, null);
        controlForButtonCreateXML();
    }

    public void removePanelLocalNetworkGroupExternalStorage(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupExternalStoragePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupExternalStorageList()
                        .get(indexPanelParent).getLocalNetworkList()
                        .set(indexPanelToRemove, null);
                this.countESLocalNetworkList.set(indexPanelParent,
                        this.countESLocalNetworkList.get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelLocalNetworkGroupFirewall(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupFirewallPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupFirewallList()
                        .get(indexPanelParent).getLocalNetworkList()
                        .set(indexPanelToRemove, null);
                this.countFLocalNetworkList.set(indexPanelParent,
                        this.countFLocalNetworkList.get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelLocalNetworkGroupHostMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupHostMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupHostMachineList()
                        .get(indexPanelParent).getLocalNetworkList()
                        .set(indexPanelToRemove, null);
                this.countHMLocalNetworkList.set(indexPanelParent,
                        this.countHMLocalNetworkList.get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelLocalNetworkGroupRouter(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupRouterPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupRouterList()
                        .get(indexPanelParent).getLocalNetworkList()
                        .set(indexPanelToRemove, null);
                this.countRLocalNetworkList.set(indexPanelParent,
                        this.countRLocalNetworkList.get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelLocalStorageGroupHostMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupHostMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupHostMachineList()
                        .get(indexPanelParent).getLocalStorageList()
                        .set(indexPanelToRemove, null);
                this.countHMLocalStorageList.set(indexPanelParent,
                        this.countHMLocalStorageList.get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelMonitorInfoGroupExternalStorage(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupExternalStoragePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupExternalStorageList()
                        .get(indexPanelParent).getMonitorInfoList()
                        .set(indexPanelToRemove, null);

            }
        }
    }

    public void removePanelMonitorInfoGroupFirewall(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupFirewallPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupFirewallList()
                        .get(indexPanelParent).getMonitorInfoList()
                        .set(indexPanelToRemove, null);
            }

        }
    }

    public void removePanelMonitorInfoGroupHostMachine(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupHostMachinePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupHostMachineList()
                        .get(indexPanelParent).getMonitorInfoList()
                        .set(indexPanelToRemove, null);
            }

        }
    }

    public void removePanelMonitorInfoGroupRouter(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupRouterPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupRouterList()
                        .get(indexPanelParent).getMonitorInfoList()
                        .set(indexPanelToRemove, null);
            }

        }
    }

    public void removePanelSharedStorageVolumeGroupExternalStorage(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.groupExternalStoragePanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.dataCenterController.getGroupExternalStorageList()
                        .get(indexPanelParent).getSharedStorageVolumeList()
                        .set(indexPanelToRemove, null);
            }
        }
    }

    private void controlForButtonCreateXML() {
        this.buttonCreateXML = false;
        this.entityToAddMessage = "";
        if (this.countHostMachine >= 1) {
            for (int i = 0; i < this.countHMLocalStorageList.size(); i++) {
                if (this.countHMLocalStorageList.get(i) < 1
                        && this.dataCenterController.getGroupHostMachineList()
                                .get(i) != null) {
                    this.entityToAddMessage = "Add to each hostMachine at least one  localStorage. ";
                    return;
                }
            }
            for (int i = 0; i < this.countHMLocalNetworkList.size(); i++) {
                if (this.countHMLocalNetworkList.get(i) < 1
                        && this.dataCenterController.getGroupHostMachineList()
                                .get(i) != null) {
                    this.entityToAddMessage = "Add to each hostMachine at least one localNetwork. ";
                    return;
                }
            }
            for (int i = 0; i < this.countESLocalNetworkList.size(); i++) {
                if (this.countESLocalNetworkList.get(i) < 1
                        && this.dataCenterController
                                .getGroupExternalStorageList().get(i) != null) {
                    this.entityToAddMessage = "Add to each externalStorage at least one localNetwork. ";
                    return;
                }
            }
            for (int i = 0; i < this.countFLocalNetworkList.size(); i++) {
                if (this.countFLocalNetworkList.get(i) < 1
                        && this.dataCenterController.getGroupFirewallList()
                                .get(i) != null) {
                    this.entityToAddMessage = "Add to each firewall at least one localNetwork. ";
                    return;
                }
            }
            for (int i = 0; i < this.countRLocalNetworkList.size(); i++) {
                if (this.countRLocalNetworkList.get(i) < 1
                        && this.dataCenterController.getGroupRouterList()
                                .get(i) != null) {
                    this.entityToAddMessage = "Add to each router at least one localNetwork. ";
                    return;
                }
            }
            this.buttonCreateXML = true;
        } else {
            this.entityToAddMessage = "Add at least one hostMachine to DataCenter";
        }
    }

    public String dataCenterViewClear() {
        panelDataCenterBody.getChildren().clear();
        return "dataCenter";
    }

    public boolean isButtonCreateXML() {
        return buttonCreateXML;
    }

    public String getEntityToAddMessage() {
        return entityToAddMessage;
    }

    public HtmlPanelGroup getPanelDataCenterBody() {
        return this.panelDataCenterBody;
    }

    public void setPanelDataCenterBody(HtmlPanelGroup panelDataCenterBody) {
        this.panelDataCenterBody = panelDataCenterBody;
    }

    public List<HtmlPanelGroup> getGroupHostMachinePanelBodyList() {
        return this.groupHostMachinePanelBodyList;
    }

    public void setGroupHostMachinePanelBodyList(
            List<HtmlPanelGroup> groupHostMachinePanelBodyList) {
        this.groupHostMachinePanelBodyList = groupHostMachinePanelBodyList;
    }

    public List<HtmlPanelGroup> getGroupExternalStoragePanelBodyList() {
        return this.groupExternalStoragePanelBodyList;
    }

    public void setGroupExternalStoragePanelBodyList(
            List<HtmlPanelGroup> groupExternalStoragePanelBodyList) {
        this.groupExternalStoragePanelBodyList = groupExternalStoragePanelBodyList;
    }

    public List<HtmlPanelGroup> getGroupRouterPanelBodyList() {
        return this.groupRouterPanelBodyList;
    }

    public void setGroupRouterPanelBodyList(
            List<HtmlPanelGroup> groupRouterPanelBodyList) {
        this.groupRouterPanelBodyList = groupRouterPanelBodyList;
    }

    public List<HtmlPanelGroup> getGroupFirewallPanelBodyList() {
        return this.groupFirewallPanelBodyList;
    }

    public void setGroupFirewallPanelBodyList(
            List<HtmlPanelGroup> groupFirewallPanelBodyList) {
        this.groupFirewallPanelBodyList = groupFirewallPanelBodyList;
    }

}
