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

package com.hrules.cryptokeys.presentation.views.activities

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.hrules.cryptokeys.BuildConfig
import com.hrules.cryptokeys.R
import com.hrules.cryptokeys.data.AndroidDataSource
import com.hrules.cryptokeys.domain.entities.Item
import com.hrules.cryptokeys.presentation.adapters.ItemAdapter
import com.hrules.cryptokeys.presentation.extensions.showMessageShort
import com.hrules.cryptokeys.presentation.extensions.textWatcher
import com.hrules.cryptokeys.presentation.presenters.MainPresenter
import com.hrules.cryptokeys.presentation.presenters.models.MainModel
import com.hrules.cryptokeys.presentation.resources.AndroidResString
import com.hrules.cryptokeys.presentation.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_entry.*
import kotlinx.android.synthetic.main.layout_password.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivityView : BaseActivity<MainModel, MainPresenter.Contract, MainPresenter>(), MainPresenter.Contract {
  override val presenter: MainPresenter = MainPresenter(AndroidResString, AndroidDataSource())

  private val itemAdapter = ItemAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    edit_password.textWatcher { onTextChanged { text, _, _, _ -> presenter.password(text.toString()) } }
    edit_password.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE && action_password.isEnabled) presenter.action()
      true
    }
    action_password.setOnClickListener { presenter.action() }

    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerView.adapter = itemAdapter

    val bottomSheetBehavior = BottomSheetBehavior.from(layout_entry)
    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onSlide(bottomSheet: View, slideOffset: Float) {
      }

      override fun onStateChanged(bottomSheet: View, newState: Int) {
        when (newState) {
          BottomSheetBehavior.STATE_EXPANDED -> presenter.entry(edit_description.text.toString(), edit_text.text.toString())
          else -> action_add.visibility = View.INVISIBLE
        }
      }
    })
    text_entryTitle.setOnClickListener {
      when (bottomSheetBehavior.state) {
        BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      }
    }

    edit_description.textWatcher {
      onTextChanged { _, _, _, _ -> presenter.entry(edit_description.text.toString(), edit_text.text.toString()) }
    }
    edit_text.textWatcher {
      onTextChanged { _, _, _, _ -> presenter.entry(edit_description.text.toString(), edit_text.text.toString()) }
    }

    action_add.setOnClickListener { presenter.add(edit_description.toString(), edit_text.toString()) }
  }

  override fun onResume() {
    super.onResume()
    presenter.resume()
  }

  override fun unlock(state: Boolean) {
    layout_password.visibility = if (state) View.GONE else View.VISIBLE
    layout_entry.visibility = if (state) View.VISIBLE else View.GONE
    toolbar.subtitle = if (state) getString(R.string.text_unlocked) else String()
  }

  override fun passwordValidate(validated: Boolean) {
    input_password.error = if (!validated) getString(R.string.error_password) else null
  }

  override fun passwordText(password: String) {
    edit_password.setText(if (BuildConfig.DEBUG) "aaAA11$$" else password)
  }

  override fun entryValidate(validated: Boolean) {
    if (validated) action_add.show() else action_add.visibility = View.INVISIBLE
  }

  override fun actionEnabled(state: Boolean) {
    action_password.isEnabled = if (BuildConfig.DEBUG) true else state
  }

  override fun actionTitle(title: String) {
    action_password.text = title
  }

  override fun list(list: List<Item>) {
    itemAdapter.list(list)
  }

  override fun showMessage(message: String) {
    this.showMessageShort(message)
  }
}