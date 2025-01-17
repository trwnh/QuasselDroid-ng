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
import de.kuschku.libquassel.protocol.*
import de.kuschku.libquassel.protocol.Type

@Syncable(name = "BacklogManager")
interface IBacklogManager : ISyncableObject {
  @Slot
  fun requestBacklog(bufferId: BufferId, first: MsgId = MsgId(-1), last: MsgId = MsgId(-1),
                     limit: Int = -1, additional: Int = 0) {
    REQUEST(
      "requestBacklog", ARG(bufferId, QType.BufferId), ARG(first, QType.MsgId),
      ARG(last, QType.MsgId), ARG(limit, Type.Int), ARG(additional, Type.Int)
    )
  }

  @Slot
  fun requestBacklogFiltered(bufferId: BufferId, first: MsgId = MsgId(-1),
                             last: MsgId = MsgId(-1), limit: Int = -1, additional: Int = 0,
                             type: Int = -1, flags: Int = -1) {
    REQUEST(
      "requestBacklogFiltered", ARG(bufferId, QType.BufferId), ARG(first, QType.MsgId),
      ARG(last, QType.MsgId), ARG(limit, Type.Int), ARG(additional, Type.Int), ARG(type, Type.Int),
      ARG(flags, Type.Int)
    )
  }

  @Slot
  fun requestBacklogAll(first: MsgId = MsgId(-1), last: MsgId = MsgId(-1), limit: Int = -1,
                        additional: Int = 0) {
    REQUEST(
      "requestBacklogAll", ARG(first, QType.MsgId), ARG(last, QType.MsgId),
      ARG(limit, Type.Int), ARG(additional, Type.Int)
    )
  }

  @Slot
  fun requestBacklogAllFiltered(first: MsgId = MsgId(-1), last: MsgId = MsgId(-1),
                                limit: Int = -1, additional: Int = 0, type: Int = -1,
                                flags: Int = -1) {
    REQUEST(
      "requestBacklogAllFiltered", ARG(first, QType.MsgId), ARG(last, QType.MsgId),
      ARG(limit, Type.Int), ARG(additional, Type.Int), ARG(type, Type.Int), ARG(flags, Type.Int)
    )
  }

  @Slot
  fun receiveBacklog(bufferId: BufferId, first: MsgId, last: MsgId, limit: Int, additional: Int,
                     messages: QVariantList)

  @Slot
  fun receiveBacklogFiltered(bufferId: BufferId, first: MsgId, last: MsgId, limit: Int,
                             additional: Int, type: Int, flags: Int, messages: QVariantList)

  @Slot
  fun receiveBacklogAll(first: MsgId, last: MsgId, limit: Int, additional: Int,
                        messages: QVariantList)

  @Slot
  fun receiveBacklogAllFiltered(first: MsgId, last: MsgId, limit: Int, additional: Int, type: Int,
                                flags: Int, messages: QVariantList)

  @Slot
  override fun update(properties: QVariantMap) {
    super.update(properties)
  }
}
