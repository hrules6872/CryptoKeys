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

import com.hrules.cryptokeys.App
import com.hrules.cryptokeys.domain.commons.decrypt
import com.hrules.cryptokeys.domain.commons.encrypt
import com.hrules.cryptokeys.domain.entities.Item
import com.hrules.cryptokeys.domain.entities.serializers.ItemListSerializer
import java.io.File

private const val FILE_NAME = "database.dat"

class AndroidDataSource : DataSource {
  override fun get(password: String): List<Item> {
    return ItemListSerializer.parse(getFile().readText().decrypt(password))
  }

  override fun put(password: String, list: List<Item>) {
    getFile().writeText(ItemListSerializer.stringify(list).encrypt(password))
  }

  override fun initialized(): Boolean = getFile().exists()

  override fun delete() {
    getFile().delete()
  }

  private fun getFile(): File = App.instance.filesDir.resolveSibling(FILE_NAME)
}