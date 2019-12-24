package com.sinothk.android.serial.port;

import com.aill.androidserialport.SerialPort;

import java.io.File;
import java.io.IOException;

public class SerialPortManager {

    private static SerialPort serialPort;

    /**
     * 打开串口
     *
     * @param devicePath 串口路径
     * @param baudRate   波特率
     * @param flags      flags 给0就好
     */
    public static void init(String devicePath, int baudRate, int flags) throws IOException {
        // "/dev/ttyS1"), 9600, 0
        // new File("/dev/ttyS1"), 9600, flags
        serialPort = new SerialPort(new File(devicePath), baudRate, flags);
    }
}
