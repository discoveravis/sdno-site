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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.site.VlanService;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.vlan.NbiVlanModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for VLAN Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Path("/sdnolocalsite/v1/vlans")
@Controller("VlanRoaResource")
public class VlanRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VlanRoaResource.class);

    @Autowired
    private VlanService service;

    /**
     * Query one Vlan.<br>
     * 
     * @param request HttpServletRequest Object
     * @param vlanUuid Vlan Model Uuid
     * @return VlanModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiVlanModel query(@Context HttpServletRequest request, @PathParam("uuid") String vlanUuid)
            throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method vlanUuid:" + vlanUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(vlanUuid);

        // Query Vlan Model
        ResultRsp<NbiVlanModel> resultRsp = service.query(request, vlanUuid);
        if(!resultRsp.isValid()) {
            LOGGER.error("VlanModel query failed or not exist");
            throw new ServiceException("VlanModel query failed or not exist");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultRsp.getData();
    }

    /**
     * Batch query Vlans.<br>
     * 
     * @param name Vlan name
     * @param tenantId Tenant Id
     * @param siteId Site Id
     * @param pageNum Page Number
     * @param pageSize Page Size
     * @return List of VlanModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComplexResult<NbiVlanModel> batchQuery(@QueryParam("name") String name,
            @QueryParam("tenantId") String tenantId, @QueryParam("siteId") String siteId,
            @QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        ComplexResult<NbiVlanModel> resultRsp = service.batchQuery(name, tenantId, siteId, pageNum, pageSize);

        LOGGER.debug("Exit batch query method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultRsp;
    }

    /**
     * Create one Vlan.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param vlanModel VlanModel need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiVlanModel vlanModel) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create vlan method");

        // Check VlanMode Data
        ValidationUtil.validateModel(vlanModel);

        // Create VlanModel
        ResultRsp<NbiVlanModel> resultRsp = service.create(request, vlanModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("VlanModel create failed");
            throw new ServiceException("VlanModel create failed");
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", resultRsp.getData().getUuid());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create vlan method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultMap;
    }

    /**
     * Delete one Vlan.<br>
     * 
     * @param request HttpServletRequest Object
     * @param vlanUuid VlanModel Uuid
     * @return delete result
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> delete(@Context HttpServletRequest request, @PathParam("uuid") String vlanUuid)
            throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, vlanUuid:" + vlanUuid);

        // Check uuid
        CheckStrUtil.checkUuidStr(vlanUuid);

        // Check whether this data exist
        ResultRsp<NbiVlanModel> queryResultRsp = service.query(request, vlanUuid);
        if(!queryResultRsp.isSuccess()) {
            LOGGER.error("VlanModel query failed");
            throw new ServiceException("VlanModel query failed");
        }

        NbiVlanModel vlanModel = queryResultRsp.getData();

        Map<String, String> resultMap = new HashMap<>();

        // Resource not exist
        if(null == vlanModel) {
            resultMap.put("id", vlanUuid);
            return resultMap;
        }

        // Delete Vlan Model
        ResultRsp<NbiVlanModel> deleteResultRsp = service.delete(request, vlanModel);
        if(!deleteResultRsp.isValid()) {
            LOGGER.error("VlanModel delete failed");
            throw new ServiceException("VlanModel delete failed");
        }

        resultMap.put("id", deleteResultRsp.getData().getUuid());

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultMap;
    }

    /**
     * Update VlanModel data.<br>
     * 
     * @param request HttpServletRequest Object
     * @param vlanUuid VlanModel Uuid
     * @param vlanModel VlanModel need to update
     * @return VlanModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NbiVlanModel update(@Context HttpServletRequest request, @PathParam("uuid") String vlanUuid,
            NbiVlanModel vlanModel) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter upadte method, vlanUuid:" + vlanUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(vlanUuid);

        // Query VlanModel data
        ResultRsp<NbiVlanModel> queryResultRsp = service.query(request, vlanUuid);
        if(!queryResultRsp.isValid()) {
            LOGGER.error("VlanModel  query failed or not exist");
            throw new ServiceException("VlanModel  query failed or not exist");
        }

        // Check VlanModel
        if(null == vlanModel) {
            LOGGER.error("VlanModel  is null");
            throw new ServiceException("VlanModel  is null");
        }

        // Check ports data
        if(CollectionUtils.isEmpty(vlanModel.getPorts())) {
            LOGGER.error("Port list is empty");
            throw new ServiceException("Port list is empty");
        }

        // Merge old and new models
        NbiVlanModel newVlanModel = mergeModel(queryResultRsp.getData(), vlanModel);

        // Validate Model data
        ValidationUtil.validateModel(vlanModel);

        ResultRsp<NbiVlanModel> updateResultRsp = service.update(request, newVlanModel);
        if(!updateResultRsp.isValid()) {
            LOGGER.debug("Vlan Model data update failed");
            throw new ServiceException("Vlan Model data update failed");
        }

        LOGGER.debug("Exit upadte method cost time:" + (System.currentTimeMillis() - beginTime));
        return updateResultRsp.getData();
    }

    /**
     * Merge NbiVlanModel data.<br>
     * 
     * @param oldNbiVlanModel old NbiVlanModel data
     * @param newNbiVlanModel new NbiVlanModel data
     * @return NbiVlanModel data merged
     * @since SDNO 0.5
     */
    private NbiVlanModel mergeModel(NbiVlanModel oldNbiVlanModel, NbiVlanModel newNbiVlanModel) {
        NbiVlanModel mergedNbiVlanModel = new NbiVlanModel();
        mergedNbiVlanModel.copyBasicData(oldNbiVlanModel);

        if(StringUtils.isNotEmpty(newNbiVlanModel.getName())) {
            mergedNbiVlanModel.setName(newNbiVlanModel.getName());
        }

        if(StringUtils.isNotEmpty(newNbiVlanModel.getSiteId())) {
            mergedNbiVlanModel.setSiteId(newNbiVlanModel.getSiteId());
        }

        if(null != newNbiVlanModel.getVlanId()) {
            mergedNbiVlanModel.setVlanId(newNbiVlanModel.getVlanId());
        }

        if(CollectionUtils.isNotEmpty(newNbiVlanModel.getPorts())) {
            mergedNbiVlanModel.setPorts(newNbiVlanModel.getPorts());
        }

        return mergedNbiVlanModel;
    }
}
