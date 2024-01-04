package com.example.rfid_c72_plugin;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * RfidC72Plugin
 */
public class RfidC72Plugin implements FlutterPlugin, MethodCallHandler {
  private static final String CHANNEL_IsStarted = "isStarted";
  private static final String CHANNEL_StartSingle = "startSingle";
  private static final String CHANNEL_StartContinuous = "startContinuous";
  private static final String CHANNEL_Stop = "stop";
  private static final String CHANNEL_ClearData = "clearData";
  private static final String CHANNEL_IsEmptyTags = "isEmptyTags";
  private static final String CHANNEL_Close = "close";
  private static final String CHANNEL_Connect = "connect";
  private static final String CHANNEL_IsConnected = "isConnected";
  private static final String CHANNEL_SETPOWERLEVEL = "setPowerLevel";
  private static final String CHANNEL_SETWORKAREA = "setWorkArea";
  private static final String CHANNEL_ConnectedStatus = "ConnectedStatus";
  private static final String CHANNEL_TagsStatus = "TagsStatus";
  private static final String CHANNEL_CONNECT_BARCODE = "connectBarcode";
  private static final String CHANNEL_SCAN_BARCODE = "scanBarcode";
  private static final String CHANNEL_STOP_SCAN_BARCODE = "stopScan";
  private static final String CHANNEL_READ_BARCODE = "readBarcode";
  private static final String CHANNEL_CLOSE_SCAN_BARCODE="closeScan";
  private static PublishSubject<Boolean> connectedStatus = PublishSubject.create();
  private static PublishSubject<String> tagsStatus = PublishSubject.create();

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "rfid_c72_plugin");
    initConnectedEvent(registrar.messenger());
    initReadEvent(registrar.messenger());
    channel.setMethodCallHandler(new RfidC72Plugin());

    UHFHelper.getInstance().init(registrar.context());
    UHFHelper.getInstance().setUhfListener(new UHFListener() {
      @Override
      public void onRead(String tagsJson) {
        if (tagsJson != null)
          tagsStatus.onNext(tagsJson);
      }

      @Override
      public void onConnect(boolean isConnected, int powerLevel) {
        connectedStatus.onNext(isConnected);
      }
    });
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "rfid_c72_plugin");
    initConnectedEvent(flutterPluginBinding.getBinaryMessenger());
    initReadEvent(flutterPluginBinding.getBinaryMessenger());
    Context applicationContext = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(new RfidC72Plugin());
    UHFHelper.getInstance().init(applicationContext);
    UHFHelper.getInstance().setUhfListener(new UHFListener() {
      @Override
      public void onRead(String tagsJson) {
        if (tagsJson != null)
          tagsStatus.onNext(tagsJson);
      }

      @Override
      public void onConnect(boolean isConnected, int powerLevel) {
        connectedStatus.onNext(isConnected);
      }
    });
  }


  private static void initConnectedEvent(BinaryMessenger messenger) {
    final EventChannel scannerEventChannel = new EventChannel(messenger, CHANNEL_ConnectedStatus);
    scannerEventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object o, final EventChannel.EventSink eventSink) {
        connectedStatus
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(Boolean isConnected) {
                    eventSink.success(isConnected);
                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onComplete() {

                  }
                });
      }

      @Override
      public void onCancel(Object o) {

      }
    });
  }

  private static void initReadEvent(BinaryMessenger messenger) {
    final EventChannel scannerEventChannel = new EventChannel(messenger, CHANNEL_TagsStatus);
    scannerEventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object o, final EventChannel.EventSink eventSink) {
        tagsStatus
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(String tag) {
                    eventSink.success(tag);
                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onComplete() {

                  }
                });
      }

      @Override
      public void onCancel(Object o) {

      }
    });
  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    handleMethods(call, result);
  }

  private void handleMethods(MethodCall call, Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case CHANNEL_IsStarted:
        result.success(UHFHelper.getInstance().isStarted());
        break;
      case CHANNEL_StartSingle:
        result.success(UHFHelper.getInstance().start(true));
        break;
      case CHANNEL_StartContinuous:
        result.success(UHFHelper.getInstance().start(false));
        break;
      case CHANNEL_Stop:
        result.success(UHFHelper.getInstance().stop());
        break;
      case CHANNEL_ClearData:
        UHFHelper.getInstance().clearData();
        result.success(true);
        break;
      case CHANNEL_IsEmptyTags:
        result.success(UHFHelper.getInstance().isEmptyTags());
        break;
      case CHANNEL_Close:
        UHFHelper.getInstance().close();
        result.success(true);
        break;
      case CHANNEL_Connect:
        result.success(UHFHelper.getInstance().connect());
        break;
      case CHANNEL_IsConnected:
        result.success(UHFHelper.getInstance().isConnected());
        break;
      case CHANNEL_SETPOWERLEVEL:
        String powerLevel = call.argument("value");
        result.success(UHFHelper.getInstance().setPowerLevel(powerLevel));
        break;
      case CHANNEL_SETWORKAREA:
        String workArea = call.argument("value");
        result.success(UHFHelper.getInstance().setWorkArea(workArea));
        break;
      case CHANNEL_CONNECT_BARCODE:
        result.success(UHFHelper.getInstance().connectBarcode());
        break;
      case CHANNEL_SCAN_BARCODE:
        result.success(UHFHelper.getInstance().scanBarcode());
        break;
      case CHANNEL_STOP_SCAN_BARCODE:
        result.success(UHFHelper.getInstance().stopScan());
        break;
      case CHANNEL_CLOSE_SCAN_BARCODE:
        result.success(UHFHelper.getInstance().closeScan());
        break;
      case CHANNEL_READ_BARCODE:
        result.success(UHFHelper.getInstance().readBarcode());
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

}
