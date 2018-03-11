package tenyond.yank.com.carlocation.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;

import tenyond.yank.com.carlocation.R;
import tenyond.yank.com.carlocation.main.MainActivity;
import tenyond.yank.com.carlocation.register.RegisterActivity;
import tenyond.yank.com.carlocation.utils.ViewUtils;

/**
 * AUTHOR: Dream
 * DATE:  2018/3/8
 * EMAIL: YANK.TENYOND@GMAIL.COM
 * FUNC:
 */

public class LoginActivity extends AppCompatActivity {

    private final int STORAGE_CODE= 0;

    private ImageButton toolbarBack,toolbarMore;
    private Button toolbarRegister,loginButton,forgetPasswordBtn,needHelp;
    private TextView toolbarTitle;
    private TextInputLayout usernameWrapper,passwordWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setStatusBar(this,getResources().getColor(R.color.toolBar));
        setContentView(R.layout.activity_login);

        initView();
        initData();
        checkPermission();

    }

    private void checkPermission(){
        Permissions4M.get(LoginActivity.this) .requestForce(true) .requestUnderM(false)
                .requestPermissions(Manifest.permission_group.STORAGE)
                .requestCodes(STORAGE_CODE)
                .requestOnRationale().requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(LoginActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionsGranted(STORAGE_CODE)
    public void PermissionsGranted() {
        ViewUtils.showToast(LoginActivity.this,"存储权限授权成功");
    }

    @PermissionsDenied(STORAGE_CODE)
    public void PermissionsDenied() {
        ViewUtils.showToast(LoginActivity.this,"存储权限授权失败");
    }

    @PermissionsRationale(STORAGE_CODE)
    public void PermissionsRationale() {
        ViewUtils.showToast(LoginActivity.this,"请开启存储权限授权");
    }

    @PermissionsNonRationale(STORAGE_CODE)
    public void PermissionsNonRationale(Intent intent){
        startActivity(intent);
    }


    private void initData() {

        toolbarBack.setVisibility(View.GONE);
        toolbarMore.setVisibility(View.GONE);
        toolbarRegister.setVisibility(View.VISIBLE);
        toolbarRegister.setText("注册");
        toolbarTitle.setText("登录");

    }

    private void initView() {

        toolbarBack = findViewById(R.id.title_back);
        toolbarMore = findViewById(R.id.title_more);
        toolbarRegister = findViewById(R.id.choice_btn);
        loginButton = findViewById(R.id.btn_login);
        forgetPasswordBtn = findViewById(R.id.btn_forgetPassword);
        needHelp = findViewById(R.id.btn_needHelp);
        toolbarTitle = findViewById(R.id.title_text);
        usernameWrapper = findViewById(R.id.username_input_wrapper);
        passwordWrapper = findViewById(R.id.password_input_wrapper);

        toolbarRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()){
                    ViewUtils.showToast(LoginActivity.this,"用户名或密码不能为空");
                }else {
                    Intent toNew = new Intent(LoginActivity.this , MainActivity.class);
                    Log.e("@@toNewActivity","WORK");
                    startActivity(toNew);
                    finish();
                }
            }
        });

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showToast(LoginActivity.this,"功能开发中，敬请期待");
            }
        });
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showToast(LoginActivity.this,"功能开发中，敬请期待");
            }
        });
    }

    public boolean checkNull() {
        if(usernameWrapper.getEditText().getText().toString().isEmpty()||passwordWrapper.getEditText().getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
}
