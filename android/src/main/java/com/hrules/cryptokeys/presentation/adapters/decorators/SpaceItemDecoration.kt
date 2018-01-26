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

package com.hrules.cryptokeys.presentation.adapters.decorators

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

private const val DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM = false
private const val DEFAULT_ADD_SPACE_BELOW_LAST_ITEM = false

class SpaceItemDecoration(
    private val space: Int,
    private val addSpaceAboveFirstItem: Boolean = DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM,
    private val addSpaceBelowLastItem: Boolean = DEFAULT_ADD_SPACE_BELOW_LAST_ITEM
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    super.getItemOffsets(outRect, view, parent, state)
    if (space <= 0) return
    if (addSpaceAboveFirstItem && parent.getChildLayoutPosition(view) < 1 || parent.getChildLayoutPosition(view) >= 1) {
      if (getOrientation(parent) == LinearLayoutManager.VERTICAL) outRect.top = space else outRect.left = space
    }
    if (addSpaceBelowLastItem && parent.getChildAdapterPosition(view) == getTotalItemCount(parent) - 1) {
      if (getOrientation(parent) == LinearLayoutManager.VERTICAL) outRect.bottom = space else outRect.right = space
    }
  }

  private fun getTotalItemCount(parent: RecyclerView): Int = parent.adapter.itemCount

  private fun getOrientation(parent: RecyclerView): Int = if (parent.layoutManager is LinearLayoutManager) {
    (parent.layoutManager as LinearLayoutManager).orientation
  } else {
    throw IllegalStateException("SpaceItemDecoration can only be used with a LinearLayoutManager")
  }
}