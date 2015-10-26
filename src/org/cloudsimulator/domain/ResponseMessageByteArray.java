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

package org.cloudsimulator.domain;

public class ResponseMessageByteArray {

    private Integer responseCode;
    private String reason;
    private byte[] responseBody;

    // Constructor----------------------------------------------------------------------------------

    public ResponseMessageByteArray(final Integer responseCode,
            final String reason, final byte[] responseBody) {
        super();
        this.responseCode = responseCode;
        this.reason = reason;
        if (responseBody != null) {
            this.responseBody = responseBody.clone();
        }
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public Integer getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(final Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public byte[] getResponseBody() {
        return this.responseBody.clone();
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody.clone();
    }

}
