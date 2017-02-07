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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.BaseResourceDao;
import org.openo.sdno.localsiteservice.inf.cpe.LocalCpeService;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.sbi.cpe.CpeSbiService;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.cpe.CpeRoleType;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of LocalCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Service
public class LocalCpeServiceImpl implements LocalCpeService {

    @Autowired
    private NetworkElementInvDao neInvDao;

    @Autowired
    private CpeSbiService sbiService;

    @Autowired
    private BaseResourceDao baseResourceDao;

    @Override
    public ResultRsp<NetworkElementMO> query(HttpServletRequest req, String localCpeUuid) throws ServiceException {
        NetworkElementMO networkElement = neInvDao.query(localCpeUuid);
        return new ResultRsp<NetworkElementMO>(ErrorCode.OVERLAYVPN_SUCCESS, networkElement);
    }

    @Override
    public ResultRsp<List<NetworkElementMO>> batchQuery(String siteId, String cpeType, int pageNum, int pageSize)
            throws ServiceException {
        Map<String, String> filterMap = new HashMap<String, String>();
        if(0 != pageSize) {
            filterMap.put("pageSize", String.valueOf(pageSize));
            filterMap.put("pageNum", String.valueOf(pageNum));
        }

        if(StringUtils.hasLength(siteId)) {
            filterMap.put("siteID", siteId);
        }

        if(StringUtils.hasLength(cpeType)) {
            filterMap.put("productName", cpeType);
        }

        filterMap.put("neRole", CpeRoleType.THIN_CPE.getName());

        List<NetworkElementMO> networkElementList = neInvDao.query(filterMap);

        return new ResultRsp<List<NetworkElementMO>>(ErrorCode.OVERLAYVPN_SUCCESS, networkElementList);
    }

    @Override
    public ResultRsp<NetworkElementMO> create(HttpServletRequest req, NbiLocalCpeModel localCpeModel)
            throws ServiceException {

        VCpePlanInfo planInfo = new VCpePlanInfo(localCpeModel);
        sbiService.create(planInfo);

        NetworkElementMO neMO = baseResourceDao.queryNeByEsn(localCpeModel.getEsn());

        return new ResultRsp<NetworkElementMO>(ErrorCode.OVERLAYVPN_SUCCESS, neMO);
    }

    @Override
    public void delete(HttpServletRequest req, String localCpeUuid) throws ServiceException {
        sbiService.delete(localCpeUuid);
    }
}
