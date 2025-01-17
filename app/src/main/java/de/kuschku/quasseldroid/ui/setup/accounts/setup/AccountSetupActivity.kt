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

package de.kuschku.quasseldroid.ui.setup.accounts.setup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.kuschku.quasseldroid.persistence.db.AccountDatabase
import de.kuschku.quasseldroid.persistence.models.Account
import de.kuschku.quasseldroid.ui.setup.SetupActivity
import de.kuschku.quasseldroid.util.AndroidHandlerThread
import org.threeten.bp.Instant
import javax.inject.Inject

class AccountSetupActivity : SetupActivity() {
  private val handler = AndroidHandlerThread("Setup")

  @Inject
  lateinit var database: AccountDatabase

  override fun onDone(data: Bundle) {
    val account = Account(
      id = 0,
      host = data.getString("host", ""),
      port = data.getInt("port"),
      requireSsl = data.getBoolean("require_ssl"),
      user = data.getString("user", ""),
      pass = data.getString("pass", ""),
      name = data.getString("name", ""),
      lastUsed = Instant.now().epochSecond,
      acceptedMissingFeatures = false,
      defaultFiltered = 0
    )
    handler.post {
      database.accounts().create(account)
      runOnUiThread {
        setResult(Activity.RESULT_OK)
        finish()
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    handler.onCreate()
    super.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    handler.onDestroy()
    super.onDestroy()
  }

  override val fragments = listOf(
    AccountSetupConnectionSlide(),
    AccountSetupUserSlide(),
    AccountSetupNameSlide()
  )

  companion object {
    fun launch(context: Context) = context.startActivity(intent(context))
    fun intent(context: Context) = Intent(context, AccountSetupActivity::class.java)
  }
}
