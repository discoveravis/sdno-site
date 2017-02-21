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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.inventory.sdk.model.RelationMO;
import org.openo.sdno.overlayvpn.inventory.sdk.util.InventoryDaoUtil;
import org.openo.sdno.overlayvpn.model.v2.uuid.UuidModel;
import org.openo.sdno.overlayvpn.result.ResultRsp;

/**
 * Template class of Model data.<br>
 * 
 * @param <T> Model Data Class
 * @author
 * @version SDNO 0.5 2017-1-14
 */
public class ModelDataDao<T extends UuidModel> {

    /**
     * Query Model by Uuid.<br>
     * 
     * @param clazz Model data class
     * @param uuid Model data uuid
     * @return Model data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<T> query(Class<T> clazz, String uuid) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.query(clazz, uuid, null);
    }

    /**
     * Insert Model data.<br>
     * 
     * @param modelData Model data need to insert
     * @return Model data inserted
     * @throws ServiceException when insert failed
     * @since SDNO 0.5
     */
    public ResultRsp<T> insert(T modelData) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.insert(modelData);
    }

    /**
     * Update Model data.<br>
     * 
     * @param modelData Model data need to update
     * @param updateFileds update fields
     * @return Model data updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    public ResultRsp<T> update(T modelData, String updateFileds) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.update(modelData, updateFileds);
    }

    /**
     * Delete Model data.<br>
     * 
     * @param clazz Model data class
     * @param uuid uuid of model need to delete
     * @return model data deleted
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<String> delete(Class<T> clazz, String uuid) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.delete(clazz, uuid);
    }

    /**
     * Query Model data by filter.<br>
     * 
     * @param clazz Model data class
     * @param filterMap Filter map
     * @param queryResultFields query result fields
     * @return Model data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<T>> queryByFilter(Class<T> clazz, Map<String, Object> filterMap, String queryResultFields)
            throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.queryByFilter(clazz, JsonUtil.toJson(filterMap), queryResultFields);
    }

    /**
     * Batch Insert Model data.<br>
     * 
     * @param modelDataList List of model data need to insert
     * @return model data inserted
     * @throws ServiceException when batch insert failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<T>> batchInsert(List<T> modelDataList) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.batchInsert(modelDataList);
    }

    /**
     * Batch update model data.<br>
     * 
     * @param clazz model data class
     * @param updateModelDataList List of model data need to update
     * @param updateFileds Fields need to update
     * @return model data updated
     * @throws ServiceException when batch update failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<T>> batchUpdate(Class<T> clazz, List<T> updateModelDataList, String updateFileds)
            throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.update(clazz, updateModelDataList, updateFileds);
    }

    /**
     * Batch query model data.<br>
     * 
     * @param clazz model data class
     * @param filter query filter
     * @return model data queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public ResultRsp<List<T>> batchQuery(Class<T> clazz, String filter) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.batchQuery(clazz, filter);
    }

    /**
     * Batch delete model data.<br>
     * 
     * @param clazz model data class
     * @param modelDataList List of model data need to delete
     * @return delete result
     * @throws ServiceException when batch delete failed
     * @since SDNO 0.5
     */
    public ResultRsp<String> batchDelete(Class<T> clazz, List<T> modelDataList) throws ServiceException {

        @SuppressWarnings("unchecked")
        List<String> modelDataUuids = new ArrayList<>(CollectionUtils.collect(modelDataList, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                return ((T)arg0).getUuid();
            }
        }));

        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.batchDelete(clazz, modelDataUuids);
    }

    /**
     * Add RelationMO.<br>
     * 
     * @param realtionMO RelationMO need to add
     * @return operation result
     * @throws ServiceException when add relation failed
     * @since SDNO 0.5
     */
    public ResultRsp<T> addRelation(RelationMO realtionMO) throws ServiceException {
        InventoryDao<T> inventoryDao = new InventoryDaoUtil<T>().getInventoryDao();
        return inventoryDao.addRelation(realtionMO);
    }

}
