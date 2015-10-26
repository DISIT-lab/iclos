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

package org.cloudsimulator.xsd;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;

public class Input implements LSInput {

    private String publicId;
    private String systemId;
    private BufferedInputStream inputStream;

    private static final Logger LOGGER = LoggerFactory.getLogger(Input.class);

    public Input(final String publicId, final String sysId,
            final InputStream input) {
        this.publicId = publicId;
        this.systemId = sysId;
        this.inputStream = new BufferedInputStream(input);
    }

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public InputStream getByteStream() {
        return null;
    }

    @Override
    public boolean getCertifiedText() {
        return false;
    }

    @Override
    public Reader getCharacterStream() {
        return null;
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public String getStringData() {
        synchronized (inputStream) {
            try {
                final byte[] input = new byte[inputStream.available()];
                inputStream.read(input);
                return new String(input, "UTF-8");
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public void setBaseURI(final String baseURI) {
        // Not Called
    }

    @Override
    public void setByteStream(final InputStream byteStream) {
        // Not Called
    }

    @Override
    public void setCertifiedText(final boolean certifiedText) {
        // Not Called
    }

    @Override
    public void setCharacterStream(final Reader characterStream) {
        // Not Called
    }

    @Override
    public void setEncoding(final String encoding) {
        // Not Called
    }

    @Override
    public void setStringData(final String stringData) {
        // Not Called
    }

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

}
