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

package org.openo.sdno.localsiteservice.checker;

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.localsiteservice.dao.SiteModelDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Checker Class of Site Model.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-16
 */
@Component
public class SiteModelChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteModelChecker.class);

    @Autowired
    private SiteModelDao siteModelDao;

    /**
     * Check Resource Dependency.<br>
     * 
     * @param siteId Site Uuid
     * @return true if Site has resource dependency, false otherwise
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    public boolean checkResourceDependency(String siteId) throws ServiceException {

        if(checkSubnetVlanDependency(siteId)) {
            return true;
        }

        // Check RouteEntries
        if(CollectionUtils.isNotEmpty(siteModelDao.querySiteRouteEntrys(siteId))) {
            LOGGER.warn("Site includes route entry");
            return true;
        }

        // Check Internet Gateways
        if(null != siteModelDao.querySiteInternetGateways(siteId)) {
            LOGGER.warn("Site includes internet gateway");
            return true;
        }

        // Check VpnGateways
        if(CollectionUtils.isNotEmpty(siteModelDao.querySiteVpnGateways(siteId))) {
            LOGGER.warn("Site includes vpn gateway");
            return true;
        }

        return false;
    }

    private boolean checkSubnetVlanDependency(String siteId) throws ServiceException {

        // Check Subnets
        if(CollectionUtils.isNotEmpty(siteModelDao.querySiteSubnets(siteId))) {
            LOGGER.warn("Site includes subnet data");
            return true;
        }

        // Check Vlans
        if(CollectionUtils.isNotEmpty(siteModelDao.querySiteVlans(siteId))) {
            LOGGER.warn("Site includes vlan data");
            return true;
        }

        return false;
    }

}
