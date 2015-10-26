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

import org.cloudsimulator.domain.ontology.User;
import org.cloudsimulator.xml.sparql.XmlSparqlUtility;
import org.cloudsimulator.xml.sparql.xmlsparqlconverter.XmlSparqlToUser;

@Named("userDAO")
public class UserDAO extends GenericDAO {

    public List<User> getUserListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        List<User> userList = new ArrayList<User>();
        for (String uri : getURIUserListByBusinessConfiguration(ipAddressOfKB,
                uriBusinessConfiguration)) {
            userList.add(getUserByURI(ipAddressOfKB, uri));
        }
        return userList;
    }

    public List<String> getURIUserListByBusinessConfiguration(
            final String ipAddressOfKB, final String uriBusinessConfiguration) {
        String select = "select ?uri where { graph ?g { <"
                + uriBusinessConfiguration
                + "> rdf:type icr:BusinessConfiguration . ?uri rdf:type icr:User.}}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<User> getCreatorListByIcaroApplication(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        List<User> creatorList = new ArrayList<User>();
        for (String uri : getURICreatorListByIcaroApplication(ipAddressOfKB,
                uriIcaroApplication)) {
            creatorList.add(getUserByURI(ipAddressOfKB, uri));
        }
        return creatorList;
    }

    public List<String> getURICreatorListByIcaroApplication(
            final String ipAddressOfKB, final String uriIcaroApplication) {
        String select = "select ?uri where {<" + uriIcaroApplication
                + "> rdf:type icr:IcaroApplication . <" + uriIcaroApplication
                + "> icr:createdBy ?uri.}}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public List<User> getCreatorListByIcaroTenant(final String ipAddressOfKB,
            final String uriIcaroTenant) {
        List<User> creatorList = new ArrayList<User>();
        for (String uri : getURICreatorListByIcaroTenant(ipAddressOfKB,
                uriIcaroTenant)) {
            creatorList.add(getUserByURI(ipAddressOfKB, uri));
        }
        return creatorList;
    }

    public List<String> getURICreatorListByIcaroTenant(
            final String ipAddressOfKB, final String uriIcaroTenant) {
        String select = "select ?uri where {<" + uriIcaroTenant
                + "> rdf:type icr:IcaroTenant . <" + uriIcaroTenant
                + "> icr:createdBy ?uri.}}";

        return XmlSparqlUtility.createUriList(executeQuery(ipAddressOfKB,
                select));
    }

    public User getUserByURI(final String ipAddressOfKB, final String uri) {
        String select = "select ?hasName ?hasEmail where { <" + uri
                + "> rdf:type icr:User . <" + uri + "> foaf:name ?hasName . <"
                + uri + "> foaf:mbox ?hasEmail }    }";

        String result = executeQuery(ipAddressOfKB, select);

        return XmlSparqlToUser.createUser(result, uri);
    }

}
