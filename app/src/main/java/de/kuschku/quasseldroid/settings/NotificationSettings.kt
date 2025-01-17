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

package de.kuschku.quasseldroid.settings

data class NotificationSettings(
  val query: Level = Level.ALL,
  val channel: Level = Level.HIGHLIGHT,
  val other: Level = Level.NONE,
  val sound: String = "content://settings/system/notification_sound",
  val vibrate: Boolean = true,
  val light: Boolean = true,
  val markReadOnSwipe: Boolean = false,
  val networkNameInNotificationTitle: Boolean = false,
  val showAllActivitiesInToolbar: Boolean = false
) {
  enum class Level {
    ALL,
    HIGHLIGHT,
    NONE;

    companion object {
      private val map = values().associateBy { it.name }
      fun of(name: String) = map[name]
    }
  }

  companion object {
    val DEFAULT = NotificationSettings()
  }
}
