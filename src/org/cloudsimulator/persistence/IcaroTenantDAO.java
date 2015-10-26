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

import org.cloudsimulator.domain.ontology.IcaroTenant;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToIcaroTenant;

@Named("icaroTenantDAO")
public class IcaroTenantDAO extends GenericDAO {

    public List<String> getURIIcaroTenantListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        String select = "select ?uri where {?uri rdf:type icr:IcaroTenant . <"
                + uriBusinessConfiguration + "> icr:hasPart ?uri}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<IcaroTenant> getIcaroTenantListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        List<IcaroTenant> icaroTenantList = new ArrayList<IcaroTenant>();
        for (String uri : getURIIcaroTenantListByBusinessConfiguration(
                ipAddressOfKB, uriBusinessConfiguration)) {
            icaroTenantList.add(getIcaroTenantByURI(ipAddressOfKB, uri));
        }
        return icaroTenantList;

    }

    public IcaroTenant getIcaroTenantByURI(final String ipAddressOfKB,
            final String uriIcaroTenant) {
        String select = "select ?hasName ?hasIdentifier ?hasMonitorState where { <"
                + uriIcaroTenant
                + "> rdf:type icr:IcaroTenant . <"
                + uriIcaroTenant
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriIcaroTenant
                + "> icr:hasName ?hasName . <"
                + uriIcaroTenant + "> icr:hasMonitorState ?hasMonitorState}";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToIcaroTenant.createIcaroTenant(result, uriIcaroTenant);
    }

    public IcaroTenant fetchIcaroTenantByURI(final String ipAddressOfKB,
            final String uriIcaroTenant) {
        IcaroTenant icaroTenant = getIcaroTenantByURI(ipAddressOfKB,
                uriIcaroTenant);

        UserDAO userDAO = new UserDAO();
        for (String uriUser : userDAO.getURICreatorListByIcaroTenant(
                ipAddressOfKB, uriIcaroTenant)) {
            icaroTenant.getCreatorList().add(
                    userDAO.getUserByURI(ipAddressOfKB, uriUser));
        }

        MonitorInfoDAO monitorInfoDAO = new MonitorInfoDAO();
        icaroTenant.setMonitorInfoList(monitorInfoDAO
                .getMonitorInfoListByEntity(ipAddressOfKB, uriIcaroTenant));

        // TODO SLA
        /*
         * SLAgreementDAO slAgreementDAO = new SLAgreementDAO(); for (String
         * uriSLAgreement : slAgreementDAO
         * .getURISLAgreementListByIcaroTenant(ipAddressOfKB, uriIcaroTenant)) {
         * icaroTenant.getSlAgreementList().add(
         * slAgreementDAO.fetchSLAgreementByURI( ipAddressOfKB,
         * uriSLAgreement)); }
         */

        return icaroTenant;
    }

}
