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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.cloudsimulator.domain.NagiosServer;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.ExternalStorage;
import org.cloudsimulator.domain.ontology.Firewall;
import org.cloudsimulator.domain.ontology.GroupExternalStorage;
import org.cloudsimulator.domain.ontology.GroupFirewall;
import org.cloudsimulator.domain.ontology.GroupHostMachine;
import org.cloudsimulator.domain.ontology.GroupRouter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.Router;
import org.cloudsimulator.domain.ontology.SharedStorageVolume;
import org.cloudsimulator.exception.LocalNetworkDuplicateException;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;
import org.cloudsimulator.injection.InjectedConfiguration;
import org.cloudsimulator.persistence.DataCenterDAO;
import org.cloudsimulator.persistence.KBDAO;
import org.cloudsimulator.simulator.DataCenterSimulatorFaster;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.viewer.compositecomponent.MenuPattern;
import org.cloudsimulator.xml.rdf.XmlRdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@Named("dataCenterController")
@ConversationScoped
public class DataCenterController implements Serializable {

    private static final long serialVersionUID = -8670700791817521246L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataCenterController.class);
    private boolean ipInserted;
    @Inject
    @InjectedConfiguration(key = "icaroKB.addressIP", defaultValue = "localhost:8080")
    private String ipOfKB;
    @Inject
    private NagiosServer nagiosServer;
    @Inject 
    private MenuPattern menuPattern;
    private String dataCenterChoice;
    private DataCenter dataCenter;
    private List<GroupHostMachine> groupHostMachineList;

    private List<GroupExternalStorage> groupExternalStorageList;
    private List<GroupFirewall> groupFirewallList;
    private List<GroupRouter> groupRouterList;
    private Map<String, LocalNetworkController> mapLocalNetworkController;
    private DataCenterSimulatorFaster dataCenterSimulatorFaster;
    @Inject @Named("dataCenterVmPlacement")
    private DataCenterVirtualMachinePlacementSimulator dataCenterVirtualMachinePlacementSimulator;
    @Inject
    private PlacementSimulatorTestRunner placementSimulatorTestRunner;
    private Date dateFrom;
    private Integer days;
    private String createdXmlRdfDataCenter;
    private boolean okXmlRdf;
    private String errorMessage;
    private String alertMessage;
    private String sendDone;
    private boolean sendForm;
    private boolean simulationFasterStarted;
    private boolean virtualMachinePlacementSimulatorStarted;
    private boolean placementSimulatorTestRunnerStarted;
    public DataCenterController() {
        super();
        this.dataCenter = new DataCenter();
        this.groupHostMachineList = new ArrayList<GroupHostMachine>();
        this.groupExternalStorageList = new ArrayList<GroupExternalStorage>();
        this.groupFirewallList = new ArrayList<GroupFirewall>();
        this.groupRouterList = new ArrayList<GroupRouter>();
        this.dateFrom = new Date();
        init();
    }

    // Constructor----------------------------------------------------------------------------------

    private boolean controlInputConsistency() {

        for (GroupHostMachine groupHostMachine : groupHostMachineList) {
            if (groupHostMachine != null) {
                for (String useSharedStorage : groupHostMachine
                        .getSharedStorageList()) {
                    boolean control = false;
                    for (GroupExternalStorage groupExternalStorage : groupExternalStorageList) {
                        if (groupExternalStorage != null) {
                            for (SharedStorageVolume sharedStorageVolume : groupExternalStorage
                                    .getSharedStorageVolumeList()) {
                                if (useSharedStorage.equals(sharedStorageVolume
                                        .getLocalUri())) {
                                    control = true;
                                }
                            }
                        }
                    }
                    if (!control) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void controlLocalNetwork(final LocalNetwork localNetwork)
            throws LocalNetworkDuplicateException {
        if (localNetwork != null) {
            if (!mapLocalNetworkController.containsKey(localNetwork
                    .getLocalUri())) {
                mapLocalNetworkController.put(localNetwork.getLocalUri(),
                        new LocalNetworkController(localNetwork));
            } else {
                if (!mapLocalNetworkController.get(localNetwork.getLocalUri())
                        .getLocalNetwork().getHasIPAddress()
                        .equals(localNetwork.getHasIPAddress())
                        || !mapLocalNetworkController
                                .get(localNetwork.getLocalUri())
                                .getLocalNetwork().getHasSubNetworkMask()
                                .equals(localNetwork.getHasSubNetworkMask())) {
                    throw new LocalNetworkDuplicateException(
                            mapLocalNetworkController.get(
                                    localNetwork.getLocalUri())
                                    .getLocalNetwork(), localNetwork);
                }
            }
        }
    }

    // Method--------------------------------------------------------------------------------------

    private void createDataCenter() throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {

        // Questa variabile mi serve perch� nel tornare indietro dalla pagina di
        // visualizzazione dell'XML creato devo svuotare in qualche modo
        // tutte le variabili eccetto localUri, hasName e hasIdentifier perch�
        // queste vengono riaggiornate.
        DataCenter oldDataCenter = this.dataCenter;
        this.dataCenter = new DataCenter(oldDataCenter.getLocalUri(),
                oldDataCenter.getHasName(), oldDataCenter.getHasIdentifier());
        this.mapLocalNetworkController = new HashMap<String, LocalNetworkController>();

        createHostMachine();

        createExternalStorage();

        createFirewall();

        createRouter();

        for (LocalNetworkController localNetworkController : mapLocalNetworkController
                .values()) {
            this.dataCenter.getLocalNetworkList().add(
                    localNetworkController.getLocalNetwork());
        }

    }

    public String createDataCenterXML() {

        this.okXmlRdf = false;
        if (controlInputConsistency()) {

            try {
                this.createDataCenter();

                this.dataCenterChoice = this.dataCenter.getUri();
                XmlRdfCreator xmlRdfCreator = new XmlRdfCreator();
                createdXmlRdfDataCenter = xmlRdfCreator
                        .createDataCenterXmlRdf(this.dataCenter);
                this.okXmlRdf = true;
            } catch (TransformerException | ParserConfigurationException
                    | IOException | SAXException
                    | LocalNetworkDuplicateException
                    | LocalNetworkTooLittleException e) {
                LOGGER.error(e.getMessage(), e);
                this.errorMessage = e.toString();
            }

        } else {
            this.errorMessage = "There is a problem with consistency of inserted informations. <br /> Check the URI of <strong>SharedStorageVolume </strong> of <strong>ExternalStorage</strong> and the URI of <strong>useShareStorage</strong> property of <strong>HostMachine </strong>.";
        }

        return "visualizeXMLDataCenter";
    }

    private void createExternalStorage() throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {
        for (GroupExternalStorage groupExternalStorage : this.groupExternalStorageList) {
            if (groupExternalStorage != null) {

                for (int i = 0; i < groupExternalStorage
                        .getNumberExternalStorage(); i++) {
                    String suffixExternalStorage = ""
                            + this.groupExternalStorageList
                                    .indexOf(groupExternalStorage) + (i + 1);

                    List<NetworkAdapter> hasNetworkAdapterList = new ArrayList<NetworkAdapter>();

                    for (LocalNetwork localNetwork : groupExternalStorage
                            .getLocalNetworkList()) {
                        if (localNetwork != null) {
                            controlLocalNetwork(localNetwork);
                            hasNetworkAdapterList.add(new NetworkAdapter(
                                    mapLocalNetworkController.get(
                                            localNetwork.getLocalUri())
                                            .getAddress(), localNetwork
                                            .getUri()));
                        }
                    }

                    this.dataCenter
                            .getExternalStorageList()
                            .add(new ExternalStorage(
                                    dataCenter.getLocalUri() + "_ES"
                                            + suffixExternalStorage,
                                    hasNetworkAdapterList,
                                    groupExternalStorage.getMonitorInfoList(),
                                    groupExternalStorage
                                            .getSharedStorageVolumeList(),
                                    groupExternalStorage
                                            .getHasPrefixIdentifier()
                                            + suffixExternalStorage,
                                    groupExternalStorage.getHasPrefixName()
                                            + suffixExternalStorage
                                            + Utility
                                                    .getTimeStampForWriteDirectory(),
                                    groupExternalStorage.getHasMonitorState(),
                                    groupExternalStorage.getHasModelName()));
                }
            }
        }
    }

    private void createFirewall() throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {
        for (GroupFirewall groupFirewall : this.groupFirewallList) {
            if (groupFirewall != null) {

                for (int i = 0; i < groupFirewall.getNumberFirewall(); i++) {
                    String suffixFirewall = ""
                            + this.groupFirewallList.indexOf(groupFirewall)
                            + (i + 1);

                    List<NetworkAdapter> hasNetworkAdapterList = new ArrayList<NetworkAdapter>();

                    for (LocalNetwork localNetwork : groupFirewall
                            .getLocalNetworkList()) {
                        if (localNetwork != null) {
                            controlLocalNetwork(localNetwork);
                            hasNetworkAdapterList.add(new NetworkAdapter(
                                    mapLocalNetworkController.get(
                                            localNetwork.getLocalUri())
                                            .getAddress(), localNetwork
                                            .getUri()));
                        }
                    }

                    this.dataCenter
                            .getFirewallList()
                            .add(new Firewall(
                                    dataCenter.getLocalUri() + "_F"
                                            + suffixFirewall,
                                    hasNetworkAdapterList,
                                    groupFirewall.getMonitorInfoList(),
                                    groupFirewall.getHasPrefixIdentifier()
                                            + suffixFirewall,
                                    groupFirewall.getHasPrefixName()
                                            + suffixFirewall
                                            + Utility
                                                    .getTimeStampForWriteDirectory(),
                                    groupFirewall.getHasMonitorState(),
                                    groupFirewall.getHasModelName()));
                }
            }
        }
    }

    private void createHostMachine() throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {
        for (GroupHostMachine groupHost : this.groupHostMachineList) {
            if (groupHost != null) {
                for (int i = 0; i < groupHost.getNumberHost(); i++) {
                    String suffixHostMachine = ""
                            + this.groupHostMachineList.indexOf(groupHost)
                            + (i + 1);

                    List<NetworkAdapter> hasNetworkAdapterList = new ArrayList<NetworkAdapter>();

                    for (LocalNetwork localNetwork : groupHost
                            .getLocalNetworkList()) {
                        if (localNetwork != null) {
                            controlLocalNetwork(localNetwork);
                            hasNetworkAdapterList.add(new NetworkAdapter(
                                    mapLocalNetworkController.get(
                                            localNetwork.getLocalUri())
                                            .getAddress(), localNetwork
                                            .getUri()));
                        }
                    }

                    List<LocalStorage> hasLocalStorageList = new ArrayList<LocalStorage>();

                    for (LocalStorage localStorage : groupHost
                            .getLocalStorageList()) {
                        if (localStorage != null) {
                            hasLocalStorageList
                                    .add(new LocalStorage(
                                            this.dataCenter.getLocalUri()
                                                    + "_HM"
                                                    + suffixHostMachine
                                                    + "_LS"
                                                    + groupHost
                                                            .getLocalStorageList()
                                                            .indexOf(
                                                                    localStorage),
                                            localStorage.getHasName()
                                                    + Utility
                                                            .getTimeStampForWriteDirectory(),
                                            localStorage.getHasIdentifier(),
                                            localStorage.getHasDiskSize()));
                        }
                    }

                    List<String> useSharedStorageList = new ArrayList<String>();

                    for (String sharedStorageLocalURI : groupHost
                            .getSharedStorageList()) {
                        if (!"".equals(sharedStorageLocalURI)) {
                            useSharedStorageList.add(SharedStorageVolume
                                    .getPathuri() + sharedStorageLocalURI);
                        }
                    }

                    this.dataCenter
                            .getHostMachineList()
                            .add(new HostMachine(
                                    dataCenter.getLocalUri() + "_HM"
                                            + suffixHostMachine,
                                    groupHost.getHasOS(),
                                    hasNetworkAdapterList,
                                    groupHost.getMonitorInfoList(),
                                    hasLocalStorageList,
                                    useSharedStorageList,
                                    groupHost.getHasCPUCount(),
                                    groupHost.getHasCPUSpeed(),
                                    groupHost.getHasCPUType(),
                                    groupHost.getHasPrefixIdentifier()
                                            + suffixHostMachine,
                                    groupHost.getHasMemorySize(),
                                    groupHost.getHasPrefixName()
                                            + suffixHostMachine
                                            + Utility
                                                    .getTimeStampForWriteDirectory(),
                                    groupHost.getHasAuthUserName(), groupHost
                                            .getHasAuthUserPassword(),
                                    groupHost.getHasCapacity(), groupHost
                                            .getHasMonitorState(), groupHost
                                            .getIsInDomain()));
                }
            }
        }

    }

    public List<String> createHostMachineList() {
        List<String> hostList = new ArrayList<String>();
        for (HostMachine hostMachine : dataCenter.getHostMachineList()) {
            if (hostMachine != null) {
                hostList.add(hostMachine.getUri());
            }
        }
        return hostList;
    }

    private void createRouter() throws LocalNetworkDuplicateException,
            LocalNetworkTooLittleException {
        for (GroupRouter groupRouter : this.groupRouterList) {
            if (groupRouter != null) {

                for (int i = 0; i < groupRouter.getNumberRouter(); i++) {
                    String suffixRouter = ""
                            + this.groupRouterList.indexOf(groupRouter)
                            + (i + 1);

                    List<NetworkAdapter> hasNetworkAdapterList = new ArrayList<NetworkAdapter>();

                    for (LocalNetwork localNetwork : groupRouter
                            .getLocalNetworkList()) {
                        if (localNetwork != null) {
                            controlLocalNetwork(localNetwork);
                            hasNetworkAdapterList.add(new NetworkAdapter(
                                    mapLocalNetworkController.get(
                                            localNetwork.getLocalUri())
                                            .getAddress(), localNetwork
                                            .getUri()));
                        }
                    }

                    this.dataCenter
                            .getRouterList()
                            .add(new Router(
                                    dataCenter.getLocalUri() + "_R"
                                            + suffixRouter,
                                    hasNetworkAdapterList,
                                    groupRouter.getMonitorInfoList(),
                                    groupRouter.getHasPrefixIdentifier()
                                            + suffixRouter,
                                    groupRouter.getHasPrefixName()
                                            + suffixRouter
                                            + Utility
                                                    .getTimeStampForWriteDirectory(),
                                    groupRouter.getHasMonitorState(),
                                    groupRouter.getHasModelName()));
                }
            }
        }
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public String getCreatedXmlRdfDataCenter() {
        return this.createdXmlRdfDataCenter;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public String getDataCenterChoice() {
        return this.dataCenterChoice;
    }

    public DataCenterSimulatorFaster getDataCenterSimulatorFaster() {
        return dataCenterSimulatorFaster;
    }

    public DataCenterVirtualMachinePlacementSimulator getDataCenterVirtualMachinePlacementSimulator() {
        return dataCenterVirtualMachinePlacementSimulator;
    }
    
    public Date getDateFrom() {
        return (Date) this.dateFrom.clone();
    }
    
    public Integer getDays() {
        return this.days;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<GroupExternalStorage> getGroupExternalStorageList() {
        return groupExternalStorageList;
    }

    public List<GroupFirewall> getGroupFirewallList() {
        return groupFirewallList;
    }

    public List<GroupHostMachine> getGroupHostMachineList() {
        return groupHostMachineList;
    }

    // Getters and
    // setters-------------------------------------------------------------------------

    public List<GroupRouter> getGroupRouterList() {
        return groupRouterList;
    }

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public MenuPattern getMenuPattern() {
        return menuPattern;
    }

    public PlacementSimulatorTestRunner getPlacementSimulatorTestRunner() {
        return placementSimulatorTestRunner;
    }
    
    public String getSendDone() {
        return sendDone;
    }

    public void getXmlRdfOfDataCenterFromServer() throws IOException {
        Utility.getXMLFromServer(this.createdXmlRdfDataCenter, "dataCenter");
    }

    public void hideSendForm() {
        this.sendForm = false;
    }

    @PostConstruct
    public void init() {
            this.dataCenterSimulatorFaster = new DataCenterSimulatorFaster(this.nagiosServer);
            //this.dataCenterVirtualMachinePlacementSimulator = new DataCenterVirtualMachinePlacementSimulator(this.nagiosServer,this.menuPattern,this);
    }

    public void invertIpInserted() {
        this.ipInserted = !ipInserted;
    }

    public boolean isIpInserted() {
        return this.ipInserted;
    }

    public boolean isOkXmlRdf() {
        return okXmlRdf;
    }

    public boolean isPlacementSimulatorTestRunnerStarted() {
        return placementSimulatorTestRunnerStarted;
    }

    public boolean issendForm() {
        return sendForm;
    }

    public boolean isSimulationFasterStarted() {
        return this.simulationFasterStarted;
    }

    public boolean isVirtualMachinePlacementSimulatorStarted() {
        return this.virtualMachinePlacementSimulatorStarted;
    }

    public void loadDataCenter() {
        DataCenterDAO dataCenterDAO = new DataCenterDAO();
        this.dataCenter = dataCenterDAO.fetchDataCenterByURI(this.ipOfKB,
                this.dataCenterChoice);
    }

    public void resetMessages() {
        this.sendDone = "";
        this.alertMessage = "";
        this.errorMessage = "";
    }

    public void runVmPlacementTest() {
//      this.dataCenterVirtualMachinePlacementSimulator.setDataCenter(this.dataCenter);        
      this.placementSimulatorTestRunner.setRefreshTime(1);
      Thread simulatorVirtualMachinePlacement;
      simulatorVirtualMachinePlacement = new Thread(placementSimulatorTestRunner,
              "placementSimulatorTestRunner");
      simulatorVirtualMachinePlacement.start();
      this.setPlacementSimulatorTestRunnerStarted(true);
      
      FacesContext context = FacesContext.getCurrentInstance();
      String url = context
              .getApplication()
              .getViewHandler()
              .getActionURL(context,
                      context.getViewRoot().getViewId());

      try {
          // Invoke a redirect to the action URL.
          context.getExternalContext().redirect(url);
      } catch (IOException e) {
          // Uhh, something went seriously wrong.
          throw new FacesException("Cannot redirect to " + url
                  + " due to IO exception.", e);
      }
  }

    public void sendXmlRdfOfDataCenterToKB() {
        ResponseMessageString responseMessage;
        hideSendForm();
        try {
            responseMessage = KBDAO.sendXMLToKB("PUT", this.ipOfKB,
                    "dataCenter", this.dataCenter.getLocalUri(),
                    this.createdXmlRdfDataCenter);

            if (responseMessage != null) {
                if (responseMessage.getResponseCode() < 400) {
                    this.sendDone = "done";

                } else {
                    this.sendDone = "error";
                }

                if (responseMessage.getResponseBody() != null) {
                    this.alertMessage = "<strong>Response Code </strong>: "
                            + responseMessage.getResponseCode()
                            + " <strong>Reason</strong>: "
                            + responseMessage.getReason()
                            + "<br />"
                            + responseMessage.getResponseBody().replace("\n",
                                    "<br />");
                } else {
                    this.alertMessage = "<strong>Response Code </strong>: "
                            + responseMessage.getResponseCode()
                            + " <strong>Reason</strong>: "
                            + responseMessage.getReason() + "<br />";
                }
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            this.sendDone = "exception";
            this.alertMessage = e.toString();
        } 

    }

    public void setCreatedXmlRdfDataCenter(final String createdXmlRdfDataCenter) {
        this.createdXmlRdfDataCenter = createdXmlRdfDataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public void setDataCenterChoice(final String dataCenterChoice) {
        this.dataCenterChoice = dataCenterChoice;
    }

    public void setDataCenterSimulatorFaster(
            final DataCenterSimulatorFaster dataCenterSimulatorFaster) {
        this.dataCenterSimulatorFaster = dataCenterSimulatorFaster;
    }

    public void setDataCenterVirtualMachinePlacementSimulator(
            final DataCenterVirtualMachinePlacementSimulator dataCenterVirtualMachinePlacementSimulator) {
        this.dataCenterVirtualMachinePlacementSimulator = dataCenterVirtualMachinePlacementSimulator;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = new Date(dateFrom.getTime());
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setIpInserted(boolean ipInserted) {
        this.ipInserted = ipInserted;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

    public void setMenuPattern(MenuPattern menuPattern) {
        this.menuPattern = menuPattern;
    }

    public void setOkXmlRdf(final boolean okXmlRdf) {
        this.okXmlRdf = okXmlRdf;
    }
    
    public void setPlacementSimulatorTestRunner(
            PlacementSimulatorTestRunner placementSimulatorTestRunner) {
        this.placementSimulatorTestRunner = placementSimulatorTestRunner;
    }

    public void setPlacementSimulatorTestRunnerStarted(
            boolean placementSimulatorTestRunnerStarted) {
        this.placementSimulatorTestRunnerStarted = placementSimulatorTestRunnerStarted;
    }

    public void setSimulationFasterStarted(boolean simulationFasterStarted) {
        this.simulationFasterStarted = simulationFasterStarted;
    }

    public void setVirtualMachinePlacementSimulatorStarted(boolean virtualMachinePlacementSimulatorStarted) {
        this.virtualMachinePlacementSimulatorStarted = virtualMachinePlacementSimulatorStarted;
    }

    public void showSendForm() {
        this.sendForm = true;
    }

    public void simulate() {
        this.dataCenterSimulatorFaster.setDataCenter(this.dataCenter);
        this.dataCenterSimulatorFaster.setRefreshTime(1);
        Thread simulatorFaster;
        simulatorFaster = new Thread(dataCenterSimulatorFaster,
                "simulatorFaster");
        simulatorFaster.start();
        this.setSimulationFasterStarted(true);
    }

    public void simulateVirtualMachinePlacement() {
//        this.dataCenterVirtualMachinePlacementSimulator.setDataCenter(this.dataCenter);        
        
        if(this.dataCenterVirtualMachinePlacementSimulator.isStopSimulationIfNotFitting() && 
                this.dataCenterVirtualMachinePlacementSimulator.isHostParameterChanged() &&
                !this.dataCenterVirtualMachinePlacementSimulator.isReservedResourceConstraintPassed())
        {}
        else{           
            this.dataCenterVirtualMachinePlacementSimulator.setRefreshTime(1);
            Thread simulatorVirtualMachinePlacement;            
            simulatorVirtualMachinePlacement = new Thread(dataCenterVirtualMachinePlacementSimulator,
                    "simulatorVirtualMachinePlacement");
            simulatorVirtualMachinePlacement.start();
            this.setVirtualMachinePlacementSimulatorStarted(true);           
        }
        
        
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context
                .getApplication()
                .getViewHandler()
                .getActionURL(context,
                        context.getViewRoot().getViewId());

        try {
            // Invoke a redirect to the action URL.
            context.getExternalContext().redirect(url);
        } catch (IOException e) {
            // Uhh, something went seriously wrong.
            throw new FacesException("Cannot redirect to " + url
                    + " due to IO exception.", e);
        }
    }

}
