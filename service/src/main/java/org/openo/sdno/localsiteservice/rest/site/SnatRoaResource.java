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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.inf.site.SnatService;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.openo.sdno.overlayvpn.util.check.CheckStrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Restful Interface for SNAT Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Path("/sdnolocalsite/v1/snats")
@Controller("SnatRoaResource")
public class SnatRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnatRoaResource.class);

    @Autowired
    private SnatService service;

    /**
     * Query SNatNetMdel by Internet Gateway Id.<br>
     * 
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param internetGatewayId Internet Gateway Id
     * @return List of SNatNetModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SbiSnatNetModel> batchQuery(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @QueryParam("igId") String internetGatewayId) throws ServiceException {

        long beginTime = System.currentTimeMillis();
        LOGGER.debug("Enter batch query method");

        // Check Uuid
        CheckStrUtil.checkUuidStr(internetGatewayId);

        // Query Snat List
        List<SbiSnatNetModel> sNatNetModelList = service.query(request, internetGatewayId);

        LOGGER.debug("Exit batch query method cost time:" + (System.currentTimeMillis() - beginTime));

        return sNatNetModelList;
    }

}
