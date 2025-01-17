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

@Syncable(name = "HighlightRuleManager")
interface IHighlightRuleManager : ISyncableObject {
  enum class HighlightNickType(val value: Int) {
    NoNick(0x00),
    CurrentNick(0x01),
    AllNicks(0x02);

    companion object {
      private val map = values().associateBy(HighlightNickType::value)
      fun of(value: Int) = map[value]
    }
  }

  fun initHighlightRuleList(): QVariantMap
  fun initSetHighlightRuleList(highlightRuleList: QVariantMap)

  /**
   * Request removal of an ignore rule based on the rule itself.
   * Use this method if you want to remove a single ignore rule
   * and get that synced with the core immediately.
   * @param highlightRule A valid ignore rule
   */
  @Slot
  fun requestRemoveHighlightRule(highlightRule: Int) {
    REQUEST("requestRemoveHighlightRule", ARG(highlightRule, Type.Int))
  }

  @Slot
  fun removeHighlightRule(highlightRule: Int)

  /**
   * Request toggling of "isEnabled" flag of a given ignore rule.
   * Use this method if you want to toggle the "isEnabled" flag of a single ignore rule
   * and get that synced with the core immediately.
   * @param highlightRule A valid ignore rule
   */
  @Slot
  fun requestToggleHighlightRule(highlightRule: Int) {
    REQUEST("requestToggleHighlightRule", ARG(highlightRule, Type.Int))
  }

  @Slot
  fun toggleHighlightRule(highlightRule: Int)

  /**
   * Request an HighlightRule to be added to the ignore list
   * Items added to the list with this method, get immediately synced with the core
   * @param name The rule
   * @param isRegEx If the rule should be interpreted as a nickname, or a regex
   * @param isCaseSensitive If the rule should be interpreted as case-sensitive
   * @param isEnabled If the rule is active
   * @param chanName The channel in which the rule should apply
   */
  @Slot
  fun requestAddHighlightRule(id: Int, name: String?, isRegEx: Boolean, isCaseSensitive: Boolean,
                              isEnabled: Boolean, isInverse: Boolean, sender: String?,
                              chanName: String?) {
    REQUEST("requestAddHighlightRule", ARG(id, Type.Int), ARG(name, Type.QString),
            ARG(isRegEx, Type.Bool), ARG(isCaseSensitive, Type.Bool), ARG(isEnabled, Type.Bool),
            ARG(isInverse, Type.Bool), ARG(sender, Type.QString), ARG(chanName, Type.QString))
  }

  @Slot
  fun addHighlightRule(id: Int, name: String?, isRegEx: Boolean, isCaseSensitive: Boolean,
                       isEnabled: Boolean, isInverse: Boolean, sender: String?, chanName: String?)

  @Slot
  fun requestSetHighlightNick(highlightNick: Int) {
    REQUEST("requestSetHighlightNick", ARG(highlightNick, Type.Int))
  }

  @Slot
  fun setHighlightNick(highlightNick: Int)

  @Slot
  fun requestSetNicksCaseSensitive(nicksCaseSensitive: Boolean) {
    REQUEST("requestSetNicksCaseSensitive", ARG(nicksCaseSensitive, Type.Bool))
  }

  @Slot
  fun setNicksCaseSensitive(nicksCaseSensitive: Boolean)

  @Slot
  override fun update(properties: QVariantMap) {
    super.update(properties)
  }
}
