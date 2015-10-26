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

import org.cloudsimulator.domain.ontology.BusinessConfiguration;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToBusinessConfiguration;

@Named("businessConfigurationDAO")
public class BusinessConfigurationDAO extends GenericDAO {

    public List<String> getURIBusinessConfigurationListByKB(
            final String ipAddressOfKB) {
        String select = "select ?uri where {?uri rdf:type icr:BusinessConfiguration}";
        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<BusinessConfiguration> getBusinessConfigurationListByKB(
            final String ipAddressOfKB) {
        List<BusinessConfiguration> businessConfigurationList = new ArrayList<BusinessConfiguration>();
        for (String uri : getURIBusinessConfigurationListByKB(ipAddressOfKB)) {
            businessConfigurationList.add(getBusinessConfigurationByURI(
                    ipAddressOfKB, uri));
        }

        return businessConfigurationList;
    }

    public List<String> getURIBusinessConfigurationListByVirtualMachine(
            final String ipAddressOfKB, final String uriVirtualMachine) {
        String select = "select ?uri where { graph ?g { <"
                + uriVirtualMachine
                + "> rdf:type icr:VirtualMachine . ?uri rdf:type icr:BusinessConfiguration.}}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<BusinessConfiguration> getBusinessConfigurationListByVirtualMachine(
            final String ipAddressOfKB, final String uriVirtualMachine) {
        List<BusinessConfiguration> businessConfigurationList = new ArrayList<BusinessConfiguration>();
        for (String uri : getURIBusinessConfigurationListByVirtualMachine(
                ipAddressOfKB, uriVirtualMachine)) {
            businessConfigurationList.add(getBusinessConfigurationByURI(
                    ipAddressOfKB, uri));
        }

        return businessConfigurationList;
    }

    public BusinessConfiguration getBusinessConfigurationByURI(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        String select = "select ?hasName ?hasIdentifier ?hasContractId where { <"
                + uriBusinessConfiguration
                + "> rdf:type icr:BusinessConfiguration . <"
                + uriBusinessConfiguration
                + "> icr:hasName ?hasName . <"
                + uriBusinessConfiguration
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriBusinessConfiguration
                + "> icr:hasContractId ?hasContractId}";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToBusinessConfiguration.createBusinessConfiguration(
                result, uriBusinessConfiguration);
    }

    public BusinessConfiguration fetchBusinessConfigurationByURI(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        BusinessConfiguration businessConfiguration = getBusinessConfigurationByURI(
                ipAddressOfKB, uriBusinessConfiguration);

        UserDAO userDAO = new UserDAO();
        for (String uriUser : userDAO.getURIUserListByBusinessConfiguration(
                ipAddressOfKB, uriBusinessConfiguration)) {
            businessConfiguration.getCreatorList().add(
                    userDAO.getUserByURI(ipAddressOfKB, uriUser));
        }

        IcaroApplicationDAO icaroApplicationDAO = new IcaroApplicationDAO();
        for (String uriIcaroApplication : icaroApplicationDAO
                .getURIIcaroApplicationListByBusinessConfiguration(
                        ipAddressOfKB, uriBusinessConfiguration)) {
            businessConfiguration.getIcaroApplicationList().add(
                    icaroApplicationDAO.fetchIcaroApplicationByURI(
                            ipAddressOfKB, uriIcaroApplication));
        }

        IcaroTenantDAO icaroTenantDAO = new IcaroTenantDAO();
        for (String uriIcaroTenant : icaroTenantDAO
                .getURIIcaroTenantListByBusinessConfiguration(ipAddressOfKB,
                        uriBusinessConfiguration)) {
            businessConfiguration.getIcaroTenantList().add(
                    icaroTenantDAO.fetchIcaroTenantByURI(ipAddressOfKB,
                            uriIcaroTenant));
        }

        // TODO SLA
        /*
         * SLAgreementDAO slAgreementDAO = new SLAgreementDAO(); for (String
         * uriSLAgreement : slAgreementDAO
         * .getURISLAgreementListByBusinessConfiguration(ipAddressOfKB,
         * uriBusinessConfiguration)) {
         * businessConfiguration.getSlAgreementList().add(
         * slAgreementDAO.fetchSLAgreementByURI( ipAddressOfKB,
         * uriSLAgreement)); }
         */

        return businessConfiguration;
    }

}
