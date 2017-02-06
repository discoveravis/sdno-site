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

package org.openo.sdno.localsiteservice.sbi.cpe;

import java.util.Arrays;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.cpe.SbiDeviceCreateBasicInfo;
import org.openo.sdno.overlayvpn.model.v2.cpe.SbiDeviceInfo;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of CpeOnline.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class CpeOnlineSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpeOnlineSbiService.class);

    private static final String DEVICE_BASIC_URL = "/openoapi/sbi-localsite/v1/devices";

    /**
     * Create Device In Adapter.<br>
     * 
     * @param ctrlUuid Controller Uuid
     * @param deviceCreateInfo Device create model
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiDeviceInfo> create(String ctrlUuid, SbiDeviceCreateBasicInfo deviceCreateInfo)
            throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        restfulParameters.setRawData(JsonUtil.toJson(Arrays.asList(deviceCreateInfo)));

        RestfulResponse response = RestfulProxy.post(DEVICE_BASIC_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create device failed");
            return new ResultRsp<SbiDeviceInfo>(ErrorCode.OVERLAYVPN_FAILED);
        }

        ResultRsp<SbiDeviceInfo> resultRsp =
                JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiDeviceInfo>>() {});

        return resultRsp;
    }

    /**
     * Delete Device In Adapter.<br>
     * 
     * @param ctrlUuid Controller Uuid
     * @param deviceId Device Id
     * @return delete result
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<String> delete(String ctrlUuid, String deviceId) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, ctrlUuid);
        restfulParameters.put("deviceIds", JsonUtil.toJson(Arrays.asList(deviceId)));

        RestfulResponse response = RestfulProxy.delete(DEVICE_BASIC_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete device failed");
            return new ResultRsp<String>(ErrorCode.OVERLAYVPN_FAILED);
        }

        ResultRsp<String> resultRsp =
                JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<String>>() {});

        return resultRsp;
    }

}
