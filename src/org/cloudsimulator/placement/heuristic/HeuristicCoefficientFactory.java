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

public class HeuristicCoefficientFactory {
    
    public static final String AVG_DEMAND = "Average demand";
    public static final String EXP_AVG_DEMAND = "Exponential average demand";
    public static ArrayList<String> available_Type;
    
        
    public static HeuristicCoefficientBuilder getInstance(String type){
        switch (type) {
        case AVG_DEMAND:
            return new AvgDemandCoefficientBuilder();
        case EXP_AVG_DEMAND:
            return new ExpAvgDemandCoefficientBuilder();        
        default:
            return new AvgDemandCoefficientBuilder();            
        }
    }
    
    public static ArrayList<String> getAvailableType(){
        if(available_Type == null){
            available_Type = new ArrayList<String>();
            available_Type.add(AVG_DEMAND);
            available_Type.add(EXP_AVG_DEMAND);
        }
        return available_Type;
    }
}
