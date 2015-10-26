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

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.persistence.KBDAO;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.xml.rdf.XmlRdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class DataCenterMetricSender implements Runnable, Serializable {

    private static final long serialVersionUID = -3788085632772075986L;
    private Integer periodTimeSend = 5;
    private boolean started;
    private DataCenterSimulatorRealTime dataCenterSimulator;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataCenterMetricSender.class);

    public DataCenterMetricSender(
            final DataCenterSimulatorRealTime dataCenterSimulator) {
        this.dataCenterSimulator = dataCenterSimulator;
    }

    @Override
    public void run() {
        LOGGER.info("Start: DataCenterMetricSender");
        XmlRdfCreator xmlRdfCreator = new XmlRdfCreator();
        this.started = true;
        while (this.started) {
            String serviceMetricXML = "";
            try {
                for (HostMachineSimulator hostMachineSimulator : this.dataCenterSimulator
                        .getHostMachineSimulatorList()) {
                    if ("".equals(serviceMetricXML)) {
                        serviceMetricXML = xmlRdfCreator
                                .createServiceMetricXmlRdf(new ServiceMetric(
                                        Utility.getTimeStamp(),
                                        "avgCPUPuntual", hostMachineSimulator
                                                .getCpuMhzActuallyUsed(),
                                        "Mhz", hostMachineSimulator
                                                .getUriSimulatedEntity()));
                    } else {
                        serviceMetricXML = new StringBuilder(serviceMetricXML)
                                .insert(serviceMetricXML.length() - 12,
                                        xmlRdfCreator
                                                .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                        Utility.getTimeStamp(),
                                                        "avgCPUPuntual",
                                                        hostMachineSimulator
                                                                .getCpuMhzActuallyUsed(),
                                                        "Mhz",
                                                        hostMachineSimulator
                                                                .getUriSimulatedEntity())))
                                .toString();
                    }

                    serviceMetricXML = new StringBuilder(serviceMetricXML)
                            .insert(serviceMetricXML.length() - 12,
                                    xmlRdfCreator
                                            .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                    Utility.getTimeStamp(),
                                                    "avgMemoryPuntual",
                                                    hostMachineSimulator
                                                            .getMemoryActuallyUsed(),
                                                    "GB",
                                                    hostMachineSimulator
                                                            .getUriSimulatedEntity())))
                            .toString();

                    serviceMetricXML = new StringBuilder(serviceMetricXML)
                            .insert(serviceMetricXML.length() - 12,
                                    xmlRdfCreator
                                            .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                    Utility.getTimeStamp(),
                                                    "avgStoragePuntual",
                                                    hostMachineSimulator
                                                            .getStorageActuallyUsed(),
                                                    "GB",
                                                    hostMachineSimulator
                                                            .getUriSimulatedEntity())))
                            .toString();

                    for (VirtualMachineSimulator virtualMachineSimulator : hostMachineSimulator
                            .getVirtualMachineSimulatorList()) {

                        serviceMetricXML = new StringBuilder(serviceMetricXML)
                                .insert(serviceMetricXML.length() - 12,
                                        xmlRdfCreator
                                                .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                        Utility.getTimeStamp(),
                                                        "avgCPUPuntual",
                                                        virtualMachineSimulator
                                                                .getCpuMhzActuallyUsed(),
                                                        "Mhz",
                                                        virtualMachineSimulator
                                                                .getUriSimulatedEntity())))
                                .toString();

                        serviceMetricXML = new StringBuilder(serviceMetricXML)
                                .insert(serviceMetricXML.length() - 12,
                                        xmlRdfCreator
                                                .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                        Utility.getTimeStamp(),
                                                        "avgMemoryPuntual",
                                                        virtualMachineSimulator
                                                                .getMemoryActuallyUsed(),
                                                        "GB",
                                                        virtualMachineSimulator
                                                                .getUriSimulatedEntity())))
                                .toString();

                        serviceMetricXML = new StringBuilder(serviceMetricXML)
                                .insert(serviceMetricXML.length() - 12,
                                        xmlRdfCreator
                                                .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                        Utility.getTimeStamp(),
                                                        "avgStoragePuntual",
                                                        virtualMachineSimulator
                                                                .getStorageActuallyUsed(),
                                                        "GB",
                                                        virtualMachineSimulator
                                                                .getUriSimulatedEntity())))
                                .toString();

                    }
                }

                final ResponseMessageString responseMessage = KBDAO
                        .sendXMLToKB("POST",
                                this.dataCenterSimulator.getIpOfKB(),
                                "serviceMetric", "", serviceMetricXML);
                LOGGER.info(serviceMetricXML);
                LOGGER.info(responseMessage.getResponseCode() + " "
                        + responseMessage.getReason() + " "
                        + responseMessage.getResponseBody());
                LOGGER.info("==================================================");

                Thread.sleep(this.periodTimeSend * 1000L);
            } catch (InterruptedException | ParserConfigurationException | TransformerException | SAXException | IOException  e) {
                LOGGER.error(e.getMessage(), e);
            } 

        }
        LOGGER.info("Stop: DataCenterMetricSender");
        return;
    }

    public void stopSender() {
        this.started = false;
    }

    public boolean getStarted() {
        return this.started;
    }

    public int getPeriodTimeSend() {
        return periodTimeSend;
    }

    public void setPeriodTimeSend(final int periodTimeSend) {
        this.periodTimeSend = periodTimeSend;
    }
}
