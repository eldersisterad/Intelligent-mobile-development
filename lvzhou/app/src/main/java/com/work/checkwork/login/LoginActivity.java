package com.work.checkwork.login;

import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.work.checkwork.BaseActivity;
import com.work.checkwork.MainActivity;
import com.work.checkwork.R;
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserName, mPassword;
    private TextView mRegisterText;
    private Button mLogin;

    // 设置 一下内容   xml  布局文件
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewCreated() {
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mRegisterText = findViewById(R.id.register_text);


        mLogin.setOnClickListener(this);
        mRegisterText.setOnClickListener(this);

        if ((Boolean) AppPreferences.get(this, AppConstant.EXTRA_ISLOGIN, false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (TextUtils.isEmpty(getUserName())) {
                    Toast.makeText(this, "用户名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(getPassword())) {
                    Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    Looper.prepare();
                    UserBean mUserBean = UserOpe.getUserOpe().getUserData(getUserName(), getPassword());
                    if (mUserBean != null) {
                        AppPreferences.setBeanByFastJson(this, AppConstant.EXTRA_USERINFO, mUserBean);
                        AppPreferences.put(this, AppConstant.EXTRA_ISLOGIN, true);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }).start();

                break;
            case R.id.register_text:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.EXTRA_USERTYPE, 1);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public String getUserName() {
        return mUserName.getText().toString().trim();
    }

    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

}
