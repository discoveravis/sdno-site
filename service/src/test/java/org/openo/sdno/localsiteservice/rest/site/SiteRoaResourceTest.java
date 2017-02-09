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

package org.openo.sdno.localsiteservice.rest.site;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockNetworkElementInvDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockSiteInvDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mocked;

public class SiteRoaResourceTest extends SpringTest {

    @Autowired
    private SiteRoaResource siteRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiSiteModel siteModel = new NbiSiteModel();
    static {
        siteModel.setUuid("SiteId");
        siteModel.setTenantId("TenantId");
        siteModel.setName("TestSite");
        siteModel.setDescription("Test for Site");
        siteModel.setLocalCpeType("China ShenZhen");
        siteModel.setSiteDescriptor("enterprise_l2cpe");
        siteModel.setReliability("singleFixedNetwork");
        siteModel.setIsEncrypt("false");
    };

    @Before
    public void setUp() {
        new MockInventoryDao<NbiSiteModel>();
        new MockSiteInvDao();
        new MockSiteRestfulProxy();
        new MockNetworkElementInvDao();
    }

    @Test
    public void queryTest() throws ServiceException {
        NbiSiteModel siteModel = siteRoaResource.query(httpRequest, httpResponse, "SiteId");
        assertTrue(null != siteModel);
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        ComplexResult<NbiSiteModel> queryResult =
                siteRoaResource.batchQuery(httpRequest, httpResponse, "[\"SiteId\",\"SiteId2\"]");
        assertTrue(2 == queryResult.getTotal());
    }

    @Test
    public void createTest() throws ServiceException {
        Map<String, String> createResult = siteRoaResource.create(httpRequest, httpResponse, siteModel);
        assertTrue("SiteId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        siteRoaResource.delete(httpRequest, httpResponse, "SiteId");
    }

    @Test
    public void updateTest() throws ServiceException {
        Map<String, Object> updateResult = siteRoaResource.update(httpRequest, httpResponse, "SiteId", siteModel);
        assertTrue(!updateResult.isEmpty());
    }
}
