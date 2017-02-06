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

package org.openo.sdno.localsiteservice.sbi.site;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.v2.subnet.SbiSubnetNetModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Sbi Service of Subnet Net Model.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class SubnetSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubnetSbiService.class);

    private static final String SUBNET_BASE_URL = "/openoapi/sbi-localsite/v1/device/{0}/subnet";

    private static final String SUBNET_OPERATION_URL = "/openoapi/sbi-localsite/v1/device/{0}/subnet/{1}";

    @Autowired
    private NetworkElementInvDao networkElementInvDao;

    /**
     * Query SubnetNetModels.<br>
     * 
     * @param subnetNetModel SubnetNetModel need to query
     * @return SubnetNetModel queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSubnetNetModel> query(SbiSubnetNetModel subnetNetModel) throws ServiceException {

        String controllerId = subnetNetModel.getControllerId();
        String deviceId = networkElementInvDao.query(subnetNetModel.getNeId()).getNativeID();

        String queryUrl = MessageFormat.format(SUBNET_OPERATION_URL, deviceId, subnetNetModel.getNetworkId());

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        RestfulResponse response = RestfulProxy.get(queryUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query SbiSubnetNetModel failed");
            return new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiSubnetNetModel>>() {});
    }

    /**
     * Create SubnetNetModel.<br>
     * 
     * @param subnetNetModel SubnetNetModel need to create
     * @return SubnetNetModel created
     * @throws ServiceException ServiceException when create failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSubnetNetModel> create(SbiSubnetNetModel subnetNetModel) throws ServiceException {

        String controllerId = subnetNetModel.getControllerId();
        String deviceId = networkElementInvDao.query(subnetNetModel.getNeId()).getNativeID();

        if(StringUtils.isBlank(controllerId) || StringUtils.isBlank(deviceId)) {
            LOGGER.error("Controller Uuid or Device Uuid is invalid");
            return new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        String createUrl = MessageFormat.format(SUBNET_BASE_URL, deviceId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        restfulParameters.setRawData(JsonUtil.toJson(subnetNetModel));
        RestfulResponse response = RestfulProxy.post(createUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Create SubnetModel failed");
            return new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiSubnetNetModel>>() {});
    }

    /**
     * Delete SubnetNetModel.<br>
     * 
     * @param subnetNetModel SbiSubnetNetModel need to delete
     * @return SubnetNetModel deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSubnetNetModel> delete(SbiSubnetNetModel subnetNetModel) throws ServiceException {

        String controllerId = subnetNetModel.getControllerId();
        String deviceId = networkElementInvDao.query(subnetNetModel.getNeId()).getNativeID();

        String deleteUrl = MessageFormat.format(SUBNET_OPERATION_URL, deviceId, subnetNetModel.getNetworkId());

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        RestfulResponse response = RestfulProxy.delete(deleteUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Delete SbiSubnetNetModel failed");
            return new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiSubnetNetModel>>() {});
    }

    /**
     * Update SubnetNetModel.<br>
     * 
     * @param subnetNetModel SbiSubnetNetModel need to update
     * @return SubnetNetModel updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    public ResultRsp<SbiSubnetNetModel> update(SbiSubnetNetModel subnetNetModel) throws ServiceException {

        String controllerId = subnetNetModel.getControllerId();
        String deviceId = networkElementInvDao.query(subnetNetModel.getNeId()).getNativeID();

        String updateUrl = MessageFormat.format(SUBNET_BASE_URL, deviceId);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulParameterUtil.setControllerUuid(restfulParameters, controllerId);
        restfulParameters.setRawData(JsonUtil.toJson(subnetNetModel));
        RestfulResponse response = RestfulProxy.put(updateUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Update SbiSubnetNetModel failed");
            return new ResultRsp<SbiSubnetNetModel>(ErrorCode.OVERLAYVPN_FAILED);
        }

        return JsonUtil.fromJson(response.getResponseContent(), new TypeReference<ResultRsp<SbiSubnetNetModel>>() {});
    }

}
