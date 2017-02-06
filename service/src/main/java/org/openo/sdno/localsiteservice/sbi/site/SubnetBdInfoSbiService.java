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

package org.openo.sdno.localsiteservice.sbi.site;

import java.text.MessageFormat;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetBdInfoModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of SubnetBdInfo.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class SubnetBdInfoSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetSbiService.class);

    private static final String SUBNET_BDINFO_URL = "/openoapi/sbi-localsite/v1/device/{0}/bdinfo/vni/{1}";

    /**
     * Query Subnet BdInfo.<br>
     * 
     * @param subnetBdInfoModel SbiSubnetBdInfoModel need to query
     * @return SbiSubnetBdInfoModel queryied out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSubnetBdInfoModel> query(SbiSubnetBdInfoModel subnetBdInfoModel) throws ServiceException {

        String deviceId = subnetBdInfoModel.getDeviceId();
        String ctrlUuid = subnetBdInfoModel.getControllerId();
        String vni = subnetBdInfoModel.getVni();

        String queryUrl = MessageFormat.format(SUBNET_BDINFO_URL, deviceId, vni);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        RestfulResponse response = RestfulProxy.get(queryUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query BdInfo from driver failed");
            return new ResultRsp<SbiSubnetBdInfoModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(),
                new TypeReference<ResultRsp<SbiSubnetBdInfoModel>>() {});
    }

}
