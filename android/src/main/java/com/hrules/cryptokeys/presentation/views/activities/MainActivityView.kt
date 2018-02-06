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

package com.hrules.cryptokeys.presentation.views.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.hrules.cryptokeys.BuildConfig
import com.hrules.cryptokeys.R
import com.hrules.cryptokeys.R.id.action_clear
import com.hrules.cryptokeys.R.id.action_visitWebSite
import com.hrules.cryptokeys.data.AndroidDataSource
import com.hrules.cryptokeys.domain.entities.Item
import com.hrules.cryptokeys.presentation.adapters.ItemAdapter
import com.hrules.cryptokeys.presentation.adapters.decorators.SpaceItemDecoration
import com.hrules.cryptokeys.presentation.extensions.hideSoftKeyboardAndClearEditFocus
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

private const val DEBUG_PASSWORD = "aaAA11$$"

class MainActivityView : BaseActivity<MainModel, MainPresenter.Contract, MainPresenter>(), MainPresenter.Contract {
  override val presenter: MainPresenter = MainPresenter(AndroidResString, AndroidDataSource())
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

  private val itemAdapter = ItemAdapter(
      onItemDeleteClick = { position ->
        AlertDialog.Builder(this@MainActivityView)
            .setMessage(getString(R.string.text_confirmation))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.text_yes), { _, _ -> presenter.delete(position) })
            .setNegativeButton(getString(R.string.text_no), null).show()
      },
      onItemCopyClick = { position -> presenter.copy(position) }
  )

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
    recyclerView.addItemDecoration(SpaceItemDecoration(resources.getInteger(R.integer.recyclerView_itemSpace)))
    recyclerView.adapter = itemAdapter

    bottomSheetBehavior = BottomSheetBehavior.from(layout_entry)
    text_entryTitle.setOnClickListener {
      when (bottomSheetBehavior.state) {
        BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      }
    }

    edit_description.textWatcher { onTextChangedShout { presenter.entry(edit_description.text.toString(), edit_text.text.toString()) } }
    edit_text.textWatcher { onTextChangedShout { presenter.entry(edit_description.text.toString(), edit_text.text.toString()) } }

    action_entry.setOnClickListener {
      when (bottomSheetBehavior.state) {
        BottomSheetBehavior.STATE_EXPANDED -> presenter.add(edit_description.text.toString(), edit_text.text.toString())
        else -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.resume()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      action_clear -> {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.app_name), String())
        clipboard.primaryClip = clip
        showMessageShort(getString(R.string.text_clearAll))
      }
      action_visitWebSite -> visitWebsiteClick()
    }
    return true
  }

  override fun onBackPressed() {
    when (bottomSheetBehavior.state) {
      BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      else -> super.onBackPressed()
    }
  }

  private fun visitWebsiteClick() {
    try {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_webSite))))
    } catch (e: Exception) {
    }
  }

  override fun unlock(state: Boolean) {
    layout_password.visibility = if (state) View.GONE else View.VISIBLE
    layout_entry.visibility = if (state) View.VISIBLE else View.GONE
    action_entry.visibility = if (state) View.VISIBLE else View.GONE
    toolbar.subtitle = if (state) getString(R.string.text_unlocked) else String()
  }

  override fun passwordValidate(validate: Boolean) {
    input_password.error = if (!validate) getString(R.string.error_password) else null
  }

  override fun passwordText(password: String) {
    edit_password.setText(if (BuildConfig.DEBUG) DEBUG_PASSWORD else password)
  }

  override fun actionEnabled(state: Boolean) {
    action_password.isEnabled = if (BuildConfig.DEBUG) true else state
  }

  override fun actionTitle(title: String) {
    action_password.text = title
  }

  override fun entryText(description: String, text: String) {
    edit_description.setText(description)
    edit_text.setText(text)
  }

  override fun entryValidate(descriptionValidate: Boolean, textValidate: Boolean) {
    input_description.error = if (!descriptionValidate) getString(R.string.error_empty) else null
    input_text.error = if (!textValidate) getString(R.string.error_empty) else null
  }

  override fun entryError() {
    hideSoftKeyboardAndClearEditFocus(recyclerView)
  }

  override fun list(list: List<Item>) {
    itemAdapter.list(list)
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    hideSoftKeyboardAndClearEditFocus(recyclerView)
  }

  override fun message(message: String) {
    showMessageShort(message)
  }

  override fun copy(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(getString(R.string.app_name), text)
    clipboard.primaryClip = clip
    showMessageShort(getString(R.string.text_copied))
  }
}