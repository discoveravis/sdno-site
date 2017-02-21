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
import org.openo.sdno.localsiteservice.inf.site.RouteEntryService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for RouteEntry Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Path("/sdnolocalsite/v1/route-entries")
@Controller("RouteEntryRoaResource")
public class RouteEntryRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteEntryRoaResource.class);

    @Autowired
    private RouteEntryService service;

    /**
     * Query one RouteEntity.<br>
     * 
     * @param request HttpServletRequest Object
     * @param routeEntryUuid RouteEntity Uuid
     * @return RouteEntity queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiRouteEntryModel query(@Context HttpServletRequest request, @PathParam("uuid") String routeEntryUuid)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method routeEntityUuid:" + routeEntryUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(routeEntryUuid);

        // Query RouteEntry
        ResultRsp<NbiRouteEntryModel> resutRsp = service.query(request, routeEntryUuid);
        if(!resutRsp.isValid()) {
            LOGGER.error("Query RouteEntry failed or does not exist,exception will be throwed");
            throw new ServiceException("Query RouteEntry failed or does not exist");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resutRsp.getData();
    }

    /**
     * Batch query RouteEntitys.<br>
     * 
     * @param name RouteEntry name
     * @param tenantId Tenant Id
     * @param siteId Site Id
     * @param pageNum Page Number
     * @param pageSize Page Size
     * @return List of RouteEntitys queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComplexResult<NbiRouteEntryModel> batchQuery(@QueryParam("name") String name,
            @QueryParam("tenantId") String tenantId, @QueryParam("siteId") String siteId,
            @QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        ComplexResult<NbiRouteEntryModel> result = service.batchQuery(name, tenantId, siteId, pageNum, pageSize);

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return result;
    }

    /**
     * Create one RouteEntry.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param routeEntryModel RouteEntry need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiRouteEntryModel routeEntryModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create route entry method");

        // Check RouteEntryModel
        ValidationUtil.validateModel(routeEntryModel);

        // Create RouteEntryModel
        ResultRsp<NbiRouteEntryModel> resultRsp = service.create(request, routeEntryModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("SubnetModel create failed");
            throw new ServiceException("SubnetModel create failed");
        }

        Map<String, String> createRouteEntryResultMap = new HashMap<>();
        createRouteEntryResultMap.put("id", resultRsp.getData().getUuid());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create route entry method cost time:" + (System.currentTimeMillis() - beginTime));

        return createRouteEntryResultMap;
    }

    /**
     * Delete one RouteEntry.<br>
     * 
     * @param request HttpServletRequest Object
     * @param routeEntryUuid Uuid of RouteEntity which need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Context HttpServletRequest request, @PathParam("uuid") String routeEntryUuid)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, routeEntityUuid:" + routeEntryUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(routeEntryUuid);

        // Query RouteEntryModel
        ResultRsp<NbiRouteEntryModel> resutRsp = service.query(request, routeEntryUuid);
        if(!resutRsp.isValid()) {
            LOGGER.error("Current RouteEntry query failed or does not exist,can not delete");
            throw new ServiceException("Current RouteEntry query failed or does not exist");
        }

        // Delete RouteEntry Model
        service.delete(request, routeEntryUuid);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
    }

    /**
     * Update one RouteEntry.<br>
     * 
     * @param request HttpServletRequest Object
     * @param routeEntryUuid Uuid of RouteEntity which need to update
     * @param routeEntryModel RouteEntryModel need to update
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiRouteEntryModel update(@Context HttpServletRequest request, @PathParam("uuid") String routeEntryUuid,
            NbiRouteEntryModel routeEntryModel) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter update method, routeEntityUuid:" + routeEntryUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(routeEntryUuid);

        // Query RouteEntryModel
        ResultRsp<NbiRouteEntryModel> queryResutRsp = service.query(request, routeEntryUuid);
        if(!queryResutRsp.isValid()) {
            LOGGER.error("Current RouteEntry does not exist");
            throw new ServiceException("Current RouteEntry does not exist");
        }

        NbiRouteEntryModel updateRouteEntryModel = queryResutRsp.getData();
        updateRouteEntryModel.setName(routeEntryModel.getName());
        updateRouteEntryModel.setDescription(routeEntryModel.getDescription());

        // Update RouteEntry Model
        ResultRsp<NbiRouteEntryModel> updateResultRsp = service.update(request, updateRouteEntryModel);
        if(!updateResultRsp.isValid()) {
            LOGGER.error("RouteEntry update failed");
            throw new ServiceException("RouteEntry update failed");
        }

        LOGGER.debug("Exit update method cost time:" + (System.currentTimeMillis() - beginTime));

        return updateResultRsp.getData();
    }

}
