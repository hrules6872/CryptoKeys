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

package com.hrules.cryptokeys.presentation.base.mvp

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<MODEL : BaseModel<*>, VIEW : BaseView> {
  private var _view: VIEW? = null
  private var _model: MODEL = BaseModel<Any>() as MODEL

  val view
    get() = _view
  val model
    get() = _model

  open fun bind(model: MODEL, view: VIEW) {
    this._view = view
    this._model = model
  }

  open fun viewReady(first: Boolean = true) {}

  open fun unbind() {
    _view = null
  }
}