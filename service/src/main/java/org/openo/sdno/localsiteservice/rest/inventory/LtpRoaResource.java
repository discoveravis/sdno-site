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

package org.openo.sdno.localsiteservice.rest.inventory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.inventory.LtpService;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for LogicalTernminationPoint Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Path("/sdnolocalsite/v1/ports")
@Controller("LtpRoaResource")
public class LtpRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LtpRoaResource.class);

    @Autowired
    private LtpService service;

    /**
     * Batch query ports.<br>
     * 
     * @param request HttpServletRequest Object
     * @param portUuids List of Port Uuid
     * @return Ports queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<LogicalTernminationPointMO> query(@Context HttpServletRequest request,
            @QueryParam("uuids") String portUuids) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter query method");

        ResultRsp<List<LogicalTernminationPointMO>> resultRsp = service.batchQuery(request, portUuids);
        if(!resultRsp.isValid()) {
            LOGGER.error("Batch Query LogicalTernminationPointMOs failed");
            throw new ServiceException("Batch Query LogicalTernminationPointMOs failed");
        }

        LOGGER.debug("Exit query method cost time:" + (System.currentTimeMillis() - beginTime));
        return resultRsp.getData();
    }
}
