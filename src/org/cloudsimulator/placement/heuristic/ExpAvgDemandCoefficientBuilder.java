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
import org.jblas.DoubleMatrix;

/*
 * ai = exp(0.01 · avdemi)
 */
public class ExpAvgDemandCoefficientBuilder implements HeuristicCoefficientBuilder {

    private float epsilon = 0.01f;
    
    @Override
    public DoubleMatrix buildCoefficients(DoubleMatrix demands) {
        //calcolo la media dei valori colonna per colonna        
        DoubleMatrix meanResourceDemandCoefVec = demands.columnMeans();              
        //non sono scalati tra 0 ed 1 ma sono percentuali. Devo quindi dividere per 100
        meanResourceDemandCoefVec.divi(100);                       
        for (int i = 0; i < meanResourceDemandCoefVec.columns; i++) {
            meanResourceDemandCoefVec.put(i, Math.pow(Math.E,epsilon * meanResourceDemandCoefVec.get(i)));
        }
        
        return meanResourceDemandCoefVec;
    }

    public float getEpsilon() {
        return epsilon;
    }

    @Override
    public String getMethodName() {        
        return "Exponential avg demand";
    }
    
    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }

}
