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

import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.domain.ontology.IcaroTenant;
import org.cloudsimulator.domain.ontology.SLAgreement;
import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class BusinessConfigurationToXmlRdf {

    private BusinessConfigurationToXmlRdf() {
        // Not Called
    }

    public static Element createBusinessConfigurationElement(
            final Namespace nameSpace, final Document documentParent,
            final BusinessConfiguration businessConfiguration,
            final String uriBusinessConfiguration) {
        Element businessConfigurationElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":BusinessConfiguration");

        businessConfigurationElement.setAttributeNS(
                NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about",
                uriBusinessConfiguration);
        businessConfigurationElement.appendChild(XmlUtility
                .createSimpleTextElement(nameSpace, documentParent, "hasName",
                        businessConfiguration.getHasName()));
        businessConfigurationElement.appendChild(XmlUtility
                .createSimpleTextElement(nameSpace, documentParent,
                        "hasIdentifier",
                        businessConfiguration.getHasIdentifier()));
        businessConfigurationElement.appendChild(XmlUtility
                .createSimpleTextElement(nameSpace, documentParent,
                        "hasContractId",
                        businessConfiguration.getHasContractId()));

        for (User creator : businessConfiguration.getCreatorList()) {
            if (creator != null) {
                businessConfigurationElement.appendChild(XmlUtility
                        .createResourceElement(nameSpace, documentParent,
                                "createdBy", creator.getUri()));
            }
        }

        boolean firstIcaroApplicationOrTenant = true;
        Element hasPartElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasPart");
        for (IcaroApplication icaroApplication : businessConfiguration
                .getIcaroApplicationList()) {
            if (icaroApplication != null) {
                if (firstIcaroApplicationOrTenant) {
                    businessConfigurationElement.appendChild(hasPartElement);
                    firstIcaroApplicationOrTenant = false;
                }
                Element icaroApplicationElement = IcaroApplicationToXmlRdf
                        .createIcaroApplicationElement(nameSpace,
                                documentParent, icaroApplication,
                                IcaroApplication.getPathUri()
                                        + icaroApplication.getTypeRdf()
                                                .replace("&app;", "")
                                        + ":"
                                        + businessConfiguration.getLocalUri()
                                        + "_"
                                        + businessConfiguration
                                                .getIcaroApplicationList()
                                                .indexOf(icaroApplication));
                hasPartElement.appendChild(icaroApplicationElement);
            }
        }
        for (IcaroTenant icaroTenant : businessConfiguration
                .getIcaroTenantList()) {
            if (icaroTenant != null) {
                if (firstIcaroApplicationOrTenant) {
                    businessConfigurationElement.appendChild(hasPartElement);
                    firstIcaroApplicationOrTenant = false;
                }
                Element icaroTenantElement = IcaroTenantToXmlRdf
                        .createIcaroTenantElement(
                                nameSpace,
                                documentParent,
                                icaroTenant,
                                IcaroTenant.getPathUri()
                                        + "Tenant:"
                                        + businessConfiguration.getLocalUri()
                                        + "_"
                                        + businessConfiguration
                                                .getIcaroTenantList().indexOf(
                                                        icaroTenant));
                // Element icaroTenantElement =
                // IcaroTenantToXmlRdf.createIcaroTenantElement(nameSpace,
                // documentParent, icaroTenant, IcaroTenant.getPathUri()
                // +
                // icaroTenant.getIsTenantOf().substring(IcaroApplication.getPathUri().length(),
                // icaroTenant.getIsTenantOf().lastIndexOf(":")) + "Tenant:" +
                // businessConfiguration.getLocalUri() + "_"
                // +
                // businessConfiguration.getIcaroTenantList().indexOf(icaroTenant));
                hasPartElement.appendChild(icaroTenantElement);
                // TODO Correggere
            }
        }

        boolean firstSlAgreement = true;
        Element hasSLAElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLA");
        for (SLAgreement slAgreement : businessConfiguration
                .getSlAgreementList()) {
            if (slAgreement != null) {
                if (firstSlAgreement) {
                    businessConfigurationElement.appendChild(hasSLAElement);
                    firstSlAgreement = false;
                }
                hasSLAElement.appendChild(SLAgreementToXmlRdf
                        .createSLAgreementElement(
                                nameSpace,
                                documentParent,
                                slAgreement,
                                slAgreement.getPathUri()
                                        + businessConfiguration.getLocalUri()
                                        + "_"
                                        + businessConfiguration
                                                .getSlAgreementList().indexOf(
                                                        slAgreement)));
            }
        }

        return businessConfigurationElement;
    }

}
