package com.sinothk.android.serial.port;

public interface SerialPortCallback<T> {

    void onComplete(int code, T result);

}
