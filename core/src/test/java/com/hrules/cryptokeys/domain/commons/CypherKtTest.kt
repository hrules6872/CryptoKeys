/*
 * 	Copyright (c) 2018. Héctor de Isidro - hrules6872
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

package com.hrules.cryptokeys.domain.commons

import com.hrules.cryptokeys.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

private const val PASSWORD_TEST = "PASSWORD_TEST"

@RunWith(JUnit4::class)
class CypherKtTest {
  @Test
  fun `given a valid json when encrypt then ok`() {
    val validJson = Utils.readFile("json/valid_items.json")
    val encrypted = Utils.readFile("json/valid_items.encrypted")
    assertEquals(encrypted, validJson.encrypt(PASSWORD_TEST))
  }

  @Test
  fun `given a encrypted json when decrypt then ok`() {
    val validJson = Utils.readFile("json/valid_items.json")
    val encrypted = Utils.readFile("json/valid_items.encrypted")
    assertEquals(validJson, encrypted.decrypt(PASSWORD_TEST))
  }
}