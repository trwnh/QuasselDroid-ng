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

package de.kuschku.quasseldroid.ssl

import de.kuschku.libquassel.connection.HostnameVerifier
import de.kuschku.libquassel.connection.QuasselSecurityException
import de.kuschku.libquassel.connection.SocketAddress
import de.kuschku.libquassel.ssl.BrowserCompatibleHostnameVerifier
import de.kuschku.quasseldroid.ssl.custom.QuasselHostnameManager
import java.security.cert.X509Certificate
import javax.net.ssl.SSLException

class QuasselHostnameVerifier(
  private val hostnameManager: QuasselHostnameManager,
  private val hostnameVerifier: HostnameVerifier = BrowserCompatibleHostnameVerifier()
) : HostnameVerifier {
  override fun checkValid(address: SocketAddress, chain: Array<out X509Certificate>) {
    try {
      if (!hostnameManager.isValid(address, chain)) {
        hostnameVerifier.checkValid(address, chain)
      }
    } catch (e: SSLException) {
      throw QuasselSecurityException.Hostname(chain, address, e)
    }
  }
}
