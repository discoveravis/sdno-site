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

package org.openo.sdno.localsiteservice.inf.site;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;

/**
 * Interface class of Snat Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface SnatService {

    /**
     * Query NatNetModel By InternetGateway Id.<br>
     * 
     * @param req HttpServletRequest object
     * @param internetGatewayId InternetGateway Uuid
     * @return NatNetModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    List<SbiSnatNetModel> query(HttpServletRequest req, String internetGatewayId) throws ServiceException;
}
