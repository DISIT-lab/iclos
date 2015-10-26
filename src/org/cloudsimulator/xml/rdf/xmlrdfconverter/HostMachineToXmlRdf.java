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

import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class HostMachineToXmlRdf {

    private HostMachineToXmlRdf() {
        // Not Called
    }

    public static Element createHostMachineElement(final Namespace nameSpace,
            final Document documentParent, final Element dataCenterElement,
            final String uriDataCenter, final HostMachine hostMachine)
            throws LocalNetworkTooLittleException {
        Element hasPartElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasPart");
        hasPartElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":resource",
                hostMachine.getUri());
        dataCenterElement.appendChild(hasPartElement);

        Element hostMachineElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":HostMachine");
        hostMachineElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                hostMachine.getUri());
        hostMachineElement
                .appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                        documentParent, "hasName", hostMachine.getHasName()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                hostMachine.getHasIdentifier()));
        hostMachineElement.appendChild(XmlUtility.createPositiveIntegerElement(
                nameSpace, documentParent, "hasCPUCount",
                hostMachine.getHasCPUCount()));
        hostMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCPUSpeed",
                hostMachine.getHasCPUSpeed()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasCPUType",
                hostMachine.getHasCPUType()));
        hostMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasMemorySize",
                hostMachine.getHasMemorySize()));

        boolean firstLocalStorage = true;
        Element hasLocalStorageElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasLocalStorage");
        for (LocalStorage localStorage : hostMachine.getHasLocalStorageList()) {
            if (localStorage != null) {
                if (firstLocalStorage) {
                    hostMachineElement.appendChild(hasLocalStorageElement);
                    firstLocalStorage = false;
                }
                hasLocalStorageElement.appendChild(LocalStorageToXmlRdf
                        .createLocalStorageElement(nameSpace, documentParent,
                                localStorage));
            }
        }

        for (String uriSharedStorageVolume : hostMachine
                .getUseSharedStorageList()) {
            hostMachineElement.appendChild(XmlUtility.createResourceElement(
                    nameSpace, documentParent, "useSharedStorage",
                    uriSharedStorageVolume));
        }

        boolean firstNetworkAdapter = true;
        Element hasNetworkAdapterElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":hasNetworkAdapter");
        for (NetworkAdapter networkAdapter : hostMachine
                .getHasNetworkAdapterList()) {
            if (networkAdapter != null) {
                if (firstNetworkAdapter) {
                    hostMachineElement.appendChild(hasNetworkAdapterElement);
                    firstNetworkAdapter = false;
                }
                hasNetworkAdapterElement.appendChild(NetworkAdapterToXmlRdf
                        .createNetworkAdapterElement(nameSpace, documentParent,
                                networkAdapter));
            }
        }

        hostMachineElement.appendChild(XmlUtility.createResourceElement(
                nameSpace,
                documentParent,
                "hasOS",
                "http://www.cloudicaro.it/cloud_ontology/core#"
                        + hostMachine.getHasOS()));
        hostMachineElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "isPartOf", uriDataCenter));
        hostMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCapacity",
                hostMachine.getHasCapacity()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "isInDomain",
                hostMachine.getIsInDomain()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserName",
                hostMachine.getHasAuthUserName()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserPassword",
                hostMachine.getHasAuthUserPassword()));
        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorIPAddress",
                hostMachine.getHasMonitorIPAddress()));

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : hostMachine.getHasMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    hostMachineElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        hostMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                hostMachine.getHasMonitorState()));

        return hostMachineElement;
    }
}