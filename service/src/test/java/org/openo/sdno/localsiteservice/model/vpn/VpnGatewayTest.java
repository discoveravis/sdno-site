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

package org.openo.sdno.localsiteservice.model.vpn;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VpnGatewayTest {

    private VpnGateway vpnGateway= new VpnGateway();

    @Test
    public void setGetAttributeTest() {

        vpnGateway.setDeployPosition("localFirst");
        assertTrue("localFirst".equals(vpnGateway.getDeployPosition()));

        vpnGateway.setDescription("description");
        assertTrue("description".equals(vpnGateway.getDescription()));

        vpnGateway.setDownstreamBandwidth(12);
        assertTrue(12 == vpnGateway.getDownstreamBandwidth());

        vpnGateway.setId("id");
        assertTrue("id".equals(vpnGateway.getId()));

        vpnGateway.setName("name");
        assertTrue("name".equals(vpnGateway.getName()));

        vpnGateway.setSiteId("siteId");
        assertTrue("siteId".equals(vpnGateway.getSiteId()));

        vpnGateway.setTenantId("tenantId");
        assertTrue("tenantId".equals(vpnGateway.getTenantId()));

        vpnGateway.setUpstreamBandwidth(13);
        assertTrue(13 == vpnGateway.getUpstreamBandwidth());

        vpnGateway.setVpcId("vpcId");
        assertTrue("vpcId".equals(vpnGateway.getVpcId()));

        vpnGateway.setVpnId("vpnId");
        assertTrue("vpnId".equals(vpnGateway.getVpnId()));

        vpnGateway.setVpnName("vpnName");
        assertTrue("vpnName".equals(vpnGateway.getVpnName()));
    }

}
