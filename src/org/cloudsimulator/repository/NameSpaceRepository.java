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

package org.cloudsimulator.repository;

import org.jdom2.Namespace;

public final class NameSpaceRepository {

    public static final Namespace APP = Namespace.getNamespace("app",
            "http://www.cloudicaro.it/cloud_ontology/applications#");
    public static final Namespace RDF = Namespace.getNamespace("rdf",
            "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    public static final Namespace ICR = Namespace.getNamespace("icr",
            "http://www.cloudicaro.it/cloud_ontology/core#");
    public static final Namespace FOAF = Namespace.getNamespace("foaf",
            "http://xmlns.com/foaf/0.1/");
    public static final Namespace XSI = Namespace.getNamespace("xsi",
            "http://www.w3.org/2001/XMLSchema-instance");
    public static final Namespace OWL = Namespace.getNamespace("owl",
            "http://www.w3.org/2002/07/owl#");

    private NameSpaceRepository() {
        // Not Called
    }

}
