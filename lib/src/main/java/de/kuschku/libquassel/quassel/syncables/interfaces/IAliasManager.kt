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

package de.kuschku.libquassel.quassel.syncables.interfaces

import de.kuschku.libquassel.annotations.Slot
import de.kuschku.libquassel.annotations.Syncable
import de.kuschku.libquassel.protocol.ARG
import de.kuschku.libquassel.protocol.QVariantMap
import de.kuschku.libquassel.protocol.Type
import de.kuschku.libquassel.quassel.BufferInfo
import java.io.Serializable

@Syncable(name = "AliasManager")
interface IAliasManager : ISyncableObject {
  fun initAliases(): QVariantMap
  fun initSetAliases(aliases: QVariantMap)
  @Slot
  fun addAlias(name: String?, expansion: String?) {
    SYNC("addAlias", ARG(name, Type.QString), ARG(expansion, Type.QString))
  }

  @Slot
  override fun update(properties: QVariantMap) {
    super.update(properties)
  }

  data class Alias(
    val name: String?,
    val expansion: String?
  ) : Serializable

  data class Command(
    val buffer: BufferInfo,
    val message: String
  )
}
