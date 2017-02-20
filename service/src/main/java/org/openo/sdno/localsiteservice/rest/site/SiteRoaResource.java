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
import java.util.List;
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

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.localsiteservice.checker.SiteModelChecker;
import org.openo.sdno.localsiteservice.inf.site.SiteService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.site.NbiSiteModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * Restful Interface for Site Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-01-04
 */
@Path("/sdnolocalsite/v1/sites")
@Controller("SiteRoaResource")
public class SiteRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteRoaResource.class);

    @Autowired
    private SiteService service;

    @Autowired
    private SiteModelChecker modelChecker;

    /**
     * Query one site.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param siteUuid Site Uuid
     * @return Site queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiSiteModel query(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @PathParam("uuid") String siteUuid) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method siteUuid:" + siteUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(siteUuid);

        // Query Site
        ResultRsp<NbiSiteModel> resutRsp = service.query(request, siteUuid);
        if(!resutRsp.isValid()) {
            LOGGER.error("Query Site failed");
            throw new ServiceException("Query Site failed");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resutRsp.getData();
    }

    /**
     * Batch query sites.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param uuids List of Uuid need to query
     * @return List of Sites queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComplexResult<NbiSiteModel> batchQuery(@Context HttpServletRequest request,
            @Context HttpServletResponse response, @QueryParam("uuids") String uuids) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        if(!StringUtils.hasLength(uuids)) {
            LOGGER.error("uuids is empty");
            throw new ParameterServiceException("uuids is empty");
        }

        ComplexResult<NbiSiteModel> result =
                service.batchQuery(request, JsonUtil.fromJson(uuids, new TypeReference<List<String>>() {}));

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return result;
    }

    /**
     * Create one site.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param siteModel Site need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiSiteModel siteModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create method");

        // Check SiteModel
        ValidationUtil.validateModel(siteModel);

        // Allocate Uuid
        if(!StringUtils.hasLength(siteModel.getUuid())) {
            siteModel.setUuid(UuidUtils.createUuid());
        }

        // Create SubnetModel
        ResultRsp<NbiSiteModel> resultRsp = service.create(request, siteModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("NbiSiteModel create failed");
            throw new ServiceException("NbiSiteModel create failed");
        }

        Map<String, String> result = new HashMap<String, String>();
        result.put("id", resultRsp.getData().getUuid());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create method cost time:" + (System.currentTimeMillis() - beginTime));
        return result;
    }

    /**
     * Delete one site.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param siteUuid Uuid of Site which need to delete
     * @return SiteModel deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiSiteModel delete(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @PathParam("uuid") String siteUuid) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, siteUuid:" + siteUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(siteUuid);

        ResultRsp<NbiSiteModel> queryResultRsp = service.query(request, siteUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("VlanModel query failed");
            throw new ServiceException("VlanModel query failed");
        }

        // Check Existence
        if(!queryResultRsp.isValid()) {
            return null;
        }

        // Check Resource Dependency
        if(modelChecker.checkResourceDependency(siteUuid)) {
            LOGGER.error("dependency resource Exist, can not be deleted");
            throw new ServiceException("dependency resource Exist, can not be deleted");
        }

        // Delete Site
        service.delete(request, siteUuid);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));

        return queryResultRsp.getData();
    }

    /**
     * Update Site data.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param siteUuid Uuid of Site need to update
     * @param siteModel NbiSiteModel need to update
     * @return Site updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> update(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @PathParam("uuid") String siteUuid, NbiSiteModel siteModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter update method, siteUuid:" + siteUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(siteUuid);

        ResultRsp<NbiSiteModel> queryResultRsp = service.query(request, siteUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("VlanModel query failed");
            throw new ServiceException("VlanModel query failed");
        }

        // Check Existence
        if(!queryResultRsp.isValid()) {
            LOGGER.error("Current Site does not exist");
            throw new ServiceException("Current Site does not exist");
        }

        siteModel.setUuid(siteUuid);

        // Update Site Model
        ResultRsp<NbiSiteModel> updateResultRsp = service.update(request, siteModel);
        if(!updateResultRsp.isValid()) {
            LOGGER.error("Current Site update failed");
            throw new ServiceException("Current Site update failed");
        }

        NbiSiteModel updatedModel = updateResultRsp.getData();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", updatedModel.getUuid());

        LOGGER.debug("Exit update method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultMap;
    }

}
