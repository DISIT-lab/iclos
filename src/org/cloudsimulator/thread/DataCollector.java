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

package org.cloudsimulator.thread;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cloudsimulator.domain.ResponseMessageByteArray;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.ExtensionFileRepository;
import org.cloudsimulator.repository.RestAPIRepository;
import org.cloudsimulator.utility.RestAPI;
import org.cloudsimulator.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollector implements Runnable, Serializable {

    private static final long serialVersionUID = 5109460453490016479L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataCollector.class);
    private static final String METRICNAME_WIN_NET_TRAFFIC = "SNMP_WIN_Net_Traffic";
    private static final String METRICNAME_WIN_CPU_AVG = "SNMP_WIN_CPU_AVG";
    private static final String METRICNAME_WIN_VOLUMES = "SNMP_WIN_Volumes";
    private static final String METRICNAME_WIN_PHYSICAL_MEMORY_USED = "SNMP_WIN_Physical_Memory_Used";
    private static final String METRICNAME_LINUX_NET_TRAFFIC = "SNMP_Linux_Net_Traffic";
    private static final String METRICNAME_LINUX_CPU_AVG = "SNMP_Linux_CPU_AVG";
    private static final String METRICNAME_LINUX_DISK_USAGE = "SNMP_Linux_Disk_Usage";
    private static final String METRICNAME_LINUX_PHYSICAL_MEMORY_USED = "SNMP_Linux_Physical_Memory_Used";
    private static final String METRICNAME_VMWARE_HOST_NET_TRAFFIC = "VMWARE_Host_Net_Traffic";
    private static final String METRICNAME_VVMWARE_VM_DISK_IO_ALL = "VMWARE_VM_DISK_IO_ALL";
    private static final String METRICNAME_VMWARE_HOST_CPU_MHZ = "VMWARE_Host_CPU_MHz";
    private static final String METRICNAME_VMWARE_HOST_STORAGE_USED = "VMWARE_Host_Storage_Used";
    private static final String METRICNAME_VMWARE_HOST_OVERALL_MEMORY = "VMWARE_Host_Overall_Memory";
    private static final String METRICNAME_VMWARE_HOST_OVERHEAD_MEMORY = "VMWARE_Host_Overhead_Memory";
    private static final String METRICPERIOD_ONE_DAY = "1day";
    private static final String METRICPERIOD_SEVEN_DAY = "7day";
    private static final String METRICPERIOD_THIRTY_DAY = "30day";
    public static final String DIRECTORY = "dataCollected/";
    private int metricCollected;
    private Map<String, String> tabContentMap;
    private String tmpTabContent;
    private Integer refreshTime;

    private List<String> monitoredVirtualWinList;
    private List<String> monitoredVirtualLinuxList;
    private List<String> monitoredHostsList;
    private boolean started;
    private String baseDirectory;
    private String ipNagiosCollectData;
    private String userNameNagiosCollectData;
    private String passWordNagiosCollectData;

    private int dayCount;
    private String timestamp;

    public DataCollector(String baseDirectory, String ipNagiosCollectData,
            String userNameNagiosCollectData, String passWordNagiosCollectData) {
        this.ipNagiosCollectData = ipNagiosCollectData;
        this.userNameNagiosCollectData = userNameNagiosCollectData;
        this.passWordNagiosCollectData = passWordNagiosCollectData;
        this.baseDirectory = baseDirectory;
        createMonitoredVirtualList();
        createMonitoredHostsList();
        this.refreshTime = 1200;
        this.tabContentMap = new LinkedHashMap<String, String>();
    }

    private void createMonitoredHostsList() {
        this.monitoredHostsList = new ArrayList<String>();

        // DISIT DataCenter
        this.monitoredHostsList
                .add("host 147, FUJITSU SIEMENS, PRIMERGY RX300 S4/192.168.0.147");
        this.monitoredHostsList
                .add("host 148, Dell Inc., PowerEdge R510/192.168.0.148");
        this.monitoredHostsList
                .add("host 160, Dell Inc., PowerEdge T110/192.168.0.160");
        this.monitoredHostsList
                .add("host 142, Dell Inc., PowerEdge 2950/192.168.0.142");
        this.monitoredHostsList
                .add("host 161, Dell Inc., PowerEdge T110/192.168.0.161");
        this.monitoredHostsList
                .add("host 162, Dell Inc., PowerEdge T110/192.168.0.162");
        this.monitoredHostsList
                .add("host 164, Dell Inc., PowerEdge T620/192.168.0.164");
        this.monitoredHostsList
                .add("host 163, Dell Inc., PowerEdge T620/192.168.0.163");
        this.monitoredHostsList
                .add("host 165, Dell Inc., PowerEdge T620/192.168.0.165");
        this.monitoredHostsList
                .add("host 143, Dell Inc., PowerEdge R820/192.168.0.143");
        this.monitoredHostsList
                .add("host 141, Dell Inc., PowerEdge 2950/192.168.0.141");
        this.monitoredHostsList
                .add("host 145, HP, ProLiant DL380 G7/192.168.0.145");

    }

    private void createMonitoredVirtualList() {
        this.monitoredVirtualWinList = new ArrayList<String>();
        this.monitoredVirtualLinuxList = new ArrayList<String>();
        // ECLAP
        this.monitoredVirtualWinList.add("eclap.eu-db-running/192.168.0.13");
        this.monitoredVirtualWinList
                .add("eclap-contentgate.eclap.eu-running/192.168.0.96");
        this.monitoredVirtualWinList
                .add("eclap2-64bit.eclap.eu-running/192.168.0.54");
        this.monitoredVirtualLinuxList
                .add("eclap.eu-balancer-ubuntu-11-10-64bit-running/192.168.0.133");
        this.monitoredVirtualWinList
                .add("ebos11-eclap-bo-node11-CLUST-sitemap-running/192.168.0.53");
        this.monitoredVirtualWinList
                .add("eclap-bp64net.eclap.eu-running/192.168.0.132");
        this.monitoredVirtualWinList
                .add("ebos4-eclap-bo-node4-cbir-3-running/192.168.0.73");
        this.monitoredVirtualWinList
                .add("ebos7-eclap-bo-node7-running/192.168.0.49");
        this.monitoredVirtualWinList
                .add("eclap-tomclap.eclap.eu-running/192.168.0.60");
        this.monitoredVirtualWinList.add("ftp.eclap.eu-running/192.168.0.135");
        this.monitoredVirtualLinuxList
                .add("ECLAP-LOD-Solr-INDEX-Ubuntu 64 bit 2-running/192.168.0.125");
        this.monitoredVirtualWinList
                .add("ebos6-SOLR-eclap-bo-node6-running/192.168.0.155");
        this.monitoredVirtualWinList
                .add("ebos0-eclap-bo-scheduler-running/192.168.0.39");

        // SIIMobility
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(1d-esx4)-40-running/192.168.0.40");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(1c-esx4)-92-running/192.168.0.92");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(1b-esx4)-14-running/192.168.0.14");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(6)-42-running/192.168.0.42");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Silk-RDF-100-running/192.168.0.100");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(3)-24-running-tbreconf/192.168.0.24");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Ingestion1-21-Ubuntu64-bit-running/192.168.0.21");
        this.monitoredVirtualWinList
                .add("SiiMobility2-Mobility7-205-servicemap-running/192.168.0.205");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(2)-69-running/192.168.0.69");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Master500-72-running/192.168.0.72");
        this.monitoredVirtualLinuxList
                .add("SiiMobility-Node200(1)-70-running/192.168.0.70");

        // Disit-org
        this.monitoredVirtualWinList.add("disit.org-db-running/192.168.0.75");
        this.monitoredVirtualWinList.add("disit.org-FE-running/192.168.0.76");

        // MobMed
        this.monitoredVirtualWinList
                .add("mobmed.axmedis.org-running/192.168.0.78");

        // MusicNetwork
        this.monitoredVirtualLinuxList
                .add("musicnetwork.dsi.unifi.it-running/192.168.0.241");

        // OpenMind
        this.monitoredVirtualWinList
                .add("openmind.disit.org-running/192.168.0.61");

        // SMNet
        this.monitoredVirtualWinList
                .add("SMNet.disit.org-DMS-running/192.168.0.18");
        this.monitoredVirtualWinList
                .add("SMNet.disit.org-FE-DMS-running/192.168.0.80");

        // Log
        this.monitoredVirtualWinList
                .add("LOG-MediaCrawler.disit.org-running/192.168.0.25");
    }

    @Override
    public void run(){

        this.started = true;
        File collectionDirectory = new File(this.baseDirectory + DIRECTORY);
        if (!collectionDirectory.isDirectory()) {
            Boolean checkMkDir = collectionDirectory.mkdir();
            if(!checkMkDir){
               this.started = false;
               LOGGER.error("There was an error to create the base directory");
            }
        }

        while (this.started) {
            this.refreshTime = 1;
            this.tmpTabContent = "";
            this.timestamp = Utility.getTimeStampForWriteDirectory();

            for (String monitoredVirtual : this.monitoredVirtualWinList) {
                File workingDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredVirtual.replace("/", "@"));
                if (!workingDirectory.isDirectory()) {
                    workingDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-default'><div class='panel-heading'><strong> Host: </strong>"
                        + monitoredVirtual + " </div><div class='panel-body'>";

                File oneDayDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredVirtual.replace("/", "@") + "/"
                        + METRICPERIOD_ONE_DAY);
                if (!oneDayDirectory.isDirectory()) {
                    oneDayDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-success'><div class='panel-heading'><strong> Period: </strong>"
                        + METRICPERIOD_ONE_DAY
                        + " </div><div class='panel-body'>";
                collectMetric(monitoredVirtual, METRICNAME_WIN_CPU_AVG,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_WIN_VOLUMES,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual,
                        METRICNAME_WIN_PHYSICAL_MEMORY_USED,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_WIN_NET_TRAFFIC,
                        METRICPERIOD_ONE_DAY);
                this.tmpTabContent += "</div></div>";

                if (dayCount % 7 == 0) {

                    File sevenDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredVirtual.replace("/", "@")
                            + "/" + METRICPERIOD_SEVEN_DAY);
                    if (!sevenDayDirectory.isDirectory()) {
                        sevenDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-primary'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_SEVEN_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredVirtual, METRICNAME_WIN_CPU_AVG,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_WIN_VOLUMES,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_WIN_PHYSICAL_MEMORY_USED,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_WIN_NET_TRAFFIC,
                            METRICPERIOD_SEVEN_DAY);
                    this.tmpTabContent += "</div></div>";

                }
                if (dayCount % 30 == 0) {

                    File thirtyDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredVirtual.replace("/", "@")
                            + "/" + METRICPERIOD_THIRTY_DAY);
                    if (!thirtyDayDirectory.isDirectory()) {
                        thirtyDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-info'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_THIRTY_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredVirtual, METRICNAME_WIN_CPU_AVG,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_WIN_VOLUMES,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_WIN_PHYSICAL_MEMORY_USED,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_WIN_NET_TRAFFIC,
                            METRICPERIOD_THIRTY_DAY);
                    this.tmpTabContent += "</div></div>";

                    this.tmpTabContent += "<div class='panel panel-danger'><div class='panel-heading'><strong>Description XML</strong> </div><div class='panel-body'>";
                    saveDescriptionXml(monitoredVirtual, METRICNAME_WIN_CPU_AVG);
                    saveDescriptionXml(monitoredVirtual, METRICNAME_WIN_VOLUMES);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_VVMWARE_VM_DISK_IO_ALL);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_WIN_PHYSICAL_MEMORY_USED);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_WIN_NET_TRAFFIC);
                    this.tmpTabContent += "</div></div>";

                }

                this.tmpTabContent += "</div></div>";

            }

            for (String monitoredVirtual : this.monitoredVirtualLinuxList) {
                File workingDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredVirtual.replace("/", "@"));
                if (!workingDirectory.isDirectory()) {
                    workingDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-default'><div class='panel-heading'><strong> Host: </strong>"
                        + monitoredVirtual + " </div><div class='panel-body'>";

                File oneDayDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredVirtual.replace("/", "@") + "/"
                        + METRICPERIOD_ONE_DAY);
                if (!oneDayDirectory.isDirectory()) {
                    oneDayDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-success'><div class='panel-heading'><strong> Period: </strong>"
                        + METRICPERIOD_ONE_DAY
                        + " </div><div class='panel-body'>";
                collectMetric(monitoredVirtual, METRICNAME_LINUX_CPU_AVG,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_LINUX_DISK_USAGE,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual,
                        METRICNAME_LINUX_PHYSICAL_MEMORY_USED,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredVirtual, METRICNAME_LINUX_NET_TRAFFIC,
                        METRICPERIOD_ONE_DAY);
                this.tmpTabContent += "</div></div>";

                if (dayCount % 7 == 0) {

                    File sevenDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredVirtual.replace("/", "@")
                            + "/" + METRICPERIOD_SEVEN_DAY);
                    if (!sevenDayDirectory.isDirectory()) {
                        sevenDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-primary'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_SEVEN_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredVirtual, METRICNAME_LINUX_CPU_AVG,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_DISK_USAGE, METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_PHYSICAL_MEMORY_USED,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_NET_TRAFFIC,
                            METRICPERIOD_SEVEN_DAY);
                    this.tmpTabContent += "</div></div>";

                }
                if (dayCount % 30 == 0) {

                    File thirtyDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredVirtual.replace("/", "@")
                            + "/" + METRICPERIOD_THIRTY_DAY);
                    if (!thirtyDayDirectory.isDirectory()) {
                        thirtyDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-info'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_THIRTY_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredVirtual, METRICNAME_LINUX_CPU_AVG,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_DISK_USAGE,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual, METRICNAME_VVMWARE_VM_DISK_IO_ALL,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_PHYSICAL_MEMORY_USED,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredVirtual,
                            METRICNAME_LINUX_NET_TRAFFIC,
                            METRICPERIOD_THIRTY_DAY);
                    this.tmpTabContent += "</div></div>";

                    this.tmpTabContent += "<div class='panel panel-danger'><div class='panel-heading'><strong>Description XML</strong> </div><div class='panel-body'>";
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_LINUX_CPU_AVG);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_LINUX_DISK_USAGE);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_VVMWARE_VM_DISK_IO_ALL);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_LINUX_PHYSICAL_MEMORY_USED);
                    saveDescriptionXml(monitoredVirtual,
                            METRICNAME_LINUX_NET_TRAFFIC);
                    this.tmpTabContent += "</div></div>";

                }

                this.tmpTabContent += "</div></div>";

            }

            for (String monitoredHost : this.monitoredHostsList) {
                File workingDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredHost.replace("/", "@"));
                if (!workingDirectory.isDirectory()) {
                    workingDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-default'><div class='panel-heading'><strong> Host: </strong>"
                        + monitoredHost + " </div><div class='panel-body'>";

                File oneDayDirectory = new File(this.baseDirectory + DIRECTORY
                        + monitoredHost.replace("/", "@") + "/"
                        + METRICPERIOD_ONE_DAY);
                if (!oneDayDirectory.isDirectory()) {
                    oneDayDirectory.mkdir();
                }

                this.tmpTabContent += "<div class='panel panel-success'><div class='panel-heading'><strong> Period: </strong>"
                        + METRICPERIOD_ONE_DAY
                        + " </div><div class='panel-body'>";
                collectMetric(monitoredHost, METRICNAME_VMWARE_HOST_CPU_MHZ,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredHost,
                        METRICNAME_VMWARE_HOST_STORAGE_USED,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredHost,
                        METRICNAME_VMWARE_HOST_OVERALL_MEMORY,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredHost,
                        METRICNAME_VMWARE_HOST_OVERHEAD_MEMORY,
                        METRICPERIOD_ONE_DAY);
                collectMetric(monitoredHost,
                        METRICNAME_VMWARE_HOST_NET_TRAFFIC,
                        METRICPERIOD_ONE_DAY);
                this.tmpTabContent += "</div></div>";

                if (dayCount % 7 == 0) {

                    File sevenDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredHost.replace("/", "@") + "/"
                            + METRICPERIOD_SEVEN_DAY);
                    if (!sevenDayDirectory.isDirectory()) {
                        sevenDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-primary'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_SEVEN_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_CPU_MHZ,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_STORAGE_USED,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERALL_MEMORY,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERHEAD_MEMORY,
                            METRICPERIOD_SEVEN_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_NET_TRAFFIC,
                            METRICPERIOD_SEVEN_DAY);
                    this.tmpTabContent += "</div></div>";

                }
                if (dayCount % 30 == 0) {

                    File thirtyDayDirectory = new File(this.baseDirectory
                            + DIRECTORY + monitoredHost.replace("/", "@") + "/"
                            + METRICPERIOD_THIRTY_DAY);
                    if (!thirtyDayDirectory.isDirectory()) {
                        thirtyDayDirectory.mkdir();
                    }

                    this.tmpTabContent += "<div class='panel panel-info'><div class='panel-heading'><strong> Period: </strong>"
                            + METRICPERIOD_THIRTY_DAY
                            + " </div><div class='panel-body'>";
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_CPU_MHZ,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_STORAGE_USED,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERALL_MEMORY,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERHEAD_MEMORY,
                            METRICPERIOD_THIRTY_DAY);
                    collectMetric(monitoredHost,
                            METRICNAME_VMWARE_HOST_NET_TRAFFIC,
                            METRICPERIOD_THIRTY_DAY);
                    this.tmpTabContent += "</div></div>";

                    this.tmpTabContent += "<div class='panel panel-danger'><div class='panel-heading'><strong>Description XML</strong> </div><div class='panel-body'>";
                    saveDescriptionXml(monitoredHost,
                            METRICNAME_VMWARE_HOST_CPU_MHZ);
                    saveDescriptionXml(monitoredHost,
                            METRICNAME_VMWARE_HOST_STORAGE_USED);
                    saveDescriptionXml(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERALL_MEMORY);
                    saveDescriptionXml(monitoredHost,
                            METRICNAME_VMWARE_HOST_OVERHEAD_MEMORY);
                    saveDescriptionXml(monitoredHost,
                            METRICNAME_VMWARE_HOST_NET_TRAFFIC);
                    this.tmpTabContent += "</div></div>";

                }

                this.tmpTabContent += "</div></div>";

            }

            this.tabContentMap.put(this.timestamp, this.tmpTabContent);
            this.refreshTime = 1200;
            try {
                Thread.sleep(86400000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }

            dayCount++;
        }

    }

    private void saveDescriptionXml(String monitoredHost, String metricName) {

        try {
            ResponseMessageString responseMessage = getDescriptionXML(
                    monitoredHost, metricName.replace("_", "%20"));
            this.tmpTabContent += "<div class='row'><div class='col-md-offset-3 col-lg-offset-3 col-md-3 col-lg-3 panel panel-warning'><strong> Metric: </strong>"
                    + metricName
                    + "</div><div class='col-md-3 col-lg-3 panel panel-warning'><strong>Get Description XML:</strong> "
                    + responseMessage.getReason() + "</div></div>";
            if (responseMessage.getResponseCode() == 200) {
                Utility.stringToFile(
                        responseMessage.getResponseBody(),
                        this.baseDirectory + DIRECTORY
                                + monitoredHost.replace("/", "@") + "/"
                                + metricName + ".xml");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void collectMetric(String monitoredHost, String metricName,
            String metricPeriod) {

        File timeStampDirectory = new File(this.baseDirectory + DIRECTORY
                + monitoredHost.replace("/", "@") + "/" + metricPeriod + "/"
                + this.timestamp);

        try {
            ResponseMessageString responseMessage = collectDataXML(
                    monitoredHost, metricName.replace("_", "%20"), metricPeriod);
            this.tmpTabContent += "<div class='row'><div class='col-md-offset-2 col-lg-offset-2 col-md-4 col-lg-4 panel panel-warning'><strong> Metric: </strong>"
                    + metricName
                    + "</div><div class='col-md-2 col-lg-2 panel panel-warning'> <strong>Get XML:</strong> "
                    + responseMessage.getReason() + "</div>  ";
            if (responseMessage.getResponseCode() == 200) {
                if (!timeStampDirectory.isDirectory()) {
                    Boolean checkMkDir = timeStampDirectory.mkdir();
                    if(!checkMkDir){
                       this.started = false;
                       LOGGER.error("There was an error to create the timeStamp directory");
                    }
                }
                Utility.stringToFile(responseMessage.getResponseBody(),
                        timeStampDirectory.getAbsolutePath() + "/" + metricName
                                + ExtensionFileRepository.XML);
            }
            ResponseMessageByteArray responseMessageInputStream = collectDataPNG(
                    monitoredHost, metricName.replace("_", "%20"), metricPeriod);
            this.tmpTabContent += "<div class='col-md-2 col-lg-2 panel panel-warning'><strong>Get PNG:</strong> "
                    + responseMessage.getReason() + " ";
            if (responseMessageInputStream.getResponseCode() == 200) {
                if (!timeStampDirectory.isDirectory()) {
                    timeStampDirectory.mkdir();
                }
                Utility.byteArrayToFile(
                        responseMessageInputStream.getResponseBody(),
                        timeStampDirectory.getAbsolutePath() + "/" + metricName
                                + ExtensionFileRepository.PNG);

                this.tmpTabContent += "<button class='btn btn-default btn-xs' data-toggle='modal' data-target='#modal"
                        + metricCollected
                        + "'>View</button></div><div class='modal fade' id='modal"
                        + metricCollected
                        + "' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'> <div class='modal-dialog'><div class='modal-content'><div class='modal-body'><button type='button' class='close' data-dismiss='modal'aria-hidden='true'>&times;</button><img width='100%' src='"
                        + DIRECTORY
                        + monitoredHost.replace("/", "@")
                        + "/"
                        + metricPeriod
                        + "/"
                        + this.timestamp
                        + "/"
                        + metricName
                        + ExtensionFileRepository.PNG
                        + "'/></div></div></div>";
                metricCollected++;
            }
            this.tmpTabContent += "</div></div>";

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void stopDataCollector() {
        this.started = false;
    }

    private ResponseMessageString getDescriptionXML(final String hostName,
            final String metricName) throws IOException {
        return RestAPI.receiveString(RestAPI.PROTOCOL
                + this.ipNagiosCollectData + RestAPIRepository.SM_MONITOR_INFO
                + "/" + hostName + "/" + metricName,
                this.userNameNagiosCollectData, this.passWordNagiosCollectData,
                RestAPI.APP_XML, CharsetRepository.UTF8);
    }

    private ResponseMessageString collectDataXML(final String hostName,
            final String metricName, final String metricPeriod)
            throws IOException {
        return RestAPI.receiveString(RestAPI.PROTOCOL
                + this.ipNagiosCollectData + RestAPIRepository.SM_MONITOR_XML
                + "/" + hostName + "/" + metricName + "/-" + metricPeriod,
                this.userNameNagiosCollectData, this.passWordNagiosCollectData,
                RestAPI.APP_XML, CharsetRepository.UTF8);
    }

    private ResponseMessageByteArray collectDataPNG(final String hostName,
            final String metricName, final String metricPeriod)
            throws IOException {

        return RestAPI.receiveByteArray(RestAPI.PROTOCOL
                + this.ipNagiosCollectData + RestAPIRepository.SM_MONITOR_IMG
                + "/" + hostName + "/" + metricName + "/-" + metricPeriod,
                this.userNameNagiosCollectData, this.passWordNagiosCollectData,
                RestAPI.IMAGE_PNG);
    }

    public String getipNagiosCollectData() {
        return this.ipNagiosCollectData;
    }

    public void setipNagiosCollectData(String ipNagiosCollectData) {
        this.ipNagiosCollectData = ipNagiosCollectData;
    }

    public String getuserNameNagiosCollectData() {
        return this.userNameNagiosCollectData;
    }

    public void setuserNameNagiosCollectData(String userNameNagiosCollectData) {
        this.userNameNagiosCollectData = userNameNagiosCollectData;
    }

    public String getpassWordNagiosCollectData() {
        return this.passWordNagiosCollectData;
    }

    public void setpassWordNagiosCollectData(String passWordNagiosCollectData) {
        this.passWordNagiosCollectData = passWordNagiosCollectData;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Map<String, String> getTabContentMap() {
        return this.tabContentMap;
    }

    public void setTabContentMap(Map<String, String> tabContentMap) {
        this.tabContentMap = tabContentMap;
    }

    public Integer getRefreshTime() {
        return this.refreshTime;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime;
    }

}
