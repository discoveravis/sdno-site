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

package org.openo.sdno.localsiteservice.impl.site;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.dao.SiteModelDao;
import org.openo.sdno.localsiteservice.inf.site.SiteService;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of Site Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private SiteModelDao siteModelDao;

    @Autowired
    private SiteInvDao siteInvDao;

    @Override
    public ResultRsp<NbiSiteModel> query(HttpServletRequest req, String siteUuid) throws ServiceException {

        // Query from Brs Site Model
        SiteMO siteMO = siteInvDao.query(siteUuid);
        if(null == siteMO) {
            LOGGER.error("SiteMO does not exist");
            throw new ServiceException("SiteMO does not exist");
        }

        NbiSiteModel siteModel = querySiteModelById(siteUuid);

        siteModel.initBasicInfo(siteMO.getName(), siteMO.getTenantID(), siteMO.getLocation(), siteMO.getDescription());

        // Query ThinCpe Info
        siteModel.setLocalCpes(siteModelDao.querySiteLocalCpes(siteUuid));

        // Query CloudCpe Info
        siteModel.setCloudCpes(siteModelDao.querySiteCloudCpes(siteUuid));

        // Query Subnet Info
        siteModel.setSubnets(siteModelDao.querySiteSubnets(siteUuid));

        // Query Vlan Info
        siteModel.setVlans(siteModelDao.querySiteVlans(siteUuid));

        // Query RouteEntry Info
        siteModel.setRoutes(siteModelDao.querySiteRouteEntrys(siteUuid));

        // Query InternetGateway Info
        siteModel.setInternetGateway(siteModelDao.querySiteInternetGateways(siteUuid));

        return new ResultRsp<NbiSiteModel>(ErrorCode.OVERLAYVPN_SUCCESS, siteModel);
    }

    @Override
    public ComplexResult<NbiSiteModel> batchQuery(HttpServletRequest req, List<String> siteIds)
            throws ServiceException {

        ComplexResult<NbiSiteModel> batchQueryResult = new ComplexResult<>();
        List<NbiSiteModel> siteModelList = new ArrayList<>();
        for(String siteId : siteIds) {
            NbiSiteModel curSiteModel = querySiteModelById(siteId);
            if(null != curSiteModel) {
                siteModelList.add(curSiteModel);
            }
        }

        batchQueryResult.setData(siteModelList);
        batchQueryResult.setTotal(siteModelList.size());

        return batchQueryResult;
    }

    @Override
    public ResultRsp<NbiSiteModel> create(HttpServletRequest req, NbiSiteModel siteModel) throws ServiceException {

        SiteMO dbSiteMO = siteInvDao.query(siteModel.getUuid());
        if(null == dbSiteMO) {
            // Insert Brs Site Model
            siteInvDao.addMO(buildSiteMO(siteModel));
        }

        // Insert Mss Site Model
        ResultRsp<NbiSiteModel> insertResultRsp = (new ModelDataDao<NbiSiteModel>()).insert(siteModel);
        if(!insertResultRsp.isValid()) {
            LOGGER.error("SiteModel insert failed");
            throw new ServiceException("SiteModel insert failed");
        }

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, insertResultRsp.getData());
    }

    @Override
    public void delete(HttpServletRequest req, String siteId) throws ServiceException {
        // Delete LocalSite SiteModel data
        (new ModelDataDao<NbiSiteModel>()).delete(NbiSiteModel.class, siteId);
        // Delete Brs Site data
        siteInvDao.deleteMO(siteId);
    }

    @Override
    public ResultRsp<NbiSiteModel> update(HttpServletRequest req, NbiSiteModel siteModel) throws ServiceException {

        // Update Brs Site data
        SiteMO dbSiteMO = siteInvDao.query(siteModel.getUuid());
        if(StringUtils.hasLength(siteModel.getDescription())) {
            dbSiteMO.setDescription(siteModel.getDescription());
        }

        if(StringUtils.hasLength(siteModel.getLocation())) {
            dbSiteMO.setLocation(siteModel.getLocation());
        }

        siteInvDao.updateMO(dbSiteMO);

        // Update SiteModel data
        NbiSiteModel dbSiteModel = querySiteModelById(siteModel.getUuid());
        if(StringUtils.hasLength(siteModel.getReliability())) {
            dbSiteModel.setReliability(siteModel.getReliability());
        }

        if(StringUtils.hasLength(siteModel.getIsEncrypt())) {
            dbSiteModel.setIsEncrypt(siteModel.getIsEncrypt());
        }

        if(StringUtils.hasLength(siteModel.getDescription())) {
            dbSiteModel.setDescription(siteModel.getDescription());
        }

        if(StringUtils.hasLength(siteModel.getLocation())) {
            dbSiteModel.setLocation(siteModel.getLocation());
        }

        return (new ModelDataDao<NbiSiteModel>()).update(dbSiteModel, "reliability,isEncrypt,description,location");
    }

    private NbiSiteModel querySiteModelById(String siteUuid) throws ServiceException {
        ResultRsp<NbiSiteModel> queryResultRsp = (new ModelDataDao<NbiSiteModel>()).query(NbiSiteModel.class, siteUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("Site Model query failed");
            throw new ServiceException("Site Model query failed");
        }

        return queryResultRsp.getData();
    }

    private SiteMO buildSiteMO(NbiSiteModel siteModel) {
        SiteMO siteMO = new SiteMO();
        siteMO.setType("tenant_site");
        siteMO.setName(siteModel.getName());
        siteMO.setDescription(siteModel.getDescription());
        siteMO.setLocation(siteModel.getLocation());
        siteMO.setTenantID(siteModel.getTenantId());
        siteMO.setId(siteModel.getUuid());
        return siteMO;
    }

}
