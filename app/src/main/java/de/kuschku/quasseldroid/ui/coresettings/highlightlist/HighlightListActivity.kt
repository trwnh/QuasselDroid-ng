package de.kuschku.quasseldroid.ui.coresettings.highlightlist

import android.content.Context
import android.content.Intent
import de.kuschku.quasseldroid.util.ui.SettingsActivity

class HighlightListActivity : SettingsActivity(HighlightListFragment()) {
  companion object {
    fun launch(context: Context) = context.startActivity(intent(context))
    fun intent(context: Context) = Intent(context, HighlightListActivity::class.java)
  }
}