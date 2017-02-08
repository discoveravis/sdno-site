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
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.localsiteservice.inf.site.SnatService;
import org.openo.sdno.overlayvpn.model.v2.internetgateway.SbiSnatNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation class of Snat Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-19
 */
@Service
public class SnatServiceImpl implements SnatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnatServiceImpl.class);

    @Override
    public List<SbiSnatNetModel> query(HttpServletRequest req, String internetGatewayId) throws ServiceException {

        ModelDataDao<SbiSnatNetModel> sNatNetModelInvDao = new ModelDataDao<SbiSnatNetModel>();

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("internetGatewayId", Arrays.asList(internetGatewayId));

        ResultRsp<List<SbiSnatNetModel>> resultRsp =
                sNatNetModelInvDao.queryByFilter(SbiSnatNetModel.class, filterMap, null);
        if(!resultRsp.isValid()) {
            LOGGER.error("SbiSnatNetModel bath query failed");
            throw new ServiceException("SbiSnatNetModel bath query failed");
        }

        return resultRsp.getData();
    }

}
