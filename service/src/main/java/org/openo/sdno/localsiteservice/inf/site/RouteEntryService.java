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
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of RouteEntry Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface RouteEntryService {

    /**
     * Query one RouteEntryModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param routeEntityUuid RouteEntryModel UUid
     * @return RouteEntryModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiRouteEntryModel> query(HttpServletRequest req, String routeEntityUuid) throws ServiceException;

    /**
     * Batch query RouteEntryModel data.<br>
     * 
     * @param name RouteEntry name
     * @param tenantId tenant Uuid
     * @param siteId site Uuid
     * @param pageNum number of pages
     * @param pageSize page size
     * @return VlanModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ComplexResult<NbiRouteEntryModel> batchQuery(String name, String tenantId, String siteId, String pageNum,
            String pageSize) throws ServiceException;

    /**
     * Create RouteEntry Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param routeEntryModel RouteEntryModel data need to create
     * @return RouteEntryModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiRouteEntryModel> create(HttpServletRequest req, NbiRouteEntryModel routeEntryModel) throws ServiceException;

    /**
     * Delete RouteEntry Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param routeEntityId UUid RouteEntryModel which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest req, String routeEntityId) throws ServiceException;

    /**
     * Update RouteEntry Model data.<br>
     * 
     * @param req HttpServletRequest Object
     * @param routeEntryModel RouteEntryModel need to update
     * @return RouteEntryModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiRouteEntryModel> update(HttpServletRequest req, NbiRouteEntryModel routeEntryModel) throws ServiceException;

}
