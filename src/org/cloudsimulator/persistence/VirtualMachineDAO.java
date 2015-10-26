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

import org.cloudsimulator.domain.ontology.VirtualMachine;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToVirtualMachine;

@Named("virtualMachineDAO")
public class VirtualMachineDAO extends GenericDAO {

    public List<String> getURIVirtualMachineListByHostMachine(
            final String ipAddressOfKB, final String uriHostMachine) {
        final String select = "select ?uri where { ?uri rdf:type icr:VirtualMachine . ?uri icr:isPartOf <"
                + uriHostMachine + "> }";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<VirtualMachine> getVirtualMachineListByHostMachine(
            final String ipAddressOfKB, final String uriHostMachine) {
        List<VirtualMachine> virtualMachineList = new ArrayList<VirtualMachine>();
        for (String uriVirtualMachine : getURIVirtualMachineListByHostMachine(
                ipAddressOfKB, uriHostMachine)) {
            virtualMachineList.add(getVirtualMachineByURI(ipAddressOfKB,
                    uriVirtualMachine));
        }

        return virtualMachineList;

    }

    public VirtualMachine getVirtualMachineByURI(final String ipAddressOfKB,
            final String uriVirtualMachine) {
        final String select = "select ?hasName ?hasIdentifier  ?hasCPUCount ?hasCPUSpeedReservation ?hasCPUSpeedLimit ?hasMemorySize ?hasMemoryReservation ?hasMemoryLimit ?hasExternalIPAddress ?hasMonitorIPAddress ?hasOS ?isPartOf ?isStoredOn ?isInDomain ?hasAuthUserPassword ?hasAuthUserName ?hasMonitorState where { <"
                + uriVirtualMachine
                + "> rdf:type icr:VirtualMachine . <"
                + uriVirtualMachine
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriVirtualMachine
                + "> icr:hasName ?hasName . <"
                + uriVirtualMachine
                + "> icr:hasOS ?hasOS . <"
                + uriVirtualMachine
                + "> icr:isStoredOn ?isStoredOn . <"
                + uriVirtualMachine
                + "> icr:isPartOf ?isPartOf . <"
                + uriVirtualMachine
                + "> icr:hasCPUCount ?hasCPUCount . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasCPUSpeedReservation ?hasCPUSpeedReservation } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasCPUSpeedLimit ?hasCPUSpeedLimit } . <"
                + uriVirtualMachine
                + "> icr:hasMemorySize ?hasMemorySize . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasMemoryReservation ?hasMemoryReservation } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasMemoryLimit ?hasMemoryLimit } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasExternalIPAddress ?hasExternalIPAddress } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasMonitorIPAddress ?hasMonitorIPAddress } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:isInDomain ?isInDomain } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasAuthUserPassword ?hasAuthUserPassword } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasAuthUserName ?hasAuthUserName } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasMonitorState ?hasMonitorState } . OPTIONAL { <"
                + uriVirtualMachine
                + "> icr:hasCapacity ?hasCapacity } . "
                + "}";

        final String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToVirtualMachine.createVirtualMachine(result,
                uriVirtualMachine);
    }

    public VirtualMachine fetchVirtualMachineByURI(final String ipAddressOfKB,
            final String uriVirtualMachine) {
        final VirtualMachine virtualMachine = getVirtualMachineByURI(
                ipAddressOfKB, uriVirtualMachine);

        MonitorInfoDAO monitorInfoDAO = new MonitorInfoDAO();
        virtualMachine.setHasMonitorInfoList(monitorInfoDAO
                .getMonitorInfoListByEntity(ipAddressOfKB, uriVirtualMachine));

        NetworkAdapterDAO networkAdapterDAO = new NetworkAdapterDAO();
        virtualMachine
                .setHasNetworkAdapterList(networkAdapterDAO
                        .getNetworkAdapterListByEntity(ipAddressOfKB,
                                uriVirtualMachine));

        VirtualStorageDAO virtualStorageDAO = new VirtualStorageDAO();
        virtualMachine
                .setHasVirtualStorageList(virtualStorageDAO
                        .getVirtualStorageListByEntity(ipAddressOfKB,
                                uriVirtualMachine));

        IcaroServiceDAO icaroServiceDAO = new IcaroServiceDAO();
        for (final String uriIcaroService : icaroServiceDAO
                .getURIIcaroServiceListByVirtualMachine(ipAddressOfKB,
                        uriVirtualMachine)) {
            virtualMachine.getIcaroServiceList().add(
                    icaroServiceDAO.fetchIcaroServiceByURI(ipAddressOfKB,
                            uriIcaroService));
        }

        return virtualMachine;
    }

}
