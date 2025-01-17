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

package de.kuschku.quasseldroid.ui.coresettings.networkserver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import butterknife.BindView
import butterknife.ButterKnife
import de.kuschku.libquassel.quassel.syncables.interfaces.INetwork
import de.kuschku.libquassel.quassel.syncables.interfaces.INetwork.PortDefaults.PORT_PLAINTEXT
import de.kuschku.libquassel.quassel.syncables.interfaces.INetwork.PortDefaults.PORT_SSL
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.util.helper.setDependent
import de.kuschku.quasseldroid.util.ui.settings.fragment.Changeable
import de.kuschku.quasseldroid.util.ui.settings.fragment.Savable
import de.kuschku.quasseldroid.util.ui.settings.fragment.ServiceBoundSettingsFragment

class NetworkServerFragment : ServiceBoundSettingsFragment(), Savable,
                              Changeable {
  @BindView(R.id.host)
  lateinit var host: EditText

  @BindView(R.id.port)
  lateinit var port: EditText

  @BindView(R.id.ssl_enabled)
  lateinit var sslEnabled: SwitchCompat

  @BindView(R.id.ssl_verify)
  lateinit var sslVerify: SwitchCompat

  @BindView(R.id.password)
  lateinit var password: EditText

  @BindView(R.id.proxy_enabled)
  lateinit var proxyEnabled: SwitchCompat

  @BindView(R.id.proxy_group)
  lateinit var proxyGroup: ViewGroup

  @BindView(R.id.proxy_type)
  lateinit var proxyType: Spinner

  @BindView(R.id.proxy_host)
  lateinit var proxyHost: EditText

  @BindView(R.id.proxy_port)
  lateinit var proxyPort: EditText

  @BindView(R.id.proxy_user)
  lateinit var proxyUser: EditText

  @BindView(R.id.proxy_pass)
  lateinit var proxyPass: EditText

  private var item: INetwork.Server? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.settings_networkserver, container, false)
    ButterKnife.bind(this, view)

    (arguments?.getSerializable("server") as? INetwork.Server)?.let {
      item = it
    }

    val typeAdapter = ProxyTypeAdapter(listOf(
      ProxyTypeItem(
        value = INetwork.ProxyType.Socks5Proxy,
        name = R.string.settings_networkserver_proxy_type_socks5
      ),
      ProxyTypeItem(
        value = INetwork.ProxyType.HttpProxy,
        name = R.string.settings_networkserver_proxy_type_http
      )
    ))
    proxyType.adapter = typeAdapter

    (item ?: INetwork.Server()).let { data ->
      host.setText(data.host)
      port.setText(data.port.toString())
      sslEnabled.isChecked = data.useSsl
      sslVerify.isChecked = data.sslVerify
      password.setText(data.password)
      proxyEnabled.isChecked = data.useProxy
      proxyType.setSelection(typeAdapter.indexOf(data.proxyType) ?: 0)
      proxyHost.setText(data.proxyHost)
      proxyPort.setText(data.proxyPort.toString())
      proxyUser.setText(data.proxyUser)
      proxyPass.setText(data.proxyPass)
    }

    proxyEnabled.setDependent(proxyGroup)

    sslEnabled.setOnCheckedChangeListener { _, isChecked ->
      sslVerify.isEnabled = isChecked
      val portValue = port.text.trim().toString()
      if (isChecked && portValue == PORT_PLAINTEXT.port.toString()) {
        port.setText(PORT_SSL.port.toString())
      } else if (!isChecked && portValue == PORT_SSL.port.toString()) {
        port.setText(PORT_PLAINTEXT.port.toString())
      }
    }
    sslVerify.isEnabled = sslEnabled.isChecked

    return view
  }

  private fun applyChanges(data: INetwork.Server?): INetwork.Server {
    return INetwork.Server(
      host = host.text.toString(),
      port = port.text.toString().toUIntOrNull()
             ?: data?.port
             ?: PORT_PLAINTEXT.port,
      useSsl = sslEnabled.isChecked,
      sslVerify = sslVerify.isChecked,
      password = password.text.toString(),
      useProxy = proxyEnabled.isChecked,
      proxyType = proxyType.selectedItemId.toInt(),
      proxyHost = proxyHost.text.toString(),
      proxyPort = proxyPort.text.toString().toUIntOrNull()
                  ?: data?.proxyPort ?: 0u,
      proxyUser = proxyUser.text.toString(),
      proxyPass = proxyPass.text.toString()
    )
  }

  override fun onSave() = item.let { data ->
    val intent = Intent()
    intent.putExtra("old", data)
    val new = applyChanges(data)
    intent.putExtra("new", new)
    requireActivity().setResult(Activity.RESULT_OK, intent)
    true
  }

  override fun hasChanged() = item != applyChanges(item)
}
