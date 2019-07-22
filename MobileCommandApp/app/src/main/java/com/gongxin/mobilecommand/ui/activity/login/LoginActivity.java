package com.gongxin.mobilecommand.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.ui.activity.MainActivity;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.utils.UserUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.gyf.immersionbar.ImmersionBar;
import com.kook.KKManager;
import com.kook.sdk.wrapper.auth.consts.AuthTypeEnum;
import com.kook.view.dialog.DialogShowUtil;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.et_loginname)
    EditText mEtLoginname;
    @BindView(R.id.et_loginpsw)
    EditText mEtLoginpsw;
    @BindView(R.id.tv_login_button)
    TextView mBtLogin;
    private String token, uid, data = "";
    private CompositeDisposable mDisposable;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
        mDisposable = new CompositeDisposable();
        KKManager.openLog();
    }

    @Override
    protected void findViewById() {
        ImmersionBar.with(this)
                .statusBarColorTransform(R.color.white)
                .init();
    }

    @Override
    protected void setListener() {
        mBtLogin.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        Utils.hideSoftKeyBoard(this);
        String user = (String) SPUtil.get(context, "user", "");
        if (!TextUtils.isEmpty(user)) {
            mEtLoginname.setText(user);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_button:
                if (Utils.isNullOrEmpty(mEtLoginname.getText().toString())) {
                    ToastUtil.showToast(context, getString(R.string.toast_phone_login));
                    return;
                }
                if (Utils.isNullOrEmpty(mEtLoginpsw.getText().toString())) {
                    ToastUtil.showToast(context, getString(R.string.toast_phone_psw));
                    return;
                }
                requestLogin(1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);
        mBtLogin.setClickable(true);
        try {
            JSONObject jsonObject = JSON.parseObject(response.body());
            String code = jsonObject.getString("code");
            if ("200".equals(code)) {
                if (requestId == 1) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    Utils.hideSoftKeyBoard(this);
                    //登陆
                    token = jsonObject2.getString("token");
                    uid = jsonObject2.getString("appUserId");
                    SPUtil.put(context, "uid", uid);
                    SPUtil.put(context, "usertoken", token);
                    SPUtil.put(context, "psw", mEtLoginpsw.getText().toString());
                    SPUtil.put(context, "user", mEtLoginname.getText().toString());

                    requestUserInfo();

                } else if (requestId == 2) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    LoginUser loginUser = JSON.parseObject(
                            jsonObject2.toJSONString(), LoginUser.class);
                    //loginUser.setToken(token);
                    UserUtil.setLoginUser(loginUser);
                    ArrayList<LoginUser> loginList = new ArrayList<>();
                    loginList.add(loginUser);

                    SharePreferenceUtil.getUtil(context).setDataList("jsonList", loginList);
                    requestHomeLink();
                } else if (requestId == 3) {
                    data = jsonObject.getString("data");
                    initChat();
                }

            } else if ("4000".equals(code)) {
                ToastUtil.longToast(context, getString(R.string.toast_outdate_login));
                toLoginInfo(1);
            } else if ("4003".equals(code)) {
                ToastUtil.longToast(context, getString(R.string.toast_not_exist_phone));
            } else if ("4004".equals(code)) {
                ToastUtil.longToast(context, getString(R.string.toast_not_true_psw));
            } else {
                ToastUtil.shortToast(context, jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtil.shortToast(context, getString(R.string.toast_parse_error));
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpRequestErr(Response<String> response, int id) {
        super.onHttpRequestErr(response, id);
        if (id == 1) {
            mBtLogin.setClickable(true);
        }
    }

    private void requestLogin(int requestId) {

        showProgressDialog(getString(R.string.dialog_loading));
        mBtLogin.setClickable(false);
        try {
            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("username", mEtLoginname.getText().toString().trim());
            jsonObject.put("password", mEtLoginpsw.getText().toString().trim());
            httpRequestByPost("command/login", jsonObject, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestHomeLink() {
        try {
            HttpParams params = new HttpParams();
            //1：手机 2：pad
            params.put("devicetype", "1");
            httpRequestByGet("command/linked/home", params, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestUserInfo() {
        try {
            Map<String, String> jsonObject = new HashMap<>();
            httpRequestByPost("command/user/getUser", jsonObject, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChat() {
        // IMSDK会在后台自动登陆上一次登陆成功的账号。
        // 再次判断如果后台已经自动登陆了，就无需再度登陆。
        Disposable subscribe = KKManager.getInstance().observableInitResult()
                .take(1)
                .subscribe(integer -> {
                    // Log.e(TAG, "observableInitResult:" + integer);
                    if (integer == KKManager.LOGINED) { // 如果sdk 核心已经登陆成功了,无需登陆，直接进主界面
                        jumpToHome();
                    } else {
                        jumpToHome();
                        loginChat();
                    }
                });
        mDisposable.add(subscribe);
        //必须有 sd 卡权限
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe();
    }

    private void loginChat() {
        Disposable subscribe = KKManager.getInstance().login(mEtLoginname.getText().toString().trim(), mEtLoginpsw.getText().toString().trim(), "demo.91kook.com:8282", AuthTypeEnum.eAuthTypeNonePassword)
                .take(1)
                .subscribe(loginResult -> {
                    DialogShowUtil.dismissLoadingDialog();
                    int errorCode = loginResult.getLoginErr();
                    if (errorCode == 0) {
                        //jumpToHome();
                        getSharedPreferences("login", MODE_PRIVATE)
                                .edit().putString("ac", mEtLoginpsw.getText().toString().trim())
                                .putString("ip", "demo.91kook.com:8282")
                                .apply();
                        finish();
                    } else {
                        Toast.makeText(context, "聊天登录失败 " + errorCode, Toast.LENGTH_SHORT).show();
                    }
                });
        mDisposable.add(subscribe);
    }

    private void jumpToHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("dataUrl", data);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        /*i.putExtra("index", getIntent()
                .getIntExtra("index", 1));
        i.putExtra("id", getIntent().getStringExtra("id"));*/
        startActivity(i);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
