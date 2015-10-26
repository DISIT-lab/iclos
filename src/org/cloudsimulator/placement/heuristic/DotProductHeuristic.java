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
import java.util.Collection;
import java.util.Collections;

import org.cloudsimulator.placement.ScoredVM;
import org.cloudsimulator.placement.ontology.HostMachinePlacement;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;
import org.jblas.DoubleMatrix;

public class DotProductHeuristic extends HeuristicControllerBase {
   
    protected ArrayList<ScoredVM> computeScore(ArrayList<VirtualMachinePlacement> vmFittingBin,HostMachinePlacement openedBin,
            DoubleMatrix weigthed_requested) {
                        
        weigthed_requested.muliRowVector(this.getMeanResourceDemandCoefVec());
        
        ArrayList<ScoredVM> scoredVMs = new ArrayList<ScoredVM>(weigthed_requested.rows);
        
        float[] score = new float[weigthed_requested.rows];        
        float max_score = 0;
        for (int i = 0; i < score.length; i++) {           
            score[i] = (float) weigthed_requested.getRow(i).dot(openedBin.getResidual_resource());
            
            scoredVMs.add(new ScoredVM(vmFittingBin.get(i), score[i]));
            
            //se non faccio bubble search o grasp ritorno subito l'indice senza perdere tempo a riordinare
            //l'array degli score
            if(score[i]>max_score){
                max_score = score[i];
                max_score_item_index = i;
            }
        }
        return scoredVMs;
    }

    @Override
    public String getMethodName() {        
        return "Dot Product";
    }
}
