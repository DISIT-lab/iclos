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

package org.cloudsimulator.placement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.impl.AvalonLogger;

public class SimulatedPatternCollection implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6743023435374552303L;
    private ArrayList<SimulatedPattern> availablePatterns = new ArrayList<SimulatedPattern>();
    private Map<Integer,ArrayList<SimulatedPattern>> byLength = new HashMap<Integer,ArrayList<SimulatedPattern>>();
    
    
    public void addSimulatedPattern(SimulatedPattern simulatedPattern){
        this.availablePatterns.add(simulatedPattern);
        
        if(this.byLength.containsKey(simulatedPattern.getModelLength())){            
            ArrayList<SimulatedPattern> temp = this.byLength.get(simulatedPattern.getModelLength());
            temp.add(simulatedPattern);
            this.byLength.put(simulatedPattern.getModelLength(),temp);
        }else{
            ArrayList<SimulatedPattern> temp = new ArrayList<SimulatedPattern>();
            temp.add(simulatedPattern);
            this.byLength.put(simulatedPattern.getModelLength(),temp);
        }
    }
    
    public ArrayList<SimulatedPattern> getSimulatedPattern(String generatedFromVm){
        ArrayList<SimulatedPattern> patterns = new ArrayList<SimulatedPattern>();
        
        for(SimulatedPattern sp : availablePatterns){
            if(sp.getGeneratedFrom().equals(generatedFromVm))
                patterns.add(sp);
        }
        
        return patterns;
    }
    
    public ArrayList<SimulatedPattern> getSimulatedPattern(String generatedFromVm,
            int generationLength){
        ArrayList<SimulatedPattern> patterns = new ArrayList<SimulatedPattern>();        
        for(SimulatedPattern sp : availablePatterns){
            if(sp.getGeneratedFrom().equals(generatedFromVm) && sp.getModelLength() == generationLength)
                patterns.add(sp);
        }
        
        return patterns;
    }
    
    public SimulatedPattern getSimulatedPattern(String generatedFromVm,
            int generationLength, String timeStampName){        
                
        for(SimulatedPattern sp : availablePatterns){
            if(sp.getGeneratedFrom().equals(generatedFromVm)
                    && sp.getModelLength() == generationLength
                    && sp.getTimeStampName().equals(timeStampName))
                return sp;
        }
        
        return null;       
    }
               
    public SimulatedPattern getRandomSimulatedPattern(int dayLength){        
        if(this.byLength.containsKey(dayLength)){
            ArrayList<SimulatedPattern> patterns = this.byLength.get(dayLength);
            int choice = (int) (Math.random() * patterns.size());
            
            return patterns.get(choice);
        }      
        return null;
    }
    
//    public ArrayList<SimulatedPattern> getRandomSimulatedPattern(String generatedFromVm,
//            int generationLength){
//        
//        if(this.byLength.containsKey(generationLength)){
//            ArrayList<SimulatedPattern> patterns = this.byLength.get(generationLength);        
//            for(SimulatedPattern sp : patterns){
//                if(sp.getGeneratedFrom().equals(generatedFromVm) && sp.getModelLength() == generationLength)
//                    patterns.add(sp);
//            }
//            return patterns;
//        }
//        return null;
//    }

}
