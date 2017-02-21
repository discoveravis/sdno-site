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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.localsiteservice.util.RestfulParameterUtil;
import org.openo.sdno.overlayvpn.consts.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * SBI class of Template service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-24
 */
@Service
public class TemplateSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateSbiService.class);

    private static final String TEMPLATE_QUERY_URL = "/openoapi/sdnooverlay/v1/template/policy/{0}/{1}";

    private static final String SPEC_THINCPE_INFO = "thinCpeInfo";

    private static final String FIELD_THINCPE_LAN = "interfaceName_LAN";

    private static final String SPEC_GWPOLICY = "gwPolicy";

    private static final String FIELD_GWPOSITION = "gwPosition";

    private static final String FIELD_PRIOR_DNSSERVER = "priorDnsServerIp";

    private static final String FIELD_STANDBY_DNSSERVER = "standbyDnsServerIp";

    private static final String SPEC_CLOUDCPE = "cloudCpeInfo";

    private static final String FIELD_CPEWAN = "interfaceName_WAN";

    /**
     * Query Lan Interface List of LocalCPE Device.<br>
     * 
     * @param templateName Template name
     * @param localCpeType Type of Local CPE
     * @return List of Lan Port queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public List<String> getLanPortList(String templateName, String localCpeType) throws ServiceException {
        Map<String, Map<String, String>> templateResult = getTemplateData(templateName, SPEC_THINCPE_INFO);
        if(CollectionUtils.isEmpty(templateResult)) {
            LOGGER.error("Query Template spec failed");
            throw new ServiceException("Query Template spec failed");
        }

        Map<String, String> infNameList = templateResult.get(localCpeType);
        if(CollectionUtils.isEmpty(infNameList)) {
            LOGGER.error("Current LocalCpe type does not support");
            throw new ServiceException("Current LocalCpe type does not support");
        }

        String lanInfNameStr = infNameList.get(FIELD_THINCPE_LAN);
        if(StringUtils.isEmpty(lanInfNameStr)) {
            LOGGER.error("LocalCpe does not have Lan Interfaces, the template configuration is wrong");
            throw new ServiceException("LocalCpe LocalCpe  does not have Lan Interfaces");
        }

        String[] lanInfNameList = lanInfNameStr.split(",");
        if(null == lanInfNameList || 0 == lanInfNameList.length) {
            LOGGER.error("LocalCpe does not have Lan Interfaces, the template fomat may be wrong");
            throw new ServiceException("LocalCpe does not have Lan Interfaces");
        }

        return Arrays.asList(lanInfNameList);
    }

    /**
     * Get Gateway Position.<br>
     * 
     * @param templateName template name
     * @return Gateway Position
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getGwPosition(String templateName) throws ServiceException {
        return getTemplateData(templateName, SPEC_GWPOLICY, FIELD_GWPOSITION, null);
    }

    /**
     * Get Prior DNS Server IpAddress.<br>
     * 
     * @param templateName template name
     * @return PriorDnsServer IpAddress
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getPriorDnsServerIp(String templateName) throws ServiceException {
        return getTemplateData(templateName, SPEC_GWPOLICY, FIELD_PRIOR_DNSSERVER, null);
    }

    /**
     * Get Standby DNS Server IpAddress.<br>
     * 
     * @param templateName template name
     * @return StandbyDnsServer IpAddress
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getStandbyDnsServer(String templateName) throws ServiceException {
        return getTemplateData(templateName, SPEC_GWPOLICY, FIELD_STANDBY_DNSSERVER, null);
    }

    /**
     * Get Wan Interface Name.<br>
     * 
     * @param templateName template name
     * @return wan interface name
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String getWanName(String templateName) throws ServiceException {
        return getTemplateData(templateName, SPEC_CLOUDCPE, FIELD_CPEWAN, null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> getTemplateData(String templateName, String specName)
            throws ServiceException {

        String queryUrl = MessageFormat.format(TEMPLATE_QUERY_URL, templateName, specName);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulResponse response = RestfulProxy.get(queryUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query Template configuration failed");
            throw new ServiceException("Query Template configuration failed");
        }

        return JsonUtil.fromJson(response.getResponseContent(), Map.class);
    }

    @SuppressWarnings("unchecked")
    private String getTemplateData(String templateName, String specName, String fieldName, String localCpeType)
            throws ServiceException {

        String queryUrl = MessageFormat.format(TEMPLATE_QUERY_URL, templateName, specName);

        RestfulParametes restfulParameters = new RestfulParametes();
        RestfulParameterUtil.setContentType(restfulParameters);
        RestfulResponse response = RestfulProxy.get(queryUrl, restfulParameters);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Query Template data failed");
            throw new ServiceException("Query Template data failed");
        }

        if(null == localCpeType) {
            Map<String, String> resultMap = JsonUtil.fromJson(response.getResponseContent(), Map.class);
            return resultMap.get(fieldName);
        } else {
            Map<String, Object> resultMap = JsonUtil.fromJson(response.getResponseContent(), Map.class);
            return ((Map<String, String>)resultMap.get(localCpeType)).get(fieldName);
        }
    }
}
