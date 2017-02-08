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
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

public class SNatSbiServiceTest extends SpringTest {

    @Autowired
    private SNatSbiService sNatSbiService;

    @Test
    public void createDeviceIdErrorTest() throws ServiceException {
        SbiSnatNetModel sNatNetModel = new SbiSnatNetModel();
        sNatNetModel.setControllerId("ControllerId");
        sNatNetModel.setDeviceId(null);
        ResultRsp<SbiSnatNetModel> resultRsp = sNatSbiService.create(sNatNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void deleteControllerIdErrorTest() throws ServiceException {
        SbiSnatNetModel sNatNetModel = new SbiSnatNetModel();
        sNatNetModel.setControllerId(null);
        sNatNetModel.setDeviceId("DeviceId");
        ResultRsp<String> resultRsp = sNatSbiService.delete(sNatNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void createFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        SbiSnatNetModel sNatNetModel = new SbiSnatNetModel();
        sNatNetModel.setControllerId("ControllerId");
        sNatNetModel.setDeviceId("DeviceId");
        ResultRsp<SbiSnatNetModel> resultRsp = sNatSbiService.create(sNatNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void deleteFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        SbiSnatNetModel sNatNetModel = new SbiSnatNetModel();
        sNatNetModel.setControllerId("ControllerId");
        sNatNetModel.setDeviceId("DeviceId");
        ResultRsp<String> resultRsp = sNatSbiService.delete(sNatNetModel);
        assertTrue(!resultRsp.isSuccess());
    }

}
