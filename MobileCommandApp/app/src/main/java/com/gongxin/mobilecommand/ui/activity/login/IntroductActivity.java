package com.gongxin.mobilecommand.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.activity.GestureLockLoginActivity;
import com.gongxin.mobilecommand.ui.activity.MainActivity;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.utils.UserUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.kook.KKManager;
import com.kook.sdk.wrapper.auth.consts.AuthTypeEnum;
import com.kook.view.dialog.DialogShowUtil;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class IntroductActivity extends BaseActivity {

    private boolean start;
    private static final int PAUSE_TIME = 1;//700
    private String userId, token;
    String data = "";
    CompositeDisposable mDisposable;

    @Override
    protected void loadViewLayout() {

        start = getIntent().getBooleanExtra("start", true);
        setContentView(R.layout.introduct);
        mDisposable = new CompositeDisposable();
        KKManager.openLog();
        mHandler.sendEmptyMessageDelayed(1, PAUSE_TIME);
    }


    private void initChat() {
        // IMSDK会在后台自动登陆上一次登陆成功的账号。
        // 再次判断如果后台已经自动登陆了，就无需再度登陆。
        Disposable subscribe = KKManager.getInstance().observableInitResult()
                .take(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        // Log.e(TAG, "observableInitResult:" + integer);
                        if (integer == KKManager.LOGINED) { // 如果sdk 核心已经登陆成功了,无需登陆，直接进主界面
                            jumpToHome();
                        } else {
                            jumpToHome();
                            loginChat();
                        }
                    }
                });
        mDisposable.add(subscribe);
        //必须有 sd 卡权限
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe();
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected void setListener() {
    }


    @Override
    protected void processLogic() {
        userId = (String) SPUtil.get(context, "uid", "0");
        token = (String) SPUtil.get(context, "usertoken", "0");
    }

    private void requestUserInfo() {
        try {
            Map<String, String> jsonObject = new HashMap<>();
            httpRequestByPost("command/user/getUser", jsonObject, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestHomeLink() {
        try {
            HttpParams params = new HttpParams();
            //1：手机 2：pad
            params.put("devicetype", "1");
            httpRequestByGet("command/linked/home", params, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);
        try {
            JSONObject jsonObject = JSON.parseObject(response.body());
            Integer code = jsonObject.getInteger("code");
            if (code == 200) {
                if (requestId == 1) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                    LoginUser loginUser = JSON.parseObject(
                            jsonObject2.toJSONString(), LoginUser.class);
                    loginUser.setToken(token);
                    loginUser.setAppUserId(userId);
                    UserUtil.setLoginUser(loginUser);
                    ArrayList<LoginUser> loginList = new ArrayList<>();
                    loginList.add(loginUser);

                    SharePreferenceUtil.getUtil(context).setDataList("jsonList", loginList);

                    requestHomeLink();
                } else if (requestId == 2) {
                    data = jsonObject.getString("data");
                    initChat();
                }
            } else if (code == 4000) {
                ToastUtil.longToast(context, getString(R.string.toast_outdate_login));
                toLoginInfo(1);
                finish();

            } else {
                ToastUtil.shortToast(context, jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtil.shortToast(context, getString(R.string.toast_parse_error));
            jumpToHome();
            e.printStackTrace();
        }
    }

    @Override
    protected void onHttpRequestErr(Response<String> response, int id) {
        super.onHttpRequestErr(response, id);
        ToastUtil.shortToast(context, getString(R.string.toast_net_failed));
        ToastUtil.showToast(context, "ip地址" + HttpUtil.BASEURL);
        jumpToHome();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (!"0".equals(userId) && Utils.isNetWorkAvailable(context)) {
                    requestUserInfo();
                } else if (!"0".equals(userId) && !Utils.isNetWorkAvailable(context)) {
                    List<LoginUser> jsonList = SharePreferenceUtil.getUtil(context).getJsonDataList("jsonList");
                    UserUtil.setLoginUser(jsonList.get(0));
                    ToastUtil.showToast(context, "连接异常，请检查网络设置");
                    jumpToHome();
                } else {
                    toLoginInfo(1);
                    finish();
                }
            }
        }
    };

    private void loginChat() {
        String user = (String) SPUtil.get(context, "user", "");
        String psw = (String) SPUtil.get(context, "psw", "");

        Disposable subscribe = KKManager.getInstance().login(user, psw, "demo.91kook.com:8282", AuthTypeEnum.eAuthTypeNonePassword)
                .take(1)
                .subscribe(loginResult -> {
                    DialogShowUtil.dismissLoadingDialog();
                    int errorCode = loginResult.getLoginErr();
                    if (errorCode == 0) {
                        // jumpToHome();
                        getSharedPreferences("login", MODE_PRIVATE)
                                .edit().putString("ac", "chengyuanyi")
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
        String loginType = (String) SPUtil.get(this, Constants.LOGINTYPE, "0");
        if ("0".equals(loginType)) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("dataUrl", data);
            startActivity(i);
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
            finish();
        } else {
            Intent intent = new Intent(context, GestureLockLoginActivity.class);
            intent.putExtra("type", "login");
            intent.putExtra(Constants.LOGINTYPE, loginType);
            intent.putExtra("dataUrl", data);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (start) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
