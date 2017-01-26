/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.localsiteservice.model.inf;

/**
 * Model class of Site Interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-6
 */
public class InterfaceModel {

    /**
     * Interface Id
     */
    private String id;

    /**
     * Interface name
     */
    private String name;

    /**
     * Run Status
     */
    private Integer runStatus;

    /**
     * Administrative Status
     */
    private Integer mgrStatus;

    /**
     * Index of Interface
     */
    private String index;

    /**
     * Alias of Interface
     */
    private String alias;

    /**
     * Interface Ipv4 Address
     */
    private String ipAddr;

    /**
     * Interface Ipv6 Address
     */
    private String ipv6Addr;

    /**
     * Bandwidth of Interface
     */
    private String bandWidth;

    /**
     * Mtu
     */
    private Integer mtu;

    /**
     * Whether to report warnings
     */
    private Boolean report;

    /**
     * Interface Mode
     */
    private Integer mode;

    /**
     * iccid in SIM card
     */
    private String iccid;

    /**
     * Mac Address
     */
    private String mac;

    /**
     * Termination Mode
     */
    private String terminationMode;

    /**
     * Low Vlan(1~4096)
     */
    private Integer ceLowVlan;

    /**
     * High Vlan(1~4096)
     */
    private Integer ceHighVlan;

    /**
     * pVlan in Qing mode
     */
    private Integer peVlan;

    /**
     * Ip Mask
     */
    private String mask;

    /**
     * Ipv6 Mask length
     */
    private String prefixLength;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getMgrStatus() {
        return mgrStatus;
    }

    public void setMgrStatus(Integer mgrStatus) {
        this.mgrStatus = mgrStatus;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getIpv6Addr() {
        return ipv6Addr;
    }

    public void setIpv6Addr(String ipv6Addr) {
        this.ipv6Addr = ipv6Addr;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth) {
        this.bandWidth = bandWidth;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTerminationMode() {
        return terminationMode;
    }

    public void setTerminationMode(String terminationMode) {
        this.terminationMode = terminationMode;
    }

    public Integer getCeLowVlan() {
        return ceLowVlan;
    }

    public void setCeLowVlan(Integer ceLowVlan) {
        this.ceLowVlan = ceLowVlan;
    }

    public Integer getCeHighVlan() {
        return ceHighVlan;
    }

    public void setCeHighVlan(Integer ceHighVlan) {
        this.ceHighVlan = ceHighVlan;
    }

    public Integer getPeVlan() {
        return peVlan;
    }

    public void setPeVlan(Integer peVlan) {
        this.peVlan = peVlan;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getPrefixLength() {
        return prefixLength;
    }

    public void setPrefixLength(String prefixLength) {
        this.prefixLength = prefixLength;
    }

}
