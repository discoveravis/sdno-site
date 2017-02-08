/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.sdno.localsiteservice.inventory.testcase;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;

public class LtpTest extends TestManager {

    private static final String ADD_LTP1_TESTCASE =
            "src/integration-test/resources/ltp/addlogicalterminationpoint1.json";

    private static final String ADD_LTP2_TESTCASE =
            "src/integration-test/resources/ltp/addlogicalterminationpoint2.json";

    private static final String DELETE_LTP_TESTCASE =
            "src/integration-test/resources/ltp/deletelogicalterminationpoint.json";

    private static final String QUERY_TESTCASE = "src/integration-test/resources/ltp/query.json";

    private static final String QUERY_PARAM_WRONG_TESTCASE = "src/integration-test/resources/ltp/queryparamwrong.json";

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpAddLtp1Object = HttpModelUtils.praseHttpRquestResponseFromFile(ADD_LTP1_TESTCASE);
        execTestCase(httpAddLtp1Object.getRequest(), new StatusChecker(httpAddLtp1Object.getResponse()));

        HttpRquestResponse httpAddLtp2Object = HttpModelUtils.praseHttpRquestResponseFromFile(ADD_LTP2_TESTCASE);
        execTestCase(httpAddLtp2Object.getRequest(), new StatusChecker(httpAddLtp2Object.getResponse()));

        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusAndBodyChecker(httpQueryObject.getResponse()));

        HttpRquestResponse httpDeleteLtp1Object = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LTP_TESTCASE);
        httpDeleteLtp1Object.getRequest()
                .setUri(PathReplace.replaceUuid("objectId", httpDeleteLtp1Object.getRequest().getUri(), "LtpId1"));
        execTestCase(httpDeleteLtp1Object.getRequest(), new StatusChecker(httpDeleteLtp1Object.getResponse()));

        HttpRquestResponse httpDeleteLtp2Object = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_LTP_TESTCASE);
        httpDeleteLtp2Object.getRequest()
                .setUri(PathReplace.replaceUuid("objectId", httpDeleteLtp2Object.getRequest().getUri(), "LtpId2"));
        execTestCase(httpDeleteLtp2Object.getRequest(), new StatusChecker(httpDeleteLtp2Object.getResponse()));
    }

    @Test
    public void queryParamWrongTest() throws ServiceException {
        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_PARAM_WRONG_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusChecker(httpQueryObject.getResponse()));
    }

}
