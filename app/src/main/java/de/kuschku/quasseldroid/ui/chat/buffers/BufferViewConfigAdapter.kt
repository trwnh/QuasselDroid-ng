/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2019 Janne Mareike Koschinski
 * Copyright (c) 2019 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.kuschku.quasseldroid.ui.chat.buffers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import de.kuschku.libquassel.quassel.syncables.BufferViewConfig
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.util.ui.RecyclerSpinnerAdapter

class BufferViewConfigAdapter :
  RecyclerSpinnerAdapter<BufferViewConfigAdapter.BufferViewConfigViewHolder>(),
  ThemedSpinnerAdapter {
  var data = emptyList<BufferViewConfig>()


  private var updateFinishedListener: ((List<BufferViewConfig>) -> Unit)? = null

  fun setOnUpdateFinishedListener(listener: ((List<BufferViewConfig>) -> Unit)?) {
    this.updateFinishedListener = listener
  }

  fun submitList(list: List<BufferViewConfig>) {
    data = list
    notifyDataSetChanged()
    updateFinishedListener?.invoke(list)
  }

  fun indexOf(id: Int) = data.indexOfFirst { it.bufferViewId() == id }

  override fun isEmpty() = data.isEmpty()

  override fun onBindViewHolder(holder: BufferViewConfigViewHolder, position: Int) =
    holder.bind(getItem(position))

  override fun onCreateViewHolder(parent: ViewGroup, dropDown: Boolean)
    : BufferViewConfigViewHolder {
    val inflater = LayoutInflater.from(
      if (dropDown) ContextThemeWrapper(parent.context, dropDownViewTheme)
      else parent.context
    )
    val view = inflater.inflate(R.layout.widget_spinner_item_toolbar, parent, false)
    return BufferViewConfigViewHolder(
      view
    )
  }

  override fun getItem(position: Int): BufferViewConfig? = when (position) {
    in (0 until data.size) -> data[position]
    else                   -> null
  }

  override fun getItemId(position: Int) = getItem(position)?.bufferViewId()?.toLong() ?: -1L

  override fun hasStableIds() = true

  override fun getCount() = data.size

  class BufferViewConfigViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(android.R.id.text1)
    lateinit var text: TextView

    init {
      ButterKnife.bind(this, itemView)
    }

    fun bind(bufferViewConfig: BufferViewConfig?) {
      text.text = bufferViewConfig?.bufferViewName() ?: ""
    }
  }
}
