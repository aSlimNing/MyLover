package cn.bzu.internet.mylover;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 打电话发短信工具类
 * Created by angcyo on 15-10-11-01.
 */
public class PhoneUtil {

    /**
     * 显示短信发送界面
     */
    public static void sendSMSTo(Context context, String number, String msg) {
        // 弹出可发送短信的应用
        // Uri uri = Uri.parse("smsto:" + num);
        // Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        // sendIntent.putExtra("sms_body", msg);
        // context.startActivity(sendIntent);

        // 直接调用默认的短信应用
        Uri uri = Uri.parse("smsto:" + number);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", msg);
        context.startActivity(it);
    }

    /**
     * 打电话, 不弹出界面,直接拨打 需要权限
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    public static void call(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void callTo(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 分割短信, 并发送 权限
     * <uses-permission android:name="android.permission.SEND_SMS"/>
     * <uses-permission android:name="android.permission.READ_SMS" />
     * <uses-permission android:name="android.permission.RECEIVE_SMS" />
     */
    public static void sendSMS(final Context context, final String number, final String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        if (smsManager == null || !checkPermission(context, Manifest.permission.SEND_SMS)) {
            sendSMSTo(context, number, msg);
            return;
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SENT_SMS_ACTION"), 0);
        ArrayList<String> texts = smsManager.divideMessage(msg);// 分割短信
        for (String text : texts) {
            smsManager.sendTextMessage(number, null, text, sentIntent, null);
        }

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        MessageInfo.MsgToast(context, "短信发送成功");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        MessageInfo.MsgToast(context, "短信发送失败,一般错误原因");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        MessageInfo.MsgToast(context, "短信发送失败,无线发送信号被关闭");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        MessageInfo.MsgToast(context, "短信发送失败,没有提供数据单元");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        MessageInfo.MsgToast(context, "短信发送失败,当前没有获得可用的服务");
                        break;
                }
                if (getResultCode() != Activity.RESULT_OK) {
                    sendSMSTo(context, number, msg);
                }
            }
        }, new IntentFilter("SENT_SMS_ACTION"));
    }

    /**
     * 检查权限是否被禁用
     */
    public static boolean checkPermission(Context context, String permission) {
        // String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = context.checkCallingOrSelfPermission(permission);
//		int n = context.checkCallingPermission(permission);
//		context.checkPermission(permission, pid, uid)
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static class MessageInfo {
        public static void MsgToast(Context context, String toask) {
            Toast.makeText(context, toask, Toast.LENGTH_LONG).show();
        }
    }
}
