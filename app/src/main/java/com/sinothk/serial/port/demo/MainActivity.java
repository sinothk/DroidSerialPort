package com.sinothk.serial.port.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sinothk.android.serial.port.SerialPortManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SerialPortManager.init("/dev/ttyS1",  9600, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
