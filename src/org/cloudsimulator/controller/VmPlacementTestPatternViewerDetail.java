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
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.placement.TestPatternReview;

@Named("placementTVCtrl")
@ConversationScoped
public class VmPlacementTestPatternViewerDetail implements Serializable {
    
    
   
        private String id;    
        
        @Inject
        protected DataCenterController dataCenterController;

        private TestPatternReview testPatternReview;

        

        public String getId() {
            return id;
        }
        
        public TestPatternReview getTestPatternReview() {
            return testPatternReview;
        }    

        public void init(){                
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Map<String, String> requestParameters = externalContext.getRequestParameterMap();
            if (requestParameters.containsKey("id")) {
               setId(requestParameters.get("id"));
            
                int id = Integer.parseInt(getId());        
                
                for (TestPatternReview result : dataCenterController.getDataCenterVirtualMachinePlacementSimulator().getTestCaseViewer()) {
                
                    if(result.getId() == id){
                        result.buildChart();
                        setTestPatternReview(result);                        
                        break;
                    }
                        
                }
            }
            
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTestPatternReview(TestPatternReview testPatternReview) {
            this.testPatternReview = testPatternReview;
        }                   
}
