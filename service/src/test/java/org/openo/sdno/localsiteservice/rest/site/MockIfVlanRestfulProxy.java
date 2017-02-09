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

import java.util.Arrays;
import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.vlan.SbiIfVlan;
import org.openo.sdno.overlayvpn.result.ResultRsp;

import mockit.Mock;
import mockit.MockUp;

public class MockIfVlanRestfulProxy extends MockUp<RestfulProxy> {

    private static RestfulResponse restfulResponse = new RestfulResponse();
    static {
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
        SbiIfVlan infVlanModel = new SbiIfVlan();
        infVlanModel.setUuid(UuidUtils.createUuid());
        ResultRsp<List<SbiIfVlan>> resultRsp = new ResultRsp<List<SbiIfVlan>>(ErrorCode.OVERLAYVPN_SUCCESS);
        resultRsp.setData(Arrays.asList(infVlanModel));
        restfulResponse.setResponseJson(JsonUtil.toJson(resultRsp));
    }

    @Mock
    public static RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {
        return restfulResponse;
    }

    @Mock
    public static RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {
        return restfulResponse;
    }

    @Mock
    public static RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {
        return restfulResponse;
    }

}
