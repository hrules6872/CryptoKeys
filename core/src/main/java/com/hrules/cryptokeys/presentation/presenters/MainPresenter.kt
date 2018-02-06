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

package com.hrules.cryptokeys.presentation.presenters

import com.hrules.cryptokeys.data.DataSource
import com.hrules.cryptokeys.domain.commons.PasswordValidator
import com.hrules.cryptokeys.domain.entities.Item
import com.hrules.cryptokeys.presentation.base.mvp.BasePresenter
import com.hrules.cryptokeys.presentation.base.mvp.BaseView
import com.hrules.cryptokeys.presentation.presenters.models.MainModel
import com.hrules.cryptokeys.presentation.resources.base.ResString

class MainPresenter(
    private val resString: ResString,
    private val dataSource: DataSource
) : BasePresenter<MainModel, MainPresenter.Contract>() {

  fun resume() {
    view?.run {
      model.password = String()
      passwordText(model.password)
      actionTitle(if (dataSource.initialized()) resString.unlock else resString.create)
      actionEnabled(false)
      unlock(false)

      entryText(String(), String())

      model.list = mutableListOf()
      list(model.list)
    }
  }

  fun password(password: String) {
    model.password = password
    val validate = PasswordValidator.validate(password)
    view?.run {
      actionEnabled(validate)
      passwordValidate(validate)
    }
  }

  fun action() {
    if (PasswordValidator.validate(model.password)) {
      try {
        if (dataSource.initialized()) {
          model.list = dataSource.get(model.password).toMutableList()
        } else {
          dataSource.put(model.password, listOf())
        }

        view?.run {
          actionTitle(resString.unlock)
          actionEnabled(false)
          unlock(true)
          list(model.list)
        }
      } catch (e: Exception) {
        view?.message(resString.errorWrongPassword)
      }
    }
  }

  fun entry(description: String, text: String) {
    val descriptionValidate = description.trim().isNotEmpty()
    val textValidate = text.trim().isNotEmpty()
    view?.entryValidate(descriptionValidate, textValidate)
  }

  fun add(description: String, text: String) {
    if (description.trim().isNotEmpty() and text.trim().isNotEmpty()) {
      try {
        model.list.add(Item(description, text))
        dataSource.put(model.password, model.list)
        view?.run {
          entryText(String(), String())
          list(model.list)
        }
      } catch (e: Exception) {
        view?.message(resString.errorUnknown)
      }
    } else {
      view?.entryError()
    }
  }

  fun delete(position: Int) {
    try {
      model.list.removeAt(position)
      dataSource.put(model.password, model.list)
      view?.list(model.list)
    } catch (e: Exception) {
      view?.message(resString.errorUnknown)
    }
  }

  fun copy(position: Int) {
    if (position < model.list.size) view?.copy(model.list[position].text)
  }

  interface Contract : BaseView {
    fun unlock(state: Boolean)

    fun passwordValidate(validate: Boolean)
    fun passwordText(password: String)

    fun actionEnabled(state: Boolean)
    fun actionTitle(title: String)

    fun entryValidate(descriptionValidate: Boolean, textValidate: Boolean)
    fun entryText(description: String, text: String)
    fun entryError()

    fun list(list: List<Item>)
    fun message(message: String)

    fun copy(text: String)
  }
}