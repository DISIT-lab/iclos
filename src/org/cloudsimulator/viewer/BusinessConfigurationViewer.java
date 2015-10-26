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

import org.cloudsimulator.controller.BusinessConfigurationController;
import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.domain.ontology.IcaroService;
import org.cloudsimulator.domain.ontology.IcaroTenant;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.SLAction;
import org.cloudsimulator.domain.ontology.SLAgreement;
import org.cloudsimulator.domain.ontology.SLMetric;
import org.cloudsimulator.domain.ontology.SLObjective;
import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("businessConfigurationViewer")
@ConversationScoped
public class BusinessConfigurationViewer implements Serializable {

    private static final long serialVersionUID = 2736989737825705221L;
    @Inject
    private BusinessConfigurationController businessConfigurationController;
    private boolean buttonCreateXML;
    private String entityToAddMessage;
    private transient HtmlPanelGroup panelBusinessConfigurationBody;
    private transient List<HtmlPanelGroup> icaroApplicationPanelBodyList;
    private transient List<HtmlPanelGroup> icaroTenantPanelBodyList;
    private transient List<HtmlPanelGroup> slAgreementPanelBodyList;
    private transient List<HtmlPanelGroup> creatorPanelBodyList;
    private transient List<List<HtmlPanelGroup>> icaroServicePanelBodyList;
    private transient List<List<HtmlPanelGroup>> slAgreementIcaroApplicationPanelBodyList;
    private transient List<List<HtmlPanelGroup>> slAgreementIcaroTenantPanelBodyList;
    private transient List<List<HtmlPanelGroup>> slObjectiveSlAgreementPanelBodyList;
    private transient List<List<List<HtmlPanelGroup>>> slObjectiveSlAgreementIAPanelBodyList;
    private transient List<List<List<HtmlPanelGroup>>> slObjectiveSlAgreementITPanelBodyList;
    private int indexIcaroApplication;
    private int countIcaroApplication;
    private int indexIcaroTenant;
    private int countIcaroTenant;
    private int indexSlAgreement;
    private int countSlAgreement;
    private int indexCreator;
    private int countCreator;
    private List<Integer> indexIAIcaroServiceList;
    private List<Integer> countIAIcaroServiceList;
    private List<Integer> indexIASlAgreementList;
    private List<Integer> countIASlAgreementList;
    private List<Integer> indexIACreatorList;
    private List<Integer> countIACreatorList;
    private List<Integer> indexITMonitorInfoList;
    private List<Integer> indexITSlAgreementList;
    private List<Integer> countITSlAgreementList;
    private List<Integer> indexSASlObjectiveList;
    private List<Integer> countSASlObjectiveList;
    private List<List<Integer>> indexIAISMonitorInfoList;
    private List<List<Integer>> indexIAISTcpPortList;
    private List<List<Integer>> indexIAISUdpPortList;
    private List<List<Integer>> indexIASASlObjectiveList;
    private List<List<Integer>> countIASASlObjectiveList;
    private List<List<Integer>> indexITSASlObjectiveList;
    private List<List<Integer>> countITSASlObjectiveList;
    private List<List<Integer>> indexSASOSlActionList;
    private List<List<Integer>> countSASOSlActionList;
    private List<List<Integer>> indexSASOSlMetricList;
    private List<List<Integer>> countSASOSlMetricList;
    private List<List<List<Integer>>> indexIASASOSlActionList;
    private List<List<List<Integer>>> countIASASOSlActionList;
    private List<List<List<Integer>>> indexIASASOSlMetricList;
    private List<List<List<Integer>>> countIASASOSlMetricList;
    private List<List<List<Integer>>> indexITSASOSlActionList;
    private List<List<List<Integer>>> countITSASOSlActionList;
    private List<List<List<Integer>>> indexITSASOSlMetricList;
    private List<List<List<Integer>>> countITSASOSlMetricList;

    public BusinessConfigurationViewer() {
        super();
        initReset();
    }

    private void initReset() {
        this.icaroApplicationPanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.icaroTenantPanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.slAgreementPanelBodyList = new ArrayList<HtmlPanelGroup>();
        this.creatorPanelBodyList = new ArrayList<HtmlPanelGroup>();

        this.slObjectiveSlAgreementPanelBodyList = new ArrayList<List<HtmlPanelGroup>>();
        this.icaroServicePanelBodyList = new ArrayList<List<HtmlPanelGroup>>();
        this.slAgreementIcaroApplicationPanelBodyList = new ArrayList<List<HtmlPanelGroup>>();
        this.slObjectiveSlAgreementIAPanelBodyList = new ArrayList<List<List<HtmlPanelGroup>>>();
        this.slAgreementIcaroTenantPanelBodyList = new ArrayList<List<HtmlPanelGroup>>();
        this.slObjectiveSlAgreementITPanelBodyList = new ArrayList<List<List<HtmlPanelGroup>>>();

        this.indexIAIcaroServiceList = new ArrayList<Integer>();
        this.countIAIcaroServiceList = new ArrayList<Integer>();
        this.indexIASlAgreementList = new ArrayList<Integer>();
        this.countIASlAgreementList = new ArrayList<Integer>();
        this.indexIACreatorList = new ArrayList<Integer>();
        this.countIACreatorList = new ArrayList<Integer>();

        this.indexITMonitorInfoList = new ArrayList<Integer>();
        this.indexITSlAgreementList = new ArrayList<Integer>();
        this.countITSlAgreementList = new ArrayList<Integer>();

        this.indexSASlObjectiveList = new ArrayList<Integer>();
        this.countSASlObjectiveList = new ArrayList<Integer>();

        this.indexIAISMonitorInfoList = new ArrayList<List<Integer>>();
        this.indexIAISTcpPortList = new ArrayList<List<Integer>>();
        this.indexIAISUdpPortList = new ArrayList<List<Integer>>();
        this.indexSASOSlActionList = new ArrayList<List<Integer>>();
        this.countSASOSlActionList = new ArrayList<List<Integer>>();
        this.indexSASOSlMetricList = new ArrayList<List<Integer>>();
        this.countSASOSlMetricList = new ArrayList<List<Integer>>();

        this.indexIASASlObjectiveList = new ArrayList<List<Integer>>();
        this.countIASASlObjectiveList = new ArrayList<List<Integer>>();
        this.indexITSASlObjectiveList = new ArrayList<List<Integer>>();
        this.countITSASlObjectiveList = new ArrayList<List<Integer>>();

        this.indexIASASOSlActionList = new ArrayList<List<List<Integer>>>();
        this.countIASASOSlActionList = new ArrayList<List<List<Integer>>>();
        this.indexIASASOSlMetricList = new ArrayList<List<List<Integer>>>();
        this.countIASASOSlMetricList = new ArrayList<List<List<Integer>>>();
        this.indexITSASOSlActionList = new ArrayList<List<List<Integer>>>();
        this.countITSASOSlActionList = new ArrayList<List<List<Integer>>>();
        this.indexITSASOSlMetricList = new ArrayList<List<List<Integer>>>();
        this.countITSASOSlMetricList = new ArrayList<List<List<Integer>>>();

        indexIcaroApplication = 0;
        countIcaroApplication = 0;
        indexIcaroTenant = 0;
        countIcaroTenant = 0;
        indexSlAgreement = 0;
        countSlAgreement = 0;
        indexCreator = 0;
        countCreator = 0;

        controlForButtonCreateXML();
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addInputNumberTcpPortIcaroService(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexTcpPort = this.indexIAISTcpPortList.get(
                indexPanelGParent).get(indexPanelParent);

        this.icaroServicePanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGParent)
                .getIcaroServiceList().get(indexPanelParent)
                .getUsesTcpPortList().add(indexTcpPort, 0);
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = "ccInputNumberTcpPort" + indexTcpPort;
        mapValueExpression.put(
                "preAddon",
                CCUtility.createValueExpression("#" + (indexTcpPort + 1)
                        + " TCPPort", String.class));
        mapValueExpression.put("placeholder", CCUtility
                .createValueExpression(
                        "Insert number of TCP port used by icaro service",
                        String.class));
        mapValueExpression.put("value", CCUtility.createValueExpression(
                "#{businessConfigurationController.businessConfiguration.icaroApplicationList["
                        + indexPanelGParent + "].icaroServiceList["
                        + indexPanelParent + "].usesTcpPortList["
                        + indexTcpPort + "]}", Object.class));
        mapValueExpression.put("converterId", CCUtility.createValueExpression(
                "DigitsOnlyConverter", String.class));
        mapValueExpression.put("converterMessage", CCUtility
                .createValueExpression("You can only insert digit.",
                        String.class));
        mapValueExpression.put("minimumValue",
                CCUtility.createValueExpression("0", Long.class));
        mapValueExpression.put("maximumValue",
                CCUtility.createValueExpression("65535", Long.class));
        CCUtility.includeCompositeComponent(htmlPanelParent, "inputComponent",
                "inputNumberBS.xhtml", idCC, mapValueExpression);
        this.indexIAISTcpPortList.get(indexPanelGParent).set(indexPanelParent,
                indexTcpPort + 1);
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addInputNumberUdpPortIcaroService(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexUdpPort = this.indexIAISUdpPortList.get(
                indexPanelGParent).get(indexPanelParent);

        this.icaroServicePanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGParent)
                .getIcaroServiceList().get(indexPanelParent)
                .getUsesUdpPortList().add(indexUdpPort, 0);
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = "ccInputNumberUdpPort" + indexUdpPort;
        mapValueExpression.put(
                "preAddon",
                CCUtility.createValueExpression("#" + (indexUdpPort + 1)
                        + " UDPPort", String.class));
        mapValueExpression.put("placeholder", CCUtility
                .createValueExpression(
                        "Insert number of UDP port used by icaro service",
                        String.class));
        mapValueExpression.put("value", CCUtility.createValueExpression(
                "#{businessConfigurationController.businessConfiguration.icaroApplicationList["
                        + indexPanelGParent + "].icaroServiceList["
                        + indexPanelParent + "].usesUdpPortList["
                        + indexUdpPort + "]}", Object.class));
        mapValueExpression.put("converterId", CCUtility.createValueExpression(
                "DigitsOnlyConverter", String.class));
        mapValueExpression.put("converterMessage", CCUtility
                .createValueExpression("You can only insert digit.",
                        String.class));
        mapValueExpression.put("minimumValue",
                CCUtility.createValueExpression("0", Long.class));
        mapValueExpression.put("maximumValue",
                CCUtility.createValueExpression("65535", Long.class));
        CCUtility.includeCompositeComponent(htmlPanelParent, "inputComponent",
                "inputNumberBS.xhtml", idCC, mapValueExpression);
        this.indexIAISUdpPortList.get(indexPanelGParent).set(indexPanelParent,
                indexUdpPort + 1);

    }

    public void addPanelCreator() {
        if (countCreator == 0) {
            this.creatorPanelBodyList.add(indexCreator, new HtmlPanelGroup());
            this.businessConfigurationController.getBusinessConfiguration()
                    .getCreatorList().add(indexCreator, new User());
            CCUtility.setFirstPanel(indexCreator,
                    panelBusinessConfigurationBody, "creator",
                    "businessConfiguration");
            this.indexCreator++;
            this.countCreator++;
            controlForButtonCreateXML();

        }
    }

    public void addPanelCreatorIcaroApplication(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        if (this.countIACreatorList.get(indexPanelParent) == 0) {
            final Integer indexCreatorLocal = this.indexIACreatorList
                    .get(indexPanelParent);
            this.icaroApplicationPanelBodyList.set(indexPanelParent,
                    htmlPanelParent);
            this.businessConfigurationController.getBusinessConfiguration()
                    .getIcaroApplicationList().get(indexPanelParent)
                    .getCreatorList().add(indexCreatorLocal, new User());
            CCUtility.setSecondPanel(indexCreatorLocal, indexPanelParent,
                    htmlPanelParent, "creator", "icaroApplication",
                    "businessConfiguration");
            this.indexIACreatorList
                    .set(indexPanelParent, indexCreatorLocal + 1);
            this.countIACreatorList.set(indexPanelParent,
                    this.countIACreatorList.get(indexPanelParent) + 1);
            controlForButtonCreateXML();
        }
    }

    public void addPanelIcaroApplication() {
        this.icaroApplicationPanelBodyList.add(indexIcaroApplication,
                new HtmlPanelGroup());
        this.icaroServicePanelBodyList.add(indexIcaroApplication,
                new ArrayList<HtmlPanelGroup>());

        this.slAgreementIcaroApplicationPanelBodyList.add(
                indexIcaroApplication, new ArrayList<HtmlPanelGroup>());
        this.slObjectiveSlAgreementIAPanelBodyList.add(indexIcaroApplication,
                new ArrayList<List<HtmlPanelGroup>>());

        this.indexIAIcaroServiceList.add(indexIcaroApplication, 0);
        this.countIAIcaroServiceList.add(indexIcaroApplication, 0);

        this.indexIASlAgreementList.add(indexIcaroApplication, 0);
        this.countIASlAgreementList.add(indexIcaroApplication, 0);
        this.indexIACreatorList.add(indexIcaroApplication, 0);
        this.countIACreatorList.add(indexIcaroApplication, 0);

        this.indexIAISMonitorInfoList.add(indexIcaroApplication,
                new ArrayList<Integer>());
        this.indexIAISTcpPortList.add(indexIcaroApplication,
                new ArrayList<Integer>());
        this.indexIAISUdpPortList.add(indexIcaroApplication,
                new ArrayList<Integer>());
        this.indexIASASlObjectiveList.add(indexIcaroApplication,
                new ArrayList<Integer>());
        this.countIASASlObjectiveList.add(indexIcaroApplication,
                new ArrayList<Integer>());

        this.indexIASASOSlActionList.add(indexIcaroApplication,
                new ArrayList<List<Integer>>());
        this.countIASASOSlActionList.add(indexIcaroApplication,
                new ArrayList<List<Integer>>());
        this.indexIASASOSlMetricList.add(indexIcaroApplication,
                new ArrayList<List<Integer>>());
        this.countIASASOSlMetricList.add(indexIcaroApplication,
                new ArrayList<List<Integer>>());

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList()
                .add(indexIcaroApplication, new IcaroApplication());
        CCUtility.setFirstPanel(indexIcaroApplication,
                panelBusinessConfigurationBody, "icaroApplication",
                "businessConfiguration");
        this.indexIcaroApplication++;
        this.countIcaroApplication++;
        controlForButtonCreateXML();
    }

    public void addPanelIcaroServiceIcaroApplication(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final Integer indexIcaroService = this.indexIAIcaroServiceList
                .get(indexPanelParent);

        this.icaroApplicationPanelBodyList.set(indexPanelParent,
                htmlPanelParent);
        this.icaroServicePanelBodyList.get(indexPanelParent).add(
                indexIcaroService, new HtmlPanelGroup());

        this.indexIAISMonitorInfoList.get(indexPanelParent).add(
                indexIcaroService, 0);
        this.indexIAISTcpPortList.get(indexPanelParent).add(indexIcaroService,
                0);
        this.indexIAISUdpPortList.get(indexPanelParent).add(indexIcaroService,
                0);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelParent)
                .getIcaroServiceList()
                .add(indexIcaroService, new IcaroService());
        CCUtility.setSecondPanel(indexIcaroService, indexPanelParent,
                htmlPanelParent, "icaroService", "icaroApplication",
                "businessConfiguration");
        this.indexIAIcaroServiceList.set(indexPanelParent,
                indexIcaroService + 1);
        this.countIAIcaroServiceList.set(indexPanelParent,
                this.countIAIcaroServiceList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelIcaroTenant() {
        this.icaroTenantPanelBodyList.add(indexIcaroTenant,
                new HtmlPanelGroup());
        this.slAgreementIcaroTenantPanelBodyList.add(indexIcaroTenant,
                new ArrayList<HtmlPanelGroup>());
        this.slObjectiveSlAgreementITPanelBodyList.add(indexIcaroTenant,
                new ArrayList<List<HtmlPanelGroup>>());

        this.indexITMonitorInfoList.add(indexIcaroTenant, 0);
        this.indexITSlAgreementList.add(indexIcaroTenant, 0);
        this.countITSlAgreementList.add(indexIcaroTenant, 0);
        this.indexITSASlObjectiveList.add(indexIcaroTenant,
                new ArrayList<Integer>());
        this.countITSASlObjectiveList.add(indexIcaroTenant,
                new ArrayList<Integer>());
        this.indexITSASOSlActionList.add(indexIcaroTenant,
                new ArrayList<List<Integer>>());
        this.countITSASOSlActionList.add(indexIcaroTenant,
                new ArrayList<List<Integer>>());
        this.indexITSASOSlMetricList.add(indexIcaroTenant,
                new ArrayList<List<Integer>>());
        this.countITSASOSlMetricList.add(indexIcaroTenant,
                new ArrayList<List<Integer>>());

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().add(indexIcaroTenant, new IcaroTenant());
        CCUtility.setFirstPanel(indexIcaroTenant,
                panelBusinessConfigurationBody, "icaroTenant",
                "businessConfiguration");
        this.indexIcaroTenant++;
        this.countIcaroTenant++;
    }

    public void addPanelMonitorInfoIcaroServiceIA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexMonitorInfo = this.indexIAISMonitorInfoList.get(
                indexPanelGParent).get(indexPanelParent);
        this.icaroServicePanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);
        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGParent)
                .getIcaroServiceList().get(indexPanelParent)
                .getMonitorInfoList().add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setThirdPanel(indexMonitorInfo, indexPanelParent,
                indexPanelGParent, htmlPanelParent, "monitorInfo",
                "icaroService", "icaroApplication", "businessConfiguration",
                "ia");
        this.indexIAISMonitorInfoList.get(indexPanelGParent).set(
                indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelMonitorInfoIcaroTenant(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final Integer indexMonitorInfo = this.indexITMonitorInfoList
                .get(indexPanelParent);
        this.icaroTenantPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().get(indexPanelParent)
                .getMonitorInfoList().add(indexMonitorInfo, new MonitorInfo());
        CCUtility.setSecondPanel(indexMonitorInfo, indexPanelParent,
                htmlPanelParent, "monitorInfo", "icaroTenant",
                "businessConfiguration");
        this.indexITMonitorInfoList.set(indexPanelParent, indexMonitorInfo + 1);
    }

    public void addPanelSlActionSlObjectiveSA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexSlAction = this.indexSASOSlActionList.get(
                indexPanelGParent).get(indexPanelParent);

        this.slObjectiveSlAgreementPanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlActionList()
                .add(indexSlAction, new SLAction());
        CCUtility.setThirdPanel(indexSlAction, indexPanelParent,
                indexPanelGParent, htmlPanelParent, "slAction", "slObjective",
                "slAgreement", "businessConfiguration", "sa");
        this.indexSASOSlActionList.get(indexPanelGParent).set(indexPanelParent,
                indexSlAction + 1);
        this.countSASOSlActionList.get(indexPanelGParent).set(
                indexPanelParent,
                this.countSASOSlActionList.get(indexPanelGParent).get(
                        indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addPanelSlActionSlObjectiveIASA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGGParent, final Integer indexPanelGParent,
            final Integer indexPanelParent) {
        final Integer indexSlAction = this.indexIASASOSlActionList
                .get(indexPanelGGParent).get(indexPanelGParent)
                .get(indexPanelParent);

        this.slObjectiveSlAgreementIAPanelBodyList.get(indexPanelGGParent)
                .get(indexPanelGParent).set(indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGGParent)
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlActionList()
                .add(indexSlAction, new SLAction());
        CCUtility.setForthPanel(indexSlAction, indexPanelParent,
                indexPanelGParent, indexPanelGGParent, htmlPanelParent,
                "slAction", "slObjective", "slAgreement", "icaroApplication",
                "businessConfiguration", "iasa");
        this.indexIASASOSlActionList.get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent, indexSlAction + 1);
        this.countIASASOSlActionList
                .get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent,
                        this.countIASASOSlActionList.get(indexPanelGGParent)
                                .get(indexPanelGParent).get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addPanelSlActionSlObjectiveITSA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGGParent, final Integer indexPanelGParent,
            final Integer indexPanelParent) {
        final Integer indexSlAction = this.indexITSASOSlActionList
                .get(indexPanelGGParent).get(indexPanelGParent)
                .get(indexPanelParent);

        this.slObjectiveSlAgreementITPanelBodyList.get(indexPanelGGParent)
                .get(indexPanelGParent).set(indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().get(indexPanelGGParent)
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlActionList()
                .add(indexSlAction, new SLAction());
        CCUtility.setForthPanel(indexSlAction, indexPanelParent,
                indexPanelGParent, indexPanelGGParent, htmlPanelParent,
                "slAction", "slObjective", "slAgreement", "icaroTenant",
                "businessConfiguration", "itsa");
        this.indexITSASOSlActionList.get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent, indexSlAction + 1);
        this.countITSASOSlActionList
                .get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent,
                        this.countITSASOSlActionList.get(indexPanelGGParent)
                                .get(indexPanelGParent).get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelSlAgreement() {

        if (countSlAgreement == 0) {

            this.slAgreementPanelBodyList.add(indexSlAgreement,
                    new HtmlPanelGroup());
            this.slObjectiveSlAgreementPanelBodyList.add(indexSlAgreement,
                    new ArrayList<HtmlPanelGroup>());

            this.indexSASlObjectiveList.add(indexSlAgreement, 0);
            this.countSASlObjectiveList.add(indexSlAgreement, 0);

            this.indexSASOSlActionList.add(indexSlAgreement,
                    new ArrayList<Integer>());
            this.countSASOSlActionList.add(indexSlAgreement,
                    new ArrayList<Integer>());
            this.indexSASOSlMetricList.add(indexSlAgreement,
                    new ArrayList<Integer>());
            this.countSASOSlMetricList.add(indexSlAgreement,
                    new ArrayList<Integer>());

            this.businessConfigurationController.getBusinessConfiguration()
                    .getSlAgreementList()
                    .add(indexSlAgreement, new SLAgreement());
            CCUtility.setFirstPanel(indexSlAgreement,
                    panelBusinessConfigurationBody, "slAgreement",
                    "businessConfiguration");
            this.indexSlAgreement++;
            this.countSlAgreement++;
            controlForButtonCreateXML();
        }
    }

    public void addPanelSlAgreementIcaroApplication(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        if (this.countIASlAgreementList.get(indexPanelParent) == 0) {

            final Integer indexIASlAgreement = this.indexIASlAgreementList
                    .get(indexPanelParent);

            this.icaroApplicationPanelBodyList.set(indexPanelParent,
                    htmlPanelParent);
            this.slAgreementIcaroApplicationPanelBodyList.get(indexPanelParent)
                    .add(indexIASlAgreement, new HtmlPanelGroup());
            this.slObjectiveSlAgreementIAPanelBodyList.get(indexPanelParent)
                    .add(indexIASlAgreement, new ArrayList<HtmlPanelGroup>());

            this.indexIASASlObjectiveList.get(indexPanelParent).add(
                    indexIASlAgreement, 0);
            this.countIASASlObjectiveList.get(indexPanelParent).add(
                    indexIASlAgreement, 0);
            this.indexIASASOSlActionList.get(indexPanelParent).add(
                    indexIASlAgreement, new ArrayList<Integer>());
            this.countIASASOSlActionList.get(indexPanelParent).add(
                    indexIASlAgreement, new ArrayList<Integer>());
            this.indexIASASOSlMetricList.get(indexPanelParent).add(
                    indexIASlAgreement, new ArrayList<Integer>());
            this.countIASASOSlMetricList.get(indexPanelParent).add(
                    indexIASlAgreement, new ArrayList<Integer>());

            this.businessConfigurationController.getBusinessConfiguration()
                    .getIcaroApplicationList().get(indexPanelParent)
                    .getSlAgreementList()
                    .add(indexIASlAgreement, new SLAgreement());
            CCUtility.setSecondPanel(indexIASlAgreement, indexPanelParent,
                    htmlPanelParent, "slAgreement", "icaroApplication",
                    "businessConfiguration");
            this.indexIASlAgreementList.set(indexPanelParent,
                    indexIASlAgreement + 1);
            this.countIASlAgreementList.set(indexPanelParent,
                    this.countIASlAgreementList.get(indexPanelParent) + 1);
            controlForButtonCreateXML();
        }
    }

    public void addPanelSlAgreementIcaroTenant(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        if (this.countITSlAgreementList.get(indexPanelParent) == 0) {

            final Integer indexITSlAgreement = this.indexITSlAgreementList
                    .get(indexPanelParent);

            this.icaroTenantPanelBodyList
                    .set(indexPanelParent, htmlPanelParent);
            this.slAgreementIcaroTenantPanelBodyList.get(indexPanelParent).add(
                    indexITSlAgreement, new HtmlPanelGroup());
            this.slObjectiveSlAgreementITPanelBodyList.get(indexPanelParent)
                    .add(indexITSlAgreement, new ArrayList<HtmlPanelGroup>());

            this.indexITSASlObjectiveList.get(indexPanelParent).add(
                    indexITSlAgreement, 0);
            this.countITSASlObjectiveList.get(indexPanelParent).add(
                    indexITSlAgreement, 0);
            this.indexITSASOSlActionList.get(indexPanelParent).add(
                    indexITSlAgreement, new ArrayList<Integer>());
            this.countITSASOSlActionList.get(indexPanelParent).add(
                    indexITSlAgreement, new ArrayList<Integer>());
            this.indexITSASOSlMetricList.get(indexPanelParent).add(
                    indexITSlAgreement, new ArrayList<Integer>());
            this.countITSASOSlMetricList.get(indexPanelParent).add(
                    indexITSlAgreement, new ArrayList<Integer>());

            this.businessConfigurationController.getBusinessConfiguration()
                    .getIcaroTenantList().get(indexPanelParent)
                    .getSlAgreementList()
                    .add(indexITSlAgreement, new SLAgreement());
            CCUtility.setSecondPanel(indexITSlAgreement, indexPanelParent,
                    htmlPanelParent, "slAgreement", "icaroTenant",
                    "businessConfiguration");
            this.indexITSlAgreementList.set(indexPanelParent,
                    indexITSlAgreement + 1);
            this.countITSlAgreementList.set(indexPanelParent,
                    this.countITSlAgreementList.get(indexPanelParent) + 1);
            controlForButtonCreateXML();
        }
    }

    public void addPanelSlMetricSlObjectiveSA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexSlMetric = this.indexSASOSlMetricList.get(
                indexPanelGParent).get(indexPanelParent);

        this.slObjectiveSlAgreementPanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlMetricList()
                .add(indexSlMetric, new SLMetric());
        CCUtility.setThirdPanel(indexSlMetric, indexPanelParent,
                indexPanelGParent, htmlPanelParent, "slMetric", "slObjective",
                "slAgreement", "businessConfiguration", "sa");
        this.indexSASOSlMetricList.get(indexPanelGParent).set(indexPanelParent,
                indexSlMetric + 1);
        this.countSASOSlMetricList.get(indexPanelGParent).set(
                indexPanelParent,
                this.countSASOSlMetricList.get(indexPanelGParent).get(
                        indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelSlMetricSlObjectiveIASA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGGParent, final Integer indexPanelGParent,
            final Integer indexPanelParent) {
        final Integer indexSlMetric = this.indexIASASOSlMetricList
                .get(indexPanelGGParent).get(indexPanelGParent)
                .get(indexPanelParent);

        this.slObjectiveSlAgreementIAPanelBodyList.get(indexPanelGGParent)
                .get(indexPanelGParent).set(indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGGParent)
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlMetricList()
                .add(indexSlMetric, new SLMetric());
        CCUtility.setForthPanel(indexSlMetric, indexPanelParent,
                indexPanelGParent, indexPanelGGParent, htmlPanelParent,
                "slMetric", "slObjective", "slAgreement", "icaroApplication",
                "businessConfiguration", "iasa");
        this.indexIASASOSlMetricList.get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent, indexSlMetric + 1);
        this.countIASASOSlMetricList
                .get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent,
                        this.countIASASOSlMetricList.get(indexPanelGGParent)
                                .get(indexPanelGParent).get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelSlMetricSlObjectiveITSA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGGParent, final Integer indexPanelGParent,
            final Integer indexPanelParent) {
        final Integer indexSlMetric = this.indexITSASOSlMetricList
                .get(indexPanelGGParent).get(indexPanelGParent)
                .get(indexPanelParent);

        this.slObjectiveSlAgreementITPanelBodyList.get(indexPanelGGParent)
                .get(indexPanelGParent).set(indexPanelParent, htmlPanelParent);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().get(indexPanelGGParent)
                .getSlAgreementList().get(indexPanelGParent)
                .getSlObjectiveList().get(indexPanelParent).getSlMetricList()
                .add(indexSlMetric, new SLMetric());
        CCUtility.setForthPanel(indexSlMetric, indexPanelParent,
                indexPanelGParent, indexPanelGGParent, htmlPanelParent,
                "slMetric", "slObjective", "slAgreement", "icaroTenant",
                "businessConfiguration", "itsa");
        this.indexITSASOSlMetricList.get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent, indexSlMetric + 1);
        this.countITSASOSlMetricList
                .get(indexPanelGGParent)
                .get(indexPanelGParent)
                .set(indexPanelParent,
                        this.countITSASOSlMetricList.get(indexPanelGGParent)
                                .get(indexPanelGParent).get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public void addPanelSlObjectiveSlAgreement(
            final HtmlPanelGroup htmlPanelParent, final Integer indexPanelParent) {
        final Integer indexSlObjective = this.indexSASlObjectiveList
                .get(indexPanelParent);

        this.slAgreementPanelBodyList.set(indexPanelParent, htmlPanelParent);
        this.slObjectiveSlAgreementPanelBodyList.get(indexPanelParent).add(
                indexSlObjective, htmlPanelParent);

        this.indexSASOSlActionList.get(indexPanelParent).add(indexSlObjective,
                0);
        this.countSASOSlActionList.get(indexPanelParent).add(indexSlObjective,
                0);
        this.indexSASOSlMetricList.get(indexPanelParent).add(indexSlObjective,
                0);
        this.countSASOSlMetricList.get(indexPanelParent).add(indexSlObjective,
                0);

        this.businessConfigurationController.getBusinessConfiguration()
                .getSlAgreementList().get(indexPanelParent)
                .getSlObjectiveList().add(indexSlObjective, new SLObjective());
        CCUtility.setSecondPanel(indexSlObjective, indexPanelParent,
                htmlPanelParent, "slObjective", "slAgreement",
                "businessConfiguration");
        this.indexSASlObjectiveList.set(indexPanelParent, indexSlObjective + 1);
        this.countSASlObjectiveList.set(indexPanelParent,
                this.countSASlObjectiveList.get(indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addPanelSlObjectiveSlAgreementIA(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexSlObjective = this.indexIASASlObjectiveList.get(
                indexPanelGParent).get(indexPanelParent);

        this.slAgreementIcaroApplicationPanelBodyList.get(indexPanelGParent)
                .set(indexPanelParent, htmlPanelParent);
        this.slObjectiveSlAgreementIAPanelBodyList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, htmlPanelParent);

        this.indexIASASOSlActionList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.countIASASOSlActionList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.indexIASASOSlMetricList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.countIASASOSlMetricList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().get(indexPanelGParent)
                .getSlAgreementList().get(indexPanelParent)
                .getSlObjectiveList().add(indexSlObjective, new SLObjective());
        CCUtility.setThirdPanel(indexSlObjective, indexPanelParent,
                indexPanelGParent, htmlPanelParent, "slObjective",
                "slAgreement", "icaroApplication", "businessConfiguration",
                "ia");
        this.indexIASASlObjectiveList.get(indexPanelGParent).set(
                indexPanelParent, indexSlObjective + 1);
        this.countIASASlObjectiveList.get(indexPanelGParent).set(
                indexPanelParent,
                this.countIASASlObjectiveList.get(indexPanelGParent).get(
                        indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    @SuppressWarnings({ "el-syntax", "el-unresolved" })
    public void addPanelSlObjectiveSlAgreementIT(
            final HtmlPanelGroup htmlPanelParent,
            final Integer indexPanelGParent, final Integer indexPanelParent) {
        final Integer indexSlObjective = this.indexITSASlObjectiveList.get(
                indexPanelGParent).get(indexPanelParent);

        this.slAgreementIcaroTenantPanelBodyList.get(indexPanelGParent).set(
                indexPanelParent, htmlPanelParent);
        this.slObjectiveSlAgreementITPanelBodyList.get(indexPanelGParent)
                .get(indexPanelParent)
                .add(indexSlObjective, new HtmlPanelGroup());

        this.indexITSASOSlActionList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.countITSASOSlActionList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.indexITSASOSlMetricList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);
        this.countITSASOSlMetricList.get(indexPanelGParent)
                .get(indexPanelParent).add(indexSlObjective, 0);

        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().get(indexPanelGParent)
                .getSlAgreementList().get(indexPanelParent)
                .getSlObjectiveList().add(indexSlObjective, new SLObjective());
        CCUtility.setThirdPanel(indexSlObjective, indexPanelParent,
                indexPanelGParent, htmlPanelParent, "slObjective",
                "slAgreement", "icaroTenant", "businessConfiguration", "it");
        this.indexITSASlObjectiveList.get(indexPanelGParent).set(
                indexPanelParent, indexSlObjective + 1);
        this.countITSASlObjectiveList.get(indexPanelGParent).set(
                indexPanelParent,
                this.countITSASlObjectiveList.get(indexPanelGParent).get(
                        indexPanelParent) + 1);
        controlForButtonCreateXML();
    }

    public String businessConfigurationViewClear() {
        panelBusinessConfigurationBody.getChildren().clear();
        initReset();
        return "businessConfiguration";
    }

    private void controlForButtonCreateXML() {
        this.buttonCreateXML = false;
        this.entityToAddMessage = "";
        if (this.countIcaroApplication >= 1) {
            for (int i = 0; i < this.countIAIcaroServiceList.size(); i++) {
                if (this.countIAIcaroServiceList.get(i) < 1
                        && this.businessConfigurationController
                                .getBusinessConfiguration()
                                .getIcaroApplicationList().get(i) != null) {
                    this.entityToAddMessage = "Add to each icaroApplication at least one icaroService. ";
                    return;
                }
            }
            for (int i = 0; i < this.countIACreatorList.size(); i++) {
                if (this.countIACreatorList.get(i) < 1
                        && this.businessConfigurationController
                                .getBusinessConfiguration()
                                .getIcaroApplicationList().get(i) != null) {
                    this.entityToAddMessage = "Add to each icaroApplication at least one creator. ";
                    return;
                }
            }
            for (int i = 0; i < this.countIASlAgreementList.size(); i++) {
                if (this.countIASlAgreementList.get(i) >= 1) {
                    for (List<Integer> countSASlObjectiveListLocal : this.countIASASlObjectiveList) {
                        for (int j = 0; j < countSASlObjectiveListLocal.size(); j++) {
                            if (countSASlObjectiveListLocal.get(j) < 1
                                    && this.businessConfigurationController
                                            .getBusinessConfiguration()
                                            .getIcaroApplicationList()
                                            .get(this.countIASASlObjectiveList
                                                    .indexOf(countSASlObjectiveListLocal))
                                            .getSlAgreementList().get(j) != null) {
                                this.entityToAddMessage = "Add to each icaroApplication's slAgreement at least one SLObjective. ";
                                return;
                            } else {
                                for (List<List<Integer>> countSASOSlMetricListLocal : this.countIASASOSlMetricList) {
                                    for (List<Integer> countSOSlMetricList : countSASOSlMetricListLocal) {
                                        for (int k = 0; k < countSOSlMetricList
                                                .size(); k++) {
                                            if (countSOSlMetricList.get(k) < 1
                                                    && this.businessConfigurationController
                                                            .getBusinessConfiguration()
                                                            .getIcaroApplicationList()
                                                            .get(this.countIASASOSlMetricList
                                                                    .indexOf(countSASOSlMetricListLocal))
                                                            .getSlAgreementList()
                                                            .get(countSASOSlMetricListLocal
                                                                    .indexOf(countSOSlMetricList))
                                                            .getSlObjectiveList()
                                                            .get(k) != null) {
                                                this.entityToAddMessage = "Add to each icaroApplication's SLObjective at least one SLMetric. ";
                                                return;
                                            }
                                        }
                                    }
                                    for (List<List<Integer>> countSASOSlActionListLocal : this.countIASASOSlActionList) {
                                        for (List<Integer> countSOSlActionList : countSASOSlActionListLocal) {
                                            for (int k = 0; k < countSOSlActionList
                                                    .size(); k++) {
                                                if (countSOSlActionList.get(k) < 1
                                                        && this.businessConfigurationController
                                                                .getBusinessConfiguration()
                                                                .getIcaroApplicationList()
                                                                .get(this.countIASASOSlActionList
                                                                        .indexOf(countSASOSlActionListLocal))
                                                                .getSlAgreementList()
                                                                .get(countSASOSlActionListLocal
                                                                        .indexOf(countSOSlActionList))
                                                                .getSlObjectiveList()
                                                                .get(k) != null) {
                                                    this.entityToAddMessage = "Add to each icaroApplication's SLObjective at least one SLAction. ";
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            this.entityToAddMessage += "Add at least one icaroApplication to BusinessConfiguration";
            return;
        }
        if (this.countIcaroTenant >= 1) {
            for (int i = 0; i < this.countITSlAgreementList.size(); i++) {
                if (this.countITSlAgreementList.get(i) >= 1) {
                    for (List<Integer> countSASlObjectiveListLocal : this.countITSASlObjectiveList) {
                        for (int j = 0; j < countSASlObjectiveListLocal.size(); j++) {
                            if (countSASlObjectiveListLocal.get(j) < 1
                                    && this.businessConfigurationController
                                            .getBusinessConfiguration()
                                            .getIcaroTenantList()
                                            .get(this.countITSASlObjectiveList
                                                    .indexOf(countSASlObjectiveListLocal))
                                            .getSlAgreementList().get(j) != null) {
                                this.entityToAddMessage = "Add to each icaroTenant's slAgreement at least one SLObjective. ";
                                return;
                            } else {
                                for (List<List<Integer>> countSASOSlMetricListLocal : this.countITSASOSlMetricList) {
                                    for (List<Integer> countSOSlMetricList : countSASOSlMetricListLocal) {
                                        for (int k = 0; k < countSOSlMetricList
                                                .size(); k++) {
                                            if (countSOSlMetricList.get(k) < 1
                                                    && this.businessConfigurationController
                                                            .getBusinessConfiguration()
                                                            .getIcaroTenantList()
                                                            .get(this.countITSASOSlMetricList
                                                                    .indexOf(countSASOSlMetricListLocal))
                                                            .getSlAgreementList()
                                                            .get(countSASOSlMetricListLocal
                                                                    .indexOf(countSOSlMetricList))
                                                            .getSlObjectiveList()
                                                            .get(k) != null) {
                                                this.entityToAddMessage = "Add to each icaroTenant's SLObjective at least one SLMetric. ";
                                                return;
                                            }
                                        }
                                    }
                                    for (List<List<Integer>> countSASOSlActionListLocal : this.countITSASOSlActionList) {
                                        for (List<Integer> countSOSlActionList : countSASOSlActionListLocal) {
                                            for (int k = 0; k < countSOSlActionList
                                                    .size(); k++) {
                                                if (countSOSlActionList.get(k) < 1
                                                        && this.businessConfigurationController
                                                                .getBusinessConfiguration()
                                                                .getIcaroTenantList()
                                                                .get(this.countITSASOSlActionList
                                                                        .indexOf(countSASOSlActionListLocal))
                                                                .getSlAgreementList()
                                                                .get(countSASOSlActionListLocal
                                                                        .indexOf(countSOSlActionList))
                                                                .getSlObjectiveList()
                                                                .get(k) != null) {
                                                    this.entityToAddMessage = "Add to each icaroTenant's SLObjective at least one SLAction. ";
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.countSlAgreement >= 1) {
            for (int i = 0; i < this.countSASlObjectiveList.size(); i++) {
                if (this.countSASlObjectiveList.get(i) < 1
                        && this.businessConfigurationController
                                .getBusinessConfiguration()
                                .getSlAgreementList().get(i) != null) {
                    this.entityToAddMessage = "Add to each businessConfiguration's slAgreement at least one SLObjective. ";
                    return;
                } else {
                    for (List<Integer> countSOSlMetricList : this.countSASOSlMetricList) {
                        for (int j = 0; j < countSOSlMetricList.size(); j++) {
                            if (countSOSlMetricList.get(j) < 1
                                    && this.businessConfigurationController
                                            .getBusinessConfiguration()
                                            .getSlAgreementList()
                                            .get(countSASOSlMetricList
                                                    .indexOf(countSOSlMetricList))
                                            .getSlObjectiveList().get(j) != null) {
                                this.entityToAddMessage = "Add to each businessConfiguration's SLObjective at least one SLMetric. ";
                                return;
                            }
                        }
                    }
                    for (List<Integer> countSOSlActionList : this.countSASOSlActionList) {
                        for (int j = 0; j < countSOSlActionList.size(); j++) {
                            if (countSOSlActionList.get(j) < 1
                                    && this.businessConfigurationController
                                            .getBusinessConfiguration()
                                            .getSlAgreementList()
                                            .get(countSASOSlActionList
                                                    .indexOf(countSOSlActionList))
                                            .getSlObjectiveList().get(j) != null) {
                                this.entityToAddMessage = "Add to each businessConfiguration's SLObjective at least one SLAction. ";
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (this.countCreator < 1) {
            this.entityToAddMessage += "Add one creator to BusinessConfiguration";
            return;
        }
        this.buttonCreateXML = true;
    }

    public int getCountCreator() {
        return countCreator;
    }

    public List<Integer> getCountIACreatorList() {
        return countIACreatorList;
    }

    public List<Integer> getCountIASlAgreementList() {
        return countIASlAgreementList;
    }

    public List<Integer> getCountITSlAgreementList() {
        return countITSlAgreementList;
    }

    public int getCountSlAgreement() {
        return countSlAgreement;
    }

    public String getEntityToAddMessage() {
        return entityToAddMessage;
    }

    public HtmlPanelGroup getPanelBusinessConfigurationBody() {
        return panelBusinessConfigurationBody;
    }

    public List<List<HtmlPanelGroup>> getPanelIAIcaroServiceBodyList() {
        return icaroServicePanelBodyList;
    }

    public List<List<List<HtmlPanelGroup>>> getPanelIASASlObjectiveBodyList() {
        return slObjectiveSlAgreementIAPanelBodyList;
    }

    public List<List<HtmlPanelGroup>> getPanelIASlAgreementBodyList() {
        return slAgreementIcaroApplicationPanelBodyList;
    }

    public List<HtmlPanelGroup> getPanelIcaroApplicationBodyList() {
        return icaroApplicationPanelBodyList;
    }

    public List<HtmlPanelGroup> getPanelIcaroTenantBodyList() {
        return icaroTenantPanelBodyList;
    }

    public List<List<List<HtmlPanelGroup>>> getPanelITSASlObjectiveBodyList() {
        return slObjectiveSlAgreementITPanelBodyList;
    }

    public List<List<HtmlPanelGroup>> getPanelITSlAgreementBodyList() {
        return slAgreementIcaroTenantPanelBodyList;
    }

    public List<List<HtmlPanelGroup>> getPanelSASlObjectiveBodyList() {
        return slObjectiveSlAgreementPanelBodyList;
    }

    public List<HtmlPanelGroup> getPanelSlAgreementBodyList() {
        return slAgreementPanelBodyList;
    }

    public boolean isButtonCreateXML() {
        return buttonCreateXML;
    }

    public void removePanelCreator(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        this.creatorPanelBodyList.set(indexPanelToRemove, null);
        this.businessConfigurationController.getBusinessConfiguration()
                .getCreatorList().set(indexPanelToRemove, null);
        this.countCreator--;
        controlForButtonCreateXML();
    }

    public void removePanelCreatorIcaroApplication(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroApplicationPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelParent)
                        .getCreatorList().set(indexPanelToRemove, null);
                this.countIACreatorList.set(indexPanelParent,
                        this.countIACreatorList.get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelIcaroApplication(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        for (HtmlPanelGroup panelIcaroServiceBody : this.icaroServicePanelBodyList
                .get(indexPanelToRemove)) {
            if (panelIcaroServiceBody != null) {
                this.removePanelIcaroServiceIcaroApplication(idPanelToRemove,
                        panelIcaroServiceBody.getId(), indexPanelToRemove,
                        this.icaroServicePanelBodyList.get(indexPanelToRemove)
                                .indexOf(panelIcaroServiceBody));
            }
        }
        for (HtmlPanelGroup panelSlAgreementBody : this.slAgreementIcaroApplicationPanelBodyList
                .get(indexPanelToRemove)) {
            if (panelSlAgreementBody != null) {
                this.removePanelSlAgreementIcaroApplication(idPanelToRemove,
                        panelSlAgreementBody.getId(), indexPanelToRemove,
                        this.slAgreementIcaroApplicationPanelBodyList.get(indexPanelToRemove)
                                .indexOf(panelSlAgreementBody));
            }
        }
        this.icaroApplicationPanelBodyList.set(indexPanelToRemove, null);
        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroApplicationList().set(indexPanelToRemove, null);
        this.indexIAIcaroServiceList.set(indexPanelToRemove, null);
        this.indexIASlAgreementList.set(indexPanelToRemove, null);
        this.countIcaroApplication--;
        controlForButtonCreateXML();
    }

    public void removePanelIcaroTenant(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        for (HtmlPanelGroup panelSlAgreementBody : this.slAgreementIcaroTenantPanelBodyList
                .get(indexPanelToRemove)) {
            if (panelSlAgreementBody != null) {
                this.removePanelSlAgreementIcaroTenant(idPanelToRemove,
                        panelSlAgreementBody.getId(), indexPanelToRemove,
                        this.slAgreementIcaroTenantPanelBodyList.get(indexPanelToRemove)
                                .indexOf(panelSlAgreementBody));
            }
        }
        this.icaroTenantPanelBodyList.set(indexPanelToRemove, null);
        this.businessConfigurationController.getBusinessConfiguration()
                .getIcaroTenantList().set(indexPanelToRemove, null);
        this.indexITMonitorInfoList.set(indexPanelToRemove, null);
        this.indexITSlAgreementList.set(indexPanelToRemove, null);
    }

    public void removePanelIcaroServiceIcaroApplication(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroApplicationPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.icaroServicePanelBodyList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelParent)
                        .getIcaroServiceList().set(indexPanelToRemove, null);
                this.indexIAISMonitorInfoList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.indexIAISTcpPortList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.indexIAISUdpPortList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
            }

        }
        this.countIAIcaroServiceList.set(indexPanelParent,
                this.countIAIcaroServiceList.get(indexPanelParent) - 1);
        controlForButtonCreateXML();
    }

    public void removePanelMonitorInfoIcaroServiceIA(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelGParent, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroServicePanelBodyList
                .get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelGParent)
                        .getIcaroServiceList().get(indexPanelParent)
                        .getMonitorInfoList().set(indexPanelToRemove, null);
            }

        }

    }

    public void removePanelMonitorInfoIcaroTenant(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroTenantPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroTenantList().get(indexPanelParent)
                        .getMonitorInfoList().set(indexPanelToRemove, null);
            }

        }
    }

    public void removePanelSlActionSlObjectiveSA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGParent,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementPanelBodyList
                .get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlActionList().set(indexPanelToRemove, null);
                this.countSASOSlActionList.get(indexPanelGParent).set(
                        indexPanelParent,
                        this.countSASOSlActionList.get(indexPanelGParent).get(
                                indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlActionSlObjectiveIASA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGGParent,
            final Integer indexPanelGParent, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementIAPanelBodyList
                .get(indexPanelGGParent).get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelGGParent)
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlActionList().set(indexPanelToRemove, null);
                this.countIASASOSlActionList
                        .get(indexPanelGGParent)
                        .get(indexPanelGParent)
                        .set(indexPanelParent,
                                this.countIASASOSlActionList
                                        .get(indexPanelGGParent)
                                        .get(indexPanelGParent)
                                        .get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlActionSlObjectiveITSA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGGParent,
            final Integer indexPanelGParent, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementITPanelBodyList
                .get(indexPanelGGParent).get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroTenantList().get(indexPanelGGParent)
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlActionList().set(indexPanelToRemove, null);
                this.countITSASOSlActionList
                        .get(indexPanelGGParent)
                        .get(indexPanelGParent)
                        .set(indexPanelParent,
                                this.countITSASOSlActionList
                                        .get(indexPanelGGParent)
                                        .get(indexPanelGParent)
                                        .get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelSlAgreement(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        for (HtmlPanelGroup panelSlObjectiveBody : this.slObjectiveSlAgreementPanelBodyList.get(indexPanelToRemove)) {
            if (panelSlObjectiveBody != null) {
                this.removePanelSlObjectiveSlAgreement(idPanelToRemove,
                        panelSlObjectiveBody.getId(), indexPanelToRemove,
                        this.slObjectiveSlAgreementPanelBodyList.get(indexPanelToRemove)
                                .indexOf(panelSlObjectiveBody));
            }
        }

        this.slAgreementPanelBodyList.set(indexPanelToRemove, null);
        this.businessConfigurationController.getBusinessConfiguration()
                .getSlAgreementList().set(indexPanelToRemove, null);
        this.indexSASlObjectiveList.set(indexPanelToRemove, null);
        this.countSlAgreement--;
        controlForButtonCreateXML();
    }

    public void removePanelSlAgreementIcaroApplication(
            final String idPanelParent, final String panelToRemove,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroApplicationPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.slAgreementIcaroApplicationPanelBodyList.get(
                        indexPanelParent).set(indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelParent)
                        .getSlAgreementList().set(indexPanelToRemove, null);
                this.indexIASASlObjectiveList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.countIASlAgreementList.set(indexPanelParent,
                        this.countIASlAgreementList.get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlAgreementIcaroTenant(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.icaroTenantPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.slAgreementIcaroTenantPanelBodyList.get(indexPanelParent)
                        .set(indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroTenantList().get(indexPanelParent)
                        .getSlAgreementList().set(indexPanelToRemove, null);
                this.indexITSASlObjectiveList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.countITSlAgreementList.set(indexPanelParent,
                        this.countITSlAgreementList.get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlMetricSlObjectiveSA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGParent,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementPanelBodyList
                .get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlMetricList().set(indexPanelToRemove, null);
                this.countSASOSlMetricList.get(indexPanelGParent).set(
                        indexPanelParent,
                        this.countSASOSlMetricList.get(indexPanelGParent).get(
                                indexPanelParent) + 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlMetricSlObjectiveIASA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGGParent,
            final Integer indexPanelGParent, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementIAPanelBodyList
                .get(indexPanelGGParent).get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelGGParent)
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlMetricList().set(indexPanelToRemove, null);
                this.countIASASOSlMetricList
                        .get(indexPanelGGParent)
                        .get(indexPanelGParent)
                        .set(indexPanelParent,
                                this.countIASASOSlMetricList
                                        .get(indexPanelGGParent)
                                        .get(indexPanelGParent)
                                        .get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlMetricSlObjectiveITSA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGGParent,
            final Integer indexPanelGParent, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slObjectiveSlAgreementITPanelBodyList
                .get(indexPanelGGParent).get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroTenantList().get(indexPanelGGParent)
                        .getSlAgreementList().get(indexPanelGParent)
                        .getSlObjectiveList().get(indexPanelParent)
                        .getSlMetricList().set(indexPanelToRemove, null);
                this.countITSASOSlMetricList
                        .get(indexPanelGGParent)
                        .get(indexPanelGParent)
                        .set(indexPanelParent,
                                this.countITSASOSlMetricList
                                        .get(indexPanelGGParent)
                                        .get(indexPanelGParent)
                                        .get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void removePanelSlObjectiveSlAgreement(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelParent,
            final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slAgreementPanelBodyList) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.slObjectiveSlAgreementPanelBodyList.get(indexPanelParent)
                        .set(indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getSlAgreementList().get(indexPanelParent)
                        .getSlObjectiveList().set(indexPanelToRemove, null);
                this.indexSASOSlActionList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.indexSASOSlMetricList.get(indexPanelParent).set(
                        indexPanelToRemove, null);
                this.countSASlObjectiveList.set(indexPanelParent,
                        this.countSASlObjectiveList.get(indexPanelParent) - 1);
            }
        }

        controlForButtonCreateXML();
    }

    public void removePanelSlObjectiveSlAgreementIA(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGParent,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slAgreementIcaroApplicationPanelBodyList
                .get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.slObjectiveSlAgreementIAPanelBodyList
                        .get(indexPanelGParent).get(indexPanelParent)
                        .set(indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroApplicationList().get(indexPanelGParent)
                        .getSlAgreementList().get(indexPanelParent)
                        .getSlObjectiveList().set(indexPanelToRemove, null);
                this.indexIASASOSlActionList.get(indexPanelGParent)
                        .get(indexPanelParent).set(indexPanelToRemove, null);
                this.indexIASASOSlMetricList.get(indexPanelGParent)
                        .get(indexPanelParent).set(indexPanelToRemove, null);
                this.countIASASlObjectiveList.get(indexPanelGParent).set(
                        indexPanelParent,
                        this.countIASASlObjectiveList.get(indexPanelGParent)
                                .get(indexPanelParent) - 1);
            }

        }
        controlForButtonCreateXML();
    }

    public void removePanelSlObjectiveSlAgreementIT(final String idPanelParent,
            final String panelToRemove, final Integer indexPanelGParent,
            final Integer indexPanelParent, final Integer indexPanelToRemove) {
        for (HtmlPanelGroup panelParentContainer : this.slAgreementIcaroTenantPanelBodyList
                .get(indexPanelGParent)) {
            if (panelParentContainer != null
                    && panelParentContainer.getId().equals(idPanelParent)) {
                panelParentContainer.getChildren().remove(
                        panelParentContainer.findComponent(panelToRemove));
                this.slObjectiveSlAgreementITPanelBodyList
                        .get(indexPanelGParent).get(indexPanelParent)
                        .set(indexPanelToRemove, null);
                this.businessConfigurationController.getBusinessConfiguration()
                        .getIcaroTenantList().get(indexPanelGParent)
                        .getSlAgreementList().get(indexPanelParent)
                        .getSlObjectiveList().set(indexPanelToRemove, null);
                this.indexITSASOSlActionList.get(indexPanelGParent)
                        .get(indexPanelParent).set(indexPanelToRemove, null);
                this.indexITSASOSlMetricList.get(indexPanelGParent)
                        .get(indexPanelParent).set(indexPanelToRemove, null);
                this.countITSASlObjectiveList.get(indexPanelGParent).set(
                        indexPanelParent,
                        this.countITSASlObjectiveList.get(indexPanelGParent)
                                .get(indexPanelParent) - 1);
            }
        }
        controlForButtonCreateXML();
    }

    public void setButtonCreateXML(final boolean buttonCreateXML) {
        this.buttonCreateXML = buttonCreateXML;
    }

    public void setPanelBusinessConfigurationBody(
            final HtmlPanelGroup panelBusinessConfigurationBody) {
        this.panelBusinessConfigurationBody = panelBusinessConfigurationBody;
    }

    public void setPanelIAIcaroServiceBodyList(
            final List<List<HtmlPanelGroup>> panelIAIcaroServiceBodyList) {
        this.icaroServicePanelBodyList = panelIAIcaroServiceBodyList;
    }

    public void setPanelIASASlObjectiveBodyList(
            final List<List<List<HtmlPanelGroup>>> panelIASASlObjectiveBodyList) {
        this.slObjectiveSlAgreementIAPanelBodyList = panelIASASlObjectiveBodyList;
    }

    public void setPanelIASlAgreementBodyList(
            final List<List<HtmlPanelGroup>> panelIASlAgreementBodyList) {
        this.slAgreementIcaroApplicationPanelBodyList = panelIASlAgreementBodyList;
    }

    public void setPanelIcaroApplicationBodyList(
            final List<HtmlPanelGroup> panelIcaroApplicationBodyList) {
        this.icaroApplicationPanelBodyList = panelIcaroApplicationBodyList;
    }

    public void setPanelIcaroTenantBodyList(
            final List<HtmlPanelGroup> panelIcaroTenantBodyList) {
        this.icaroTenantPanelBodyList = panelIcaroTenantBodyList;
    }

    public void setPanelITSASlObjectiveBodyList(
            final List<List<List<HtmlPanelGroup>>> panelITSASlObjectiveBodyList) {
        this.slObjectiveSlAgreementITPanelBodyList = panelITSASlObjectiveBodyList;
    }

    public void setPanelITSlAgreementBodyList(
            final List<List<HtmlPanelGroup>> panelITSlAgreementBodyList) {
        this.slAgreementIcaroTenantPanelBodyList = panelITSlAgreementBodyList;
    }

    public void setPanelSASlObjectiveBodyList(
            final List<List<HtmlPanelGroup>> panelSASlObjectiveBodyList) {
        this.slObjectiveSlAgreementPanelBodyList = panelSASlObjectiveBodyList;
    }

    public void setPanelSlAgreementBodyList(
            final List<HtmlPanelGroup> panelSlAgreementBodyList) {
        this.slAgreementPanelBodyList = panelSlAgreementBodyList;
    }
}
