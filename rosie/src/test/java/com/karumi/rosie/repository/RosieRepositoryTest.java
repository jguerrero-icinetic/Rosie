/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.repository;

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyCacheableItem;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class RosieRepositoryTest extends UnitTest {

  @Mock private DataSource<AnyCacheableItem> inMemoryDataSource;
  @Mock private DataSource<AnyCacheableItem> apiDataSource;

  @Test public void shouldReturnNullIfThereAreNoDataSourcesWithData() throws Exception {
    givenTheDataSourcesHasNoData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertNull(data);
  }

  @Test public void shouldReturnDataFromTheFirstDataSourceIfTheDataIsValid() throws Exception {
    Collection<AnyCacheableItem> cacheData = givenTheCacheReturnsValidData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(cacheData, data);
  }

  @Test public void shouldReturnDataFromTheLastDataSourceIfTheFirstOneReturnNullOnGetAll()
      throws Exception {
    Collection<AnyCacheableItem> apiData = givenTheCacheReturnsNullAndTheApiReturnSomeData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(apiData, data);
  }

  @Test public void shouldReturnDataFromTheLastDataSourceIfTheFirstOneReturnsNoValidData()
      throws Exception {
    Collection<AnyCacheableItem> apiData = givenTheCacheReturnsNoValidDataAndTheApiReturnsData();
    RosieRepository<AnyCacheableItem> repository = givenARepositoryWithTwoDataSources();

    Collection<AnyCacheableItem> data = repository.getAll();

    assertEquals(apiData, data);
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsNoValidDataAndTheApiReturnsData()
      throws Exception {
    Collection<AnyCacheableItem> cacheData = getSomeItems();
    Collection<AnyCacheableItem> apiData = getSomeItems();
    when(inMemoryDataSource.getAll()).thenReturn(cacheData);
    when(inMemoryDataSource.isValidItem(any(AnyCacheableItem.class))).thenReturn(false);
    when(apiDataSource.getAll()).thenReturn(apiData);
    return apiData;
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsValidData() throws Exception {
    Collection<AnyCacheableItem> data = getSomeItems();
    when(inMemoryDataSource.getAll()).thenReturn(data);
    when(inMemoryDataSource.isValidItem(any(AnyCacheableItem.class))).thenReturn(true);
    when(apiDataSource.getAll()).thenReturn(null);
    return data;
  }

  private void givenTheDataSourcesHasNoData() throws Exception {
    when(inMemoryDataSource.getAll()).thenReturn(null);
    when(apiDataSource.getAll()).thenReturn(null);
  }

  private Collection<AnyCacheableItem> givenTheCacheReturnsNullAndTheApiReturnSomeData()
      throws Exception {
    when(inMemoryDataSource.getAll()).thenReturn(null);
    Collection<AnyCacheableItem> allData = getSomeItems();
    when(apiDataSource.getAll()).thenReturn(allData);
    return allData;
  }

  private RosieRepository<AnyCacheableItem> givenARepositoryWithTwoDataSources() {
    return RosieRepository.with(inMemoryDataSource, apiDataSource);
  }

  private LinkedList<AnyCacheableItem> getSomeItems() {
    LinkedList<AnyCacheableItem> anyCacheableItems = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      anyCacheableItems.add(new AnyCacheableItem(String.valueOf(i)));
    }
    return anyCacheableItems;
  }
}