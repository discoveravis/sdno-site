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

package org.openo.sdno.localsiteservice.rest.cpe;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockNetworkElementInvDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockSiteInvDao;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mocked;

public class CpeOnlineRoaResourceTest extends SpringTest{

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    @Autowired
    private CpeOnlineRoaResource cpeOnlineRoaResource;

    private static VCpePlanInfo vCpePlanInfo = new VCpePlanInfo();
    static {
        vCpePlanInfo.setEsn("ABCDEFGHIJKLMNOP1234");
        vCpePlanInfo.setRole("Thin CPE");
        vCpePlanInfo.setSiteId("SiteId");
        vCpePlanInfo.setUuid("vCpePlanInfoId");
    }
    
    @Before
    public void setUp() {
        new MockSiteInvDao();
        new MockNetworkElementInvDao();
        new MockCpeOnlineRestfulProxy();
    }

    @Test
    public void createTest() throws ServiceException {
        Map<String, String> createResult = cpeOnlineRoaResource.create(httpRequest, httpResponse, vCpePlanInfo);
        assertTrue("NeId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        cpeOnlineRoaResource.delete(httpRequest, "[\"vCpePlanInfoId\"]");
    }

}
