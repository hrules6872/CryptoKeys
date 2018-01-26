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

package com.hrules.cryptokeys.presentation.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.hrules.cryptokeys.R
import com.hrules.cryptokeys.domain.entities.Item

interface OnClickListener {
  fun onItemCopyClick(position: Int)
  fun onItemDeleteClick(position: Int)
}

class ItemAdapter(private val listener: OnClickListener) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
  private var list: List<Item> = listOf()


  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val description: TextView = itemView.findViewById(R.id.description)
    val text: TextView = itemView.findViewById(R.id.text)
    val delete: ImageButton = itemView.findViewById(R.id.action_delete)
    val copy: ImageButton = itemView.findViewById(R.id.action_copy)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = list[position]
    holder.apply {
      description.text = item.description
      text.text = item.text
      delete.setOnClickListener { listener.onItemDeleteClick(position) }
      copy.setOnClickListener { listener.onItemCopyClick(position) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
      ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_item, parent, false))

  override fun getItemCount(): Int = list.size

  fun list(list: List<Item>) {
    this.list = list
    notifyDataSetChanged()
  }
}