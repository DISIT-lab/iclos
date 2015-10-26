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

package org.cloudsimulator.placement.ontology;

import java.util.ArrayList;

public class TestCaseMixed extends TestCaseBase {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5536959148357596012L;
    
    private String type;  
    private ArrayList<TestCaseRatio> testCaseRatios;

    
    public TestCaseMixed(String name,String type,ArrayList<TestCaseRatio> testCaseRatios) {
        super();
        this.name = name;
        this.type = type;
        setTestCaseRatios(testCaseRatios);
    }
    
    public TestCaseMixed() {
        super();
        setTestCaseRatios(new ArrayList<TestCaseRatio>());
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<TestCaseRatio> getTestCaseRatios() {
        return testCaseRatios;
    }

    public void setTestCaseRatios(ArrayList<TestCaseRatio> testCaseRatios) {
        this.testCaseRatios = testCaseRatios;
    }

    

}
