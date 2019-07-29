package com.gongxin.mobilecommand.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.ui.activity.GestureLockLoginActivity;
import com.gongxin.mobilecommand.ui.activity.SelectLoginTypeActivity;
import com.gongxin.mobilecommand.ui.activity.SetGestureLockActivity;
import com.gongxin.mobilecommand.ui.activity.SetIpAddressActivity;
import com.gongxin.mobilecommand.utils.DataCleanManager;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.kook.KKCallback;
import com.kook.KKManager;

import java.util.ArrayList;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * author:geekLi
 */
public class Fragment4 extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.login_btn_back)
    ImageView mLoginBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_nick_name)
    TextView mTvNickName;
    @BindView(R.id.tv_login_name)
    TextView mTvLoginName;
    @BindView(R.id.tv_department)
    TextView mTvDepartment;
    @BindView(R.id.rl_set_info)
    RelativeLayout mRlSetInfo;
    @BindView(R.id.rl_login_type)
    RelativeLayout mRlLoginType;
    @BindView(R.id.rl_set_psw)
    RelativeLayout mRlSetPsw;
    @BindView(R.id.rl_set_gesture)
    RelativeLayout mRlSetGesture;
    @BindView(R.id.rl_set_fingerprint)
    RelativeLayout mRlSetFingerprint;
    @BindView(R.id.rl_set_face)
    RelativeLayout mRlSetFace;
    @BindView(R.id.tv_cachesize)
    TextView mTvCachesize;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout mRlClearCache;
    @BindView(R.id.rl_set_ipaddress)
    RelativeLayout mRlSetIpaddress;
    @BindView(R.id.bt_loginout)
    TextView mBtLoginout;
    private final int SETGESTURELOCK = 100;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_me_list, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
        mLoginBtnBack.setVisibility(View.GONE);
        mTvTitle.setText(R.string.main_tab4_txt);
        mBtLoginout.setOnClickListener(this);
        mRlClearCache.setOnClickListener(this);
        mRlLoginType.setOnClickListener(this);
        mRlSetFace.setOnClickListener(this);
        mRlSetGesture.setOnClickListener(this);
        mRlSetIpaddress.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String cacheSize = "0M";
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTvCachesize.setText(cacheSize);
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_loginout:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getResources().getString(R.string.dialog_tips_login_out));
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.dialog_tips_exit_confirm)
                        , (DialogInterface dialog, int which) -> {
                            SPUtil.put(context, "uid", "0");
                            ArrayList<LoginUser> loginList = new ArrayList<>();
                            LoginUser loginUser = new LoginUser();
                            loginUser.setId("0");
                            loginList.add(loginUser);
                            SharePreferenceUtil.getUtil(context).setDataList("jsonList", loginList);
                            SPUtil.put(context, "usertoken", "null");
                            getActivity().finish();
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
                            toLoginInfo(1);
                        }).setNegativeButton(getResources().getString(R.string.dialog_tips_exit_cancel),
                        (dialog, which) -> dialog.dismiss());
                builder.create().show();
                break;
            case R.id.rl_clear_cache:
                DataCleanManager.clearAllCache(context);
                mTvCachesize.setText("0M");
                ToastUtil.showToast(context, "缓存清理完成");
                break;
            case R.id.rl_login_type:

                Intent intent = new Intent(context, SelectLoginTypeActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_set_face:

                break;
            case R.id.rl_set_gesture:
                Boolean isSetGesture = (Boolean) SPUtil.get(context, "isSetGesture", false);
                if (isSetGesture) {
                    //修改手势
                    Intent intent2 = new Intent(context, GestureLockLoginActivity.class);
                    intent2.putExtra("type", "change");
                    startActivity(intent2);

                } else {
                    //没有设置
                    Intent intent1 = new Intent(context, SetGestureLockActivity.class);
                    startActivityForResult(intent1, SETGESTURELOCK);
                }
                break;
            case R.id.rl_set_ipaddress:
                Intent intent2 = new Intent(context, SetIpAddressActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SETGESTURELOCK:
                    SPUtil.put(context, "isSetGesture", true);
                    ToastUtil.showToast(context, "设置手势验证成功");
                    break;
            }
        }
    }
}
