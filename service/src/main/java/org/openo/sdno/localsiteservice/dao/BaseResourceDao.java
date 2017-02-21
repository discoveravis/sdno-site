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

package org.openo.sdno.localsiteservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.LogicalTernminationPointInvDao;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.LogicalTernminationPointMO;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * Dao class of Basic Resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-14
 */
@Repository
public class BaseResourceDao {

    @Autowired
    private NetworkElementInvDao neInvDao;

    @Autowired
    private LogicalTernminationPointInvDao ltpInvDao;

    /**
     * Query Local CPE Device by SiteId and Cpe Role type.<br>
     * 
     * @param siteId Site Uuid
     * @param cpeRoleTpe Cpe Role type
     * @return LocalCPE NetworkElement
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NetworkElementMO queryCpeBySiteIdAndCpeType(String siteId, String cpeRoleTpe) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("siteID", siteId);
        conditionMap.put("neRole", cpeRoleTpe);

        List<NetworkElementMO> networkElementMOList = neInvDao.query(conditionMap);
        if(CollectionUtils.isEmpty(networkElementMOList)) {
            return null;
        }

        return neInvDao.query(networkElementMOList.get(0).getId());
    }

    /**
     * Query LtpMO by Uuid.<br>
     * 
     * @param uuids list of LtpMO Uuid
     * @return list of LtpMO queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<LogicalTernminationPointMO> queryLtpByUuids(List<String> uuids) throws ServiceException {
        return ltpInvDao.query(uuids);
    }

    /**
     * Query NetworkElement by Esn.<br>
     * 
     * @param esn Esn data
     * @return NetworkElement queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public NetworkElementMO queryNeByEsn(String esn) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("serialNumber", esn);
        List<NetworkElementMO> neList = neInvDao.query(conditionMap);
        if(neList.isEmpty()) {
            return null;
        } else if(neList.size() == 1) {
            return neList.get(0);
        } else {
            throw new ServiceException("Esn should be unique");
        }
    }

    /**
     * Query Ne Info By SiteId.<br>
     * 
     * @param siteId Site Uuid
     * @return List of Cpe queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<NetworkElementMO> queryNeBySiteId(String siteId) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("siteID", siteId);
        return neInvDao.query(conditionMap);
    }

    /**
     * Query Interface By name.<br>
     * 
     * @param neId NetworkElement Id
     * @param infName Interface Name
     * @return Interface queried out
     * @throws ServiceException when query interface failed
     * @since SDNO 0.5
     */
    public LogicalTernminationPointMO queryInterfaceByName(String neId, String infName) throws ServiceException {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("meID", neId);
        conditionMap.put("name", infName);
        return ltpInvDao.query(conditionMap).get(0);
    }

}
