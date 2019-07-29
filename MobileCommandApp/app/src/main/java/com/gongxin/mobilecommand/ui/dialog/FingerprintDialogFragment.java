package com.gongxin.mobilecommand.ui.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import androidx.fragment.app.DialogFragment;

import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.global.Constants;
import com.gongxin.mobilecommand.ui.activity.login.LoginActivity;
import com.gongxin.mobilecommand.utils.SPUtil;
import com.gongxin.mobilecommand.utils.SharePreferenceUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.kook.KKCallback;
import com.kook.KKManager;

import java.util.ArrayList;

import javax.crypto.Cipher;

@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {

    //    private FingerprintManager fingerprintManager;
    private FingerprintManagerCompat fingerprintManagerCompat;

    private CancellationSignal mCancellationSignal;

    private Cipher mCipher;

    private Context mActivity;

    private TextView errorMsg;

    private Boolean isLoginOut;

    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    public void setIsLoginOut(Boolean isLoginOut) {
        this.isLoginOut = isLoginOut;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fingerprintManager = getContext().getSystemService(FingerprintManager.class);
        fingerprintManagerCompat = FingerprintManagerCompat.from(mActivity);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fingerprint_dialog, container, false);
        errorMsg = v.findViewById(R.id.error_msg);
        TextView cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                stopListening();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始指纹认证监听
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止指纹认证监听
        stopListening();
    }

    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);

//        fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, CharSequence errString) {
//                if (!isSelfCancelled) {
//                    errorMsg.setText(errString);
//                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
//                        Toast.makeText(mActivity, errString, Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                }
//            }
//
//            @Override
//            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                errorMsg.setText(helpString);
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//                Toast.makeText(mActivity, "指纹认证成功", Toast.LENGTH_SHORT).show();
//                mActivity.onAuthenticated();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                errorMsg.setText("指纹认证失败，请再试一次");
//            }
//        }, null);

        fingerprintManagerCompat.authenticate(cryptoObject, 0, mCancellationSignal, new MyCallBack(), null);

    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            if (!isSelfCancelled) {
                // errorMsg.setText(errString);
                Log.e("TAG", "errMsgId=" + errMsgId);
                //Toast.makeText(mActivity, "errMsgId=" + errMsgId, Toast.LENGTH_SHORT).show();
                if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                    Log.e("TAG", "" + errString);
                    errorMsg.setText("错误次数过多,请稍候再试");
                    Vibrator vibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                    if (isLoginOut) {
                        toLoginOut();
                    }
                    //  dismiss();
                }
            }
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            errorMsg.setText("指纹认证失败，请再试一次");
            Vibrator vibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            Log.e("TAG", "onAuthenticationFailed");
        }

        //错误时提示帮助，比如说指纹错误，我们将显示在界面上 让用户知道情况
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            // errorMsg.setText(helpString);
            Log.e("TAG", "helpString=" + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            FingerprintManagerCompat.CryptoObject cryptoObject = result.getCryptoObject();
          /*  try {
                byte[] bytes = cryptoObject.getCipher().doFinal();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }*/
            if (onFingerprintSetting != null) {
                onFingerprintSetting.onFingerprint(true);
            }
            dismiss();
        }
    }

    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }

    private OnFingerprintSetting onFingerprintSetting;

    public void setOnFingerprintSetting(
            OnFingerprintSetting onFingerprintSetting) {
        this.onFingerprintSetting = onFingerprintSetting;
    }

    public interface OnFingerprintSetting {
        void onFingerprint(boolean isSucceed);
    }

    private DialogInterface.OnDismissListener mOnClickListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mOnClickListener = listener;
    }

    //做一些弹框的初始化，以及创建一个弹框

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnClickListener != null) {
            mOnClickListener.onDismiss(dialog);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListening();
    }

    private void toLoginOut() {
        SPUtil.put(mActivity, "uid", "0");
        ArrayList<LoginUser> loginList = new ArrayList<>();
        LoginUser loginUser = new LoginUser();
        loginUser.setId("0");
        loginList.add(loginUser);
        SharePreferenceUtil.getUtil(mActivity).setDataList("jsonList", loginList);
        SPUtil.put(mActivity, "usertoken", "null");
        SPUtil.put(mActivity, Constants.LOGINTYPE, "0");

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
        getActivity().finish();
        toLoginInfo(1);
    }

    protected void toLoginInfo(int index) {
        ToastUtil.showToast(mActivity, "错误次数过多，请重新登陆");
        Intent in = new Intent(mActivity, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra("index", index);
        startActivity(in);
    }
}
