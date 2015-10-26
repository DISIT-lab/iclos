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
import java.util.List;

import javax.inject.Named;

import org.cloudsimulator.domain.ontology.DataCenter;
import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToDataCenter;

@Named("dataCenterDAO")
public class DataCenterDAO extends GenericDAO {

    public List<String> getURIDataCenterListByKB(final String ipAddressOfKB) {
        String select = "select ?uri where {?uri rdf:type icr:DataCenter}";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<DataCenter> getDataCenterListByKB(final String ipAddressOfKB) {
        List<DataCenter> dataCenterList = new ArrayList<DataCenter>();
        for (String uri : getURIDataCenterListByKB(ipAddressOfKB)) {
            dataCenterList.add(getDataCenterByURI(ipAddressOfKB, uri));
        }
        return dataCenterList;
    }

    public DataCenter getDataCenterByURI(final String ipAddressOfKB,
            final String uriDataCenter) {
        String select = "select ?hasName ?hasIdentifier where { <"
                + uriDataCenter + "> rdf:type icr:DataCenter . <"
                + uriDataCenter + "> icr:hasName ?hasName . <" + uriDataCenter
                + "> icr:hasIdentifier ?hasIdentifier}";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToDataCenter.createDataCenter(result, uriDataCenter);
    }

    public DataCenter fetchDataCenterByURI(final String ipAddressOfKB,
            final String uriDataCenter) {
        DataCenter dataCenter = getDataCenterByURI(ipAddressOfKB, uriDataCenter);

        HostMachineDAO hostMachineDAO = new HostMachineDAO();
        for (String uriHostMachine : hostMachineDAO
                .getURIHostMachineListByDataCenter(ipAddressOfKB, uriDataCenter)) {
            dataCenter.getHostMachineList().add(
                    (HostMachine) hostMachineDAO.fetchHostMachineByURI(ipAddressOfKB,
                            uriHostMachine));
        }

        LocalNetworkDAO localNetworkDAO = new LocalNetworkDAO();
        for (String uriLocalNetwork : localNetworkDAO
                .getURILocalNetworkListByDataCenter(ipAddressOfKB,
                        uriDataCenter)) {
            dataCenter.getLocalNetworkList().add(
                    localNetworkDAO.fetchLocalNetworkWithUsedIPByURI(
                            ipAddressOfKB, uriLocalNetwork));
        }

        return dataCenter;
    }

}
