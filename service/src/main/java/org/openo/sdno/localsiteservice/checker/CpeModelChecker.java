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

package org.openo.sdno.localsiteservice.checker;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.esr.invdao.SdnControllerDao;
import org.openo.sdno.overlayvpn.esr.model.SdnController;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Checker Class of Cpe Model.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-14
 */
@Component
public class CpeModelChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpeModelChecker.class);

    private static final Pattern PHYCPE_ESN_PATTERN = Pattern.compile("^[A-Z0-9]{20}$");

    private static final Pattern CLOUDCPE_ESN_PATTERN = Pattern.compile("^21[a-zA-Z0-9]{8}[0-9]{14}[a-zA-Z0-9]{8}$");

    private static final Pattern CLOUDFW_ESN_PATTERN =
            Pattern.compile("^0[1248][a-zA-Z0-9]{8}[0-9]{14}[a-zA-Z0-9]{8}$");

    @Autowired
    private SdnControllerDao sdnControllerDao;

    @Autowired
    private SiteInvDao siteInvDao;

    /**
     * Check LocalCpeModel data.<br>
     * 
     * @param localCpeModel LocalCpeModel data need to check
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    public void checkLocalCpeModel(NbiLocalCpeModel localCpeModel) throws ServiceException {

        // Check Esn Format
        checkEsnFormat(CpeRoleType.THIN_CPE.getName(), localCpeModel.getEsn());

        // Check Site
        checkSiteId(localCpeModel.getSiteId());

        // Check Controller
        checkControllerId(localCpeModel.getControllerId());

        // Check LocalCpeType
        checkLocalCpeType(localCpeModel);
    }

    /**
     * Check VCpePlanInfo data.<br>
     * 
     * @param cpePlanInfo VCpePlanInfo data need to check
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    public void checkCpePlanInfo(VCpePlanInfo cpePlanInfo) throws ServiceException {
        checkNeRole(cpePlanInfo.getRole());
        checkEsnFormat(cpePlanInfo.getRole(), cpePlanInfo.getEsn());
        checkSiteId(cpePlanInfo.getSiteId());
    }

    private void checkNeRole(String neRole) throws ServiceException {
        if(StringUtils.isEmpty(neRole)) {
            LOGGER.error("No Ne Role in  model data");
            throw new ServiceException("No Ne Role in  model data");
        }
    }

    private void checkSiteId(String siteId) throws ServiceException {
        if(StringUtils.isEmpty(siteId)) {
            LOGGER.error("No Site Id in model data");
            throw new ServiceException("No Site Id in model data");
        }

        SiteMO site = siteInvDao.query(siteId);
        if(null == site) {
            LOGGER.error("Site in model data does not exist");
            throw new ServiceException("Site in model data does not exist");
        }
    }

    private void checkControllerId(String controllerId) throws ServiceException {
        if(StringUtils.isEmpty(controllerId)) {
            LOGGER.error("No Controller Id in model data");
            throw new ServiceException("No Controller Id in model data");
        }

        SdnController controller = sdnControllerDao.querySdnControllerById(controllerId);
        if(null == controller) {
            LOGGER.error("Controller in model data does not exist");
            throw new ServiceException("Controller in model data does not exist");
        }
    }

    private void checkLocalCpeType(NbiLocalCpeModel localCpeModel) throws ServiceException {
        String localCpeType = localCpeModel.getLocalCpeType();

        if(StringUtils.isEmpty(localCpeType)) {
            LOGGER.error("No LocalCpeType Id in model data");
            throw new ServiceException("No LocalCpeType  in model data");
        }

        ResultRsp<NbiSiteModel> queryResultRsp =
                (new ModelDataDao<NbiSiteModel>()).query(NbiSiteModel.class, localCpeModel.getSiteId());
        if(!queryResultRsp.isValid()) {
            LOGGER.error("Site Model query failed or does not exist");
            throw new ServiceException("Site Model query failed or does not exist");
        }

        if((!localCpeType.equals(queryResultRsp.getData().getLocalCpeType()))) {
            LOGGER.error("LocalCpeType is invalid in model data");
            throw new ServiceException("LocalCpeType is invalid in model data");
        }
    }

    private void checkEsnFormat(String neRole, String esn) throws ServiceException {

        Pattern targetPattern = null;

        if(CpeRoleType.THIN_CPE.getName().equals(neRole)) {
            targetPattern = PHYCPE_ESN_PATTERN;
        } else if(CpeRoleType.CLOUD_CPE.getName().equals(neRole)) {
            targetPattern = CLOUDCPE_ESN_PATTERN;
        } else if(CpeRoleType.CLOUD_FW.getName().equals(neRole)) {
            targetPattern = CLOUDFW_ESN_PATTERN;
        } else {
            LOGGER.error("This NeRole does not support");
            throw new ServiceException("This NeRole does not support");
        }

        if(!targetPattern.matcher(esn).matches()) {
            LOGGER.error("This Esn is invalid");
            throw new ServiceException("This Esn is invalid");
        }

    }

}
