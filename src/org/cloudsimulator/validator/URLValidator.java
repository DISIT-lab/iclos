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

package org.cloudsimulator.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;

@FacesValidator("URLValidator")
public class URLValidator implements Validator, Serializable {

    private static final long serialVersionUID = 7314532253959105361L;

    @Override
    public void validate(final FacesContext facesContext,
            final UIComponent uiComponent, final Object value) {
        String ipAddress;
        int port = 0;
        if (((String) value).contains(":")) {
            ipAddress = ((String) value).substring(0,
                    ((String) value).indexOf(':'));
            port = Integer.parseInt(((String) value).substring(((String) value)
                    .indexOf(':') + 1));
            if (port < 0 || port > 65535) {
                FacesMessage facesMessage = new FacesMessage();
                String message = "The port number is invalid!";
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesMessage.setSummary(message);
                facesMessage.setDetail(message);
                throw new ValidatorException(facesMessage);
            }
        } else {
            ipAddress = ((String) value);
        }
        if (ipAddress
                .matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")) {
            if (!InetAddressValidator.getInstance().isValidInet4Address(
                    ipAddress)) {
                FacesMessage facesMessage = new FacesMessage();
                String message = "The string inserted is not a valid IP Address!";
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesMessage.setSummary(message);
                facesMessage.setDetail(message);
                throw new ValidatorException(facesMessage);
            }
        } else {

            if (!UrlValidator.getInstance().isValid("http://" + ipAddress)
                    && !"localhost".equals(ipAddress)) {
                FacesMessage facesMessage = new FacesMessage();
                String message = "The string inserted is not a valid URL!";
                facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                facesMessage.setSummary(message);
                facesMessage.setDetail(message);
                throw new ValidatorException(facesMessage);
            }
        }

    }

}
