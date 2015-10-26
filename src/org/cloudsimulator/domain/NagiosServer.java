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

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.injection.InjectedConfiguration;

@Named("nagiosServer")
public class NagiosServer implements Serializable {

    private static final long serialVersionUID = -1603019874298204296L;

    @Inject
    @InjectedConfiguration(key = "nagios.simulateData.addressIP", defaultValue = "localhost")
    private String ipAddress;
    @Inject
    @InjectedConfiguration(key = "nagios.simulateData.port", defaultValue = "22")
    private String port;
    @Inject
    @InjectedConfiguration(key = "nagios.simulateData.userName", defaultValue = "userName")
    private String user;
    @Inject
    @InjectedConfiguration(key = "nagios.simulateData.passWord", defaultValue = "passWord")
    private String password;
    @Inject
    @InjectedConfiguration(key = "nagios.simulateData.directory", defaultValue = "/home")
    private String directoryInside;

    // Constructor----------------------------------------------------------------------------------

    public NagiosServer() {
        super();
    }

    // GettersAndSetters----------------------------------------------------------------------------

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getIntPort() {
        return Integer.parseInt(this.port);
    }

    public String getDirectoryInside() {
        if (!directoryInside.endsWith("/")) {
            this.directoryInside = directoryInside + "/";
        }
        return this.directoryInside;
    }

    public void setDirectoryInside(String directoryInside) {
        if (directoryInside.endsWith("/")) {
            this.directoryInside = directoryInside;
        } else {
            this.directoryInside = directoryInside + "/";
        }
    }

}
