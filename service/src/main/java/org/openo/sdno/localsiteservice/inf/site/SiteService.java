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
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of Site Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface SiteService {

    /**
     * Query one NbiSiteModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param siteUuid NbiSiteModel Uuid
     * @return NbiSiteModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSiteModel> query(HttpServletRequest req, String siteUuid) throws ServiceException;

    /**
     * Batch query NbiSiteModel data.<br>
     * 
     * @param name Site name
     * @param siteIds List of Site Uuid
     * @return NbiSiteModel data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ComplexResult<NbiSiteModel> batchQuery(HttpServletRequest req, List<String> siteIds) throws ServiceException;

    /**
     * Create NbiSiteModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param siteModel NbiSiteModel data need to create
     * @return NbiSiteModel created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSiteModel> create(HttpServletRequest req, NbiSiteModel siteModel) throws ServiceException;

    /**
     * Delete Site Model data.<br>
     * 
     * @param req HttpServletRequest object
     * @param siteId UUid NbiSiteModel which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest req, String siteId) throws ServiceException;

    /**
     * Update NbiSiteModel data.<br>
     * 
     * @param req HttpServletRequest object
     * @param siteModel NbiSiteModel data need to update
     * @return NbiSiteModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiSiteModel> update(HttpServletRequest req, NbiSiteModel siteModel) throws ServiceException;

}
