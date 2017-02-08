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

package org.openo.sdno.localsiteservice.pop.testcase;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusAndBodyChecker;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class PopTest extends TestManager {

    private static final String ADD_POP_TESTCASE = "src/integration-test/resources/pop/addpop.json";

    private static final String DELETE_POP_TESTCASE = "src/integration-test/resources/pop/deletepop.json";

    private static final String QUERY_POP_TESTCASE = "src/integration-test/resources/pop/query.json";

    private static final String QUERY_UNEXIST_TESTCASE = "src/integration-test/resources/pop/queryunexist.json";

    private static final String QUERY_WRONGFORMAT_TESTCASE = "src/integration-test/resources/pop/querywrongformat.json";

    @Test
    public void successTest() throws ServiceException {

        HttpRquestResponse httpAddPopObject = HttpModelUtils.praseHttpRquestResponseFromFile(ADD_POP_TESTCASE);
        execTestCase(httpAddPopObject.getRequest(), new StatusChecker(httpAddPopObject.getResponse()));

        HttpRquestResponse httpQueryPopObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_POP_TESTCASE);
        execTestCase(httpQueryPopObject.getRequest(), new StatusAndBodyChecker(httpQueryPopObject.getResponse()));

        HttpRquestResponse httpDeletePopObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_POP_TESTCASE);
        execTestCase(httpDeletePopObject.getRequest(), new StatusChecker(httpDeletePopObject.getResponse()));
    }

    @Test
    public void queryUnExistTest() throws ServiceException {
        HttpRquestResponse httpQueryPopObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_UNEXIST_TESTCASE);
        execTestCase(httpQueryPopObject.getRequest(), new StatusChecker(httpQueryPopObject.getResponse()));
    }

    @Test
    public void queryWrongFormatTest() throws ServiceException {
        HttpRquestResponse httpQueryPopObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_WRONGFORMAT_TESTCASE);
        execTestCase(httpQueryPopObject.getRequest(), new StatusChecker(httpQueryPopObject.getResponse()));
    }

}
