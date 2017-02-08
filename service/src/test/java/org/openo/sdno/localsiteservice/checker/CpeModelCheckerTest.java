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

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.BaseResourceDao;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.springtest.SpringTest;
import org.openo.sdno.overlayvpn.brs.invdao.SiteInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.brs.model.SiteMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.esr.invdao.SdnControllerDao;
import org.openo.sdno.overlayvpn.esr.model.SdnController;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.springframework.beans.factory.annotation.Autowired;

import mockit.Mock;
import mockit.MockUp;

public class CpeModelCheckerTest extends SpringTest {

    @Autowired
    private CpeModelChecker modelChecker;

    private static NbiLocalCpeModel localCpeModel = new NbiLocalCpeModel();

    static {
        localCpeModel.setEsn("ABCDEFGHIJKLMNOP1234");
        localCpeModel.setSiteId("SiteId");
        localCpeModel.setControllerId("ControllerId");
        localCpeModel.setLocalCpeType("AR169FW-L");
    }

    public final class MockQueryEsnDao extends MockUp<BaseResourceDao> {

        private boolean existence;

        public MockQueryEsnDao(boolean existence) {
            this.existence = existence;
        }

        @Mock
        public NetworkElementMO queryNeByEsn(String esn) throws ServiceException {
            return existence ? new NetworkElementMO() : null;
        }
    }

    public final class MockQuerySiteDao extends MockUp<SiteInvDao> {

        private boolean existence;

        public MockQuerySiteDao(boolean existence) {
            this.existence = existence;
        }

        @Mock
        public SiteMO query(String id) throws ServiceException {
            return existence ? new SiteMO() : null;
        }
    }

    public final class MockQueryControllerDao extends MockUp<SdnControllerDao> {

        private boolean existence;

        public MockQueryControllerDao(boolean existence) {
            this.existence = existence;
        }

        @Mock
        public SdnController querySdnControllerById(String controllerId) throws ServiceException {
            return existence ? new SdnController() : null;
        }

    }

    public final class MockSiteModelInventoryDao extends MockUp<ModelDataDao<NbiSiteModel>> {

        private boolean existence;

        public MockSiteModelInventoryDao(boolean existence) {
            this.existence = existence;
        }

        @Mock
        public ResultRsp<NbiSiteModel> query(Class<NbiSiteModel> clazz, String uuid) throws ServiceException {
            ResultRsp<NbiSiteModel> resultRsp = new ResultRsp<NbiSiteModel>(ErrorCode.OVERLAYVPN_SUCCESS);
            if(existence) {
                NbiSiteModel siteModel = new NbiSiteModel();
                siteModel.setLocalCpeType("AR167FW-L");
                resultRsp.setData(siteModel);
            }
            return resultRsp;
        }
    }

    @Test(expected = ServiceException.class)
    public void checkEsnFormatTest() throws ServiceException {
        NbiLocalCpeModel esnErrorLocalCpeModel = new NbiLocalCpeModel();
        esnErrorLocalCpeModel.setEsn("errorformatesn");
        modelChecker.checkLocalCpeModel(esnErrorLocalCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkEsnExistenceTest() throws ServiceException {
        new MockQueryEsnDao(true);
        modelChecker.checkLocalCpeModel(localCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkSiteNullTest() throws ServiceException {
        new MockQueryEsnDao(false);
        NbiLocalCpeModel siteNullLocalCpeModel = new NbiLocalCpeModel();
        siteNullLocalCpeModel.setEsn("ABCDEFGHIJKLMNOP1234");
        siteNullLocalCpeModel.setSiteId(null);
        modelChecker.checkLocalCpeModel(siteNullLocalCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkSiteNotExistTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(false);
        modelChecker.checkLocalCpeModel(localCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkControllerNullTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(true);
        NbiLocalCpeModel controllerNullLocalCpeModel = new NbiLocalCpeModel();
        controllerNullLocalCpeModel.setEsn("ABCDEFGHIJKLMNOP1234");
        controllerNullLocalCpeModel.setSiteId("SiteId");
        controllerNullLocalCpeModel.setControllerId(null);
        modelChecker.checkLocalCpeModel(controllerNullLocalCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkControllerNotExistTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(true);
        new MockQueryControllerDao(false);
        modelChecker.checkLocalCpeModel(localCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkLocalCpeTypeNullTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(true);
        new MockQueryControllerDao(true);
        NbiLocalCpeModel cpeTypeNullLocalCpeModel = new NbiLocalCpeModel();
        cpeTypeNullLocalCpeModel.setEsn("ABCDEFGHIJKLMNOP1234");
        cpeTypeNullLocalCpeModel.setSiteId("SiteId");
        cpeTypeNullLocalCpeModel.setControllerId("ControllerId");
        cpeTypeNullLocalCpeModel.setLocalCpeType(null);
        modelChecker.checkLocalCpeModel(cpeTypeNullLocalCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkSiteModelNotExistTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(true);
        new MockQueryControllerDao(true);
        new MockSiteModelInventoryDao(false);
        modelChecker.checkLocalCpeModel(localCpeModel);
    }

    @Test(expected = ServiceException.class)
    public void checkLocalCpeTypeMismatchNotExistTest() throws ServiceException {
        new MockQueryEsnDao(false);
        new MockQuerySiteDao(true);
        new MockQueryControllerDao(true);
        new MockSiteModelInventoryDao(true);
        modelChecker.checkLocalCpeModel(localCpeModel);
    }

}
