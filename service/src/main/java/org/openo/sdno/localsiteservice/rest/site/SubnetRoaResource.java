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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.checker.SubnetModelChecker;
import org.openo.sdno.localsiteservice.inf.site.SubnetService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for Subnet Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Path("/sdnolocalsite/v1/subnets")
@Controller("SubnetRoaResource")
public class SubnetRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetRoaResource.class);

    @Autowired
    private SubnetService service;

    @Autowired
    private SubnetModelChecker modelChecker;

    /**
     * Query one Subnet.<br>
     * 
     * @param request HttpServletRequest Object
     * @param subnetUuid Subnet Uuid
     * @return NbiSubnetModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiSubnetModel query(@Context HttpServletRequest request, @PathParam("uuid") String subnetUuid)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method subnetUuid:" + subnetUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(subnetUuid);

        // Query Subnet
        ResultRsp<NbiSubnetModel> resutRsp = service.query(request, subnetUuid);
        if(!resutRsp.isValid()) {
            LOGGER.error("Query Subnet failed");
            throw new ServiceException("Query Subnet failed");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resutRsp.getData();
    }

    /**
     * Batch query Subnets.<br>
     * 
     * @param request HttpServletRequest Object
     * @return List of Subnets queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComplexResult<NbiSubnetModel> batchQuery(@Context HttpServletRequest request) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query subnet method");

        // Extract query parameter
        String name = request.getParameter("name");
        String tenantId = request.getParameter("tenantId");
        String siteId = request.getParameter("siteId");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        ComplexResult<NbiSubnetModel> result = service.batchQuery(name, tenantId, siteId, pageNum, pageSize);

        LOGGER.debug("Exit batch query subnet method cost time:" + (System.currentTimeMillis() - beginTime));

        return result;
    }

    /**
     * Create Subnet.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param subnetModel Subnet need to create
     * @throws ServiceException when create failed
     * @return operation result with created Subnet
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiSubnetModel subnetModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create method");

        // Check SubnetModel
        modelChecker.check(subnetModel);

        // Create Subnet
        ResultRsp<NbiSubnetModel> resultRsp = service.create(request, subnetModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("SubnetModel create failed");
            throw new ServiceException("SubnetModel create failed");
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", resultRsp.getData().getUuid());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultMap;
    }

    /**
     * Delete Subnet.<br>
     * 
     * @param request HttpServletRequest Object
     * @param subnetUuid Uuid of Subnet which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Context HttpServletRequest request, @PathParam("uuid") String subnetUuid)
            throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, subnetUuid:" + subnetUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(subnetUuid);

        // Check whether this data exist
        ResultRsp<NbiSubnetModel> queryResultRsp = service.query(request, subnetUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("NbiSubnetModel query failed");
            throw new ServiceException("NbiSubnetModel query failed");
        }

        NbiSubnetModel subnetModel = queryResultRsp.getData();

        // Resource not exist
        if(null == subnetModel) {
            return;
        }

        // Delete Subnet Model
        ResultRsp<NbiSubnetModel> deleteResultRsp = service.delete(request, subnetModel);
        if(!deleteResultRsp.isValid()) {
            LOGGER.error("NbiSubnetModel delete failed");
            throw new ServiceException("NbiSubnetModel delete failed");
        }

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * Update Subnet.<br>
     * 
     * @param request HttpServletRequest Object
     * @param subnetUuid Uuid of Subnet which need to update
     * @param subnetModel new NbiSubnetModel
     * @throws ServiceException when update failed
     * @return operation result with updated Subnet
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiSubnetModel update(@Context HttpServletRequest request, @PathParam("uuid") String subnetUuid,
            NbiSubnetModel subnetModel) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter update method, subnetUuid:" + subnetUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(subnetUuid);

        ResultRsp<NbiSubnetModel> queryResultRsp = service.query(request, subnetUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("NbiSubnetModel query failed");
            throw new ServiceException("NbiSubnetModel query failed");
        }

        // Check whether this data exist
        if(!queryResultRsp.isValid()) {
            LOGGER.error("This Subnet does not exist, can not update");
            throw new ServiceException("This Subnet does not exist, can not update");
        }

        // Currently only support description update
        NbiSubnetModel dbNbiSubnetModel = queryResultRsp.getData();
        dbNbiSubnetModel.setDescription(subnetModel.getDescription());

        ResultRsp<NbiSubnetModel> updateResultRsp = service.update(request, dbNbiSubnetModel);
        if(!updateResultRsp.isValid()) {
            LOGGER.error("NbiSubnetModel update failed");
            throw new ServiceException("NbiSubnetModel update failed");
        }

        LOGGER.debug("Exit update method cost time:" + (System.currentTimeMillis() - beginTime));

        return updateResultRsp.getData();
    }

}
