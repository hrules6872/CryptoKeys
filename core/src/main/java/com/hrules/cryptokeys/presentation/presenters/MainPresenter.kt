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
    view?.apply {
      model.password = String()
      passwordText(model.password)
      actionTitle(if (dataSource.initialized()) resString.unlock else resString.create)
      actionEnabled(false)
      unlock(false)

      model.list = mutableListOf()
      list(model.list)
    }
  }

  fun password(text: String) {
    view?.apply {
      val validate = PasswordValidator.validate(text)
      actionEnabled(validate)
      passwordValidate(validate)
    }
  }

  fun action() {
    try {
      var list = listOf<Item>()
      if (dataSource.initialized()) {
        list = dataSource.get(model.password)
      } else {
        dataSource.create(model.password)
      }

      view?.apply {
        actionTitle(resString.unlock)
        actionEnabled(false)
        unlock(true)
        list(list)
      }
    } catch (e: Exception) {
      view?.showMessage(resString.errorUnknown)
    }
  }

  fun entry(description: String, text: String) {
    view?.entryValidate(description.isNotEmpty() && text.isNotEmpty())
  }

  fun add(description: String, text: String) {
    try {
      model.list.add(Item(description, text))
      dataSource.put(model.password, model.list)
      view?.list(model.list)
    } catch (e: Exception) {
      view?.showMessage(resString.errorUnknown)
    }
  }

  interface Contract : BaseView {
    fun unlock(state: Boolean)

    fun passwordValidate(validated: Boolean)
    fun passwordText(password: String)
    fun entryValidate(validated: Boolean)

    fun actionEnabled(state: Boolean)
    fun actionTitle(title: String)

    fun list(list: List<Item>)
    fun showMessage(message: String)
  }
}