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

package org.openo.sdno.localsiteservice.rest.pop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.pop.PopService;
import org.openo.sdno.overlayvpn.brs.model.PopMO;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for Pop Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Path("/sdnolocalsite/v1/pops")
@Controller("PopRoaResource")
public class PopRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopRoaResource.class);

    @Autowired
    private PopService service;

    /**
     * Query Pop Data.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param popUuid Pop Uuid
     * @return Pop data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Path("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PopMO query(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @PathParam("uuid") String popUuid) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method popUuid:" + popUuid);

        // Check Uuid
        CheckStrUtil.checkUuidStr(popUuid);

        // Query Pop
        ResultRsp<PopMO> resultRsp = service.query(request, popUuid);
        if(!resultRsp.isValid()) {
            LOGGER.error("This Pop does not exist");
            throw new ServiceException("This Pop does not exist");
        }

        LOGGER.debug("Exit delete method cost time:" + (System.currentTimeMillis() - beginTime));

        return resultRsp.getData();
    }

}
