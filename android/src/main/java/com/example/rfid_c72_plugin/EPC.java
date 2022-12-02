package com.example.rfid_c72_plugin;

public class EPC {
    private String count;
    private String epc;
    private String id;
    private String rssi;

    private boolean isFind;

    public boolean isFind() {
        return this.isFind;
    }

    public void setFind(boolean isFind2) {
        this.isFind = isFind2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getEpc() {
        return this.epc;
    }

    public void setEpc(String epc2) {
        this.epc = epc2;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count2) {
        this.count = count2;
    }

    public String getRssi() {
        return this.rssi;
    }

    public void setRssi(String rssi2) {
        this.rssi = rssi2;
    }

    public String toString() {
        return "EPC [id=" + this.id + ", epc=" + this.epc + ", count=" + this.count + "]";
    }
}
