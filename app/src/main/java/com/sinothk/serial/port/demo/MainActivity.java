package com.sinothk.serial.port.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sinothk.android.serial.port.SerialPortCallback;
import com.sinothk.android.serial.port.SerialPortManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText devPathEt, baudRateEt, flagsEt, dataEt, suEt;
    private Button initBtn, closeBtn, sendDataBtn, receiveBtn, devInfoBtn, devPathInfoBtn, setSuBtn;
    private TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        suEt = this.findViewById(R.id.suEt);
        devPathEt = this.findViewById(R.id.devPathEt);
        baudRateEt = this.findViewById(R.id.baudRateEt);
        flagsEt = this.findViewById(R.id.flagsEt);
        dataEt = this.findViewById(R.id.dataEt);

        infoTv = this.findViewById(R.id.infoTv);

        setSuBtn = this.findViewById(R.id.setSuBtn);
        initBtn = this.findViewById(R.id.initBtn);
        closeBtn = this.findViewById(R.id.closeBtn);
        devInfoBtn = this.findViewById(R.id.devInfoBtn);
        devPathInfoBtn = this.findViewById(R.id.devPathInfoBtn);

        sendDataBtn = this.findViewById(R.id.sendDataBtn);
        receiveBtn = this.findViewById(R.id.receiveBtn);

        setSuBtn.setOnClickListener(this);
        initBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        devInfoBtn.setOnClickListener(this);
        devPathInfoBtn.setOnClickListener(this);

        sendDataBtn.setOnClickListener(this);
        receiveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.initBtn:
                //"/dev/ttyS1" 9600 0
                String devPath = devPathEt.getText().toString();
                String baudRate = baudRateEt.getText().toString();
                String flags = flagsEt.getText().toString();

                if (TextUtils.isEmpty(devPath)) {
                    ToastUtil.show(this, "设备路径不能为空");
                    return;
                }

                if (TextUtils.isEmpty(baudRate)) {
                    ToastUtil.show(this, "波特率不能为空");
                    return;
                }

                if (TextUtils.isEmpty(flags)) {
                    ToastUtil.show(this, "flags不能为空");
                    return;
                }
                String msg = SerialPortManager.init(devPath, Integer.valueOf(baudRate), Integer.valueOf(flags));

                String logTxtInit = infoTv.getText().toString() + "\n" + msg;
                infoTv.setText(logTxtInit);
                break;
            case R.id.closeBtn:
                SerialPortManager.close();
                infoTv.setText("");

                break;
            case R.id.sendDataBtn:
                String sendMsg = dataEt.getText().toString();
                SerialPortManager.send(sendMsg, new SerialPortCallback<String>() {
                    @Override
                    public void onComplete(int code, String result) {
                        String logTxtSend = infoTv.getText().toString() + "\n" + code + ": " + result;
                        infoTv.setText(logTxtSend);
                    }
                });
                break;
            case R.id.receiveBtn:
                SerialPortManager.receive(new SerialPortCallback<String>() {
                    @Override
                    public void onComplete(int code, String result) {
                        String logTxtReceive = infoTv.getText().toString() + "\n" + code + ": " + result;
                        infoTv.setText(logTxtReceive);
                    }
                });
                break;

            case R.id.devInfoBtn:
                String[] allDevices = SerialPortManager.getAllDevices();

                StringBuilder allDevicesStr = new StringBuilder();
                for (String allDevice : allDevices) {
                    allDevicesStr.append(allDevice).append("；");
                }

                String logTxtDevInfo = infoTv.getText().toString() + "\n" + allDevicesStr;
                infoTv.setText(logTxtDevInfo);
                break;

            case R.id.devPathInfoBtn:
                String[] allDevicesPath = SerialPortManager.getAllDevicesPath();

                StringBuilder allDevicesPathStr = new StringBuilder();
                for (String s : allDevicesPath) {
                    allDevicesPathStr.append(s).append("；");
                }

                String logTxtDevicesPath = infoTv.getText().toString() + "\n" + allDevicesPathStr;
                infoTv.setText(logTxtDevicesPath);
                break;

            case R.id.setSuBtn:
                String suPath = suEt.getText().toString();
                SerialPortManager.setSuPath(suPath);
                break;
        }
    }
}
