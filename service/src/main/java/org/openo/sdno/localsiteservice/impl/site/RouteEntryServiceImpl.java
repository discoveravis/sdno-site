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

package org.openo.sdno.localsiteservice.impl.site;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.inf.site.RouteEntryService;
import org.openo.sdno.overlayvpn.model.v2.result.ComplexResult;
import org.openo.sdno.overlayvpn.model.v2.routeentry.NbiRouteEntryModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of RouteEntry Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Service
public class RouteEntryServiceImpl implements RouteEntryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteEntryServiceImpl.class);

    @Override
    public ResultRsp<NbiRouteEntryModel> query(HttpServletRequest req, String routeEntityUuid) throws ServiceException {
        ModelDataDao<NbiRouteEntryModel> routeEntryModelDao = new ModelDataDao<>();
        return routeEntryModelDao.query(NbiRouteEntryModel.class, routeEntityUuid);
    }

    @Override
    public ComplexResult<NbiRouteEntryModel> batchQuery(String name, String tenantId, String siteId, String pageNum,
            String pageSize) throws ServiceException {

        Map<String, Object> filterMap = new HashMap<>();
        if(StringUtils.hasLength(name)) {
            filterMap.put("name", Arrays.asList(name));
        }

        if(StringUtils.hasLength(tenantId)) {
            filterMap.put("tenantId", Arrays.asList(tenantId));
        }

        if(StringUtils.hasLength(siteId)) {
            filterMap.put("siteId", Arrays.asList(siteId));
        }

        ModelDataDao<NbiRouteEntryModel> routeEntryModelDao = new ModelDataDao<>();

        ResultRsp<List<NbiRouteEntryModel>> resultRsp =
                routeEntryModelDao.batchQuery(NbiRouteEntryModel.class, JsonUtil.toJson(filterMap));

        if(!resultRsp.isValid()) {
            LOGGER.error("Batch query RouteEntryModel failed");
            throw new ServiceException("Batch query RouteEntryModel failed");
        }

        ComplexResult<NbiRouteEntryModel> complexResult = new ComplexResult<>();
        complexResult.setData(resultRsp.getData());
        complexResult.setTotal(resultRsp.getData().size());

        return complexResult;
    }

    @Override
    public ResultRsp<NbiRouteEntryModel> create(HttpServletRequest req, NbiRouteEntryModel routeEntryModel)
            throws ServiceException {
        ModelDataDao<NbiRouteEntryModel> routeEntryModelDao = new ModelDataDao<>();
        return routeEntryModelDao.insert(routeEntryModel);
    }

    @Override
    public void delete(HttpServletRequest req, String routeEntityId) throws ServiceException {
        ModelDataDao<NbiRouteEntryModel> routeEntryModelDao = new ModelDataDao<>();
        routeEntryModelDao.delete(NbiRouteEntryModel.class, routeEntityId);
    }

    @Override
    public ResultRsp<NbiRouteEntryModel> update(HttpServletRequest req, NbiRouteEntryModel routeEntryModel)
            throws ServiceException {
        ModelDataDao<NbiRouteEntryModel> routeEntryModelDao = new ModelDataDao<>();
        return routeEntryModelDao.update(routeEntryModel, "name,description");
    }
}
