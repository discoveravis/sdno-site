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

package org.openo.sdno.localsiteservice.snat.testcase;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class SnatSuccessTest extends TestManager {

    private static final String BATCH_QUERY_TESTCASE = "src/integration-test/resources/snat/testcase/batchquery.json";

    private static final String BATCH_EMPTY_QUERY_TESTCASE =
            "src/integration-test/resources/snat/testcase/batchemptyquery.json";

    private static final String CREATE_SNAT_TESTCASE = "src/integration-test/resources/snat/testcase/createsnat.json";

    private static final String DELETE_SNAT_TESTCASE = "src/integration-test/resources/snat/testcase/deletesnat.json";

    @Test
    public void batchQueryTest() throws ServiceException {
        HttpRquestResponse httpCreateSnatObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SNAT_TESTCASE);
        execTestCase(httpCreateSnatObject.getRequest(), new StatusChecker(httpCreateSnatObject.getResponse()));

        HttpRquestResponse httpQuerySnatObject = HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_QUERY_TESTCASE);
        execTestCase(httpQuerySnatObject.getRequest(), new StatusAndBodyChecker(httpQuerySnatObject.getResponse()));

        HttpRquestResponse httpDeleteSnatObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SNAT_TESTCASE);
        execTestCase(httpDeleteSnatObject.getRequest(), new StatusChecker(httpDeleteSnatObject.getResponse()));
    }

    @Test
    public void batchEmptyQueryTest() throws ServiceException {

        HttpRquestResponse httpBatchQuerySnatObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(BATCH_EMPTY_QUERY_TESTCASE);
        execTestCase(httpBatchQuerySnatObject.getRequest(),
                new StatusAndBodyChecker(httpBatchQuerySnatObject.getResponse()));

    }
}
