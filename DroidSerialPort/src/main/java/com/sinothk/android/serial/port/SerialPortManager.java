package com.sinothk.android.serial.port;

import com.aill.androidserialport.ByteUtil;
import com.aill.androidserialport.SerialPort;
import com.aill.androidserialport.SerialPortFinder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * https://codeload.github.com/AIlll/AndroidSerialPort/zip/master
 */
public class SerialPortManager {

    private static SerialPort serialPort;

    /**
     * 关闭端口
     */
    public static void close() {
        if (serialPort != null) {
            serialPort.close();
        }
    }

    /**
     * 打开串口
     *
     * @param devicePath 串口路径
     * @param baudRate   波特率
     * @param flags      flags 给0就好
     */
    public static String init(String devicePath, int baudRate, int flags) {
        // "/dev/ttyS1"), 9600, 0
        // new File("/dev/ttyS1"), 9600, flags

        try {
            if (devicePath == null || devicePath.length() == 0) {
                return "devicePath不能为空";
            }
            serialPort = new SerialPort(new File(devicePath), baudRate, flags);
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "初始化异常";
        }
    }

    /**
     * 发送数据
     *
     * @param dataStr
     * @param callback
     */
    public static void send(String dataStr, SerialPortCallback<String> callback) {
        if (serialPortDisable()) {
            callback.onComplete(0, "端口未开启");
            return;
        }

        try {
//            byte[] data = dataStr.getBytes();
            byte[] data = ByteUtil.hexStringToBytes(dataStr);

            //从串口对象中获取输出流
            OutputStream outputStream = serialPort.getOutputStream();
            //        //需要写入的数据
            //        byte[] data = new byte[x];
            //        data[0] = ...;
            //        data[1] = ...;
            //        data[x] = ...;
            //写入数据
            outputStream.write(data);
            outputStream.flush();

            callback.onComplete(200, "操作成功");
        } catch (IOException e) {
            callback.onComplete(0, e.getMessage());
        }
    }

    private static boolean serialPortDisable() {
        return serialPort == null;
    }

    /**
     * 接收数据
     *
     * @param callback
     */
    public static void receive(SerialPortCallback<String> callback) {
        if (serialPortDisable()) {
            callback.onComplete(0, "端口未开启");
            return;
        }

        try {
            //从串口对象中获取输入流
            InputStream inputStream = serialPort.getInputStream();

            byte[] buffers = null;

            //使用循环读取数据，建议放到子线程去
            while (true) {
                if (inputStream.available() > 0) {
                    //当接收到数据时，sleep 500毫秒（sleep时间自己把握）
                    Thread.sleep(500);
                    //sleep过后，再读取数据，基本上都是完整的数据
                    byte[] buffer = new byte[inputStream.available()];
                    int size = inputStream.read(buffer);

                    buffers = buffer;
                } else {
                    break;
                }
            }

            if (buffers != null) {
//                callback.onComplete(200, new String(buffers));
                callback.onComplete(200, ByteUtil.hexBytesToString(buffers));
            } else {
                callback.onComplete(201, "数据为空");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            callback.onComplete(0, e.getMessage());
        }
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String[] getAllDevices() {
//        ByteUtil类：工具类，字符串转字节数组，字节数组转字符串
//        SerialFinder类：用于查找设备下所有串口路径
        return new SerialPortFinder().getAllDevices();
    }

    /**
     * 获取设备信息路径
     *
     * @return
     */
    public static String[] getAllDevicesPath() {
//        ByteUtil类：工具类，字符串转字节数组，字节数组转字符串
//        SerialFinder类：用于查找设备下所有串口路径
        return new SerialPortFinder().getAllDevicesPath();
    }

    /**
     * 设置Su路径
     *
     * @param suPath
     */
    public static void setSuPath(String suPath) {
//        打开串口时，会检测读写权限，当没有权限时，会尝试对其进行提权
//        默认su路径是/system/bin/su，
//        有些设备su路径是/system/xbin/su
//        在new SerialPort();之前设置su路径
        SerialPort.setSuPath(suPath);
    }


//    //Original byte[]
//    byte[] bytes = "hello world".getBytes();
//    //Base64 Encoded
//    String encoded = Base64.getEncoder().encodeToString(bytes);
//    //Base64 Decoded
//    byte[] decoded = Base64.getDecoder().decode(encoded);
//    //Verify original content
//        System.out.println( new String(decoded) );
}
