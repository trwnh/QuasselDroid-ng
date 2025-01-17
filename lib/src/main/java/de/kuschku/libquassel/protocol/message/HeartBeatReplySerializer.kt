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

package de.kuschku.libquassel.protocol.message

import de.kuschku.libquassel.protocol.QVariant
import de.kuschku.libquassel.protocol.QVariantList
import de.kuschku.libquassel.protocol.Type
import de.kuschku.libquassel.protocol.value
import org.threeten.bp.Instant

object HeartBeatReplySerializer : SignalProxyMessageSerializer<SignalProxyMessage.HeartBeatReply> {
  override fun serialize(data: SignalProxyMessage.HeartBeatReply) = listOf(
    QVariant.of(RequestType.HeartBeatReply.value, Type.Int),
    QVariant.of(data.timestamp, Type.QDateTime)
  )

  override fun deserialize(data: QVariantList) = SignalProxyMessage.HeartBeatReply(
    data[0].value(Instant.EPOCH)
  )
}
