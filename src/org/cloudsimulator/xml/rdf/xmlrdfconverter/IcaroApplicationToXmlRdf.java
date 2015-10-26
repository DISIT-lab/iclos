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

import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.domain.ontology.IcaroService;
import org.cloudsimulator.domain.ontology.SLAgreement;
import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class IcaroApplicationToXmlRdf {

    private IcaroApplicationToXmlRdf() {
        // Not Called
    }

    public static Element createIcaroApplicationElement(
            final Namespace nameSpace, final Document documentParent,
            final IcaroApplication icaroApplication,
            final String uriIcaroApplication) {

        Element icaroApplicationElement = documentParent
                .createElementNS(nameSpace.getURI(), nameSpace.getPrefix()
                        + ":IcaroApplication");
        icaroApplicationElement.setAttributeNS(
                NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                uriIcaroApplication);
        icaroApplicationElement.appendChild(XmlUtility.createResourceElement(
                NameSpaceRepository.RDF, documentParent, "type",
                icaroApplication.getTypeRdf()));
        icaroApplicationElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasName",
                icaroApplication.getHasName()));
        icaroApplicationElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                icaroApplication.getHasIdentifier()));

        for (User creator : icaroApplication.getCreatorList()) {
            if (creator != null) {
                icaroApplicationElement.appendChild(XmlUtility
                        .createResourceElement(nameSpace, documentParent,
                                "createdBy", creator.getUri()));
            }
        }

        boolean firstNeeds = true;
        Element needsElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":needs");
        for (IcaroService icaroService : icaroApplication.getIcaroServiceList()) {
            if (icaroService != null) {
                if (firstNeeds) {
                    icaroApplicationElement.appendChild(needsElement);
                    firstNeeds = false;
                }
                needsElement.appendChild(IcaroServiceToXmlRdf
                        .createIcaroServiceElement(
                                nameSpace,
                                documentParent,
                                icaroService,
                                IcaroService.getPathUri()
                                        + icaroService.getTypeRdf().replace(
                                                "&icr;", "")
                                        + ":"
                                        + icaroApplication
                                                .getIcaroServiceList().indexOf(
                                                        icaroService)));
            }
        }

        boolean firstSlAgreement = true;
        Element hasSLAElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLA");
        for (SLAgreement slAgreement : icaroApplication.getSlAgreementList()) {
            if (slAgreement != null) {
                if (firstSlAgreement) {
                    icaroApplicationElement.appendChild(hasSLAElement);
                    firstSlAgreement = false;
                }
                hasSLAElement.appendChild(SLAgreementToXmlRdf
                        .createSLAgreementElement(
                                nameSpace,
                                documentParent,
                                slAgreement,
                                slAgreement.getPathUri()
                                        + uriIcaroApplication.replace(
                                                IcaroApplication.getPathUri(),
                                                "")
                                        + "_"
                                        + icaroApplication.getSlAgreementList()
                                                .indexOf(slAgreement)));
            }
        }

        icaroApplicationElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCapacity",
                icaroApplication.getHasCapacity()));

        return icaroApplicationElement;
    }
}