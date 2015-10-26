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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cloudsimulator.domain.ontology.ServiceMetric;
import org.cloudsimulator.persistence.HostMachineDAO;
import org.cloudsimulator.persistence.ServiceMetricsDAO;
import org.cloudsimulator.persistence.VirtualMachineDAO;
import org.cloudsimulator.utility.Utility;
import org.cloudsimulator.viewer.compositecomponent.AnalyzeMetricsMenuTree;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

@Named("analyzeMetricsController")
@ConversationScoped
public class AnalyzeMetricsController implements Serializable {

    private static final long serialVersionUID = 3991066091205261993L;
    private static final String DATEFORMATFORCALENDAR = "yyyy-MM-dd HH:mm:ss";
    private static final String DATEFORMATFORKB = "yyyy-MM-dd'T'HH:mm:ss";
    private Date dateFrom;
    private Date dateTo;

    private List<Boolean> filled;
    private List<Boolean> stacked;
    private List<Boolean> rescaled;

    private Map<String, List<List<ServiceMetric>>> serviceMetricMap;
    private List<CartesianChartModel> cartesianChartModelList;
    private List<CartesianChartModel> cartesianCharModelBackupList;
    @Inject
    private AnalyzeMetricsMenuTree analyzeMetricMenuTree;
    @Inject
    private DataCenterController dataCenterController;
    private String ipOfKB;

    public AnalyzeMetricsController() {
        resetCharts();
    }

    @PostConstruct
    public void init() {
        this.ipOfKB = this.dataCenterController.getIpOfKB();
        this.dateTo = new Date();
        this.dateFrom = new Date(this.dateTo.getTime() - 604800000L);

    }

    public void resetCharts() {
        this.cartesianChartModelList = new ArrayList<CartesianChartModel>();
        this.cartesianCharModelBackupList = new ArrayList<CartesianChartModel>();
        this.filled = new ArrayList<Boolean>();
        this.stacked = new ArrayList<Boolean>();
        this.rescaled = new ArrayList<Boolean>();
        this.stacked.add(false);
        this.filled.add(false);
        this.rescaled.add(false);
    }

    public void createChartWithMetrics() {
        resetCharts();
        ServiceMetricsDAO serviceMetricDAO = new ServiceMetricsDAO();
        List<String> uriEntityList = new ArrayList<String>();
        for (TreeNode selectedNode : analyzeMetricMenuTree.getSelectedNodes()) {
            uriEntityList.add(selectedNode.getData().toString());
        }

        this.serviceMetricMap = serviceMetricDAO
                .getServiceMetricMapByEntityList(this.ipOfKB, uriEntityList,
                        Utility.convertDateToString(DATEFORMATFORKB, dateFrom),
                        Utility.convertDateToString(DATEFORMATFORKB, dateTo));

        List<String> keysList = Utility.asSortedList(serviceMetricMap.keySet());

        for (String key : keysList) {
            List<List<ServiceMetric>> serviceMetricListList = serviceMetricMap
                    .get(key);
            if (!serviceMetricListList.isEmpty()) {
                this.cartesianChartModelList
                        .add(createModelFromList(serviceMetricListList));
                if (serviceMetricListList.get(0).get(0).getHasMetricUnit() != null
                        && !serviceMetricListList
                                .get(0)
                                .get(0)
                                .getHasMetricName()
                                .contains(
                                        serviceMetricListList.get(0).get(0)
                                                .getHasMetricUnit())) {
                    this.cartesianChartModelList.get(
                            this.cartesianChartModelList.size() - 1).setTitle(
                            serviceMetricListList.get(0).get(0)
                                    .getHasMetricName()
                                    + " ("
                                    + serviceMetricListList.get(0).get(0)
                                            .getHasMetricUnit() + ")");
                } else {
                    this.cartesianChartModelList.get(
                            this.cartesianChartModelList.size() - 1).setTitle(
                            serviceMetricListList.get(0).get(0)
                                    .getHasMetricName());
                }
                this.stacked.add(false);
                this.filled.add(false);
                this.rescaled.add(false);
            }
        }

        this.cartesianCharModelBackupList.addAll(this.cartesianChartModelList);

    }

    private CartesianChartModel createModelFromList(
            final List<List<ServiceMetric>> serviceMetricListList) {
        CartesianChartModel cartesianChart = new CartesianChartModel();
        for (List<ServiceMetric> serviceMetricList : serviceMetricListList) {
            if (!serviceMetricList.isEmpty()) {
                ChartSeries chartSeries = new ChartSeries();
                for (ServiceMetric serviceMetric : serviceMetricList) {
                    if (serviceMetric.getDependsOn().contains("Host")) {
                        HostMachineDAO hostMachineDAO = new HostMachineDAO();
                        chartSeries.setLabel(hostMachineDAO
                                .getHostMachineByURI(this.ipOfKB,
                                        serviceMetric.getDependsOn())
                                .getHasName());
                    }
                    if (serviceMetric.getDependsOn().contains("Virtual")) {
                        VirtualMachineDAO virtualMachineDAO = new VirtualMachineDAO();
                        chartSeries.setLabel(virtualMachineDAO
                                .getVirtualMachineByURI(this.ipOfKB,
                                        serviceMetric.getDependsOn())
                                .getHasName());
                    }
                    chartSeries.set(serviceMetric.getAtTime(),
                            serviceMetric.getHasMetricValue());
                }

                cartesianChart.addSeries(chartSeries);
            }

        }

        return cartesianChart;
    }

    public void restoreScaleXAxis(final Integer index) {
        this.cartesianChartModelList.set(index,
                this.cartesianCharModelBackupList.get(index));
        this.rescaled.set(index, false);
    }

    /*
     * In the following methods, for rescaling the X-Axis of the charts I
     * exploit the date format used in the KB yyyy-mm-ddThh:mm:ss. So, if you
     * want rescaling the axis in daily axis, the method inserts in a map the
     * sum of values that have the same first ten characters in date: yyyy-mm-dd
     * and then it calculates the average on that sum. So, for others the same
     * tecnique is used.
     */

    public void changeScaleXAxisMonthly(final Integer index) {
        changeScaleXAxis(index, 7);
    }

    public void changeScaleXAxisDaily(final Integer index) {
        changeScaleXAxis(index, 10);

    }

    public void changeScaleXAxisHourly(final Integer index) {
        changeScaleXAxis(index, 13);
    }

    private void changeScaleXAxis(final Integer index,
            final Integer characterOfSubString) {
        CartesianChartModel oldChartModel = this.cartesianCharModelBackupList
                .get(index);
        CartesianChartModel newChartModel = new CartesianChartModel();

        for (ChartSeries oldChartSerie : oldChartModel.getSeries()) {
            Map<Object, Number> oldMapSerie = oldChartSerie.getData();
            Map<String, Integer> newMapSerieCounter = new HashMap<String, Integer>();
            Map<String, Number> newMapSerie = new HashMap<String, Number>();
            for (Entry<Object, Number> entrySet : oldMapSerie.entrySet()) {
                if (newMapSerie.get(entrySet.getKey().toString()
                        .substring(0, characterOfSubString)) == null) {
                    newMapSerie.put(
                            entrySet.getKey().toString()
                                    .substring(0, characterOfSubString), 0);
                }
                newMapSerie.put(
                        entrySet.getKey().toString()
                                .substring(0, characterOfSubString),
                        (Number) (newMapSerie.get(
                                entrySet.getKey().toString()
                                        .substring(0, characterOfSubString))
                                .floatValue() + entrySet.getValue()
                                .floatValue()));
                if (newMapSerieCounter.get(entrySet.getKey().toString()
                        .substring(0, characterOfSubString)) == null) {
                    newMapSerieCounter.put(entrySet.getKey().toString()
                            .substring(0, characterOfSubString), 0);
                }
                newMapSerieCounter.put(
                        entrySet.getKey().toString()
                                .substring(0, characterOfSubString),
                        newMapSerieCounter.get(entrySet.getKey().toString()
                                .substring(0, characterOfSubString)) + 1);
            }

            ChartSeries newChartSerie = new ChartSeries();
            List<String> keysList = Utility.asSortedList(newMapSerie.keySet());

            for (String key : keysList) {
                newChartSerie
                        .set(key,
                                (Number) (newMapSerie.get(key).floatValue() / newMapSerieCounter
                                        .get(key).intValue()));
            }

            newChartSerie.setLabel(oldChartSerie.getLabel());
            newChartModel.addSeries(newChartSerie);

        }

        this.cartesianChartModelList.set(index, newChartModel);
        this.cartesianChartModelList.get(index).setTitle(
                oldChartModel.getTitle() + " - X-Axis Rescaled");
        this.rescaled.set(index, true);
    }

    public String getDATEFORMATFORCALENDAR() {
        return DATEFORMATFORCALENDAR;
    }

    public String getDATEFORMATFORKB() {
        return DATEFORMATFORKB;
    }

    public Date getDateFrom() {
        return (Date) this.dateFrom.clone();

    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = new Date(dateFrom.getTime());
    }

    public Date getDateTo() {
        return (Date) this.dateTo.clone();
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = new Date(dateTo.getTime());
    }

    public List<CartesianChartModel> getCartesianChartModelList() {
        return this.cartesianChartModelList;
    }

    public void setCartesianChartModelList(
            List<CartesianChartModel> cartesianChartModelList) {
        this.cartesianChartModelList = cartesianChartModelList;
    }

    public List<Boolean> getFilled() {
        return this.filled;
    }

    public void setFilled(List<Boolean> filled) {
        this.filled = filled;
    }

    public List<Boolean> getStacked() {
        return this.stacked;
    }

    public void setStacked(List<Boolean> stacked) {
        this.stacked = stacked;
    }

    public List<Boolean> getRescaled() {
        return this.rescaled;
    }

    public void setRescaled(List<Boolean> rescaled) {
        this.rescaled = rescaled;
    }

}
