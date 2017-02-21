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
import org.openo.sdno.localsiteservice.moco.inventorydao.MockSdnControllerDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockSiteInvDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class LocalCpeRoaResourceTest extends SpringTest {

    @Autowired
    private LocalCpeRoaResource localCpeRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiLocalCpeModel localCpeModel = new NbiLocalCpeModel();
    static {
        localCpeModel.setName("CpeDevice");
        localCpeModel.setEsn("ABCDEFGHIJKLMNOP1234");
        localCpeModel.setTenantId("TenantId");
        localCpeModel.setControllerId("SdnControllerId");
        localCpeModel.setSiteId("siteId");
        localCpeModel.setLocalCpeType("AR169FGW-L");
        localCpeModel.setUuid("LocalCpeId");
    }

    @Before
    public void setUp() {
        new MockInventoryDao<NbiSiteModel>();
        new MockNetworkElementInvDao();
        new MockCpeRestfulProxy();
        new MockSiteInvDao();
        new MockSdnControllerDao();
    }

    @Test
    public void queryTest() throws ServiceException {
        NetworkElementMO localCpeMO = localCpeRoaResource.query(httpRequest, "localCpeUuid");
        assertTrue("neName".equals(localCpeMO.getName()));
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        List<NetworkElementMO> localCpeMOList =
                localCpeRoaResource.batchQuery(httpRequest, "siteId", "cpeType", 12, 13);
        assertTrue(1 == localCpeMOList.size());
        assertTrue("neName".equals(localCpeMOList.get(0).getName()));
    }

    @Test
    public void createTest() throws ServiceException {
        new MockUp<NbiSiteModel>() {

            @Mock
            public String getLocalCpeType() {
                return "AR169FGW-L";
            }
        };
        Map<String, String> createResult = localCpeRoaResource.create(httpRequest, httpResponse, localCpeModel);
        assertTrue("NeId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        localCpeRoaResource.delete(httpRequest, "LocalCpeId");
    }
}
