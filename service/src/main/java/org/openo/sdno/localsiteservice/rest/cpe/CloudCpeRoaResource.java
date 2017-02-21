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

package org.openo.sdno.localsiteservice.rest.cpe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.cpe.CloudCpeService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiCloudCpeModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * Restful Interface for CloudCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Path("/sdnolocalsite/v1/cloud-cpes")
@Controller("CloudCpeRoaResource")
public class CloudCpeRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudCpeRoaResource.class);

    @Autowired
    private CloudCpeService service;

    /**
     * Query CloudCpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param cloudCpeUuid CloudCpe Uuid
     * @return CloudCpe queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiCloudCpeModel query(@Context HttpServletRequest request, @PathParam("uuid") String cloudCpeUuid)
            throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method cloudCpeUuid:" + cloudCpeUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(cloudCpeUuid);

        // Query CloudCpe
        ResultRsp<NbiCloudCpeModel> resultRsp = service.query(request, cloudCpeUuid);
        if(!resultRsp.isValid()) {
            LOGGER.error("Query CloudCpe failed");
            throw new ServiceException("Query CloudCpe failed");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultRsp.getData();
    }

    /**
     * Batch query CloudCpes.<br>
     * 
     * @param request HttpServletRequest Object
     * @param tenantId Tenant Uuid
     * @param popId Pop Uuid
     * @param siteId Site Uuid
     * @return CloudCpes queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<NbiCloudCpeModel> batchQuery(@Context HttpServletRequest request,
            @QueryParam("tenantId") String tenantId, @QueryParam("popId") String popId,
            @QueryParam("siteId") String siteId) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        Map<String, Object> filterMap = new HashMap<>();
        if(StringUtils.hasLength(tenantId)) {
            filterMap.put("tenantId", Arrays.asList(tenantId));
        }

        if(StringUtils.hasLength(popId)) {
            filterMap.put("popId", Arrays.asList(popId));
        }

        if(StringUtils.hasLength(siteId)) {
            filterMap.put("siteId", Arrays.asList(siteId));
        }

        ResultRsp<List<NbiCloudCpeModel>> resultRsp = service.batchQuery(request, filterMap);
        if(!resultRsp.isValid()) {
            LOGGER.error("Batch Query CloudCpe failed");
            throw new ServiceException("Batch Query CloudCpe failed");
        }

        LOGGER.debug("Exit batch query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resultRsp.getData();
    }

    /**
     * Create CloudCpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param cloudCpeModel CloudCpe need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiCloudCpeModel cloudCpeModel) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create method");

        ResultRsp<NbiCloudCpeModel> resultRsp = service.create(request, cloudCpeModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("Create CloudCpe failed");
            throw new ServiceException("Create CloudCpe failed");
        }

        Map<String, String> createCloudCpeResultMap = new HashMap<>();
        createCloudCpeResultMap.put("id", resultRsp.getData().getUuid());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create method cost time:" + (System.currentTimeMillis() - beginTime));
        return createCloudCpeResultMap;
    }

    /**
     * Delete CloudCpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param cloudCpeUuid CloudCpe Uuid
     * @return Uuid of NbiCloudCpeModel deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@Context HttpServletRequest request, @PathParam("uuid") String cloudCpeUuid)
            throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method cloudCpeUuid:" + cloudCpeUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(cloudCpeUuid);

        ResultRsp<NbiCloudCpeModel> queryResultRsp = service.query(request, cloudCpeUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("Query CloudCpeModel failed");
            throw new ServiceException("Query CloudCpeModel failed");
        }

        // Check Existence
        if(!queryResultRsp.isValid()) {
            LOGGER.debug("Current  CloudCpeModel do not exist");
            return null;
        }

        // Delete CloudCpe
        service.delete(request, cloudCpeUuid);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));

        return cloudCpeUuid;
    }

}
