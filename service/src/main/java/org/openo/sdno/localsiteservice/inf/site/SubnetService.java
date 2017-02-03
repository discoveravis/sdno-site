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
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of Subnet Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface SubnetService {

    /**
     * Query one NbiSubnetModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param subnetUuid NbiSubnetModel UUid
     * @return NbiSubnetModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSubnetModel> query(HttpServletRequest req, String subnetUuid) throws ServiceException;

    /**
     * Batch query NbiSubnetModel data.<br>
     * 
     * @param name NbiSubnetModel name
     * @param tenantId tenant Uuid
     * @param siteId site Uuid
     * @param pageNum number of pages
     * @param pageSize page size
     * @return VlanModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ComplexResult<NbiSubnetModel> batchQuery(String name, String tenantId, String siteId, String pageNum, String pageSize)
            throws ServiceException;

    /**
     * Create Subnet Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param subnetModel NbiSubnetModel data need to create
     * @return NbiSubnetModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSubnetModel> create(HttpServletRequest req, NbiSubnetModel subnetModel) throws ServiceException;

    /**
     * Delete Subnet Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param subnetModel NbiSubnetModel need to delete
     * @return NbiSubnetModel data deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSubnetModel> delete(HttpServletRequest req, NbiSubnetModel subnetModel) throws ServiceException;

    /**
     * Update Subnet Model data.<br>
     * 
     * @param req HttpServletRequest request
     * @param subnetModel NbiSubnetModel need to update
     * @return NbiSubnetModel data updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSubnetModel> update(HttpServletRequest req, NbiSubnetModel subnetModel) throws ServiceException;
}
