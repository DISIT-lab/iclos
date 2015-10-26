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

import java.util.Comparator;

import org.cloudsimulator.placement.ontology.VirtualMachinePlacement;

public class WeightedVmComparator implements Comparator<VirtualMachinePlacement>{

    @Override
    public int compare(VirtualMachinePlacement o1, VirtualMachinePlacement o2) {
        if(o1.getFFDWeight() == o2.getFFDWeight())
            return 0;
        else 
            if(o1.getFFDWeight() < o2.getFFDWeight())
                return -1;
            else
                return 1;
    }

}
