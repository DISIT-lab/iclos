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

package org.cloudsimulator.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("configurationController")
@ConversationScoped
public class ConfigurationController implements Serializable {

    private static final long serialVersionUID = 3309256214365098864L;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigurationController.class);
    private Map<String, String> configurationPropertiesMap;
    private static final String CONFIGURATION_PROPERTIES_NAME = "configuration";
    private static final String CONFIGURATION_PROPERTIES_IT_RELATIVE_PATH = "WEB-INF/classes/configuration_it_IT.properties";
    private static final String CONFIGURATION_PROPERTIES_EN_RELATIVE_PATH = "WEB-INF/classes/configuration_en_US.properties";
    private transient ResourceBundle resourceBundle;
    private List<String> keyList;

    private boolean changeIPConfiguration;

    public ConfigurationController() {
        super();
        this.configurationPropertiesMap = new TreeMap<String, String>();
        loadConfigurationPropertiesFromFile();
        createKeyListFromMap();
        hideChangeIPConfigurationForm();
    }

    public void saveConfigurationPropertiesToFile() {
        Properties configurationProperties = new Properties();
        for (String key : this.configurationPropertiesMap.keySet()) {
            configurationProperties.put(key,
                    this.configurationPropertiesMap.get(key));
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        if (request != null) {
            try {
                FileOutputStream fileOutputStreamIT = new FileOutputStream(
                        request.getServletContext().getRealPath("/")
                                + CONFIGURATION_PROPERTIES_IT_RELATIVE_PATH);
                configurationProperties.store(fileOutputStreamIT, "");
                fileOutputStreamIT.close();
                FileOutputStream fileOutputStreamEN = new FileOutputStream(
                        request.getServletContext().getRealPath("/")
                                + CONFIGURATION_PROPERTIES_EN_RELATIVE_PATH);
                configurationProperties.store(fileOutputStreamEN, "");
                fileOutputStreamEN.close();
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            hideChangeIPConfigurationForm();
        }

    }

    public void loadConfigurationPropertiesFromFile() {
        this.resourceBundle = ResourceBundle
                .getBundle(CONFIGURATION_PROPERTIES_NAME);
        Set<String> keySet = resourceBundle.keySet();
        for (String key : keySet) {
            this.configurationPropertiesMap.put(key,
                    resourceBundle.getString(key));
        }
    }

    public void createKeyListFromMap() {
        this.keyList = new ArrayList<String>();
        for (String key : this.configurationPropertiesMap.keySet()) {
            this.keyList.add(key);
        }
    }

    public void showChangeIPConfigurationForm() {
        this.changeIPConfiguration = true;
    }

    public void hideChangeIPConfigurationForm() {
        this.changeIPConfiguration = false;
    }

    public Map<String, String> getConfigurationPropertiesMap() {
        return configurationPropertiesMap;
    }

    public void setConfigurationPropertiesMap(
            Map<String, String> configurationPropertiesMap) {
        this.configurationPropertiesMap = configurationPropertiesMap;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public boolean isChangeIPConfiguration() {
        return changeIPConfiguration;
    }

}
