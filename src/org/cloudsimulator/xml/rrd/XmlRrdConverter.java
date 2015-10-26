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

package org.cloudsimulator.xml.rrd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cloudsimulator.domain.NagiosRrdDescription;
import org.cloudsimulator.domain.XportRrd;
import org.cloudsimulator.xml.XmlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XmlRrdConverter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlRrdConverter.class);

    private XmlRrdConverter() {
        // Not Called
    }

    public static XportRrd createXportRRD(File xmlRRDXport) {

        XportRrd xportRrd = new XportRrd();

        Document documentXmlRrd = null;
        try {
            documentXmlRrd = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(xmlRRDXport);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (documentXmlRrd != null) {
            documentXmlRrd.getDocumentElement().normalize();

            Node metaNode = documentXmlRrd.getElementsByTagName("meta").item(0);
            NodeList metaChildList = metaNode.getChildNodes();

            for (int i = 0; i < metaChildList.getLength(); i++) {
                Element metaChildElement = null;
                if (metaChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    metaChildElement = (Element) metaChildList.item(i);
                }
                if (metaChildElement != null
                        && metaChildElement.getChildNodes().getLength() == 1) {
                    xportRrd.getMetaMap().put(metaChildElement.getNodeName(),
                            Integer.valueOf(metaChildElement.getTextContent()));
                }
            }

            Node legendNode = documentXmlRrd.getElementsByTagName("legend")
                    .item(0);
            NodeList legendChildList = legendNode.getChildNodes();

            for (int i = 0; i < legendChildList.getLength(); i++) {
                Element legendChildElement = null;
                if (legendChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    legendChildElement = (Element) legendChildList.item(i);
                }
                if (legendChildElement != null) {
                    xportRrd.getColumnsLegend().add(
                            legendChildElement.getTextContent());
                }
            }

            Node dataNode = documentXmlRrd.getElementsByTagName("data").item(0);
            NodeList dataChildList = dataNode.getChildNodes();

            for (int i = 0; i < dataChildList.getLength(); i++) {
                Element rowElement = null;
                if (dataChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    rowElement = (Element) dataChildList.item(i);
                }
                if (rowElement != null) {
                    NodeList rowChildList = rowElement.getChildNodes();
                    List<Float> valueList = new ArrayList<Float>();
                    for (int j = 1; j < rowChildList.getLength(); j++) {
                        Element rowChildElement = null;
                        if (rowChildList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            rowChildElement = (Element) rowChildList.item(j);
                        }
                        if (rowChildElement != null) {
                            valueList.add(Float.valueOf(rowChildElement
                                    .getTextContent()));
                        }
                    }
                    xportRrd.getDataMap().put(
                            Integer.parseInt(rowChildList.item(0)
                                    .getTextContent()), valueList);
                }
            }
        }

        return xportRrd;
    }

    public static NagiosRrdDescription createNagiosRrdDescriptionFromXml(
            File xmlFile) {

        NagiosRrdDescription nagiosRrdDescription = new NagiosRrdDescription();

        Document documentXmlRrd = null;
        try {
            documentXmlRrd = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(xmlFile);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (documentXmlRrd != null) {

            documentXmlRrd.getDocumentElement().normalize();

            Node dataSourceNode = documentXmlRrd.getElementsByTagName(
                    "DATASOURCE").item(0);
            NodeList dataSourceChildList = dataSourceNode.getChildNodes();

            for (int i = 0; i < dataSourceChildList.getLength(); i++) {
                Element dataSourceChildElement = null;
                if (dataSourceChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    dataSourceChildElement = (Element) dataSourceChildList
                            .item(i);
                }
                if (dataSourceChildElement != null
                        && dataSourceChildElement.getChildNodes().getLength() == 1) {
                    nagiosRrdDescription.getDataSourceMap().put(
                            dataSourceChildElement.getNodeName(),
                            dataSourceChildElement.getTextContent());
                }
            }

            Node rrdNode = documentXmlRrd.getElementsByTagName("RRD").item(0);
            NodeList rrdChildList = rrdNode.getChildNodes();

            for (int i = 0; i < rrdChildList.getLength(); i++) {
                Element rrdChildElement = null;
                if (rrdChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    rrdChildElement = (Element) rrdChildList.item(i);
                }
                if (rrdChildElement != null) {
                    nagiosRrdDescription.getRrdMap().put(
                            rrdChildElement.getNodeName(),
                            rrdChildElement.getTextContent());
                }
            }

            Node xmlNode = documentXmlRrd.getElementsByTagName("XML").item(0);
            NodeList xmlChildList = xmlNode.getChildNodes();

            for (int i = 0; i < xmlChildList.getLength(); i++) {
                Element xmlChildElement = null;
                if (xmlChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    xmlChildElement = (Element) xmlChildList.item(i);
                }
                if (xmlChildElement != null) {
                    nagiosRrdDescription.getXmlMap().put(
                            xmlChildElement.getNodeName(),
                            xmlChildElement.getTextContent());
                }
            }

            Node nagiosNode = documentXmlRrd.getElementsByTagName("NAGIOS")
                    .item(0);
            NodeList nagiosChildList = nagiosNode.getChildNodes();

            for (int i = 0; i < nagiosChildList.getLength(); i++) {
                Element nagiosChildElement = null;
                if (nagiosChildList.item(i).getNodeType() == Node.ELEMENT_NODE
                        && nagiosChildList.item(i).getNodeName()
                                .contains("NAGIOS")) {
                    nagiosChildElement = (Element) nagiosChildList.item(i);
                }
                if (nagiosChildElement != null) {
                    nagiosRrdDescription.getNagiosMetaMap().put(
                            nagiosChildElement.getNodeName(),
                            nagiosChildElement.getTextContent());
                }
            }

        } 

        return nagiosRrdDescription;
    }

    public static Document createXmlFromNagiosRrdDescription(
            NagiosRrdDescription nagiosRrdDescription) {

        Document documentXml = null;
        try {
            documentXml = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();

            Element nagiosElement = documentXml.createElement("NAGIOS");
            documentXml.appendChild(nagiosElement);

            Element dataSourceElement = documentXml.createElement("DATASOURCE");
            nagiosElement.appendChild(dataSourceElement);

            for (String key : nagiosRrdDescription.getDataSourceMap().keySet()) {
                dataSourceElement.appendChild(XmlUtility
                        .createSimpleTextElementWithoutNS(documentXml, key,
                                nagiosRrdDescription.getDataSourceMap()
                                        .get(key)));
            }

            Element rrdElement = documentXml.createElement("RRD");
            nagiosElement.appendChild(rrdElement);

            for (String key : nagiosRrdDescription.getRrdMap().keySet()) {
                rrdElement.appendChild(XmlUtility
                        .createSimpleTextElementWithoutNS(documentXml, key,
                                nagiosRrdDescription.getRrdMap().get(key)));
            }

            for (String key : nagiosRrdDescription.getNagiosMetaMap().keySet()) {
                nagiosElement.appendChild(XmlUtility
                        .createSimpleTextElementWithoutNS(documentXml, key,
                                nagiosRrdDescription.getNagiosMetaMap()
                                        .get(key)));
            }

            Element xmlElement = documentXml.createElement("XML");
            nagiosElement.appendChild(xmlElement);

            for (String key : nagiosRrdDescription.getXmlMap().keySet()) {
                xmlElement.appendChild(XmlUtility
                        .createSimpleTextElementWithoutNS(documentXml, key,
                                nagiosRrdDescription.getXmlMap().get(key)));
            }

        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return documentXml;
    }

}
