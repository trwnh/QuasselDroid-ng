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

package de.kuschku.quasseldroid.ui.info.user

import android.content.Context
import android.content.Intent
import de.kuschku.libquassel.protocol.BufferId
import de.kuschku.libquassel.protocol.NetworkId
import de.kuschku.quasseldroid.util.ui.settings.ServiceBoundSettingsActivity

class UserInfoActivity : ServiceBoundSettingsActivity(UserInfoFragment()) {
  companion object {
    fun launch(
      context: Context,
      openBuffer: Boolean,
      bufferId: BufferId? = null,
      nick: String? = null,
      networkId: NetworkId? = null
    ) = context.startActivity(intent(context, openBuffer, bufferId, nick, networkId))

    fun intent(
      context: Context,
      openBuffer: Boolean,
      bufferId: BufferId? = null,
      nick: String? = null,
      networkId: NetworkId? = null
    ) = Intent(context, UserInfoActivity::class.java).apply {
      putExtra("openBuffer", openBuffer)
      if (bufferId != null) {
        putExtra("bufferId", bufferId.id)
      }
      if (nick != null) {
        putExtra("nick", nick)
      }
      if (networkId != null) {
        putExtra("networkId", networkId.id)
      }
    }
  }
}
