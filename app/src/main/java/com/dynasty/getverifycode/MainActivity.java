package com.dynasty.getverifycode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
/**
 * 功能：Android.os.Handler负责接收，并按计划发送和处理消息.
 * 特点：处理消息是阻塞式的.
 */
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    TextView tv;
    //创建一个Handler对象
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用activity_main.xml文件定义的界面布局
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                tv.setText(getSmsInPhone());
                handler.postDelayed(this, 20000);
            }
        });
    }

    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";            //所有短信
        final String SMS_URI_INBOX = "content://sms/inbox";     //收件箱
        final String SMS_URI_SEND = "content://sms/sent";       //已发送
        final String SMS_URI_DRAFT = "content://sms/draft";     //草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox";   //发件箱
        final String SMS_URI_FAILED = "content://sms/failed";   //发送失败
        final String SMS_URI_QUEUED = "content://sms/queued";   //待发送列表

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

            if (cur.moveToFirst()) {
                /**
                 * sms主要结构：
                 * 1._id => 短消息序号 如100
                 * 2.thread_id => 对话的序号 如100
                 * 3.address => 发件人地址，手机号.如+8613811810000
                 * 4.person => 发件人，返回一个数字就是联系人列表里的序号，陌生人为null
                 * 5.date => 日期  long型。如1256539465022
                 * 6.protocol => 协议 0 SMS_RPOTO, 1 MMS_PROTO
                 * 7.read => 是否阅读 0未读， 1已读
                 * 8.status => 状态 -1接收，0 complete, 64 pending, 128 failed
                 * 9.type => 类型 1是接收到的，2是已发出
                 * 10.body => 短消息内容
                 * 11.service_center => 短信服务中心号码编号。如+8613800755500
                 */
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");

                do {
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);


                    if (strAddress.equals("95516")) {
                        //这里我是要获取自己短信服务号码中的验证码~~
                        Pattern pattern = Pattern.compile("\\d{6}");
                        Matcher matcher = pattern.matcher(strbody);
                        if (matcher.find()) {
                            String res = matcher.group();
                            smsBuilder.append(res);

                        }
                        break;
                    }

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            } else {
                smsBuilder.append("no result!");
            }
            //smsBuilder.append("已获取银行邀请验证码!");

        } catch (SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        return smsBuilder.toString();
    }
}