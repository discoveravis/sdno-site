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

package org.openo.sdno.localsiteservice.rest.site;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;

import mockit.Mock;
import mockit.MockUp;

public class MockSiteRestfulProxy extends MockUp<RestfulProxy> {

    private static RestfulResponse restfulResponse = new RestfulResponse();
    static {
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
    }

    @Mock
    public static RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {

        if("/openoapi/sdnolocalsite/v1/subnets".equals(uri)) {
            ComplexResult<NbiSubnetModel> subnetResult = new ComplexResult<NbiSubnetModel>();
            subnetResult.setTotal(0);
            restfulResponse.setResponseJson(JsonUtil.toJson(subnetResult));
        } else if("/openoapi/sdnolocalsite/v1/vlans".equals(uri)) {
            ComplexResult<NbiVlanModel> vlanResult = new ComplexResult<NbiVlanModel>();
            vlanResult.setTotal(0);
            restfulResponse.setResponseJson(JsonUtil.toJson(vlanResult));
        } else if("/openoapi/sdnolocalsite/v1/route-entries".equals(uri)) {
            ComplexResult<NbiRouteEntryModel> routeEntryResult = new ComplexResult<NbiRouteEntryModel>();
            routeEntryResult.setTotal(0);
            restfulResponse.setResponseJson(JsonUtil.toJson(routeEntryResult));
        } else if("/openoapi/sdnolocalsite/v1/internet-gateways".equals(uri)) {
            ComplexResult<NbiInternetGatewayModel> intGatewayResult = new ComplexResult<NbiInternetGatewayModel>();
            intGatewayResult.setTotal(0);
            restfulResponse.setResponseJson(JsonUtil.toJson(intGatewayResult));
        }

        return restfulResponse;
    }

}
