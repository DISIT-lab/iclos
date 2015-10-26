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

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.cloudsimulator.injection.InjectedConfiguration;
import org.cloudsimulator.thread.DataCollector;

@Named("collectorController")
@ApplicationScoped
public class CollectorController implements Serializable {

    private static final long serialVersionUID = -8670700791817521246L;

    @Inject
    @InjectedConfiguration(key = "nagios.collectData.addressIP", defaultValue = "localhost")
    private String ipNagiosCollectData;
    @Inject
    @InjectedConfiguration(key = "nagios.collectData.userName", defaultValue = "userName")
    private String userNameNagiosCollectData;
    @Inject
    @InjectedConfiguration(key = "nagios.collectData.passWord", defaultValue = "passWord")
    private String passWordNagiosCollectData;
    private DataCollector dataCollector;
    private static final String THREAD_NAME = "dataCollector";
    public static final String BASE_DIRECTORY = System.getProperty("user.home") + "/IcaroCloudSimulator/";

    // Constructor----------------------------------------------------------------------------------

    public CollectorController() {
        init();
    }

    // Method--------------------------------------------------------------------------------------
    @PostConstruct
    public void init() {
        this.dataCollector = new DataCollector(BASE_DIRECTORY, this.ipNagiosCollectData,
                this.userNameNagiosCollectData, this.passWordNagiosCollectData);
    }

    public void startCollect() {

        if (isOldThreadAlive()) {
            return;
        } else {
            Thread threadCollector;
            threadCollector = new Thread(dataCollector, THREAD_NAME);
            threadCollector.start();
            ((ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext()).setAttribute(
                    THREAD_NAME, threadCollector);
        }
    }

    public void stopCollect() {
        if (isOldThreadAlive()) {
            this.dataCollector.stopDataCollector();
            ((ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext())
                    .removeAttribute(THREAD_NAME);
        }
    }

    public boolean isOldThreadAlive() {
        Thread oldThread = (Thread) ((ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext())
                .getAttribute(THREAD_NAME);
        if (oldThread != null && oldThread.isAlive()) {
            return true;
        }

        return false;
    }

    public DataCollector getDataCollector() {
        return this.dataCollector;
    }

    public void setDataCollector(DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

}
