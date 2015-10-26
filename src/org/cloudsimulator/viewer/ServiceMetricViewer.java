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

import org.cloudsimulator.controller.ServiceMetricController;
import org.cloudsimulator.domain.ontology.GroupServiceMetric;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("serviceMetricViewer")
@ConversationScoped
public class ServiceMetricViewer implements Serializable {

    private static final long serialVersionUID = -1152120530332084589L;
    @Inject
    private ServiceMetricController serviceMetricController;
    private boolean buttonCreateXML;
    private String entityToAddMessage;
    private transient HtmlPanelGroup panelBodyServiceMetricPrincipal;
    private List<HtmlPanelGroup> panelServiceMetricBodyList;
    private int indexServiceMetric;
    private int countServiceMetric;

    public ServiceMetricViewer() {
        super();
        this.panelServiceMetricBodyList = new ArrayList<HtmlPanelGroup>();
        controlForButtonCreateXML();
    }

    public String serviceMetricViewClear() {
        panelBodyServiceMetricPrincipal.getChildren().clear();
        return "serviceMetric";
    }

    @SuppressWarnings("el-syntax")
    public void addPanelServiceMetric() {
        this.serviceMetricController.getGroupServiceMetricList().add(
                indexServiceMetric, new GroupServiceMetric());
        this.panelServiceMetricBodyList.add(indexServiceMetric,
                new HtmlPanelGroup());
        CCUtility.setFirstPanel(indexServiceMetric,
                panelBodyServiceMetricPrincipal, "groupServiceMetric",
                "serviceMetric");
        this.indexServiceMetric++;
        this.countServiceMetric++;
        controlForButtonCreateXML();
    }

    public void removePanelGroupServiceMetric(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBodyServiceMetricPrincipal
                .findComponent(idPanelToRemove);
        panelBodyServiceMetricPrincipal.getChildren().remove(uiPanelToRemove);
        this.panelServiceMetricBodyList.set(indexPanelToRemove, null);
        this.serviceMetricController.getGroupServiceMetricList().set(
                indexPanelToRemove, null);
        this.countServiceMetric--;
        controlForButtonCreateXML();
    }

    private void controlForButtonCreateXML() {
        this.buttonCreateXML = false;
        this.entityToAddMessage = "";
        if (this.countServiceMetric < 1) {
            this.entityToAddMessage = "Add at least one servicMetric";
            return;
        }

        this.buttonCreateXML = true;
    }

    public boolean isButtonCreateXML() {
        return this.buttonCreateXML;
    }

    public void setButtonCreateXML(boolean buttonCreateXML) {
        this.buttonCreateXML = buttonCreateXML;
    }

    public String getEntityToAddMessage() {
        return this.entityToAddMessage;
    }

    public HtmlPanelGroup getPanelBodyServiceMetricPrincipal() {
        return this.panelBodyServiceMetricPrincipal;
    }

    public void setPanelBodyServiceMetricPrincipal(
            final HtmlPanelGroup panelBodyServiceMetricPrincipal) {
        this.panelBodyServiceMetricPrincipal = panelBodyServiceMetricPrincipal;
    }

    public List<HtmlPanelGroup> getPanelServiceMetricBodyList() {
        return this.panelServiceMetricBodyList;
    }

    public void setPanelServiceMetricBodyList(
            final List<HtmlPanelGroup> panelServiceMetricBodyList) {
        this.panelServiceMetricBodyList = panelServiceMetricBodyList;
    }

}
