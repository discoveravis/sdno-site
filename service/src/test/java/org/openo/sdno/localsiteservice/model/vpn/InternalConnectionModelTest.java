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

public class InternalConnectionModelTest {

    private InternalConnectionModel internalConnectionModel= new InternalConnectionModel();

    @Test
    public void setGetAttributeTest() {
        internalConnectionModel.setCreatetime(123);
        assertTrue(123 == internalConnectionModel.getCreatetime());

        internalConnectionModel.setDeployPosition("localFirst");
        assertTrue("localFirst".equals(internalConnectionModel.getDeployPosition()));

        internalConnectionModel.setDeployStatus("Normal");
        assertTrue("Normal".equals(internalConnectionModel.getDeployStatus()));

        internalConnectionModel.setDescription("description");
        assertTrue("description".equals(internalConnectionModel.getDescription()));

        internalConnectionModel.setDestNeId("destNeId");
        assertTrue("destNeId".equals(internalConnectionModel.getDestNeId()));

        internalConnectionModel.setId("id");
        assertTrue("id".equals(internalConnectionModel.getId()));

        internalConnectionModel.setName("name");
        assertTrue("name".equals(internalConnectionModel.getName()));

        internalConnectionModel.setSiteId("siteId");
        assertTrue("siteId".equals(internalConnectionModel.getSiteId()));

        internalConnectionModel.setSrcNeId("srcNeId");
        assertTrue("srcNeId".equals(internalConnectionModel.getSrcNeId()));

        internalConnectionModel.setTenantId("tenantId");
        assertTrue("tenantId".equals(internalConnectionModel.getTenantId()));

        internalConnectionModel.setVpnTemplateName("vpnTemplateName");
        assertTrue("vpnTemplateName".equals(internalConnectionModel.getVpnTemplateName()));
    }

}
