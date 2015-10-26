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

package org.cloudsimulator.utility;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.cloudsimulator.domain.ResponseMessageByteArray;
import org.cloudsimulator.domain.ResponseMessageString;
import org.cloudsimulator.repository.CharsetRepository;

@Named("restAPI")
public final class RestAPI implements Serializable {

    private static final long serialVersionUID = -8722325767040255881L;

    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENTTYPE = "Content-Type";

    public static final String PROTOCOL = "http://";
    public static final String ACCEPT = "Accept";
    public static final String APP_XML = "application/xml";
    public static final String APP_SPARQL = "application/sparql-results+xml";
    public static final String IMAGE_PNG = "image/png";

    private RestAPI() {
        // Not called
    }

    public static ResponseMessageString sendString(final String requestMethod,
            final String restAPIURI, final String username,
            final String password, final String stringToSend,
            final String typeOfString, final String charset) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        ResponseMessageString responseMessageString = null;
        if ("PUT".equals(requestMethod)) {
            httpResponse = putRequestBasicAuth(httpClient,
                    escapeURI(restAPIURI), username, password, typeOfString,
                    new ByteArrayEntity(stringToSend.getBytes(charset)));

        }

        if ("POST".equals(requestMethod)) {
            httpResponse = postRequestBasicAuth(httpClient,
                    escapeURI(restAPIURI), username, password, typeOfString,
                    new ByteArrayEntity(stringToSend.getBytes(charset)));
        }

        if (httpResponse != null) {
            if (httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    responseMessageString = new ResponseMessageString(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            IOUtils.toString(httpResponse.getEntity()
                                    .getContent(), charset));
                } else {
                    responseMessageString = new ResponseMessageString(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            null);
                }

            } else {
                if (httpResponse.getEntity() != null) {
                    responseMessageString = new ResponseMessageString(null,
                            null, IOUtils.toString(httpResponse.getEntity()
                                    .getContent(), charset));
                }
            }

            httpResponse.close();
        }

        httpClient.close();
        return responseMessageString;

    }

    public static ResponseMessageString receiveString(final String restAPIURI,
            final String username, final String password,
            final String typeOfString, final String charset) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        ResponseMessageString responseMessageString = null;

        httpResponse = getRequestBasicAuth(httpClient, escapeURI(restAPIURI),
                username, password, typeOfString);

        if (httpResponse != null) {
            if (httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    responseMessageString = new ResponseMessageString(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            IOUtils.toString(httpResponse.getEntity()
                                    .getContent(), charset));
                } else {
                    responseMessageString = new ResponseMessageString(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            null);
                }

            } else {
                if (httpResponse.getEntity() != null) {
                    responseMessageString = new ResponseMessageString(null,
                            null, IOUtils.toString(httpResponse.getEntity()
                                    .getContent(), charset));
                }
            }

            httpResponse.close();
        }

        httpClient.close();
        return responseMessageString;

    }

    public static ResponseMessageByteArray receiveByteArray(
            final String restAPIURI, final String username,
            final String password, final String typeOfByteArray)
            throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        ResponseMessageByteArray responseMessageByteArray = null;

        httpResponse = getRequestBasicAuth(httpClient, escapeURI(restAPIURI),
                username, password, typeOfByteArray);

        if (httpResponse != null) {
            if (httpResponse.getStatusLine() != null) {
                if (httpResponse.getEntity() != null) {
                    responseMessageByteArray = new ResponseMessageByteArray(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            IOUtils.toByteArray(httpResponse.getEntity()
                                    .getContent()));
                } else {
                    responseMessageByteArray = new ResponseMessageByteArray(
                            httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.getStatusLine().getReasonPhrase(),
                            null);
                }

            } else {
                if (httpResponse.getEntity() != null) {
                    responseMessageByteArray = new ResponseMessageByteArray(
                            null, null, IOUtils.toByteArray(httpResponse
                                    .getEntity().getContent()));
                }
            }

            httpResponse.close();
        }

        httpClient.close();
        return responseMessageByteArray;

    }

    private static String getBasicAuth(final String username,
            final String password) {
        String authString = username + ":" + password;
        Base64 base64 = new Base64();
        String authStringEnc = new String(base64.encode(authString
                .getBytes(Charset.forName(CharsetRepository.UTF8))),
                Charset.forName(CharsetRepository.UTF8));
        return "Basic "
                + authStringEnc.substring(0, authStringEnc.length() - 2);
    }

    private static CloseableHttpResponse getRequestBasicAuth(
            final CloseableHttpClient httpClient, final String uri,
            final String username, final String password,
            final String contentType) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader(ACCEPT, contentType);
        httpGet.addHeader(AUTHORIZATION, getBasicAuth(username, password));
        return httpClient.execute(httpGet);
    }

    private static CloseableHttpResponse putRequestBasicAuth(
            final CloseableHttpClient httpClient, final String uri,
            final String username, final String password,
            final String contentType, final HttpEntity entityToSend) throws IOException {
        HttpPut httpPut = new HttpPut(uri);
        httpPut.addHeader(CONTENTTYPE, contentType);
        httpPut.addHeader(AUTHORIZATION, getBasicAuth(username, password));
        httpPut.setEntity(entityToSend);
        return httpClient.execute(httpPut);
    }

    private static CloseableHttpResponse postRequestBasicAuth(
            final CloseableHttpClient httpClient, final String uri,
            final String username, final String password,
            final String contentType, final HttpEntity entityToSend) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader(CONTENTTYPE, contentType);
        httpPost.addHeader(AUTHORIZATION, getBasicAuth(username, password));
        httpPost.setEntity(entityToSend);
        return httpClient.execute(httpPost);
    }

    private static String escapeURI(final String query) {
        return query.replace("%", "%25").replace(" ", "%20")
                .replace("\"", "%22").replace("#", "%23").replace("&", "%26")
                .replace("<", "%3C").replace(">", "%3E").replace("^", "%5E")
                .replace("{", "%7B").replace("}", "%7D");
    }

    public String getProtocol() {
        return PROTOCOL;
    }

}
