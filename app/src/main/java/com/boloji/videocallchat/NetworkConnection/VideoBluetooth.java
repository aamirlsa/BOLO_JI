package com.boloji.videocallchat.NetworkConnection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.List;
import java.util.Set;

public class VideoBluetooth {
    private static final int BLUETOOTH_SCO_TIMEOUT_MS = 4000;
    private static final int MAX_SCO_CONNECTION_ATTEMPTS = 2;
    private static final String TAG = "VideoCallBluetoothManager";
    private final AudioCallManger apprtcAudioManager;
    private final Context apprtcContext;
    private final AudioManager audioManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothHeadset bluetoothHeadset;
    private final BroadcastReceiver bluetoothHeadsetReceiver;
    private final BluetoothProfile.ServiceListener bluetoothServiceListener;
    private State bluetoothState;
    private final Runnable bluetoothTimeoutRunnable = new Runnable() {
      

        public void run() {
            VideoBluetooth.this.bluetoothTimeout();
        }
    };
    private final Handler handler;
    int scoConnectionAttempts;

    public enum State {
        UNINITIALIZED,
        ERROR,
        HEADSET_UNAVAILABLE,
        HEADSET_AVAILABLE,
        SCO_DISCONNECTING,
        SCO_CONNECTING,
        SCO_CONNECTED
    }

    
    private String stateToString(int i) {
        if (i == 0) {
            return "DISCONNECTED";
        }
        if (i == 1) {
            return "CONNECTING";
        }
        if (i == 2) {
            return "CONNECTED";
        }
        if (i == 3) {
            return "DISCONNECTING";
        }
        switch (i) {
            case 10:
                return "OFF";
            case 11:
                return "TURNING_ON";
            case 12:
                return "ON";
            case 13:
                return "TURNING_OFF";
            default:
                return "INVALID";
        }
    }

    private class BluetoothServiceListener implements BluetoothProfile.ServiceListener {
        private BluetoothServiceListener() {
        }

        @SuppressLint("LongLogTag")
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (i == 1 && VideoBluetooth.this.bluetoothState != State.UNINITIALIZED) {
                Log.d(VideoBluetooth.TAG, "BluetoothServiceListener.onServiceConnected: BT state=" + VideoBluetooth.this.bluetoothState);
                VideoBluetooth.this.bluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
                VideoBluetooth.this.updateAudioDeviceState();
                Log.d(VideoBluetooth.TAG, "onServiceConnected done: BT state=" + VideoBluetooth.this.bluetoothState);
            }
        }

        @SuppressLint("LongLogTag")
        public void onServiceDisconnected(int i) {
            if (i == 1 && VideoBluetooth.this.bluetoothState != State.UNINITIALIZED) {
                Log.d(VideoBluetooth.TAG, "BluetoothServiceListener.onServiceDisconnected: BT state=" + VideoBluetooth.this.bluetoothState);
                VideoBluetooth.this.stopScoAudio();
                VideoBluetooth.this.bluetoothHeadset = null;
                VideoBluetooth.this.bluetoothDevice = null;
                VideoBluetooth.this.bluetoothState = State.HEADSET_UNAVAILABLE;
                VideoBluetooth.this.updateAudioDeviceState();
                Log.d(VideoBluetooth.TAG, "onServiceDisconnected done: BT state=" + VideoBluetooth.this.bluetoothState);
            }
        }
    }

    private class BluetoothHeadsetBroadcastReceiver extends BroadcastReceiver {
        private BluetoothHeadsetBroadcastReceiver() {
        }

        @SuppressLint("LongLogTag")
        public void onReceive(Context context, Intent intent) {
            if (VideoBluetooth.this.bluetoothState != State.UNINITIALIZED) {
                String action = intent.getAction();
                if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                    int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                    Log.d(VideoBluetooth.TAG, "BluetoothHeadsetBroadcastReceiver.onReceive: a=ACTION_CONNECTION_STATE_CHANGED, s=" + VideoBluetooth.this.stateToString(intExtra) + ", sb=" + isInitialStickyBroadcast() + ", BT state: " + VideoBluetooth.this.bluetoothState);
                    if (intExtra == 2) {
                        VideoBluetooth.this.scoConnectionAttempts = 0;
                        VideoBluetooth.this.updateAudioDeviceState();
                    } else if (!(intExtra == 1 || intExtra == 3 || intExtra != 0)) {
                        VideoBluetooth.this.stopScoAudio();
                        VideoBluetooth.this.updateAudioDeviceState();
                    }
                } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                    int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 10);
                    Log.d(VideoBluetooth.TAG, "BluetoothHeadsetBroadcastReceiver.onReceive: a=ACTION_AUDIO_STATE_CHANGED, s=" + VideoBluetooth.this.stateToString(intExtra2) + ", sb=" + isInitialStickyBroadcast() + ", BT state: " + VideoBluetooth.this.bluetoothState);
                    if (intExtra2 == 12) {
                        VideoBluetooth.this.cancelTimer();
                        if (VideoBluetooth.this.bluetoothState == State.SCO_CONNECTING) {
                            Log.d(VideoBluetooth.TAG, "+++ Bluetooth audio SCO is now connected");
                            VideoBluetooth.this.bluetoothState = State.SCO_CONNECTED;
                            VideoBluetooth.this.scoConnectionAttempts = 0;
                            VideoBluetooth.this.updateAudioDeviceState();
                        } else {
                            Log.w(VideoBluetooth.TAG, "Unexpected state BluetoothHeadset.STATE_AUDIO_CONNECTED");
                        }
                    } else if (intExtra2 == 11) {
                        Log.d(VideoBluetooth.TAG, "+++ Bluetooth audio SCO is now connecting...");
                    } else if (intExtra2 == 10) {
                        Log.d(VideoBluetooth.TAG, "+++ Bluetooth audio SCO is now disconnected");
                        if (isInitialStickyBroadcast()) {
                            Log.d(VideoBluetooth.TAG, "Ignore STATE_AUDIO_DISCONNECTED initial sticky broadcast.");
                            return;
                        }
                        VideoBluetooth.this.updateAudioDeviceState();
                    }
                }
                Log.d(VideoBluetooth.TAG, "onReceive done: BT state=" + VideoBluetooth.this.bluetoothState);
            }
        }
    }

    @SuppressLint("LongLogTag")
    static VideoBluetooth create(Context context, AudioCallManger videoCallAudioManager) {
        Log.d(TAG, "create" + VideoDatas.getThreadInfo());
        return new VideoBluetooth(context, videoCallAudioManager);
    }

    @SuppressLint("LongLogTag")
    protected VideoBluetooth(Context context, AudioCallManger videoCallAudioManager) {
        Log.d(TAG, "ctor");
        this.apprtcContext = context;
        this.apprtcAudioManager = videoCallAudioManager;
        this.audioManager = getAudioManager(context);
        this.bluetoothState = State.UNINITIALIZED;
        this.bluetoothServiceListener = new BluetoothServiceListener();
        this.bluetoothHeadsetReceiver = new BluetoothHeadsetBroadcastReceiver();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public State getState() {
        return this.bluetoothState;
    }

    @SuppressLint({"LongLogTag", "MissingPermission"})
    public void start() {
        if (!hasPermission(this.apprtcContext, "android.permission.BLUETOOTH")) {
            Log.w(TAG, "Process (pid=" + Process.myPid() + ") lacks BLUETOOTH permission");
        } else if (this.bluetoothState != State.UNINITIALIZED) {
            Log.w(TAG, "Invalid BT state");
        } else {
            this.bluetoothHeadset = null;
            this.bluetoothDevice = null;
            this.scoConnectionAttempts = 0;
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            this.bluetoothAdapter = defaultAdapter;
            if (defaultAdapter == null) {
                Log.w(TAG, "Device does not support Bluetooth");
            } else if (!this.audioManager.isBluetoothScoAvailableOffCall()) {
                Log.e(TAG, "Bluetooth SCO audio is not available off call");
            } else {
                logBluetoothAdapterInfo(this.bluetoothAdapter);
                if (!getBluetoothProfileProxy(this.apprtcContext, this.bluetoothServiceListener, 1)) {
                    Log.e(TAG, "BluetoothAdapter.getProfileProxy(HEADSET) failed");
                    return;
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
                registerReceiver(this.bluetoothHeadsetReceiver, intentFilter);
                Log.d(TAG, "HEADSET profile state: " + stateToString(this.bluetoothAdapter.getProfileConnectionState(1)));
                Log.d(TAG, "Bluetooth proxy for headset profile has started");
                this.bluetoothState = State.HEADSET_UNAVAILABLE;
                Log.d(TAG, "start done: BT state=" + this.bluetoothState);
            }
        }
    }

    @SuppressLint("LongLogTag")
    public void stop() {
        Log.d(TAG, "stop: BT state=" + this.bluetoothState);
        if (this.bluetoothAdapter != null) {
            stopScoAudio();
            if (this.bluetoothState != State.UNINITIALIZED) {
                unregisterReceiver(this.bluetoothHeadsetReceiver);
                cancelTimer();
                BluetoothHeadset bluetoothHeadset2 = this.bluetoothHeadset;
                if (bluetoothHeadset2 != null) {
                    this.bluetoothAdapter.closeProfileProxy(1, bluetoothHeadset2);
                    this.bluetoothHeadset = null;
                }
                this.bluetoothAdapter = null;
                this.bluetoothDevice = null;
                this.bluetoothState = State.UNINITIALIZED;
                Log.d(TAG, "stop done: BT state=" + this.bluetoothState);
            }
        }
    }

    @SuppressLint("LongLogTag")
    public boolean startScoAudio() {
        Log.d(TAG, "startSco: BT state=" + this.bluetoothState + ", attempts: " + this.scoConnectionAttempts + ", SCO is on: " + isScoOn());
        if (this.scoConnectionAttempts >= 2) {
            Log.e(TAG, "BT SCO connection fails - no more attempts");
            return false;
        } else if (this.bluetoothState != State.HEADSET_AVAILABLE) {
            Log.e(TAG, "BT SCO connection fails - no headset available");
            return false;
        } else {
            Log.d(TAG, "Starting Bluetooth SCO and waits for ACTION_AUDIO_STATE_CHANGED...");
            this.bluetoothState = State.SCO_CONNECTING;
            this.audioManager.startBluetoothSco();
            this.audioManager.setBluetoothScoOn(true);
            this.scoConnectionAttempts++;
            startTimer();
            Log.d(TAG, "startScoAudio done: BT state=" + this.bluetoothState + ", SCO is on: " + isScoOn());
            return true;
        }
    }

    @SuppressLint("LongLogTag")
    public void stopScoAudio() {
        Log.d(TAG, "stopScoAudio: BT state=" + this.bluetoothState + ", SCO is on: " + isScoOn());
        if (this.bluetoothState == State.SCO_CONNECTING || this.bluetoothState == State.SCO_CONNECTED) {
            cancelTimer();
            this.audioManager.stopBluetoothSco();
            this.audioManager.setBluetoothScoOn(false);
            this.bluetoothState = State.SCO_DISCONNECTING;
            Log.d(TAG, "stopScoAudio done: BT state=" + this.bluetoothState + ", SCO is on: " + isScoOn());
        }
    }

    @SuppressLint({"MissingPermission", "LongLogTag"})
    public void updateDevice() {
        if (this.bluetoothState != State.UNINITIALIZED && this.bluetoothHeadset != null) {
            Log.d(TAG, "updateDevice");
            @SuppressLint("MissingPermission") List<BluetoothDevice> connectedDevices = this.bluetoothHeadset.getConnectedDevices();
            if (connectedDevices.isEmpty()) {
                this.bluetoothDevice = null;
                this.bluetoothState = State.HEADSET_UNAVAILABLE;
                Log.d(TAG, "No connected bluetooth headset");
            } else {
                this.bluetoothDevice = connectedDevices.get(0);
                this.bluetoothState = State.HEADSET_AVAILABLE;
                Log.d(TAG, "Connected bluetooth headset: name=" + this.bluetoothDevice.getName() + ", state=" + stateToString(this.bluetoothHeadset.getConnectionState(this.bluetoothDevice)) + ", SCO audio=" + this.bluetoothHeadset.isAudioConnected(this.bluetoothDevice));
            }
            Log.d(TAG, "updateDevice done: BT state=" + this.bluetoothState);
        }
    }

    @SuppressLint("WrongConstant")
    public AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService("audio");
    }

    
    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        this.apprtcContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        this.apprtcContext.unregisterReceiver(broadcastReceiver);
    }

    
    public boolean getBluetoothProfileProxy(Context context, BluetoothProfile.ServiceListener serviceListener, int i) {
        return this.bluetoothAdapter.getProfileProxy(context, serviceListener, i);
    }

    
    @SuppressLint("WrongConstant")
    public boolean hasPermission(Context context, String str) {
        return this.apprtcContext.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    
    @SuppressLint({"MissingPermission", "LongLogTag"})
    public void logBluetoothAdapterInfo(BluetoothAdapter bluetoothAdapter2) {
        Log.d(TAG, "BluetoothAdapter: enabled=" + bluetoothAdapter2.isEnabled() + ", state=" + stateToString(bluetoothAdapter2.getState()) + ", name=" + bluetoothAdapter2.getName() + ", address=" + bluetoothAdapter2.getAddress());
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter2.getBondedDevices();
        if (!bondedDevices.isEmpty()) {
            Log.d(TAG, "paired devices:");
            for (BluetoothDevice bluetoothDevice2 : bondedDevices) {
                Log.d(TAG, " name=" + bluetoothDevice2.getName() + ", address=" + bluetoothDevice2.getAddress());
            }
        }
    }

    

    @SuppressLint("LongLogTag")
    private void updateAudioDeviceState() {
        Log.d(TAG, "updateAudioDeviceState");
        this.apprtcAudioManager.updateAudioDeviceState();
    }

    @SuppressLint("LongLogTag")
    private void startTimer() {
        Log.d(TAG, "startTimer");
        this.handler.postDelayed(this.bluetoothTimeoutRunnable, 4000);
    }

    
    
    @SuppressLint("LongLogTag")
    private void cancelTimer() {
        Log.d(TAG, "cancelTimer");
        this.handler.removeCallbacks(this.bluetoothTimeoutRunnable);
    }

    

    @SuppressLint({"LongLogTag", "MissingPermission"})
    private void bluetoothTimeout() {
        boolean z;
        if (this.bluetoothState != State.UNINITIALIZED && this.bluetoothHeadset != null) {
            Log.d(TAG, "bluetoothTimeout: BT state=" + this.bluetoothState + ", attempts: " + this.scoConnectionAttempts + ", SCO is on: " + isScoOn());
            if (this.bluetoothState == State.SCO_CONNECTING) {
                @SuppressLint("MissingPermission") List<BluetoothDevice> connectedDevices = this.bluetoothHeadset.getConnectedDevices();
                if (connectedDevices.size() > 0) {
                    BluetoothDevice bluetoothDevice2 = connectedDevices.get(0);
                    this.bluetoothDevice = bluetoothDevice2;
                    if (this.bluetoothHeadset.isAudioConnected(bluetoothDevice2)) {
                        Log.d(TAG, "SCO connected with " + this.bluetoothDevice.getName());
                        z = true;
                        if (!z) {
                            this.bluetoothState = State.SCO_CONNECTED;
                            this.scoConnectionAttempts = 0;
                        } else {
                            Log.w(TAG, "BT failed to connect after timeout");
                            stopScoAudio();
                        }
                        updateAudioDeviceState();
                        Log.d(TAG, "bluetoothTimeout done: BT state=" + this.bluetoothState);
                    }
                    Log.d(TAG, "SCO is not connected with " + this.bluetoothDevice.getName());
                }
                z = false;
                if (!z) {
                }
                updateAudioDeviceState();
                Log.d(TAG, "bluetoothTimeout done: BT state=" + this.bluetoothState);
            }
        }
    }

    private boolean isScoOn() {
        return this.audioManager.isBluetoothScoOn();
    }
}
