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
import java.util.Random;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.cloudsimulator.domain.ontology.LocalNetwork;
import org.cloudsimulator.exception.LocalNetworkTooLittleException;

public class LocalNetworkController implements Serializable {

    private static final long serialVersionUID = -5682684891411375170L;
    private LocalNetwork localNetwork;
    private String[] allAddresses;
    private int addressCount;
    private String currentAddress;

    // Constructor----------------------------------------------------------------------------------

    public LocalNetworkController(final LocalNetwork localNetwork) {
        super();
        SubnetUtils subNetUtils = new SubnetUtils(
                localNetwork.getHasIPAddress(),
                localNetwork.getHasSubNetworkMask());
        SubnetInfo subNetInfo = subNetUtils.getInfo();
        this.allAddresses = subNetInfo.getAllAddresses();
        this.addressCount = subNetInfo.getAddressCount();
        this.localNetwork = localNetwork;
    }

    // Method--------------------------------------------------------------------------------------

    public String getAddress() throws LocalNetworkTooLittleException {
        if (this.localNetwork.getAssignedIP().size() < this.addressCount - 1) {
            Random random = new Random();
            do {
                this.currentAddress = this.allAddresses[random
                        .nextInt(this.allAddresses.length)];
            } while (this.localNetwork.getAssignedIP().contains(currentAddress));

            this.localNetwork.getAssignedIP().add(this.currentAddress);

            return this.currentAddress;
        } else {
            throw new LocalNetworkTooLittleException(this.localNetwork);
        }
    }

    // GettersAndSetters-------------------------------------------------------------------------

    public LocalNetwork getLocalNetwork() {
        return localNetwork;
    }

}
