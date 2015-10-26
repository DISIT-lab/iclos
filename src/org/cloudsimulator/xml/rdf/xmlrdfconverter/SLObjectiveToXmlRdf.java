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

import org.cloudsimulator.domain.ontology.SLAction;
import org.cloudsimulator.domain.ontology.SLMetric;
import org.cloudsimulator.domain.ontology.SLObjective;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SLObjectiveToXmlRdf {

    private SLObjectiveToXmlRdf() {
        // Not Called
    }

    public static Element createSLObjectiveElement(final Namespace nameSpace,
            final Document documentParent, final SLObjective slObjective) {
        Element slObjectiveElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":ServiceLevelObjective");

        boolean firstSlAction = true;
        Element hasSLActionElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLAction");
        for (SLAction slAction : slObjective.getSlActionList()) {
            if (slAction != null) {
                if (firstSlAction) {
                    slObjectiveElement.appendChild(hasSLActionElement);
                    firstSlAction = false;
                }
                hasSLActionElement.appendChild(SLActionToXmlRdf
                        .createSLActionElement(nameSpace, documentParent,
                                slAction));
            }
        }

        boolean firstSlMetric = true;
        Element hasSLMetricElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":hasSLMetric");
        Element hasSLAndMetricElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":ServiceLevelAndMetric");
        Element dependsOnElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":dependsOn");
        for (SLMetric slMetric : slObjective.getSlMetricList()) {
            if (slMetric != null) {
                if (firstSlMetric) {
                    slObjectiveElement.appendChild(hasSLMetricElement);
                    firstSlMetric = false;
                    if (slObjective.getSlMetricList().size() > 1) {
                        hasSLMetricElement.appendChild(hasSLAndMetricElement);
                        hasSLAndMetricElement.appendChild(dependsOnElement);
                    }
                }
                if (slObjective.getSlMetricList().size() > 1) {
                    dependsOnElement.appendChild(SLMetricToXmlRdf
                            .createSLMetricElement(nameSpace, documentParent,
                                    slMetric));
                } else {
                    hasSLMetricElement.appendChild(SLMetricToXmlRdf
                            .createSLMetricElement(nameSpace, documentParent,
                                    slMetric));
                }
                // TODO rivedere se si pu� far di meglio
            }
        }

        return slObjectiveElement;
    }

}
