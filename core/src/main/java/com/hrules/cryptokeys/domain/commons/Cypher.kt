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

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES/CBC/PKCS5Padding"
private const val SPECIFICATION = "AES"
private const val MIN_KEY_LENGTH = 16
private const val CHAR_FILL = '*'
private val IV = byteArrayOf('K'.toByte(), 'e'.toByte(), 'e'.toByte(), 'p'.toByte(), 'C'.toByte(), 'a'.toByte(), 'l'.toByte(),
    'm'.toByte(), 'a'.toByte(), 'n'.toByte(), 'd'.toByte(), 'C'.toByte(), 'k'.toByte(), 'e'.toByte(), 'y'.toByte(), 's'.toByte())

private val cipher = Cipher.getInstance(ALGORITHM)

fun String.encrypt(password: String): String {
  cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password), getIvParameterSpec())
  return cipher.doFinal(this.toByteArray()).encodeBase64ToString()
}

fun String.decrypt(password: String): String {
  cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password), getIvParameterSpec())
  return String(cipher.doFinal(this.decodeBase64ToByteArray()))
}

private fun getSecretKeySpec(password: String) = SecretKeySpec(
    password.padEnd(MIN_KEY_LENGTH, CHAR_FILL).substring(0, MIN_KEY_LENGTH).toByteArray(),
    SPECIFICATION)

private fun getIvParameterSpec(): IvParameterSpec = IvParameterSpec(IV)