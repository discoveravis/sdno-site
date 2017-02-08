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

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.responsechecker.StatusChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class SubnetFailureTest extends TestManager {

    private static final String QUERY_UNEXIST_TESTCASE =
            "src/integration-test/resources/subnet/testcase/queryunexistsubnet.json";

    private static final String CREATE_SUBNET_PARAM_ERROR1_TESTCASE =
            "src/integration-test/resources/subnet/testcase/createsubnetparamerror1.json";

    private static final String CREATE_SUBNET_PARAM_ERROR2_TESTCASE =
            "src/integration-test/resources/subnet/testcase/createsubnetparamerror2.json";

    private static final String UPDATE_UNEXIST_SUBNET_TESTCASE =
            "src/integration-test/resources/subnet/testcase/updateunexistsubnet.json";

    @Test
    public void queryUnExistSubnetTest() throws ServiceException {
        HttpRquestResponse httpQuerySubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_UNEXIST_TESTCASE);
        execTestCase(httpQuerySubnetObject.getRequest(), new StatusChecker(httpQuerySubnetObject.getResponse()));
    }

    @Test
    public void createSubnetParamError1Test() throws ServiceException {
        HttpRquestResponse httpCreateSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SUBNET_PARAM_ERROR1_TESTCASE);
        execTestCase(httpCreateSubnetObject.getRequest(), new StatusChecker(httpCreateSubnetObject.getResponse()));
    }

    @Test
    public void createSubnetParamError2Test() throws ServiceException {
        HttpRquestResponse httpCreateSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SUBNET_PARAM_ERROR2_TESTCASE);
        execTestCase(httpCreateSubnetObject.getRequest(), new StatusChecker(httpCreateSubnetObject.getResponse()));
    }

    @Test
    public void updateUnExistSubnetTest() throws ServiceException {
        HttpRquestResponse httpUpdateSubnetObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_UNEXIST_SUBNET_TESTCASE);
        execTestCase(httpUpdateSubnetObject.getRequest(), new StatusChecker(httpUpdateSubnetObject.getResponse()));
    }

}
