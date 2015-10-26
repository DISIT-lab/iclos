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

package org.cloudsimulator.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.domain.ontology.GroupVirtualMachine;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.exception.LocalNetworkDuplicateException;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;
import org.cloudsimulator.placement.ontology.TestCase;
import org.cloudsimulator.placement.ontology.TestCaseBase;
import org.cloudsimulator.placement.ontology.TestCaseMixed;
import org.cloudsimulator.placement.ontology.TestMachine;
import org.cloudsimulator.placement.ontology.TestCaseRatio;
import org.cloudsimulator.utility.RatioNumberContainer;
import org.cloudsimulator.utility.Utility;

@Named("testBuilderPlacementController")
@ApplicationScoped
public class TestBuilderPlacementController implements Serializable {   
    /**
     * 
     */
    private static final long serialVersionUID = -2766303064937012590L; 
    private ArrayList<TestMachine> testMachines;
    private ArrayList<TestCaseBase> testCase;    
            
    public TestBuilderPlacementController() {
        super();  
        
        //INIT WITH DEFAULT TEST CASE
        
        testMachines = new ArrayList<TestMachine>();
        testCase = new ArrayList<TestCaseBase>();        
        
        /* START ECLAP */
        testMachines.add(new TestMachine("eclap-bp64net.eclap.eu-running@192.168.0.132","web"));
        testMachines.add(new TestMachine("eclap2-64bit.eclap.eu-running@192.168.0.54","web"));
        testMachines.add(new TestMachine("eclap.eu-balancer-ubuntu-11-10-64bit-running@192.168.0.133","balancer"));
        testMachines.add(new TestMachine("eclap.eu-db-running@192.168.0.13","db"));
                
        ArrayList<TestMachine> eclapTestRatios = new ArrayList<TestMachine>();
        eclapTestRatios.add(testMachines.get(0)); //web 1
        eclapTestRatios.add(testMachines.get(1)); //web 2
        eclapTestRatios.add(testMachines.get(2)); //bal
        eclapTestRatios.add(testMachines.get(3)); //db
        testCase.add(new TestCase("Eclap", "3 Tier Web App", eclapTestRatios)); //test case 0
        /* END ECLAP */
        
        
        /* START DISIT */
        testMachines.add(new TestMachine("disit.org-FE-running@192.168.0.76","web"));
        testMachines.add(new TestMachine("disit.org-db-running@192.168.0.75","db"));        
        
        ArrayList<TestMachine> disitTestRatios = new ArrayList<TestMachine>();
        disitTestRatios.add(testMachines.get(4)); //web 1
        disitTestRatios.add(testMachines.get(5)); //db
        testCase.add(new TestCase("Disit.org", "2 Tier Web App", disitTestRatios)); //test case 1
        /* END DISIT */
        
        /* START SIIMOBILITY */
        testMachines.add(new TestMachine("SiiMobility-Master500-72-running@192.168.0.72","master"));
        testMachines.add(new TestMachine("SiiMobility2-Mobility7-205-servicemap-running@192.168.0.205","web"));
        testMachines.add(new TestMachine("SiiMobility-Ingestion1-21-Ubuntu64-bit-running@192.168.0.21","ingestion"));
        testMachines.add(new TestMachine("SiiMobility-Silk-RDF-100-running@192.168.0.100","rdf"));
        testMachines.add(new TestMachine("SiiMobility-Node200(1)-70-running@192.168.0.70","node"));
        testMachines.add(new TestMachine("SiiMobility-Node200(1b-esx4)-14-running@192.168.0.14","node"));
        testMachines.add(new TestMachine("SiiMobility-Node200(1c-esx4)-92-running@192.168.0.92","node"));
        testMachines.add(new TestMachine("SiiMobility-Node200(1d-esx4)-40-running@192.168.0.40","node"));
        testMachines.add(new TestMachine("SiiMobility-Node200(2)-69-running@192.168.0.69","node"));        
        testMachines.add(new TestMachine("SiiMobility-Node200(6)-42-running@192.168.0.42","node"));   
        
        ArrayList<TestMachine> siiMobilityTestRatios = new ArrayList<TestMachine>();
        siiMobilityTestRatios.add(testMachines.get(6)); //master
        siiMobilityTestRatios.add(testMachines.get(7)); //web
        siiMobilityTestRatios.add(testMachines.get(8)); //ingestion
        siiMobilityTestRatios.add(testMachines.get(9)); //rdf
        siiMobilityTestRatios.add(testMachines.get(10)); //node
        siiMobilityTestRatios.add(testMachines.get(11)); //node
        siiMobilityTestRatios.add(testMachines.get(12)); //node
        siiMobilityTestRatios.add(testMachines.get(13)); //node
        siiMobilityTestRatios.add(testMachines.get(14)); //node
        siiMobilityTestRatios.add(testMachines.get(15)); //node        
        testCase.add(new TestCase("SiiMobility", "Multi Tier Web App", siiMobilityTestRatios)); //test case 2
        /* END SIIMOBILITY */
        
        /* START OPENMIND */
        testMachines.add(new TestMachine("openmind.disit.org-running@192.168.0.61","web"));
        testMachines.add(new TestMachine("openmind.disit.org-running@192.168.0.61","db"));        
        
        ArrayList<TestMachine> openMindTestRatios = new ArrayList<TestMachine>();
        openMindTestRatios.add(testMachines.get(16)); //web 1
        openMindTestRatios.add(testMachines.get(17)); //db
        testCase.add(new TestCase("OpenMind", "Single Tier Web App", openMindTestRatios)); //test case 3
        /* END OPENMIND */
        
        
        /* START MIXED ECLAP-DISIT.ORG */                
        ArrayList<TestCaseRatio> eclapDisitMixedTestRatios = new ArrayList<TestCaseRatio>();
        eclapDisitMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(0),0.5f)); //web 1
        eclapDisitMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(1),0.5f)); //db
        testCase.add(new TestCaseMixed("Mixed Eclap-Disit.org", "Mixed test case app", eclapDisitMixedTestRatios)); //test case 4
        /* END MIXED ECLAP-DISIT.ORG */
        
        /* START MIXED SiiMOBILITY-OPENMIND */                
        ArrayList<TestCaseRatio> siiMobOpenMindMixedTestRatios = new ArrayList<TestCaseRatio>();
        siiMobOpenMindMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(2),0.5f)); // sii mobility
        siiMobOpenMindMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(3),0.5f)); // open mind
        testCase.add(new TestCaseMixed("Mixed SiiMobility-OpenMind", "Mixed test case app", siiMobOpenMindMixedTestRatios)); //test case 5
        /* END MIXED SiiMOBILITY-OPENMIND */
        
        /* START MIXED SiiMOBILITY-OPENMIND */                
        ArrayList<TestCaseRatio> siiMobEclapMixedTestRatios = new ArrayList<TestCaseRatio>();
        siiMobEclapMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(0),0.5f)); // eclap
        siiMobEclapMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(2),0.5f)); // sii mobility
        testCase.add(new TestCaseMixed("Mixed SiiMobility-Eclap", "Mixed test case app", siiMobEclapMixedTestRatios)); //test case 6
        /* END MIXED SiiMOBILITY-OPENMIND */        
        
        /* START MIXED Disit-OPENMIND */                
        ArrayList<TestCaseRatio> disitOpenMindMixedTestRatios = new ArrayList<TestCaseRatio>();
        disitOpenMindMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(1),0.5f)); // disit
        disitOpenMindMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(3),0.5f)); // openMind
        testCase.add(new TestCaseMixed("Mixed Disit-OpenMind", "Mixed test case app", disitOpenMindMixedTestRatios)); //test case 7
        /* END MIXED SiiMOBILITY-OPENMIND */ 
        
        /* START MIXED Eclap-Disit-SiiMobility-OpenMind */                
        ArrayList<TestCaseRatio> allMixedTestRatios = new ArrayList<TestCaseRatio>();
        allMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(0),0.35f)); // eclap
        allMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(1),0.15f)); // disit
        allMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(2),0.35f)); // siimobility
        allMixedTestRatios.add(new TestCaseRatio((TestCase)testCase.get(3),0.15f)); // openMind
        testCase.add(new TestCaseMixed("Mixed Eclap-Disit-SiiMobility-OpenMind", "Mixed test case app", allMixedTestRatios)); //test case 8
        /* END MIXED Eclap-Disit-SiiMobility-OpenMind */ 

    }

    public ArrayList<TestMachine> getTestMachines() {
        return testMachines;
    }

    public void setTestMachines(ArrayList<TestMachine> testMachines) {
        this.testMachines = testMachines;
    }

    public ArrayList<TestCaseBase> getTestCase() {
        return testCase;
    }

    public void setTestCase(ArrayList<TestCaseBase> testCase) {
        this.testCase = testCase;
    }
    
}
