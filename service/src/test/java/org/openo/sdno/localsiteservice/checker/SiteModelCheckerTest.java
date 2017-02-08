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

package org.openo.sdno.localsiteservice.checker;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.SiteModelDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;

public class SiteModelCheckerTest extends SpringTest{

    @Autowired
    private SiteModelChecker modelChecker;

    public final class MockSubnetNotEmptySiteDao extends MockUp<SiteModelDao> {

        @Mock
        public List<NbiSubnetModel> querySiteSubnets(String siteId) throws ServiceException {
            return Arrays.asList(new NbiSubnetModel());
        }
    }

    public final class MockVlanNotEmptySiteDao extends MockUp<SiteModelDao> {

        @Mock
        public List<NbiSubnetModel> querySiteSubnets(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public List<NbiVlanModel> querySiteVlans(String siteId) throws ServiceException {
            return Arrays.asList(new NbiVlanModel());
        }
    }

    public final class MockRouteEntryNotEmptySiteDao extends MockUp<SiteModelDao> {

        @Mock
        public List<NbiSubnetModel> querySiteSubnets(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public List<NbiVlanModel> querySiteVlans(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public List<NbiRouteEntryModel> querySiteRouteEntrys(String siteId) throws ServiceException {
            return Arrays.asList(new NbiRouteEntryModel());
        }
    }

    public final class MockInternetGatewayNotEmptySiteDao extends MockUp<SiteModelDao> {

        @Mock
        public List<NbiSubnetModel> querySiteSubnets(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public List<NbiVlanModel> querySiteVlans(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public List<NbiRouteEntryModel> querySiteRouteEntrys(String siteId) throws ServiceException {
            return null;
        }

        @Mock
        public NbiInternetGatewayModel querySiteInternetGateways(String siteId) throws ServiceException {
            return new NbiInternetGatewayModel();
        }
    }

    @Test
    public void subnetNotEmptyTest() throws ServiceException {
        new MockSubnetNotEmptySiteDao();
        boolean checkResult = modelChecker.checkResourceDependency("SiteId");
        assertTrue(true == checkResult);
    }

    @Test
    public void vlanNotEmptyTest() throws ServiceException {
        new MockVlanNotEmptySiteDao();
        boolean checkResult = modelChecker.checkResourceDependency("SiteId");
        assertTrue(true == checkResult);
    }

    @Test
    public void routeEntryNotEmptyTest() throws ServiceException {
        new MockRouteEntryNotEmptySiteDao();
        boolean checkResult = modelChecker.checkResourceDependency("SiteId");
        assertTrue(true == checkResult);
    }

    @Test
    public void internetGatewayNotEmptyTest() throws ServiceException {
        new MockInternetGatewayNotEmptySiteDao();
        boolean checkResult = modelChecker.checkResourceDependency("SiteId");
        assertTrue(true == checkResult);
    }

}
