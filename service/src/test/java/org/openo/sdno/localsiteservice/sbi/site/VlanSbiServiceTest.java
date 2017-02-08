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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.restfulproxy.MocoFailRestfulProxy;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.vlan.SbiIfVlan;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

public class VlanSbiServiceTest extends SpringTest {

    @Autowired
    private VlanSbiService vlanSbiService;

    @Test
    public void queryControllerIdErrorTest() throws ServiceException {
        ResultRsp<List<SbiIfVlan>> resultRsp = vlanSbiService.query(null, "DeviceId", "PortId");
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void queryPortIdErrorTest() throws ServiceException {
        ResultRsp<List<SbiIfVlan>> resultRsp = vlanSbiService.query("ControllerId", "DeviceId", null);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void queryFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        ResultRsp<List<SbiIfVlan>> resultRsp = vlanSbiService.query("ControllerId", "DeviceId", "PortId");
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void createControllerIdErrorTest() throws ServiceException {
        ResultRsp<List<SbiIfVlan>> resultRsp = vlanSbiService.create(null, "DeviceId", Arrays.asList(new SbiIfVlan()));
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void createFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        ResultRsp<List<SbiIfVlan>> resultRsp =
                vlanSbiService.create("ControllerId", "DeviceId", Arrays.asList(new SbiIfVlan()));
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void updateControllerIdErrorTest() throws ServiceException {
        ResultRsp<List<SbiIfVlan>> resultRsp = vlanSbiService.update(null, "DeviceId", Arrays.asList(new SbiIfVlan()));
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void updateFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        ResultRsp<List<SbiIfVlan>> resultRsp =
                vlanSbiService.update("ControllerId", "DeviceId", Arrays.asList(new SbiIfVlan()));
        assertTrue(!resultRsp.isSuccess());
    }

}
