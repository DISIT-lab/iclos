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
import org.cloudsimulator.xml.XmlUtility;
import org.jdom2.Namespace;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class MonitorInfoToXmlRdf {

    private MonitorInfoToXmlRdf() {
        // Not Called
    }

    public static Element createMonitorInfoElement(final Namespace nameSpace,
            final Document documentParent, final MonitorInfo monitorInfo) {
        Element monitorInfoElement = documentParent.createElementNS(
                nameSpace.getURI(), nameSpace.getPrefix() + ":MonitorInfo");

        monitorInfoElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMetricName",
                monitorInfo.getHasMetricName()));
        monitorInfoElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasArguments",
                monitorInfo.getHasArguments()));
        monitorInfoElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasWarningValue",
                monitorInfo.getHasWarningValue()));
        monitorInfoElement.appendChild(XmlUtility.createDecimalElement(
                nameSpace, documentParent, "hasCriticalValue",
                monitorInfo.getHasCriticalValue()));
        monitorInfoElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasMonitorState",
                monitorInfo.getHasMonitorState()));
        monitorInfoElement.appendChild(XmlUtility.createPositiveIntegerElement(
                nameSpace, documentParent, "hasMaxCheckAttempts",
                monitorInfo.getHasMaxCheckAttempts()));
        monitorInfoElement.appendChild(XmlUtility.createPositiveIntegerElement(
                nameSpace, documentParent, "hasCheckInterval",
                monitorInfo.getHasCheckInterval()));
        monitorInfoElement.appendChild(XmlUtility.createSimpleTextElement(
                nameSpace, documentParent, "hasCheckMode",
                monitorInfo.getHasCheckMode()));

        return monitorInfoElement;
    }
}
