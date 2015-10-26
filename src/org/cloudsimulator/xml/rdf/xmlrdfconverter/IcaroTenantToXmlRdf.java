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
import org.cloudsimulator.domain.ontology.IcaroTenant;
import org.cloudsimulator.domain.ontology.MonitorInfo;
import org.cloudsimulator.domain.ontology.SLAgreement;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class IcaroTenantToXmlRdf {

    private IcaroTenantToXmlRdf() {
        // Not Called
    }

    public static Element createIcaroTenantElement(final Namespace nameSpace,
            final Document documentParent, final IcaroTenant icaroTenant,
            final String uriIcaroTenant) {

        Element icaroTenantElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":IcaroTenant");
        icaroTenantElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about", uriIcaroTenant);
        icaroTenantElement
                .appendChild(XmlUtility.createSimpleTextElement(nameSpace,
                        documentParent, "hasName", icaroTenant.getHasName()));
        icaroTenantElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasIdentifier",
                icaroTenant.getHasIdentifier()));
        icaroTenantElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "isTenantOf",
                "urn:cloudicaro:ApplicazioneDiProva"));

        // for (User creator : icaroTenant.getCreatorList()) {
        // if (creator != null) {
        icaroTenantElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "createdBy",
                "urn:cloudicaro:User:UtenteDiProva"));
        // }
        // }
        // TODO da modificare inserire il pannelo di creazioneutente

        boolean firstMonitorInfo = true;
        Element hasMonitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasMonitorInfo");
        for (MonitorInfo monitorInfo : icaroTenant.getMonitorInfoList()) {
            if (monitorInfo != null) {
                if (firstMonitorInfo) {
                    icaroTenantElement.appendChild(hasMonitorInfoElement);
                    firstMonitorInfo = false;
                }
                hasMonitorInfoElement.appendChild(MonitorInfoToXmlRdf
                        .createMonitorInfoElement(nameSpace, documentParent,
                                monitorInfo));
            }
        }

        icaroTenantElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                icaroTenant.getHasMonitorState()));

        boolean firstSlAgreement = true;
        Element hasSLAElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLA");
        for (SLAgreement slAgreement : icaroTenant.getSlAgreementList()) {
            if (slAgreement != null) {
                if (firstSlAgreement) {
                    icaroTenantElement.appendChild(hasSLAElement);
                    firstSlAgreement = false;
                }
                hasSLAElement.appendChild(SLAgreementToXmlRdf
                        .createSLAgreementElement(
                                nameSpace,
                                documentParent,
                                slAgreement,
                                slAgreement.getPathUri()
                                        + uriIcaroTenant.replace(
                                                IcaroApplication.getPathUri(),
                                                "")
                                        + "_"
                                        + icaroTenant.getSlAgreementList()
                                                .indexOf(slAgreement)));
            }
        }

        return icaroTenantElement;
    }
}