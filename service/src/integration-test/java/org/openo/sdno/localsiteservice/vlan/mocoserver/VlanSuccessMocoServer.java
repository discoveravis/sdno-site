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

package org.openo.sdno.localsiteservice.vlan.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

public class VlanSuccessMocoServer extends MocoHttpServer {

    private static final String CREATE_VLAN_MOCOFILE = "src/integration-test/resources/vlan/mocodata/createvlan.json";

    private static final String UPDATE_VLAN_MOCOFILE = "src/integration-test/resources/vlan/mocodata/updatevlan.json";

    private static final String QUERY_VLAN_MOCOFILE = "src/integration-test/resources/vlan/mocodata/queryvlan.json";

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(CREATE_VLAN_MOCOFILE, new VlanResponseHandler());
        this.addRequestResponsePair(UPDATE_VLAN_MOCOFILE, new VlanResponseHandler());
        this.addRequestResponsePair(QUERY_VLAN_MOCOFILE);
    }

}
