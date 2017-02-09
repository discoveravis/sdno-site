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

package org.openo.sdno.localsiteservice.model.cpe;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.openo.sdno.overlayvpn.model.v2.basemodel.NvString;

public class VCpePlanInfoTest {

    private VCpePlanInfo vCpePlanInfo = new VCpePlanInfo();

    @Test
    public void setGetAttributeTest() {

        vCpePlanInfo.setAdditionalInfo(Arrays.asList(new NvString()));
        assertTrue(1 == vCpePlanInfo.getAdditionalInfo().size());

        vCpePlanInfo.setControllerId("controllerId");
        assertTrue("controllerId".equals(vCpePlanInfo.getControllerId()));

        vCpePlanInfo.setDescription("description");
        assertTrue("description".equals(vCpePlanInfo.getDescription()));

        vCpePlanInfo.setEsn("esn");
        assertTrue("esn".equals(vCpePlanInfo.getEsn()));

        vCpePlanInfo.setName("name");
        assertTrue("name".equals(vCpePlanInfo.getName()));

        vCpePlanInfo.setOldEsn("oldEsn");
        assertTrue("oldEsn".equals(vCpePlanInfo.getOldEsn()));

        vCpePlanInfo.setPopId("popId");
        assertTrue("popId".equals(vCpePlanInfo.getPopId()));

        vCpePlanInfo.setRole("role");
        assertTrue("role".equals(vCpePlanInfo.getRole()));

        vCpePlanInfo.setSiteId("siteId");
        assertTrue("siteId".equals(vCpePlanInfo.getSiteId()));

        vCpePlanInfo.setStatus("status");
        assertTrue("status".equals(vCpePlanInfo.getStatus()));

        vCpePlanInfo.setTenantId("tenantId");
        assertTrue("tenantId".equals(vCpePlanInfo.getTenantId()));

        vCpePlanInfo.setUuid("uuid");
        assertTrue("uuid".equals(vCpePlanInfo.getUuid()));
    }
}
