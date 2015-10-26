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

import org.cloudsimulator.domain.ontology.ExternalStorage;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.NetworkAdapter;
import org.cloudsimulator.domain.ontology.SharedStorageVolume;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ExternalStorageToXmlRdf {

    private ExternalStorageToXmlRdf() {
        // Not Called
    }

    public static Element createExternalStorageElement(
            final Namespace nameSpace, final Document documentParent,
            final ExternalStorage externalStorage) {

        Element externalStorageElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":ExternalStorage");
        externalStorageElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                externalStorage.getUri());
        externalStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasName",
                externalStorage.getHasName()));
        externalStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                externalStorage.getHasIdentifier()));
        externalStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasModelName",
                externalStorage.getHasModelName()));

        boolean firstSharedStorageVolume = true;
        Element hasPartElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasPart");
        for (SharedStorageVolume sharedStorageVolume : externalStorage
                .getSharedStorageVolumeList()) {
            if (sharedStorageVolume != null) {
                if (firstSharedStorageVolume) {
                    externalStorageElement.appendChild(hasPartElement);
                    firstSharedStorageVolume = false;
                }
                Element sharedStorageVolumeElement = SharedStorageVolumeToXmlRdf
                        .createSharedStorageVolumeElement(nameSpace,
                                documentParent, sharedStorageVolume);
                hasPartElement.appendChild(sharedStorageVolumeElement);
            }
        }

        boolean firstNetworkAdapter = true;
        Element hasNetworkAdapterElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":hasNetworkAdapter");
        for (NetworkAdapter networkAdapter : externalStorage
                .getHasNetworkAdapterList()) {
            if (networkAdapter != null) {
                if (firstNetworkAdapter) {
                    externalStorageElement
                            .appendChild(hasNetworkAdapterElement);
                    firstNetworkAdapter = false;
                }
                hasNetworkAdapterElement.appendChild(NetworkAdapterToXmlRdf
                        .createNetworkAdapterElement(nameSpace, documentParent,
                                networkAdapter));
            }
        }

        externalStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorIPAddress",
                externalStorage.getHasMonitorIPAddress()));

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : externalStorage.getHasMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    externalStorageElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        externalStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                externalStorage.getHasMonitorState()));

        return externalStorageElement;
    }

}