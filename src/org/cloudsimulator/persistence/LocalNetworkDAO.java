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

import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToLocalNetwork;

@Named("localNetworkDAO")
public class LocalNetworkDAO extends GenericDAO {

    public List<LocalNetwork> getLocalNetworkListByDataCenter(
            final String ipAddressOfKB, final String uriDataCenter) {
        List<LocalNetwork> localNetworkList = new ArrayList<LocalNetwork>();
        for (String uri : getURILocalNetworkListByDataCenter(ipAddressOfKB,
                uriDataCenter)) {
            localNetworkList.add(getLocalNetworkByURI(ipAddressOfKB, uri));
        }
        return localNetworkList;
    }

    public List<String> getURILocalNetworkListByDataCenter(
            final String ipAddressOfKB, final String uriDataCenter) {
        String select = "select ?uri where { graph ?g { <"
                + uriDataCenter
                + "> rdf:type icr:DataCenter . ?uri rdf:type icr:LocalNetwork.}}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public LocalNetwork getLocalNetworkByURI(final String ipAddressOfKB,
            final String uri) {
        String select = "select ?hasName ?hasIdentifier ?hasIPAddress ?hasSubNetworkMask where { <"
                + uri
                + "> rdf:type icr:LocalNetwork . <"
                + uri
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uri
                + "> icr:hasName ?hasName . OPTIONAL { <"
                + uri
                + "> icr:hasIPAddress ?hasIPAddress } . OPTIONAL { <"
                + uri
                + "> icr:hasSubNetworkMask ?hasSubNetworkMask }}";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToLocalNetwork.createLocalNetwork(result, uri);
    }

    public LocalNetwork fetchLocalNetworkWithUsedIPByURI(
            final String ipAddressOfKB, final String uri) {
        LocalNetwork localNetwork = getLocalNetworkByURI(ipAddressOfKB, uri);

        String select = "select ?hasIPAddress where { <"
                + uri
                + "> rdf:type icr:LocalNetwork . ?boundToNetwork  icr:boundToNetwork <"
                + uri + "> . ?boundToNetwork icr:hasIPAddress ?hasIPAddress }";

        String result = executeQuery(ipAddressOfKB, select);
        localNetwork.setAssignedIP(XmlSparqlUtility.createStringList(result));

        return localNetwork;
    }

}
