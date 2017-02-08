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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.common.enums.ActionStatus;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;

public class SubnetModelCheckerTest extends SpringTest {

    @Autowired
    private SubnetModelChecker modelChecker;

    private static NbiSubnetModel subnetModel = new NbiSubnetModel();

    static {
        subnetModel.setTenantId("TenantId");
        subnetModel.setName("Subnet1");
        subnetModel.setSiteId("SiteId");
        subnetModel.setGatewayIp("10.100.100.1");
        subnetModel.setUuid(UuidUtils.createUuid());
        subnetModel.setActionState(ActionStatus.NORMAL.getName());
        subnetModel.setCidrBlock("10.100.100.0/24");
        subnetModel.setVni("13");
    }

    public final class MockNameExistInventoryDao extends MockUp<ModelDataDao<NbiSubnetModel>> {

        @Mock
        ResultRsp<List<NbiSubnetModel>> queryByFilter(Class<NbiSubnetModel> clazz, Map<String, Object> filterMap,
                String queryResultFields) throws ServiceException {
            if(null != filterMap.get("name")) {
                return new ResultRsp<List<NbiSubnetModel>>(ErrorCode.OVERLAYVPN_SUCCESS,
                        Arrays.asList(new NbiSubnetModel()));
            } else {
                return new ResultRsp<List<NbiSubnetModel>>(ErrorCode.OVERLAYVPN_SUCCESS,
                        new ArrayList<NbiSubnetModel>());
            }
        }
    }

    public final class MockCidrExistInventoryDao extends MockUp<ModelDataDao<NbiSubnetModel>> {

        @Mock
        ResultRsp<List<NbiSubnetModel>> queryByFilter(Class<NbiSubnetModel> clazz, Map<String, Object> filterMap,
                String queryResultFields) throws ServiceException {
            if(null != filterMap.get("cidrBlock")) {
                return new ResultRsp<List<NbiSubnetModel>>(ErrorCode.OVERLAYVPN_SUCCESS,
                        Arrays.asList(new NbiSubnetModel()));
            } else {
                return new ResultRsp<List<NbiSubnetModel>>(ErrorCode.OVERLAYVPN_SUCCESS,
                        new ArrayList<NbiSubnetModel>());
            }
        }
    }

    @Test(expected = ServiceException.class)
    public void cidrExistCheckTest() throws ServiceException {
        new MockCidrExistInventoryDao();
        modelChecker.check(subnetModel);
    }

    @Test(expected = ServiceException.class)
    public void nameExistCheckTest() throws ServiceException {
        new MockNameExistInventoryDao();
        modelChecker.check(subnetModel);
    }

}
