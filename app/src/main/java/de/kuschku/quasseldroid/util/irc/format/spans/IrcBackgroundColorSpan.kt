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

package de.kuschku.quasseldroid.util.irc.format.spans

import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt

sealed class IrcBackgroundColorSpan<T : IrcBackgroundColorSpan<T>>(@ColorInt color: Int) :
  BackgroundColorSpan(color), Copyable<T> {
  class MIRC(private val mircColor: Int, @ColorInt color: Int) :
    IrcBackgroundColorSpan<MIRC>(color), Copyable<MIRC> {
    override fun copy() = MIRC(mircColor, backgroundColor)
    override fun toString(): String {
      return "IrcBackgroundColorSpan.MIRC(mircColor=$mircColor, color=${backgroundColor.toString(16)})"
    }

    override fun equals(other: Any?) = when (other) {
      is IrcBackgroundColorSpan.MIRC -> other.mircColor == mircColor
      else                           -> false
    }
  }

  class HEX(@ColorInt color: Int) :
    IrcBackgroundColorSpan<HEX>(color), Copyable<HEX> {
    override fun copy() = HEX(backgroundColor)
    override fun toString(): String {
      return "IrcBackgroundColorSpan.HEX(color=${backgroundColor.toString(16)})"
    }

    override fun equals(other: Any?) = when (other) {
      is IrcBackgroundColorSpan.HEX -> other.backgroundColor == backgroundColor
      else                          -> false
    }
  }
}
