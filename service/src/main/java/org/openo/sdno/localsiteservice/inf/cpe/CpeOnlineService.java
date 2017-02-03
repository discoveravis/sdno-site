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

package org.openo.sdno.localsiteservice.inf.cpe;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of CpeOnline Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface CpeOnlineService {

    /**
     * Create Cpe Model Data.<br>
     * 
     * @param req HttpServletRequest Object
     * @param cpePlanInfo CpeModel need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<VCpePlanInfo> create(HttpServletRequest req, VCpePlanInfo cpePlanInfo) throws ServiceException;

    /**
     * Delete Cpe Model Data.<br>
     * 
     * @param req HttpServletRequest Object
     * @param uuids List of Cpe Model Uuid
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest req, List<String> uuids) throws ServiceException;

}
