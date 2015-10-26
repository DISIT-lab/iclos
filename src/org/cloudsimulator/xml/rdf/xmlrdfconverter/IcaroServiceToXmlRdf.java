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

package org.cloudsimulator.xml.rdf.xmlrdfconverter;

import org.cloudsimulator.domain.ontology.IcaroService;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class IcaroServiceToXmlRdf {

    private IcaroServiceToXmlRdf() {
        // Not Called
    }

    public static Element createIcaroServiceElement(final Namespace nameSpace,
            final Document documentParent, final IcaroService icaroService,
            final String uriIcaroService) {
        Element icaroServiceElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":IcaroService");
        icaroServiceElement
                .setAttributeNS(NameSpaceRepository.RDF.getURI(),
                        NameSpaceRepository.RDF.getPrefix() + ":about",
                        uriIcaroService);
        icaroServiceElement.appendChild(XmlUtility.createResourceElement(
                NameSpaceRepository.RDF, documentParent, "type",
                icaroService.getTypeRdf()));
        icaroServiceElement
                .appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                        documentParent, "hasName", icaroService.getHasName()));
        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                icaroService.getHasIdentifier()));
        icaroServiceElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "runsOnVM",
                icaroService.getRunsOnVm()));
        icaroServiceElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "supportsLanguage",
                icaroService.getSupportsLanguage()));

        for (Integer tcpPort : icaroService.getUsesTcpPortList()) {
            icaroServiceElement.appendChild(XmlUtility
                    .createUnsignedShortElement(nameSpace, documentParent,
                            "usesTcpPort", tcpPort));
        }

        for (Integer udpPort : icaroService.getUsesUdpPortList()) {
            icaroServiceElement.appendChild(XmlUtility
                    .createUnsignedShortElement(nameSpace, documentParent,
                            "usesUdpPort", udpPort));
        }

        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorIPAddress",
                icaroService.getHasMonitorIPAddress()));
        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasProcessName",
                icaroService.getHasProcessName()));
        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserName",
                icaroService.getHasAuthUserName()));
        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserPassword",
                icaroService.getHasAuthUserPassword()));

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : icaroService.getMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    icaroServiceElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        icaroServiceElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                icaroService.getHasMonitorState()));

        return icaroServiceElement;
    }

}
