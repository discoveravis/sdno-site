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

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of SNat.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class SNatSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SNatSbiService.class);

    private static final String SNAT_BASE_URL = "/openoapi/sbi-localsite/v1/device/{0}/snat";

    private static final String SNAT_OPERATION_URL = "/openoapi/sbi-localsite/v1/device/{0}/snat/{1}/acl/{2}";

    /**
     * Create Snat Model.<br>
     * 
     * @param sNatNetModel SbiSnatNetModel need to create
     * @return SbiSnatNetModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSnatNetModel> create(SbiSnatNetModel sNatNetModel) throws ServiceException {

        String controllerId = sNatNetModel.getControllerId();
        String deviceId = sNatNetModel.getDeviceId();

        if(StringUtils.isBlank(controllerId) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller Uuid or Device Uuid is invalid");
            return new ResultRsp<SbiSnatNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String createUrl = MessageFormat.format(SNAT_BASE_URL, deviceId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        restfulParameters.setRawData(JsonUtil.toJson(sNatNetModel));
        RestfulResponse response = RestfulProxy.post(createUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create Snat in Driver failed");
            return new ResultRsp<SbiSnatNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiSnatNetModel>>() {});
    }

    /**
     * Delete Snat Model.<br>
     * 
     * @param sNatNetModel SbiSnatNetModel need to delete
     * @return delete result
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<String> delete(SbiSnatNetModel sNatNetModel) throws ServiceException {

        String controllerId = sNatNetModel.getControllerId();
        String deviceId = sNatNetModel.getDeviceId();

        if(StringUtils.isBlank(controllerId) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller Uuid or Device Uuid is invalid");
            return new ResultRsp<String>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String deleteUrl =
                MessageFormat.format(SNAT_OPERATION_URL, deviceId, sNatNetModel.getNatId(), sNatNetModel.getAclId());

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        RestfulResponse response = RestfulProxy.delete(deleteUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Snat Model failed");
            return new ResultRsp<String>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<String>>() {});
    }

}
