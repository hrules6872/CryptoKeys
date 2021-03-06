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

package com.hrules.cryptokeys.presentation.views.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hrules.cryptokeys.presentation.base.mvp.BaseModel
import com.hrules.cryptokeys.presentation.base.mvp.BasePresenter
import com.hrules.cryptokeys.presentation.base.mvp.BaseView
import java.lang.reflect.ParameterizedType


private const val DEFAULT_STATE_KEY = "DEFAULT_STATE_KEY"

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<MODEL : BaseModel<String>, VIEW : BaseView, out PRESENTER : BasePresenter<MODEL, VIEW>> : AppCompatActivity() {
  protected abstract val presenter: PRESENTER

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val arguments = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
    val model = Class.forName(arguments.first().toString().split(" ").last()).newInstance()
    presenter.bind(model as MODEL, this as VIEW)
    presenter.model.load(savedInstanceState?.getString(DEFAULT_STATE_KEY))
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    presenter.viewReady(first = savedInstanceState == null)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    outState?.putString(DEFAULT_STATE_KEY, presenter.model.save())
    super.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    presenter.unbind()
    super.onDestroy()
  }
}