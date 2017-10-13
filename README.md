# Android接入聚合支付（SMPay）SDK文档
## 版本要求 
1.minSdkVersion 14+
## 接入流程
1.添加聚合支付（smpay）库到你的项目中：
```android
dependencies {
    compile 'com.smpay:smpay:1.0.7'
}
```
2.在AndroidManifest.xml文件中:
<br>&ensp;a.添加权限：
```android
<uses-permission android:name="android.permission.INTERNET"/>
```
<br>&ensp;b.注册activity：
```android
<activity
            android:name="com.jy.smpay.H5PayWebActivity"
            android:theme="@style/PayTheme"
            android:screenOrientation="portrait" />
```
3.在style.xml文件中添加以下代码
```
<style name="PayTheme" parent="Theme.AppCompat.Light.NoActionBar"/>
```
4.在application中初始化sdk（这里的获取请求订单信息链接（REQUEST_PARAMS_URL）需要自己配置）
  ```
  SMPaySDK.init(APPContants.REQUEST_PARAMS_URL);
  ```
5.调起支付界面：
```
SMPaySDK.startPay(context,REQUEST_PAY_CODE,orderListId,chanelType);
```
context：  
orderId：需要支付的订单id  
REQUEST_PAY_CODE：调起支付的请求码  
chanelType:支付通道，暂时只有两种微信扫码支付（"w-yun_wxscan"）和快捷支付（"mo_kj"）

6.最后还需要在onActivityResult方法里面写入下面代码
```
switch (requestCode){
    case REQUEST_PAY_CODE://支付结果回调
          String payResult = data.getExtras().getString("pay_result");
          break;
    default:
          break;
        }
```
payResult：支付结果返回值（这里需要解析,具体可以参考demo里面的做法）
