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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;

import mockit.Mock;
import mockit.MockUp;

public class MockSiteInvDao extends MockUp<SiteInvDao> {

    @Mock
    public SiteMO query(String id) throws ServiceException {
        SiteMO siteMO = new SiteMO();
        siteMO.setId(id);
        return siteMO;
    }

    @Mock
    public void deleteMO(String uuid) throws ServiceException {
        return;
    }

    @Mock
    public void updateMO(SiteMO curMO) throws ServiceException {
        return;
    }

}
