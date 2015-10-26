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
import java.io.Serializable;

import javax.inject.Named;

import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.RestAPIRepository;
import org.cloudsimulator.utility.RestAPI;

@Named("kbDAO")
public final class KBDAO implements Serializable {

    private static final long serialVersionUID = -8722325767040255881L;
    private static final String USERNAME_WRITE = "kb-write";
    private static final String USERNAME_READ = "kb-read";
    private static final String PASSWORD_R_W = "icaro";

    private KBDAO() {
        // Not called
    }

    public static ResponseMessageString sendXMLToKB(final String requestMethod,
            final String ipAddressOfKB, final String context,
            final String identifier, final String xmlRdfStringToSend)
            throws IOException {

        ResponseMessageString responseMessageString = null;
        if ("businessConfiguration".equals(context)) {
            responseMessageString = RestAPI
                    .sendString(requestMethod, RestAPI.PROTOCOL + ipAddressOfKB
                            + RestAPIRepository.KB_BUSINESS_CONFIGURATION
                            + identifier, USERNAME_WRITE, PASSWORD_R_W,
                            xmlRdfStringToSend, RestAPI.APP_XML,
                            CharsetRepository.UTF8);
        }
        if ("dataCenter".equals(context)) {
            responseMessageString = RestAPI.sendString(requestMethod,
                    RestAPI.PROTOCOL + ipAddressOfKB
                            + RestAPIRepository.KB_DATA_CENTER + identifier,
                    USERNAME_WRITE, PASSWORD_R_W, xmlRdfStringToSend,
                    RestAPI.APP_XML, CharsetRepository.UTF8);
        }
        if ("serviceMetric".equals(context)) {
            responseMessageString = RestAPI.sendString(requestMethod,
                    RestAPI.PROTOCOL + ipAddressOfKB
                            + RestAPIRepository.KB_SERVICE_METRIC + identifier,
                    USERNAME_WRITE, PASSWORD_R_W, xmlRdfStringToSend,
                    RestAPI.APP_XML, CharsetRepository.UTF8);
        }

        return responseMessageString;
    }

    public static ResponseMessageString queryKB(final String ipAddressOfKB,
            final String query) throws IOException {
        return RestAPI.receiveString(RestAPI.PROTOCOL + ipAddressOfKB
                + RestAPIRepository.KB_QUERY + query, USERNAME_READ,
                PASSWORD_R_W, RestAPI.APP_SPARQL, CharsetRepository.UTF8);
    }

}
