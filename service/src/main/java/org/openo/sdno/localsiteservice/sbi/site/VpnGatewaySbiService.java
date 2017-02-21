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

import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.model.vpn.VpnGateway;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of Vpn Gateway<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class VpnGatewaySbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpnGatewaySbiService.class);

    private static final String VPN_GATEWAY_QUERY_URL = "/openoapi/sdnooverlay/v1/vpn-gateways";

    /**
     * Query Vpn Gateway.<br>
     * 
     * @param siteId Site Uuid
     * @return List of VpnGateway queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<VpnGateway> queryVpnGateway(String siteId) throws ServiceException {

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        restfulParameters.put("siteId", siteId);
        RestfulResponse response = RestfulProxy.get(VPN_GATEWAY_QUERY_URL, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query VpnGateway failed");
            throw new ServiceException("Query VpnGateway failed");
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ComplexVpnResult<VpnGateway>>() {})
                .getObjects();
    }

}
