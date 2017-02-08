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

package org.openo.sdno.localsiteservice.inf.testcase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.drivermanager.DriverRegisterManager;
import org.openo.sdno.localsiteservice.inf.mocoserver.InterfaceFailureMocoServer;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.Topology;

public class InterfaceFailureTest extends TestManager {

    private static final String ADAPTER_FAILURE_QUERY_TESTCASE =
            "src/integration-test/resources/interface/testcase/adapterfailurequery.json";

    private static final String NOTEXISTDEVICE_QUERY_TESTCASE =
            "src/integration-test/resources/interface/testcase/notexistdevicequery.json";

    private static final String INVALIDDEVICEID_QUERY_TESTCASE =
            "src/integration-test/resources/interface/testcase/invaliddeviceidquery.json";

    private static final String TOPO_DATA_PATH = "src/integration-test/resources/interface/topodata";

    private static InterfaceFailureMocoServer failureMocoServer = new InterfaceFailureMocoServer();

    private static Topology topo = new Topology(TOPO_DATA_PATH);

    @BeforeClass
    public static void setup() throws ServiceException {
        topo.createInvTopology();
        DriverRegisterManager.registerDriver();
        failureMocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        failureMocoServer.stop();
        DriverRegisterManager.unRegisterDriver();
        topo.clearInvTopology();
    }

    @Test
    public void queryNotExistDeviceInterfaceTest() throws ServiceException {
        HttpRquestResponse httpQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(NOTEXISTDEVICE_QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusChecker(httpQueryObject.getResponse()));
    }

    @Test
    public void queryInvalidDeviceIdInterfaceTest() throws ServiceException {
        HttpRquestResponse httpQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(INVALIDDEVICEID_QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusChecker(httpQueryObject.getResponse()));
    }

    @Test
    public void queryAdpaterFailInterfaceTest() throws ServiceException {
        HttpRquestResponse httpQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(ADAPTER_FAILURE_QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusChecker(httpQueryObject.getResponse()));
    }

}
