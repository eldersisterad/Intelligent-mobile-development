package com.work.checkwork.login;

import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.UserBean;


public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    public static final String EXTRA_USERTYPE = "userType";

    private TextView textview_title, teacher_register;
    private EditText user_name, user_phone, login_password;
    private UserBean mUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onViewCreated() {
        textview_title = findViewById(R.id.textview_title);
        textview_title.setText("注册");
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        login_password = findViewById(R.id.login_password);
        teacher_register = findViewById(R.id.teacher_register);

        findViewById(R.id.button_back).setOnClickListener(this);
        teacher_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.teacher_register:
                if (TextUtils.isEmpty(getUserName())) {
                    Toast.makeText(this, "姓名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getUsePhone())) {
                    Toast.makeText(this, "手机号码不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getPassword())) {
                    Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    Looper.prepare();
                    mUserBean = new UserBean(getUserName(), getUsePhone(), getPassword(), null);
                    int userInsert = UserOpe.getUserOpe().insertUserData(mUserBean);
                    if (userInsert == 1) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    Looper.loop();
                }).start();
                break;
        }
    }

    public String getUserName() {
        return user_name.getText().toString().trim();
    }

    public String getUsePhone() {
        return user_phone.getText().toString().trim();
    }

    public String getPassword() {
        return login_password.getText().toString().trim();
    }

}
