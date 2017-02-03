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

package org.openo.sdno.localsiteservice.inf.inventory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of LogicalTerminationPoint Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface LtpService {

    /**
     * Batch query ports.<br>
     * 
     * @param req HttpServletRequest Object
     * @param portUuids List of Port Uuid
     * @return Ports queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<List<LogicalTernminationPointMO>> batchQuery(HttpServletRequest req, String portUuids)
            throws ServiceException;

}
