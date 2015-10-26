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

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.injection.InjectedConfiguration;
import org.cloudsimulator.persistence.KBDAO;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.xml.rdf.XmlRdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("businessConfigurationController")
@ConversationScoped
public class BusinessConfigurationController implements Serializable {

    private static final long serialVersionUID = 5707035027108023942L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BusinessConfigurationController.class);
    private BusinessConfiguration businessConfiguration;
    @Inject
    @InjectedConfiguration(key = "icaroKB.addressIP", defaultValue = "localhost:8080")
    private String ipOfKB;
    @Inject
    private VirtualMachineController virtualMachineController;
    private String createdXmlRdfBusinessConfiguration;
    private boolean okXmlRdf;
    private String errorMessage;
    private String alertMessage;
    private String sendDone;
    private boolean sendForm;

    // Constructor----------------------------------------------------------------------------------

    public BusinessConfigurationController() {
        super();
        this.businessConfiguration = new BusinessConfiguration();
    }

    // Method--------------------------------------------------------------------------------------

    public void insertBusinessConfigurationOnSimulator() {
        this.sendDone = "done";
        this.alertMessage = "Prova del pannello";
    }

    public String createBusinessConfigurationXML() {

        try {
            this.okXmlRdf = false;
            XmlRdfCreator xmlRdfCreator = new XmlRdfCreator();
            createdXmlRdfBusinessConfiguration = xmlRdfCreator
                    .createBusinessConfigurationXmlRdf(
                            this.virtualMachineController
                                    .getVirtualMachineList(),
                            this.businessConfiguration);
            this.okXmlRdf = true;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            this.errorMessage = e.toString();
        }

        return "visualizeXMLBusinessConfiguration";
    }

    public void getXmlRdfOfBusinessConfigurationFromServer() throws IOException {
        Utility.getXMLFromServer(this.createdXmlRdfBusinessConfiguration,
                "businessConfiguration");
    }

    public void sendXmlRdfOfBusinessConfigurationToKB() {
        ResponseMessageString responseMessage;
        hideSendForm();
        try {
            responseMessage = KBDAO.sendXMLToKB("PUT", this.ipOfKB,
                    "businessConfiguration",
                    this.businessConfiguration.getLocalUri(),
                    this.createdXmlRdfBusinessConfiguration);

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
        } catch (ClientProtocolException e) {
            LOGGER.error(e.getMessage(), e);
            this.sendDone = "exception";
            this.alertMessage = e.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            this.sendDone = "exception";
            this.alertMessage = e.toString();
        }
    }

    public void showSendForm() {
        this.sendForm = true;
    }

    public void hideSendForm() {
        this.sendForm = false;
    }

    // Getters&setters-------------------------------------------------------------------------

    public BusinessConfiguration getBusinessConfiguration() {
        return businessConfiguration;
    }

    public void setBusinessConfiguration(
            final BusinessConfiguration businessConfiguration) {
        this.businessConfiguration = businessConfiguration;
    }

    public String getCreatedXmlRdfBusinessConfiguration() {
        return this.createdXmlRdfBusinessConfiguration;
    }

    public void setCreatedXmlRdfBusinessConfiguration(
            String createdXmlRdfBusinessConfiguration) {
        this.createdXmlRdfBusinessConfiguration = createdXmlRdfBusinessConfiguration;
    }

    public boolean isOkXmlRdf() {
        return okXmlRdf;
    }

    public void setOkXmlRdf(final boolean okXmlRdf) {
        this.okXmlRdf = okXmlRdf;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public String getSendDone() {
        return sendDone;
    }

    public void setSendDone(String sendDone) {
        this.sendDone = sendDone;
    }

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

    public boolean issendForm() {
        return sendForm;
    }

}
