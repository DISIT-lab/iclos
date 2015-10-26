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

import org.cloudsimulator.domain.ontology.IcaroApplication;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToIcaroApplication;

@Named("icaroApplicationDAO")
public class IcaroApplicationDAO extends GenericDAO {

    public List<String> getURIIcaroApplicationListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        String select = "select ?uri where {?uri rdf:type icr:IcaroApplication . <"
                + uriBusinessConfiguration + "> icr:hasPart ?uri}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<IcaroApplication> getIcaroApplicationListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        List<IcaroApplication> icaroApplicationList = new ArrayList<IcaroApplication>();
        for (String uri : getURIIcaroApplicationListByBusinessConfiguration(
                ipAddressOfKB, uriBusinessConfiguration)) {
            icaroApplicationList.add(getIcaroApplicationByURI(ipAddressOfKB,
                    uri));
        }
        return icaroApplicationList;

    }

    public IcaroApplication getIcaroApplicationByURI(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        String select = "select ?hasName ?hasIdentifier ?hasCapacity where { <"
                + uriIcaroApplication + "> rdf:type icr:IcaroApplication . <"
                + uriIcaroApplication
                + "> icr:hasIdentifier ?hasIdentifier . <"
                + uriIcaroApplication + "> icr:hasName ?hasName . <"
                + uriIcaroApplication + "> icr:hasCapacity ?hasCapacity . }";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToIcaroApplication.createIcaroApplication(result,
                uriIcaroApplication);
    }

    public IcaroApplication fetchIcaroApplicationByURI(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        IcaroApplication icaroApplication = getIcaroApplicationByURI(
                ipAddressOfKB, uriIcaroApplication);

        UserDAO userDAO = new UserDAO();
        for (String uriUser : userDAO.getURICreatorListByIcaroApplication(
                ipAddressOfKB, uriIcaroApplication)) {
            icaroApplication.getCreatorList().add(
                    userDAO.getUserByURI(ipAddressOfKB, uriUser));
        }

        IcaroServiceDAO icaroServiceDAO = new IcaroServiceDAO();
        for (final String uriIcaroService : icaroServiceDAO
                .getURIIcaroServiceListByIcaroApplication(ipAddressOfKB,
                        uriIcaroApplication)) {
            icaroApplication.getIcaroServiceList().add(
                    icaroServiceDAO.fetchIcaroServiceByURI(ipAddressOfKB,
                            uriIcaroService));
        }

        // TODO SLA
        /*
         * SLAgreementDAO slAgreementDAO = new SLAgreementDAO(); for (String
         * uriSLAgreement : slAgreementDAO
         * .getURISLAgreementListByIcaroApplication(ipAddressOfKB,
         * uriIcaroApplication)) { icaroApplication.getSlAgreementList().add(
         * slAgreementDAO.fetchSLAgreementByURI( ipAddressOfKB,
         * uriSLAgreement)); }
         */

        return icaroApplication;
    }

}
