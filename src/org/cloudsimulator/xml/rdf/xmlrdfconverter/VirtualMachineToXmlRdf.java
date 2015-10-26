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
import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class VirtualMachineToXmlRdf {

    private VirtualMachineToXmlRdf() {
    }

    public static Element createVirtualMachineElement(
            final Namespace nameSpace, final Document documentParent,
            final VirtualMachine virtualMachine) {
        Element virtualMachineElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":VirtualMachine");
        virtualMachineElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                virtualMachine.getUri());
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasName",
                virtualMachine.getHasName()));
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                virtualMachine.getHasIdentifier()));
        virtualMachineElement.appendChild(XmlUtility
                .createPositiveIntegerElement(nameSpace, documentParent,
                        "hasCPUCount", virtualMachine.getHasCPUCount()));
        virtualMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCPUSpeedReservation",
                virtualMachine.getHasCPUSpeedReservation()));
        virtualMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCPUSpeedLimit",
                virtualMachine.getHasCPUSpeedLimit()));
        virtualMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasMemorySize",
                virtualMachine.getHasMemorySize()));
        virtualMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasMemoryReservation",
                virtualMachine.getHasMemoryReservation()));
        virtualMachineElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasMemoryLimit",
                virtualMachine.getHasMemoryLimit()));

        boolean firstVirtualStorage = true;
        Element hasVirtualStorageElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":hasVirtualStorage");
        for (VirtualStorage virtualStorage : virtualMachine
                .getHasVirtualStorageList()) {
            if (virtualStorage != null) {
                if (firstVirtualStorage) {
                    virtualMachineElement.appendChild(hasVirtualStorageElement);
                    firstVirtualStorage = false;
                }
                hasVirtualStorageElement.appendChild(VirtualStorageToXmlRdf
                        .createVirtualStorageElement(nameSpace, documentParent,
                                virtualStorage));
            }
        }

        boolean firstNetworkAdapter = true;
        Element hasNetworkAdapterElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":hasNetworkAdapter");
        for (NetworkAdapter networkAdapter : virtualMachine
                .getHasNetworkAdapterList()) {
            if (networkAdapter != null) {
                if (firstNetworkAdapter) {
                    virtualMachineElement.appendChild(hasNetworkAdapterElement);
                    firstNetworkAdapter = false;
                }
                hasNetworkAdapterElement.appendChild(NetworkAdapterToXmlRdf
                        .createNetworkAdapterElement(nameSpace, documentParent,
                                networkAdapter));
            }
        }

        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasExternalIPAddress",
                virtualMachine.getHasExternalIPAddress()));
        virtualMachineElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "hasOS",
                "http://www.cloudicaro.it/cloud_ontology/core#"
                        + virtualMachine.getHasOS()));
        virtualMachineElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "isPartOf",
                virtualMachine.getIsPartOf()));
        virtualMachineElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "isStoredOn",
                virtualMachine.getIsStoredOn()));
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "isInDomain",
                virtualMachine.getIsInDomain()));
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserName",
                virtualMachine.getHasAuthUserName()));
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasAuthUserPassword",
                virtualMachine.getHasAuthUserPassword()));
        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorIPAddress",
                virtualMachine.getHasMonitorIPAddress()));

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : virtualMachine.getHasMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    virtualMachineElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        virtualMachineElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                virtualMachine.getHasMonitorState()));

        return virtualMachineElement;
    }
}