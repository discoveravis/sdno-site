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

package org.openo.sdno.localsiteservice.vlan.testcase;

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
import org.openo.sdno.localsiteservice.vlan.mocoserver.VlanSuccessMocoServer;
import org.openo.sdno.localsiteservice.vpnservice.mocoserver.VpnServiceSuccessMocoServer;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class VlanSuccessTest extends TestManager {

    private static final String CREATE_SITE_TESTCASE = "src/integration-test/resources/vlan/preload/createsite.json";

    private static final String DELETE_SITE_TESTCASE = "src/integration-test/resources/vlan/preload/deletesite.json";

    private static final String CREATE_LOCALCPE_TESTCASE =
            "src/integration-test/resources/vlan/preload/createlocalcpe.json";

    private static final String DELETE_LOCALCPE_TESTCASE =
            "src/integration-test/resources/vlan/preload/deletelocalcpe.json";

    private static final String CREATE_LOCALCPELTP1_TESTCASE =
            "src/integration-test/resources/vlan/preload/createlocalcpeltp1.json";

    private static final String DELETE_LOCALCPELTP1_TESTCASE =
            "src/integration-test/resources/vlan/preload/deletelocalcpeltp1.json";

    private static final String CREATE_LOCALCPELTP2_TESTCASE =
            "src/integration-test/resources/vlan/preload/createlocalcpeltp2.json";

    private static final String DELETE_LOCALCPELTP2_TESTCASE =
            "src/integration-test/resources/vlan/preload/deletelocalcpeltp2.json";

    private static final String CREATE_VLAN_TESTCASE = "src/integration-test/resources/vlan/testcase/createvlan.json";

    private static final String DELETE_VLAN_TESTCASE = "src/integration-test/resources/vlan/testcase/deletevlan.json";

    private static final String UPDATE_VLAN_TESTCASE = "src/integration-test/resources/vlan/testcase/updatevlan.json";

    private static final String QUERY_VLAN_TESTCASE = "src/integration-test/resources/vlan/testcase/queryvlan.json";

    private static final String QUERY_UPDATED_VLAN_TESTCASE =
            "src/integration-test/resources/vlan/testcase/queryupdatedvlan.json";

    private static final String BATCH_QUERY_VLAN_TESTCASE =
            "src/integration-test/resources/vlan/testcase/batchqueryvlan.json";

    private static final String BATCH_EMPTYQUERY_VLAN_TESTCASE =
            "src/integration-test/resources/vlan/testcase/batchemptyqueryvlan.json";

    private static VlanSuccessMocoServer successMocoServer = new VlanSuccessMocoServer();

    private static VpnServiceSuccessMocoServer vpnServiceSuccessMocoServer = new VpnServiceSuccessMocoServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        MsbRegisterManager.registerOverlayMsb();
        successMocoServer.start();
        DriverRegisterManager.registerDriver();
        EsrRegisterManager.registerEsr();
        vpnServiceSuccessMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        successMocoServer.stop();
        DriverRegisterManager.unRegisterDriver();
        EsrRegisterManager.unRegisterEsr();
        vpnServiceSuccessMocoServer.stop();
        MsbRegisterManager.unRegisterOverlayMsb();
    }

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpCreateSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SITE_TESTCASE);
        execTestCase(httpCreateSiteObject.getRequest(), new StatusChecker(httpCreateSiteObject.getResponse()));

        HttpRquestResponse httpCreateLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_LOCALCPE_TESTCASE);
        HttpRequest httpCreateLocalCpeRequest = httpCreateLocalCpeObject.getRequest();
        Map<String, NetworkElementMO> neData = JsonUtil.fromJson(httpCreateLocalCpeRequest.getData(),
                new TypeReference<Map<String, NetworkElementMO>>() {});
        neData.get("managedElement").setControllerID(Arrays.asList(EsrRegisterManager.getControllerId()));
        httpCreateLocalCpeRequest.setData(JsonUtil.toJson(neData));
        execTestCase(httpCreateLocalCpeRequest, new StatusChecker(httpCreateLocalCpeObject.getResponse()));

        HttpRquestResponse httpCreateLocalCpeLtp1Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_LOCALCPELTP1_TESTCASE);
        execTestCase(httpCreateLocalCpeLtp1Object.getRequest(),
                new StatusChecker(httpCreateLocalCpeLtp1Object.getResponse()));

        HttpRquestResponse httpCreateLocalCpeLtp2Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_LOCALCPELTP2_TESTCASE);
        execTestCase(httpCreateLocalCpeLtp2Object.getRequest(),
                new StatusChecker(httpCreateLocalCpeLtp2Object.getResponse()));

        HttpRquestResponse httpCreateVlanObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_VLAN_TESTCASE);
        execTestCase(httpCreateVlanObject.getRequest(), new StatusAndBodyChecker(httpCreateVlanObject.getResponse()));

        HttpRquestResponse httpQueryVlanObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_VLAN_TESTCASE);
        execTestCase(httpQueryVlanObject.getRequest(), new StatusChecker(httpQueryVlanObject.getResponse()));

        HttpRquestResponse httpBatchQueryVlanObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_VLAN_TESTCASE);
        execTestCase(httpBatchQueryVlanObject.getRequest(), new StatusChecker(httpBatchQueryVlanObject.getResponse()));

        HttpRquestResponse httpBatchEmptyQueryVlanObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTYQUERY_VLAN_TESTCASE);
        execTestCase(httpBatchEmptyQueryVlanObject.getRequest(),
                new StatusAndBodyChecker(httpBatchEmptyQueryVlanObject.getResponse()));

        HttpRquestResponse httpUpdateVlanObject = HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_VLAN_TESTCASE);
        execTestCase(httpUpdateVlanObject.getRequest(), new StatusChecker(httpUpdateVlanObject.getResponse()));

        HttpRquestResponse httpQueryUpdatedVlanObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_UPDATED_VLAN_TESTCASE);
        execTestCase(httpQueryUpdatedVlanObject.getRequest(),
                new StatusChecker(httpQueryUpdatedVlanObject.getResponse()));

        HttpRquestResponse httpDeleteVlanObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_VLAN_TESTCASE);
        execTestCase(httpDeleteVlanObject.getRequest(), new StatusAndBodyChecker(httpDeleteVlanObject.getResponse()));

        HttpRquestResponse httpDeleteSiteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SITE_TESTCASE);
        execTestCase(httpDeleteSiteObject.getRequest(), new StatusChecker(httpDeleteSiteObject.getResponse()));

        HttpRquestResponse httpDeleteLocalCpeObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LOCALCPE_TESTCASE);
        execTestCase(httpDeleteLocalCpeObject.getRequest(), new StatusChecker(httpDeleteLocalCpeObject.getResponse()));

        HttpRquestResponse httpDeleteLocalCpeLtp1Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LOCALCPELTP1_TESTCASE);
        execTestCase(httpDeleteLocalCpeLtp1Object.getRequest(),
                new StatusChecker(httpDeleteLocalCpeLtp1Object.getResponse()));

        HttpRquestResponse httpDeleteLocalCpeLtp2Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LOCALCPELTP2_TESTCASE);
        execTestCase(httpDeleteLocalCpeLtp2Object.getRequest(),
                new StatusChecker(httpDeleteLocalCpeLtp2Object.getResponse()));
    }

}
