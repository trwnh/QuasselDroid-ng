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

package de.kuschku.quasseldroid.ui.coresettings.networkserver

import android.content.Context
import android.content.Intent
import de.kuschku.libquassel.quassel.syncables.interfaces.INetwork
import de.kuschku.quasseldroid.util.ui.settings.ServiceBoundSettingsActivity

class NetworkServerActivity : ServiceBoundSettingsActivity(NetworkServerFragment()) {
  companion object {
    fun launch(
      context: Context,
      server: INetwork.Server? = null
    ) = context.startActivity(intent(context, server))

    fun intent(
      context: Context,
      server: INetwork.Server? = null
    ) = Intent(context, NetworkServerActivity::class.java).apply {
      if (server != null) {
        putExtra("server", server)
      }
    }
  }
}
