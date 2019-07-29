package com.gongxin.mobilecommand.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentActivity;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.activity.login.LoginActivity;
import com.gongxin.mobilecommand.ui.dialog.FingerprintDialogFragment;
import com.gongxin.mobilecommand.utils.AppManager;
import com.gongxin.mobilecommand.utils.LockUtils;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.view.gesture.GestureLockLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.kook.KKCallback;
import com.kook.KKManager;

import java.util.ArrayList;

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：验证也设置密码（解锁，重置）
 */
public class GestureLockLoginActivity extends FragmentActivity {
    @BindView(R.id.gestureLock)
    GestureLockLayout mGestureLockLayout;
    @BindView(R.id.hintTV)
    TextView hintTV;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.login_btn_back)
    ImageView mLoginBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.head_layout)
    LinearLayout mHeadLayout;
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    private Context mContext;
    private Animation animation;

    /**
     * 最大解锁次数
     */
    private int mNumber = 5;
    /**
     * change:修改手势  login:登录
     */
    private String type;
    private FingerprintDialogFragment dialogFragment;
    private Cipher cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mContext = this;
        type = getIntent().getStringExtra("type");
        String loginType = getIntent().getStringExtra(Constants.LOGINTYPE);

        if ("change".equals(type)) {

            setGestureListener();

            name.setText("请验证已设置手势");
            mHeadLayout.setVisibility(View.VISIBLE);
            ImmersionBar.with(this)
                    .fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
            mTvTitle.setText("重置手势");
            mLoginBtnBack.setOnClickListener(view -> finish());
        } else {
            if ("1".equals(loginType)) {
                //手势
                Boolean isGesture = (Boolean) SPUtil.get(mContext, "isSetGesture", false);
                if (isGesture) {
                    setGestureListener();
                } else {
                    toLoginOut();
                }

            } else if ("2".equals(loginType)) {
                group.setVisibility(View.GONE);
                mIvBg.setVisibility(View.VISIBLE);
                setFingerprint();
            } else {
                ToastUtil.showToast(mContext, "刷脸识别解锁");
                jumpToHome();
            }
        }
    }

    private void setFingerprint() {
        if (LockUtils.supportFingerprint(this)) {
            LockUtils.initKey(); //生成一个对称加密的key
            //生成一个Cipher对象
            cipher = LockUtils.initCipher();
        } else {
            toLoginOut();
        }
        if (cipher != null) {
            showFingerPrintDialog(cipher);
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        dialogFragment = new FingerprintDialogFragment();
        dialogFragment.setCipher(cipher);
        dialogFragment.setIsLoginOut(true);
        dialogFragment.show(getSupportFragmentManager(), "fingerprint");

        dialogFragment.setOnFingerprintSetting(isSucceed -> {
            if (isSucceed) {
                //  ToastUtil.showToast(mContext, "指纹解锁成功！");
                jumpToHome();
            } else {
                ToastUtil.showToast(mContext, "指纹解锁失败！");
            }
        });
        dialogFragment.setOnDismissListener(dialogInterface -> {
            finish();
        });
    }

    private void setGestureListener() {
        String gestureLockPwd = (String) SPUtil.get(mContext, Constants.GESTURELOCK_KEY, "");
        if (!TextUtils.isEmpty(gestureLockPwd)) {
            mGestureLockLayout.setAnswer(gestureLockPwd);
        } else {
            ToastUtil.showToast(mContext, "没有设置过手势密码");
            toLoginOut();
        }
        mGestureLockLayout.setDotCount(3);
        mGestureLockLayout.setMode(GestureLockLayout.VERIFY_MODE);
        //设置手势解锁最大尝试次数 默认 5
        mGestureLockLayout.setTryTimes(5);
        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        mGestureLockLayout.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            @Override
            public void onGestureSelected(int id) {
                //每选中一个点时调用
            }

            @Override
            public void onGestureFinished(boolean isMatched) {
                //绘制手势解锁完成时调用
                if (isMatched) {
                    if ("change".equals(type)) {
                        Intent intent = new Intent(mContext, SetGestureLockActivity.class);
                        startActivity(intent);
                        finish();
                    } else if ("login".equals(type)) {
                        jumpToHome();
                    }
                } else {
                    hintTV.setVisibility(View.VISIBLE);
                    mNumber = --mNumber;
                    hintTV.setText("你还有" + mNumber + "次机会");
                    hintTV.startAnimation(animation);
                    mGestureLockLayout.startAnimation(animation);
                    LockUtils.setVibrate(mContext);
                }
                resetGesture();
            }

            @Override
            public void onGestureTryTimesBoundary() {
                //超出最大尝试次数时调用
                mGestureLockLayout.setTouchable(false);
                toLoginOut();
            }
        });
    }

    /**
     * 重置手势布局（只是布局）
     */
    private void resetGesture() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mGestureLockLayout.resetGesture();
            }
        }, 300);
    }

    private void toLoginOut() {
        SPUtil.put(mContext, "uid", "0");
        ArrayList<LoginUser> loginList = new ArrayList<>();
        LoginUser loginUser = new LoginUser();
        loginUser.setId("0");
        loginList.add(loginUser);
        SharePreferenceUtil.getUtil(mContext).setDataList("jsonList", loginList);
        SPUtil.put(mContext, "usertoken", "null");
        //可以删除以设置手势isSet，logintype

        KKManager.getInstance().logout(new KKCallback() {
            @Override
            public void onError(int i) {
                Log.e("LOGINOUT", "退出失败");
            }

            @Override
            public void onSucceed() {
                Log.e("LOGINOUT", "退出成功");
            }
        });
        finish();
        AppManager.getAppManager().finishAllActivity();
        toLoginInfo(1);
    }

    protected void toLoginInfo(int index) {
        ToastUtil.showToast(mContext, "错误次数过多，请重新登陆");
        Intent in = new Intent(mContext, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("index", index);
        startActivity(in);
    }

    private void jumpToHome() {

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("dataUrl", getIntent().getStringExtra("dataUrl"));
        startActivity(i);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();

    }
}
