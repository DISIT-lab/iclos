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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.cloudsimulator.viewer.compositecomponent.DropDownItem;
import org.cloudsimulator.viewer.compositecomponent.DropDownMenu;
import org.cloudsimulator.viewer.compositecomponent.InputText;
import org.cloudsimulator.viewer.compositecomponent.InputTextHtml5;
import org.cloudsimulator.viewer.compositecomponent.InputTextSelectOneMenu;

@Named("staticAttributesRepository")
public class StaticAttributesRepository {

    public List<InputText> getCreatorForSimulatorListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "nameFoaf", "Insert name of user")));
    }

    public List<InputText> getCreatorListInputEmail() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "email", "mboxFoaf", "Insert email of the user")));
    }

    public List<InputText> getCreatorListInputText() {
        return new ArrayList<InputText>(Arrays.asList(
                new InputTextHtml5("urn:cloudicaro:User:", "localUri",
                        "Insert local URI of user"), new InputTextHtml5("name",
                        "nameFoaf", "Insert name of user")));
    }

    public List<DropDownMenu> getGroupExternalStorageListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoGroupExternalStorage",
                        "Monitor Info"), new DropDownItem(
                        "addPanelLocalNetworkGroupExternalStorage",
                        "Local Network"), new DropDownItem(
                        "addPanelSharedStorageGroupExternalStorage",
                        "Shared Storage")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupExternalStorageListInputNumber() {
        return new ArrayList<InputText>(
                Arrays.asList(new InputTextHtml5("# External Storage",
                        "numberExternalStorage",
                        "Insert the number of external storage with this specification")));

    }

    public List<InputText> getGroupExternalStorageListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "prefixName", "hasPrefixName",
                "Insert prefixName of external storage"), new InputTextHtml5(
                "prefixIdentifier", "hasPrefixIdentifier",
                "Insert prefixIdentifier of firewall"), new InputTextHtml5(
                "model", "hasModelName",
                "Insert model name of external storage")));
    }

    public List<InputTextSelectOneMenu> getGroupExternalStorageListInputSelectOneMenu() {
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("monitorState",
                        "hasMonitorState", listMonitorState)));
    }

    public List<DropDownMenu> getGroupFirewallListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoGroupFirewall",
                        "Monitor Info"), new DropDownItem(
                        "addPanelLocalNetworkGroupFirewall", "Local Network")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupFirewallListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "# Firewall", "numberFirewall",
                "Insert the number of firewall with this specification")));

    }

    public List<InputText> getGroupFirewallListInputText() {
        return new ArrayList<InputText>(Arrays.asList(
                new InputTextHtml5("prefixName", "hasPrefixName",
                        "Insert prefixName of firewall"), new InputTextHtml5(
                        "prefixIdentifier", "hasPrefixIdentifier",
                        "Insert prefixIdentifier of firewall"),
                new InputTextHtml5("model", "hasModelName",
                        "Insert model name of firewall")));
    }

    public List<InputTextSelectOneMenu> getGroupFirewallListInputSelectOneMenu() {
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("monitorState",
                        "hasMonitorState", listMonitorState)));
    }

    public List<DropDownMenu> getGroupHostMachineListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoGroupHostMachine",
                        "Monitor Info"),
                new DropDownItem("addPanelLocalNetworkGroupHostMachine",
                        "Local Network"),
                new DropDownItem("addPanelLocalStorageGroupHostMachine",
                        "Local Storage"), new DropDownItem(
                        "addInputTextSharedStorageGroupHostMachine",
                        "Shared Storage")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupHostMachineListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "# Host", "numberHost",
                "Insert the number of host with this specification"),
                new InputTextHtml5("CPU", "hasCPUCount",
                        "Insert the number of CPU in each host"),
                new InputTextHtml5("CPUSpeed", "hasCPUSpeed",
                        "Insert the speed of each CPU in the host"),
                new InputTextHtml5("memorySize", "hasMemorySize",
                        "Insert size of primary memory in GB"),
                new InputTextHtml5("capacity", "hasCapacity",
                        "Insert the capacity of host")));

    }

    public List<InputText> getGroupHostMachineListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "prefixName", "hasPrefixName", "Insert prefixName of host"),
                new InputTextHtml5("prefixIdentifier", "hasPrefixIdentifier",
                        "Insert prefixIdentifier of host"), new InputTextHtml5(
                        "CPUType", "hasCPUType",
                        "Insert the type (Model) of each CPU in the host"),
                new InputTextHtml5("domain", "isInDomain",
                        "Insert domain of host"), new InputTextHtml5(
                        "username", "hasAuthUserName",
                        "Insert username of host"), new InputTextHtml5(
                        "password", "hasAuthUserPassword",
                        "Insert password of host")));
    }

    public List<InputTextSelectOneMenu> getGroupHostMachineListInputSelectOneMenu() {
        List<InputText> listOS = new ArrayList<InputText>(Arrays.asList(
                new InputText("Linux", "linux"), new InputText("centOS",
                        "centos"), new InputText("Red Hat", "redHat"),
                new InputText("Ubuntu", "ubuntu"), new InputText("Windows XP",
                        "windowsXP"), new InputText("Windows 2012 Server",
                        "windows2012Server"), new InputText(
                        "Windows 2008 Server", "windows2008Server"),
                new InputText("Windows 2003 Server", "windows2003Server"),
                new InputText("Windows Vista", "windowsVista"), new InputText(
                        "Windows 7", "windows7"), new InputText("AS 400",
                        "as400"), new InputText("VMWare ESXI", "vmware_esxi")));
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));

        return new ArrayList<InputTextSelectOneMenu>(Arrays.asList(
                new InputTextSelectOneMenu("operatingSystem", "hasOS", listOS),
                new InputTextSelectOneMenu("monitorState", "hasMonitorState",
                        listMonitorState)));
    }

    public List<DropDownMenu> getGroupRouterListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoGroupRouter",
                        "Monitor Info"), new DropDownItem(
                        "addPanelLocalNetworkGroupRouter", "Local Network")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupRouterListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "# Router", "numberRouter",
                "Insert the number of router with this specification")));

    }

    public List<InputText> getGroupRouterListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "prefixName", "hasPrefixName", "Insert prefixName of router"),
                new InputTextHtml5("prefixIdentifier", "hasPrefixIdentifier",
                        "Insert prefixIdentifier of router"),
                new InputTextHtml5("model", "hasModelName",
                        "Insert model name of router")));
    }

    public List<InputTextSelectOneMenu> getGroupRouterListInputSelectOneMenu() {
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("monitorState",
                        "hasMonitorState", listMonitorState)));
    }

    public List<InputText> getGroupServiceMetricListInputDate() {
        return new ArrayList<InputText>(
                Arrays.asList(
                        new InputTextHtml5(
                                "from",
                                "dateFrom",
                                "Insert date and time in which the metric started or will start (i.e., YYYY-MM-DDTHH:MM)"),
                        new InputTextHtml5(
                                "to",
                                "dateTo",
                                "Insert date and time in which the metric ended or will end (i.e., YYYY-MM-DDTHH:MM)")));
    }

    public List<InputText> getGroupServiceMetricListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "# serviceMetric", "numberServiceMetric",
                "Insert the number of service metric to generate"),
                new InputTextHtml5("min value", "minValue",
                        "Insert the minimum value assumed from the metric"),
                new InputTextHtml5("max value", "maxValue",
                        "Insert the maximum value assumed from the metric"),
                new InputTextHtml5("final value", "finalValue",
                        "Insert the final value assumed from the metric")));

    }

    public List<InputText> getGroupServiceMetricListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "uriEntity", "dependsOn",
                "Insert uri of the entity associated with metric"),
                new InputTextHtml5("name", "metricName",
                        "Insert name of the metric"), new InputTextHtml5(
                        "unit", "metricUnit", "Insert the unit of metric")));
    }

    public List<DropDownMenu> getGroupVirtualMachineForSimulatorListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelVirtualStorageVirtualMachine",
                        "Virtual Storage")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupVirtualMachineForSimulatorListInputNumber() {
        return new ArrayList<InputText>(
                Arrays.asList(
                        new InputTextHtml5("# Virtual", "numberVirtual",
                                "Insert the number of virtual machine with this specification inside each host"),
                        new InputTextHtml5("CPU", "hasCPUCount",
                                "Insert the number of CPU in each virtual machine"),
                        new InputTextHtml5("CPUReservation",
                                "hasCPUSpeedReservation",
                                "Insert the reservated speed for each CPU"),
                        new InputTextHtml5("CPULimit", "hasCPUSpeedLimit",
                                "Insert the limit speed for each CPU"),
                        new InputTextHtml5("memorySize", "hasMemorySize",
                                "Insert size of primary memory in GB"),
                        new InputTextHtml5("memoryReservation",
                                "hasMemoryReservation",
                                "Insert reservated memory in GB"),
                        new InputTextHtml5("memoryLimit", "hasMemoryLimit",
                                "Insert limit memory in GB")));

    }

    public List<InputTextSelectOneMenu> getGroupVirtualMachineForSimulatorListInputSelectOneMenu() {
        List<InputText> listOS = new ArrayList<InputText>(Arrays.asList(
                new InputText("Red Hat", "redHat"), new InputText("Ubuntu",
                        "ubuntu"), new InputText("Windows XP", "windowsXP"),
                new InputText("Windows 2003 Server", "windows2003Server"),
                new InputText("Windows Vista", "windowsVista"), new InputText(
                        "Windows 7", "windows7"), new InputText("OS 400",
                        "os400")));

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("operatingSystem",
                        "hasOS", listOS)));
    }

    public List<DropDownMenu> getGroupVirtualMachineListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoGroupVirtualMachine",
                        "Monitor Info"), new DropDownItem(
                        "addPanelLocalNetworkGroupVirtualMachine",
                        "Local Network"), new DropDownItem(
                        "addPanelVirtualStorageGroupVirtualMachine",
                        "Virtual Storage")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getGroupVirtualMachineListInputIP() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "externalIPAddress", "hasExternalIPAddress",
                "Insert the external IP address of virtual machine")));
        // TODO vedere in che modo gestire ExternalIPAddress: nel senso che
        // questo indirizzo dovrebbe essere unico per ogni VirtualMachine e
        // generando un gruppo di VirtualMachine
        // devo vedere come gestire la cosa.
    }

    public List<InputText> getGroupVirtualMachineListInputNumber() {
        return new ArrayList<InputText>(
                Arrays.asList(
                        new InputTextHtml5("# Virtual", "numberVirtual",
                                "Insert the number of virtual machine with this specification inside each host"),
                        new InputTextHtml5("CPU", "hasCPUCount",
                                "Insert the number of CPU in each virtual machine"),
                        new InputTextHtml5("CPUReservation",
                                "hasCPUSpeedReservation",
                                "Insert the reservated speed for each CPU"),
                        new InputTextHtml5("CPULimit", "hasCPUSpeedLimit",
                                "Insert the limit speed for each CPU"),
                        new InputTextHtml5("memorySize", "hasMemorySize",
                                "Insert size of primary memory in GB"),
                        new InputTextHtml5("memoryReservation",
                                "hasMemoryReservation",
                                "Insert reservated memory in GB"),
                        new InputTextHtml5("memoryLimit", "hasMemoryLimit",
                                "Insert limit memory in GB")));

    }

    public List<InputText> getGroupVirtualMachineListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "prefixName", "hasPrefixName",
                "Insert prefixName of virtual machine"), new InputTextHtml5(
                "prefixIdentifier", "hasPrefixIdentifier",
                "Insert prefixIdentifier of virtual machine"),
                new InputTextHtml5("domain", "isInDomain",
                        "Insert domain of virtual machine"),
                new InputTextHtml5("username", "hasAuthUserName",
                        "Insert username of virtual machine"),
                new InputTextHtml5("password", "hasAuthUserPassword",
                        "Insert password of virtual machine")));

    }

    public List<InputTextSelectOneMenu> getGroupVirtualMachineListInputSelectOneMenu() {
        List<InputText> listOS = new ArrayList<InputText>(Arrays.asList(
                new InputText("Linux", "linux"), new InputText("centOS",
                        "centos"), new InputText("Red Hat", "redHat"),
                new InputText("Ubuntu", "ubuntu"), new InputText("Windows XP",
                        "windowsXP"), new InputText("Windows 2012 Server",
                        "windows2012Server"), new InputText(
                        "Windows 2008 Server", "windows2008Server"),
                new InputText("Windows 2003 Server", "windows2003Server"),
                new InputText("Windows Vista", "windowsVista"), new InputText(
                        "Windows 7", "windows7"), new InputText("AS 400",
                        "as400")));
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));

        return new ArrayList<InputTextSelectOneMenu>(Arrays.asList(
                new InputTextSelectOneMenu("operatingSystem", "hasOS", listOS),
                new InputTextSelectOneMenu("monitorState", "hasMonitorState",
                        listMonitorState)));
    }

    public List<InputTextSelectOneMenu> getIcaroApplicationForSimulatorOfSelectOneMenu() {
        List<InputText> listType = new ArrayList<InputText>(Arrays.asList(
                new InputText("Joomla", "&app;Joomla"), new InputText("XLMS",
                        "&app;XLMS"), new InputText("JoomlaBalanced",
                        "&app;JoomlaBalanced"), new InputText("XLMSBalanced",
                        "&app;XLMSBalanced"), new InputText("MyXLMS",
                        "&app;MyXLMS")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("typeOfApplication",
                        "typeRdf", listType)));
    }

    public List<DropDownMenu> getIcaroApplicationListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelIcaroServiceIcaroApplication",
                        "Icaro Service"), new DropDownItem(
                        "addPanelSlAgreementIcaroApplication", "SLAgreement"),
                new DropDownItem("addPanelCreatorIcaroApplication", "Creator")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getIcaroApplicationListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "capacity", "hasCapacity",
                "Insert the capacity of icaro application")));
    }

    public List<InputText> getIcaroApplicationListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of icaro application"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of icaro application")));
    }

    public List<InputTextSelectOneMenu> getIcaroApplicationListInputSelectOneMenu() {
        List<InputText> listType = new ArrayList<InputText>(Arrays.asList(
                new InputText("Joomla", "&app;Joomla"), new InputText("XLMS",
                        "&app;XLMS"), new InputText("JoomlaBalanced",
                        "&app;JoomlaBalanced"), new InputText("XLMSBalanced",
                        "&app;XLMSBalanced"), new InputText("MyXLMS",
                        "&app;MyXLMS")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("typeOfApplication",
                        "typeRdf", listType)));
    }

    public List<DropDownMenu> getIcaroServiceIcaroApplicationListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoIcaroServiceIA",
                        "Monitor Info"), new DropDownItem(
                        "addInputNumberTcpPortIcaroService", "TCP Port"),
                new DropDownItem("addInputNumberUdpPortIcaroService",
                        "UDP Port")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getIcaroServiceListInputIP() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "monitorIPAddress", "hasMonitorIPAddress",
                "Insert IP address of monitor info of icaro service")));
    }

    public List<InputText> getIcaroServiceListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of icaro service"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of icaro service"),
                new InputTextHtml5("processName", "hasProcessName",
                        "Insert name of the process of icaro service"),
                new InputTextHtml5("username", "hasAuthUserName",
                        "Insert username of icaro service"),
                new InputTextHtml5("password", "hasAuthUserPassword",
                        "Insert password of icaro service")));
    }

    public List<InputTextSelectOneMenu> getIcaroServiceListInputSelectOneMenu() {
        List<InputText> listType = new ArrayList<InputText>(Arrays.asList(
                new InputText("AxcpGridNode", "&icr;AxcpGridNode"),
                new InputText("AxcpScheduler", "&icr;AxcpScheduler"),
                new InputText("FtpServer", "&icr;FtpServer"), new InputText(
                        "HttpBalancer", "&icr;HttpBalancer"), new InputText(
                        "MailServer", "&icr;MailServer"), new InputText(
                        "MSSQL", "&icr;MSSQL"), new InputText("MySQL",
                        "&icr;MySQL"), new InputText("PostgresSQL",
                        "&icr;PostgresSQL"), new InputText("NFSServer",
                        "&icr;NFSServer"), new InputText("SambaServer",
                        "&icr;SambaServer"), new InputText("ApacheTomcat",
                        "&icr;ApacheTomcat"), new InputText("JBoss",
                        "&icr;JBoss"), new InputText("ApacheWebServer",
                        "&icr;ApacheWebServer"), new InputText(
                        "InternetInformationServer",
                        "&icr;InternetInformationServer")));
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        List<InputText> listSupportsLanguage = new ArrayList<InputText>(
                Arrays.asList(new InputText("C#", "&icr;csharp"),
                        new InputText("Java", "&icr;java"), new InputText(
                                "PHP", "&icr;php"), new InputText("Ruby",
                                "&icr;ruby")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("typeOfService",
                        "typeRdf", listType), new InputTextSelectOneMenu(
                        "supportedLanguage", "supportsLanguage",
                        listSupportsLanguage), new InputTextSelectOneMenu(
                        "monitorState", "hasMonitorState", listMonitorState)));
    }

    public List<DropDownMenu> getIcaroTenantListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelMonitorInfoIcaroTenant",
                        "Monitor Info"), new DropDownItem(
                        "addPanelSlAgreementIcaroTenant", "SLAgreement")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getIcaroTenantListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of icaro application"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of icaro application")));
    }

    public List<InputTextSelectOneMenu> getIcaroTenantListInputSelectOneMenu() {
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("monitorState",
                        "hasMonitorState", listMonitorState)));
    }

    public List<InputText> getLocalNetworkListInputIP() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "networkAddress", "hasIPAddress",
                "Insert network address that identify the local Network"),
                new InputTextHtml5("subNetMask", "hasSubNetworkMask",
                        "Insert subnet mask of local network")));
    }

    public List<InputText> getLocalNetworkListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "urn:cloudicaro:LocalNetwork:", "localUri",
                "Insert localURI of local network"), new InputTextHtml5("name",
                "hasName", "Insert name of local network"), new InputTextHtml5(
                "identifier", "hasIdentifier",
                "Insert identifier of local network")));
    }

    public List<InputText> getLocalStorageListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "diskSize", "hasDiskSize", "Insert disk's size")));

    }

    public List<InputText> getLocalStorageListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of localStorage"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of localStorage")));
    }

    public List<InputText> getMonitorInfoListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "warningValue", "hasWarningValue",
                "Insert the warning value for metric"), new InputTextHtml5(
                "criticalValue", "hasCriticalValue",
                "Insert the critical value for metric"), new InputTextHtml5(
                "maxCheckAttemps", "hasMaxCheckAttempts",
                "Insert the max check attemps for metric")));

    }

    public List<InputText> getMonitorInfoListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "metricName", "hasMetricName", "Insert the name of metric"),
                new InputTextHtml5("arguments", "hasArguments",
                        "Insert arguments of metric")));
    }

    public List<InputTextSelectOneMenu> getMonitorInfoListInputSelectOneMenu() {
        List<InputText> listCheckMode = new ArrayList<InputText>(Arrays.asList(
                new InputText("Passive", "passive"), new InputText("Active",
                        "active")));
        List<InputText> listMonitorState = new ArrayList<InputText>(
                Arrays.asList(new InputText("Enabled", "enabled"),
                        new InputText("Disabled", "disabled")));
        return new ArrayList<InputTextSelectOneMenu>(Arrays.asList(
                new InputTextSelectOneMenu("monitorState", "hasMonitorState",
                        listMonitorState), new InputTextSelectOneMenu(
                        "checkMode", "hasCheckMode", listCheckMode)));
    }

    public List<InputText> getSharedStorageVolumeListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "diskSize", "hasDiskSize", "Insert disk's size")));

    }

    public List<InputText> getSharedStorageVolumeListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "urn:cloudicaro:SharedStorageVolume:", "localUri",
                "Insert localUri of sharedStorageVolume"), new InputTextHtml5(
                "name", "hasName", "Insert name of sharedStorageVolume"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of sharedStorageVolume")));
    }

    public List<InputText> getSlActionListInputText() {
        return new ArrayList<InputText>(
                Arrays.asList(
                        new InputTextHtml5("name", "hasName",
                                "Insert name of action to perform when SLMetric is not respected"),
                        new InputTextHtml5("callURI", "callUri",
                                "Insert URI to call when SLMetric is not respected")));
    }

    public List<DropDownMenu> getSlAgreementIcaroApplicationListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelSlObjectiveSlAgreementIA", "SLObjective")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<DropDownMenu> getSlAgreementIcaroTenantListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelSlObjectiveSlAgreementIT", "SLObjective")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<DropDownMenu> getSlAgreementListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelSlObjectiveSlAgreement", "SLObjective")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getSlAgreementListInputDate() {
        return new ArrayList<InputText>(
                Arrays.asList(
                        new InputTextHtml5(
                                "startTime",
                                "hasStartTime",
                                "Insert date and time in which the SLA started or will start (i.e., YYYY-MM-DDTHH:MM)"),
                        new InputTextHtml5(
                                "endTime",
                                "hasEndTime",
                                "Insert date and time in which the SLA ended or will end (i.e., YYYY-MM-DDTHH:MM)")));
    }

    public List<InputText> getSlMetricListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "value", "hasMetricValue",
                "Insert value of SLMetric to observe for respect the limit")));
    }

    public List<InputText> getSlMetricListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of SLMetric"),
                new InputTextHtml5("unit", "hasMetricUnit",
                        "Insert unit of SLMetric")));
    }

    public List<InputTextSelectOneMenu> getSlMetricListInputSelectOneMenu() {
        List<InputText> listSymbol = new ArrayList<InputText>(Arrays.asList(
                new InputText("Greater", "greater"), new InputText("Less",
                        "less"), new InputText("Equal", "equal")));

        return new ArrayList<InputTextSelectOneMenu>(
                Arrays.asList(new InputTextSelectOneMenu("Limit", "hasSymbol",
                        listSymbol)));
    }

    public List<DropDownMenu> getSlObjectiveSlAgreementIAListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelSlActionSlObjectiveIASA", "SLAction"),
                        new DropDownItem("addPanelSlMetricSlObjectiveIASA",
                                "SLMetric")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<DropDownMenu> getSlObjectiveSlAgreementITListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(
                Arrays.asList(new DropDownItem(
                        "addPanelSlActionSlObjectiveITSA", "SLAction"),
                        new DropDownItem("addPanelSlMetricSlObjectiveITSA",
                                "SLMetric")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<DropDownMenu> getSlObjectiveSlAgreementListDropDownMenu() {
        List<DropDownItem> listAdd = new ArrayList<DropDownItem>(Arrays.asList(
                new DropDownItem("addPanelSlActionSlObjectiveSA", "SLAction"),
                new DropDownItem("addPanelSlMetricSlObjectiveSA", "SLMetric")));

        return new ArrayList<DropDownMenu>(Arrays.asList(new DropDownMenu(
                listAdd, "Add")));
    }

    public List<InputText> getVirtualStorageListInputNumber() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "diskSize", "hasDiskSize", "Insert disk's size")));

    }

    public List<InputText> getVirtualStorageListInputText() {
        return new ArrayList<InputText>(Arrays.asList(new InputTextHtml5(
                "name", "hasName", "Insert name of virtualStorage"),
                new InputTextHtml5("identifier", "hasIdentifier",
                        "Insert identifier of virtualStorage")));
    }
}
