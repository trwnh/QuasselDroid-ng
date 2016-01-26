package de.kuschku.libquassel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kuschku.libquassel.backlogmanagers.BacklogManager;
import de.kuschku.libquassel.backlogmanagers.SimpleBacklogManager;
import de.kuschku.libquassel.events.ConnectionChangeEvent;
import de.kuschku.libquassel.events.StatusMessageEvent;
import de.kuschku.libquassel.functions.types.InitRequestFunction;
import de.kuschku.libquassel.functions.types.RpcCallFunction;
import de.kuschku.libquassel.localtypes.Buffer;
import de.kuschku.libquassel.localtypes.Buffers;
import de.kuschku.libquassel.message.Message;
import de.kuschku.libquassel.objects.types.ClientInitAck;
import de.kuschku.libquassel.objects.types.SessionState;
import de.kuschku.libquassel.primitives.types.BufferInfo;
import de.kuschku.libquassel.primitives.types.QVariant;
import de.kuschku.libquassel.syncables.types.BufferSyncer;
import de.kuschku.libquassel.syncables.types.BufferViewManager;
import de.kuschku.libquassel.syncables.types.Network;
import de.kuschku.libquassel.syncables.types.SyncableObject;
import de.kuschku.quasseldroid_ng.ui.chat.drawer.NetworkWrapper;
import de.kuschku.util.backports.Stream;
import de.kuschku.util.observables.callbacks.UICallback;
import de.kuschku.util.observables.lists.IObservableList;
import de.kuschku.util.observables.lists.ObservableComparableSortedList;

import static de.kuschku.util.AndroidAssert.assertNotNull;


public class Client {
    @NonNull
    private final Map<Integer, Network> networks = new HashMap<>();
    @NonNull
    private final IObservableList<UICallback, NetworkWrapper> networkList = new ObservableComparableSortedList<>(NetworkWrapper.class);
    @NonNull
    private final Map<Integer, Buffer> buffers = new HashMap<>();
    @NonNull
    private final List<String> initDataQueue = new ArrayList<>();
    @NonNull
    private final BacklogManager backlogManager;
    @NonNull
    private final BusProvider busProvider;
    public int lag;
    private ConnectionChangeEvent.Status connectionStatus;
    private ClientInitAck core;
    @Nullable
    private SessionState state;
    private BufferViewManager bufferViewManager;
    private BufferSyncer bufferSyncer;
    private ClientData clientData;

    public Client(@NonNull final BusProvider busProvider) {
        this(new SimpleBacklogManager(busProvider), busProvider);
    }

    public Client(@NonNull final BacklogManager backlogManager, @NonNull final BusProvider busProvider) {
        this.backlogManager = backlogManager;
        this.busProvider = busProvider;
    }

    public void sendInput(@NonNull final BufferInfo info, @NonNull final String input) {
        busProvider.dispatch(new RpcCallFunction(
                "2sendInput(BufferInfo,QString)",
                new QVariant<>(info),
                new QVariant<>(input)
        ));
    }

    public void displayMsg(@NonNull final Message message) {
        backlogManager.displayMessage(message.bufferInfo.id, message);
    }

    public void displayStatusMsg(@NonNull String scope, @NonNull String message) {
        busProvider.sendEvent(new StatusMessageEvent(scope, message));
    }

    public void putNetwork(@NonNull final Network network) {
        assertNotNull(state);

        networks.put(network.getNetworkId(), network);

        for (BufferInfo info : state.BufferInfos) {
            if (info.networkId == network.getNetworkId()) {
                Buffer buffer = Buffers.fromType(info, network);
                assertNotNull(buffer);

                putBuffer(buffer);
            }
        }
    }

    @Nullable
    public Network getNetwork(final int networkId) {
        return this.networks.get(networkId);
    }

    public void putBuffer(@NonNull final Buffer buffer) {
        this.buffers.put(buffer.getInfo().id, buffer);
    }

    @Nullable
    public Buffer getBuffer(final int bufferId) {
        return this.buffers.get(bufferId);
    }

    void sendInitRequest(@NonNull final String className, @Nullable final String objectName) {
        sendInitRequest(className, objectName, false);
    }

    void sendInitRequest(@NonNull final String className, @Nullable final String objectName, boolean addToList) {
        busProvider.dispatch(new InitRequestFunction(className, objectName));

        if (addToList)
            getInitDataQueue().add(className + ":" + objectName);
    }

    public void __objectRenamed__(@NonNull String className, @NonNull String newName, @NonNull String oldName) {
        safeGetObjectByIdentifier(className, oldName).renameObject(newName);
    }

    @NonNull
    private SyncableObject safeGetObjectByIdentifier(@NonNull String className, @NonNull String oldName) {
        SyncableObject val = getObjectByIdentifier(className, oldName);
        if (val == null)
            throw new IllegalArgumentException(String.format("Object %s::%s does not exist", className, oldName));
        else return val;
    }

    @Nullable
    public SyncableObject getObjectByIdentifier(@NonNull final String className, @NonNull final String objectName) {
        switch (className) {
            case "BacklogManager":
                return getBacklogManager();
            case "IrcChannel": {
                final int networkId = Integer.parseInt(objectName.split("/")[0]);
                final String channelname = objectName.split("/")[1];

                // Assert that networkId is valid
                Network network = getNetwork(networkId);
                assertNotNull(network);
                return network.getChannels().get(channelname);
            }
            case "BufferSyncer":
                return bufferSyncer;
            case "BufferViewConfig":
                assertNotNull(getBufferViewManager());

                return getBufferViewManager().BufferViews.get(Integer.valueOf(objectName));
            case "IrcUser": {
                final int networkId = Integer.parseInt(objectName.split("/")[0]);
                final String username = objectName.split("/")[1];
                Network network = getNetwork(networkId);
                assertNotNull(network);
                return network.getUser(username);
            }
            case "Network": {
                return getNetwork(Integer.parseInt(objectName));
            }
            default:
                throw new IllegalArgumentException(String.format("No object of type %s known: %s", className, objectName));
        }
    }

    @Nullable
    public SessionState getState() {
        return state;
    }

    public void setState(@Nullable SessionState state) {
        this.state = state;
    }

    @NonNull
    public List<String> getInitDataQueue() {
        return initDataQueue;
    }

    @NonNull
    public BacklogManager getBacklogManager() {
        return backlogManager;
    }

    @Nullable
    public BufferViewManager getBufferViewManager() {
        return bufferViewManager;
    }

    public void setBufferViewManager(@NonNull final BufferViewManager bufferViewManager) {
        this.bufferViewManager = bufferViewManager;
        for (int id : bufferViewManager.BufferViews.keySet()) {
            sendInitRequest("BufferViewConfig", String.valueOf(id), true);
        }
    }

    public BufferSyncer getBufferSyncer() {
        return bufferSyncer;
    }

    public void setBufferSyncer(BufferSyncer bufferSyncer) {
        this.bufferSyncer = bufferSyncer;
    }

    public ClientInitAck getCore() {
        return core;
    }

    public void setCore(ClientInitAck core) {
        this.core = core;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    @NonNull
    public Collection<Buffer> getBuffers(int networkId) {
        return new Stream<>(this.buffers.values()).filter(buffer -> buffer.getInfo().networkId == networkId).list();
    }

    @NonNull
    public Collection<Network> getNetworks() {
        return networks.values();
    }

    @NonNull
    public ConnectionChangeEvent.Status getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(@NonNull final ConnectionChangeEvent.Status connectionStatus) {
        this.connectionStatus = connectionStatus;
        busProvider.sendEvent(new ConnectionChangeEvent(connectionStatus));
    }

    @NonNull
    public IObservableList<UICallback, NetworkWrapper> getNetworkList() {
        return networkList;
    }
}
