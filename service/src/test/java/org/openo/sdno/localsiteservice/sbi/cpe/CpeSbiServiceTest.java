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

package org.openo.sdno.localsiteservice.sbi.cpe;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.localsiteservice.restfulproxy.MocoFailRestfulProxy;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.springframework.beans.factory.annotation.Autowired;

public class CpeSbiServiceTest extends SpringTest {

    @Autowired
    private CpeSbiService cpeSbiService;

    @Test(expected = ServiceException.class)
    public void createFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        cpeSbiService.create(new VCpePlanInfo());
    }

    @Test(expected = ServiceException.class)
    public void deleteFailedTest() throws ServiceException {
        new MocoFailRestfulProxy();
        cpeSbiService.delete("CpeUuid");
    }
}
