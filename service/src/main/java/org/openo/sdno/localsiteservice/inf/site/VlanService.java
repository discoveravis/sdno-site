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

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of Vlan Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface VlanService {

    /**
     * Query one NbiVlanModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param vlanUuid NbiVlanModel UUid
     * @return NbiVlanModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiVlanModel> query(HttpServletRequest req, String vlanUuid) throws ServiceException;

    /**
     * Batch query NbiVlanModel data.<br>
     * 
     * @param name NbiVlanModel name
     * @param tenantId tenant Uuid
     * @param siteId site Uuid
     * @param pageNum number of pages
     * @param pageSize page size
     * @return NbiVlanModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ComplexResult<NbiVlanModel> batchQuery(String name, String tenantId, String siteId, String pageNum, String pageSize)
            throws ServiceException;

    /**
     * Create VLAN Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param vlanModel NbiVlanModel data need to create
     * @return NbiVlanModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiVlanModel> create(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException;

    /**
     * Delete VLAN Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param vlanModel NbiVlanModel need to delete
     * @return NbiVlanModel data deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiVlanModel> delete(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException;

    /**
     * Update NbiVlanModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param vlanModel NbiVlanModel data need to update
     * @return NbiVlanModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiVlanModel> update(HttpServletRequest req, NbiVlanModel vlanModel) throws ServiceException;

}
