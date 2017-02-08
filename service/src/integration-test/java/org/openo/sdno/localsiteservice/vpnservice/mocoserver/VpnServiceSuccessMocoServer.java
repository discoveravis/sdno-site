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

package org.openo.sdno.localsiteservice.vpnservice.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class VpnServiceSuccessMocoServer extends MocoHttpServer {

    private static final String QUERY_VPNGATEWAY_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/queryvpngateway.json";

    private static final String QUERY_CLOUDCPEINFO_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/queryCloudCpeInfo.json";

    private static final String QUERY_GATEWAYPOLICY_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/queryGatewayPolicy.json";

    private static final String QUERY_THINCPEINFO_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/queryThinCpeInfo.json";

    private static final String CREATE_INTERNALCONNECTION_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/createinternalconnection.json";

    private static final String DELETE_INTERNALCONNECTION_MOCOFILE =
            "src/integration-test/resources/vpnservice/mocodata/deleteinternalconnection.json";

    private static final int MOCO_SERVER_PORT = 12308;

    public VpnServiceSuccessMocoServer() {
        super(MOCO_SERVER_PORT);
    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(QUERY_VPNGATEWAY_MOCOFILE);
        this.addRequestResponsePair(QUERY_CLOUDCPEINFO_MOCOFILE);
        this.addRequestResponsePair(QUERY_GATEWAYPOLICY_MOCOFILE);
        this.addRequestResponsePair(QUERY_THINCPEINFO_MOCOFILE);
        this.addRequestResponsePair(CREATE_INTERNALCONNECTION_MOCOFILE);
        this.addRequestResponsePair(DELETE_INTERNALCONNECTION_MOCOFILE);
    }

}
