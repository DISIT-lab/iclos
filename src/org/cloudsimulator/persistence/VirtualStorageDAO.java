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

import org.cloudsimulator.domain.ontology.VirtualStorage;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToVirtualStorage;

@Named("virtualStorageDAO")
public class VirtualStorageDAO extends GenericDAO {

    public List<String> getURIVirtualStorageListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        final String select = "select ?uri where { ?uri rdf:type icr:VirtualStorage . <"
                + uriEntity + "> icr:hasVirtualStorage ?uri }";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<VirtualStorage> getVirtualStorageListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        List<VirtualStorage> virtualStorageList = new ArrayList<VirtualStorage>();
        for (String uriVirtualStorage : getURIVirtualStorageListByEntity(
                ipAddressOfKB, uriEntity)) {
            virtualStorageList.add(getVirtualStorageByURI(ipAddressOfKB,
                    uriVirtualStorage));
        }

        return virtualStorageList;

    }

    public VirtualStorage getVirtualStorageByURI(final String ipAddressOfKB,
            final String uriVirtualStorage) {
        final String select = "select ?hasName ?hasIdentifier ?hasDiskSize ?isStoredOn where { <"
                + uriVirtualStorage
                + "> rdf:type icr:VirtualStorage . <"
                + uriVirtualStorage
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriVirtualStorage
                + "> icr:hasName ?hasName . <"
                + uriVirtualStorage
                + "> icr:hasDiskSize ?hasDiskSize . OPTIONAL { <"
                + uriVirtualStorage + "> icr:isStoredOn ?isStoredOn } }";

        final String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToVirtualStorage.createVirtualStorage(result,
                uriVirtualStorage);
    }

}
