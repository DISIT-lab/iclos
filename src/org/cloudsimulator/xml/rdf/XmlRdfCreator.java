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

package org.cloudsimulator.xml.rdf;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.ExternalStorage;
import org.cloudsimulator.domain.ontology.Firewall;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.domain.ontology.Router;
import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.exception.LocalNetworkDuplicateException;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;
import org.cloudsimulator.exception.UserDuplicateException;
import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.BusinessConfigurationToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.DataCenterToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.ExternalStorageToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.FirewallToRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.HostMachineToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.LocalNetworkToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.RouterToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.ServiceMetricToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.UserToXmlRdf;
import org.cloudsimulator.xml.rdf.xmlrdfconverter.VirtualMachineToXmlRdf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlRdfCreator implements Serializable {

    private static final long serialVersionUID = 2856560090570444140L;
    private transient Document documentXmlRdf;
    private String createdXmlRdf;
    private static final String STRINGTODELETE = ":DELETETHIS";

    private Map<String, User> mapUser = new HashMap<String, User>();

    private void controlUser(final User user) throws UserDuplicateException {
        if (user != null) {
            if (!mapUser.containsKey(user.getLocalUri())) {
                mapUser.put(user.getLocalUri(), user);
            } else {
                if (!mapUser.get(user.getLocalUri()).getNameFoaf()
                        .equals(user.getNameFoaf())
                        || !mapUser.get(user.getLocalUri()).getMboxFoaf()
                                .equals(user.getMboxFoaf())) {
                    throw new UserDuplicateException(mapUser.get(user
                            .getLocalUri()), user);
                }
            }
        }
    }

    public String createBusinessConfigurationXmlRdf(
            final List<VirtualMachine> virtualMachineList,
            final BusinessConfiguration businessConfiguration)
            throws SAXException, IOException, ParserConfigurationException,
            LocalNetworkTooLittleException, UserDuplicateException,
            TransformerException {

        final String schemaXsd = "schema-icaro-kb-businessConfiguration.xsd";
        final String uriBusinessConfiguration = businessConfiguration.getUri();

        documentXmlRdf = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();

        Element rdfElement = createRdfElement(schemaXsd);
        documentXmlRdf.appendChild(rdfElement);

        Element businessConfigurationElement = BusinessConfigurationToXmlRdf
                .createBusinessConfigurationElement(NameSpaceRepository.ICR,
                        documentXmlRdf, businessConfiguration,
                        uriBusinessConfiguration);
        rdfElement.appendChild(businessConfigurationElement);

        for (VirtualMachine virtualMachine : virtualMachineList) {
            if (virtualMachine != null) {
                Element virtualMachineElement = VirtualMachineToXmlRdf
                        .createVirtualMachineElement(NameSpaceRepository.ICR,
                                documentXmlRdf, virtualMachine);
                rdfElement.appendChild(virtualMachineElement);
            }
        }

        for (User creator : businessConfiguration.getCreatorList()) {
            controlUser(creator);
        }
        for (IcaroApplication icaroApplication : businessConfiguration
                .getIcaroApplicationList()) {
            if (icaroApplication != null) {
                for (User creator : icaroApplication.getCreatorList()) {
                    controlUser(creator);
                }
            }
        }
        for (User user : mapUser.values()) {
            Element userElement = UserToXmlRdf.createUserElement(
                    NameSpaceRepository.ICR, documentXmlRdf, user,
                    user.getUri());
            rdfElement.appendChild(userElement);
        }

        createdXmlRdf = finishAndCleanDocumentXmlRdf(documentXmlRdf);

        XmlUtility.validateXML(createdXmlRdf, schemaXsd);

        return createdXmlRdf;
    }

    public String createDataCenterXmlRdf(final DataCenter dataCenter)
            throws SAXException, IOException, ParserConfigurationException,
            LocalNetworkDuplicateException, LocalNetworkTooLittleException,
            TransformerException {

        final String schemaXsd = "schema-icaro-kb-dataCenter.xsd";

        documentXmlRdf = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();

        Element rdfElement = createRdfElement(schemaXsd);
        documentXmlRdf.appendChild(rdfElement);

        Element dataCenterElement = DataCenterToXmlRdf.createDataCenterElement(
                NameSpaceRepository.ICR, documentXmlRdf, dataCenter);
        rdfElement.appendChild(dataCenterElement);

        for (HostMachine hostMachine : dataCenter.getHostMachineList()) {
            if (hostMachine != null) {
                Element hostMachineElement = HostMachineToXmlRdf
                        .createHostMachineElement(NameSpaceRepository.ICR,
                                documentXmlRdf, dataCenterElement,
                                dataCenter.getUri(), hostMachine);
                rdfElement.appendChild(hostMachineElement);
            }
        }

        for (ExternalStorage externalStorage : dataCenter
                .getExternalStorageList()) {
            if (externalStorage != null) {
                Element externalStorageElement = ExternalStorageToXmlRdf
                        .createExternalStorageElement(NameSpaceRepository.ICR,
                                documentXmlRdf, externalStorage);
                rdfElement.appendChild(externalStorageElement);
            }
        }

        for (Firewall firewall : dataCenter.getFirewallList()) {
            if (firewall != null) {
                Element firewallElement = FirewallToRdf.createFirewallElement(
                        NameSpaceRepository.ICR, documentXmlRdf, firewall);
                rdfElement.appendChild(firewallElement);
            }
        }

        for (Router router : dataCenter.getRouterList()) {
            if (router != null) {
                Element routerElement = RouterToXmlRdf.createRouterElement(
                        NameSpaceRepository.ICR, documentXmlRdf, router);
                rdfElement.appendChild(routerElement);
            }
        }

        for (LocalNetwork localNetwork : dataCenter.getLocalNetworkList()) {
            Element localNetworkElement = LocalNetworkToXmlRdf
                    .createLocalNetworkElement(NameSpaceRepository.ICR,
                            documentXmlRdf, localNetwork);
            rdfElement.appendChild(localNetworkElement);

        }

        createdXmlRdf = finishAndCleanDocumentXmlRdf(documentXmlRdf);

        XmlUtility.validateXML(createdXmlRdf, schemaXsd);

        return createdXmlRdf;
    }

    private Element createRdfElement(final String schemaLocation) {
        Element rdfElement = documentXmlRdf.createElementNS(
                NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":"
                        + NameSpaceRepository.RDF.getPrefix().toUpperCase());
        rdfElement.setAttributeNS(NameSpaceRepository.APP.getURI(),
                NameSpaceRepository.APP.getPrefix() + STRINGTODELETE, "");
        rdfElement.setAttributeNS(NameSpaceRepository.ICR.getURI(),
                NameSpaceRepository.ICR.getPrefix() + STRINGTODELETE, "");
        rdfElement.setAttributeNS(NameSpaceRepository.FOAF.getURI(),
                NameSpaceRepository.FOAF.getPrefix() + STRINGTODELETE, "");
        rdfElement.setAttributeNS(NameSpaceRepository.XSI.getURI(),
                NameSpaceRepository.XSI.getPrefix() + STRINGTODELETE, "");
        rdfElement.setAttributeNS(NameSpaceRepository.XSI.getURI(),
                NameSpaceRepository.XSI.getPrefix() + ":schemaLocation",
                NameSpaceRepository.RDF.getURI() + " " + schemaLocation);
        return rdfElement;
    }

    public String createServiceMetricXmlRdf(final ServiceMetric serviceMetric)
            throws ParserConfigurationException, TransformerException,
            SAXException, IOException {

        final String schemaXsd = "schema-icaro-kb-serviceMetric.xsd";

        documentXmlRdf = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();

        Element rdfElement = createRdfElement(schemaXsd);
        documentXmlRdf.appendChild(rdfElement);

        Element serviceMetricElement = ServiceMetricToXmlRdf
                .createServiceMetricElement(NameSpaceRepository.ICR,
                        documentXmlRdf, serviceMetric);
        rdfElement.appendChild(serviceMetricElement);

        createdXmlRdf = finishAndCleanDocumentXmlRdf(documentXmlRdf);

        XmlUtility.validateXML(createdXmlRdf, schemaXsd);

        return createdXmlRdf;
    }

    public String createServiceMetricXmlRdfWithoutRDFElement(
            final ServiceMetric serviceMetric)
            throws ParserConfigurationException, TransformerException,
            SAXException, IOException {

        final String schemaXsd = "schema-icaro-kb-serviceMetric.xsd";

        documentXmlRdf = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();

        Element rdfElement = createRdfElement(schemaXsd);
        documentXmlRdf.appendChild(rdfElement);

        Element serviceMetricElement = ServiceMetricToXmlRdf
                .createServiceMetricElement(NameSpaceRepository.ICR,
                        documentXmlRdf, serviceMetric);
        rdfElement.appendChild(serviceMetricElement);

        createdXmlRdf = removeRDFElementFromDocumentXmlRdf(documentXmlRdf);

        return createdXmlRdf;
    }

    private String finishAndCleanDocumentXmlRdf(final Document documentXmlRdf)
            throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING,
                CharsetRepository.UTF8);
        transformer
                .setOutputProperty(
                        OutputKeys.DOCTYPE_SYSTEM,
                        "rdf:RDF ["
                                + "<!ENTITY owl \"http://www.w3.org/2002/07/owl#\">"
                                + "<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\">"
                                + "<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\">"
                                + "<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">"
                                + "<!ENTITY icr \"http://www.cloudicaro.it/cloud_ontology/core#\">"
                                + "<!ENTITY app \"http://www.cloudicaro.it/cloud_ontology/applications#\">]");

        StringWriter streamWriter = new StringWriter();
        transformer.transform(new DOMSource(documentXmlRdf), new StreamResult(
                streamWriter));

        return streamWriter.toString()
                .replaceAll(" .{3,5}" + STRINGTODELETE + "=\"\"", " ")
                .replaceAll("amp;", "").replaceAll("rdf:RDF SYSTEM \"", "")
                .replaceAll(">]\">", ">]>");
    }

    private String removeRDFElementFromDocumentXmlRdf(
            final Document documentXmlRdf) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING,
                CharsetRepository.UTF8);

        StringWriter streamWriter = new StringWriter();
        transformer.transform(new DOMSource(documentXmlRdf), new StreamResult(
                streamWriter));

        return streamWriter
                .toString()
                .substring(streamWriter.toString().indexOf('>') + 1,
                        streamWriter.toString().length() - 12)
                .replaceAll("amp;", "");
    }
}
