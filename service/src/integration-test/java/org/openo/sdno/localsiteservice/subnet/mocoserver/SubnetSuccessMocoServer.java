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

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class SubnetSuccessMocoServer extends MocoHttpServer {

    private static final String CREATE_SUBNET_MOCOFILE =
            "src/integration-test/resources/subnet/mocodata/createsubnet.json";

    private static final String DELETE_SUBNET_MOCOFILE =
            "src/integration-test/resources/subnet/mocodata/deletesubnet.json";

    private static final String UPDATE_SUBNET_MOCOFILE =
            "src/integration-test/resources/subnet/mocodata/updatesubnet.json";

    private static final String QUERY_BDIF_MOCOFILE = "src/integration-test/resources/subnet/mocodata/querybdif.json";

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(CREATE_SUBNET_MOCOFILE, new SubnetCreateResponseHandler());
        this.addRequestResponsePair(DELETE_SUBNET_MOCOFILE);
        this.addRequestResponsePair(QUERY_BDIF_MOCOFILE);
        this.addRequestResponsePair(UPDATE_SUBNET_MOCOFILE, new SubnetUpdateResponseHandler());
    }

}
