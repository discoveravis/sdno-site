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
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of LocalCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface LocalCpeService {

    /**
     * Query one LocalCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param localCpeUuid LocalCpe Uuid
     * @return NetworkElementMO data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NetworkElementMO> query(HttpServletRequest req, String localCpeUuid) throws ServiceException;

    /**
     * Batch query LocalCpe data.<br>
     * 
     * @param siteId Site Uuid
     * @param cpeType LocalCpe Type
     * @param pageNum number of pages
     * @param pageSize page size
     * @return NetworkElementMO data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<List<NetworkElementMO>> batchQuery(String siteId, String cpeType, int pageNum, int pageSize)
            throws ServiceException;

    /**
     * Create LocalCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param localCpeModel LocalCpeModel data need to create
     * @return LocalCpeModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NetworkElementMO> create(HttpServletRequest req, NbiLocalCpeModel localCpeModel) throws ServiceException;

    /**
     * Delete LocalCpe data.<br>
     * 
     * @param req HttpServletRequest object
     * @param localCpeUuid Uuid of LocalCpe which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest req, String localCpeUuid) throws ServiceException;
}
