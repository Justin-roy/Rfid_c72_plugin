package com.example.rfid_c72_plugin;

import java.util.Map;

public class TagKey {
    public final static String ID = "KEY_ID";
    public final static String RSSI = "KEY_RSSI";
    public final static String EPC = "KEY_EPC";
    public final static String COUNT = "KEY_COUNT";

    public  static String getTag(Map<String, Object> map){
        return ((String) map.get(EPC));
    }
}
