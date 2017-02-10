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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockNetworkElementInvDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.util.OverlayvpnContextHelper;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class CloudCpeRoaResourceTest extends SpringTest {

    private CloudCpeRoaResource cloudCpeRoaResource =
            (CloudCpeRoaResource)OverlayvpnContextHelper.getBean("CloudCpeRoaResource");

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiCloudCpeModel cloudCpeModel = new NbiCloudCpeModel();
    static {
        cloudCpeModel.setTenantId("TenantId");
        cloudCpeModel.setSiteId("SiteId");
        cloudCpeModel.setEsn("21ABCDEFGH12345678901234abcdefgh");
        cloudCpeModel.setControllerId("ControllerId");
        cloudCpeModel.setUuid("CpeId");
    }

    @Before
    public void setUp() {
        new MockNetworkElementInvDao();
        new MockInventoryDao<NbiCloudCpeModel>();
        new MockCpeRestfulProxy();
    }

    @Test
    public void queryTest() throws ServiceException {
        NbiCloudCpeModel cloudCpeModel = cloudCpeRoaResource.query(httpRequest, httpResponse, "cloudCpeUuid");
        assertTrue(null != cloudCpeModel);
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        List<NbiCloudCpeModel> cloudCpeModelList =
                cloudCpeRoaResource.batchQuery(httpRequest, httpResponse, "tenantId", "popId", "siteId");
        assertTrue(2 == cloudCpeModelList.size());
    }

    @Test
    public void createTest() throws ServiceException {
        new MockUp<NbiSiteModel>() {

            @Mock
            public String getSiteDescriptor() {
                return "enterprise_l2cpe";
            }

            @Mock
            public String getReliability() {
                return "singleFixedNetwork";
            }
        };

        Map<String, String> createResult = cloudCpeRoaResource.create(httpRequest, httpResponse, cloudCpeModel);
        assertTrue("CpeId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        cloudCpeRoaResource.delete(httpRequest, httpResponse, "CloudCpeId");
    }

}
