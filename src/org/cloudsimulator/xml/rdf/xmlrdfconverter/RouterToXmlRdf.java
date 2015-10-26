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

import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.Router;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class RouterToXmlRdf {

    private RouterToXmlRdf() {
        // Not Called
    }

    public static Element createRouterElement(final Namespace nameSpace,
            final Document documentParent, final Router router) {

        Element routerElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":Router");
        routerElement
                .setAttributeNS(NameSpaceRepository.RDF.getURI(),
                        NameSpaceRepository.RDF.getPrefix() + ":about",
                        router.getUri());
        routerElement.appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                documentParent, "hasName", router.getHasName()));
        routerElement.appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                documentParent, "hasIdentifier", router.getHasIdentifier()));
        routerElement.appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                documentParent, "hasModelName", router.getHasModelName()));

        boolean firstNetworkAdapter = true;
        Element hasNetworkAdapterElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":hasNetworkAdapter");
        for (NetworkAdapter networkAdapter : router.getHasNetworkAdapterList()) {
            if (networkAdapter != null) {
                if (firstNetworkAdapter) {
                    routerElement.appendChild(hasNetworkAdapterElement);
                    firstNetworkAdapter = false;
                }
                hasNetworkAdapterElement.appendChild(NetworkAdapterToXmlRdf
                        .createNetworkAdapterElement(nameSpace, documentParent,
                                networkAdapter));
            }
        }

        routerElement.appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                documentParent, "hasMonitorIPAddress",
                router.getHasMonitorIPAddress()));

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : router.getHasMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    routerElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        routerElement
                .appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                        documentParent, "hasMonitorState",
                        router.getHasMonitorState()));

        return routerElement;
    }

}
