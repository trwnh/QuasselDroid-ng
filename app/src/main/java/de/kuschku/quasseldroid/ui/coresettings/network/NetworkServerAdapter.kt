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

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import de.kuschku.libquassel.quassel.syncables.interfaces.INetwork
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.util.helper.getVectorDrawableCompat
import de.kuschku.quasseldroid.util.helper.styledAttributes
import de.kuschku.quasseldroid.util.helper.tint
import java.util.*

class NetworkServerAdapter(
  private val clickListener: (INetwork.Server) -> Unit,
  private val dragListener: (NetworkServerViewHolder) -> Unit
) : RecyclerView.Adapter<NetworkServerAdapter.NetworkServerViewHolder>() {
  private val data = mutableListOf<INetwork.Server>()
  var list: List<INetwork.Server>
    get() = data
    set(value) {
      val length = data.size
      data.clear()
      notifyItemRangeRemoved(0, length)
      data.addAll(value)
      notifyItemRangeInserted(0, list.size)
    }

  fun add(item: INetwork.Server) {
    val index = data.size
    data.add(item)
    notifyItemInserted(index)
  }

  fun indexOf(item: INetwork.Server): Int? {
    for ((index, it) in data.withIndex()) {
      if (it == item) {
        return index
      }
    }
    return null
  }

  fun replace(old: INetwork.Server, new: INetwork.Server) {
    indexOf(old)?.let {
      data[it] = new
      notifyItemChanged(it)
    }
  }

  fun remove(index: Int) {
    data.removeAt(index)
    notifyItemRemoved(index)
  }

  fun move(from: Int, to: Int) {
    Collections.swap(data, from, to)
    notifyItemMoved(from, to)
  }

  override fun getItemCount() = data.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NetworkServerViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.settings_network_server, parent, false),
    clickListener,
    dragListener
  )

  override fun onBindViewHolder(holder: NetworkServerViewHolder, position: Int) {
    holder.bind(data[position])
  }

  class NetworkServerViewHolder(
    itemView: View,
    clickListener: (INetwork.Server) -> Unit,
    dragListener: (NetworkServerViewHolder) -> Unit
  ) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.host)
    lateinit var host: TextView

    @BindView(R.id.port)
    lateinit var port: TextView

    @BindView(R.id.ssl_enabled)
    lateinit var sslEnabled: ImageView

    @BindView(R.id.handle)
    lateinit var handle: View

    private var item: INetwork.Server? = null

    private val secure: Drawable?
    private val partiallySecure: Drawable?
    private val insecure: Drawable?

    init {
      ButterKnife.bind(this, itemView)
      itemView.setOnClickListener {
        item?.let {
          clickListener(it)
        }
      }
      handle.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
          dragListener.invoke(this)
        }
        false
      }
      secure = itemView.context.getVectorDrawableCompat(R.drawable.ic_lock)?.mutate()
      partiallySecure = itemView.context.getVectorDrawableCompat(R.drawable.ic_lock)?.mutate()
      insecure = itemView.context.getVectorDrawableCompat(R.drawable.ic_lock_open)?.mutate()
      itemView.context.theme.styledAttributes(
        R.attr.colorTintSecure,
        R.attr.colorTintPartiallySecure,
        R.attr.colorTintInsecure
      ) {
        secure?.tint(getColor(0, 0))
        partiallySecure?.tint(getColor(1, 0))
        insecure?.tint(getColor(2, 0))
      }
    }

    fun bind(item: INetwork.Server) {
      this.item = item
      host.text = item.host
      port.text = item.port.toString()
      sslEnabled.setImageDrawable(
        when {
          item.useSsl && item.sslVerify -> secure
          item.useSsl                   -> partiallySecure
          else                          -> insecure
        }
      )
    }
  }
}
