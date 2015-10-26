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

package org.cloudsimulator.injection;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationInjectionManager {
    public static final String INVALID_KEY = "Invalid key '{0}'";
    public static final String MANDATORY_PARAM_MISSING = "No definition found for a mandatory configuration parameter : '{0}'";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigurationInjectionManager.class);
    public static final String BUNDLE_FILE_NAME = "configuration";
    private final ResourceBundle bundle = ResourceBundle
            .getBundle(BUNDLE_FILE_NAME);

    @Produces
    @InjectedConfiguration
    public String injectConfiguration(InjectionPoint ip) {
        InjectedConfiguration param = ip.getAnnotated().getAnnotation(
                InjectedConfiguration.class);
        if (param.key() == null || param.key().length() == 0) {
            return param.defaultValue();
        }
        String value;
        try {
            value = bundle.getString(param.key());
            if (value == null || value.trim().length() == 0) {
                if (param.mandatory())
                    throw new IllegalStateException(MessageFormat.format(
                            MANDATORY_PARAM_MISSING,
                            new Object[] { param.key() }));
                else
                    return param.defaultValue();
            }
            return value;
        } catch (MissingResourceException e) {
            LOGGER.error(e.getMessage(), e);
            if (param.mandatory())
                throw new IllegalStateException(MessageFormat.format(
                        MANDATORY_PARAM_MISSING, new Object[] { param.key() }));
            return MessageFormat.format(INVALID_KEY,
                    new Object[] { param.key() });
        }
    }
}
