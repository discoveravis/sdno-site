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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockLtpInvDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockNetworkElementInvDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class VlanRoaResourceTest extends SpringTest {

    @Autowired
    private VlanRoaResource vlanRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiVlanModel vlanModel = new NbiVlanModel();
    static {
        vlanModel.setUuid("VlanId");
        vlanModel.setTenantId("TenantId");
        vlanModel.setName("Vlan1");
        vlanModel.setDescription("Test for Vlan Model data");
        vlanModel.setSiteId("SiteId");
        vlanModel.setVlanId(15);
        List<String> portIds = new ArrayList<String>();
        portIds.add("LtpId1");
        portIds.add("LtpId2");
        vlanModel.setPorts(portIds);
        List<String> portNames = new ArrayList<String>();
        portNames.add("Ltp1");
        portNames.add("Ltp2");
        vlanModel.setPortNames(portNames);
    };

    @Before
    public void setUp() {
        new MockInventoryDao<NbiVlanModel>();
        new MockInventoryDao<NbiSiteModel>();
        new MockNetworkElementInvDao();
        new MockLtpInvDao();
        new MockIfVlanRestfulProxy();
    }

    @Test
    public void queryTest() throws ServiceException {
        NbiVlanModel vlanModel = vlanRoaResource.query(httpRequest, httpResponse, "vlanUuid");
        assertTrue(null != vlanModel);
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        new MockUp<HttpServletRequest>() {

            @Mock
            public String getParameter(String parameter) {
                return parameter;
            }
        };
        ComplexResult<NbiVlanModel> queryResult = vlanRoaResource.batchQuery(httpRequest, httpResponse);
        assertTrue(2 == queryResult.getTotal());
    }

    @Test
    public void createTest() throws ServiceException {
        Map<String, String> createResult = vlanRoaResource.create(httpRequest, httpResponse, vlanModel);
        assertTrue("VlanId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        Map<String, String> deleteResult = vlanRoaResource.delete(httpRequest, httpResponse, "VlanId");
        assertFalse(deleteResult.isEmpty());
    }

    @Test
    public void updateTest() throws ServiceException {

        new MockUp<NbiVlanModel>() {

            @Mock
            public List<String> getPorts() {
                return Arrays.asList("LtpId1");
            }
        };

        NbiVlanModel vlanModel = new NbiVlanModel();
        vlanModel.setName("Vlan2");
        vlanModel.setDescription("Test for New Vlan Model data");
        NbiVlanModel updatedModel = vlanRoaResource.update(httpRequest, httpResponse, "VlanId", vlanModel);
        assertTrue(null != updatedModel);
    }

}
