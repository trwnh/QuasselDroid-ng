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

package de.kuschku.quasseldroid.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.kuschku.quasseldroid.service.QuasselService
import de.kuschku.quasseldroid.service.QuasselServiceModule
import de.kuschku.quasseldroid.ui.chat.ChatActivity
import de.kuschku.quasseldroid.ui.chat.ChatActivityModule
import de.kuschku.quasseldroid.ui.chat.ChatFragmentProvider
import de.kuschku.quasseldroid.ui.chat.add.create.ChannelCreateActivity
import de.kuschku.quasseldroid.ui.chat.add.create.ChannelCreateFragmentProvider
import de.kuschku.quasseldroid.ui.chat.add.join.ChannelJoinActivity
import de.kuschku.quasseldroid.ui.chat.add.join.ChannelJoinFragmentProvider
import de.kuschku.quasseldroid.ui.chat.add.query.QueryCreateActivity
import de.kuschku.quasseldroid.ui.chat.add.query.QueryCreateFragmentProvider
import de.kuschku.quasseldroid.ui.chat.archive.ArchiveActivity
import de.kuschku.quasseldroid.ui.chat.archive.ArchiveFragmentProvider
import de.kuschku.quasseldroid.ui.chat.topic.TopicActivity
import de.kuschku.quasseldroid.ui.chat.topic.TopicFragmentProvider
import de.kuschku.quasseldroid.ui.clientsettings.about.AboutActivity
import de.kuschku.quasseldroid.ui.clientsettings.about.AboutFragmentProvider
import de.kuschku.quasseldroid.ui.clientsettings.client.ClientSettingsActivity
import de.kuschku.quasseldroid.ui.clientsettings.client.ClientSettingsFragmentProvider
import de.kuschku.quasseldroid.ui.clientsettings.crash.CrashActivity
import de.kuschku.quasseldroid.ui.clientsettings.crash.CrashFragmentProvider
import de.kuschku.quasseldroid.ui.clientsettings.license.LicenseActivity
import de.kuschku.quasseldroid.ui.clientsettings.license.LicenseFragmentProvider
import de.kuschku.quasseldroid.ui.clientsettings.whitelist.WhitelistActivity
import de.kuschku.quasseldroid.ui.clientsettings.whitelist.WhitelistFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.CoreSettingsActivity
import de.kuschku.quasseldroid.ui.coresettings.CoreSettingsFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.aliasitem.AliasItemActivity
import de.kuschku.quasseldroid.ui.coresettings.aliasitem.AliasItemFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.aliaslist.AliasListActivity
import de.kuschku.quasseldroid.ui.coresettings.aliaslist.AliasListFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.chatlist.ChatlistCreateActivity
import de.kuschku.quasseldroid.ui.coresettings.chatlist.ChatlistCreateFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.chatlist.ChatlistEditActivity
import de.kuschku.quasseldroid.ui.coresettings.chatlist.ChatlistEditFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.highlightlist.HighlightListActivity
import de.kuschku.quasseldroid.ui.coresettings.highlightlist.HighlightListFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.highlightrule.HighlightRuleActivity
import de.kuschku.quasseldroid.ui.coresettings.highlightrule.HighlightRuleFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.identity.IdentityCreateActivity
import de.kuschku.quasseldroid.ui.coresettings.identity.IdentityCreateFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.identity.IdentityEditActivity
import de.kuschku.quasseldroid.ui.coresettings.identity.IdentityEditFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.ignoreitem.IgnoreItemActivity
import de.kuschku.quasseldroid.ui.coresettings.ignoreitem.IgnoreItemFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.ignorelist.IgnoreListActivity
import de.kuschku.quasseldroid.ui.coresettings.ignorelist.IgnoreListFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.network.NetworkCreateActivity
import de.kuschku.quasseldroid.ui.coresettings.network.NetworkCreateFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.network.NetworkEditActivity
import de.kuschku.quasseldroid.ui.coresettings.network.NetworkEditFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.networkconfig.NetworkConfigActivity
import de.kuschku.quasseldroid.ui.coresettings.networkconfig.NetworkConfigFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.networkserver.NetworkServerActivity
import de.kuschku.quasseldroid.ui.coresettings.networkserver.NetworkServerFragmentProvider
import de.kuschku.quasseldroid.ui.coresettings.passwordchange.PasswordChangeActivity
import de.kuschku.quasseldroid.ui.coresettings.passwordchange.PasswordChangeFragmentProvider
import de.kuschku.quasseldroid.ui.info.certificate.CertificateInfoActivity
import de.kuschku.quasseldroid.ui.info.certificate.CertificateInfoFragmentProvider
import de.kuschku.quasseldroid.ui.info.channel.ChannelInfoActivity
import de.kuschku.quasseldroid.ui.info.channel.ChannelInfoFragmentProvider
import de.kuschku.quasseldroid.ui.info.channellist.ChannelListActivity
import de.kuschku.quasseldroid.ui.info.channellist.ChannelListFragmentProvider
import de.kuschku.quasseldroid.ui.info.core.CoreInfoActivity
import de.kuschku.quasseldroid.ui.info.core.CoreInfoFragmentProvider
import de.kuschku.quasseldroid.ui.info.user.UserInfoActivity
import de.kuschku.quasseldroid.ui.info.user.UserInfoFragmentProvider
import de.kuschku.quasseldroid.ui.setup.accounts.edit.AccountEditActivity
import de.kuschku.quasseldroid.ui.setup.accounts.edit.AccountEditFragmentProvider
import de.kuschku.quasseldroid.ui.setup.accounts.selection.AccountSelectionActivity
import de.kuschku.quasseldroid.ui.setup.accounts.selection.AccountSelectionFragmentProvider
import de.kuschku.quasseldroid.ui.setup.accounts.setup.AccountSetupActivity
import de.kuschku.quasseldroid.ui.setup.accounts.setup.AccountSetupFragmentProvider
import de.kuschku.quasseldroid.ui.setup.core.CoreSetupActivity
import de.kuschku.quasseldroid.ui.setup.core.CoreSetupFragmentProvider
import de.kuschku.quasseldroid.ui.setup.network.NetworkSetupActivity
import de.kuschku.quasseldroid.ui.setup.network.NetworkSetupFragmentProvider
import de.kuschku.quasseldroid.ui.setup.user.UserSetupActivity
import de.kuschku.quasseldroid.ui.setup.user.UserSetupFragmentProvider

@Module
abstract class ActivityModule {
  @ActivityScope
  @ContributesAndroidInjector(modules = [ChatActivityModule::class, ChatFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChatActivity(): ChatActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ArchiveFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindArchiveActivity(): ArchiveActivity

  // Info

  @ActivityScope
  @ContributesAndroidInjector(modules = [UserInfoFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindUserInfoActivity(): UserInfoActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChannelInfoFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChannelInfoActivity(): ChannelInfoActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [CoreInfoFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindCoreInfoActivity(): CoreInfoActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [TopicFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindTopicActivity(): TopicActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChannelListFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChannelListActivity(): ChannelListActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [CertificateInfoFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindCertificateInfoActivity(): CertificateInfoActivity

  // Add

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChannelCreateFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChannelCreateActivity(): ChannelCreateActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChannelJoinFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChannelJoinActivity(): ChannelJoinActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [QueryCreateFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindQueryCreateActivity(): QueryCreateActivity

  // Client Settings

  @ActivityScope
  @ContributesAndroidInjector(modules = [ClientSettingsFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindClientSettingsActivity(): ClientSettingsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [WhitelistFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindWhitelistActivity(): WhitelistActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [CrashFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindCrashActivity(): CrashActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AboutFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAboutActivity(): AboutActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [LicenseFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindLicenseActivity(): LicenseActivity

  // Core Settings

  @ActivityScope
  @ContributesAndroidInjector(modules = [CoreSettingsFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindCoreSettingsActivity(): CoreSettingsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [NetworkCreateFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindNetworkCreateActivity(): NetworkCreateActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [NetworkEditFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindNetworkEditActivity(): NetworkEditActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [NetworkServerFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindNetworkServerActivity(): NetworkServerActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [IdentityCreateFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindIdentityCreateActivity(): IdentityCreateActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [IdentityEditFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindIdentityEditActivity(): IdentityEditActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChatlistCreateFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChatListCreateActivity(): ChatlistCreateActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ChatlistEditFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindChatListEditActivity(): ChatlistEditActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [IgnoreListFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindIgnoreListActivity(): IgnoreListActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [IgnoreItemFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindIgnoreItemActivity(): IgnoreItemActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [HighlightListFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindHighlightListActivity(): HighlightListActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [HighlightRuleFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindHighlightRuleActivity(): HighlightRuleActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AliasListFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAliasListActivity(): AliasListActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AliasItemFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAliasItemActivity(): AliasItemActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [NetworkConfigFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindNetworkConfigActivity(): NetworkConfigActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [PasswordChangeFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindPasswordChangeActivity(): PasswordChangeActivity

  // Setup

  @ActivityScope
  @ContributesAndroidInjector(modules = [AccountSetupFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAccountSetupActivity(): AccountSetupActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AccountSelectionFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAccountSelectionActivity(): AccountSelectionActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AccountEditFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindAccountEditActivity(): AccountEditActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [UserSetupFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindUserSetupActivity(): UserSetupActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [NetworkSetupFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindNetworkSetupActivity(): NetworkSetupActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [CoreSetupFragmentProvider::class, SettingsModule::class, DatabaseModule::class, ActivityBaseModule::class])
  abstract fun bindCoreSetupActivity(): CoreSetupActivity

  // Service

  @ActivityScope
  @ContributesAndroidInjector(modules = [QuasselServiceModule::class, SettingsModule::class, DatabaseModule::class])
  abstract fun bindQuasselService(): QuasselService
}
