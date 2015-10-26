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

import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.simulator.FakeSCE;
import org.cloudsimulator.viewer.compositecomponent.CCUtility;

@Named("businessConfigurationForSimulatorViewer")
@ConversationScoped
public class BusinessConfigurationForSimulatorViewer implements Serializable {

    private static final long serialVersionUID = -7734670309495125940L;
    @Inject
    private FakeSCE fakeSCE;
    private boolean buttonInsertBC;
    private String entityToAddMessage;
    private HtmlPanelGroup panelBusinessConfigurationBody;
    private List<HtmlPanelGroup> panelIcaroApplicationBodyList;
    private List<HtmlPanelGroup> panelCreatorBodyList;
    private int indexIcaroApplication;
    private int countIcaroApplication;
    private int indexCreator;
    private int countCreator;

    public BusinessConfigurationForSimulatorViewer() {
        super();
        this.panelIcaroApplicationBodyList = new ArrayList<HtmlPanelGroup>();
        this.panelCreatorBodyList = new ArrayList<HtmlPanelGroup>();
        controlForButtonInsertBC();
    }

    public void addPanelCreator() {

        if (countCreator == 0) {
            this.panelCreatorBodyList.add(indexCreator, new HtmlPanelGroup());
            this.fakeSCE.getBusinessConfiguration().getCreatorList()
                    .add(indexCreator, new User());
            CCUtility.setFirstPanel(indexCreator,
                    panelBusinessConfigurationBody, "creator",
                    "businessConfigurationForSimulator");
            this.indexCreator++;
            this.countCreator++;
            controlForButtonInsertBC();

        }
    }

    public void addPanelIcaroApplication() {
        this.panelIcaroApplicationBodyList.add(indexIcaroApplication,
                new HtmlPanelGroup());
        this.fakeSCE.getBusinessConfiguration().getIcaroApplicationList()
                .add(indexIcaroApplication, new IcaroApplication());
        CCUtility.setFirstPanel(indexCreator, panelBusinessConfigurationBody,
                "icaroApplication", "businessConfigurationForSimulator");
        this.indexIcaroApplication++;
        this.countIcaroApplication++;
        controlForButtonInsertBC();
    }

    public void removePanelCreator(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        this.panelCreatorBodyList.set(indexPanelToRemove, null);
        this.fakeSCE.getBusinessConfiguration().getCreatorList()
                .set(indexPanelToRemove, null);
        this.countCreator--;
        controlForButtonInsertBC();
    }

    public void removePanelIcaroApplication(final String idPanelToRemove,
            final Integer indexPanelToRemove) {
        UIComponent uiPanelToRemove = panelBusinessConfigurationBody
                .findComponent(idPanelToRemove);
        panelBusinessConfigurationBody.getChildren().remove(uiPanelToRemove);
        this.panelIcaroApplicationBodyList.set(indexPanelToRemove, null);
        this.fakeSCE.getBusinessConfiguration().getIcaroApplicationList()
                .set(indexPanelToRemove, null);
        this.countIcaroApplication--;
        controlForButtonInsertBC();
    }

    public String businessConfigurationViewClear() {
        panelBusinessConfigurationBody.getChildren().clear();
        return "businessConfiguration";
    }

    private void controlForButtonInsertBC() {
        this.buttonInsertBC = false;
        this.entityToAddMessage = "";
        if (this.countIcaroApplication < 1) {
            this.entityToAddMessage += "Add at least one icaroApplication to BusinessConfiguration";
            return;
        }
        if (this.countCreator < 1) {
            this.entityToAddMessage += "Add one creator to BusinessConfiguration";
            return;
        }
        this.buttonInsertBC = true;
    }

    public int getCountCreator() {
        return countCreator;
    }

    public String getEntityToAddMessage() {
        return entityToAddMessage;
    }

    public HtmlPanelGroup getPanelBusinessConfigurationBody() {
        return panelBusinessConfigurationBody;
    }

    public List<HtmlPanelGroup> getPanelIcaroApplicationBodyList() {
        return panelIcaroApplicationBodyList;
    }

    public boolean isButtonInsertBC() {
        return this.buttonInsertBC;
    }

    public void setButtonInsertBC(boolean buttonInsertBC) {
        this.buttonInsertBC = buttonInsertBC;
    }

    public void setPanelBusinessConfigurationBody(
            final HtmlPanelGroup panelBusinessConfigurationBody) {
        this.panelBusinessConfigurationBody = panelBusinessConfigurationBody;
    }

    public void setPanelIcaroApplicationBodyList(
            final List<HtmlPanelGroup> panelIcaroApplicationBodyList) {
        this.panelIcaroApplicationBodyList = panelIcaroApplicationBodyList;
    }

}
