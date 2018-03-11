package tenyond.yank.com.carlocation.welcome;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;

import tenyond.yank.com.carlocation.R;
import tenyond.yank.com.carlocation.login.LoginActivity;
import tenyond.yank.com.carlocation.main.MainActivity;
import tenyond.yank.com.carlocation.register.RegisterActivity;
import tenyond.yank.com.carlocation.utils.ViewUtils;

/**
 * AUTHOR: Dream
 * DATE:  2018/1/20
 * EMAIL: YANK.TENYOND@GMAIL.COM
 * FUNC:
 */
public class WelcomeActivity extends AppCompatActivity {


    //申请存储权限
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        intentGo();

    }


    /**
     * 完善SP的登录逻辑
     */
    private void intentGo(){

        if(getSharedPreferences("isFirstIn",MODE_PRIVATE).getBoolean("isFirstIn",false)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
        }else if(getSharedPreferences("isLogin",MODE_PRIVATE).getBoolean("isLogin",false)){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //此处添加获取本地数据库缓存的代码
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
    }
}
