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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.localsiteservice.dao.BaseResourceDao;
import org.openo.sdno.localsiteservice.inf.cpe.CpeOnlineService;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.sbi.cpe.CpeOnlineSbiService;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.common.enums.AdminStatus;
import org.openo.sdno.overlayvpn.model.common.enums.OperStatus;
import org.openo.sdno.overlayvpn.model.v2.cpe.SbiDeviceCreateBasicInfo;
import org.openo.sdno.overlayvpn.model.v2.cpe.SbiDeviceInfo;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of CpeOnline Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Service
public class CpeOnlineServiceImpl implements CpeOnlineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpeOnlineServiceImpl.class);

    @Autowired
    private CpeOnlineSbiService sbiService;

    @Autowired
    private BaseResourceDao baseResourceDao;

    @Autowired
    private NetworkElementInvDao networkElementInvDao;

    @Override
    public ResultRsp<VCpePlanInfo> create(HttpServletRequest req, VCpePlanInfo cpePlanInfo) throws ServiceException {

        // Check whether this device exist
        NetworkElementMO cpeNetworkElement = baseResourceDao.queryNeByEsn(cpePlanInfo.getEsn());
        if(null == cpeNetworkElement) {
            // Insert new cpe into database
            cpeNetworkElement = convertCpePlanInfoToNe(cpePlanInfo);
            cpeNetworkElement.setAdminState(AdminStatus.INACTIVE.getName());
            cpeNetworkElement.setOperState(OperStatus.DOWN.getName());
            networkElementInvDao.addMO(cpeNetworkElement);
        }

        SbiDeviceCreateBasicInfo createBasicInfo = convertCpePlanInfoToDeviceCreateInfo(cpePlanInfo);

        // Create device in driver
        String controllerId = cpePlanInfo.getControllerId();
        ResultRsp<SbiDeviceInfo> resultRsp = sbiService.create(controllerId, createBasicInfo);
        if(!resultRsp.isValid()) {
            LOGGER.error("Create Device failed");
            return new ResultRsp<VCpePlanInfo>(ErrorCode.OVERLAYVPN_FAILED);
        }

        // Update cpe in database
        cpeNetworkElement.setNativeID(resultRsp.getData().getId());
        cpeNetworkElement.setAdminState(AdminStatus.ACTIVE.getName());
        cpeNetworkElement.setOperState(OperStatus.UP.getName());
        networkElementInvDao.updateMO(cpeNetworkElement);

        return new ResultRsp<VCpePlanInfo>(ErrorCode.OVERLAYVPN_SUCCESS, cpePlanInfo);
    }

    @Override
    public void delete(HttpServletRequest req, List<String> uuids) throws ServiceException {
        for(String uuid : uuids) {

            // Check Uuid
            CheckStrUtil.checkUuidStr(uuid);

            NetworkElementMO neMO = networkElementInvDao.query(uuid);
            if(null == neMO) {
                LOGGER.warn("This Device does not exist");
                continue;
            }

            // Delete in Adapter
            ResultRsp<String> resultRsp = sbiService.delete(neMO.getControllerID().get(0), neMO.getNativeID());
            if(!resultRsp.isSuccess()) {
                LOGGER.error("Device delete failed");
                throw new ServiceException("Device delete failed");
            }

            networkElementInvDao.deleteMO(uuid);
        }
    }

    private NetworkElementMO convertCpePlanInfoToNe(VCpePlanInfo cpePlanInfo) {
        NetworkElementMO neMO = new NetworkElementMO();
        if(StringUtils.hasLength(cpePlanInfo.getName())) {
            neMO.setName(cpePlanInfo.getName());
        } else {
            neMO.setName(cpePlanInfo.getEsn());
        }

        if(StringUtils.hasLength(cpePlanInfo.getUuid())) {
            neMO.setId(cpePlanInfo.getUuid());
        } else {
            neMO.setId(UuidUtils.createUuid());
        }

        neMO.setSiteID(Arrays.asList(cpePlanInfo.getSiteId()));
        neMO.setControllerID(Arrays.asList(cpePlanInfo.getControllerId()));
        neMO.setSerialNumber(cpePlanInfo.getEsn());
        neMO.setNeRole(cpePlanInfo.getRole());
        neMO.setPopID(cpePlanInfo.getPopId());

        return neMO;
    }

    private SbiDeviceCreateBasicInfo convertCpePlanInfoToDeviceCreateInfo(VCpePlanInfo cpePlanInfo) {
        SbiDeviceCreateBasicInfo createBasicInfo = new SbiDeviceCreateBasicInfo();
        createBasicInfo.setEsn(cpePlanInfo.getEsn());
        createBasicInfo.setName(cpePlanInfo.getName());
        return createBasicInfo;
    }

}
