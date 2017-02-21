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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.checker.CpeModelChecker;
import org.openo.sdno.localsiteservice.dao.BaseResourceDao;
import org.openo.sdno.localsiteservice.inf.cpe.CpeOnlineService;
import org.openo.sdno.localsiteservice.model.cpe.VCpePlanInfo;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for Cpe Online Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Path("/sdnolocalsite/v1/cpes")
@Controller("CpeOnlineRoaResource")
public class CpeOnlineRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpeOnlineRoaResource.class);

    @Autowired
    private CpeOnlineService service;

    @Autowired
    private CpeModelChecker modelChecker;

    @Autowired
    private BaseResourceDao baseResourceDao;

    /**
     * Create Cpe Device.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param cpePlanInfo VCpePlanInfo data
     * @return create result
     * @throws ServiceException when create failed
     * @since SDNO 0.5
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> create(@Context HttpServletRequest request, @Context HttpServletResponse response,
            VCpePlanInfo cpePlanInfo) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter create method");

        // Check data
        modelChecker.checkCpePlanInfo(cpePlanInfo);

        // Create Cpe
        ResultRsp<VCpePlanInfo> resultRsp = service.create(request, cpePlanInfo);
        if(!resultRsp.isValid()) {
            LOGGER.error("Create VCpePlanInfo failed");
            throw new ServiceException("Create VCpePlanInfo failed");
        }

        // Query created device
        NetworkElementMO cpeNetworkElement = baseResourceDao.queryNeByEsn(cpePlanInfo.getEsn());
        if(null == cpeNetworkElement) {
            LOGGER.error("Cpe Device create failed");
            throw new ServiceException("Cpe Device create failed");
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", cpeNetworkElement.getId());

        response.setStatus(HttpCode.CREATE_OK);

        LOGGER.debug("Exit create method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultMap;
    }

    /**
     * Delete Cpe Device.<br>
     * 
     * @param request HttpServletRequest Object
     * @param inputUuids Uuids need to delete
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Context HttpServletRequest request, @QueryParam("uuids") String inputUuids)
            throws ServiceException {
        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter delete method");

        List<String> uuids = JsonUtil.fromJson(inputUuids, new TypeReference<List<String>>() {});

        // Check Uuids
        if(CollectionUtils.isEmpty(uuids)) {
            LOGGER.error("uuids parameter is invalid");
            throw new ServiceException("uuids parameter is invalid");
        }

        service.delete(request, uuids);

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));
    }

}
