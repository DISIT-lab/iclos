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

import org.cloudsimulator.domain.ontology.SLAgreement;
import org.cloudsimulator.domain.ontology.SLObjective;
import org.cloudsimulator.repository.NameSpaceRepository;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SLAgreementToXmlRdf {

    private SLAgreementToXmlRdf() {
        // Not Called
    }

    public static Element createSLAgreementElement(final Namespace nameSpace,
            final Document documentParent, final SLAgreement slAgreement,
            final String uriSlAgreement) {
        Element slAgreementElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":ServiceLevelAgreement");
        slAgreementElement.setAttributeNS(NameSpaceRepository.RDF.getURI(),
                NameSpaceRepository.RDF.getPrefix() + ":about", uriSlAgreement);

        boolean firstSlObjective = true;
        Element hasSLObjectiveElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLObjective");
        for (SLObjective slObjective : slAgreement.getSlObjectiveList()) {
            if (slObjective != null) {
                if (firstSlObjective) {
                    slAgreementElement.appendChild(hasSLObjectiveElement);
                    firstSlObjective = false;
                }
                hasSLObjectiveElement.appendChild(SLObjectiveToXmlRdf
                        .createSLObjectiveElement(nameSpace, documentParent,
                                slObjective));
            }
        }

        slAgreementElement.appendChild(XmlUtility.createDateTimeElement(
                nameSpace, documentParent, "hasStartTime",
                slAgreement.getHasStartTime() + ":00"));
        slAgreementElement.appendChild(XmlUtility.createDateTimeElement(
                nameSpace, documentParent, "hasEndTime",
                slAgreement.getHasEndTime() + ":00"));

        return slAgreementElement;
    }
}
