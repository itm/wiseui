/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.persistence.dao;

import java.util.List;

/**
 * Generic interface for a Data Access Object (DAO) that encompasses all typical operations
 * that needs to be carried out on business objects (BOs).
 *
 * @param <T> Type of the business objects on which the DAO shall
 *            operate.
 */
public interface Dao<T> {

    T findById(Integer id);

    T update(T object);

    void persist(T object);

    void remove(T object);

    List<T> findAll();
}
