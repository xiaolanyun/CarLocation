package tenyond.yank.com.carlocation.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import tenyond.yank.com.carlocation.R;
import tenyond.yank.com.carlocation.login.LoginActivity;
import tenyond.yank.com.carlocation.utils.ViewUtils;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton toolbarBack,toolbarMore;
    private Button toolbarLogin,registerButton,verificationSend;
    private TextView toolbarTitle;
    private TextInputLayout phoneNumWrapper,verificateNumWrapper,passwordWrapper,carNumWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setStatusBar(this,getResources().getColor(R.color.toolBar));
        setContentView(R.layout.activity_register);

        initView();
        initData();
    }

    private void initData() {
        toolbarBack.setVisibility(View.GONE);
        toolbarMore.setVisibility(View.GONE);
        toolbarLogin.setVisibility(View.VISIBLE);
        toolbarLogin.setText("登录");
        toolbarLogin.setTextSize(15);
        toolbarTitle.setText("注册");

    }

    private void initView() {
        toolbarBack = findViewById(R.id.title_back);
        toolbarMore = findViewById(R.id.title_more);
        toolbarTitle = findViewById(R.id.title_text);
        toolbarLogin = findViewById(R.id.choice_btn);
        registerButton = findViewById(R.id.btn_register);
        verificationSend = findViewById(R.id.btn_verification);
        phoneNumWrapper = findViewById(R.id.phone_input_wrapper);
        passwordWrapper = findViewById(R.id.password_wrapper);
        carNumWrapper = findViewById(R.id.carNum_input_wrapper);
        verificateNumWrapper = findViewById(R.id.verification_input_wrapper);

        toolbarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        verificationSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(60*1000,1000) {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onTick(long millisUntilFinished) {
                        verificationSend.setEnabled(false);
                        verificationSend.setBackground(getDrawable(R.drawable.false_btn_bg));
                        verificationSend.setText("重新获取( " + millisUntilFinished / 1000 + "s )");
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onFinish() {
                        verificationSend.setEnabled(true);
                        verificationSend.setBackground(getDrawable(R.drawable.common_btn_bg));
                        verificationSend.setText("重新获取");
                    }
                }.start();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()){
                    ViewUtils.showToast(RegisterActivity.this,"请完善您的信息");
                }else {
                    Intent toNew = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(toNew);
                    finish();
                }
            }
        });
    }

    private boolean checkNull(){
        if(verificateNumWrapper.getEditText().getText().toString().trim().isEmpty()
                ||phoneNumWrapper.getEditText().getText().toString().trim().isEmpty()
                ||passwordWrapper.getEditText().getText().toString().trim().isEmpty()
                ||carNumWrapper.getEditText().getText().toString().trim().isEmpty())
            return true;
        return false;
    }
}
