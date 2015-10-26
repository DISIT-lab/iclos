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
import java.util.Collections;
import java.util.Random;

import org.cloudsimulator.placement.ScoredVM;
import org.cloudsimulator.placement.ScoredVmComparator;
import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;

public class GraspL2NormHeuristic extends L2NormHeuristic {
    private int k=15;
    private Random rand = new Random();
    
    
    public GraspL2NormHeuristic(){
        super();
    };    
    
    public GraspL2NormHeuristic(int k) {
        super();
        this.setK(k);
    }

    public int getK() {
        return k;
    }

    @Override
    public String getMethodName() {        
        return "Grasp L2 Norm";
    }

    @Override
    protected VirtualMachinePlacement selectVM(
            ArrayList<VirtualMachinePlacement> vmFittingBin,
            ArrayList<ScoredVM> scoredVMs) {
     
        Collections.sort(scoredVMs,Collections.reverseOrder(new ScoredVmComparator()));       
        
        int limit = (k > scoredVMs.size() ? scoredVMs.size() : k );
        
        //devo scegliere una vm tra le prime k milgiori
        int rndFromBestK = rand.nextInt(limit);
        return scoredVMs.get(rndFromBestK).getVirtualMachinePlacement();
    }
    
    public void setK(int k) {
        this.k = k;
    }

}
