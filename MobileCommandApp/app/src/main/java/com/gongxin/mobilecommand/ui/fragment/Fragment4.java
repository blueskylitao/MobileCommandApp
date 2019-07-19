package com.gongxin.mobilecommand.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseFragment;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.utils.DataCleanManager;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.kook.KKCallback;
import com.kook.KKManager;

import java.util.ArrayList;

import butterknife.BindView;

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
    @BindView(R.id.tv_cachesize)
    TextView mTvCachesize;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout mRlClearCache;
    @BindView(R.id.bt_loginout)
    TextView mBtLoginout;

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
            default:
                break;
        }
    }
}
