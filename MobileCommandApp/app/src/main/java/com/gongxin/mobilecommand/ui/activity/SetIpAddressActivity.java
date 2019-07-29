package com.gongxin.mobilecommand.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.utils.HttpUtil;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;

import butterknife.BindView;

public class SetIpAddressActivity extends BaseActivity {


    @BindView(R.id.login_btn_back)
    ImageView mLoginBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_setip)
    EditText mEtSetip;
    @BindView(R.id.tv_set)
    TextView mTvSet;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_set_ip_address);
    }

    @Override
    protected void findViewById() {
        mTvTitle.setText("设置地址");
        mEtSetip.setText(HttpUtil.BASEURL);
    }

    @Override
    protected void setListener() {
        mLoginBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEtSetip.getText().toString().trim())) {
                    ToastUtil.shortToast(context, "地址不能为空！");
                    return;
                }
                HttpUtil.BASEURL = mEtSetip.getText().toString().trim();
                SPUtil.put(context, "ipaddress", mEtSetip.getText().toString().trim());
                ToastUtil.showToast(context, "修改成功");
            }
        });
    }

    @Override
    protected void processLogic() {


    }
}

