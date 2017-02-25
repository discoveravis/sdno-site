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

package org.openo.sdno.localsiteservice.model.cpe;

import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.verify.annotation.AString;
import org.springframework.util.StringUtils;

/**
 * Model class of Cpe Plan Info.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-6
 */
public class VCpePlanInfo extends CpeBasicPlanInfo {

    /**
     * Site Uuid
     */
    private String siteId;

    /**
     * Pop Uuid
     */
    private String popId;

    /**
     * Ne Role
     */
    @AString(scope = "Thin CPE,vCPE,vFW")
    private String role;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    public VCpePlanInfo() {
        super();
    }

    /**
     * Constructor<br>
     * 
     * @param localCpeModel LocalCpeModel data
     * @since SDNO 0.5
     */
    public VCpePlanInfo(NbiLocalCpeModel localCpeModel) {
        super();
        if(StringUtils.hasLength(localCpeModel.getUuid())) {
            this.setUuid(localCpeModel.getUuid());
        }
        this.setTenantId(localCpeModel.getTenantId());
        this.setName(localCpeModel.getName());
        this.setRole("Thin CPE");
        this.setEsn(localCpeModel.getEsn());
        this.setControllerId(localCpeModel.getControllerId());
        this.setSiteId(localCpeModel.getSiteId());
        this.setOldEsn(localCpeModel.getOldEsn());
        this.setDescription(localCpeModel.getDescription());
    }

    /**
     * Constructor<br>
     * 
     * @param cloudCpeModel CloudCpeModel data
     * @since SDNO 0.5
     */
    public VCpePlanInfo(NbiCloudCpeModel cloudCpeModel) {
        super();
        if(StringUtils.hasLength(cloudCpeModel.getUuid())) {
            this.setUuid(cloudCpeModel.getUuid());
        }
        this.setTenantId(cloudCpeModel.getTenantId());
        this.setName(cloudCpeModel.getName());
        this.setRole("vCPE");
        this.setEsn(cloudCpeModel.getEsn());
        this.setControllerId(cloudCpeModel.getControllerId());
        this.setSiteId(cloudCpeModel.getSiteId());
        this.setDescription(cloudCpeModel.getDescription());
        this.setPopId(cloudCpeModel.getPopId());
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPopId() {
        return popId;
    }

    public void setPopId(String popId) {
        this.popId = popId;
    }
}
