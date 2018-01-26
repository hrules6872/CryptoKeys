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

import org.junit.Assert.assertFalse

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

private const val SYMBOLS = "&!@#\$%_/"

@RunWith(JUnit4::class)
class PasswordValidatorTest {
  @Test
  fun `given an invalid password when validate then fail`() {
    assertFalse(PasswordValidator.validate("1234"))
    assertFalse(PasswordValidator.validate("1234567890123456789012345"))
    assertFalse(PasswordValidator.validate("TEST1234"))
    assertFalse(PasswordValidator.validate("test1234"))
    assertFalse(PasswordValidator.validate("TESTtest1234"))
    assertFalse(PasswordValidator.validate("TEST$SYMBOLS"))
    assertFalse(PasswordValidator.validate("test$SYMBOLS"))
    assertFalse(PasswordValidator.validate("TESTtest$SYMBOLS"))
    assertFalse(PasswordValidator.validate("TESTtest1234-.,;"))
  }

  @Test
  fun `given a valid password when validate then ok`() {
    assertTrue(PasswordValidator.validate("TESTtest1234$SYMBOLS"))
    assertTrue(PasswordValidator.validate("TEte12${SYMBOLS.substring(0..2)}"))
  }
}