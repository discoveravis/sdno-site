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

package org.openo.sdno.localsiteservice.subnet.testcase;

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
import org.openo.sdno.localsiteservice.msbmanager.MsbRegisterManager;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.localsiteservice.subnet.mocoserver.SubnetSuccessMocoServer;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class SubnetSuccessTest extends TestManager {

    private static final String CREATE_SITE_TESTCASE = "src/integration-test/resources/subnet/testcase/createsite.json";

    private static final String DELETE_SITE_TESTCASE = "src/integration-test/resources/subnet/testcase/deletesite.json";

    private static final String CREATE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/subnet/testcase/createcloudcpe.json";

    private static final String DELETE_CLOUDCPE_TESTCASE =
            "src/integration-test/resources/subnet/testcase/deletecloudcpe.json";

    private static final String CREATE_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/createsubnet.json";

    private static final String DELETE_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/deletesubnet.json";

    private static final String UPDATE_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/updatesubnet.json";

    private static final String QUERY_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/querysubnet.json";

    private static final String BATCH_QUERY_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/batchquerysubnet.json";

    private static final String BATCH_EMPTY_QUERY_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/batchemptyquerysubnet.json";

    private static SubnetSuccessMocoServer subnetSuccessMocoServer = new SubnetSuccessMocoServer();

    private static VpnServiceSuccessMocoServer vpnServiceSuccessMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        DriverRegisterManager.registerDriver();
        EsrRegisterManager.registerEsr();
        subnetSuccessMocoServer.start();
        vpnServiceSuccessMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        MsbRegisterManager.unRegisterOverlayMsb();
        DriverRegisterManager.unRegisterDriver();
        EsrRegisterManager.unRegisterEsr();
        subnetSuccessMocoServer.stop();
        vpnServiceSuccessMocoServer.stop();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpCreateCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_CLOUDCPE_TESTCASE);
        HttpRequest httpCreateCloudCpeRequest = httpCreateCloudCpeObject.getRequest();
        Map<String, NetworkElementMO> neData = JsonUtil.fromJson(httpCreateCloudCpeRequest.getData(),
                new TypeReference<Map<String, NetworkElementMO>>() {});
        neData.get("managedElement").setControllerID(Arrays.asList(EsrRegisterManager.getControllerId()));
        neData.get("managedElement").setSiteID(Arrays.asList("SiteId"));
        httpCreateCloudCpeRequest.setData(JsonUtil.toJson(neData));
        execTestCase(httpCreateCloudCpeRequest, new StatusChecker(httpCreateCloudCpeObject.getResponse()));

        HttpRquestResponse httpCreateSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SUBNET_TESTCASE);
        execTestCase(httpCreateSubnetObject.getRequest(),
                new StatusAndBodyChecker(httpCreateSubnetObject.getResponse()));

        HttpRquestResponse httpUpdateSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_SUBNET_TESTCASE);
        execTestCase(httpUpdateSubnetObject.getRequest(), new StatusChecker(httpUpdateSubnetObject.getResponse()));

        HttpRquestResponse httpQuerySubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_SUBNET_TESTCASE);
        execTestCase(httpQuerySubnetObject.getRequest(), new StatusChecker(httpQuerySubnetObject.getResponse()));

        HttpRquestResponse httpBatchQuerySubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_SUBNET_TESTCASE);
        HttpResponse queryResponse = execTestCase(httpBatchQuerySubnetObject.getRequest(),
                new StatusChecker(httpBatchQuerySubnetObject.getResponse()));
        ComplexResult<NbiSubnetModel> queryResultRsp =
                JsonUtil.fromJson(queryResponse.getData(), new TypeReference<ComplexResult<NbiSubnetModel>>() {});
        assertTrue(1 == queryResultRsp.getTotal());

        HttpRquestResponse httpDeleteSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SUBNET_TESTCASE);
        execTestCase(httpDeleteSubnetObject.getRequest(), new StatusChecker(httpDeleteSubnetObject.getResponse()));

        HttpRquestResponse httpDeleteCloudCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_CLOUDCPE_TESTCASE);
        execTestCase(httpDeleteCloudCpeObject.getRequest(), new StatusChecker(httpDeleteCloudCpeObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {
        HttpRquestResponse httpBatchQuerySubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTY_QUERY_SUBNET_TESTCASE);
        execTestCase(httpBatchQuerySubnetObject.getRequest(),
                new StatusAndBodyChecker(httpBatchQuerySubnetObject.getResponse()));
    }

}
