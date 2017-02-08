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

package org.openo.sdno.localsiteservice.routeentry.testcase;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class RouteEntrySuccessTest extends TestManager {

    private static final String CREATE_TESTCASE = "src/integration-test/resources/routeentry/testcase/create.json";

    private static final String UPDATE_TESTCASE = "src/integration-test/resources/routeentry/testcase/update.json";

    private static final String QUERY_TESTCASE = "src/integration-test/resources/routeentry/testcase/query.json";

    private static final String BATCHQUERY_TESTCASE =
            "src/integration-test/resources/routeentry/testcase/batchquery.json";

    private static final String BATCHEMPTYQUERY_TESTCASE =
            "src/integration-test/resources/routeentry/testcase/batchemptyquery.json";

    private static final String DELETE_TESTCASE = "src/integration-test/resources/routeentry/testcase/delete.json";

    @Test
    public void successTest() throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_TESTCASE);
        execTestCase(httpCreateObject.getRequest(), new StatusAndBodyChecker(httpCreateObject.getResponse()));

        HttpRquestResponse httpUpdateObject = HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_TESTCASE);
        execTestCase(httpUpdateObject.getRequest(), new StatusAndBodyChecker(httpUpdateObject.getResponse()));

        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusAndBodyChecker(httpQueryObject.getResponse()));

        HttpRquestResponse httpBatchQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(BATCHQUERY_TESTCASE);
        execTestCase(httpBatchQueryObject.getRequest(), new StatusAndBodyChecker(httpBatchQueryObject.getResponse()));

        HttpRquestResponse httpDeleteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_TESTCASE);
        execTestCase(httpDeleteObject.getRequest(), new StatusChecker(httpDeleteObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {
        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(BATCHEMPTYQUERY_TESTCASE);
        execTestCase(httpQueryObject.getRequest(), new StatusAndBodyChecker(httpQueryObject.getResponse()));
    }

}
