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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.cloudsimulator.repository.CharsetRepository;
import org.cloudsimulator.repository.ExtensionFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utility implements Serializable {

    private static final long serialVersionUID = -8722325767040255881L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);

    private Utility() {
        // Not Called
    }

    public static void stringToFile(final String content, final String fileName) {

        FileOutputStream fileOutputStream = null;

        try {

            File file = new File(fileName);

            if (!file.exists() && !file.createNewFile()) {
                throw new IOException();

            }

            fileOutputStream = new FileOutputStream(file);

            byte[] contentInBytes = content.getBytes(Charset
                    .forName(CharsetRepository.UTF8));

            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static void getXMLFromServer(final String xmlRdfStringToGet,
            final String fileNameWithoutExt) throws IOException {
        final InputStream stringInputStream = new ByteArrayInputStream(
                xmlRdfStringToGet.getBytes(Charset
                        .forName(CharsetRepository.UTF8)));
        final int numberOfBytes = stringInputStream.available();
        byte[] bufferBytes = new byte[numberOfBytes];
        stringInputStream.read(bufferBytes);

        stringInputStream.close();
        HttpServletResponse response = (HttpServletResponse) FacesContext
                .getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("text/xml");
        response.setHeader("Content-Disposition", "attachment;filename="
                + fileNameWithoutExt + getTimeStamp()
                + ExtensionFileRepository.XML);
        response.getOutputStream().write(bufferBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public static String fileToString(final String fileName) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        Charset stringCharset = Charset.forName(CharsetRepository.UTF8);
        String decodedString = null;
        
        if (stringCharset != null){
            decodedString = new String(encoded, stringCharset);
        }
        
        return decodedString;
    }

    public static void byteArrayToFile(final byte[] content,
            final String fileName) {

        FileOutputStream fileOutputStream = null;

        try {

            File file = new File(fileName);

            if (!file.exists() && !file.createNewFile()) {
                throw new IOException();

            }

            fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(content);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getTimeStampForWriteDirectory() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static String convertDateToString(final String stringDateFormat,
            final Date date) {
        DateFormat dateFormat = new SimpleDateFormat(stringDateFormat);
        return dateFormat.format(date);
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(
            Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

}
