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

public class CpeBasicPlanInfoTest extends CpeBasicPlanInfo {

    private CpeBasicPlanInfo cpeBasicPlanInfo = new CpeBasicPlanInfo();

    @Test
    public void setGetAttributeTest() {

        cpeBasicPlanInfo.setAdditionalInfo(Arrays.asList(new NvString()));
        assertTrue(1 == cpeBasicPlanInfo.getAdditionalInfo().size());

        cpeBasicPlanInfo.setControllerId("controllerId");
        assertTrue("controllerId".equals(cpeBasicPlanInfo.getControllerId()));

        cpeBasicPlanInfo.setDescription("description");
        assertTrue("description".equals(cpeBasicPlanInfo.getDescription()));

        cpeBasicPlanInfo.setEsn("esn");
        assertTrue("esn".equals(cpeBasicPlanInfo.getEsn()));

        cpeBasicPlanInfo.setName("name");
        assertTrue("name".equals(cpeBasicPlanInfo.getName()));

        cpeBasicPlanInfo.setOldEsn("oldEsn");
        assertTrue("oldEsn".equals(cpeBasicPlanInfo.getOldEsn()));

        cpeBasicPlanInfo.setStatus("status");
        assertTrue("status".equals(cpeBasicPlanInfo.getStatus()));

        cpeBasicPlanInfo.setTenantId("tenantId");
        assertTrue("tenantId".equals(cpeBasicPlanInfo.getTenantId()));

        cpeBasicPlanInfo.setUuid("uuid");
        assertTrue("uuid".equals(cpeBasicPlanInfo.getUuid()));
    }

}
