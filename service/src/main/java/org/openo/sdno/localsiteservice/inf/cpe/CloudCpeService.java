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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of CloudCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface CloudCpeService {

    /**
     * Query one CloudCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param cloudCpeUuid CloudCpe Uuid
     * @return CloudCpeModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiCloudCpeModel> query(HttpServletRequest req, String cloudCpeUuid) throws ServiceException;

    /**
     * Batch query CloudCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param filterMap Filter Map
     * @return CloudCpeModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<List<NbiCloudCpeModel>> batchQuery(HttpServletRequest req, Map<String, Object> filterMap)
            throws ServiceException;

    /**
     * Create CloudCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param cloudCpeModel CloudCpeModel data need to create
     * @return CloudCpeModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiCloudCpeModel> create(HttpServletRequest req, NbiCloudCpeModel cloudCpeModel) throws ServiceException;

    /**
     * Delete CloudCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param cloudCpeUuid Uuid of Cloud Cpe which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest req, String cloudCpeUuid) throws ServiceException;
}
