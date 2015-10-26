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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cloudsimulator.domain.ontology.IcaroTenant;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlSparqlToIcaroTenant {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlSparqlToIcaroTenant.class);

    private XmlSparqlToIcaroTenant() {
        // Not Called
    }

    public static IcaroTenant createIcaroTenant(final String xmlSparqlResult,
            final String uri) {
        IcaroTenant icaroTenant = new IcaroTenant();

        try {

            Document documentXmlSparql = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSparqlResult.getBytes("utf-8"))));

            documentXmlSparql.getDocumentElement().normalize();

            NodeList resultList = documentXmlSparql
                    .getElementsByTagName(XmlSparqlUtility.RESULT);

            Element resultElement = (Element) resultList.item(0);

            NodeList bindingList = resultElement
                    .getElementsByTagName(XmlSparqlUtility.BINDING);

            for (int j = 0; j < bindingList.getLength(); j++) {
                Element bindingElement = (Element) bindingList.item(j);

                String name = bindingElement.getAttribute("name");

                if ("hasIdentifier".equals(name)) {
                    icaroTenant.setHasIdentifier(bindingElement
                            .getElementsByTagName(XmlSparqlUtility.LITERAL)
                            .item(0).getTextContent());
                }
                if ("hasName".equals(name)) {
                    icaroTenant.setHasName(bindingElement
                            .getElementsByTagName(XmlSparqlUtility.LITERAL)
                            .item(0).getTextContent());
                }
                if ("hasMonitorState".equals(name)) {
                    icaroTenant.setHasMonitorState(bindingElement
                            .getElementsByTagName(XmlSparqlUtility.LITERAL)
                            .item(0).getTextContent());
                }
            }

            icaroTenant.setUri(uri);

        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (DOMException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return icaroTenant;
    }

}
