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

package org.openo.sdno.localsiteservice.sbi.site;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.restfulproxy.MocoFailRestfulProxy;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;

public class SubnetSbiServiceTest extends SpringTest {

    @Autowired
    private SubnetSbiService subnetSbiService;

    private static SbiSubnetNetModel subnetNetModel = new SbiSubnetNetModel();

    static {
        subnetNetModel.setNeId("NeId");
        subnetNetModel.setControllerId("ControllerId");
        subnetNetModel.setNetworkId("NetworkId");
    }

    public final class MockNeInvDao extends MockUp<NetworkElementInvDao> {

        @Mock
        public NetworkElementMO query(String id) throws ServiceException {
            NetworkElementMO neMO = new NetworkElementMO();
            neMO.setNativeID("DeviceId");
            return neMO;
        }
    }

    @Test
    public void queryFailedTest() throws ServiceException {
        new MockNeInvDao();
        new MocoFailRestfulProxy();
        SbiSubnetNetModel subnetNetModel = new SbiSubnetNetModel();
        ResultRsp<SbiSubnetNetModel> resultRsp = subnetSbiService.query(subnetNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void createControllerErrorTest() throws ServiceException {
        new MockNeInvDao();
        SbiSubnetNetModel subnetNetModel = new SbiSubnetNetModel();
        subnetNetModel.setNeId("NeId");
        subnetNetModel.setControllerId(null);
        ResultRsp<SbiSubnetNetModel> resultRsp = subnetSbiService.create(subnetNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void createFailedTest() throws ServiceException {
        new MockNeInvDao();
        new MocoFailRestfulProxy();
        SbiSubnetNetModel subnetNetModel = new SbiSubnetNetModel();
        subnetNetModel.setNeId("NeId");
        subnetNetModel.setControllerId("ControllerId");
        subnetNetModel.setNetworkId(null);
        ResultRsp<SbiSubnetNetModel> resultRsp = subnetSbiService.create(subnetNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void deleteFailedTest() throws ServiceException {
        new MockNeInvDao();
        new MocoFailRestfulProxy();
        ResultRsp<SbiSubnetNetModel> resultRsp = subnetSbiService.delete(subnetNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void updateFailedTest() throws ServiceException {
        new MockNeInvDao();
        new MocoFailRestfulProxy();
        ResultRsp<SbiSubnetNetModel> resultRsp = subnetSbiService.update(subnetNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

}
