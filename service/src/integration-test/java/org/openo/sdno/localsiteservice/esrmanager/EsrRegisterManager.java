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

package org.openo.sdno.localsiteservice.esrmanager;

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.restclient.HttpRestClient;

public class EsrRegisterManager {

    private static final String ESR_REGISTER_FILE = "src/integration-test/resources/esr/registeresr.json";

    private static final String ESR_UNREGISTER_FILE = "src/integration-test/resources/esr/unregisteresr.json";

    private static String sdnControllerId = null;

    private static final HttpRestClient restClient = new HttpRestClient();

    public static void registerEsr() throws ServiceException {

        HttpRequest httpRegisterRequest =
                HttpModelUtils.praseHttpRquestResponseFromFile(ESR_REGISTER_FILE).getRequest();
        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.setRawData(httpRegisterRequest.getData());
        restfulParametes.setHeaderMap(httpRegisterRequest.getHeaders());
        RestfulResponse registerReponse = restClient.post(httpRegisterRequest.getUri(), restfulParametes);
        if(!HttpCode.isSucess(registerReponse.getStatus())) {
            throw new ServiceException("Register Esr failed");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = JsonUtil.fromJson(registerReponse.getResponseContent(), Map.class);
        sdnControllerId = (String)resultMap.get("sdnControllerId");
    }

    public static String getControllerId() {
        return sdnControllerId;
    }

    public static void unRegisterEsr() throws ServiceException {

        HttpRequest httpUnRegisterRequest =
                HttpModelUtils.praseHttpRquestResponseFromFile(ESR_UNREGISTER_FILE).getRequest();
        RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.setHeaderMap(httpUnRegisterRequest.getHeaders());

        httpUnRegisterRequest
                .setUri(PathReplace.replaceUuid("objectId", httpUnRegisterRequest.getUri(), sdnControllerId));

        RestfulResponse unRegisterReponse = restClient.delete(httpUnRegisterRequest.getUri(), new RestfulParametes());
        if(!HttpCode.isSucess(unRegisterReponse.getStatus())) {
            throw new ServiceException("UnRegister Esr failed");
        }

        sdnControllerId = null;
    }
}
