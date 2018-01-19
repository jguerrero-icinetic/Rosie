/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.characters.repository;

import com.karumi.rosie.repository.PaginatedRosieRepository;
import com.karumi.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.karumi.rosie.sample.characters.domain.model.CharacterCrash;

import javax.inject.Inject;

public class CharactersCrashRepository extends PaginatedRosieRepository<String, CharacterCrash> {

  @Inject public CharactersCrashRepository(CharacterCrashDataSourceFactory characterDataSourceFactory,
                                           PaginatedCacheDataSource<String, CharacterCrash> inMemoryPaginatedCache) {
    addCacheDataSources(inMemoryPaginatedCache);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);

    PaginatedReadableDataSource<String, CharacterCrash> characterDataSource =
        characterDataSourceFactory.createDataSource();
    addReadableDataSources(characterDataSource);
    addPaginatedReadableDataSources(characterDataSource);
  }
}
