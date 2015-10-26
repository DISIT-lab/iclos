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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.domain.ontology.GroupServiceMetric;
import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.injection.InjectedConfiguration;
import org.cloudsimulator.persistence.KBDAO;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.xml.rdf.XmlRdfCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("serviceMetricController")
@ConversationScoped
public class ServiceMetricController implements Serializable {

    private static final long serialVersionUID = -8249366451367114224L;
    private static final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceMetricController.class);
    private List<GroupServiceMetric> groupServiceMetricList;
    @Inject
    @InjectedConfiguration(key = "icaroKB.addressIP", defaultValue = "localhost:8080")
    private String ipOfKB;
    private String createdXmlRdfServiceMetric;
    private boolean okXmlRdf;
    private String errorMessage;
    private String alertMessage;
    private String sendDone;
    private boolean sendForm;

    public ServiceMetricController() {
        super();
        this.groupServiceMetricList = new ArrayList<GroupServiceMetric>();
    }

    public String createServiceMetric() {
        XmlRdfCreator xmlRdfCreator = new XmlRdfCreator();
        Random random = new Random();
        createdXmlRdfServiceMetric = "";
        this.okXmlRdf = false;
        for (GroupServiceMetric groupServiceMetric : groupServiceMetricList) {
            try {
                Date dateFrom = new SimpleDateFormat(DATEFORMAT)
                        .parse(groupServiceMetric.getDateFrom() + ":00");
                Date dateTo = new SimpleDateFormat(DATEFORMAT)
                        .parse(groupServiceMetric.getDateTo() + ":00");
                Long period = (dateTo.getTime() - dateFrom.getTime())
                        / (groupServiceMetric.getNumberServiceMetric() + 1);
                Date currentDate = dateFrom;
                for (int i = 0; i < groupServiceMetric.getNumberServiceMetric(); i++) {
                    currentDate = new Date(currentDate.getTime() + period);

                    if ("".equals(createdXmlRdfServiceMetric)) {
                        createdXmlRdfServiceMetric = xmlRdfCreator
                                .createServiceMetricXmlRdf(new ServiceMetric(
                                        Utility.convertDateToString(DATEFORMAT,
                                                currentDate),
                                        groupServiceMetric.getMetricName(),
                                        random.nextFloat()
                                                * (groupServiceMetric
                                                        .getMaxValue() - groupServiceMetric
                                                        .getMinValue())
                                                + groupServiceMetric
                                                        .getMinValue(),
                                        groupServiceMetric.getMetricUnit(),
                                        groupServiceMetric.getDependsOn()));
                    } else {
                        createdXmlRdfServiceMetric = new StringBuilder(
                                createdXmlRdfServiceMetric)
                                .insert(createdXmlRdfServiceMetric.length() - 12,
                                        xmlRdfCreator
                                                .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                        Utility.convertDateToString(
                                                                DATEFORMAT,
                                                                currentDate),
                                                        groupServiceMetric
                                                                .getMetricName(),
                                                        random.nextFloat()
                                                                * (groupServiceMetric
                                                                        .getMaxValue() - groupServiceMetric
                                                                        .getMinValue())
                                                                + groupServiceMetric
                                                                        .getMinValue(),
                                                        groupServiceMetric
                                                                .getMetricUnit(),
                                                        groupServiceMetric
                                                                .getDependsOn())))
                                .toString();
                    }

                }
                createdXmlRdfServiceMetric = new StringBuilder(
                        createdXmlRdfServiceMetric)
                        .insert(createdXmlRdfServiceMetric.length() - 12,
                                xmlRdfCreator
                                        .createServiceMetricXmlRdfWithoutRDFElement(new ServiceMetric(
                                                Utility.convertDateToString(
                                                        DATEFORMAT,
                                                        new Date(currentDate
                                                                .getTime()
                                                                + period)),
                                                groupServiceMetric
                                                        .getMetricName(),
                                                groupServiceMetric
                                                        .getFinalValue(),
                                                groupServiceMetric
                                                        .getMetricUnit(),
                                                groupServiceMetric
                                                        .getDependsOn())))
                        .toString();

            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
                this.errorMessage = e.toString();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                this.errorMessage = e.toString();
            }
        }
        this.okXmlRdf = true;

        return "visualizeXMLServiceMetric";
    }

    public void getXmlRdfOfServiceMetricFromServer() throws IOException {
        Utility.getXMLFromServer(this.createdXmlRdfServiceMetric,
                "serviceMetric");
    }

    public void sendXmlRdfOfServiceMetricToKB() {
        ResponseMessageString responseMessage;
        hideSendForm();
        try {
            responseMessage = KBDAO.sendXMLToKB("POST", this.ipOfKB,
                    "serviceMetric", "", this.createdXmlRdfServiceMetric);

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

    public String getIpOfKB() {
        return this.ipOfKB;
    }

    public void setIpOfKB(String ipOfKB) {
        this.ipOfKB = ipOfKB;
    }

    public List<GroupServiceMetric> getGroupServiceMetricList() {
        return this.groupServiceMetricList;
    }

    public void setGroupServiceMetricList(
            List<GroupServiceMetric> groupServiceMetricList) {
        this.groupServiceMetricList = groupServiceMetricList;
    }

    public String getCreatedXmlRdfServiceMetric() {
        return this.createdXmlRdfServiceMetric;
    }

    public void setCreatedXmlRdfServiceMetric(String createdXmlRdfServiceMetric) {
        this.createdXmlRdfServiceMetric = createdXmlRdfServiceMetric;
    }

    public boolean isOkXmlRdf() {
        return this.okXmlRdf;
    }

    public void setOkXmlRdf(boolean okXmlRdf) {
        this.okXmlRdf = okXmlRdf;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAlertMessage() {
        return this.alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getSendDone() {
        return this.sendDone;
    }

    public void setSendDone(String sendDone) {
        this.sendDone = sendDone;
    }

    public boolean issendForm() {
        return sendForm;
    }

}
