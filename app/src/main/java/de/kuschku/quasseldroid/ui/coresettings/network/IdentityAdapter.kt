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

package de.kuschku.quasseldroid.ui.coresettings.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import de.kuschku.libquassel.protocol.IdentityId
import de.kuschku.libquassel.quassel.syncables.Identity
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.util.ui.ContextThemeWrapper
import de.kuschku.quasseldroid.util.ui.RecyclerSpinnerAdapter

class IdentityAdapter : RecyclerSpinnerAdapter<IdentityAdapter.NetworkViewHolder>(),
                        ThemedSpinnerAdapter {
  var data = emptyList<Identity>()

  fun submitList(list: List<Identity>) {
    data = list
    notifyDataSetChanged()
  }

  override fun isEmpty() = data.isEmpty()

  override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) =
    holder.bind(getItem(position))

  override fun onCreateViewHolder(parent: ViewGroup, dropDown: Boolean)
    : NetworkViewHolder {
    val inflater = LayoutInflater.from(
      if (dropDown) ContextThemeWrapper(parent.context, dropDownViewTheme) else parent.context
    )
    return NetworkViewHolder(inflater.inflate(R.layout.widget_spinner_item_material, parent, false))
  }

  fun indexOf(id: IdentityId): Int? {
    for ((key, item) in data.withIndex()) {
      if (item.id() == id) {
        return key
      }
    }
    return null
  }

  override fun getItem(position: Int): Identity? =
    if (position in 0 until data.size) data[position] else null

  override fun getItemId(position: Int) = getItem(position)?.id()?.id?.toLong() ?: -1

  override fun hasStableIds() = true

  override fun getCount() = data.size

  class NetworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(android.R.id.text1)
    lateinit var text: TextView

    init {
      ButterKnife.bind(this, itemView)
    }

    fun bind(network: Identity?) {
      text.text = network?.identityName()
    }
  }
}
