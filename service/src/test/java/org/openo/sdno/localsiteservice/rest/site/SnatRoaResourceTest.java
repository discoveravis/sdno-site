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

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.moco.inventorydao.MockInventoryDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class SnatRoaResourceTest extends SpringTest {

    @Autowired
    private SnatRoaResource snatRoaResource;

    @Mocked
    private HttpServletRequest httpRequest;

    @Test
    public void batchQueryTest() throws ServiceException {
        new MockInventoryDao<SbiSnatNetModel>();
        List<SbiSnatNetModel> sNatNetModelList = snatRoaResource.batchQuery(httpRequest, "internetGatewayId");
        assertTrue(2 == sNatNetModelList.size());
    }

    @Test(expected = ServiceException.class)
    public void batchQueryFailedTest() throws ServiceException {
        new MockUp<InventoryDao<SbiSnatNetModel>>() {

            @Mock
            ResultRsp<List<SbiSnatNetModel>> queryByFilter(Class<SbiSnatNetModel> clazz, String filter,
                    String queryResultFields) throws ServiceException {
                return new ResultRsp<>(ErrorCode.OVERLAYVPN_FAILED);
            }
        };
        snatRoaResource.batchQuery(httpRequest, "internetGatewayId");
    }

}
