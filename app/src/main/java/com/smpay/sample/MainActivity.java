package com.smpay.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jy.smpay.SMPaySDK;

import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PAY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPay();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.main_pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开支付页面，这里的“326”代表订单id
                SMPaySDK.startPay(MainActivity.this,REQUEST_PAY_CODE,"326");
            }
        });
    }

    private void initPay() {
        //初始化聚合支付
        SMPaySDK.init(APPContants.REQUEST_PARAMS_URL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null||resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case REQUEST_PAY_CODE://支付结果回调
                String payResult = data.getExtras().getString("pay_result");
                showPayResult(payResult);
                break;
            default:
                break;
        }
    }

    /**
     * 解析支付结果
     * @param payResult
     */
    private void showPayResult(String payResult) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String substring = payResult.substring(1);
            String[] strs = substring.split("&");
            for (int i = 0; i < strs.length; i++) {
                switch (i) {
                    case 0:
                        stringBuilder.append("支付状态：");
                        switch (strs[i].substring(strs[i].indexOf("=")+1)) {
                            case "paid":
                                stringBuilder.append("支付完成\n");
                                break;
                            case "paying":
                                stringBuilder.append("支付中\n");
                                break;
                            case "paid_fail":
                                stringBuilder.append("支付失败");
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        stringBuilder.append("支付金额：");
                        stringBuilder.append((Double.parseDouble(strs[i].substring(strs[i].indexOf("=")+1)))/100L);
                        break;
                    case 2:
                        stringBuilder.append("\n订单号：");
                        stringBuilder.append(strs[i].substring(strs[i].indexOf("=")+1));
                        break;
                    case 3:
                        stringBuilder.append("\n商品名称：");
                        stringBuilder.append(URLDecoder.decode(strs[i].substring(strs[i].indexOf("=")+1),"UTF-8"));
                        break;
                }
            }
            payResult = stringBuilder.toString();
            showPayResultDialog(payResult);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 展示支付结果
     * @param result
     */
    private void showPayResultDialog(String result) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("支付结果");
        mBuilder.setMessage(result);
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.create().show();
    }
}
