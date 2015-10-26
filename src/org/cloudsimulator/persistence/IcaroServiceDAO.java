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

import org.cloudsimulator.domain.ontology.IcaroService;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToIcaroService;

@Named("icaroServiceDAO")
public class IcaroServiceDAO extends GenericDAO {

    public List<String> getURIIcaroServiceListByVirtualMachine(
            final String ipAddressOfKB, final String uriVirtualMachine) {
        final String select = "select ?uri where { ?uri rdf:type icr:IcaroService . ?uri icr:runsOnVM <"
                + uriVirtualMachine + "> }";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<IcaroService> getIcaroServiceListByVirtualMachine(
            final String ipAddressOfKB, final String uriVirtualMachine) {
        List<IcaroService> icaroServiceList = new ArrayList<IcaroService>();
        for (String uri : getURIIcaroServiceListByVirtualMachine(ipAddressOfKB,
                uriVirtualMachine)) {
            icaroServiceList.add(getIcaroServiceByURI(ipAddressOfKB, uri));
        }

        return icaroServiceList;
    }

    public List<String> getURIIcaroServiceListByIcaroApplication(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        final String select = "select ?uri where { <" + uriIcaroApplication
                + "> rdf:type icr:IcaroApplication . <" + uriIcaroApplication
                + "> icr:needs ?uri . ?uri rdf:type icr:IcaroService }";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<IcaroService> getIcaroServiceListByIcaroApplication(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        List<IcaroService> icaroServiceList = new ArrayList<IcaroService>();
        for (String uri : getURIIcaroServiceListByIcaroApplication(
                ipAddressOfKB, uriIcaroApplication)) {
            icaroServiceList.add(getIcaroServiceByURI(ipAddressOfKB, uri));
        }

        return icaroServiceList;
    }

    public IcaroService getIcaroServiceByURI(final String ipAddressOfKB,
            final String uriIcaroService) {
        String select = "select ?hasName ?hasIdentifier ?supportsLanguage ?hasAuthUserPassword ?hasAuthUserName ?hasProcessName ?hasMonitorIPAddress ?hasMonitorState where { <"
                + uriIcaroService
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriIcaroService
                + "> icr:hasName ?hasName . OPTIONAL { <"
                + uriIcaroService
                + "> icr:supportsLanguage ?supportsLanguage } . "
                + "OPTIONAL { <"
                + uriIcaroService
                + "> icr:hasAuthUserPassword ?hasAuthUserPassword } . OPTIONAL { <"
                + uriIcaroService
                + "> icr:hasAuthUserName ?hasAuthUserName } . OPTIONAL { <"
                + uriIcaroService
                + "> icr:hasProcessName ?hasProcessName } . OPTIONAL { <"
                + uriIcaroService
                + "> icr:hasMonitorIPAddress ?hasMonitorIPAddress } . OPTIONAL { <"
                + uriIcaroService
                + "> icr:hasMonitorState ?hasMonitorState } . }";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToIcaroService.createIcaroService(result,
                uriIcaroService);
    }

    public IcaroService fetchIcaroServiceByURI(final String ipAddressOfKB,
            final String uriIcaroService) {
        IcaroService icaroService = getIcaroServiceByURI(ipAddressOfKB,
                uriIcaroService);

        MonitorInfoDAO monitorInfoDAO = new MonitorInfoDAO();
        icaroService.setMonitorInfoList(monitorInfoDAO
                .getMonitorInfoListByEntity(ipAddressOfKB, uriIcaroService));

        // Devo prendere queste due entità in questo modo perché potrebbero
        // essere delle liste di valori e non un singolo valore.
        String select = "select ?usesTcpPort where { <" + uriIcaroService
                + "> rdf:type icr:IcaroService . <" + uriIcaroService
                + "> icr:usesTcpPort ?usesTcpPort . }";

        String result = executeQuery(ipAddressOfKB, select);
        icaroService.setUsesTcpPortList(XmlSparqlUtility
                .createIntegerList(result));

        select = "select ?uri where { <" + uriIcaroService
                + "> rdf:type icr:IcaroService . ?uri icr:usesUdpPort <"
                + uriIcaroService + ">  . }";

        result = executeQuery(ipAddressOfKB, select);
        icaroService.setUsesUdpPortList(XmlSparqlUtility
                .createIntegerList(result));

        return icaroService;
    }

}
