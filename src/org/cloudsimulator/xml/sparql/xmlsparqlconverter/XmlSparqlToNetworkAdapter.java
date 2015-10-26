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

package org.cloudsimulator.xml.sparql.xmlsparqlconverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlSparqlToNetworkAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlSparqlToNetworkAdapter.class);

    private XmlSparqlToNetworkAdapter() {
        // Not Called
    }

    public static List<NetworkAdapter> createNetworkAdapterList(
            final String xmlSparqlResult) {

        List<NetworkAdapter> networkAdapterList = new ArrayList<NetworkAdapter>();

        try {

            Document documentXmlSparql = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSparqlResult.getBytes("utf-8"))));

            documentXmlSparql.getDocumentElement().normalize();

            NodeList resultList = documentXmlSparql
                    .getElementsByTagName(XmlSparqlUtility.RESULT);

            for (int i = 0; i < resultList.getLength(); i++) {

                Element resultElement = (Element) resultList.item(i);

                NetworkAdapter networkAdapter = new NetworkAdapter();

                NodeList bindingList = resultElement
                        .getElementsByTagName(XmlSparqlUtility.BINDING);

                for (int j = 0; j < bindingList.getLength(); j++) {
                    Element bindingElement = (Element) bindingList.item(j);

                    String name = bindingElement.getAttribute("name");

                    if ("hasIPAddress".equals(name)) {
                        networkAdapter.setHasIPAddress(bindingElement
                                .getElementsByTagName(XmlSparqlUtility.LITERAL)
                                .item(0).getTextContent());
                    }
                    if ("boundToNetwork".equals(name)) {
                        networkAdapter.setBoundToNetwork(bindingElement
                                .getElementsByTagName(XmlSparqlUtility.URI)
                                .item(0).getTextContent());
                    }
                }

                networkAdapterList.add(networkAdapter);

            }

        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return networkAdapterList;

    }
}
