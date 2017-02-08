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

package org.openo.sdno.localsiteservice.site.testcase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.msbmanager.MsbRegisterManager;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class BrsSiteExistSuccessTest extends TestManager {

    private static final String CREATE_BRS_SITE_TESTCASE =
            "src/integration-test/resources/site/testcase/createbrssite.json";

    private static final String CREATE_SITE_TESTCASE = "src/integration-test/resources/site/testcase/createsite.json";

    private static final String DELETE_SITE_TESTCASE = "src/integration-test/resources/site/testcase/deletesite.json";

    private static VpnServiceSuccessMocoServer vpnGatewayMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        vpnGatewayMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        vpnGatewayMocoServer.stop();
        MsbRegisterManager.unRegisterOverlayMsb();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateBrsSiteObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_BRS_SITE_TESTCASE);
        execTestCase(httpCreateBrsSiteObject.getRequest(), new StatusChecker(httpCreateBrsSiteObject.getResponse()));

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));
    }

}
