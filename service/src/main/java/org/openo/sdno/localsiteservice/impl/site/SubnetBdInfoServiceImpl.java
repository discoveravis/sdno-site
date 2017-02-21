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

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.site.SubnetBdInfoService;
import org.openo.sdno.localsiteservice.sbi.site.SubnetBdInfoSbiService;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetBdInfoModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class of Subnet Bd Information.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Service
public class SubnetBdInfoServiceImpl implements SubnetBdInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetBdInfoServiceImpl.class);

    @Autowired
    private SubnetBdInfoSbiService sbiBdInfoService;

    @Autowired
    private NetworkElementInvDao neInvDao;

    @Override
    public String getSubnetBdInfo(String vni, String neId) throws ServiceException {
        if(StringUtils.isBlank(vni)) {
            LOGGER.error("subnet vni parameter is wrong");
            return null;
        }

        SbiSubnetBdInfoModel subnetBdInfoModel = new SbiSubnetBdInfoModel();
        subnetBdInfoModel.setNeId(neId);
        NetworkElementMO curNeDevice = neInvDao.query(neId);
        subnetBdInfoModel.setDeviceId(curNeDevice.getNativeID());
        subnetBdInfoModel.setVni(vni);
        subnetBdInfoModel.setControllerId(curNeDevice.getControllerID().get(0));

        ResultRsp<SbiSubnetBdInfoModel> queryResultRsp = sbiBdInfoService.query(subnetBdInfoModel);
        if(!queryResultRsp.isValid()) {
            LOGGER.error("SbiSubnetBdInfoModel query failed");
            throw new ServiceException("SbiSubnetBdInfoModel query failed");
        }

        return queryResultRsp.getData().getVbdifName();
    }

}
