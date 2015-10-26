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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.cloudsimulator.domain.NagiosServer;
import org.cloudsimulator.domain.XportRrd;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.Machine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.placement.AddedHostDetail;
import org.cloudsimulator.placement.PatternAssignamentWrapper;
import org.cloudsimulator.placement.PlacementResult;
import org.cloudsimulator.placement.SimulatedPattern;
import org.cloudsimulator.placement.SimulatedPatternCollection;
import org.cloudsimulator.placement.SimulationResultWrapper;
import org.cloudsimulator.placement.TestCaseMachineAssignament;
import org.cloudsimulator.placement.TestCasePatternWrapper;
import org.cloudsimulator.placement.TestPatternReview;
import org.cloudsimulator.placement.VmResourceValueContainer;
import org.cloudsimulator.placement.controller.PlacementControllerBase;
import org.cloudsimulator.placement.controller.PlacementControllerBinCentric;
import org.cloudsimulator.placement.controller.PlacementControllerItemCentric;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientBuilder;
import org.cloudsimulator.placement.heuristic.HeuristicCoefficientFactory;
import org.cloudsimulator.placement.heuristic.HeuristicControllerFFD;
import org.cloudsimulator.placement.heuristic.HeuristicControllerFactory;
import org.cloudsimulator.placement.heuristic.HeuristicInterface;
import org.cloudsimulator.placement.ontology.BaseMachinePlacementBase;
import org.cloudsimulator.placement.ontology.DataCenterMachinePlacement;
import org.cloudsimulator.placement.ontology.TestCase;
import org.cloudsimulator.placement.ontology.TestCaseBase;
import org.cloudsimulator.placement.ontology.TestCaseMixed;
import org.cloudsimulator.placement.ontology.TestCaseRatio;
import org.cloudsimulator.placement.ontology.TestMachine;
import org.cloudsimulator.repository.ExtensionFileRepository;
import org.cloudsimulator.thread.DataCollector;
import org.cloudsimulator.utility.filter.DirectoryFilter;
import org.cloudsimulator.utility.filter.PngFilter;
import org.cloudsimulator.utility.filter.XmlFilter;
import org.cloudsimulator.viewer.compositecomponent.MenuPattern;
import org.cloudsimulator.xml.rrd.XmlRrdConverter;

@Named("dataCenterVmPlacement")
//@ConversationScoped
public class DataCenterVirtualMachinePlacementSimulator implements Runnable, Serializable {

    private static final String ASSIGN_PATTERN_FROM_TEST_CASE = "Assign pattern based on test case ratio";
    
    private static final String ASSIGN_RANDOM_MODEL_PATTERN = "Assign random pattern from model data";
    private static final String ASSIGN_RANDOM_REAL_PATTERN = "Assign random pattern from real data";
    private static final long serialVersionUID = -730072561992572607L;
    private static final String DATEFORMATFORCALENDAR = "yyyy-MM-dd HH:mm:ss";
    private static final String METRICCPUAVG = "CPU_AVG";
    // private static final String METRICNETTRAFFIC = "Net_Traffic";
    private static final String METRICPHYSICALMEMORYUSED = "Physical_Memory_Used";
    public static final String DIRECTORY = "rrd/";
    //ho uno step di 10 minuiti
    protected static final int DAYSAMPLE = 144;
    private static final int QUANTIZATIONLEVEL = 10;
    private static final float FIXEDRESCALINGOFFSET = 5f;
    private static final boolean USEFIXEDRESCALINGOFFSET = true;
    
    //@Inject
    //private Conversation conversation;
    //private Map<String, List<String>> virtualMachinePatternMap;
    protected PatternAssignamentWrapper patternAssignamentWrapper;
           

    protected Integer refreshTime;

    protected DataCenter dataCenter;

    protected Integer days;
    private Date dateFrom;
    protected String baseDirectory = CollectorController.BASE_DIRECTORY;
    @Inject
    private NagiosServer nagiosServer;
    private boolean existsCollectedData;
    private Machine selectedHostMachine;
    private VirtualMachine selectedVirtualMachine;
    private String editPatternForVM;  
    protected float reservedOsHostResource = 0.05f;
    protected float hostMaxRisk = -0.1f;        
    protected float maxOverprovisioningTime = 0.25f;    
    @Inject
    private MenuPattern menuPattern;
    protected boolean stopSimulationIfNotFitting = false;
    protected boolean simulationEnded = false;
    protected boolean allVmHasPattern = false;
    private boolean reservedResourceConstraintChecked = false;  
    private boolean reservedResourceConstraintPassed = false;
    private boolean hostParameterChanged = false;  
    @Inject
    protected DataCenterController dataCenterController;
    private String selectedHostMachinePlacementAvailableName;
    private String selectedHostMachinePlacementAddedName;
    
    
    private BaseMachinePlacementBase selectedHostMachinePlacement;
    
    private DataCenterMachinePlacement selectedDataCenterMachinePlacement;
    private int selectedDataCenterMachinePlacementId;
    protected int n_runs = 5;
    protected int n_simulation = 2;
    private ArrayList<String> euristicControllers;
    protected String selectedEuristicController;
    private ArrayList<String> euristicCoefficientBuilders;
    protected String selectedEuristicCoefficientBuilder;
    protected SimulationResultWrapper simulationResultWrapper;
    protected PlacementControllerBase placementController;

    

    private boolean loadingPatternAssignament = false;
    
    @Inject
    protected TestBuilderPlacementController testBuilderPlacementController;
    protected int addedHostCpuCount = 2;
    protected float addedHostCpuFreq = 2000f;
    protected float addedHostMemSize = 128.0f;
    protected String selectedTestCase = "";

    protected ArrayList<String> availableTestCases;
    protected String selectedPatternAssignament = ASSIGN_PATTERN_FROM_TEST_CASE;
    protected ArrayList<String> availablePatternAssignament;

    private boolean loadingControllerState;
    

    private Part uploadedVmAssignament;
    private boolean autoExportResult = false;

    private SimulatedPatternCollection patternCollection;

    private ArrayList<VirtualMachine> virtualMachineList;

    protected String iterationStatus;

    protected ArrayList<TestCaseMachineAssignament> testCaseMachineAssignament;

    protected ArrayList<VirtualMachine> originalVirtualMachineList;

    private transient ArrayList<TestPatternReview> testCaseViewer;

    public DataCenterVirtualMachinePlacementSimulator() {
        super();
        this.refreshTime = 1200;
        this.dateFrom = Calendar.getInstance().getTime();
        this.days = 1;
        //this.virtualMachinePatternMap = new HashMap<String, List<String>>();
        this.patternAssignamentWrapper = new PatternAssignamentWrapper();
        this.euristicControllers = new ArrayList<String>();
        this.euristicControllers.addAll(HeuristicControllerFactory.getAvailableType());
        this.euristicCoefficientBuilders = new ArrayList<String>();
        this.euristicCoefficientBuilders.addAll(HeuristicCoefficientFactory.getAvailableType());
        this.selectedEuristicController = HeuristicControllerFactory.DOT_PRODUCT;
        this.selectedEuristicCoefficientBuilder = HeuristicCoefficientFactory.AVG_DEMAND;
        this.availableTestCases = new ArrayList<String>();      
        this.testCaseMachineAssignament = new ArrayList<TestCaseMachineAssignament>();
        this.simulationResultWrapper = null;
        
        this.availablePatternAssignament = new ArrayList<String>();
        this.availablePatternAssignament.add(DataCenterVirtualMachinePlacementSimulator.ASSIGN_PATTERN_FROM_TEST_CASE);
//        this.availablePatternAssignament.add(DataCenterVirtualMachinePlacementSimulator.ASSIGN_RANDOM_MODEL_PATTERN);
//        this.availablePatternAssignament.add(DataCenterVirtualMachinePlacementSimulator.ASSIGN_RANDOM_REAL_PATTERN);
        
            
        
        
    }

    public void addPatternToVirtualMachine(String hasNameVirtualMachine,
        TestCasePatternWrapper from) {
    this.patternAssignamentWrapper.getVirtualMachinePatternMap().put(hasNameVirtualMachine, from);
    //this.virtualMachinePatternMap.put(hasNameVirtualMachine, pathPattern);
    checkPatternSelected();
   }

    public void assignPatternBasedOnTestCase(){                
        if( this.selectedTestCase.length() > 0){

            //get the right test case based on name
            TestCaseBase selectedTest=null;
            for (TestCaseBase testCase : testBuilderPlacementController.getTestCase()) {
                if(testCase.getName().equalsIgnoreCase(selectedTestCase)){
                    selectedTest = testCase;
                    break;
                }
            }
            if(selectedTest == null) return;
            
            
            this.testCaseMachineAssignament = new ArrayList<TestCaseMachineAssignament>();            
            //quando asssegno nuovamente il pattern, potrebbe cambiare il numero delle macchine utilizzate
            //visto che ho introdotto della casualità nel metodo
            setVirtualMachineList(this.originalVirtualMachineList);
            
            //
            this.patternAssignamentWrapper = new PatternAssignamentWrapper(getDataCenter().getHasName(),selectedTest);

            int n_vms = dataCenter.getVirtualMachineList().size();
            
            
            List<TestCasePatternWrapper> machinPatternNames = createRandomPathOfPattern(n_vms,selectedTest);
            
            //adesso rimuovo le macchine virtuali che altrimenti invalidano la completezza della BC
            //scelgo a caso quelle da rimuovere
            //se to remove è positivo devo mostrare all'utente la situazione
            int n_to_remove = getVirtualMachineList().size() - machinPatternNames.size();
            if(n_to_remove > 0){                
                for (int i = 0; i < n_to_remove; i++) {
                    int to_remove = (int) (Math.random() * getVirtualMachineList().size());
                    getVirtualMachineList().remove(to_remove);
                }                                
            }
            
            
            File file = new File(this.baseDirectory + DataCollector.DIRECTORY);

            int k = 0;
            String previousMachineName = "";
            ArrayList<SimulatedPattern> machinePatterns = null;
            for(VirtualMachine vm : getVirtualMachineList()){
                TestCasePatternWrapper from = machinPatternNames.get(k);                 

                if(!from.getPath().equalsIgnoreCase(previousMachineName))
                    machinePatterns = patternCollection.getSimulatedPattern(from.getPath(),this.days);

                List<String> pathOfPattern = new ArrayList<String>();
                for (int i = 0; i < this.days; i++) {
                    //random select a pattern for the machine
                    int choice = (int) (Math.random() * machinePatterns.size());                                       
                    SimulatedPattern pattern = machinePatterns.get(choice);
                    String temp = ("../"                    
                            + file.getAbsolutePath() +
                            "/" + pattern.getGeneratedFrom() +
                            "/" + pattern.getModelLength()+"day_simulated" +
                            "/" + pattern.getTimeStampName()).replace(
                                    this.baseDirectory, "");
                    pathOfPattern.add(temp);
                }

                from.setServerFilePath(pathOfPattern);
                addPatternToVirtualMachine(vm.getHasName(), from);
                k++;
                previousMachineName = from.getPath();
            }
            this.allVmHasPattern = true;
        }
    }

    public void assignPatternToVMs() {
        if(this.selectedPatternAssignament.equals(ASSIGN_PATTERN_FROM_TEST_CASE))
            this.assignPatternBasedOnTestCase();
        else if(this.selectedPatternAssignament.equals(ASSIGN_RANDOM_MODEL_PATTERN))
            this.assignRandomPattern("model", false);
        else if(this.selectedPatternAssignament.equals(ASSIGN_RANDOM_REAL_PATTERN))
            this.assignRandomPattern("real", false);

    }

    public void assignRandomPattern(String fromPattern, boolean assignPatternToEmpty){        
        for(VirtualMachine vm : getVirtualMachineList()){
            List<String> pathOfPattern = this.patternAssignamentWrapper.getVirtualMachinePatternMap().get(vm.getHasName()).getServerFilePath();
            //            List<String> pathOfPattern = this.virtualMachinePatternMap.get(vm.getHasName());
            if (pathOfPattern == null && assignPatternToEmpty) {
                if(fromPattern.equals("real")){
                    pathOfPattern = getRandomPattern(this.baseDirectory
                            + DataCollector.DIRECTORY);                    
                }else{
                    pathOfPattern = getRandomModel(this.days);                    
                }
                TestCasePatternWrapper casePatternWrapper = new TestCasePatternWrapper("","","");
                casePatternWrapper.setServerFilePath(pathOfPattern);
                
                addPatternToVirtualMachine(vm.getHasName(), casePatternWrapper);                    
            }else if(!assignPatternToEmpty){
                if(fromPattern.equals("real")){
                    pathOfPattern = getRandomPattern(this.baseDirectory
                            + DataCollector.DIRECTORY);                    
                }else{
                    pathOfPattern = getRandomModel(this.days);                    
                }
                
                TestCasePatternWrapper casePatternWrapper = new TestCasePatternWrapper("","","");
                casePatternWrapper.setServerFilePath(pathOfPattern);
                
                addPatternToVirtualMachine(vm.getHasName(), casePatternWrapper);
            }

        }
        this.allVmHasPattern = true;
    }

    public String buildDetailView(){
        //setSelectedPlacementResult(selectBestIteration(MIN_BIN,placementResults));
        return "dataCenterVirtualMachinePlacementAnalyzeResult?faces-redirect=true";
    }

    public String buildTestCasePattern(int id){
        return "dataCenterVirtualMachinePlacementTestCaseViewer?faces-redirect=true&includeViewParams=true&id="+id;
    }
    
        public boolean checkCollectedDataExistence() {

            this.existsCollectedData = false;

            File file = new File(this.baseDirectory + DataCollector.DIRECTORY);
            if (file.exists() && file.listFiles(new DirectoryFilter()).length != 0) {
                File[] virtualMachineCollectedDirectoryArray = file
                        .listFiles(new DirectoryFilter());
                if (virtualMachineCollectedDirectoryArray[0]
                        .listFiles(new DirectoryFilter()).length != 0) {
                    File[] periodDirectoryArray = virtualMachineCollectedDirectoryArray[0]
                            .listFiles(new DirectoryFilter());
                    if (periodDirectoryArray[0].listFiles(new DirectoryFilter()).length != 0) {
                        File[] timeStampDirectoryArray = periodDirectoryArray[0]
                                .listFiles(new DirectoryFilter());
                        if (timeStampDirectoryArray[0].listFiles(new XmlFilter()).length != 0) {
                            existsCollectedData = true;
                        }
                    }
                }
            }
            return existsCollectedData;
        }
        
        public boolean checkPattern(String hasNameVirtualMachine) {
            if (this.patternAssignamentWrapper.getVirtualMachinePatternMap().containsKey(hasNameVirtualMachine)) {
                //if (this.virtualMachinePatternMap.containsKey(hasNameVirtualMachine)) {
                return true;
            }
            return false;
        }
        
    public void checkPatternSelected(){
        for(VirtualMachine vm : getVirtualMachineList()){
            if(! checkPattern(vm.getHasName())){
                allVmHasPattern = false;
                return;
            }
        }                        
        allVmHasPattern = true;
    }
    /*
     * Check if host have enough resource to allocate vms
     * with the given reserved resource constraints
     */
    public void checkReservedResourceConstraint(){
        this.setReservedResourceConstraintChecked(true);
        setHostParameterChanged(false);
        List<VirtualMachine> vms = getVirtualMachineList();
        List<HostMachine> hosts = this.dataCenter.getHostMachineList();

        for(HostMachine hm : hosts){
            hm.initResidualCapacity(this.reservedOsHostResource);
        }

        for(VirtualMachine vm : vms){
            boolean vm_placed = false;

            Iterator<HostMachine> it = hosts.iterator();            
            while (it.hasNext() && vm_placed == false) {
                HostMachine hm = it.next();

                float host_residual_cpu= hm.getHasResidualCPU() - vm.getReservedConsumptionCPU();
                float host_residual_memory= hm.getHasResidualMemory() - vm.getHasMemoryReservation();

                if(host_residual_cpu >=0 && host_residual_memory >=0){
                    hm.setHasResidualCPU(host_residual_cpu);
                    hm.setHasResidualMemory(host_residual_memory);
                    vm_placed = true;
                    break;
                }                            
            }
            if(vm_placed == false){
                this.setReservedResourceConstraintPassed(false);                
                return;
            }             
        }        
        this.setReservedResourceConstraintPassed(true);
        this.setHostParameterChanged(false);
    }
    
    private List<Float> createPatternValueFromFile(File xmlFile,
            int sampleToSimulate, boolean needToBeRescaled) {
        List<Float> patternList = new ArrayList<Float>();
        if (xmlFile != null) {

            XportRrd xportRrd = XmlRrdConverter.createXportRRD(xmlFile);
            int indexAverageValue = 0;

            for (String columnsValue : xportRrd.getColumnsLegend()) {
                if (columnsValue.contains("AVERAGE")) {
                    indexAverageValue = xportRrd.getColumnsLegend().indexOf(
                            columnsValue);
                }
            }


            Collection<List<Float>> patternValueCollection = xportRrd.getDataMap().values();

            int numberRepeat = (int) Math.ceil(((double) sampleToSimulate)
                    / ((double) patternValueCollection.size()));

            boolean finished = false;
            for (int i = 0; i < numberRepeat && !finished; i++) {
                for (List<Float> patternValue : patternValueCollection) {
                    Float temp = patternValue.get(indexAverageValue);
                    if(needToBeRescaled)
                    {                        
                        temp =(float) (temp * QUANTIZATIONLEVEL +
                                (USEFIXEDRESCALINGOFFSET ? FIXEDRESCALINGOFFSET : (float) Math.random() * QUANTIZATIONLEVEL));
                    }
                    patternList.add(temp);
                    if (patternList.size() == sampleToSimulate) {
                        finished = true;
                        break;
                    }
                }
            }
        }
        return patternList;
    }

    protected List<TestCasePatternWrapper> createRandomPathOfPattern(int n_vms,
            TestCaseBase selectedTest) {

        List<TestCasePatternWrapper> paths = new ArrayList<TestCasePatternWrapper>();
                
        if(selectedTest instanceof TestCase){
            TestCase currentTest = (TestCase)selectedTest;
            
            if(currentTest.getMachines().size() == 0) return paths;
            
            int testMod = currentTest.getMachines().size();
            int remainder = n_vms % testMod;
            
            int vmToUse = n_vms - remainder; //devo allocare configurazioni complete
            
            
            for (int i = 0; i < vmToUse; i++) {      
                int machine_index = i % testMod;

                TestMachine testMachine = currentTest.getMachines().get(machine_index);
                paths.add(new TestCasePatternWrapper
                        (testMachine.getName(),currentTest.getName(),currentTest.getMachines().get(machine_index).getRole() ));
            }
        }else{                    
           
           
            //è un caso misto, devo gestire anche i resti
            TestCaseMixed currentTest = (TestCaseMixed)selectedTest;
           
            TestCaseBase[] testCases = new TestCaseBase[currentTest.getTestCaseRatios().size()];
            int[] nVms = new int[currentTest.getTestCaseRatios().size()];
           
            int remainder = 0;
//            int allocated = 0;
            
            ArrayList<TestCaseRatio> testCaseRatios = currentTest.getTestCaseRatios();
            //mescolo la lista, in modo da cambiare anche se di poco i rapporti
            //degli oggetti
            Collections.shuffle(testCaseRatios, new Random(System.nanoTime()));
            
            
            int i=0;
            for (TestCaseRatio testCaseRatio : testCaseRatios) {
                int vm_ratio = (int) Math.floor((n_vms * testCaseRatio.getRatio())) + remainder;
                //per non allocare più macchine di quelle che ho, all'ultimo ciclo
                // controllo quante ne ho già allocate e faccio
                /*
                 vm_ratio = n_vms - allocated;
                  
                 */
                
                int testMod = testCaseRatio.getTestCase().getMachines().size();
                remainder = vm_ratio % testMod;
                
                int vmToUse = vm_ratio - remainder;
                
                testCases[i] = testCaseRatio.getTestCase();
                nVms[i] = vmToUse;
//                allocated += vmToUse;
                i++;
            }
            
            for (int j = 0; j < nVms.length; j++) {
                paths.addAll(createRandomPathOfPattern(nVms[j],testCases[j]));
                this.testCaseMachineAssignament.add(new TestCaseMachineAssignament((TestCase)testCases[j],nVms[j]));
            }
           
        }

        return paths;
    }
    
    
    
//    public ArrayList<HostMachinePlacement> getAddedHost() {
//        if(selectedPlacementResult != null)
//            return selectedPlacementResult.getAddedHost();
//        return null;
//    }

    public int getAddedHostCpuCount() {
        return addedHostCpuCount;
    }

    public float getAddedHostCpuFreq() {
        return addedHostCpuFreq;
    }

    public float getAddedHostMemSize() {
        return addedHostMemSize;
    }

//    public ArrayList<HostMachinePlacement> getAvailableHost() {
//        if(selectedPlacementResult != null)
//            return selectedPlacementResult.getAvailableHost();
//        return null;
//    }

    public ArrayList<String> getAvailablePatternAssignament() {
        return availablePatternAssignament;
    }

//    public void detailViewAdded(){
//        BaseMachinePlacementBase hm = null;
//        hm = this.findHostMachinePlacement(this.selectedHostMachinePlacementAddedName,getAddedHost());
//
//        if(hm == null) return; //non dovrebbe mai essere nullo
//        hm.buildChartModel();               
//        selectedHostMachinePlacement = hm;
//    }
//
//    public void detailViewUsed(){
//        BaseMachinePlacementBase hm = null;
//        hm = this.findHostMachinePlacement(this.selectedHostMachinePlacementAvailableName,getUsedHost());
//
//        if(hm == null) return; //non dovrebbe mai essere nullo
//        hm.buildChartModel();
//        selectedHostMachinePlacement = hm;
//    }

    public ArrayList<String> getAvailableTestCases() {
        return availableTestCases;
    }

    public Integer getCompletedSimulation() {
        if(placementController != null)
            return placementController.getCompletedSimulation();
        return 0;
    }

    public DataCenter getDataCenter() {
        return this.dataCenter;
    }


    public String getDATEFORMATFORCALENDAR() {
        return DATEFORMATFORCALENDAR;
    }

    public Date getDateFrom() {
        return (Date) this.dateFrom.clone();
    }

    public Integer getDays() {
        return this.days;
    }

    public String getEditPatternForVM() {
        return editPatternForVM;
    }

    public ArrayList<String> getEuristic_method() {
        return euristicControllers;
    }

    public ArrayList<String> getEuristicCoefficientBuilders() {
        return euristicCoefficientBuilders;
    }

    public float getHostMaxRisk() {
        return hostMaxRisk;
    }

    public String getIterationStatus() {
        return iterationStatus;
    }

    public float getMaxOverprovisioningTime() {
        return maxOverprovisioningTime;
    }

    public String getMonitorString() {
        if(placementController != null)
            return this.placementController.getMonitorString();
        return "";
    }

    public int getN_runs() {
        return n_runs;
    }

    public int getN_simulation() {
        return n_simulation;
    }

    public NagiosServer getNagiosServer() {
        return this.nagiosServer;
    }

    private List<Float> getPatternValue(List<String> pathOfPattern, String metricType) {

        ArrayList<Float> values = new ArrayList<Float>();
        for (int i = 0; i < pathOfPattern.size(); i++) {

            String temp_path = this.baseDirectory + (pathOfPattern.get(i).replace("../", ""));
            File[] simulated_resource = new File(temp_path).listFiles();

            for(File f: simulated_resource){
                if (f.getName().contains(metricType) && f.getName().endsWith(ExtensionFileRepository.XML)) {                   
                    values.addAll(createPatternValueFromFile(f, DAYSAMPLE,f.getPath().contains("day_simulated")));
                }    
            }

        }

        return values;
    }
    
//    public ArrayList<PlacementResult> getPlacementResults() {
//        return simulationResults;
//    }

    private List<String> getRandomModel(int dayLength) {

        File file = new File(this.baseDirectory
                + DataCollector.DIRECTORY);

        SimulatedPatternCollection patternCollection = menuPattern.getSimulatedPatternCollection();

        SimulatedPattern pattern = patternCollection.getRandomSimulatedPattern(dayLength);

        List<String> pathOfPattern = new ArrayList<String>();
        for (int i = 0; i < dayLength; i++) {
            String temp = ("../"                    
                    + file.getAbsolutePath() +
                    "/" + pattern.getGeneratedFrom() +
                    "/" + pattern.getModelLength()+"day_simulated" +
                    "/" + pattern.getTimeStampName()).replace(
                            this.baseDirectory, "");
            pathOfPattern.add(temp);
        }

        return pathOfPattern;
    }

    private List<String> getRandomPattern(String findDirectory) {

        File file = new File(findDirectory);
        File[] virtualMachineCollectedDirectoryArray = file
                .listFiles(new DirectoryFilter());
        Integer indexChoiceVirtualMachineCollected;
        indexChoiceVirtualMachineCollected = (int) (Math.random() * virtualMachineCollectedDirectoryArray.length);
        File[] periodDirectoryArray = virtualMachineCollectedDirectoryArray[indexChoiceVirtualMachineCollected]
                .listFiles(new DirectoryFilter());
        Integer indexChoicePeriod;
        indexChoicePeriod = (int) (Math.random() * periodDirectoryArray.length);
        File[] timeStampDirectoryArray = periodDirectoryArray[indexChoicePeriod]
                .listFiles(new DirectoryFilter());
        Integer indexChoiceTimeStamp;
        indexChoiceTimeStamp = (int) (Math.random() * timeStampDirectoryArray.length);
        File[] patternImageArray = timeStampDirectoryArray[indexChoiceTimeStamp]
                .listFiles(new PngFilter());
        // Cerco le immagini e non gli XMl per uniformare il codice nel caso in
        // cui il pattern sia scelto e non random.
        // Vedi creazione MenuPattern e addPatternToVirtualMachine
        List<String> pathOfPattern = new ArrayList<String>();
        for (int i = 0; i < patternImageArray.length; i++) {
            pathOfPattern.add("../"
                    + patternImageArray[i].getAbsolutePath().replace(
                            this.baseDirectory, ""));
        }

        return pathOfPattern;
    }   

    public Integer getRefreshTime() {
        return this.refreshTime;
    }    

    public float getReservedOsHostResource() {
        return reservedOsHostResource;
    }

    public DataCenterMachinePlacement getSelectedDataCenterMachinePlacement() {
        return selectedDataCenterMachinePlacement;
    }

    public int getSelectedDataCenterMachinePlacementId() {
        return selectedDataCenterMachinePlacementId;
    }

    public String getSelectedEuristicCoefficientBuilder() {
        return selectedEuristicCoefficientBuilder;
    }
    
    public String getSelectedEuristicMethod() {
        return selectedEuristicController;
    }

    public Machine getSelectedHostMachine() {
        return selectedHostMachine;
    }

    public BaseMachinePlacementBase getSelectedHostMachinePlacement() {
        return selectedHostMachinePlacement;
    }

    public String getSelectedHostMachinePlacementAddedName() {
        return selectedHostMachinePlacementAddedName;
    }

    public String getSelectedHostMachinePlacementName() {
        return selectedHostMachinePlacementAvailableName;
    }

    public String getSelectedPatternAssignament() {
        return selectedPatternAssignament;
    }

//    public PlacementResult getSelectedPlacementResult() {
//        return selectedPlacementResult;
//    }

    public String getSelectedTestCase() {
        return selectedTestCase;
    }

    public VirtualMachine getSelectedVirtualMachine() {
        return selectedVirtualMachine;
    }

    public String getSimulationResult() {
        if(placementController != null)
            return placementController.getSimulationResult();
        return "";
    }

    public SimulationResultWrapper getSimulationResultWrapper() {
        return simulationResultWrapper;
    }

    public TestBuilderPlacementController getTestBuilderPlacementController() {
        return testBuilderPlacementController;
    }

    public ArrayList<TestCaseMachineAssignament> getTestCaseMachineAssignament() {
        return testCaseMachineAssignament;
    }

    public ArrayList<TestPatternReview> getTestCaseViewer() {
        return testCaseViewer;
    }

    public Part getUploadedVmAssignament() {
        return uploadedVmAssignament;
    }

    public ArrayList<VirtualMachine> getVirtualMachineList() {
        return virtualMachineList;
    }

//    public ArrayList<HostMachinePlacement> getUsedHost() {
//        if(selectedPlacementResult != null)
//            return selectedPlacementResult.getUsedHost();
//        return null;
//    }

    

    public void hideLoadPatternAssignament(){
        this.setLoadingPatternAssignament(false);
    }

    @PostConstruct
    public void init() {
        this.availableTestCases.add("");
        for (TestCaseBase testCase : testBuilderPlacementController.getTestCase()) {
            this.availableTestCases.add(testCase.getName());
        }
        
        patternCollection = menuPattern.getSimulatedPatternCollection();                
    }

    //    public Map<String, List<String>> getVirtualMachinePatternMap() {
    //        return this.virtualMachinePatternMap;
    //    }

    public boolean isAllVmHasPattern() {
        return allVmHasPattern;
    }

    public boolean isAutoExportResult() {
        return autoExportResult;
    }

    public boolean isExistsCollectedData() {
        return existsCollectedData;
    }

    public boolean isHostParameterChanged() {
        return hostParameterChanged;
    }

    public boolean isLoadingControllerState() {
        return loadingControllerState;
    }    

    public boolean isLoadingPatternAssignament() {
        return loadingPatternAssignament;
    }

    public boolean isReservedResourceConstraintChecked() {
        return reservedResourceConstraintChecked;
    }

    public boolean isReservedResourceConstraintPassed() {
        return reservedResourceConstraintPassed;
    }

    public boolean isSimulationEnded() {
        return simulationEnded;
    }

    
    public boolean isSimulationSuccess() {
        if(placementController != null)
            return placementController.isSimulationSuccess();
        return false;
    }

    public boolean isStopSimulationIfNotFitting() {
        return stopSimulationIfNotFitting;
    }

//    public void iterationViewAvailable(){        
//        selectedHostMachinePlacement = null;
//        for (PlacementResult placementResult : simulationResults) {
//            if(placementResult.getId() == this.selectedDataCenterMachinePlacementId){
//                selectedPlacementResult = placementResult;
//                break;
//            }
//        }
//
//        selectedDataCenterMachinePlacement = selectedPlacementResult.getDataCenterMachinePlacement();
//    }
    

    

    protected ArrayList<VmResourceValueContainer> pullResourceValue() {
        ArrayList<VmResourceValueContainer> vmResourceValueContainers =
                new ArrayList<VmResourceValueContainer>();
        for (VirtualMachine virtualMachine : getVirtualMachineList()) {        
            
            TestCasePatternWrapper testCasePatternWrapper = this.patternAssignamentWrapper
                    .getVirtualMachinePatternMap()
                    .get(virtualMachine.getHasName());
            virtualMachine.setTestCasePatternWrapper(testCasePatternWrapper);
            
            List<Float> cpu_val = readSampleForVM(METRICCPUAVG, virtualMachine);
            List<Float> mem_val = readSampleForVM(METRICPHYSICALMEMORYUSED,virtualMachine);

            VmResourceValueContainer vmrc = new VmResourceValueContainer(cpu_val, mem_val);            
            vmResourceValueContainers.add(vmrc);            
        }
        return vmResourceValueContainers;
    }

//    public void loadPatternAssignament() throws IOException, ClassNotFoundException{
//        if(uploadedVmAssignament != null){
//            InputStream inputStream = uploadedVmAssignament.getInputStream();                
//            ObjectInputStream ois = new ObjectInputStream(inputStream);
//
//            PatternAssignamentWrapper result = (PatternAssignamentWrapper) ois.readObject();            
//            patternAssignamentWrapper = result;
//            allVmHasPattern = true;
//        }        
//        this.setLoadingPatternAssignament(false);
//    }

    private List<Float> readSampleForVM(String resource, VirtualMachine virtualMachine) {

        String hasName = virtualMachine.getHasName();

        List<Float> sampleList;
        //List<String> pathOfPattern = this.virtualMachinePatternMap.get(hasName);
        TestCasePatternWrapper testCasePatternWrapper = this.patternAssignamentWrapper.getVirtualMachinePatternMap().get(hasName);

        sampleList = getPatternValue(testCasePatternWrapper.getServerFilePath(), resource);                

        return sampleList;

    }

    private void reset() {
        this.simulationEnded = false;
    }

    @Override
    public void run() {

        AddedHostDetail addedHostDetail = new AddedHostDetail(addedHostCpuCount, addedHostCpuFreq, addedHostMemSize);

        
        ArrayList<PlacementResult> placementResults = new ArrayList<PlacementResult>();
        
        for (int i = 0; i < n_simulation; i++) {
            
            this.setIterationStatus(String.format("Running iteration %d of %d", i+1,n_simulation));    
        
            this.setIterationStatus(this.getIterationStatus() + String.format("...Loading vm's resources workload"));
            /*   Read vm values. Non devono cambiare quando faccio una nuova iterazione ma devono cambiare quando faccio una nuova simulazione   */
            ArrayList<VmResourceValueContainer> vmResourceValueContainers = pullResourceValue();
    
            HeuristicInterface ec = HeuristicControllerFactory.getInstance(getSelectedEuristicMethod());
            HeuristicCoefficientBuilder coeffBuilder = HeuristicCoefficientFactory.getInstance(getSelectedEuristicCoefficientBuilder());                
            simulationEnded = false;
    
            if(ec instanceof HeuristicControllerFFD){
                placementController = new PlacementControllerItemCentric(dataCenter, hostMaxRisk, 
                        maxOverprovisioningTime, 
                        reservedOsHostResource, 
                        days, n_runs, DAYSAMPLE,addedHostDetail,getVirtualMachineList());
            }else{
                placementController = new PlacementControllerBinCentric(dataCenter, hostMaxRisk, 
                        maxOverprovisioningTime, 
                        reservedOsHostResource, 
                        days, n_runs, DAYSAMPLE,addedHostDetail,getVirtualMachineList());
            }                        
    
            PlacementResult currentPlacementResult = null;
            try {
                this.setIterationStatus(this.getIterationStatus() + String.format("...Allocating Vms")); 
                currentPlacementResult = placementController.allocateVm( 
                        vmResourceValueContainers, ec, coeffBuilder, stopSimulationIfNotFitting,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            if(currentPlacementResult != null){
                this.setIterationStatus(this.getIterationStatus() + String.format("...Compute statistics"));
                placementController.computeIterationStatistics(currentPlacementResult);
                placementResults.add(currentPlacementResult);
            }
            this.setIterationStatus(this.getIterationStatus() + String.format("...DONE"));
                        
            
            
            
            if(i < n_simulation -1){
                //dopo ho un'altra simulazione, devo riassegnare i pattern in maniera casuale
                patternAssignamentWrapper.setVirtualMachinePatternMap(new Hashtable<String, TestCasePatternWrapper>());
                assignPatternToVMs();
            }
        }
        
        StringBuffer testAssignamentString = new StringBuffer();
        for (TestCaseMachineAssignament assignament : testCaseMachineAssignament) {
            testAssignamentString.append(String.format("", assignament.getTestCase().getName(),assignament.getMachinesWithPattern()));
        }
        
        simulationResultWrapper = new SimulationResultWrapper(selectedEuristicController, selectedEuristicCoefficientBuilder,
                placementResults, this.originalVirtualMachineList, getVirtualMachineList(),testAssignamentString.toString(),
                selectedTestCase,reservedOsHostResource,hostMaxRisk,maxOverprovisioningTime,n_runs,n_simulation);                
        
        
        simulationEnded = true;
        this.dataCenterController.setVirtualMachinePlacementSimulatorStarted(false);
        this.refreshTime = 1200;
    }

    public void setAddedHostCpuCount(int addedHostCpuCount) {
        this.addedHostCpuCount = addedHostCpuCount;
    }

    public void setAddedHostCpuFreq(float addedHostCpuFreq) {
        this.addedHostCpuFreq = addedHostCpuFreq;
    }

//    public void savePatternAssignament() throws IOException{
//        String fileName = "icaroPatternAssignament.ser";
//        String tempFileName = "icaroPatternAssignament";
//        PatternAssignamentWrapper toSerialize = this.patternAssignamentWrapper;
//
//        prepareDownload(fileName, tempFileName, toSerialize);
//
//    }

//    private PlacementResult selectBestIteration(String method_selection,ArrayList<PlacementResult> placementResults) {
//        int best_iteration_index = 0;
//        switch (method_selection) {
//        case MIN_BIN:
//            int used_bin = Integer.MAX_VALUE;
//            for (int i = 0; i < placementResults.size(); i++) {
//                PlacementResult result = placementResults.get(i);
//                int total_bin = result.getAddedHost().size() + result.getUsedHost().size();
//                if(used_bin > total_bin){
//                    used_bin = total_bin;
//                    best_iteration_index = i;
//                }
//            }
//
//        }
//        return placementResults.get(best_iteration_index);
//    }

    public void setAddedHostMemSize(float addedHostMemSize) {
        this.addedHostMemSize = addedHostMemSize;
    }

    public void setAllVmHasPattern(boolean allVmHasPattern) {
        this.allVmHasPattern = allVmHasPattern;
    }

    public void setAutoExportResult(boolean autoExportResult) {
        this.autoExportResult = autoExportResult;
    }

    public void setAvailablePatternAssignament(
            ArrayList<String> availablePatternAssignament) {
        this.availablePatternAssignament = availablePatternAssignament;
    }

    public void setAvailableTestCases(ArrayList<String> availableTestCases) {
        this.availableTestCases = availableTestCases;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
        setVirtualMachineList(new ArrayList<VirtualMachine>(dataCenter.getVirtualMachineList()));
        this.originalVirtualMachineList = new ArrayList<VirtualMachine>();
        for (VirtualMachine virtualMachine : dataCenter.getVirtualMachineList()) {
            this.originalVirtualMachineList.add(virtualMachine);
        }
        
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = new Date(dateFrom.getTime());
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setEditPatternForVM(String editPatternForVM) {
        this.editPatternForVM = editPatternForVM;
    }

    public void setEuristic_method(ArrayList<String> euristic_method) {
        this.euristicControllers = euristic_method;
    }

    public void setEuristicCoefficientBuilders(
            ArrayList<String> euristicCoefficientBuilders) {
        this.euristicCoefficientBuilders = euristicCoefficientBuilders;
    }

    public void setExistsCollectedData(boolean existsCollectedData) {
        this.existsCollectedData = existsCollectedData;
    }   

    public void setHostMaxRisk(float hostMaxRisk) {
        this.hostMaxRisk = hostMaxRisk;
    }

    public void setHostParameterChanged(boolean hostParameterChanged) {
        this.hostParameterChanged = hostParameterChanged;
        //this.reset();
    }

    public void setIterationStatus(String iterationStatus) {
        this.iterationStatus = iterationStatus;
    }

    public void setLoadingControllerState(boolean loadingControllerState) {
        this.loadingControllerState = loadingControllerState;
    }    

    public void setLoadingPatternAssignament(boolean loadingPatternAssignament) {
        this.loadingPatternAssignament = loadingPatternAssignament;
    }

    public void setMaxOverprovisioningTime(float maxOverprovisioningTime) {
        this.maxOverprovisioningTime = maxOverprovisioningTime;
    }

    public void setN_runs(int n_runs) {
        this.n_runs = n_runs;
    }   

    public void setN_simulation(int n_simulation) {
        this.n_simulation = n_simulation;
    }

    public void setNagiosServer(NagiosServer nagiosServer) {
        this.nagiosServer = nagiosServer;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime;
    }

//    public void setPlacementResults(ArrayList<PlacementResult> placementResults) {
//        this.simulationResults = placementResults;
//    }

    public void setReservedOsHostResource(float reservedOsHostResource) {
        if(this.reservedOsHostResource != reservedOsHostResource){
            hostParameterChanged = true;
            this.reservedResourceConstraintChecked = false;
            this.reservedResourceConstraintPassed = false;
            this.reset();    
        }
        this.reservedOsHostResource = reservedOsHostResource;

    }

    public void setReservedResourceConstraintChecked(
            boolean reservedResourceConstraintChecked) {
        this.reservedResourceConstraintChecked = reservedResourceConstraintChecked;
    }

    public void setReservedResourceConstraintPassed(
            boolean reservedResourceConstraintPassed) {
        this.reservedResourceConstraintPassed = reservedResourceConstraintPassed;
    }

    public void setSelectedDataCenterMachinePlacement(
            DataCenterMachinePlacement selectedDataCenterMachinePlacement) {
        this.selectedDataCenterMachinePlacement = selectedDataCenterMachinePlacement;
    }

    public void setSelectedDataCenterMachinePlacementId(
            int selectedDataCenterMachinePlacementId) {
        this.selectedDataCenterMachinePlacementId = selectedDataCenterMachinePlacementId;
    }

    public void setSelectedEuristicCoefficientBuilder(
            String selectedEuristicCoefficientBuilder) {
        this.selectedEuristicCoefficientBuilder = selectedEuristicCoefficientBuilder;
    }

    public void setSelectedEuristicMethod(String selectedEuristicMethod) {
        this.selectedEuristicController = selectedEuristicMethod;
    }

    public void setSelectedHostMachine(Machine selectedHostMachine) {
        this.selectedHostMachine = selectedHostMachine;
    }

    //    public void setVirtualMachinePatternMap(
    //            Map<String, List<String>> virtualMachinePatternMap) {
    //        this.virtualMachinePatternMap = virtualMachinePatternMap;
    //    }

    public void setSelectedHostMachinePlacementAddedName(
            String selectedHostMachinePlacementAddedName) {
        this.selectedHostMachinePlacementAddedName = selectedHostMachinePlacementAddedName;
    }

    public void setSelectedHostMachinePlacementName(
            String selectedHostMachinePlacementName) {
        this.selectedHostMachinePlacementAvailableName = selectedHostMachinePlacementName;
    }

    public void setSelectedPatternAssignament(String selectedPatternAssignament) {
        this.selectedPatternAssignament = selectedPatternAssignament;
    }

    public void setSelectedTestCase(String selectedTestCase) {
        this.selectedTestCase = selectedTestCase;
    }

//    public void setSelectedPlacementResult(PlacementResult selectedPlacementResult) {
//        this.selectedPlacementResult = selectedPlacementResult;
//        if(selectedPlacementResult != null)
//            setSelectedDataCenterMachinePlacement(selectedPlacementResult.getDataCenterMachinePlacement());
//    }

    public void setSelectedVirtualMachine(VirtualMachine selectedVirtualMachine) {
        this.selectedVirtualMachine = selectedVirtualMachine;
    }

    public void setSimulationEnded(boolean simulationEnded) {
        this.simulationEnded = simulationEnded;
    }

    public void setSimulationResultWrapper(
            SimulationResultWrapper simulationResultWrapper) {
        this.simulationResultWrapper = simulationResultWrapper;
    }

    public void setStopSimulationIfNotFitting(boolean stopSimulationIfNotFitting) {
        this.stopSimulationIfNotFitting = stopSimulationIfNotFitting;
    }

    public void setTestBuilderPlacementController(
            TestBuilderPlacementController testBuilderPlacementController) {
        this.testBuilderPlacementController = testBuilderPlacementController;
    }

    public void setTestCaseMachineAssignament(
            ArrayList<TestCaseMachineAssignament> testCaseMachineAssignament) {
        this.testCaseMachineAssignament = testCaseMachineAssignament;
    }
    public void setTestCaseViewer(ArrayList<TestPatternReview> testCaseViewer) {
        this.testCaseViewer = testCaseViewer;
    }
    
    public void setUploadedVmAssignament(Part uploadedVmAssignament) {
        this.uploadedVmAssignament = uploadedVmAssignament;
    }
    public void setVirtualMachineList(ArrayList<VirtualMachine> virtualMachineList) {
        this.virtualMachineList = virtualMachineList;
    }
        
    public String viewAssignedTestCasePattern(){
        /* Costruisco la vista dei test case assegnandoli anche l'id*/
        testCaseViewer = new ArrayList<TestPatternReview>();
        
        int id=0;
        //Set<String> keys = patternAssignamentWrapper.getVirtualMachinePatternMap().keySet();
        for (Entry<String, TestCasePatternWrapper> entry : patternAssignamentWrapper.getVirtualMachinePatternMap().entrySet()) {
            TestCasePatternWrapper currentTestPattern = entry.getValue();
            
            testCaseViewer.add(
                    new TestPatternReview(id, currentTestPattern.getTestName(), currentTestPattern.getType(), currentTestPattern.getServerFilePath(),
                            getPatternValue(currentTestPattern.getServerFilePath(), METRICCPUAVG),
                            getPatternValue(currentTestPattern.getServerFilePath(), METRICPHYSICALMEMORYUSED)));
            
                    id++;
        }
        
        //setSelectedPlacementResult(selectBestIteration(MIN_BIN,placementResults));
        return "dataCenterVirtualMachinePlacementAssignedTestCasePattern";        
    }

    

    

}
