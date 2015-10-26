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

package org.cloudsimulator.viewer.compositecomponent;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.controller.DataCenterController;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.primefaces.model.TreeNode;
import org.primefaces.model.DefaultTreeNode;

@Named("analyzeMetricsMenu")
@ConversationScoped
public class AnalyzeMetricsMenuTree implements Serializable {

    private static final long serialVersionUID = 5648666371543279209L;
    private TreeNode root;
    private TreeNode[] selectedNodes = {};

    @Inject
    private DataCenterController dataCenterController;
    private DataCenter dataCenter;

    public void createTree() {
        this.dataCenter = this.dataCenterController.getDataCenter();
        root = new DefaultTreeNode("Root", null);
        if (this.dataCenter != null) {
            for (HostMachine hostMachine : this.dataCenter.getHostMachineList()) {
                TreeNode nodeHostMachine = new DefaultTreeNode(
                        new DataForMenuTreeNode(hostMachine.getHasName(),
                                hostMachine.getUri()), root);
                nodeHostMachine.setExpanded(true);
                for (VirtualMachine virtualMachine : hostMachine
                        .getVirtualMachineList()) {
                    TreeNode nodeVirtualMachine = new DefaultTreeNode(
                            new DataForMenuTreeNode(
                                    virtualMachine.getHasName(),
                                    virtualMachine.getUri()), nodeHostMachine);
                    nodeVirtualMachine.setExpanded(true);
                }
            }
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode[] getSelectedNodes() {
        return this.selectedNodes.clone();
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes.clone();
    }

}