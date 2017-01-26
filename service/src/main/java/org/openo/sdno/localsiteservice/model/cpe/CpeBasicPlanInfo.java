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

package org.openo.sdno.localsiteservice.model.cpe;

import org.openo.sdno.overlayvpn.model.v2.basemodel.ModelBase;
import org.openo.sdno.overlayvpn.verify.annotation.AEsn;
import org.openo.sdno.overlayvpn.verify.annotation.AUuid;

/**
 * Model class of Basic Cpe Plan Info.<br>
 * 
 * @author
 * @version SDNO 0.5 2017-1-6
 */
public class CpeBasicPlanInfo extends ModelBase {

    /**
     * Esn of Cpe
     */
    @AEsn
    private String esn;

    /**
     * Old Esn of Cpe
     */
    @AEsn(require = false)
    private String oldEsn;

    /**
     * Controller Uuid
     */
    @AUuid
    private String controllerId;

    /**
     * Cpe Status
     */
    private String status;

    public String getEsn() {
        return esn;
    }

    public void setEsn(String esn) {
        this.esn = esn;
    }

    public String getOldEsn() {
        return oldEsn;
    }

    public void setOldEsn(String oldEsn) {
        this.oldEsn = oldEsn;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
