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

package org.openo.sdno.localsiteservice.subnet.mocoserver;

import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.moco.responsehandler.MocoResponseHandler;

public class SubnetCreateResponseHandler extends MocoResponseHandler {

    @Override
    public void processRequestandResponse(HttpRquestResponse httpObject) {
        HttpRequest httpRequest = httpObject.getRequest();
        HttpResponse httpResponse = httpObject.getResponse();
        SbiSubnetNetModel inputSubnetNetModel = JsonUtil.fromJson(httpRequest.getData(), SbiSubnetNetModel.class);
        inputSubnetNetModel.setNetworkId("SubnetNetId");
        ResultRsp<SbiSubnetNetModel> successResultRsp =
                new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_SUCCESS, inputSubnetNetModel);
        httpResponse.setData(JsonUtil.toJson(successResultRsp));
    }
}
