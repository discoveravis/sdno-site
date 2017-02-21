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

import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.sdno.overlayvpn.security.authentication.HttpContext;

/**
 * Util class of RestParameters.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-11
 */
public class RestfulParameterUtil {

    private RestfulParameterUtil() {
        // empty constructor
    }

    /**
     * Set Content Type.<br>
     * 
     * @param restfulParameters RestfulParametes Object
     * @param contentType Content type
     * @since SDNO 0.5
     */
    public static void setContentType(RestfulParametes restfulParameters, String contentType) {
        restfulParameters.putHttpContextHeader(HttpContext.CONTENT_TYPE_HEADER, contentType);
    }

    /**
     * Set Default Content Type.<br>
     * 
     * @param restfulParameters RestfulParametes Object
     * @since SDNO 0.5
     */
    public static void setContentType(RestfulParametes restfulParameters) {
        restfulParameters.putHttpContextHeader(HttpContext.CONTENT_TYPE_HEADER, "application/json;charset=UTF-8");
    }

    /**
     * Set Controller Uuid.<br>
     * 
     * @param restfulParameters RestfulParametes Object
     * @param ctrlUuid controller uuid
     * @since SDNO 0.5
     */
    public static void setControllerUuid(RestfulParametes restfulParameters, String ctrlUuid) {
        restfulParameters.putHttpContextHeader("X-Driver-Parameter", "extSysID=" + ctrlUuid);
    }
}
