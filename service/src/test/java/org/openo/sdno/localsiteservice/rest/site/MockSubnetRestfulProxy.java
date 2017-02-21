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

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetBdInfoModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

import mockit.Mock;
import mockit.MockUp;

public class MockSubnetRestfulProxy extends MockUp<RestfulProxy> {

    private static RestfulResponse subnetRestfulResponse = new RestfulResponse();
    static {
        subnetRestfulResponse.setStatus(HttpCode.RESPOND_OK);
        SbiSubnetNetModel subnetNetModel = new SbiSubnetNetModel();
        subnetNetModel.setUuid(UuidUtils.createUuid());
        ResultRsp<SbiSubnetNetModel> resultRsp = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, subnetNetModel);
        subnetRestfulResponse.setResponseJson(JsonUtil.toJson(resultRsp));
    }

    private static RestfulResponse bdInfoRestfulResponse = new RestfulResponse();
    static {
        bdInfoRestfulResponse.setStatus(HttpCode.RESPOND_OK);
        SbiSubnetBdInfoModel bdInfoModel = new SbiSubnetBdInfoModel();
        bdInfoModel.setBdId("13");
        bdInfoModel.setVbdifName("vbdif_13");
        ResultRsp<SbiSubnetBdInfoModel> resultRsp = new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, bdInfoModel);
        bdInfoRestfulResponse.setResponseJson(JsonUtil.toJson(resultRsp));
    }

    private static RestfulResponse templateRestfulResponse = new RestfulResponse();
    static {
        templateRestfulResponse.setStatus(HttpCode.RESPOND_OK);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("gwPosition", "cloudcpe");
        templateRestfulResponse.setResponseJson(JsonUtil.toJson(dataMap));
    }

    @Mock
    public static RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {
        return subnetRestfulResponse;
    }

    @Mock
    public static RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {
        if(uri.contains("bdinfo")) {
            return bdInfoRestfulResponse;
        } else if(uri.contains("template")) {
            return templateRestfulResponse;
        }
        return subnetRestfulResponse;
    }

    @Mock
    public static RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {
        return subnetRestfulResponse;
    }

    @Mock
    public static RestfulResponse delete(String uri, RestfulParametes restParametes) throws ServiceException {
        return subnetRestfulResponse;
    }
}
