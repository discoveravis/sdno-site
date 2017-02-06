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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.model.vpn.InternalConnectionModel;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of Internal Connection.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class InternalConnectionSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalConnectionSbiService.class);

    private static final String INTERNALCONNECTION_BASE_URL = "/openoapi/sdnooverlay/v1/site-internal-connections";

    private static final String INTERNALCONNECTION_OPERATION_URL =
            "/openoapi/sdnooverlay/v1/site-internal-connections/{0}";

    /**
     * Create Internal Connection.<br>
     * 
     * @param internalConnection InternalConnection need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<InternalConnectionModel> create(InternalConnectionModel internalConnection)
            throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.setRawData(JsonUtil.toJson(internalConnection));
        RestfulResponse response = RestfulProxy.post(INTERNALCONNECTION_BASE_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create Internal Connection failed");
            return new ResultRsp<InternalConnectionModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        InternalConnectionModel internalConnectionRsp =
                JsonUtil.fromJson(response.getResponseContent(), InternalConnectionModel.class);

        return new ResultRsp<InternalConnectionModel>(ErrorCode.OVERLAYVPN_SUCCESS, internalConnectionRsp);
    }

    /**
     * Delete Internal Connection.<br>
     * 
     * @param internalConnectionUuid Uuid of Internal Connection need to delete
     * @return InternalConnectionModel deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<InternalConnectionModel> delete(String internalConnectionUuid) throws ServiceException {

        String deleteUrl = MessageFormat.format(INTERNALCONNECTION_OPERATION_URL, internalConnectionUuid);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulResponse response = RestfulProxy.delete(deleteUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete Internal Connection failed");
            return new ResultRsp<InternalConnectionModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        InternalConnectionModel internalConnectionRsp =
                JsonUtil.fromJson(response.getResponseContent(), InternalConnectionModel.class);

        return new ResultRsp<InternalConnectionModel>(ErrorCode.OVERLAYVPN_SUCCESS, internalConnectionRsp);
    }

}
