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

package com.hrules.cryptokeys.presentation.resources

import com.hrules.cryptokeys.R
import com.hrules.cryptokeys.presentation.resources.base.ResString
import com.hrules.cryptokeys.presentation.resources.helpers.AndroidResHelper

object AndroidResString : ResString {
  override val create: String = AndroidResHelper.getString(R.string.action_create)
  override val unlock: String = AndroidResHelper.getString(R.string.action_unlock)

  override val errorUnknown: String = AndroidResHelper.getString(R.string.error_unknown)
  override val errorWrongPassword: String = AndroidResHelper.getString(R.string.error_wrongPassword)
}