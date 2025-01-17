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

package de.kuschku.quasseldroid.ui.coresettings.identity

import de.kuschku.libquassel.util.helper.value
import de.kuschku.quasseldroid.util.ui.settings.fragment.Deletable

class IdentityEditFragment : IdentityBaseFragment(false), Deletable {
  override fun onSave() = identity?.let { (it, data) ->
    applyChanges(data)
    it?.requestUpdate(data.toVariantMap())
    true
  } ?: false

  override fun onDelete() {
    identity?.let { (it, _) ->
      it?.let {
        modelHelper.connectedSession.value?.orNull()?.rpcHandler?.removeIdentity(it.id())
      }
    }
  }
}
