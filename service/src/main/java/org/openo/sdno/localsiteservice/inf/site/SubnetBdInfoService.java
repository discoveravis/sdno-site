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

package org.openo.sdno.localsiteservice.inf.site;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface class of SubnetBdInfo Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-9
 */
public interface SubnetBdInfoService {

    /**
     * Query Subnet BdInfo.<br>
     * 
     * @param vni Vni
     * @param neId NeId
     * @return  Subnet BdInfo queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    String getSubnetBdInfo(String vni, String neId) throws ServiceException;
}
