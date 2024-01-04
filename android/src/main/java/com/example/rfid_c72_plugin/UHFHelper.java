package com.example.rfid_c72_plugin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.rscja.barcode.BarcodeDecoder;
import com.rscja.barcode.BarcodeFactory;
import com.rscja.barcode.BarcodeUtility;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.BarcodeEntity;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class UHFHelper {
    private static UHFHelper instance;
    public RFIDWithUHFUART mReader;

    String TAG="MainActivity_2D";

    public BarcodeDecoder barcodeDecoder;
    Handler handler;
    private UHFListener uhfListener;
    private boolean isStart = false;
    private boolean isConnect = false;
    //private boolean isSingleRead = false;
    private HashMap<String, EPC> tagList;

    private String scannedBarcode;

    private Context context;

    private UHFHelper() {
    }

    public static UHFHelper getInstance() {
        if (instance == null)
            instance = new UHFHelper();
        return instance;
    }

    //public RFIDWithUHFUART getReader() {
    //   return mReader;
    //}

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public void setUhfListener(UHFListener uhfListener) {
        this.uhfListener = uhfListener;
    }

    public void init(Context context) {
        this.context = context;
        //this.uhfListener = uhfListener;
        tagList = new HashMap<String, EPC>();
        clearData();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                addEPCToList(strs[0], strs[1]);
            }
        };

    }

    public String readBarcode(){
        if(scannedBarcode != null) {
            return scannedBarcode;
        }else{
            return "FAIL";
        }
    }

    public boolean connect() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {
            uhfListener.onConnect(false, 0);
            return false;
        }
        if (mReader != null) {
            isConnect = mReader.init(context);
            //mReader.setFrequencyMode(2);
            //mReader.setPower(29);
            uhfListener.onConnect(isConnect, 0);
            return isConnect;
        }
        uhfListener.onConnect(false, 0);
        return false;
    }



    public boolean connectBarcode() {
        if (barcodeDecoder == null) {
            barcodeDecoder = BarcodeFactory.getInstance().getBarcodeDecoder();
        }
        barcodeDecoder.open(context);

        //BarcodeUtility.getInstance().enablePlaySuccessSound(context, true);

        barcodeDecoder.setDecodeCallback(new BarcodeDecoder.DecodeCallback() {
            @Override
            public void onDecodeComplete(BarcodeEntity barcodeEntity) {
                Log.e(TAG,"BarcodeDecoder==========================:"+barcodeEntity.getResultCode());
                if(barcodeEntity.getResultCode() == BarcodeDecoder.DECODE_SUCCESS){
                    scannedBarcode = barcodeEntity.getBarcodeData();
                    Log.e(TAG,"Data==========================:"+barcodeEntity.getBarcodeData());
                }else{
                    scannedBarcode = "FAIL";
                }
            }
        });
        return true;
    }


    public boolean scanBarcode() {
        barcodeDecoder.startScan();
        Log.i(TAG, "Calling scan code");
        return true;
    }

    public boolean stopScan() {
        barcodeDecoder.stopScan();
        Log.i(TAG, "Calling stop scan");
        return true;
    }

    public boolean start(boolean isSingleRead) {
        if (!isStart) {
            if (isSingleRead) {// Single Read
                UHFTAGInfo strUII = mReader.inventorySingleTag();
                if (strUII != null) {
                    String strEPC = strUII.getEPC();
                    addEPCToList(strEPC, strUII.getRssi());
                    return true;
                } else {
                    return false;
                }
            } else {// Auto read multi  .startInventoryTag((byte) 0, (byte) 0))
                //  mContext.mReader.setEPCTIDMode(true);
                if (mReader.startInventoryTag()) {
                    isStart = true;
                    new TagThread().start();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearData() {
        tagList.clear();
    }

    public boolean stop() {
        if (isStart && mReader != null) {
            isStart = false;
            return mReader.stopInventory();
        }
        isStart = false;
        clearData();
        return false;
    }

    public void close() {
        isStart = false;
        if (mReader != null) {
            mReader.free();
            isConnect = false;
        }
        clearData();
    }

    public boolean setPowerLevel(String level) {
        //5 dBm : 30 dBm
        if (mReader != null) {
            return mReader.setPower(Integer.parseInt(level));
        }
        return false;
    }

    public boolean setWorkArea(String area) {
        //China Area 920~925MHz
        //Chin2a Area 840~845MHz
        //ETSI Area 865~868MHz
        //Fixed Area 915MHz
        //United States Area 902~928MHz
        //{ "1", "2" 4", "8", "22", "50", "51", "52", "128"}
        if (mReader != null)
            return mReader.setFrequencyMode(Integer.parseInt(area));
        return false;
    }

    private void addEPCToList(String epc, String rssi) {
        if (!TextUtils.isEmpty(epc)) {
            EPC tag = new EPC();

            tag.setId("");
            tag.setEpc(epc);
            tag.setCount(String.valueOf(1));
            tag.setRssi(rssi);

            if (tagList.containsKey(epc)) {
                int tagCount = Integer.parseInt(Objects.requireNonNull(tagList.get(epc)).getCount()) + 1;
                tag.setCount(String.valueOf(tagCount));
            }
            tagList.put(epc, tag);

            final JSONArray jsonArray = new JSONArray();

            for (EPC epcTag : tagList.values()) {
                JSONObject json = new JSONObject();
                try {
                    json.put(TagKey.ID, Objects.requireNonNull(epcTag).getId());
                    json.put(TagKey.EPC, epcTag.getEpc());
                    json.put(TagKey.RSSI, epcTag.getRssi());
                    json.put(TagKey.COUNT, epcTag.getCount());
                    jsonArray.put(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            uhfListener.onRead(jsonArray.toString());

        }
    }

    public boolean isEmptyTags() {
        return tagList != null && !tagList.isEmpty();
    }

    public boolean isStarted() {
        return isStart;
    }

    public boolean isConnected() {
        return isConnect;
    }

    public boolean closeScan() {
        barcodeDecoder.close();
        return true;
    }

    class TagThread extends Thread {
        public void run() {
            String strTid;
            String strResult;
            UHFTAGInfo res = null;
            while (isStart) {
                res = mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res.getTid();
                    if (strTid.length() != 0 && !strTid.equals("0000000" +
                            "000000000") && !strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Log.i("data", "c" + res.getEPC() + "|" + strResult);
                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + "EPC:" + res.getEPC() + "@" + res.getRssi();

                    handler.sendMessage(msg);
                }
            }
        }
    }

}
