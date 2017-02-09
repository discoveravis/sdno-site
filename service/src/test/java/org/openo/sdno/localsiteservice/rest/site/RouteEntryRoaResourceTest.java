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
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class RouteEntryRoaResourceTest extends SpringTest {

    @Autowired
    private RouteEntryRoaResource routeEntryRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Mocked
    private HttpServletResponse httpResponse;

    private static NbiRouteEntryModel routeEntryModel = new NbiRouteEntryModel();
    static {
        routeEntryModel.setName("RouteEntry");
        routeEntryModel.setUuid("RouteEntryId");
        routeEntryModel.setTenantId("TenantId");
        routeEntryModel.setSiteId("SiteId");
        routeEntryModel.setInternetGatewayId("InternetGatewayId");
        routeEntryModel.setVpnGatewayId("VpnGateWayId");
        routeEntryModel.setRouteType("static-routing");
        routeEntryModel.setPrecedence(13);
    }

    @Before
    public void setUp() {
        new MockInventoryDao<NbiRouteEntryModel>();
    }

    @Test
    public void queryTest() throws ServiceException {
        NbiRouteEntryModel routeEntryModel = routeEntryRoaResource.query(httpRequest, httpResponse, "RouteEntryId");
        assertTrue(null != routeEntryModel);
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        new MockUp<HttpServletRequest>() {

            @Mock
            public String getParameter(String parameter) {
                return parameter;
            }
        };
        ComplexResult<NbiRouteEntryModel> queryResult = routeEntryRoaResource.batchQuery(httpRequest, httpResponse);
        assertTrue(2 == queryResult.getTotal());
    }

    @Test
    public void createTest() throws ServiceException {
        Map<String, String> createResult = routeEntryRoaResource.create(httpRequest, httpResponse, routeEntryModel);
        assertTrue("RouteEntryId".equals(createResult.get("id")));
    }

    @Test
    public void deleteTest() throws ServiceException {
        routeEntryRoaResource.delete(httpRequest, httpResponse, "RouteEntryId");
    }

    @Test
    public void updateTest() throws ServiceException {
        NbiRouteEntryModel newModel = new NbiRouteEntryModel();
        newModel.setName("newRoute");
        newModel.setDescription("Test for route");
        NbiRouteEntryModel updatedModel =
                routeEntryRoaResource.update(httpRequest, httpResponse, "RouteEntryId", newModel);
        assertTrue(null != updatedModel);
    }

}
