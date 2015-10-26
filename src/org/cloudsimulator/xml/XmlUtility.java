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

package org.cloudsimulator.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xsd.ResourceResolver;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class XmlUtility implements Serializable {

    private static final long serialVersionUID = 2856560090570444140L;
    private static final String DATATYPE = "datatype";

    private XmlUtility() {
        // Not Called
    }

    public static Element createSimpleTextElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final String elementText) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.appendChild(documentParent.createTextNode(elementText));
        return element;
    }

    public static Element createSimpleTextElementWithoutNS(
            final Document documentParent, final String elementTagName,
            final String elementText) {
        Element element = documentParent.createElement(elementTagName);
        element.appendChild(documentParent.createTextNode(elementText));
        return element;
    }

    public static Element createDecimalElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final int elementValue) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":" + DATATYPE,
                "&xsd;decimal");
        element.appendChild(documentParent.createTextNode(String
                .valueOf(elementValue)));
        return element;
    }

    public static Element createDecimalElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final float elementValue) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":" + DATATYPE,
                "&xsd;decimal");
        element.appendChild(documentParent.createTextNode(String
                .valueOf(elementValue)));
        return element;
    }

    public static Element createPositiveIntegerElement(
            final Namespace nameSpace, final Document documentParent,
            final String elementTagName, final int elementValue) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":" + DATATYPE,
                "&xsd;positiveInteger");
        element.appendChild(documentParent.createTextNode(String
                .valueOf(elementValue)));
        return element;
    }

    public static Element createUnsignedShortElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final int elementValue) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":" + DATATYPE,
                "&xsd;unsignedShort");
        element.appendChild(documentParent.createTextNode(String
                .valueOf(elementValue)));
        return element;
    }

    public static Element createDateTimeElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final String elementValue) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":" + DATATYPE,
                "&xsd;dateTime");
        element.appendChild(documentParent.createTextNode(String
                .valueOf(elementValue)));
        return element;
    }

    public static Element createResourceElement(final Namespace nameSpace,
            final Document documentParent, final String elementTagName,
            final String elementResource) {
        Element element = documentParent.createElementNS(nameSpace.getURI(),
                nameSpace.getPrefix() + ":" + elementTagName);
        element.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":resource",
                elementResource);
        return element;
    }

    public static void validateXML(final String xmlAsString,
            final String schemaXsd) throws SAXException, IOException,
            ParserConfigurationException {

        Source xmlSource = new StreamSource(new ByteArrayInputStream(
                xmlAsString.getBytes(CharsetRepository.UTF8)));
        InputStream schemaSource = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("org/cloudsimulator/xsd/" + schemaXsd);
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new ResourceResolver());
        Schema schema = schemaFactory.newSchema(new StreamSource(schemaSource));
        Validator validator = schema.newValidator();
        validator.validate(xmlSource);

    }

}
