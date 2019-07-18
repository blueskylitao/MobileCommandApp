package com.gongxin.mobilecommand.utils;

import android.util.Log;


import com.gongxin.mobilecommand.domain.LoginUser;
import com.gongxin.mobilecommand.global.MCApp;

import java.util.List;

public class UserUtil {
    public static LoginUser loginUser;

    public static synchronized String getUid() {
        if (null == loginUser) {
            loginUser = getLoginUser();
        }

        return loginUser.getAppUserId();
    }

    public synchronized static void setUid(String uid) {
        if (null == loginUser) {
            loginUser = getLoginUser();
        }
        loginUser.setAppUserId(uid);
    }

    public synchronized static LoginUser getLoginUser() {
        if (null == loginUser) {
            //TODO::reload user info.
            List<LoginUser> jsonList = SharePreferenceUtil.getUtil(MCApp.getContext()).getJsonDataList("jsonList");
            if (jsonList.size() == 0 || null == jsonList) {
                loginUser = new LoginUser();
                loginUser.setAppUserId("0");
                Log.w("JCC_USER", "-------新用户本地没保存-------");
            } else {
                //loginUser = JSON.parseObject(loginUserStr, LoginUser.class);
                //  Log.w("JCC_USER", "------ 本地保存了-------uid=" + loginUser.getId());
                return jsonList.get(0);
            }
        }

        return loginUser;
    }

    public static void setLoginUser(LoginUser ui) {
        loginUser = ui;
    }

}
