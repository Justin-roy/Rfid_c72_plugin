package com.example.rfid_c72_plugin;

public abstract class UHFListener {
    abstract void onRead(String tagsJson);

    abstract void onConnect(boolean isConnected, int powerLevel);
}
