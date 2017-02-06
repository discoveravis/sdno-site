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

package org.openo.sdno.localsiteservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.model.vpn.VpnGateway;
import org.openo.sdno.localsiteservice.sbi.site.TemplateSbiService;
import org.openo.sdno.localsiteservice.sbi.site.VpnGatewaySbiService;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.invdao.PopInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.brs.model.PopMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Dao class of Site operation.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-14
 */
@Repository
public class SiteModelDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteModelDao.class);

    private static final String TEMPLATE_NAME_SPLIT = "_";

    private static final String INTERNETGATEWAY_QUERY_URL = "/openoapi/sdnolocalsite/v1/internet-gateways";

    private static final String ROUTEENRTY_QUERY_URL = "/openoapi/sdnolocalsite/v1/route-entries";

    private static final String VLAN_QUERY_URL = "/openoapi/sdnolocalsite/v1/vlans";

    private static final String SUBNET_QUERY_URL = "/openoapi/sdnolocalsite/v1/subnets";

    @Autowired
    private TemplateSbiService templateSbiSrevice;

    @Autowired
    private VpnGatewaySbiService vpnGatewaySbiService;

    @Autowired
    private BaseResourceDao baseResourceDao;

    @Autowired
    private NetworkElementInvDao neInvDao;

    @Autowired
    private PopInvDao popInvDao;

    /**
     * Get template name of site.<br>
     * 
     * @param siteId Site Uuid
     * @return template name of site
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getTemplateName(String siteId) throws ServiceException {
        NbiSiteModel siteModel = querySiteModelById(siteId);
        String templateName = siteModel.getSiteDescriptor() + TEMPLATE_NAME_SPLIT + siteModel.getReliability();
        return templateName;
    }

    /**
     * Get local cpe type of site.<br>
     * 
     * @param siteId Site Uuid
     * @return localCPE type of site
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getLocalCpeType(String siteId) throws ServiceException {
        NbiSiteModel siteModel = querySiteModelById(siteId);
        return siteModel.getLocalCpeType();
    }

    /**
     * Get Site Local Cpe Device.<br>
     * 
     * @param siteId Site Uuid
     * @return LocalCpe Device
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NetworkElementMO getSiteLocalCpe(String siteId) throws ServiceException {
        NetworkElementMO localCPE = baseResourceDao.queryCpeBySiteIdAndCpeType(siteId, CpeRoleType.THIN_CPE.getName());
        if(null == localCPE) {
            LOGGER.error("This Site do not have one LocalCpe");
            throw new ServiceException("This Site do not have one LocalCpe");
        }
        return localCPE;
    }

    /**
     * Get Site Cloud Cpe Device.<br>
     * 
     * @param siteId Site Uuid
     * @return CloudCpe Device
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NetworkElementMO getSiteCloudCpe(String siteId) throws ServiceException {
        NetworkElementMO cloudCPE = baseResourceDao.queryCpeBySiteIdAndCpeType(siteId, CpeRoleType.CLOUD_CPE.getName());
        if(null == cloudCPE) {
            LOGGER.error("This Site do not have one CloudCpe");
            throw new ServiceException("This Site do not have one CloudCpe");
        }
        return cloudCPE;
    }

    /**
     * Get Site Gateway.<br>
     * 
     * @param siteId Site Uuid
     * @return Site Gateway
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NetworkElementMO getSiteGateway(String siteId) throws ServiceException {

        String templateName = getTemplateName(siteId);
        String gwPosition = templateSbiSrevice.getGwPosition(templateName);
        if(null == gwPosition) {
            LOGGER.error("No gateway position in template");
            throw new ServiceException("No gateway position in template");
        }

        NetworkElementMO neMO = null;
        if("localcpe".equals(gwPosition)) {
            neMO = getSiteLocalCpe(siteId);
        } else if("cloudcpe".equals(gwPosition)) {
            neMO = getSiteCloudCpe(siteId);
        } else {
            LOGGER.error("Gateway position is invalid");
            throw new ServiceException("Gateway position is invalid");
        }

        return neMO;
    }

    /**
     * Query Site by Controller Id.<br>
     * 
     * @param siteId Site Id
     * @return Controller Id
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getSiteControllerId(String siteId) throws ServiceException {
        NbiSiteModel siteModel = querySiteModelById(siteId);
        PopMO popMO = popInvDao.query(siteModel.getPopId());
        return popMO.getControllerID();
    }

    /**
     * Query Site LocalCpes.<br>
     * 
     * @param siteId Site Id
     * @return LocalCpes of Site
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NetworkElementMO> querySiteLocalCpes(String siteId) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("siteID", siteId);
        conditionMap.put("neRole", CpeRoleType.THIN_CPE.getName());

        return neInvDao.query(conditionMap);
    }

    /**
     * Query Site CloudCpes.<br>
     * 
     * @param siteId Site Id
     * @return CloudCpes of Site
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NetworkElementMO> querySiteCloudCpes(String siteId) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("siteID", siteId);
        conditionMap.put("neRole", CpeRoleType.CLOUD_CPE.getName());

        return neInvDao.query(conditionMap);
    }

    /**
     * Query SiteModel By Site Uuid.<br>
     * 
     * @param siteUuid Site Uuid
     * @return SiteModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiSiteModel querySiteModelById(String siteUuid) throws ServiceException {
        ResultRsp<NbiSiteModel> queryResultRsp = (new ModelDataDao<NbiSiteModel>()).query(NbiSiteModel.class, siteUuid);
        if(!queryResultRsp.isValid()) {
            LOGGER.error("Site model query failed or does not exist");
            throw new ServiceException("Site model query failed or does not exist");
        }

        return queryResultRsp.getData();
    }

    /**
     * Query Site Vpn Gateways.<br>
     * 
     * @param siteId Site Uuid
     * @return List of Vpn Gateways queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<VpnGateway> querySiteVpnGateways(String siteId) throws ServiceException {
        return vpnGatewaySbiService.queryVpnGateway(siteId);
    }

    /**
     * Query Site Internet Gateways.<br>
     * 
     * @param siteId Site Uuid
     * @return Internet Gateways queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NbiInternetGatewayModel querySiteInternetGateways(String siteId) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("siteId", siteId);
        RestfulResponse response = RestfulProxy.get(INTERNETGATEWAY_QUERY_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query Internet Gateways failed");
            throw new ServiceException("Query Internet Gateways failed");
        }

        ComplexResult<NbiInternetGatewayModel> queryResult = JsonUtil.fromJson(response.getResponseContent(),
                new TypeReference<ComplexResult<NbiInternetGatewayModel>>() {});

        if(queryResult.getData().isEmpty()) {
            return null;
        }

        return queryResult.getData().get(0);
    }

    /**
     * Query Site RouteEntrys.<br>
     * 
     * @param siteId Site Uuid
     * @return RouteEntrys queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NbiRouteEntryModel> querySiteRouteEntrys(String siteId) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("siteId", siteId);
        RestfulResponse response = RestfulProxy.get(ROUTEENRTY_QUERY_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query RouteEntry failed");
            throw new ServiceException("Query RouteEntry failed");
        }

        ComplexResult<NbiRouteEntryModel> queryResult = JsonUtil.fromJson(response.getResponseContent(),
                new TypeReference<ComplexResult<NbiRouteEntryModel>>() {});

        return queryResult.getData();
    }

    /**
     * Query Site Subnets.<br>
     * 
     * @param siteId Site Uuid
     * @return Subnets queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NbiSubnetModel> querySiteSubnets(String siteId) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("siteId", siteId);
        RestfulResponse response = RestfulProxy.get(SUBNET_QUERY_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query Subnets failed");
            throw new ServiceException("Query Subnets failed");
        }

        ComplexResult<NbiSubnetModel> queryResult =
                JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ComplexResult<NbiSubnetModel>>() {});

        return queryResult.getData();
    }

    /**
     * Query Site Vlans.<br>
     * 
     * @param siteId Site Uuid
     * @return Vlans queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NbiVlanModel> querySiteVlans(String siteId) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("siteId", siteId);
        RestfulResponse response = RestfulProxy.get(VLAN_QUERY_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query Vlans failed");
            throw new ServiceException("Query Vlans failed");
        }

        ComplexResult<NbiVlanModel> queryResult =
                JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ComplexResult<NbiVlanModel>>() {});

        return queryResult.getData();
    }

}
