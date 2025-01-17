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

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES
import de.kuschku.quasseldroid.R.xml.preferences

class SettingsMigrationManager(
  migrations: List<SettingsMigration>
) {
  private val migrationMap = migrations.associateBy(SettingsMigration::from)
  private val currentVersion = migrations.map(SettingsMigration::to).max()

  // This runs during initial start and has to run synchronously
  @SuppressLint("ApplySharedPref")
  fun migrate(context: Context) {
    val defaultValueSp = context.getSharedPreferences(KEY_HAS_SET_DEFAULT_VALUES,
                                                      Context.MODE_PRIVATE)

    if (defaultValueSp.getBoolean(KEY_HAS_SET_DEFAULT_VALUES, false)) {
      val preferences = PreferenceManager.getDefaultSharedPreferences(context)
      var version = preferences.getInt(SETTINGS_VERSION, 0)
      while (version != currentVersion) {
        val migration = migrationMap[version]
                        ?: throw IllegalArgumentException("Migration not available")
        val editor = preferences.edit()
        migration.migrate(preferences, editor)
        version = migration.to
        editor.putInt(SETTINGS_VERSION, version)
        editor.commit()
      }
    }
    PreferenceManager.setDefaultValues(context, preferences, false)
  }

  companion object {
    private const val SETTINGS_VERSION = "settings_version"
  }
}
