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

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.exception.EmptyQueryResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericDAO {

    private static final String PREFIX_ICR = "prefix icr:<http://www.cloudicaro.it/cloud_ontology/core#>";
    private static final String PREFIX_RDF = "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    private static final String PREFIX_XSD = "prefix xsd:<http://www.w3.org/2001/XMLSchema#>";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(GenericDAO.class);

    protected String executeQuery(final String ipAddressOfKB,
            final String select) {
        String query = PREFIX_ICR + " " + PREFIX_RDF + " " + PREFIX_XSD + " "
                + select;
        String result = null;
        try {
            ResponseMessageString responseMessage = KBDAO.queryKB(
                    ipAddressOfKB, query);
            result = responseMessage.getResponseBody();
        } catch (ClientProtocolException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (result != null) {
            return result;
        } else {
            throw new EmptyQueryResultException();
        }
    }

}
