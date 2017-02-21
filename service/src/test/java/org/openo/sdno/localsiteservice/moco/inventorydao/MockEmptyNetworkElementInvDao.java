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

package org.openo.sdno.localsiteservice.moco.inventorydao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;

import mockit.Mock;
import mockit.MockUp;

public class MockEmptyNetworkElementInvDao extends MockUp<NetworkElementInvDao> {

    private int callCount = 0;

    private NetworkElementMO neMO = new NetworkElementMO();

    public MockEmptyNetworkElementInvDao() {
        neMO.setControllerID(Arrays.asList("ControllerId"));
        neMO.setName("neName");
        neMO.setNativeID("nativeID");
        neMO.setId("NeId");
    }

    @Mock
    public List<NetworkElementMO> getMOByIds(List<String> ids) throws ServiceException {
        return new ArrayList<>();
    }

    @Mock
    public NetworkElementMO query(String id) throws ServiceException {
        return null;
    }

    @Mock
    public List<NetworkElementMO> query(Map<String, String> condition) throws ServiceException {
        if(0 == callCount++) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(neMO);
        }
    }

    @Mock
    public void updateMO(NetworkElementMO curNeMO) throws ServiceException {
        return;
    }

}
