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

package org.openo.sdno.localsiteservice.moco.inventorydao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.inventory.sdk.model.RelationMO;
import org.openo.sdno.overlayvpn.inventory.sdk.model.RelationPuerMO;
import org.openo.sdno.overlayvpn.result.ResultRsp;

import mockit.Mock;
import mockit.MockUp;

public final class MockInventoryDao<T> extends MockUp<InventoryDao<T>> {

    @Mock
    ResultRsp<List<T>> queryByFilter(Class<T> clazz, String filter, String queryResultFields) throws ServiceException {
        T instance1 = null;
        T instance2 = null;
        try {
            instance1 = clazz.newInstance();
            instance2 = clazz.newInstance();
        } catch(IllegalAccessException | InstantiationException e) {
        }

        List<T> instanceList = new ArrayList<T>();
        instanceList.add(instance1);
        instanceList.add(instance2);

        return new ResultRsp<List<T>>(ErrorCode.OVERLAYVPN_SUCCESS, instanceList);
    }

    @Mock
    public ResultRsp<List<T>> batchQuery(Class<T> clazz, String filter) throws ServiceException {
        T instance1 = null;
        T instance2 = null;
        try {
            instance1 = clazz.newInstance();
            instance2 = clazz.newInstance();
        } catch(IllegalAccessException | InstantiationException e) {
        }

        List<T> instanceList = new ArrayList<T>();
        instanceList.add(instance1);
        instanceList.add(instance2);

        return new ResultRsp<List<T>>(ErrorCode.OVERLAYVPN_SUCCESS, instanceList);
    }

    @Mock
    public ResultRsp<T> query(Class<T> clazz, String uuid, String tenantId) throws ServiceException {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch(IllegalAccessException | InstantiationException e) {
        }

        return new ResultRsp<T>(ErrorCode.OVERLAYVPN_SUCCESS, instance);
    }

    @Mock
    ResultRsp<String> batchDelete(Class<T> clazz, List<String> uuids) throws ServiceException {
        return new ResultRsp<String>(ErrorCode.OVERLAYVPN_SUCCESS);
    }

    @Mock
    public ResultRsp<List<T>> update(Class<T> clazz, List<T> oriUpdateList, String updateFieldListStr) {
        return new ResultRsp<List<T>>(ErrorCode.OVERLAYVPN_SUCCESS, oriUpdateList);
    }

    @Mock
    public ResultRsp<T> insert(T data) throws ServiceException {
        return new ResultRsp<T>(ErrorCode.OVERLAYVPN_SUCCESS, data);
    }

    @Mock
    public ResultRsp<List<T>> batchInsert(List<T> dataList) {
        return new ResultRsp<List<T>>(ErrorCode.OVERLAYVPN_SUCCESS, dataList);
    }

    @Mock
    public ResultRsp<List<RelationPuerMO>> queryByRelation(RelationMO condition) throws ServiceException {
        RelationPuerMO relationPuerMO = new RelationPuerMO();
        relationPuerMO.setSrcUuid("srcUuid");
        relationPuerMO.setDstUuid("dstUuid");
        return new ResultRsp<List<RelationPuerMO>>(ErrorCode.OVERLAYVPN_SUCCESS, Arrays.asList(relationPuerMO));
    }

    @Mock
    public ResultRsp<T> addRelation(RelationMO relationMO) throws ServiceException {
        return new ResultRsp<T>(ErrorCode.OVERLAYVPN_SUCCESS);
    }

}
