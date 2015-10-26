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

import org.cloudsimulator.domain.ontology.HostMachine;
import org.cloudsimulator.domain.ontology.Machine;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToHostMachine;

@Named("hostMachineDAO")
public class HostMachineDAO extends GenericDAO {

    public List<String> getURIHostMachineListByDataCenter(
            final String ipAddressOfKB, final String uriDataCenter) {
        String select = "select ?uri where {?uri rdf:type icr:HostMachine . ?uri icr:isPartOf* <"
                + uriDataCenter + "> }";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<HostMachine> getHostMachineListByDataCenter(
            final String ipAddressOfKB, final String uriDataCenter) {
        List<HostMachine> hostMachineList = new ArrayList<HostMachine>();
        for (String uri : getURIHostMachineListByDataCenter(ipAddressOfKB,
                uriDataCenter)) {
            hostMachineList.add(getHostMachineByURI(ipAddressOfKB, uri));
        }
        return hostMachineList;

    }

    public HostMachine getHostMachineByURI(final String ipAddressOfKB,
            final String uriHostMachine) {
        String select = "select ?hasName ?hasIdentifier ?hasOS ?hasCPUCount ?hasCPUSpeed ?hasCPUType ?hasMemorySize ?isInDomain ?hasAuthUserPassword ?hasAuthUserName ?hasMonitorIPAddress ?hasMonitorState where { <"
                + uriHostMachine
                + "> rdf:type icr:HostMachine . <"
                + uriHostMachine
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriHostMachine
                + "> icr:hasName ?hasName . <"
                + uriHostMachine
                + "> icr:hasOS ?hasOS . <"
                + uriHostMachine
                + "> icr:hasCPUCount ?hasCPUCount . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasCPUSpeed ?hasCPUSpeed } . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasCPUType ?hasCPUType } . <"
                + uriHostMachine
                + "> icr:hasMemorySize ?hasMemorySize . OPTIONAL { <"
                + uriHostMachine
                + "> icr:isInDomain ?isInDomain } . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasAuthUserPassword ?hasAuthUserPassword } . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasAuthUserName ?hasAuthUserName } . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasMonitorIPAddress ?hasMonitorIPAddress } . OPTIONAL { <"
                + uriHostMachine
                + "> icr:hasMonitorState ?hasMonitorState } . OPTIONAL { <"
                + uriHostMachine + "> icr:hasCapacity ?hasCapacity } . }";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToHostMachine.createHostMachine(result, uriHostMachine);
    }

    public Machine fetchHostMachineByURI(final String ipAddressOfKB,
            final String uriHostMachine) {
        HostMachine hostMachine = getHostMachineByURI(ipAddressOfKB,
                uriHostMachine);

        MonitorInfoDAO monitorInfoDAO = new MonitorInfoDAO();
        hostMachine.setHasMonitorInfoList(monitorInfoDAO
                .getMonitorInfoListByEntity(ipAddressOfKB, uriHostMachine));

        NetworkAdapterDAO networkAdapterDAO = new NetworkAdapterDAO();
        hostMachine.setHasNetworkAdapterList(networkAdapterDAO
                .getNetworkAdapterListByEntity(ipAddressOfKB, uriHostMachine));

        LocalStorageDAO localStorageDAO = new LocalStorageDAO();
        hostMachine.setHasLocalStorageList(localStorageDAO
                .getLocalStorageListByEntity(ipAddressOfKB, uriHostMachine));

        SharedStorageVolumeDAO sharedStorageVolumeDAO = new SharedStorageVolumeDAO();
        hostMachine.setUseSharedStorageList(sharedStorageVolumeDAO
                .getURISharedStorageVolumeListByEntity(ipAddressOfKB,
                        uriHostMachine));

        VirtualMachineDAO virtualMachineDAO = new VirtualMachineDAO();
        for (String uriVirtualMachine : virtualMachineDAO
                .getURIVirtualMachineListByHostMachine(ipAddressOfKB,
                        uriHostMachine)) {
            hostMachine.getVirtualMachineList().add(
                    virtualMachineDAO.fetchVirtualMachineByURI(ipAddressOfKB,
                            uriVirtualMachine));
        }

        return hostMachine;
    }

}
