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

package org.cloudsimulator.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.stamfest.rrd.RRDp;

import org.cloudsimulator.controller.CollectorController;
import org.cloudsimulator.domain.NagiosRrdDescription;
import org.cloudsimulator.domain.NagiosServer;
import org.cloudsimulator.domain.XportRrd;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.ExtensionFileRepository;
import org.cloudsimulator.thread.DataCollector;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.utility.filter.DirectoryFilter;
import org.cloudsimulator.utility.filter.PngFilter;
import org.cloudsimulator.utility.filter.XmlFilter;
import org.cloudsimulator.xml.rrd.XmlRrdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DataCenterSimulatorFaster implements Runnable, Serializable {

    private static final long serialVersionUID = -2118996114585804883L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataCenterSimulatorFaster.class);
    private static final String DATEFORMATFORCALENDAR = "yyyy-MM-dd HH:mm:ss";
    private static final String AT = "@";
    private static final String PREFIXFILE = "SNMP";
    private static final String METRICCPUAVG = "CPU_AVG";
    // private static final String METRICNETTRAFFIC = "Net_Traffic";
    private static final String METRICPHYSICALMEMORYUSED = "Physical_Memory_Used";
    private static final String METRICVOLUMES = "Volumes";
    private static final String METRICDISKUSAGE = "Disk_Usage";
    public static final String DIRECTORY = "rrd/";
    private Map<String, List<String>> virtualMachinePatternMap;
    private Integer refreshTime;
    private DataCenter dataCenter;
    private Integer days;
    private Date dateFrom;
    private Float percentualHyperVisor = 0.10f;
    private Integer completedSimulation = 0;
    int step = 300;
    int sampleToSimulate;
    private String monitorString = "";
    private String baseDirectory = CollectorController.BASE_DIRECTORY;
    private NagiosServer nagiosServer;
    private boolean existsCollectedData;

    public DataCenterSimulatorFaster(NagiosServer nagiosServer) {
        super();
        this.nagiosServer = nagiosServer;
        this.refreshTime = 1200;
        this.dateFrom = Calendar.getInstance().getTime();
        this.days = 7;
        this.virtualMachinePatternMap = new HashMap<String, List<String>>();

    }

    @Override
    public void run() {
        long startDate = this.dateFrom.getTime() / 1000;
        sampleToSimulate = 288 * this.days;
        File rrdDirectory = new File(this.baseDirectory + DIRECTORY);
        if (!rrdDirectory.isDirectory()) {
            rrdDirectory.mkdir();
        }

        for (HostMachine hostMachine : this.dataCenter.getHostMachineList()) {

            List<Float> currentWorkLoadHostCPUMhz = new ArrayList<Float>();
            List<Float> currentWorkLoadHostPhysicalMemoryGB = new ArrayList<Float>();
            List<Float> currentWorkLoadHostVolumesGB = new ArrayList<Float>();

            for (int m = 0; m < this.sampleToSimulate; m++) {
                currentWorkLoadHostCPUMhz.add(this.percentualHyperVisor
                        * hostMachine.getHasCPUCount()
                        * hostMachine.getHasCPUSpeed());
                currentWorkLoadHostPhysicalMemoryGB
                        .add(this.percentualHyperVisor
                                * hostMachine.getHasMemorySize());
                currentWorkLoadHostVolumesGB.add(0F);
            }

            for (VirtualMachine virtualMachine : hostMachine
                    .getVirtualMachineList()) {
                String nameDirectoryVirtualMachine = virtualMachine
                        .getHasName()
                        + AT
                        + virtualMachine.getHasMonitorIPAddress() + "/";
                File workingDirectoryVirtualMachine = new File(
                        this.baseDirectory + DIRECTORY
                                + nameDirectoryVirtualMachine);
                if (!workingDirectoryVirtualMachine.isDirectory()) {
                    workingDirectoryVirtualMachine.mkdir();
                }

                RRDp rrdPVirtualMachine = null;
                try {
                    rrdPVirtualMachine = new RRDp(this.baseDirectory
                            + DIRECTORY + nameDirectoryVirtualMachine, null);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }

                if (rrdPVirtualMachine != null) {
                    saveSampleForVM(nameDirectoryVirtualMachine,
                            rrdPVirtualMachine, METRICCPUAVG, "%%",
                            virtualMachine.getHasCPUSpeedLimit(),
                            virtualMachine, startDate,
                            currentWorkLoadHostCPUMhz);
                    saveSampleForVM(nameDirectoryVirtualMachine,
                            rrdPVirtualMachine, METRICPHYSICALMEMORYUSED, "MB",
                            virtualMachine.getHasMemoryLimit() * 1000,
                            virtualMachine, startDate,
                            currentWorkLoadHostPhysicalMemoryGB);
                    if (virtualMachine.getHasOS().contains("windows")) {
                        saveSampleForVM(nameDirectoryVirtualMachine,
                                rrdPVirtualMachine, METRICVOLUMES, "MB",
                                virtualMachine.getStorage(), virtualMachine,
                                startDate, currentWorkLoadHostVolumesGB);
                    } else {
                        saveSampleForVM(nameDirectoryVirtualMachine,
                                rrdPVirtualMachine, METRICDISKUSAGE, "MB",
                                virtualMachine.getStorage(), virtualMachine,
                                startDate, currentWorkLoadHostVolumesGB);
                    }
                }

                this.monitorString += "> Simulate virtualMachine: <strong>"
                        + virtualMachine.getHasName() + "</strong><br />";

                try {
                    this.monitorString += "-> Sending to nagiosService file of virtualMachine: <strong>"
                            + virtualMachine.getHasName() + "</strong><br />";
                    sendRRDToNagiosServer(nameDirectoryVirtualMachine,
                            virtualMachine.getHasOS(), METRICCPUAVG);
                    sendRRDToNagiosServer(nameDirectoryVirtualMachine,
                            virtualMachine.getHasOS(), METRICPHYSICALMEMORYUSED);
                    if (virtualMachine.getHasOS().contains("windows")) {
                        sendRRDToNagiosServer(nameDirectoryVirtualMachine,
                                virtualMachine.getHasOS(), METRICVOLUMES);
                    } else {
                        sendRRDToNagiosServer(nameDirectoryVirtualMachine,
                                virtualMachine.getHasOS(), METRICDISKUSAGE);
                    }
                } catch (JSchException e) {
                    LOGGER.error(e.getMessage(), e);
                }

                this.monitorString += "<hr />";

                this.completedSimulation = (int) (100F * (((float) this.dataCenter
                        .getHostMachineList().indexOf(hostMachine)) / this.dataCenter
                        .getHostMachineList().size()) + (((float) hostMachine
                        .getVirtualMachineList().indexOf(virtualMachine) + 1)
                        / hostMachine.getVirtualMachineList().size() * 100F * (1F / this.dataCenter
                        .getHostMachineList().size())));

            }

            String nameDirectoryHostMachine = hostMachine.getHasName() + AT
                    + hostMachine.getHasMonitorIPAddress() + "/";
            File workingDirectoryHostMachine = new File(this.baseDirectory
                    + DIRECTORY + nameDirectoryHostMachine);

            if (!workingDirectoryHostMachine.isDirectory()) {
                workingDirectoryHostMachine.mkdir();
            }

            RRDp rrdPHostMachine = null;
            try {
                rrdPHostMachine = new RRDp(this.baseDirectory + DIRECTORY
                        + nameDirectoryHostMachine, null);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }

            if (rrdPHostMachine != null) {
                saveSampleForHM(nameDirectoryHostMachine, rrdPHostMachine,
                        METRICCPUAVG, hostMachine.getHasCPUCount()
                                * hostMachine.getHasCPUSpeed(), hostMachine,
                        startDate, currentWorkLoadHostCPUMhz);
                saveSampleForHM(nameDirectoryHostMachine, rrdPHostMachine,
                        METRICPHYSICALMEMORYUSED,
                        hostMachine.getHasMemorySize(), hostMachine, startDate,
                        currentWorkLoadHostPhysicalMemoryGB);
                if (hostMachine.getHasOS().contains("windows")) {
                    saveSampleForHM(nameDirectoryHostMachine, rrdPHostMachine,
                            METRICVOLUMES, hostMachine.getStorage(),
                            hostMachine, startDate,
                            currentWorkLoadHostVolumesGB);
                } else {
                    saveSampleForHM(nameDirectoryHostMachine, rrdPHostMachine,
                            METRICDISKUSAGE, hostMachine.getStorage(),
                            hostMachine, startDate,
                            currentWorkLoadHostVolumesGB);
                }

            }

            this.monitorString += "> Simulate hostMachine: <strong>"
                    + hostMachine.getHasName() + "</strong><br />";

            try {
                this.monitorString += "-> Sending to nagiosService file of hostMachine: <strong>"
                        + hostMachine.getHasName() + "</strong><br />";
                sendRRDToNagiosServer(nameDirectoryHostMachine,
                        hostMachine.getHasOS(), METRICCPUAVG);
                sendRRDToNagiosServer(nameDirectoryHostMachine,
                        hostMachine.getHasOS(), METRICPHYSICALMEMORYUSED);
                if (hostMachine.getHasOS().contains("windows")) {
                    sendRRDToNagiosServer(nameDirectoryHostMachine,
                            hostMachine.getHasOS(), METRICVOLUMES);
                } else {
                    sendRRDToNagiosServer(nameDirectoryHostMachine,
                            hostMachine.getHasOS(), METRICDISKUSAGE);
                }

            } catch (JSchException e) {
                LOGGER.error(e.getMessage(), e);
            }

            this.monitorString += "<hr />";

            this.completedSimulation = (int) (((float) 100) * (((float) this.dataCenter
                    .getHostMachineList().indexOf(hostMachine) + 1) / this.dataCenter
                    .getHostMachineList().size()));

        }

        this.refreshTime = 1200;

    }

    private Session openSession() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(nagiosServer.getUser(),
                nagiosServer.getIpAddress(), nagiosServer.getIntPort());
        session.setPassword(nagiosServer.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);

        return session;
    }

    private void sendRRDToNagiosServer(String workingDirectory, String hasOS,
            String resource) throws JSchException{
        Session session = openSession();
        Channel channel = session.openChannel("exec");
        String command = "mkdir " + nagiosServer.getDirectoryInside()
                + workingDirectory + "; scp -t "
                + nagiosServer.getDirectoryInside() + workingDirectory;
        ((ChannelExec) channel).setCommand(command);

        FileInputStream fileInputStream = null;
        
        try{
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();
        checkAck(in);

        String osLabel = createOSLabel(hasOS);
        String[] fileExtension = { ExtensionFileRepository.RRD,
                ExtensionFileRepository.XML };

        for (int i = 0; i < fileExtension.length; i++) {
            String fileName = this.baseDirectory + DIRECTORY + workingDirectory
                    + PREFIXFILE + "_" + osLabel + "_" + resource
                    + fileExtension[i];
            File fileToSend = new File(fileName);

            long filesize = fileToSend.length();
            command = "C0644 " + filesize + " " + PREFIXFILE + "_" + osLabel
                    + "_" + resource + fileExtension[i] + "\n";
            out.write(command.getBytes(Charset.forName(CharsetRepository.UTF8)));
            out.flush();
            checkAck(in);

            // send a content of lfile
            fileInputStream = new FileInputStream(fileName);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fileInputStream.read(buf, 0, buf.length);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, len);
            }
            fileInputStream.close();
            fileInputStream = null;
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            checkAck(in);

        }
        out.close();
        } catch (IOException e){
            LOGGER.error(e.getMessage(), e);
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        

        channel.disconnect();
        session.disconnect();

        this.monitorString += "--> Send to nagiosService " + resource
                + " file: <strong>OK</strong><br />";

        // Remove comment for windows system, remove comment also in dumpRRD
        // fixRRDFilesOnNagiosServer(workingDirectory, hasOS, resource);

    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0)
            return b;
        if (b == -1)
            return b;

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                LOGGER.error(sb.toString());
            }
            if (b == 2) { // fatal error
                LOGGER.error(sb.toString());
            }
        }
        return b;
    }

    private void saveSampleForVM(String nameWorkingDirectory,
            RRDp rrdPVirtualMachine, String resource, String unit,
            Float maxLimitResource, VirtualMachine virtualMachine,
            long startDate, List<Float> currentWorkLoadHost) {
        String hasName = virtualMachine.getHasName();
        String hasMonitorIPAddress = virtualMachine.getHasMonitorIPAddress();

        String osLabel = createOSLabel(virtualMachine.getHasOS());

        File rrdFileVirtualMachine = new File(baseDirectory + DIRECTORY
                + nameWorkingDirectory + PREFIXFILE + "_" + osLabel + "_"
                + resource + ExtensionFileRepository.RRD);

        if (!rrdFileVirtualMachine.exists()) {
            createRRD(rrdPVirtualMachine, PREFIXFILE + "_" + osLabel + "_"
                    + resource + ExtensionFileRepository.RRD, startDate,
                    this.step);
        }

        List<Float> sampleList;
        List<String> pathOfPattern = this.virtualMachinePatternMap.get(hasName);
        if (pathOfPattern == null) {
            pathOfPattern = getRandomPattern(this.baseDirectory
                    + DataCollector.DIRECTORY);
            addPatternToVirtualMachine(hasName, pathOfPattern);
        }

        sampleList = getPatternValue(pathOfPattern, resource,
                this.sampleToSimulate);
        if (sampleList.isEmpty() && resource.equals(METRICDISKUSAGE)) {
            sampleList = getPatternValue(pathOfPattern, METRICVOLUMES,
                    this.sampleToSimulate);
        }

        for (int j = 0; j < this.sampleToSimulate; j++) {
            updateRRD(rrdPVirtualMachine, PREFIXFILE + "_" + osLabel + "_"
                    + resource + ExtensionFileRepository.RRD, startDate
                    + (j * this.step), sampleList.get(j));
            currentWorkLoadHost.set(j,
                    currentWorkLoadHost.get(j) + sampleList.get(j)
                            * maxLimitResource / 100);
        }
        // Remove comment for windows system
        /*
         * dumpRRD(rrdPVirtualMachine, PREFIXFILE + "_" + osLabel + "_" +
         * resource + ExtensionFileRepository.RRD);
         */

        try {
            Utility.stringToFile(
                    createXMLDescriptionForRRD(nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.RRD, nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.XML,
                            sampleList.get(sampleToSimulate - 1), unit,
                            startDate + this.sampleToSimulate * this.step,
                            hasName, hasMonitorIPAddress, PREFIXFILE + "_"
                                    + osLabel + "_" + resource),
                    this.baseDirectory + DIRECTORY + nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.XML);
        } catch (ParserConfigurationException |TransformerException  e) {
            LOGGER.error(e.getMessage(), e);
        } 
    }

    private void saveSampleForHM(String nameWorkingDirectory,
            RRDp rrdPHostMachine, String resource, Float maxLimitResource,
            HostMachine hostMachine, long startDate, List<Float> values) {
        String hasName = hostMachine.getHasName();
        String hasMonitorIPAddress = hostMachine.getHasMonitorIPAddress();

        String osLabel = createOSLabel(hostMachine.getHasOS());

        File rrdFileHostMachine = new File(baseDirectory + DIRECTORY
                + nameWorkingDirectory + PREFIXFILE + "_" + osLabel + "_"
                + resource + ExtensionFileRepository.RRD);

        if (!rrdFileHostMachine.exists()) {
            createRRD(rrdPHostMachine, PREFIXFILE + "_" + osLabel + "_"
                    + resource + ExtensionFileRepository.RRD, startDate,
                    this.step);
        }

        for (int j = 0; j < this.sampleToSimulate; j++) {
            updateRRD(rrdPHostMachine, PREFIXFILE + "_" + osLabel + "_"
                    + resource + ExtensionFileRepository.RRD, startDate
                    + (j * this.step), values.get(j).floatValue()
                    / (maxLimitResource) * 100
                    * (1 + this.percentualHyperVisor));

        }

        // Remove comment for windows system
        /*
         * dumpRRD(rrdPHostMachine, PREFIXFILE + "_" + osLabel + "_" + resource
         * + ExtensionFileRepository.RRD);
         */

        try {
            Utility.stringToFile(
                    createXMLDescriptionForRRD(nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.RRD, nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.XML,
                            values.get(this.sampleToSimulate - 1)
                                    / (maxLimitResource) * 100
                                    * (1 + percentualHyperVisor), "%%",
                            startDate + this.sampleToSimulate * this.step,
                            hasName, hasMonitorIPAddress, PREFIXFILE + "_"
                                    + osLabel + "_" + resource),
                    this.baseDirectory + DIRECTORY + nameWorkingDirectory
                            + PREFIXFILE + "_" + osLabel + "_" + resource
                            + ExtensionFileRepository.XML);
        } catch (ParserConfigurationException | TransformerException e) {
            LOGGER.error(e.getMessage(), e);
        } 

    }

    private void createRRD(RRDp rrdP, String fileName, long startDate, int step) {
        String[] commandCreate = { "create", fileName, "--start",
                String.valueOf(startDate), "--step", String.valueOf(step),
                "DS:1:GAUGE:8640:U:U", "RRA:AVERAGE:0.5:1:2880",
                "RRA:AVERAGE:0.5:5:2880", "RRA:AVERAGE:0.5:30:4320",
                "RRA:AVERAGE:0.5:360:5840", "RRA:MIN:0.5:1:2880",
                "RRA:MIN:0.5:5:2880", "RRA:MIN:0.5:30:4320",
                "RRA:MIN:0.5:360:5840", "RRA:MAX:0.5:1:2880",
                "RRA:MAX:0.5:5:2880", "RRA:MAX:0.5:30:4320",
                "RRA:MAX:0.5:360:5840" };
        try {
            rrdP.command(commandCreate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void updateRRD(RRDp rrdP, String fileName, long valueDate,
            Float value) {
        String[] commandUpdate = { "update", fileName, valueDate + ":" + value };
        try {
            rrdP.command(commandUpdate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    // Remove comment for windows system
    /*
     * private void dumpRRD(RRDp rrdP, String fileName) { String[] commandDump =
     * { "dump", fileName, fileName + ExtensionFileRepository.XML }; try {
     * rrdP.command(commandDump); } catch (Exception e) {
     * LOGGER.error(e.getMessage(), e); } }
     */

    /*
     * private void fixRRDFilesOnNagiosServer(String workingDirectory, String
     * hasOS, String resource) throws JSchException, IOException { Session
     * session = openSession(); Channel channel = session.openChannel("exec");
     * String osLabel = createOSLabel(hasOS); String command =
     * "rrdtool restore -f " + nagiosServer.getDirectoryInside() +
     * workingDirectory + PREFIXFILE + "_" + osLabel + "_" + resource +
     * ExtensionFileRepository.RRDXML + " " + nagiosServer.getDirectoryInside()
     * + workingDirectory + PREFIXFILE + "_" + osLabel + "_" + resource +
     * ExtensionFileRepository.RRD + "; rm " + nagiosServer.getDirectoryInside()
     * + workingDirectory + PREFIXFILE + "_" + osLabel + "_" + resource +
     * ExtensionFileRepository.RRDXML + " "; ((ChannelExec)
     * channel).setCommand(command);
     * 
     * channel.connect();
     * 
     * channel.disconnect(); session.disconnect();
     * 
     * }
     */

    private String createXMLDescriptionForRRD(String rrdFileName,
            String xmlFileName, float currentValue, String unit,
            long currentTime, String machineName, String machineIP,
            String nagiosServiceDesc) throws ParserConfigurationException,
            TransformerException {

        NagiosRrdDescription nagiosRrdDescription = new NagiosRrdDescription();

        nagiosRrdDescription.getDataSourceMap().put("TEMPLATE", "templateName");
        nagiosRrdDescription.getDataSourceMap().put("RRDFILE",
                this.nagiosServer.getDirectoryInside() + rrdFileName);
        nagiosRrdDescription.getDataSourceMap().put("RRD_STORAGE_TYPE",
                "SINGLE");
        nagiosRrdDescription.getDataSourceMap().put("RRD_HEARTBEAT", "8640");
        nagiosRrdDescription.getDataSourceMap().put("IS_MULTI", "0");
        nagiosRrdDescription.getDataSourceMap().put("DS", "1");
        nagiosRrdDescription.getDataSourceMap().put(
                "NAME",
                nagiosServiceDesc.substring(nagiosServiceDesc.indexOf('_',
                        nagiosServiceDesc.indexOf('_') + 1) + 1));
        nagiosRrdDescription.getDataSourceMap().put(
                "LABEL",
                nagiosServiceDesc.substring(
                        nagiosServiceDesc.indexOf('_',
                                nagiosServiceDesc.indexOf('_') + 1) + 1)
                        .replace('_', ' '));
        nagiosRrdDescription.getDataSourceMap().put("UNIT", unit);
        nagiosRrdDescription.getDataSourceMap().put("ACT",
                Float.toString(currentValue));
        nagiosRrdDescription.getDataSourceMap().put("WARN", "70");
        nagiosRrdDescription.getDataSourceMap().put("WARN_MIN", "");
        nagiosRrdDescription.getDataSourceMap().put("WARN_MAX", "");
        nagiosRrdDescription.getDataSourceMap().put("WARN_RANGE_TYPE", "");
        nagiosRrdDescription.getDataSourceMap().put("CRIT", "80");
        nagiosRrdDescription.getDataSourceMap().put("CRIT_MIN", "");
        nagiosRrdDescription.getDataSourceMap().put("CRIT_MAX", "");
        nagiosRrdDescription.getDataSourceMap().put("CRIT_RANGE_TYPE", "");
        nagiosRrdDescription.getDataSourceMap().put("MIN", "");
        nagiosRrdDescription.getDataSourceMap().put("MAX", "");

        nagiosRrdDescription.getRrdMap().put("RC", "0");
        nagiosRrdDescription.getRrdMap().put("TXT", "successful updated");

        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_AUTH_HOSTNAME",
                machineName + AT + machineIP);
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_AUTH_SERVICEDESC",
                nagiosServiceDesc.replace('_', ' '));
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_CHECK_COMMAND",
                "checkCommand");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_DATATYPE",
                "SERVICEPERFDATA");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_DISP_HOSTNAME",
                machineName + AT + machineIP);
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_DISP_SERVICEDESC",
                nagiosServiceDesc.replace('_', ' '));
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_HOSTNAME",
                machineName + AT + machineIP);
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_HOSTSTATE", "UP");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_HOSTSTATETYPE",
                "HARD");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_MULTI_PARENT", "");
        nagiosRrdDescription.getNagiosMetaMap().put(
                "NAGIOS_PERFDATA",
                nagiosServiceDesc.substring(
                        nagiosServiceDesc.indexOf('_',
                                nagiosServiceDesc.indexOf('_') + 1) + 1)
                        .replace('_', ' ')
                        + "=" + currentValue + unit + ";70;80 ");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_RRDFILE",
                this.nagiosServer.getDirectoryInside() + rrdFileName);
        nagiosRrdDescription.getNagiosMetaMap().put(
                "NAGIOS_SERVICECHECKCOMMAND", "serviceCheckCommand");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_SERVICEDESC",
                nagiosServiceDesc);
        nagiosRrdDescription.getNagiosMetaMap().put(
                "NAGIOS_SERVICEPERFDATA",
                nagiosServiceDesc.substring(
                        nagiosServiceDesc.indexOf('_',
                                nagiosServiceDesc.indexOf('_') + 1) + 1)
                        .replace('_', ' ')
                        + "=" + currentValue + unit + ";70;80 ");
        nagiosRrdDescription.getNagiosMetaMap()
                .put("NAGIOS_SERVICESTATE", "OK");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_SERVICESTATETYPE",
                "HARD");
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_TIMET",
                String.valueOf(currentTime));
        nagiosRrdDescription.getNagiosMetaMap().put("NAGIOS_XMLFILE",
                this.nagiosServer.getDirectoryInside() + xmlFileName);

        nagiosRrdDescription.getXmlMap().put("VERSION", "4");

        Document documentXml = XmlRrdConverter
                .createXmlFromNagiosRrdDescription(nagiosRrdDescription);

        return finishAndCleanDocumentXmlForRRD(documentXml);

    }

    private static String finishAndCleanDocumentXmlForRRD(
            final Document documentXml) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING,
                CharsetRepository.UTF8);
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        StringWriter streamWriter = new StringWriter();
        transformer.transform(new DOMSource(documentXml), new StreamResult(
                streamWriter));

        return streamWriter.toString();
    }

    public void addPatternToVirtualMachine(String hasNameVirtualMachine,
            List<String> pathPattern) {
        this.virtualMachinePatternMap.put(hasNameVirtualMachine, pathPattern);
    }

    public boolean checkPattern(String hasNameVirtualMachine) {
        if (this.virtualMachinePatternMap.containsKey(hasNameVirtualMachine)) {
            return true;
        }
        return false;
    }

    private List<Float> getPatternValue(List<String> pathOfPattern,
            String metricType, int sampleToSimulate) {

        File xmlFile = null;
        for (int i = 0; i < pathOfPattern.size(); i++) {
            if (pathOfPattern.get(i).contains(metricType)) {
                xmlFile = new File(this.baseDirectory
                        + pathOfPattern
                                .get(i)
                                .replace(ExtensionFileRepository.PNG,
                                        ExtensionFileRepository.XML)
                                .replace("../", ""));
            }
        }

        return createPatternValueFromFile(xmlFile, sampleToSimulate);
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

    private List<Float> createPatternValueFromFile(File xmlFile,
            int sampleToSimulate) {
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

            NagiosRrdDescription nagiosRrdDescription = XmlRrdConverter
                    .createNagiosRrdDescriptionFromXml(new File(
                            this.baseDirectory
                                    + DataCollector.DIRECTORY
                                    + xmlFile
                                            .getAbsolutePath()
                                            .replace(
                                                    this.baseDirectory
                                                            + DataCollector.DIRECTORY,
                                                    "")
                                            .substring(
                                                    0,
                                                    xmlFile.getAbsolutePath()
                                                            .replace(
                                                                    this.baseDirectory
                                                                            + DataCollector.DIRECTORY,
                                                                    "")
                                                            .indexOf("/"))
                                    + xmlFile
                                            .getAbsolutePath()
                                            .replace(
                                                    this.baseDirectory
                                                            + DataCollector.DIRECTORY,
                                                    "")
                                            .substring(
                                                    xmlFile.getAbsolutePath()
                                                            .replace(
                                                                    this.baseDirectory
                                                                            + DataCollector.DIRECTORY,
                                                                    "")
                                                            .lastIndexOf("/"))));

            Float maxValue = 0F;
            if (nagiosRrdDescription.getDataSourceMap().get("UNIT")
                    .contains("%")) {
                maxValue = 1F;
            } else {
                maxValue = 100F / Float.valueOf(nagiosRrdDescription
                        .getDataSourceMap().get("MAX"));
            }

            Collection<List<Float>> patternValueCollection = xportRrd
                    .getDataMap().values();
            int numberRepeat = (int) Math.ceil(((double) sampleToSimulate)
                    / ((double) patternValueCollection.size()));
            boolean finished = false;
            for (int i = 0; i < numberRepeat && !finished; i++) {
                for (List<Float> patternValue : patternValueCollection) {
                    // Moltiplico per maxValue anzich� dividere perch� ho
                    // spostato la moltiplicazione per 100 che si deve fare nel
                    // calcolo della percentuale all'interno del calcolo di
                    // maxValue.
                    // In questo modo non devo scrivere due righe di codice
                    // diverse per i valori che mi sono gi� in percentuale.
                    patternList.add(patternValue.get(indexAverageValue)
                            * maxValue);
                    if (patternList.size() == sampleToSimulate) {
                        finished = true;
                        break;
                    }
                }
            }
        }
        return patternList;
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

    private String createOSLabel(String hasOS) {
        if (hasOS.contains("windows")) {
            return "WIN";
        } else if (hasOS.contains("vmware_esxi")) {
            return "ESX";
        } else {
            return "Linux";
        }
    }

    public Map<String, List<String>> getVirtualMachinePatternMap() {
        return this.virtualMachinePatternMap;
    }

    public void setVirtualMachinePatternMap(
            Map<String, List<String>> virtualMachinePatternMap) {
        this.virtualMachinePatternMap = virtualMachinePatternMap;
    }

    public String getDATEFORMATFORCALENDAR() {
        return DATEFORMATFORCALENDAR;
    }

    public Integer getRefreshTime() {
        return this.refreshTime;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime;
    }

    public DataCenter getDataCenter() {
        return this.dataCenter;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public Integer getDays() {
        return this.days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getDateFrom() {
        return (Date) this.dateFrom.clone();
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = new Date(dateFrom.getTime());
    }

    public Integer getCompletedSimulation() {
        return this.completedSimulation;
    }

    public void setCompletedSimulation(Integer completedSimulation) {
        this.completedSimulation = completedSimulation;
    }

    public String getMonitorString() {
        return this.monitorString;
    }

    public void setMonitorString(String monitorString) {
        this.monitorString = monitorString;
    }

    public NagiosServer getNagiosServer() {
        return this.nagiosServer;
    }

    public void setNagiosServer(NagiosServer nagiosServer) {
        this.nagiosServer = nagiosServer;
    }

    public boolean isExistsCollectedData() {
        return existsCollectedData;
    }

    public void setExistsCollectedData(boolean existsCollectedData) {
        this.existsCollectedData = existsCollectedData;
    }

}
