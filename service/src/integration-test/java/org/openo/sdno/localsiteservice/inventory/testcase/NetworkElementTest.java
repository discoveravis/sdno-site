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

package org.openo.sdno.localsiteservice.inventory.testcase;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;

public class NetworkElementTest extends TestManager {

    private static final String ADD_NETWORKELEMENT1_TESTCASE =
            "src/integration-test/resources/networkelement/addnetworkelement1.json";

    private static final String ADD_NETWORKELEMENT2_TESTCASE =
            "src/integration-test/resources/networkelement/addnetworkelement2.json";

    private static final String DELETE_NETWORKELEMENT_TESTCASE =
            "src/integration-test/resources/networkelement/deletenetworkelement.json";

    private static final String QUERY_NETWORKELEMENT_TESTCASE =
            "src/integration-test/resources/networkelement/query.json";

    private static final String QUERY_PARAM_WRONG_TESTCASE =
            "src/integration-test/resources/networkelement/queryparamwrong.json";

    @Test
    public void successTest() throws ServiceException {
        HttpRquestResponse httpAddNe1Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(ADD_NETWORKELEMENT1_TESTCASE);
        execTestCase(httpAddNe1Object.getRequest(), new StatusChecker(httpAddNe1Object.getResponse()));

        HttpRquestResponse httpAddNe2Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(ADD_NETWORKELEMENT2_TESTCASE);
        execTestCase(httpAddNe2Object.getRequest(), new StatusChecker(httpAddNe2Object.getResponse()));

        HttpRquestResponse httpQueryObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_NETWORKELEMENT_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusAndBodyChecker(httpQueryObject.getResponse()));

        HttpRquestResponse httpDeleteNe1Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_NETWORKELEMENT_TESTCASE);
        httpDeleteNe1Object.getRequest()
                .setUri(PathReplace.replaceUuid("objectId", httpDeleteNe1Object.getRequest().getUri(), "NeId1"));
        execTestCase(httpDeleteNe1Object.getRequest(), new StatusChecker(httpDeleteNe1Object.getResponse()));

        HttpRquestResponse httpDeleteNe2Object =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_NETWORKELEMENT_TESTCASE);
        httpDeleteNe2Object.getRequest()
                .setUri(PathReplace.replaceUuid("objectId", httpDeleteNe2Object.getRequest().getUri(), "NeId2"));
        execTestCase(httpDeleteNe2Object.getRequest(), new StatusChecker(httpDeleteNe2Object.getResponse()));
    }

    @Test
    public void queryParamWrongTest() throws ServiceException {
        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_PARAM_WRONG_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusChecker(httpQueryObject.getResponse()));
    }

}
