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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.ParameterServiceException;
import org.openo.sdno.localsiteservice.dao.ModelDataDao;
import org.openo.sdno.overlayvpn.model.v2.subnet.NbiSubnetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Checker Class of Subnet Model.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-14
 */
@Component
public class SubnetModelChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetModelChecker.class);

    /**
     * Check whether NbiSubnetModel is valid.<br>
     * 
     * @param subnetModel NbiSubnetModel need to check
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    public void check(NbiSubnetModel subnetModel) throws ServiceException {

        // Check NbiSubnetModel
        ValidationUtil.validateModel(subnetModel);

        // Check Cidr Exist
        if(checkCidrExist(subnetModel)) {
            LOGGER.error("Subnet Cidr already exist");
            throw new ParameterServiceException("Subnet Cidr already exist");
        }

        // Check Name Exist
        if(checkNameExist(subnetModel)) {
            LOGGER.error("Subnet Name already exist");
            throw new ParameterServiceException("Subnet Name already exist");
        }

        // Vlan Id and Vni should not be non-empty at the same time
        if(StringUtils.isNotBlank(subnetModel.getVni()) && StringUtils.isNotBlank(subnetModel.getVlanId())) {
            LOGGER.error("Subnet Vni and Vlan should not both non-empty");
            throw new ParameterServiceException("Subnet Vni and Vlan should not both non-empty");
        }

        // CidrBlock and CidrBlockSize should not be non-empty at the same time
        if(StringUtils.isNotBlank(subnetModel.getCidrBlock()) && null != subnetModel.getCidrBlockSize()) {
            LOGGER.error("CidrBock and CidrBockSize should not both non-empty");
            throw new ParameterServiceException("CidrBock and CidrBockSize should not both non-empty");
        }

    }

    /**
     * Check Name Existence.<br>
     * 
     * @param subnetModel NbiSubnetModel need to check
     * @return if this name already exist
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    private boolean checkNameExist(NbiSubnetModel subnetModel) throws ServiceException {

        Map<String, Object> filterMap = new HashMap<String, Object>();

        filterMap.put("name", Arrays.asList(subnetModel.getName()));
        filterMap.put("tenantId", Arrays.asList(subnetModel.getTenantId()));

        ModelDataDao<NbiSubnetModel> subnetModelDao = new ModelDataDao<NbiSubnetModel>();
        ResultRsp<List<NbiSubnetModel>> queryResultRsp =
                subnetModelDao.queryByFilter(NbiSubnetModel.class, filterMap, null);

        return CollectionUtils.isNotEmpty(queryResultRsp.getData());
    }

    /**
     * Check Cidr Existence.<br>
     * 
     * @param subnetModel NbiSubnetModel need to check
     * @return true if this cidr already exist
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    private boolean checkCidrExist(NbiSubnetModel subnetModel) throws ServiceException {
        Map<String, Object> filterMap = new HashMap<String, Object>();

        filterMap.put("cidrBlock", Arrays.asList(subnetModel.getCidrBlock()));
        filterMap.put("tenantId", Arrays.asList(subnetModel.getTenantId()));

        ModelDataDao<NbiSubnetModel> subnetModelDao = new ModelDataDao<NbiSubnetModel>();
        ResultRsp<List<NbiSubnetModel>> queryResultRsp =
                subnetModelDao.queryByFilter(NbiSubnetModel.class, filterMap, null);

        return CollectionUtils.isNotEmpty(queryResultRsp.getData());
    }

}
