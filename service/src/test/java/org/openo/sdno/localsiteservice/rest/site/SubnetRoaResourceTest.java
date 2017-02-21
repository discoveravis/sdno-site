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
import org.openo.sdno.localsiteservice.checker.SubnetModelChecker;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockNetworkElementInvDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetNetModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class SubnetRoaResourceTest extends SpringTest {

    @Autowired
    private SubnetRoaResource subnetRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiSubnetModel subnetModel = new NbiSubnetModel();
    static {
        subnetModel.setTenantId("TenantId");
        subnetModel.setName("Subnet1");
        subnetModel.setDescription("Test for Subnet");
        subnetModel.setSiteId("SiteId");
        subnetModel.setCidrBlock("10.100.100.0/24");
        subnetModel.setGatewayIp("10.100.100.1");
        subnetModel.setVni("13");
        subnetModel.setEnableDhcp("false");
        subnetModel.setUuid("SubnetId");
    };

    @Before
    public void setUp() {
        new MockInventoryDao<NbiSubnetModel>();
        new MockInventoryDao<SbiSubnetNetModel>();
        new MockNetworkElementInvDao();
        new MockSubnetRestfulProxy();
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        ComplexResult<NbiSubnetModel> queryResult = subnetRoaResource.batchQuery(null, "TenantId", "SiteId", "0", "0");
        assertTrue(2 == queryResult.getTotal());
    }

    @Test
    public void queryTest() throws ServiceException {
        NbiSubnetModel subnetModel = subnetRoaResource.query(httpRequest, "SubnetUuid");
        assertTrue(null != subnetModel);
    }

    @Test
    public void createTest() throws ServiceException {
        new MockUp<SubnetModelChecker>() {

            @Mock
            private void checkNameAndCidrExist(NbiSubnetModel subnetModel) throws ServiceException {
            }
        };
        Map<String, String> createResult = subnetRoaResource.create(httpRequest, httpResponse, subnetModel);
        assertTrue(createResult.containsKey("id"));
    }

    @Test
    public void deleteTest() throws ServiceException {
        subnetRoaResource.delete(httpRequest, "SubnetUuid");
    }

}
