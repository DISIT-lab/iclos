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

import org.cloudsimulator.domain.ontology.User;

public class UserDuplicateException extends Exception {

    private static final long serialVersionUID = -2091258089120586956L;
    private final User userFirst;
    private final User userSecond;

    public UserDuplicateException(final User userFirst, final User userSecond) {
        super();
        this.userFirst = userFirst;
        this.userSecond = userSecond;
    }

    public User getUserFirst() {
        return userFirst;
    }

    public User getUserSecond() {
        return userSecond;
    }

    @Override
    public String getMessage() {
        return "The User "
                + this.userFirst.getUri()
                + "<br /> has Name <strong>"
                + this.userFirst.getNameFoaf()
                + "</strong> <br /> has Email <strong>"
                + this.userFirst.getMboxFoaf()
                + "</strong> <br /> There is another User with the same URI but <br />has Name <strong>"
                + this.userSecond.getNameFoaf()
                + "</strong> <br /> has Email <strong>"
                + this.userSecond.getMboxFoaf() + "</strong>";
    }
}
