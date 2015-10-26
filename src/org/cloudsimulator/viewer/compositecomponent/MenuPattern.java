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

package org.cloudsimulator.viewer.compositecomponent;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.cloudsimulator.controller.CollectorController;
import org.cloudsimulator.placement.SimulatedPattern;
import org.cloudsimulator.placement.SimulatedPatternCollection;
import org.cloudsimulator.thread.DataCollector;
import org.cloudsimulator.utility.filter.DirectoryFilter;
import org.cloudsimulator.utility.filter.PngFilter;

@Named("menuPattern")
@ConversationScoped
public class MenuPattern implements Serializable {

    private static final long serialVersionUID = 421588465364662131L;
    private String baseDirectory;
    private List<String> virtualMachineCollectedList;
    private List<List<String>> periodList;
    private List<List<List<String>>> timeStampList;
    private List<List<List<List<String>>>> nameImagePatternList;
    private SimulatedPatternCollection simulatedPatternCollection = new SimulatedPatternCollection();

    public SimulatedPatternCollection getSimulatedPatternCollection() {
        return simulatedPatternCollection;
    }

    public void setSimulatedPatternCollection(
            SimulatedPatternCollection simulatedPatternCollection) {
        this.simulatedPatternCollection = simulatedPatternCollection;
    }

    @PostConstruct
    public void init() throws IOException {
        
        this.baseDirectory = CollectorController.BASE_DIRECTORY;
        File collectionDirectory = new File(this.baseDirectory
                + DataCollector.DIRECTORY);
        if (collectionDirectory.exists() && collectionDirectory.isDirectory()) {
            File[] virtualMachineCollectedArray = collectionDirectory
                    .listFiles(new DirectoryFilter());
            this.virtualMachineCollectedList = new ArrayList<String>();
            this.periodList = new ArrayList<List<String>>();
            this.timeStampList = new ArrayList<List<List<String>>>();
            this.nameImagePatternList = new ArrayList<List<List<List<String>>>>();

            for (int i = 0; i < virtualMachineCollectedArray.length; i++) {
                File virtualMachineDirectory = new File(
                        virtualMachineCollectedArray[i].getAbsolutePath());
                File[] periodArray = virtualMachineDirectory
                        .listFiles(new DirectoryFilter());
                this.virtualMachineCollectedList
                        .add(virtualMachineCollectedArray[i].getName()
                                .replace("@", "AT").replace(".", "").replace(",", "").replace(" ", "").replace("(", "").replace(")",""));
                this.periodList.add(new ArrayList<String>());
                this.timeStampList.add(new ArrayList<List<String>>());
                this.nameImagePatternList
                        .add(new ArrayList<List<List<String>>>());

                for (int j = 0; j < periodArray.length; j++) {
                    File periodDirectory = new File(
                            periodArray[j].getAbsolutePath());
                    File[] timeStampArray = periodDirectory
                            .listFiles(new DirectoryFilter());
                    this.periodList.get(i).add(periodArray[j].getName());
                    this.timeStampList.get(i).add(new ArrayList<String>());
                    this.nameImagePatternList.get(i).add(
                            new ArrayList<List<String>>());

                    for (int k = 0; k < timeStampArray.length; k++) {
                        File timeStampDirectory = new File(
                                timeStampArray[k].getAbsolutePath());
                        File[] patternImageArray = timeStampDirectory
                                .listFiles(new PngFilter());
                        this.timeStampList.get(i).get(j)
                                .add(timeStampArray[k].getName());
                        this.nameImagePatternList.get(i).get(j)
                                .add(new ArrayList<String>());

                        for (int l = 0; l < patternImageArray.length; l++) {
                            this.nameImagePatternList
                                    .get(i)
                                    .get(j)
                                    .get(k)
                                    .add("../"
                                            + patternImageArray[l]
                                                    .getAbsolutePath().replace(
                                                            this.baseDirectory,
                                                            ""));
                        }
                        if(periodArray[j].getName().endsWith("simulated")){
                            int days = Integer.parseInt(periodArray[j].getName().replace("day_simulated", ""));
                            this.simulatedPatternCollection.addSimulatedPattern(
                                    new SimulatedPattern(virtualMachineCollectedArray[i].getName(),
                                         days, timeStampArray[k].getName()));
                        }

                    }
                }
            }
        }

    }

    public List<String> getVirtualMachineCollectedList() {
        return this.virtualMachineCollectedList;
    }

    public void setVirtualMachineCollectedList(
            List<String> virtualMachineCollectedList) {
        this.virtualMachineCollectedList = virtualMachineCollectedList;
    }

    public List<List<String>> getPeriodList() {
        return this.periodList;
    }

    public void setPeriodList(List<List<String>> periodList) {
        this.periodList = periodList;
    }

    public List<List<List<String>>> getTimeStampList() {
        return this.timeStampList;
    }

    public void setTimeStampList(List<List<List<String>>> timeStampList) {
        this.timeStampList = timeStampList;
    }

    public List<List<List<List<String>>>> getNameImagePatternList() {
        return this.nameImagePatternList;
    }

    public void setNameImagePatternList(
            List<List<List<List<String>>>> nameImagePatternList) {
        this.nameImagePatternList = nameImagePatternList;
    }

}
