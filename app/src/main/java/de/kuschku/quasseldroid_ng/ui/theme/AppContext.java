package de.kuschku.quasseldroid_ng.ui.theme;

import android.support.annotation.NonNull;

import de.kuschku.libquassel.BusProvider;
import de.kuschku.libquassel.Client;
import de.kuschku.quasseldroid_ng.ui.chat.WrappedSettings;

public class AppContext {
    private ThemeUtil themeUtil;
    private WrappedSettings settings;
    private Client client;
    private BusProvider provider;

    public ThemeUtil getThemeUtil() {
        return themeUtil;
    }

    public void setThemeUtil(ThemeUtil themeUtil) {
        this.themeUtil = themeUtil;
    }

    @NonNull
    public AppContext withThemeUtil(ThemeUtil themeUtil) {
        setThemeUtil(themeUtil);
        return this;
    }

    public WrappedSettings getSettings() {
        return settings;
    }

    public void setSettings(WrappedSettings settings) {
        this.settings = settings;
    }

    @NonNull
    public AppContext withSettings(WrappedSettings settings) {
        setSettings(settings);
        return this;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @NonNull
    public AppContext withClient(Client client) {
        setClient(client);
        return this;
    }

    public BusProvider getProvider() {
        return provider;
    }

    public void setProvider(BusProvider provider) {
        this.provider = provider;
    }

    @NonNull
    public AppContext withProvider(BusProvider provider) {
        setProvider(provider);
        return this;
    }
}
