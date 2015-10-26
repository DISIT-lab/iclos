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

package org.cloudsimulator.listener;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.cloudsimulator.controller.DataCenterController;
import org.cloudsimulator.controller.Navigator;

public class ForceChooseDataCenterPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -2039060642494993447L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        if ("/virtualMachine.xhtml".equals(viewId)
                || "/datacenter/dataCenterSimulationRealTime.xhtml"
                        .equals(viewId)
                || "/datacenter/dataCenterSimulationFaster.xhtml"
                        .equals(viewId)
                || "/analyzeMetrics.xhtml"
                        .equals(viewId)
                || "/datacenter/dataCenterVirtualMachinePlacement.xhtml"
                        .equals(viewId)
//                || "/datacenter/vmPlacementTestRunner.xhtml"
//                        .equals(viewId)
                        ) {
            Application app = context.getApplication();
            DataCenterController dataCenterController = (DataCenterController) app
                    .evaluateExpressionGet(context, "#{dataCenterController}",
                            DataCenterController.class);
            Navigator navigator = (Navigator) app.evaluateExpressionGet(
                    context, "#{navigator}", Navigator.class);
            if (dataCenterController.getDataCenterChoice() == null) {
                navigator.setOriginalViewId(viewId);
                ViewHandler viewHandler = app.getViewHandler();
                UIViewRoot viewRoot = viewHandler.createView(context,
                        "/datacenter/dataCenterChoice.xhtml");
                context.setViewRoot(viewRoot);

            }
        }
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        // Not Called
    }

}