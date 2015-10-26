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

package org.cloudsimulator.placement.heuristic;
import java.util.ArrayList;

import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;
import org.jblas.DoubleMatrix;

public abstract class HeuristicControllerBaseFFD implements HeuristicControllerFFD{

    protected DoubleMatrix meanResourceDemandCoefVec;
    protected int max_score_item_index;
  
    
    public DoubleMatrix getMeanResourceDemandCoefVec() {
        return meanResourceDemandCoefVec;
    }

    public void Init(ArrayList<VirtualMachinePlacement> virtualMachineToBePlaced, HeuristicCoefficientBuilder coeffBuilder) {
        int n_measure = 0;
        int n_vms = 0;
        
        if(virtualMachineToBePlaced != null && virtualMachineToBePlaced.size() > 0){
            VirtualMachinePlacement temp = virtualMachineToBePlaced.get(0);
            n_measure = temp.getOriginalRequired().columns;
            n_vms = virtualMachineToBePlaced.size();
            
            DoubleMatrix matrix = new DoubleMatrix(n_vms, n_measure);
            
            for(int i=0;i<virtualMachineToBePlaced.size();i++){
                VirtualMachinePlacement vm = virtualMachineToBePlaced.get(i);
                
                matrix.putRow(i, vm.getOriginalRequired()); // crea un row vector e lo aggiunge alla matrice
            }
            
            meanResourceDemandCoefVec = coeffBuilder.buildCoefficients(matrix);                        
        }
    }

    public void setMeanResourceDemandCoefVec(DoubleMatrix meanResourceDemandCoefVec) {
        this.meanResourceDemandCoefVec = meanResourceDemandCoefVec;
    }

}