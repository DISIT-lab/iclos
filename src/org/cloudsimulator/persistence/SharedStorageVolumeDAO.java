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

import org.cloudsimulator.domain.ontology.SharedStorageVolume;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToSharedStorageVolume;

@Named("sharedStorageVolumeDAO")
public class SharedStorageVolumeDAO extends GenericDAO {

    public List<String> getURISharedStorageVolumeListByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        final String selectForExternalStorage = "select ?uri where { ?uri rdf:type icr:SharedStorageVolume . <"
                + uriEntity + "> icr:hasPart ?uri }";
        List<String> resultStringList = XmlSparqlUtility
                .createUriList(executeQuery(ipAddressOfKB,
                        selectForExternalStorage));

        if (resultStringList.isEmpty()) {
            final String selectForHostMachine = "select ?uri where { ?uri rdf:type icr:SharedStorageVolume . <"
                    + uriEntity + "> icr:useSharedStorage ?uri }";
            resultStringList = XmlSparqlUtility.createUriList(executeQuery(
                    ipAddressOfKB, selectForHostMachine));
        }
        return resultStringList;
    }

    public List<SharedStorageVolume> getSharedStorageVolumeByEntity(
            final String ipAddressOfKB, final String uriEntity) {
        List<SharedStorageVolume> sharedStorageVolumeList = new ArrayList<SharedStorageVolume>();
        for (String uriSharedStorageVolume : getURISharedStorageVolumeListByEntity(
                ipAddressOfKB, uriEntity)) {
            sharedStorageVolumeList.add(getSharedStorageVolumeByURI(
                    ipAddressOfKB, uriSharedStorageVolume));
        }

        return sharedStorageVolumeList;

    }

    public SharedStorageVolume getSharedStorageVolumeByURI(
            final String ipAddressOfKB, final String uriSharedStorageVolume) {
        final String select = "select ?hasName ?hasIdentifier ?hasDiskSize where { <"
                + uriSharedStorageVolume
                + "> rdf:type icr:SharedStorageVolume . <"
                + uriSharedStorageVolume
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriSharedStorageVolume
                + "> icr:hasName ?hasName . <"
                + uriSharedStorageVolume + "> icr:hasDiskSize ?hasDiskSize . }";

        final String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToSharedStorageVolume.createSharedStorageVolume(result,
                uriSharedStorageVolume);
    }

}
