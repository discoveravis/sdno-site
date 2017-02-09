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

package org.openo.sdno.localsiteservice.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.sdno.overlayvpn.security.authentication.HttpContext;

public class RestfulParameterUtilTest {

    @Test
    public void setContentTypeTest() {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        assertTrue("application/json;charset=UTF-8".equals(restfulParameters.get(HttpContext.CONTENT_TYPE_HEADER)));
    }

    @Test
    public void setDefaultContentTypeTest() {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters, "application/json");
        assertTrue("application/json".equals(restfulParameters.get(HttpContext.CONTENT_TYPE_HEADER)));
    }

    public void setControllerUuidTest() {
        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setControllerUuid(restfulParameters, "ControllerId");
        assertTrue("extSysID=ControllerId".equals(restfulParameters.getHttpContextHeader("X-Driver-Parameter")));
    }

}
