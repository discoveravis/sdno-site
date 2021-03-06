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

package org.openo.sdno.localsiteservice.rest.cpe;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

public class MockCpeRestfulProxy extends MockUp<RestfulProxy> {

    private static RestfulResponse restfulResponse = new RestfulResponse();
    static {
        restfulResponse.setStatus(HttpCode.RESPOND_OK);
    }

    @Mock
    public static RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {
        Map<String, String> resultMap =  new HashMap<String, String>();
        resultMap.put("id", "CpeId");
        restfulResponse.setResponseJson(JsonUtil.toJson(resultMap));
        return restfulResponse;
    }

    @Mock
    public static RestfulResponse delete(String uri, RestfulParametes restParametes) throws ServiceException {
        return restfulResponse;
    }

}
