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

package org.cloudsimulator.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToServiceMetric;

@Named("serviceMetricsDAO")
public class ServiceMetricsDAO extends GenericDAO {

    public List<String> getServiceMetricNameListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        String select = "select distinct ?hasMetricName where { ?metric rdf:type icr:ServiceMetric . ?metric icr:dependsOn <"
                + uriEntity
                + "> . ?metric icr:hasMetricName ?hasMetricName . }";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlUtility.createStringList(result);
    }

    public Map<String, List<ServiceMetric>> getServiceMetricMapByEntity(
            final String ipAddressOfKB, final String uriEntity,
            final String dateFrom, final String dateTo) {
        List<String> serviceMetricNameList = getServiceMetricNameListByEntity(
                ipAddressOfKB, uriEntity);
        Map<String, List<ServiceMetric>> serviceMetricMap = new HashMap<String, List<ServiceMetric>>();
        for (String serviceMetricName : serviceMetricNameList) {
            String select = "select ?hasMetricName ?hasMetricValue ?hasMetricUnit ?atTime where { ?metric rdf:type icr:ServiceMetric . ?metric icr:dependsOn <"
                    + uriEntity
                    + "> . ?metric icr:hasMetricName \""
                    + serviceMetricName
                    + "\" . ?metric icr:hasMetricValue ?hasMetricValue . ?metric icr:hasMetricUnit ?hasMetricUnit . ?metric icr:atTime ?atTime . FILTER (xsd:dateTime(?atTime) > \""
                    + dateFrom
                    + "\"^^xsd:dateTime && xsd:dateTime(?atTime) < \""
                    + dateTo
                    + "\"^^xsd:dateTime) }";

            String result = executeQuery(ipAddressOfKB, select);

            serviceMetricMap.put(serviceMetricName, XmlSparqlToServiceMetric
                    .createServiceMetricList(result, serviceMetricName,
                            uriEntity));
        }
        return serviceMetricMap;
    }

    public Map<String, List<List<ServiceMetric>>> getServiceMetricMapByEntityList(
            final String ipAddressOfKB, final List<String> uriEntityList,
            final String dateFrom, final String dateTo) {
        Map<String, List<List<ServiceMetric>>> serviceMetricMap = new HashMap<String, List<List<ServiceMetric>>>();

        for (String uriEntity : uriEntityList) {
            List<String> serviceMetricNameList = getServiceMetricNameListByEntity(
                    ipAddressOfKB, uriEntity);

            for (String serviceMetricName : serviceMetricNameList) {
                String select = "select ?hasMetricValue ?hasMetricUnit ?atTime where { ?metric rdf:type icr:ServiceMetric . ?metric icr:dependsOn <"
                        + uriEntity
                        + "> . ?metric icr:hasMetricName \""
                        + serviceMetricName
                        + "\" . ?metric icr:hasMetricValue ?hasMetricValue . OPTIONAL { ?metric icr:hasMetricUnit ?hasMetricUnit } . ?metric icr:atTime ?atTime . FILTER (xsd:dateTime(?atTime) > \""
                        + dateFrom
                        + "\"^^xsd:dateTime && xsd:dateTime(?atTime) < \""
                        + dateTo + "\"^^xsd:dateTime) }";

                String result = executeQuery(ipAddressOfKB, select);

                List<ServiceMetric> serviceMetricList = XmlSparqlToServiceMetric
                        .createServiceMetricList(result, serviceMetricName,
                                uriEntity);
                if (!serviceMetricList.isEmpty()) {
                    if (serviceMetricMap.get(serviceMetricName) == null) {
                        serviceMetricMap.put(serviceMetricName,
                                new ArrayList<List<ServiceMetric>>());
                    }
                    serviceMetricMap.get(serviceMetricName).add(
                            serviceMetricList);
                }
            }
        }
        return serviceMetricMap;
    }

}
