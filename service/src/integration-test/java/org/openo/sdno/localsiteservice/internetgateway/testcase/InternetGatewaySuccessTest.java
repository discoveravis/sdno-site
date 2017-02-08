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

package org.openo.sdno.localsiteservice.internetgateway.testcase;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.drivermanager.DriverRegisterManager;
import org.openo.sdno.localsiteservice.esrmanager.EsrRegisterManager;
import org.openo.sdno.localsiteservice.internetgateway.mocoserver.InternetGatewaySuccessMocoServer;
import org.openo.sdno.localsiteservice.msbmanager.MsbRegisterManager;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class InternetGatewaySuccessTest extends TestManager {

    private static final String CREATE_SITE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/createsite.json";

    private static final String DELETE_SITE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/deletesite.json";

    private static final String CREATE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/createcloudcpe.json";

    private static final String DELETE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/deletecloudcpe.json";

    private static final String CREATE_THINCPE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/createthincpe.json";

    private static final String DELETE_THINCPE_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/deletethincpe.json";

    private static final String CREATE_WANLTP_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/createwanltp.json";

    private static final String DELETE_WANLTP_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/deletewanltp.json";

    private static final String CREATE_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/createinternetgateway.json";

    private static final String DELETE_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/deleteinternetgateway.json";

    private static final String UPDATE_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/updateinternetgateway.json";

    private static final String QUERY_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/queryinternetgateway.json";

    private static final String BATCH_QUERY_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/batchqueryinternetgateway.json";

    private static final String BATCH_EMPTYQUERY_INTERNETGATEWAY_TESTCASE =
            "src/integration-test/resources/internetgateway/testcase/batchemptyqueryintgateway.json";

    private static InternetGatewaySuccessMocoServer internetGatewaySuccessMocoServer =
            new InternetGatewaySuccessMocoServer();

    private static VpnServiceSuccessMocoServer vpnServiceSuccessMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        DriverRegisterManager.registerDriver();
        EsrRegisterManager.registerEsr();
        internetGatewaySuccessMocoServer.start();
        vpnServiceSuccessMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        DriverRegisterManager.unRegisterDriver();
        EsrRegisterManager.unRegisterEsr();
        internetGatewaySuccessMocoServer.stop();
        vpnServiceSuccessMocoServer.stop();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpCreateCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_CLOUDCPE_TESTCASE);
        HttpRequest httpCreateCloudCpeRequest = httpCreateCloudCpeObject.getRequest();
        Map<String, NetworkElementMO> cloudCpeNe = JsonUtil.fromJson(httpCreateCloudCpeRequest.getData(),
                new TypeReference<Map<String, NetworkElementMO>>() {});
        cloudCpeNe.get("managedElement").setControllerID(Arrays.asList(EsrRegisterManager.getControllerId()));
        httpCreateCloudCpeRequest.setData(JsonUtil.toJson(cloudCpeNe));
        execTestCase(httpCreateCloudCpeRequest, new StatusChecker(httpCreateCloudCpeObject.getResponse()));

        HttpRquestResponse httpCreateThinCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_THINCPE_TESTCASE);
        HttpRequest httpCreateThinCpeRequest = httpCreateThinCpeObject.getRequest();
        Map<String, NetworkElementMO> thinCpeNe = JsonUtil.fromJson(httpCreateThinCpeRequest.getData(),
                new TypeReference<Map<String, NetworkElementMO>>() {});
        thinCpeNe.get("managedElement").setControllerID(Arrays.asList(EsrRegisterManager.getControllerId()));
        httpCreateThinCpeRequest.setData(JsonUtil.toJson(thinCpeNe));
        execTestCase(httpCreateThinCpeRequest, new StatusChecker(httpCreateThinCpeObject.getResponse()));

        HttpRquestResponse httpCreateWanLtpObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_WANLTP_TESTCASE);
        execTestCase(httpCreateWanLtpObject.getRequest(), new StatusChecker(httpCreateWanLtpObject.getResponse()));

        HttpRquestResponse httpCreateIntGatewayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_INTERNETGATEWAY_TESTCASE);
        execTestCase(httpCreateIntGatewayObject.getRequest(),
                new StatusAndBodyChecker(httpCreateIntGatewayObject.getResponse()));

        HttpRquestResponse httpUpdateIntGatewayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_INTERNETGATEWAY_TESTCASE);
        execTestCase(httpUpdateIntGatewayObject.getRequest(),
                new StatusChecker(httpUpdateIntGatewayObject.getResponse()));

        HttpRquestResponse httpQueryGatewayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_INTERNETGATEWAY_TESTCASE);
        execTestCase(httpQueryGatewayObject.getRequest(), new StatusChecker(httpQueryGatewayObject.getResponse()));

        HttpRquestResponse httpBatchQueryGatewayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_INTERNETGATEWAY_TESTCASE);
        HttpResponse queryResponse = execTestCase(httpBatchQueryGatewayObject.getRequest(),
                new StatusChecker(httpBatchQueryGatewayObject.getResponse()));
        ComplexResult<NbiInternetGatewayModel> queryResultRsp =
                JsonUtil.fromJson(queryResponse.getData(), new TypeReference<ComplexResult<NbiInternetGatewayModel>>() {});
        assertTrue(1 == queryResultRsp.getTotal());

        HttpRquestResponse httpDeleteIntGatewayObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_INTERNETGATEWAY_TESTCASE);
        execTestCase(httpDeleteIntGatewayObject.getRequest(),
                new StatusChecker(httpDeleteIntGatewayObject.getResponse()));

        HttpRquestResponse httpDeleteWanLtpObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_WANLTP_TESTCASE);
        execTestCase(httpDeleteWanLtpObject.getRequest(), new StatusChecker(httpDeleteWanLtpObject.getResponse()));

        HttpRquestResponse httpDeleteThinCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_THINCPE_TESTCASE);
        execTestCase(httpDeleteThinCpeObject.getRequest(), new StatusChecker(httpDeleteThinCpeObject.getResponse()));

        HttpRquestResponse httpDeleteCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_CLOUDCPE_TESTCASE);
        execTestCase(httpDeleteCloudCpeObject.getRequest(), new StatusChecker(httpDeleteCloudCpeObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {
        HttpRquestResponse httpBatchQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTYQUERY_INTERNETGATEWAY_TESTCASE);
        execTestCase(httpBatchQueryObject.getRequest(), new StatusAndBodyChecker(httpBatchQueryObject.getResponse()));
    }

}
