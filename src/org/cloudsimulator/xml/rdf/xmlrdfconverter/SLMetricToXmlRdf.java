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

import org.cloudsimulator.domain.ontology.SLMetric;
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SLMetricToXmlRdf {

    private SLMetricToXmlRdf() {
        // Not Called
    }

    public static Element createSLMetricElement(final Namespace nameSpace,
            final Document documentParent, final SLMetric slMetric) {
        Element slMetricElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix()
                        + ":ServiceLevelSimpleMetric");
        slMetricElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMetricName",
                slMetric.getHasName()));

        if ("less".equals(slMetric.getHasSymbol())) {
            slMetricElement.appendChild(XmlUtility.createDecimalElement(
                    nameSpace, documentParent, "hasMetricValueLessThan",
                    slMetric.getHasMetricValue()));
        }
        if ("greater".equals(slMetric.getHasSymbol())) {
            slMetricElement.appendChild(XmlUtility.createDecimalElement(
                    nameSpace, documentParent, "hasMetricValueGreaterThan",
                    slMetric.getHasMetricValue()));
        }
        if ("equal".equals(slMetric.getHasSymbol())) {
            slMetricElement.appendChild(XmlUtility.createDecimalElement(
                    nameSpace, documentParent, "hasMetricValue",
                    slMetric.getHasMetricValue()));
        }

        slMetricElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMetricUnit",
                slMetric.getHasMetricUnit()));

        slMetricElement.appendChild(XmlUtility.createResourceElement(nameSpace,
                documentParent, "dependsOn", slMetric.getDependsOn()));

        return slMetricElement;
    }

}
