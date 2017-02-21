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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.dao.BaseResourceDao;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.dao.SiteModelDao;
import org.openo.sdno.localsiteservice.inf.site.VlanService;
import org.openo.sdno.localsiteservice.sbi.site.TemplateSbiService;
import org.openo.sdno.localsiteservice.sbi.site.VlanSbiService;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.inventory.sdk.model.RelationMO;
import org.openo.sdno.overlayvpn.inventory.sdk.model.RelationPuerMO;
import org.openo.sdno.overlayvpn.inventory.sdk.util.InventoryDaoUtil;
import org.openo.sdno.overlayvpn.model.common.enums.ActionStatus;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.SbiIfVlan;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of VLAN Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Service
public class VlanServiceImpl implements VlanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VlanServiceImpl.class);

    private static final int DEFAULT_VLAN_ID = 1;

    private static final String ACTION_STATE_FIELD = "actionState";

    @Autowired
    private VlanSbiService sbiService;

    @Autowired
    private SiteModelDao siteModelDao;

    @Autowired
    private TemplateSbiService templateSbiSrevice;

    @Autowired
    private BaseResourceDao baseResourceDao;

    @Autowired
    private LogicalTernminationPointInvDao ltpInvDao;

    @Override
    public ResultRsp<NbiVlanModel> query(HttpServletRequest req, String vlanUuid) throws ServiceException {
        InventoryDao<NbiVlanModel> vlanInvDao = new InventoryDaoUtil<NbiVlanModel>().getInventoryDao();
        ResultRsp<NbiVlanModel> queryResultRsp = vlanInvDao.query(NbiVlanModel.class, vlanUuid, null);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("Query Vlan Model Data failed");
            throw new ServiceException("Query Vlan Model Data failed");
        }

        NbiVlanModel vlanModel = queryResultRsp.getData();
        fillVlanModelPortsInfo(vlanModel);

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, vlanModel);
    }

    @Override
    public ComplexResult<NbiVlanModel> batchQuery(String name, String tenantId, String siteId, String pageNum,
            String pageSize) throws ServiceException {
        InventoryDao<NbiVlanModel> vlanInvDao = new InventoryDaoUtil<NbiVlanModel>().getInventoryDao();

        Map<String, Object> filterMap = new HashMap<>();
        if(StringUtils.hasLength(name)) {
            filterMap.put("name", Arrays.asList(name));
        }

        if(StringUtils.hasLength(tenantId)) {
            filterMap.put("tenantId", Arrays.asList(tenantId));
        }

        if(StringUtils.hasLength(siteId)) {
            filterMap.put("siteId", Arrays.asList(siteId));
        }

        ResultRsp<List<NbiVlanModel>> resultRsp =
                vlanInvDao.queryByFilter(NbiVlanModel.class, JsonUtil.toJson(filterMap), null);
        if(!resultRsp.isValid()) {
            LOGGER.error("VlanModel bath query failed");
            throw new ServiceException("VlanModel bath query failed");
        }

        List<NbiVlanModel> resultVlanModelList = resultRsp.getData();

        for(NbiVlanModel vlanModel : resultVlanModelList) {
            fillVlanModelPortsInfo(vlanModel);
        }

        return new ComplexResult<>(resultVlanModelList.size(), 0, resultVlanModelList.size(),
                resultVlanModelList);
    }

    @Override
    public ResultRsp<NbiVlanModel> create(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException {

        List<String> portUuids = vlanModel.getPorts();
        String siteId = vlanModel.getSiteId();

        // Query LocalCpe Network Element
        NetworkElementMO localCPE = siteModelDao.getSiteLocalCpe(siteId);

        List<LogicalTernminationPointMO> ltpMOList;

        // Query Related Ports
        if(CollectionUtils.isEmpty(portUuids)) {
            ltpMOList = queryLtpMOList(siteId, localCPE.getId());
            @SuppressWarnings("unchecked")
            List<String> ltpMOUuids = new ArrayList<>(CollectionUtils.collect(ltpMOList, new Transformer() {

                @Override
                public Object transform(Object arg0) {
                    return ((LogicalTernminationPointMO)arg0).getId();
                }
            }));
            vlanModel.setPorts(ltpMOUuids);
        } else {
            ltpMOList = baseResourceDao.queryLtpByUuids(portUuids);
        }

        // Insert VlanModel data to database
        vlanModel.setActionState(ActionStatus.CREATING.getName());
        ResultRsp<NbiVlanModel> insertResultRsp = (new ModelDataDao<NbiVlanModel>()).insert(vlanModel);
        if(!insertResultRsp.isSuccess()) {
            LOGGER.error("Insert VlanModel failed");
            throw new ServiceException("Insert VlanModel failed");
        }

        for(String portUuid : vlanModel.getPorts()) {
            RelationMO relationMO = new RelationMO(NbiVlanModel.class, LogicalTernminationPointMO.class,
                    vlanModel.getUuid(), portUuid, RelationMO.ASSOCIATION_TYPE, null);
            ResultRsp<NbiVlanModel> relationResultRsp = (new ModelDataDao<NbiVlanModel>()).addRelation(relationMO);
            if(!relationResultRsp.isSuccess()) {
                LOGGER.error("RelationMO add failed");
                return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
            }
        }

        // Construct SbiIfVlan Data
        List<SbiIfVlan> ifVlanList = constructIfVlanData(ltpMOList, vlanModel);

        // Insert SbiIfVlan to database
        ResultRsp<List<SbiIfVlan>> insertSbiIfVlanResultRsp = (new ModelDataDao<SbiIfVlan>()).batchInsert(ifVlanList);
        if(!insertSbiIfVlanResultRsp.isSuccess()) {
            LOGGER.error("Insert SbiIfVlan failed");
            throw new ServiceException("Insert SbiIfVlan failed");
        }

        // Create SbiIfVlan in Driver
        ResultRsp<List<SbiIfVlan>> createResultRsp =
                sbiService.create(localCPE.getControllerID().get(0), localCPE.getNativeID(), ifVlanList);
        if(!createResultRsp.isValid()) {
            LOGGER.error("Create SbiIfVlan failed");
            throw new ServiceException("Create SbiIfVlan failed");
        }

        // Update SbiIfVlan in database
        ResultRsp<List<SbiIfVlan>> updateResultRsp = (new ModelDataDao<SbiIfVlan>()).batchUpdate(SbiIfVlan.class,
                createResultRsp.getData(), "ethInterfaceConfigId");
        if(!updateResultRsp.isValid()) {
            LOGGER.error("Update SbiIfVlan failed");
            throw new ServiceException("Update SbiIfVlan failed");
        }

        // Update Status of Vlan Model
        vlanModel.setActionState(ActionStatus.NORMAL.getName());
        ResultRsp<NbiVlanModel> updateVlanResultRsp =
                (new ModelDataDao<NbiVlanModel>()).update(vlanModel, ACTION_STATE_FIELD);
        if(!updateVlanResultRsp.isValid()) {
            LOGGER.error("Update Vlan Model failed");
            throw new ServiceException("Update Vlan Model failed");
        }

        return updateVlanResultRsp;
    }

    @Override
    public ResultRsp<NbiVlanModel> delete(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException {

        if(ActionStatus.CREATING.getName().equals(vlanModel.getActionState())) {
            LOGGER.error("This  Vlan Model is creating, can not be deleted");
            throw new ServiceException("This  Vlan Model is creating, can not be deleted");
        }

        vlanModel.setActionState(ActionStatus.DELETING.getName());
        ResultRsp<NbiVlanModel> updateStatusResultRsp =
                (new ModelDataDao<NbiVlanModel>()).update(vlanModel, ACTION_STATE_FIELD);
        if(!updateStatusResultRsp.isValid()) {
            LOGGER.error("Update Vlan Model Status failed");
            throw new ServiceException("Update Vlan Model Status failed");
        }

        String siteId = vlanModel.getSiteId();

        // Query LocalCpe Network Element
        NetworkElementMO localCPE = siteModelDao.getSiteLocalCpe(siteId);

        // Query Related SbiIfVlan Data
        ResultRsp<List<SbiIfVlan>> queryResultRsp = queryIfVlanByVlanId(vlanModel.getUuid());
        if(!queryResultRsp.isValid()) {
            LOGGER.error("SbiIfVlan data query failed");
            throw new ServiceException("SbiIfVlan data query failed");
        }

        // Construct Delete SbiIfVlanData
        List<SbiIfVlan> ifVlanList = queryResultRsp.getData();
        for(SbiIfVlan curSbiIfVlan : ifVlanList) {
            curSbiIfVlan.setDefaultVlan(DEFAULT_VLAN_ID);
            curSbiIfVlan.setVlans(String.valueOf(DEFAULT_VLAN_ID));
        }

        // Delete SbiIfVlan from AC
        ResultRsp<List<SbiIfVlan>> deleteResultRsp =
                sbiService.update(localCPE.getControllerID().get(0), localCPE.getNativeID(), ifVlanList);
        if(!deleteResultRsp.isSuccess()) {
            LOGGER.error("SbiIfVlan Delete from AC failed");
            throw new ServiceException("SbiIfVlan Delete from AC failed");
        }

        // Delete SbiIfVlan Data
        ResultRsp<String> deleteSbiIfVlanResultRsp =
                (new ModelDataDao<SbiIfVlan>()).batchDelete(SbiIfVlan.class, ifVlanList);
        if(!deleteSbiIfVlanResultRsp.isSuccess()) {
            LOGGER.error("SbiIfVlan Delete failed");
            throw new ServiceException("SbiIfVlan Delete failed");
        }

        // Delete VlanMode data
        ResultRsp<String> deleteVlanModelResultRsp =
                (new ModelDataDao<NbiVlanModel>()).delete(NbiVlanModel.class, vlanModel.getUuid());
        if(!deleteVlanModelResultRsp.isSuccess()) {
            LOGGER.error("VlanModel Delete failed");
            throw new ServiceException("VlanModel Delete failed");
        }

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, vlanModel);
    }

    @Override
    public ResultRsp<NbiVlanModel> update(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException {

        vlanModel.setActionState(ActionStatus.UPDATING.getName());
        (new ModelDataDao<NbiVlanModel>()).update(vlanModel, "actionState,name,siteId,vlanId");

        try {
            ResultRsp<List<SbiIfVlan>> ifVlanQueryResultRsp = queryIfVlanByVlanId(vlanModel.getUuid());
            if(!ifVlanQueryResultRsp.isValid()) {
                LOGGER.error("SbiIfVlan query failed or does not exist");
                throw new ServiceException("SbiIfVlan query failed or does not exist");
            }

            NetworkElementMO localCpe = siteModelDao.getSiteLocalCpe(vlanModel.getSiteId());
            if(null == localCpe) {
                LOGGER.error("Current Site do not exist LocalCpe");
                throw new ServiceException("Current Site do not exist LocalCpe");
            }

            List<SbiIfVlan> ifVlanList = setVlanToIfVlans(ifVlanQueryResultRsp.getData(), vlanModel);
            ResultRsp<List<SbiIfVlan>> updateResultRsp =
                    sbiService.update(localCpe.getControllerID().get(0), localCpe.getNativeID(), ifVlanList);
            if(!updateResultRsp.isValid()) {
                LOGGER.error("Update SbiIfVlan failed");
                throw new ServiceException("Update SbiIfVlan failed");
            }

            ResultRsp<List<SbiIfVlan>> ifVlanUpdateResultRsp =
                    (new ModelDataDao<SbiIfVlan>()).batchUpdate(SbiIfVlan.class, ifVlanList, "defaultVlan,vlans");
            if(!ifVlanUpdateResultRsp.isValid()) {
                LOGGER.error("Update SbiIfVlan in Database failed");
                throw new ServiceException("Update SbiIfVlan in Database failed");
            }
        } catch(ServiceException e) {
            vlanModel.setActionState(ActionStatus.UPDATE_EXCEPTION.getName());
            (new ModelDataDao<NbiVlanModel>()).update(vlanModel, ACTION_STATE_FIELD);
            throw e;
        }

        vlanModel.setActionState(ActionStatus.NORMAL.getName());
        return (new ModelDataDao<NbiVlanModel>()).update(vlanModel, ACTION_STATE_FIELD);
    }

    private ResultRsp<List<SbiIfVlan>> queryIfVlanByVlanId(String vlanUuid) throws ServiceException {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("serviceVlanUuId", Arrays.asList(vlanUuid));

        ResultRsp<List<SbiIfVlan>> queryResultRsp =
                (new ModelDataDao<SbiIfVlan>()).queryByFilter(SbiIfVlan.class, filterMap, null);
        if(!queryResultRsp.isValid()) {
            LOGGER.error("IfVlan query failed");
            return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return queryResultRsp;
    }

    /**
     * Query Ltp by Site Id.<br>
     * 
     * @param siteId Site Uuid
     * @param neId Network Element Id
     * @return LtpMO queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    private List<LogicalTernminationPointMO> queryLtpMOList(String siteId, String neId) throws ServiceException {

        List<LogicalTernminationPointMO> ltpMOList = new ArrayList<>();

        String templateName = siteModelDao.getTemplateName(siteId);
        String localCpeType = siteModelDao.getLocalCpeType(siteId);

        List<String> lanInfNameList = templateSbiSrevice.getLanPortList(templateName, localCpeType);
        for(String lanInfName : lanInfNameList) {
            Map<String, String> conditionMap = new HashMap<>();
            conditionMap.put("name", lanInfName);
            conditionMap.put("meID", neId);
            List<LogicalTernminationPointMO> curLtpMOList = ltpInvDao.query(conditionMap);
            if(CollectionUtils.isNotEmpty(curLtpMOList)) {
                ltpMOList.addAll(curLtpMOList);
            }
        }

        return ltpMOList;
    }

    /**
     * Construct Interface Vlan Data.<br>
     * 
     * @param ltpMOList List of LtpMO
     * @param vlanModel Vlan Model data
     * @return List of SbiIfVlan constructed
     * @throws ServiceException when construct SbiIfVlan failed
     * @since SDNO 0.5
     */
    public static List<SbiIfVlan> constructIfVlanData(List<LogicalTernminationPointMO> ltpMOList,
            NbiVlanModel vlanModel) throws ServiceException {
        List<SbiIfVlan> ifVlanList = new ArrayList<>(ltpMOList.size());

        for(LogicalTernminationPointMO curLtpMO : ltpMOList) {
            SbiIfVlan ifVlan = new SbiIfVlan();
            ifVlan.allocateUuid();
            ifVlan.setIfId(curLtpMO.getId());
            ifVlan.setIfName(curLtpMO.getName());
            ifVlan.setServiceVlanUuId(vlanModel.getUuid());
            ifVlan.setDefaultVlan(vlanModel.getVlanId());
            ifVlan.setVlans(String.valueOf(vlanModel.getVlanId()));
            ifVlanList.add(ifVlan);
        }

        return ifVlanList;
    }

    /**
     * Set NbiVlanModel data to Interface Vlan data.<br>
     * 
     * @param ifVlanList Interface Vlan List
     * @param vlanModel NbiVlanModel data
     * @return List of SbiIfVlan data
     * @since SDNO 0.5
     */
    private List<SbiIfVlan> setVlanToIfVlans(List<SbiIfVlan> ifVlanList, NbiVlanModel vlanModel) {
        List<SbiIfVlan> resultSbiIfVlanList = new ArrayList<>();
        List<String> portIds = vlanModel.getPorts();

        for(String portId : portIds) {
            for(SbiIfVlan ifVlan : ifVlanList) {
                if(portId.equals(ifVlan.getIfId())) {
                    ifVlan.setDefaultVlan(vlanModel.getVlanId());
                    ifVlan.setVlans(String.valueOf(vlanModel.getVlanId()));
                    resultSbiIfVlanList.add(ifVlan);
                }
            }
        }

        return resultSbiIfVlanList;
    }

    /**
     * Check whether SbiIfVlan and NbiVlanModel is the same vlan.<br>
     * 
     * @param ifVlanList List of InfVlan
     * @param vlanModel VlanMode data
     * @return true if is the same vlan,false otherwise
     * @since SDNO 0.5
     */
    public static boolean isSameVlan(List<SbiIfVlan> ifVlanList, NbiVlanModel vlanModel) {
        for(SbiIfVlan ifVlan : ifVlanList) {
            if(ifVlan.getDefaultVlan() != vlanModel.getVlanId()) {
                return false;
            }
        }
        return true;
    }

    private void fillVlanModelPortsInfo(NbiVlanModel vlanModel) throws ServiceException {
        if(null == vlanModel) {
            return;
        }

        InventoryDao<NbiVlanModel> vlanDao = new InventoryDaoUtil<NbiVlanModel>().getInventoryDao();
        RelationMO relation = new RelationMO(NbiVlanModel.class, LogicalTernminationPointMO.class, vlanModel.getUuid(),
                null, RelationMO.ASSOCIATION_TYPE, null);
        ResultRsp<List<RelationPuerMO>> relationResultRsp = vlanDao.queryByRelation(relation);
        if(!relationResultRsp.isSuccess()) {
            LOGGER.error("Relation query failed");
            throw new ServiceException("Relation query failed");
        }

        List<String> portIds = new ArrayList<>();
        for(RelationPuerMO relationMO : relationResultRsp.getData()) {
            portIds.add(relationMO.getDstUuid());
        }

        if(CollectionUtils.isNotEmpty(portIds)) {
            vlanModel.setPorts(portIds);
        }
    }
}
