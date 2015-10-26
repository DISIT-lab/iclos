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

import org.cloudsimulator.domain.ontology.LocalStorage;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToLocalStorage;

@Named("localStorageDAO")
public class LocalStorageDAO extends GenericDAO {

    public List<String> getURILocalStorageListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        final String select = "select ?uri where { ?uri rdf:type icr:LocalStorage . <"
                + uriEntity + "> icr:hasLocalStorage ?uri }";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<LocalStorage> getLocalStorageListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        List<LocalStorage> localStorageList = new ArrayList<LocalStorage>();
        for (String uriLocalStorage : getURILocalStorageListByEntity(
                ipAddressOfKB, uriEntity)) {
            localStorageList.add(getLocalStorageByURI(ipAddressOfKB,
                    uriLocalStorage));
        }

        return localStorageList;

    }

    public LocalStorage getLocalStorageByURI(final String ipAddressOfKB,
            final String uriLocalStorage) {
        final String select = "select ?hasName ?hasIdentifier ?hasDiskSize where { <"
                + uriLocalStorage
                + "> rdf:type icr:LocalStorage . <"
                + uriLocalStorage
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriLocalStorage
                + "> icr:hasName ?hasName . <"
                + uriLocalStorage + "> icr:hasDiskSize ?hasDiskSize . }";

        final String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToLocalStorage.createLocalStorage(result,
                uriLocalStorage);
    }

}
