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
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Interface class of Internet Gateway Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface InternetGatewayService {

    /**
     * Query InternetGateway Model.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayId Internet Gateway Id
     * @return Internet Gateway queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    NbiInternetGatewayModel query(HttpServletRequest request, String internetGatewayId) throws ServiceException;

    /**
     * Batch Query InternetGateway Models.<br>
     * 
     * @param name Internet Gateway name
     * @param tenantId tenant Uuid
     * @param siteId site Uuid
     * @param pageNum number of pages
     * @param pageSize page size
     * @return Internet Gateway queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    ComplexResult<NbiInternetGatewayModel> batchQuery(String name, String tenantId, String siteId, String pageNum,
            String pageSize) throws ServiceException;

    /**
     * Create Internet Gateway Model.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayModel Internet Gateway Model need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiInternetGatewayModel> create(HttpServletRequest request, NbiInternetGatewayModel internetGatewayModel)
            throws ServiceException;

    /**
     * Delete Internet Gateway Model.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayModel InternetGatewayModel need to delete
     * @return Internet Gateway Model deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    void delete(HttpServletRequest request, NbiInternetGatewayModel internetGatewayModel) throws ServiceException;

    /**
     * Update Internet Gateway Model.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayModel InternetGatewayModel need to update
     * @return update result
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    ResultRsp<NbiInternetGatewayModel> update(HttpServletRequest request, NbiInternetGatewayModel internetGatewayModel)
            throws ServiceException;
}
