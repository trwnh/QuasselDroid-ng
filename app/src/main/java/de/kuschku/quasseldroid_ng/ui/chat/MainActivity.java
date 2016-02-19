/*
 * QuasselDroid - Quassel client for Android
 * Copyright (C) 2016 Janne Koschinski
 * Copyright (C) 2016 Ken Børge Viktil
 * Copyright (C) 2016 Magnus Fjell
 * Copyright (C) 2016 Martin Sandsmark <martin.sandsmark@kde.org>
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kuschku.quasseldroid_ng.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.kuschku.libquassel.client.Client;
import de.kuschku.libquassel.events.BufferChangeEvent;
import de.kuschku.libquassel.events.ConnectionChangeEvent;
import de.kuschku.libquassel.events.GeneralErrorEvent;
import de.kuschku.libquassel.events.LoginRequireEvent;
import de.kuschku.libquassel.events.UnknownCertificateEvent;
import de.kuschku.libquassel.localtypes.BacklogFilter;
import de.kuschku.libquassel.localtypes.buffers.Buffer;
import de.kuschku.libquassel.localtypes.buffers.ChannelBuffer;
import de.kuschku.libquassel.localtypes.buffers.QueryBuffer;
import de.kuschku.libquassel.message.Message;
import de.kuschku.libquassel.syncables.types.interfaces.QBacklogManager;
import de.kuschku.libquassel.syncables.types.interfaces.QBufferViewConfig;
import de.kuschku.libquassel.syncables.types.interfaces.QBufferViewManager;
import de.kuschku.libquassel.syncables.types.interfaces.QIrcChannel;
import de.kuschku.libquassel.syncables.types.interfaces.QIrcUser;
import de.kuschku.quasseldroid_ng.R;
import de.kuschku.quasseldroid_ng.service.ClientBackgroundThread;
import de.kuschku.quasseldroid_ng.ui.chat.drawer.BufferItem;
import de.kuschku.quasseldroid_ng.ui.chat.drawer.BufferViewConfigItem;
import de.kuschku.quasseldroid_ng.ui.chat.drawer.NetworkItem;
import de.kuschku.quasseldroid_ng.ui.chat.fragment.ChatFragment;
import de.kuschku.quasseldroid_ng.ui.chat.fragment.LoadingFragment;
import de.kuschku.quasseldroid_ng.ui.chat.util.ActivityImplFactory;
import de.kuschku.quasseldroid_ng.ui.chat.util.ILayoutHelper;
import de.kuschku.quasseldroid_ng.ui.chat.util.Status;
import de.kuschku.util.accounts.Account;
import de.kuschku.util.accounts.AccountManager;
import de.kuschku.util.certificates.CertificateUtils;
import de.kuschku.util.certificates.SQLiteCertificateManager;
import de.kuschku.util.servicebound.BoundActivity;

import static de.kuschku.util.AndroidAssert.assertNotNull;

public class MainActivity extends BoundActivity {

    /**
     * A helper to handle the different layout implementations
     */
    ILayoutHelper layoutHelper;

    /**
     * Host layout for content fragment, for example showing a loader or the chat
     */
    @Bind(R.id.content_host)
    FrameLayout contentHost;

    /**
     * Main ActionBar
     */
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /**
     * The left material drawer of this activity, depending on layout either in the layout hierarchy
     * or at the left as pull-out menu
     */
    Drawer drawerLeft;

    /**
     * AccountHeader field for the bufferviewconfig header
     */
    AccountHeader accountHeader;

    /**
     * This object encapsulates the current status of the activity – opened bufferview, for example
     */
    private Status status = new Status();

    private BufferViewConfigItem currentConfig;

    private AccountManager manager;

    private ToolbarWrapper toolbarWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarWrapper = new ToolbarWrapper(toolbar);
        toolbarWrapper.setOnClickListener(v -> {
            if (context.client() != null) {
                Intent intent = new Intent(this, ChannelDetailActivity.class);
                intent.putExtra("buffer", context.client().backlogManager().open());
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        layoutHelper = ActivityImplFactory.of(getResources().getBoolean(R.bool.isTablet), this);
        accountHeader = buildAccountHeader();
        drawerLeft = layoutHelper.buildDrawer(savedInstanceState, accountHeader, toolbar);
        drawerLeft.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            if (drawerItem instanceof NetworkItem) {
                drawerLeft.getAdapter().toggleExpandable(position);
                return true;
            } else if (drawerItem instanceof BufferItem) {
                int id = ((BufferItem) drawerItem).getBuffer().getInfo().id();
                context.client().backlogManager().open(id);
                return false;
            }
            return true;
        });

        replaceFragment(new LoadingFragment());

        if (savedInstanceState != null)
            status.onRestoreInstanceState(savedInstanceState);

        manager = new AccountManager(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (context.client() != null)
            context.client().backlogManager().setOpen(-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (context.client() != null)
            context.client().backlogManager().open(status.bufferId);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_host, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        assertNotNull(outState);

        super.onSaveInstanceState(outState);
        status.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        assertNotNull(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
        status.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide_events:
                displayFilterDialog();
                return true;
            case R.id.action_reauth:
                reauth();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reauth() {
        context.settings().lastAccount.set("");
        stopConnection();
        finish();
    }

    private AccountHeader buildAccountHeader() {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.bg1)
                .withProfileImagesVisible(false)
                .withOnAccountHeaderListener((view, profile, current) -> {
                    selectBufferViewConfig((int) profile.getIdentifier());
                    return true;
                })
                .build();
    }

    public void onEventMainThread(ConnectionChangeEvent event) {
        onConnectionChange(event.status);
    }

    public void onConnectionChange(ConnectionChangeEvent.Status status) {
        if (status == ConnectionChangeEvent.Status.CONNECTED) {
            updateBufferViewConfigs();
            context.client().backlogManager().open(this.status.bufferId);
            accountHeader.setActiveProfile(this.status.bufferViewConfigId, true);
            replaceFragment(new ChatFragment());
        } else if (status == ConnectionChangeEvent.Status.DISCONNECTED) {
            Toast.makeText(getApplication(), context.themeUtil().translations.statusDisconnected, Toast.LENGTH_LONG).show();
        }
    }

    public void onEventMainThread(GeneralErrorEvent event) {
        Toast.makeText(getApplication(), event.exception.getClass().getSimpleName() + ": " + event.debugInfo, Toast.LENGTH_LONG).show();
    }

    public void onEventMainThread(BufferChangeEvent event) {
        Client client = context.client();
        if (client != null) {
            QBacklogManager<? extends QBacklogManager> backlogManager = client.backlogManager();
            int id = backlogManager.open();
            status.bufferId = id;
            updateBuffer(id);
        }
    }

    private void updateBuffer(int id) {
        Client client = context.client();
        if (client != null) {
            Buffer buffer = client.bufferManager().buffer(id);
            if (buffer != null) {
                toolbarWrapper.setTitle(buffer.getName());
                if (buffer instanceof QueryBuffer) {
                    QIrcUser user = ((QueryBuffer) buffer).getUser();
                    if (user == null) {
                        toolbarWrapper.setSubtitle(null);
                    } else {
                        toolbarWrapper.setSubtitle(user.hostmask() + " | " + user.realName());
                    }
                } else if (buffer instanceof ChannelBuffer) {
                    QIrcChannel channel = ((ChannelBuffer) buffer).getChannel();
                    if (channel == null) {
                        toolbarWrapper.setSubtitle(null);
                    } else {
                        toolbarWrapper.setSubtitle(context.deserializer().formatString(channel.topic()));
                    }
                } else {
                    toolbarWrapper.setSubtitle(null);
                }
            }
        }
        drawerLeft.setSelection(id, false);
    }

    private void selectBufferViewConfig(@IntRange(from = -1) int bufferViewConfigId) {
        assertNotNull(drawerLeft);
        assertNotNull(accountHeader);
        Client client = context.client();
        assertNotNull(client);

        drawerLeft.removeAllItems();

        status.bufferViewConfigId = bufferViewConfigId;
        accountHeader.setActiveProfile(bufferViewConfigId, false);

        if (currentConfig != null)
            currentConfig.remove();
        currentConfig = null;

        QBufferViewManager bufferViewManager = client.bufferViewManager();
        if (bufferViewConfigId != -1 && bufferViewManager != null) {
            QBufferViewConfig viewConfig = bufferViewManager.bufferViewConfig(bufferViewConfigId);
            if (viewConfig != null) {
                currentConfig = new BufferViewConfigItem(drawerLeft, viewConfig, context);
            }
        }
    }

    private void updateBufferViewConfigs() {
        assertNotNull(context.client().bufferViewManager());
        List<QBufferViewConfig> bufferViews = context.client().bufferViewManager().bufferViewConfigs();
        accountHeader.clear();
        for (QBufferViewConfig viewConfig : bufferViews) {
            if (viewConfig != null) {
                if (status.bufferViewConfigId == -1) {
                    status.bufferViewConfigId = viewConfig.bufferViewId();
                }
                accountHeader.addProfiles(
                        new ProfileDrawerItem()
                                .withName(viewConfig.bufferViewName())
                                .withIdentifier(viewConfig.bufferViewId())
                );
            }
        }
        accountHeader.setActiveProfile(status.bufferViewConfigId, true);
    }

    @Override
    protected void onConnectToThread(@Nullable ClientBackgroundThread thread) {
        super.onConnectToThread(thread);
        if (thread == null)
            connectToServer(manager.account(context.settings().lastAccount.get()));
        else {
            if (context.client() != null) {
                context.client().backlogManager().init("", context.provider(), context.client());
                context.client().backlogManager().open(status.bufferId);
                updateBuffer(context.client().backlogManager().open());
                accountHeader.setActiveProfile(status.bufferViewConfigId, true);
            }
        }
    }

    // FIXME: Fix this ugly hack
    public void displayFilterDialog() {
        if (context.client() != null) {
            List<Integer> filterSettings = Arrays.asList(
                    Message.Type.Join.value,
                    Message.Type.Part.value,
                    Message.Type.Quit.value,
                    Message.Type.Nick.value,
                    Message.Type.Mode.value,
                    Message.Type.Topic.value
            );
            int[] filterSettingsInts = new int[filterSettings.size()];
            for (int i = 0; i < filterSettingsInts.length; i++) {
                filterSettingsInts[i] = filterSettings.get(i);
            }

            BacklogFilter backlogFilter = context.client().backlogManager().filter(context.client().backlogManager().open());
            int oldFilters = backlogFilter.getFilters();
            List<Integer> oldFiltersList = new ArrayList<>();
            for (int type : filterSettings) {
                if ((type & oldFilters) != 0)
                    oldFiltersList.add(filterSettings.indexOf(type));
            }
            Integer[] selectedIndices = oldFiltersList.toArray(new Integer[oldFiltersList.size()]);
            new MaterialDialog.Builder(this)
                    .items(
                            "Joins",
                            "Parts",
                            "Quits",
                            "Nick Changes",
                            "Mode Changes",
                            "Topic Changes"
                    )
                    .itemsIds(filterSettingsInts)
                    .itemsCallbackMultiChoice(
                            selectedIndices,
                            (dialog, which, text) -> false
                    )
                    .positiveText("Select")
                    .negativeText("Cancel")
                    .onPositive((dialog, which) -> {
                        int filters = 0x00000000;
                        if (dialog.getSelectedIndices() != null)
                            for (int i : dialog.getSelectedIndices()) {
                                int settingsid = filterSettings.get(i);
                                filters |= settingsid;
                                if (settingsid == Message.Type.Quit.value)
                                    filters |= Message.Type.NetsplitQuit.value;
                                else if (settingsid == Message.Type.Join.value)
                                    filters |= Message.Type.NetsplitJoin.value;
                            }
                        backlogFilter.setFilters(filters);
                    })
                    .negativeColor(context.themeUtil().res.colorForeground)
                    .backgroundColor(context.themeUtil().res.colorBackgroundCard)
                    .contentColor(context.themeUtil().res.colorForeground)
                    .build()
                    .show();
        }
    }

    public void onEventMainThread(@NonNull UnknownCertificateEvent event) {
        new MaterialDialog.Builder(this)
                .content(context.themeUtil().translations.warningCertificate + "\n" + CertificateUtils.certificateToFingerprint(event.certificate, ""))
                .title("Unknown Certificate")
                .onPositive((dialog, which) -> new SQLiteCertificateManager(this).addCertificate(event.certificate, event.address))
                .negativeColor(context.themeUtil().res.colorForeground)
                .positiveText("Yes")
                .negativeText("No")
                .backgroundColor(context.themeUtil().res.colorBackgroundCard)
                .contentColor(context.themeUtil().res.colorForeground)
                .build()
                .show();
    }

    public void onEventMainThread(LoginRequireEvent event) {
        if (event.failedLast) {
            new MaterialDialog.Builder(this)
                    .title(R.string.labelLogin)
                    .customView(R.layout.dialog_login, false)
                    .onPositive((dialog1, which) -> {
                        View parent = dialog1.getCustomView();
                        assertNotNull(parent);
                        AppCompatEditText usernameField = (AppCompatEditText) parent.findViewById(R.id.username);
                        AppCompatEditText passwordField = (AppCompatEditText) parent.findViewById(R.id.password);
                        String username = usernameField.getText().toString();
                        String password = passwordField.getText().toString();

                        Account account = manager.account(context.settings().lastAccount.get());
                        manager.update(account.withLoginData(username, password));
                    })
                    .cancelListener(dialog1 -> finish())
                    .negativeColor(context.themeUtil().res.colorForeground)
                    .positiveText(R.string.labelLogin)
                    .negativeText(R.string.labelCancel)
                    .backgroundColor(context.themeUtil().res.colorBackgroundCard)
                    .contentColor(context.themeUtil().res.colorForeground)
                    .build().show();
        }
    }
}