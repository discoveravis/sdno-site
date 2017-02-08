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

package org.openo.sdno.localsiteservice.msbmanager;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.restclient.HttpRestClient;

public class MsbRegisterManager {

    private static final String MSB_REGISTER_FILE = "src/integration-test/resources/msb/registeroverlaymsb.json";

    private static final String MSB_UNREGISTER_FILE = "src/integration-test/resources/msb/unregisteroverlaymsb.json";

    private static final HttpRestClient restClient = new HttpRestClient();

    public static void registerOverlayMsb() throws ServiceException {

        HttpRequest httpRegisterRequest =
                HttpModelUtils.praseHttpRquestResponseFromFile(MSB_REGISTER_FILE).getRequest();
        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.setRawData(httpRegisterRequest.getData());
        restfulParametes.setHeaderMap(httpRegisterRequest.getHeaders());
        RestfulResponse registerReponse = restClient.post(httpRegisterRequest.getUri(), restfulParametes);
        if(!HttpCode.isSucess(registerReponse.getStatus())) {
            throw new ServiceException("Register Msb failed");
        }

    }

    public static void unRegisterOverlayMsb() throws ServiceException {

        HttpRequest httpUnRegisterRequest =
                HttpModelUtils.praseHttpRquestResponseFromFile(MSB_UNREGISTER_FILE).getRequest();
        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.setHeaderMap(httpUnRegisterRequest.getHeaders());
        RestfulResponse unRegisterReponse = restClient.delete(httpUnRegisterRequest.getUri(), new RestfulParametes());
        if(!HttpCode.isSucess(unRegisterReponse.getStatus())) {
            throw new ServiceException("UnRegister Msb failed");
        }

    }
}
