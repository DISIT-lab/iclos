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

import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class VirtualStorageToXmlRdf {

    private VirtualStorageToXmlRdf() {
        // Not Called
    }

    public static Element createVirtualStorageElement(
            final Namespace nameSpace, final Document documentParent,
            final VirtualStorage virtualStorage) {

        Element virtualStorageElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":VirtualStorage");
        virtualStorageElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                virtualStorage.getUri());
        virtualStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasName",
                virtualStorage.getHasName()));
        virtualStorageElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                virtualStorage.getHasIdentifier()));
        virtualStorageElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasDiskSize",
                virtualStorage.getHasDiskSize()));
        virtualStorageElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "isStoredOn",
                virtualStorage.getIsStoredOn()));

        return virtualStorageElement;
    }
}
