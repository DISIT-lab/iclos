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

package org.cloudsimulator.xml.sparql;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class XmlSparqlUtility {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlSparqlUtility.class);
    public static final String LITERAL = "literal";
    public static final String URI = "uri";
    public static final String BINDING = "binding";
    public static final String RESULT = "result";
    public static final String CHARSET = "utf-8";

    private XmlSparqlUtility() {
        // Not Called
    }

    public static List<String> createUriList(final String xmlSparqlResult) {

        List<String> uriList = new ArrayList<String>();

        try {

            Document documentXmlSparql = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSparqlResult.getBytes(CHARSET))));

            documentXmlSparql.getDocumentElement().normalize();

            NodeList resultList = documentXmlSparql
                    .getElementsByTagName(RESULT);

            for (int i = 0; i < resultList.getLength(); i++) {

                Element resultElement = (Element) resultList.item(i);

                NodeList bindingList = resultElement
                        .getElementsByTagName(BINDING);

                for (int j = 0; j < bindingList.getLength(); j++) {
                    Element bindingElement = (Element) bindingList.item(j);

                    uriList.add(bindingElement.getElementsByTagName(URI)
                            .item(0).getTextContent());

                }

            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return uriList;

    }

    public static List<Integer> createIntegerList(final String xmlSparqlResult) {

        List<Integer> integerList = new ArrayList<Integer>();

        try {

            Document documentXmlSparql = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSparqlResult.getBytes(CHARSET))));

            documentXmlSparql.getDocumentElement().normalize();

            NodeList resultList = documentXmlSparql
                    .getElementsByTagName(RESULT);

            for (int i = 0; i < resultList.getLength(); i++) {

                Element resultElement = (Element) resultList.item(i);

                NodeList bindingList = resultElement
                        .getElementsByTagName(BINDING);

                for (int j = 0; j < bindingList.getLength(); j++) {
                    Element bindingElement = (Element) bindingList.item(j);
                    integerList.add(Integer.parseInt(bindingElement
                            .getElementsByTagName(LITERAL).item(0)
                            .getTextContent()));
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return integerList;

    }

    public static List<String> createStringList(final String xmlSparqlResult) {

        List<String> stringList = new ArrayList<String>();

        try {

            Document documentXmlSparql = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSparqlResult.getBytes(CHARSET))));

            documentXmlSparql.getDocumentElement().normalize();

            NodeList resultList = documentXmlSparql
                    .getElementsByTagName(RESULT);

            for (int i = 0; i < resultList.getLength(); i++) {

                Element resultElement = (Element) resultList.item(i);

                NodeList bindingList = resultElement
                        .getElementsByTagName(BINDING);

                for (int j = 0; j < bindingList.getLength(); j++) {
                    Element bindingElement = (Element) bindingList.item(j);
                    stringList.add(bindingElement.getElementsByTagName(LITERAL)
                            .item(0).getTextContent());
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return stringList;

    }

}
