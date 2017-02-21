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
import org.openo.sdno.localsiteservice.checker.CpeModelChecker;
import org.openo.sdno.localsiteservice.dao.SiteModelDao;
import org.openo.sdno.localsiteservice.inf.cpe.LocalCpeService;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.model.v2.cpe.NbiLocalCpeModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * Restful Interface for LocalCpe Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Path("/sdnolocalsite/v1/local-cpes")
@Controller("LocalCpeRoaResource")
public class LocalCpeRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCpeRoaResource.class);

    @Autowired
    private LocalCpeService service;

    @Autowired
    private CpeModelChecker modelChecker;

    @Autowired
    private SiteModelDao siteModelDao;

    /**
     * Query Local Cpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param localCpeUuid LocalCpe Uuid
     * @return LocalCpe Data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NetworkElementMO query(@Context HttpServletRequest request, @PathParam("uuid") String localCpeUuid)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method deviceUuid:" + localCpeUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(localCpeUuid);

        ResultRsp<NetworkElementMO> resultRsp = service.query(request, localCpeUuid);
        if(!resultRsp.isValid()) {
            LOGGER.error("Query CloudCpe failed");
            throw new ServiceException("Query CloudCpe failed");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultRsp.getData();
    }

    /**
     * Batch query LocalCpes.<br>
     * 
     * @param request HttpServletRequest Object
     * @param siteId Site Uuid
     * @param cpeType LocalCpe type
     * @param pageSize page size
     * @param pageNum page number
     * @return LocalCpe queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<NetworkElementMO> batchQuery(@Context HttpServletRequest request, @QueryParam("siteId") String siteId,
            @QueryParam("cpeType") String cpeType, @QueryParam("pageSize") int pageSize,
            @QueryParam("pageNum") int pageNum) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        ResultRsp<List<NetworkElementMO>> resultRsp = service.batchQuery(siteId, cpeType, pageNum, pageSize);
        if(!resultRsp.isValid()) {
            LOGGER.error("Batch query CloudCpes failed");
            throw new ServiceException("Batch query CloudCpes failed");
        }

        LOGGER.debug("Exit batch query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resultRsp.getData();
    }

    /**
     * Create LocalCpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param localCpeModel LocalCpe need to create
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            NbiLocalCpeModel localCpeModel) throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create method, localCpeModel:" + localCpeModel);
        if(!StringUtils.hasLength(localCpeModel.getEsn()) || "null".equals(localCpeModel.getEsn())) {
            LOGGER.error("Esn value is invalid");
            throw new ServiceException("Esn value is invalid");
        }

        // Fill Controller Id
        if(!StringUtils.hasLength(localCpeModel.getControllerId())) {
            String controllerId = siteModelDao.getSiteControllerId(localCpeModel.getSiteId());
            localCpeModel.setControllerId(controllerId);
        }

        // Check LocalCpe Data
        modelChecker.checkLocalCpeModel(localCpeModel);

        ResultRsp<NetworkElementMO> resultRsp = service.create(request, localCpeModel);
        if(!resultRsp.isValid()) {
            LOGGER.error("Create CloudCpe failed");
            throw new ServiceException("Create CloudCpe failed");
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("id", resultRsp.getData().getId());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create method cost time:" + (System.currentTimeMillis() - beginTime));
        return resultMap;
    }

    /**
     * Delete LocalCpe.<br>
     * 
     * @param request HttpServletRequest Object
     * @param localCpeUuid LocalCpe Uuid
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Context HttpServletRequest request, @PathParam("uuid") String localCpeUuid)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method, localCpeUuid:" + localCpeUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(localCpeUuid);

        // Query LocalCpe
        ResultRsp<NetworkElementMO> resultRsp = service.query(request, localCpeUuid);
        if(resultRsp.isSuccess() && null == resultRsp.getData()) {
            LOGGER.info("Current LocalCpe does not exist.");
            return;
        }

        // Delete LocalCpe
        service.delete(request, localCpeUuid);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
    }

}
