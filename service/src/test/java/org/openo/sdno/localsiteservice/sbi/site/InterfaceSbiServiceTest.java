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

import java.util.List;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.model.inf.InterfaceModel;
import org.openo.sdno.localsiteservice.restfulproxy.MockFailRestfulProxy;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

public class InterfaceSbiServiceTest extends SpringTest {

    @Autowired
    private InterfaceSbiService interfaceSbiService;

    @Test
    public void controllerIdErrorTest() throws ServiceException {
        ResultRsp<List<InterfaceModel>> resultRsp = interfaceSbiService.query(null, "DeviceId");
        assertTrue(!resultRsp.isSuccess());
    }

    @Test
    public void queryFailedTest() throws ServiceException {
        new MockFailRestfulProxy();
        ResultRsp<List<InterfaceModel>> resultRsp = interfaceSbiService.query("Controller", "DeviceId");
        assertTrue(!resultRsp.isSuccess());
    }

}
