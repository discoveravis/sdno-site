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
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of Cpe.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class CpeSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpeSbiService.class);

    private static final String CPE_ONLINE_URL = "/openoapi/sdnolocalsite/v1/cpes";

    /**
     * Create Cpe Device.<br>
     * 
     * @param vCpePlanInfo Cpe Plan Info data
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<Map<String, String>> create(VCpePlanInfo vCpePlanInfo) throws ServiceException {

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.setRawData(JsonUtil.toJson(vCpePlanInfo));

        RestfulResponse response = RestfulProxy.post(CPE_ONLINE_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create cpe failed");
            throw new ServiceException("Create cpe failed");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = JsonUtil.fromJson(response.getResponseContent(), Map.class);
        return new ResultRsp<Map<String, String>>(ErrorCode.OVERLAYVPN_SUCCESS, resultMap);
    }

    /**
     * Delete Cpe Device.<br>
     * 
     * @param cpeUuid Uuid of CpeModel which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public void delete(String cpeUuid) throws ServiceException {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("uuids", JsonUtil.toJson(Arrays.asList(cpeUuid)));

        RestfulResponse response = RestfulProxy.delete(CPE_ONLINE_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete cpe failed");
            throw new ServiceException("Delete cpe failed");
        }
    }

}
