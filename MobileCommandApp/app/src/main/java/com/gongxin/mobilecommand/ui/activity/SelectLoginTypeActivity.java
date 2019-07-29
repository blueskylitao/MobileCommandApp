package com.gongxin.mobilecommand.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.dialog.FingerprintDialogFragment;
import com.gongxin.mobilecommand.utils.LockUtils;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;

import javax.crypto.Cipher;

import butterknife.BindView;

public class SelectLoginTypeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.login_btn_back)
    ImageView mLoginBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.RB1)
    RadioButton mRB1;
    @BindView(R.id.RB2)
    RadioButton mRB2;
    @BindView(R.id.RB3)
    RadioButton mRB3;
    @BindView(R.id.RG)
    RadioGroup mRG;
    @BindView(R.id.view_line)
    View viewLine;
    private Cipher cipher;
    private FingerprintDialogFragment dialogFragment;
    private int checkId = 0;
    private final int SETGESTURELOCK = 100;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login_type);
    }

    @Override
    protected void findViewById() {
        mTvTitle.setText("登陆方式");
        if (!LockUtils.supportFingerprint(context)) {
            mRB2.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {

            LockUtils.initKey();
            cipher = LockUtils.initCipher();
        }
        String login_type = SPUtil.get(context, Constants.LOGINTYPE, "0") + "";
        if ("1".equals(login_type)) {
            mRB1.setChecked(true);
            checkId = R.id.RB1;
        } else if ("2".equals(login_type)) {
            mRB2.setChecked(true);
            checkId = R.id.RB2;
        } else if ("3".equals(login_type)) {
            mRB3.setChecked(true);
            checkId = R.id.RB3;
        }
    }

    @Override
    protected void setListener() {
        mLoginBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRG.setOnCheckedChangeListener(this);
    }

    @Override
    protected void processLogic() {


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.RB1:
                Boolean isSetGesture = (Boolean) SPUtil.get(context, "isSetGesture", false);
                if (isSetGesture) {
                    checkId = R.id.RB1;
                    SPUtil.put(context, Constants.LOGINTYPE, "1");
                } else {
                    //去设置手势
                    Intent intent = new Intent(context, SetGestureLockActivity.class);
                    startActivityForResult(intent, SETGESTURELOCK);
                }
                break;
            case R.id.RB2:

                showFingerPrintDialog(cipher);

                break;
            case R.id.RB3:
                checkId = R.id.RB3;
                //  ToastUtil.showToast(context, "设置扫脸验证成功");
                SPUtil.put(context, Constants.LOGINTYPE, "3");
                break;
            default:
                break;
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        dialogFragment = new FingerprintDialogFragment();
        dialogFragment.setCipher(cipher);
        dialogFragment.setIsLoginOut(false);
        dialogFragment.show(getSupportFragmentManager(), "fingerprint");

        dialogFragment.setOnFingerprintSetting(isSucceed -> {
            if (isSucceed) {
                checkId = R.id.RB2;
                SPUtil.put(context, Constants.LOGINTYPE, "2");
                ToastUtil.showToast(context, "设置指纹验证成功！");
            } else {
                ToastUtil.showToast(context, "设置指纹验证失败！");
            }
        });
        dialogFragment.setOnDismissListener(dialogInterface -> {
            setIdChecked();
        });
    }

    private void setIdChecked() {
        if (checkId == R.id.RB1) {
            mRB1.setChecked(true);
        } else if (checkId == R.id.RB2) {
            mRB2.setChecked(true);
        } else if (checkId == R.id.RB3) {
            mRB3.setChecked(true);
        } else {
            mRB1.setChecked(false);
            mRB2.setChecked(false);
            mRB3.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SETGESTURELOCK:
                    checkId = R.id.RB3;
                    SPUtil.put(context, Constants.LOGINTYPE, "1");
                    SPUtil.put(context, "isSetGesture", true);
                    ToastUtil.showToast(context, "设置手势验证成功");
                    break;
            }
        } else {
            switch (requestCode) {
                case SETGESTURELOCK:
                    setIdChecked();
                    break;
            }
        }
    }
}

