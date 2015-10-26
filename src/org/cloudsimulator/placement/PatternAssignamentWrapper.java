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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cloudsimulator.placement.ontology.TestCase;
import org.cloudsimulator.placement.ontology.TestCaseBase;

public class PatternAssignamentWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1105786475626144722L;
    
    private Hashtable<String, TestCasePatternWrapper> virtualMachinePatternMap;      
    private TestCaseBase testCase;
    private String dataCenterName;
    
    public PatternAssignamentWrapper() {
        setVirtualMachinePatternMap(new Hashtable<String, TestCasePatternWrapper>());        
    }
    
    public PatternAssignamentWrapper(String dataCenterName, TestCaseBase selectedTest) {
        setVirtualMachinePatternMap(new Hashtable<String, TestCasePatternWrapper>());      
        setDataCenterName(dataCenterName);        
        setTestCase(selectedTest);
    }

    public Hashtable<String, TestCasePatternWrapper> getVirtualMachinePatternMap() {
        return virtualMachinePatternMap;
    }

    public void setVirtualMachinePatternMap(Hashtable<String, TestCasePatternWrapper> virtualMachinePatternMap) {
        this.virtualMachinePatternMap = virtualMachinePatternMap;
    }

    public TestCaseBase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCaseBase selectedTest) {
        this.testCase = selectedTest;
    }

    public String getDataCenterName() {
        return dataCenterName;
    }

    public void setDataCenterName(String dataCenterName) {
        this.dataCenterName = dataCenterName;
    }

}
