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

package org.openo.sdno.localsiteservice.impl.cpe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.inf.cpe.CloudCpeService;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.sbi.cpe.CpeSbiService;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.inventory.sdk.util.InventoryDaoUtil;
import org.openo.sdno.overlayvpn.model.common.enums.ActionStatus;
import org.openo.sdno.overlayvpn.model.common.enums.AdminStatus;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of CloudCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Service
public class CloudCpeServiceImpl implements CloudCpeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudCpeServiceImpl.class);

    @Autowired
    private CpeSbiService sbiService;

    @Override
    public ResultRsp<NbiCloudCpeModel> query(HttpServletRequest req, String cloudCpeUuid) throws ServiceException {
        InventoryDao<NbiCloudCpeModel> cloudCpeModelDao = new InventoryDaoUtil<NbiCloudCpeModel>().getInventoryDao();
        return cloudCpeModelDao.query(NbiCloudCpeModel.class, cloudCpeUuid, null);
    }

    @Override
    public ResultRsp<List<NbiCloudCpeModel>> batchQuery(HttpServletRequest req, Map<String, Object> filterMap)
            throws ServiceException {
        InventoryDao<NbiCloudCpeModel> cloudCpeModelDao = new InventoryDaoUtil<NbiCloudCpeModel>().getInventoryDao();
        return cloudCpeModelDao.batchQuery(NbiCloudCpeModel.class, JsonUtil.toJson(filterMap));
    }

    @Override
    public ResultRsp<NbiCloudCpeModel> create(HttpServletRequest req, NbiCloudCpeModel cloudCpeModel)
            throws ServiceException {
        // Set vCPE template
        ResultRsp<NbiSiteModel> queryResultRsp =
                (new ModelDataDao<NbiSiteModel>()).query(NbiSiteModel.class, cloudCpeModel.getSiteId());
        if(!queryResultRsp.isValid()) {
            LOGGER.error("Site Model query failed or does not exist");
            throw new ServiceException("Site Model query failed or does not exist");
        }

        NbiSiteModel siteModel = queryResultRsp.getData();
        cloudCpeModel.setTemplate(siteModel.getSiteDescriptor() + "_" + siteModel.getReliability());

        // Set vCPE name
        if(!StringUtils.hasLength(cloudCpeModel.getName())) {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            cloudCpeModel.setName("vCPE" + dataFormat.format(new Date()));
        }

        // Create vCPE in adapter
        VCpePlanInfo planInfo = new VCpePlanInfo(cloudCpeModel);
        ResultRsp<Map<String, String>> resultRsp = sbiService.create(planInfo);
        if(!resultRsp.isValid()) {
            LOGGER.error("Create CloudCpe failed");
            throw new ServiceException("Create CloudCpe failed");
        }

        String cpeModelUuid = resultRsp.getData().get("id");
        cloudCpeModel.setUuid(cpeModelUuid);

        // Insert CloudCpeModel into database
        cloudCpeModel.setActionState(ActionStatus.NORMAL.getName());
        cloudCpeModel.setAdminState(AdminStatus.ACTIVE.getName());
        InventoryDao<NbiCloudCpeModel> cloudCpeModelDao = new InventoryDaoUtil<NbiCloudCpeModel>().getInventoryDao();
        cloudCpeModelDao.insert(cloudCpeModel);

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, cloudCpeModel);
    }

    @Override
    public void delete(HttpServletRequest req, String cloudCpeUuid) throws ServiceException {

        ResultRsp<NbiCloudCpeModel> resultRsp = query(req, cloudCpeUuid);
        if(!resultRsp.isSuccess()) {
            LOGGER.error("Query CloudCpe data failed");
            throw new ServiceException("uery CloudCpe data failed");
        }

        if(!resultRsp.isValid()) {
            LOGGER.warn("CloudCpe data does not exist");
            return;
        }

        // Delete CloudCpeModel in database
        InventoryDao<NbiCloudCpeModel> cloudCpeModelDao = new InventoryDaoUtil<NbiCloudCpeModel>().getInventoryDao();
        cloudCpeModelDao.delete(NbiCloudCpeModel.class, cloudCpeUuid);

        // Delete from Adapter
        sbiService.delete(cloudCpeUuid);
    }
}
