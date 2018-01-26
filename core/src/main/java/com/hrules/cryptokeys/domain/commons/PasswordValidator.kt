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

package com.hrules.cryptokeys.domain.commons

import java.util.regex.Pattern

private const val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[&!@#$%_/]).{8,20})"

object PasswordValidator {
  private val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)

  fun validate(password: String): Boolean {
    return pattern.matcher(password).matches()
  }
}