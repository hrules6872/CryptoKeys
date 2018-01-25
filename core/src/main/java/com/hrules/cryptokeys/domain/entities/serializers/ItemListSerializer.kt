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

package com.hrules.cryptokeys.domain.entities.serializers

import com.hrules.cryptokeys.domain.entities.Item
import com.hrules.cryptokeys.domain.entities.serializers.base.Serializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializerByTypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object ItemListSerializer : Serializer<List<Item>> {
  private val JSONParser: JSON = JSON()

  override fun stringify(input: List<Item>): String {
    val token = object : ParameterizedType {
      override fun getRawType(): Type = List::class.java
      override fun getOwnerType(): Type? = null
      override fun getActualTypeArguments(): Array<Type> = arrayOf(Item::class.java)
    }
    val serial = serializerByTypeToken(token)
    return JSONParser.stringify(serial, input)
  }

  override fun parse(input: String): List<Item> {
    val token = object : ParameterizedType {
      override fun getRawType(): Type = List::class.java
      override fun getOwnerType(): Type? = null
      override fun getActualTypeArguments(): Array<Type> = arrayOf(Item::class.java)
    }
    val serial = serializerByTypeToken(token)
    return JSONParser.parse(serial, input) as List<Item>
  }
}