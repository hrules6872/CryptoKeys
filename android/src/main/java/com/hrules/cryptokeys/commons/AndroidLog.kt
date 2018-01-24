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

package com.hrules.cryptokeys.commons

import android.util.Log
import com.hrules.cryptokeys.BuildConfig

object AndroidLog : DebugLog {
  private const val LOG_TAG_MAX_LENGTH = 23
  private const val TEXT_SEPARATOR = " "

  private val isDebuggable: Boolean
    get() = BuildConfig.DEBUG

  private val applicationId: String
    get() {
      return BuildConfig.APPLICATION_ID.take(LOG_TAG_MAX_LENGTH)
    }

  private fun formatMessage(message: String): String {
    val stackTrace = Throwable().stackTrace
    return if (stackTrace.size >= 2) {
      String.format("[%s:%s] -> %s(): %s", stackTrace[2].fileName, stackTrace[2].lineNumber,
          stackTrace[2].methodName, message)
    } else {
      message
    }
  }

  private fun concatenate(objects: List<Any?>): String {
    val stringBuilder = StringBuilder()
    for (o in objects) {
      stringBuilder.append(o).append(TEXT_SEPARATOR)
    }
    return stringBuilder.toString()
  }

  override fun d(vararg values: Any?) {
    if (isDebuggable) {
      Log.d(applicationId, formatMessage(
          concatenate(values.asList())))
    }
  }

  override fun d(value: Any, throwable: Throwable) {
    if (isDebuggable) {
      Log.d(applicationId,
          formatMessage(value.toString()), throwable)
    }
  }
}