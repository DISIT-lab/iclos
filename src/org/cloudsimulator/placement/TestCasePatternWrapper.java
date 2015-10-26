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

package org.cloudsimulator.placement;

import java.io.Serializable;
import java.util.List;

public class TestCasePatternWrapper implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 307522252060642091L;
    private String path;
    private String testName;
    private String type;
    private List<String> serverFilePath;
    
    public TestCasePatternWrapper(String path, String testName,String type) {
        this.path = path;
        this.testName = testName;
        this.type = type;
    }
    
    public String getPath() {
        return path;
    }
    public String getTestName() {
        return testName;
    }
    public String getType() {
        return type;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<String> getServerFilePath() {
        return serverFilePath;
    }

    public void setServerFilePath(List<String> serverFilePath) {
        this.serverFilePath = serverFilePath;
    }
    
    

}
