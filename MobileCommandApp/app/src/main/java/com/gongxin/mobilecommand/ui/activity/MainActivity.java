package com.gongxin.mobilecommand.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.ui.dialog.MyDialogHint;
import com.gongxin.mobilecommand.ui.fragment.Fragment1;
import com.gongxin.mobilecommand.ui.fragment.Fragment4;
import com.gongxin.mobilecommand.utils.AppManager;
import com.gongxin.mobilecommand.utils.DensityUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.utils.Utils;
import com.kook.KKManager;
import com.kook.im.ui.home.AbsMenuItem;
import com.kook.im.ui.home.ContactFragment;
import com.kook.im.ui.home.ConversationFragment;
import com.kook.im.ui.home.MainMenuHelper;
import com.kook.sdk.wrapper.KKClient;
import com.kook.sdk.wrapper.StatusCode;
import com.kook.sdk.wrapper.auth.AuthService;
import com.kook.sdk.wrapper.msg.MsgService;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import ezy.ui.view.BadgeButton;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fManager;
    private Fragment1 fragment1;
    private ConversationFragment fragment2;
    private ContactFragment fragment3;
    private Fragment4 fragment4;
    @BindView(R.id.button1)
    RadioButton button1;
    @BindView(R.id.button2)
    RadioButton button2;
    @BindView(R.id.button3)
    RadioButton button3;
    @BindView(R.id.button4)
    RadioButton button4;
    private Context context;
    @BindView(R.id.container)
    LinearLayout llContainer;
    TextView mTitle;
    ImageView mIvMenu;
    ImageView mIvMenuTwo;
    @BindView(R.id.ll_unread)
    LinearLayout ll_unread;
    LinearLayout mHeadLayout;
    @BindView(R.id.unread_dot)
    BadgeButton mUnreadDot;
    private PopupWindow popupWindow;
    private CompositeDisposable msubscription;//管理所有的订阅

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
        context = this;
        AppManager.getAppManager().addActivity(this);
        mHeadLayout = findViewById(R.id.head_layout);
        ImageView mBack = mHeadLayout.findViewById(R.id.login_btn_back);
        mBack.setVisibility(View.GONE);
        mIvMenu = mHeadLayout.findViewById(R.id.login_btn_right);
        mIvMenuTwo = mHeadLayout.findViewById(R.id.login_btn_right_two);
        mTitle = (TextView) mHeadLayout.findViewById(R.id.tv_title);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button1:
                changeToF1();
                break;
            case R.id.button2:
                mHeadLayout.setVisibility(View.VISIBLE);
                mTitle.setText(getString(R.string.main_tab2_txt));
                mIvMenu.setVisibility(View.VISIBLE);
                mIvMenuTwo.setVisibility(View.VISIBLE);
                changeToF2();
                break;
            case R.id.button3:
                mHeadLayout.setVisibility(View.VISIBLE);
                mTitle.setText(getString(R.string.main_tab3_txt));
                mIvMenu.setVisibility(View.VISIBLE);
                mIvMenuTwo.setVisibility(View.VISIBLE);
                changeToF3();
                break;
            case R.id.button4:
                mHeadLayout.setVisibility(View.VISIBLE);
                mTitle.setText(getString(R.string.main_tab4_txt));
                mIvMenu.setVisibility(View.GONE);
                mIvMenuTwo.setVisibility(View.GONE);
                changeToF4();
                break;
            case R.id.login_btn_right:
                popupShow();
                break;
            case R.id.login_btn_right_two:
                List<AbsMenuItem> defaultKKMenuItem = MainMenuHelper.getDefaultKKMenuItem(this);
                defaultKKMenuItem.get(0).onClick();
                break;
            default:
                break;
        }
    }

    private void changeToF1() {
        Utils.hideSoftKeyBoard(this);
        mHeadLayout.setVisibility(View.GONE);
        fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        commonChange(transaction);
        button1.setChecked(true);
        if (fragment1 == null) {
            fragment1 = new Fragment1();
            Bundle bundle = new Bundle();
            bundle.putString("dataUrl", getIntent().getStringExtra("dataUrl"));
            fragment1.setArguments(bundle);
            transaction.add(R.id.container, fragment1, "f1");
        } else {
            transaction.show(fragment1);
        }
        transaction.commitAllowingStateLoss();
    }

    private void changeToF2() {
        Utils.hideSoftKeyBoard(this);
        fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        commonChange(transaction);
        button2.setChecked(true);
        if (fragment2 == null) {
            fragment2 = new ConversationFragment();
            transaction.add(R.id.container, fragment2, "f2");
        } else {
            transaction.show(fragment2);
        }
        transaction.commitAllowingStateLoss();
    }

    private void changeToF3() {
        Utils.hideSoftKeyBoard(this);
        fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        commonChange(transaction);
        button3.setChecked(true);
        if (fragment3 == null) {
            fragment3 = new ContactFragment();
            transaction.add(R.id.container, fragment3, "f3");
        } else {
            transaction.show(fragment3);
        }
        transaction.commitAllowingStateLoss();
    }

    private void changeToF4() {
        Utils.hideSoftKeyBoard(this);
        fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        commonChange(transaction);
        button4.setChecked(true);
        if (fragment4 == null) {
            fragment4 = new Fragment4();
            transaction.add(R.id.container, fragment4, "f4");
        } else {
            transaction.show(fragment4);
        }
        transaction.commitAllowingStateLoss();
    }

    private void commonChange(FragmentTransaction transaction) {
        fragment1 = (Fragment1) fManager.findFragmentByTag("f1");
        fragment2 = (ConversationFragment) fManager.findFragmentByTag("f2");
        fragment3 = (ContactFragment) fManager.findFragmentByTag("f3");
        fragment4 = (Fragment4) fManager.findFragmentByTag("f4");
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }
    }

    @Override
    protected void findViewById() {
        ColorStateList colorStateList = getResources().getColorStateList(
                R.color.tab_text_color);
        button1.setTextColor(colorStateList);
        button2.setTextColor(colorStateList);
        button3.setTextColor(colorStateList);
        button4.setTextColor(colorStateList);

        msubscription = new CompositeDisposable();

        observerUnreadCount(); // 新消息未读数变化
        observerStatus(); // 登陆状态变化
        observerKicked(); // 被强踢
    }

    @Override
    protected void setListener() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        mIvMenuTwo.setOnClickListener(this);
        mIvMenu.setOnClickListener(this);
    }


    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);
        try {
            JSONObject jsonObject = JSON.parseObject(response.body());
            Integer code = jsonObject.getInteger("code");
            if (code == 200) {
                if (requestId == 1) {

                }
            } else if (code == 600) {
                ToastUtil.showToast(context, getString(R.string.toast_outdate_login));
                toLoginInfo(1);
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
    }

    @Override
    protected void processLogic() {

        // requestCheckUpdate();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                jump(getIntent());
            }
        }, 0);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(4 * width / 10 - 35, 0, 0, 0);
        ll_unread.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (((keyCode == KeyEvent.KEYCODE_BACK) ||
                (keyCode == KeyEvent.KEYCODE_HOME))
        ) {

            new MyDialogHint(MainActivity.this, R.style.MyDialog1).show();
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {


        super.onNewIntent(intent);
    }

    private void jump(Intent intent) {
        if (this == null)
            return;
        int index = intent.getIntExtra("index", 1);
        String id = intent.getStringExtra("id");
        Logger.e("index....", index + "");
        switch (index) {
            case 1:
                //changeToF3();
                changeToF1();
                break;
            case 2:
                changeToF2();
                break;
            case 3:
                changeToF3();
                break;
            case 4:
                changeToF4();
                break;
            default:
                break;
        }
    }

    private void observerKicked() {
        Disposable kickedSubscription = KKManager.getInstance().observerKickedEvent()
                .subscribe(s -> Log.i("MainActivity", "被强制踢出 -> " + s));
        //用于解除绑定
        msubscription.add(kickedSubscription);
    }

    private void observerStatus() {
        //订阅 登陆状态
        Disposable statusResultSub = KKClient.getService(AuthService.class)
                .observeStatus().subscribe(statusCode -> {
                    switch (statusCode) {
                        case StatusCode.logining:
                            //正在登陆中 or 重连中
                            //                          ToastUtil.showToast(context, "正在登陆...");
                        case StatusCode.logined:
                            //登陆成功
                            // ToastUtil.showToast(context, "登陆成功");
                            break;
                        case StatusCode.unlogin:
                            //未登陆
                            //                      ToastUtil.showToast(context, "未登录");
                            break;
                    }
                });
        msubscription.add(statusResultSub);
    }

    private void observerUnreadCount() {
        int totalUnReadCount = KKClient.getService(MsgService.class).getAllConversationUnReadCount();
        // tvState.setText("未读消息数: #" + totalUnReadCount);
        setCountView(totalUnReadCount);
        //收到了新消息，未读数统计
        Disposable conversiationComeSub = KKClient.getService(MsgService.class).conversationCome()
                .subscribe(holder -> {
                    int totalUnReadCount1 = KKClient.getService(MsgService.class).getAllConversationUnReadCount();
                    // tvState.setText("未读消息数: #" + totalUnReadCount);
                    setCountView(totalUnReadCount1);
                });
        msubscription.add(conversiationComeSub);
    }

    private void setCountView(int totalCount) {
        if (totalCount == 0) {
            mUnreadDot.setBadgeVisible(false);
            mUnreadDot.invalidate();
        } else if (totalCount > 0 && totalCount < 100) {
            mUnreadDot.setBadgeVisible(true);
            mUnreadDot.invalidate();
            mUnreadDot.setBadgeText(totalCount + "");
        } else {
            mUnreadDot.setBadgeVisible(true);
            mUnreadDot.invalidate();
            mUnreadDot.setBadgeText("99+");
        }
    }

    private void popupShow() {
        if (popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.popup_layout, null);
            popupWindow = new PopupWindow(view, DensityUtil.dip2px(
                    context, 120), LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(view);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());

            LinearLayout ll_selectone = (LinearLayout) view
                    .findViewById(R.id.ll_selectone);
            LinearLayout ll_selecttwo = (LinearLayout) view
                    .findViewById(R.id.ll_selecttwo);
            ll_selectone.setOnClickListener(view1 -> {
                List<AbsMenuItem> defaultKKMenuItem = MainMenuHelper.getDefaultKKMenuItem(MainActivity.this);
                defaultKKMenuItem.get(1).onClick();
                // ChatActivity.launch(context, Long.parseLong("1900024812292569"), EConvType.ECONV_TYPE_GROUP, "");
                popupWindow.dismiss();
            });
            ll_selecttwo.setOnClickListener(view12 -> {
                List<AbsMenuItem> defaultKKMenuItem = MainMenuHelper.getDefaultKKMenuItem(MainActivity.this);
                defaultKKMenuItem.get(4).onClick();
               /* ForwardItem forwardItem = ForwardUtils.buidForward(KKIMMessageFactory.createLinkCardMsg(EConvType.ECONV_TYPE_SINGLE, 1, "http://baid.com", "title", "content", "", true));
                ForwardCommand forwardCommand = new ForwardCommand() {
                    @Override
                    public void onChooseResult(SoftReference<com.kook.im.ui.BaseActivity> context, ArrayList<ChooseResultNode> arrayList) {
                        super.onChooseResult(context, arrayList);
                        for (ChooseResultNode baseMultiItemEntity : arrayList) {
                            if (baseMultiItemEntity.getDataType() == ChooseType.USER_TYPE) {
                                //用户id
                                ToastUtil.showToast(MainActivity.this, "用户id" + baseMultiItemEntity.getId());
                            }
                            if (baseMultiItemEntity.getDataType() == ChooseType.GROUP_TYPE) {
                                //群id
                                ToastUtil.showToast(MainActivity.this, "群id" + baseMultiItemEntity.getId());
                            }
                        }
                    }
                };
                forwardCommand.setForwardItem(forwardItem);
                ChooseActivity.launch(context, forwardCommand, ChooseDataSrcProvider.getForwardOption(context.getString(R.string.forword_choose_out_msg)));*/
                // ForwardUtils.startForword(this, KKIMMessageFactory.createLinkCardMsg(EConvType.ECONV_TYPE_GROUP,111,"http://www.baidu.com","sss","dsafsdafd","",true));
                popupWindow.dismiss();
            });
        }
        int xPos = mIvMenu.getWidth() - popupWindow.getWidth() - 55;
        popupWindow
                .showAsDropDown(mIvMenu, xPos, 50);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msubscription.dispose();
    }

}
