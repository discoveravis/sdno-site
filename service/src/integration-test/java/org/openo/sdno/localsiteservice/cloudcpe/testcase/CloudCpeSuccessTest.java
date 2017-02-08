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

package org.openo.sdno.localsiteservice.cloudcpe.testcase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.cpe.mocoserver.CpeDeviceMocoServer;
import org.openo.sdno.localsiteservice.drivermanager.DriverRegisterManager;
import org.openo.sdno.localsiteservice.esrmanager.EsrRegisterManager;
import org.openo.sdno.localsiteservice.msbmanager.MsbRegisterManager;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class CloudCpeSuccessTest extends TestManager {

    private static final String CREATE_SITE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/createsite.json";

    private static final String DELETE_SITE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/deletesite.json";

    private static final String CREATE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/createcloudcpe.json";

    private static final String DELETE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/deletecloudcpe.json";

    private static final String QUERY_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/querycloudcpe.json";

    private static final String BATCH_QUERY_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/batchquerycloudcpe.json";

    private static final String BATCH_EMPTY_QUERY_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/cloudcpe/testcase/batchemptyquerycloudcpe.json";

    private static CpeDeviceMocoServer successMocoServer = new CpeDeviceMocoServer();

    private static VpnServiceSuccessMocoServer vpnGatewayMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        DriverRegisterManager.registerDriver();
        EsrRegisterManager.registerEsr();
        successMocoServer.start();
        vpnGatewayMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        DriverRegisterManager.unRegisterDriver();
        EsrRegisterManager.unRegisterEsr();
        successMocoServer.stop();
        vpnGatewayMocoServer.stop();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpCreateCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_CLOUDCPE_TESTCASE);
        HttpRequest httpCreateCloudCpeRequest = httpCreateCloudCpeObject.getRequest();
        NbiCloudCpeModel cloudCpeModel = JsonUtil.fromJson(httpCreateCloudCpeRequest.getData(), NbiCloudCpeModel.class);
        cloudCpeModel.setControllerId(EsrRegisterManager.getControllerId());
        httpCreateCloudCpeRequest.setData(JsonUtil.toJson(cloudCpeModel));
        execTestCase(httpCreateCloudCpeRequest, new StatusAndBodyChecker(httpCreateCloudCpeObject.getResponse()));

        HttpRquestResponse httpQueryCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_CLOUDCPE_TESTCASE);
        execTestCase(httpQueryCloudCpeObject.getRequest(), new StatusChecker(httpQueryCloudCpeObject.getResponse()));

        HttpRquestResponse httpBatchQueryCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_CLOUDCPE_TESTCASE);
        execTestCase(httpBatchQueryCloudCpeObject.getRequest(),
                new StatusChecker(httpBatchQueryCloudCpeObject.getResponse()));

        HttpRquestResponse httpDeleteCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_CLOUDCPE_TESTCASE);
        execTestCase(httpDeleteCloudCpeObject.getRequest(), new StatusChecker(httpDeleteCloudCpeObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {
        HttpRquestResponse httpBatchEmptyQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTY_QUERY_CLOUDCPE_TESTCASE);
        execTestCase(httpBatchEmptyQueryObject.getRequest(),
                new StatusAndBodyChecker(httpBatchEmptyQueryObject.getResponse()));
    }

}
