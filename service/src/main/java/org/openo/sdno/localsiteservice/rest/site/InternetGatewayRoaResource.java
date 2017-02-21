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

package org.openo.sdno.localsiteservice.rest.site;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.site.InternetGatewayService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.NbiInternetGatewayModel;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for Internet Gateway Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Path("/sdnolocalsite/v1/internet-gateways")
@Controller("InternetGatewayRoaResource")
public class InternetGatewayRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternetGatewayRoaResource.class);

    @Autowired
    private InternetGatewayService service;

    /**
     * Query One Internet Gateway data.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayId Internet Gateway Id
     * @return Internet Gateway queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiInternetGatewayModel query(@Context HttpServletRequest request,
            @PathParam("uuid") String internetGatewayId) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method internetGatewayId:" + internetGatewayId);

        // Check Uuid
        CheckStrUtil.checkUuidStr(internetGatewayId);

        // Query InternetGateway Model
        NbiInternetGatewayModel internetGatewayModel = service.query(request, internetGatewayId);

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return internetGatewayModel;
    }

    /**
     * Batch Query Internet Gateway data.<br>
     * 
     * @param name InternetGateway name
     * @param tenantId Tenant id
     * @param siteId Site id
     * @param pageNum Page number
     * @param pageSize Page size
     * @return InternetGateway queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComplexResult<NbiInternetGatewayModel> batchQuery(@QueryParam("name") String name,
            @QueryParam("tenantId") String tenantId, @QueryParam("siteId") String siteId,
            @QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        // Batch Query InternetGateway Models
        ComplexResult<NbiInternetGatewayModel> result = service.batchQuery(name, tenantId, siteId, pageNum, pageSize);

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return result;
    }

    /**
     * Create one Internet Gateway.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param internetGatewayModel Internet Gateway need to create
     * @return Internet Gateway created
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiInternetGatewayModel internetGatewayModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter InternetGateway Create method");

        // Check NbiInternetGatewayModel
        ValidationUtil.validateModel(internetGatewayModel);

        // Create InternetGateway
        ResultRsp<NbiInternetGatewayModel> resultRsp = service.create(request, internetGatewayModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("NbiInternetGatewayModel create failed");
            throw new ServiceException("NbiInternetGatewayModel create failed");
        }

        response.setStatus(HttpCode.CREATE_OK);

        Map<String, String> createIntGatewayResultMap = new HashMap<>();
        createIntGatewayResultMap.put("id", resultRsp.getData().getUuid());

        LOGGER.debug("Exit InternetGateway Create method:" + (System.currentTimeMillis() - beginTime));

        return createIntGatewayResultMap;
    }

    /**
     * Delete one Internet Gateway.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayId Internet Gateway Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Context HttpServletRequest request, @PathParam("uuid") String internetGatewayId)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, routeEntityUuid:" + internetGatewayId);

        // Check Uuid
        CheckStrUtil.checkUuidStr(internetGatewayId);

        // Check Existence
        NbiInternetGatewayModel internetGatewayModel = service.query(request, internetGatewayId);
        if(null == internetGatewayModel) {
            LOGGER.error("InternetGatewayModel does not exist");
            return;
        }

        // Delete InternetGateway Model
        service.delete(request, internetGatewayModel);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * Update NbiInternetGatewayModel.<br>
     * 
     * @param request HttpServletRequest Object
     * @param internetGatewayId InternetGateway Id
     * @param internetGatewayModel NbiInternetGatewayModel need to update
     * @return NbiInternetGatewayModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiInternetGatewayModel update(@Context HttpServletRequest request,
            @PathParam("uuid") String internetGatewayId, NbiInternetGatewayModel internetGatewayModel)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, internetGatewayId:" + internetGatewayId);

        // Check Uuid
        CheckStrUtil.checkUuidStr(internetGatewayId);

        // Set Uuid
        internetGatewayModel.setUuid(internetGatewayId);

        // Check Existence
        NbiInternetGatewayModel existedNbiInternetGatewayModel = service.query(request, internetGatewayId);
        if(null == existedNbiInternetGatewayModel) {
            LOGGER.error("Current InternetGatewayModel does not exist, can not update");
            throw new ServiceException("NbiInternetGatewayModel does not exist");
        }

        // Only support name and description update
        existedNbiInternetGatewayModel.setName(internetGatewayModel.getName());
        existedNbiInternetGatewayModel.setDescription(internetGatewayModel.getDescription());

        ResultRsp<NbiInternetGatewayModel> updateResultRsp = service.update(request, existedNbiInternetGatewayModel);
        if(!updateResultRsp.isSuccess()) {
            LOGGER.error("NbiInternetGatewayModel update failed");
            throw new ServiceException("NbiInternetGatewayModel update failed");
        }

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
        return updateResultRsp.getData();
    }

}
