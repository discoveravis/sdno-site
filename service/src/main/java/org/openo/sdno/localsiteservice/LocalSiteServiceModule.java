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

package org.openo.sdno.localsiteservice;

import org.openo.sdno.overlayvpn.inventory.sdk.DbOwerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LocalSite Service Rest Module Initialization.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-01-04
 */
public class LocalSiteServiceModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalSiteServiceModule.class);

    /**
     * Start Rest Module.<br>
     * 
     * @since SDNO 0.5
     */
    public void start() {
        LOGGER.info("Start LocalsiteService roa module");
        DbOwerInfo.init("localsiteservice", "localsitedb");
    }

    /**
     * Stop Rest Module.<br>
     * 
     * @since SDNO 0.5
     */
    public void stop() {
        LOGGER.info("Stop LocalsiteService  roa module");
    }

}
