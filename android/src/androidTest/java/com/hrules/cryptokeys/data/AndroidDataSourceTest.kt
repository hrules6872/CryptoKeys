/*
 * Copyright (c) 2018. Héctor de Isidro - hrules6872
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

package com.hrules.cryptokeys.data

import android.support.test.runner.AndroidJUnit4
import com.hrules.cryptokeys.domain.entities.Item
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val PASSWORD = "aaAA11$$"

@RunWith(AndroidJUnit4::class)
class AndroidDataSourceTest {
  private val dataSource = AndroidDataSource()

  private val expectedItem = Item(
      "TEST",
      "TEST"
  )
  private val expectedList = listOf(expectedItem)

  @Before
  fun before() {
    dataSource.delete()
  }

  @Test
  fun given_a_item_when_get_then_result_equals() {
    dataSource.put(PASSWORD, expectedList)
    assertEquals(expectedList, dataSource.get(PASSWORD))
  }

  @Test
  fun given_some_items_when_get_then_result_equals() {
    val list = listOf(expectedItem, expectedItem.copy(description = "TEST2"), expectedItem)
    dataSource.put(PASSWORD, list)
    assertEquals(list, dataSource.get(PASSWORD))
  }

  @Test
  fun given_a_item_when_delete_all_then_result_not_initialized() {
    dataSource.put(PASSWORD, expectedList)
    assertTrue(dataSource.initialized())

    dataSource.delete()
    assertFalse(dataSource.initialized())
  }
}