package com.dynasty.getverifycode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAccount = (EditText) findViewById(R.id.et_account);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvResult = (TextView) findViewById(R.id.tv_result);

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((etAccount.getText().toString().equals("")||etAccount.getText().toString() == null)
                        && (etPassword.getText().toString().equals("")||etPassword.getText().toString() == null)) {
                    Toast.makeText(MainActivity.this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Dynasty_Qin", "都不空");
                    register(etAccount.getText().toString(), etPassword.getText().toString());
                }
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etAccount.getText().toString().equals("")||etAccount.getText().toString() == null)
                        && (etPassword.getText().toString().equals("")||etPassword.getText().toString() == null)) {
                    Toast.makeText(MainActivity.this, "账号、密码都不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    login(etAccount.getText().toString(), etPassword.getText().toString());
                    Log.e("Dynasty_Qin", tvResult.getText().toString());
                    if(tvResult.getText().toString().equals("code:200;message:登陆成功")){
                        /*此处用Intent来实现Activity与Activity之间的跳转*/
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this,GetSms.class);
                        //Intent intent=new Intent(IntentTest.this,MyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(tvResult.getText().toString().equals("code:100;message:登陆失败，密码不匹配或账号未注册")){
                        Toast.makeText(MainActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void register(String account, String password) {
        String registerUrlStr = Constant.URL_Register + "?account=" + account + "&password=" + password;
        new AsyncLoader(tvResult).execute(registerUrlStr);
    }

    private void login(String account, String password) {
        String registerUrlStr = Constant.URL_Login + "?account=" + account + "&password=" + password;
        new AsyncLoader(tvResult).execute(registerUrlStr);
    }

}