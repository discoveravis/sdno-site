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

package org.openo.sdno.localsiteservice.impl.inventory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.inf.inventory.LtpService;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation class of LogicalTerminationPoint Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-27
 */
@Service
public class LtpServiceImpl implements LtpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LtpServiceImpl.class);

    @Autowired
    private LogicalTernminationPointInvDao ltpInvDao;

    @Override
    public ResultRsp<List<LogicalTernminationPointMO>> batchQuery(HttpServletRequest req, String portUuids)
            throws ServiceException {
        if(!StringUtils.hasLength(portUuids)) {
            LOGGER.error("portUuids is invalid");
            throw new ServiceException("portUuids is invalid");
        }

        List<String> portUuidList = JsonUtil.fromJson(portUuids, new TypeReference<List<String>>() {});

        List<LogicalTernminationPointMO> ltpMOList = ltpInvDao.query(portUuidList);

        return new ResultRsp<List<LogicalTernminationPointMO>>(ErrorCode.OVERLAYVPN_SUCCESS, ltpMOList);
    }

}
