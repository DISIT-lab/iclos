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

import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ServiceMetricToXmlRdf {

    private ServiceMetricToXmlRdf() {
        // Not Called
    }

    public static Element createServiceMetricElement(final Namespace nameSpace,
            final Document documentParent, final ServiceMetric serviceMetric) {
        Element serviceMetricElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":ServiceMetric");

        serviceMetricElement
                .appendChild(XmlUtility.createDateTimeElement(nameSpace,
                        documentParent, "atTime", serviceMetric.getAtTime()));
        serviceMetricElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMetricName",
                serviceMetric.getHasMetricName()));
        serviceMetricElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasMetricValue",
                serviceMetric.getHasMetricValue()));
        serviceMetricElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMetricUnit",
                serviceMetric.getHasMetricUnit()));
        serviceMetricElement.appendChild(XmlUtility.createResourceElement(
                nameSpace, documentParent, "dependsOn",
                serviceMetric.getDependsOn()));

        return serviceMetricElement;
    }
}
