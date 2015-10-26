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

package org.cloudsimulator.exception;

import org.cloudsimulator.domain.ontology.LocalNetwork;

public class LocalNetworkDuplicateException extends Exception {

    private static final long serialVersionUID = -2091258089120586956L;
    private final LocalNetwork localNetworkFirst;
    private final LocalNetwork localNetworkSecond;

    public LocalNetworkDuplicateException(final LocalNetwork localNetworkFirst,
            final LocalNetwork localNetworkSecond) {
        super();
        this.localNetworkFirst = localNetworkFirst;
        this.localNetworkSecond = localNetworkSecond;
    }

    public LocalNetwork getLocalNetworkFirst() {
        return localNetworkFirst;
    }

    public LocalNetwork getLocalNetworkSecond() {
        return localNetworkSecond;
    }

    @Override
    public String getMessage() {
        return "The Local Network "
                + this.localNetworkFirst.getUri()
                + ": <br /> has NetworkAddress <strong>"
                + this.localNetworkFirst.getHasIPAddress()
                + "</strong> <br /> has SubNetMask <strong>"
                + this.localNetworkFirst.getHasSubNetworkMask()
                + "</strong> <br /> There is another Local Network with the same URI but: <br /> has NetworkAddress <strong>"
                + this.localNetworkSecond.getHasIPAddress()
                + "</strong> <br /> has SubNetMask <strong>"
                + this.localNetworkSecond.getHasSubNetworkMask() + "</strong>";
    }

}
