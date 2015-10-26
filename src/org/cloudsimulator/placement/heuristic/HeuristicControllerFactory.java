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

public class HeuristicControllerFactory {
    
    public static final String L2_NORM_GRASP = "L2 Norm Grasp";
    public static final String L2_NORM = "L2 Norm";
    public static final String DOT_PRODUCT_GRASP = "Dot Product Grasp";
    public static final String DOT_PRODUCT = "Dot Product";
    public static final String FFD_PRODUCT = "FFD Prod";
    public static final String FFD_SUM = "FFD Sum";
    public static ArrayList<String> available_Type;
    
        
    public static HeuristicInterface getInstance(String type){
        switch (type) {
        case DOT_PRODUCT:
            return new DotProductHeuristic();
        case DOT_PRODUCT_GRASP:
            return new GraspDotProductHeuristic();
        case L2_NORM:
            return new L2NormHeuristic();
        case L2_NORM_GRASP:
            return new GraspL2NormHeuristic();
        case FFD_PRODUCT:
            return new FFDProd();
        case FFD_SUM:
            return new FFDSum();
        default:
            return new DotProductHeuristic();            
        }
    }
    
    public static ArrayList<String> getAvailableType(){
        if(available_Type == null){
            available_Type = new ArrayList<String>();
            available_Type.add(DOT_PRODUCT);
            available_Type.add(DOT_PRODUCT_GRASP);
            available_Type.add(L2_NORM);
            available_Type.add(L2_NORM_GRASP);
            available_Type.add(FFD_PRODUCT);
            available_Type.add(FFD_SUM);
        }
        return available_Type;
    }
}
