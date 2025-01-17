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

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textfield.TextInputLayout
import de.kuschku.quasseldroid.R
import de.kuschku.quasseldroid.ui.setup.SlideFragment
import de.kuschku.quasseldroid.util.TextValidator

class AccountSetupUserSlide : SlideFragment() {
  @BindView(R.id.userWrapper)
  lateinit var userWrapper: TextInputLayout

  @BindView(R.id.user)
  lateinit var userField: EditText

  @BindView(R.id.passWrapper)
  lateinit var passWrapper: TextInputLayout

  @BindView(R.id.pass)
  lateinit var passField: EditText

  override fun isValid(): Boolean {
    return true
  }

  override val title = R.string.slide_account_user_title
  override val description = R.string.slide_account_user_description

  override fun setData(data: Bundle) {
    if (data.containsKey("user"))
      userField.setText(data.getString("user"))
    if (data.containsKey("pass"))
      passField.setText(data.getString("pass"))
  }

  override fun getData(data: Bundle) {
    data.putString("user", userField.text.toString())
    data.putString("pass", passField.text.toString())
  }

  override fun onCreateContent(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {
    val view = inflater.inflate(R.layout.setup_account_user, container, false)
    ButterKnife.bind(this, view)
    userValidator = object : TextValidator(
      requireActivity(), userWrapper::setError, resources.getString(R.string.hint_invalid_user)
    ) {
      override fun validate(text: Editable) = text.isNotBlank()

      override fun onChanged() = updateValidity()
    }
    userField.addTextChangedListener(userValidator)
    userValidator.afterTextChanged(userField.text)
    return view
  }

  private lateinit var userValidator: TextValidator
}
