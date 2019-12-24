package com.sinothk.serial.port.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sinothk.android.serial.port.SerialPortCallback;
import com.sinothk.android.serial.port.SerialPortManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SerialPortManager.init("/dev/ttyS1", 9600, 0);

        SerialPortManager.send("123456789", new SerialPortCallback<String>() {
            @Override
            public void onComplete(int code, String result) {

            }
        });

        SerialPortManager.receive(new SerialPortCallback<String>() {
            @Override
            public void onComplete(int code, String result) {

            }
        });


        String[] allDevices = SerialPortManager.getAllDevices();
        String[] allDevicesPath = SerialPortManager.getAllDevicesPath();
        if (allDevices == null) {

        }
    }
}
