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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mocked;

public class SnatRoaResourceTest extends SpringTest {

    @Autowired
    private SnatRoaResource snatRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Before
    public void setUp() {
        new MockInventoryDao<SbiSnatNetModel>();
    }

    @Test
    public void batchQueryTest() throws ServiceException {
        List<SbiSnatNetModel> sNatNetModelList =
                snatRoaResource.batchQuery(httpRequest, "internetGatewayId");
        assertTrue(2 == sNatNetModelList.size());
    }

}
