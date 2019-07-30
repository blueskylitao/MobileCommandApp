package com.gongxin.mobilecommand.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gongxin.mobilecommand.adapter.NavMenuExpandableItemAdapter.TYPE_MENU_TARGET;

/**
 * Created by jiazhen on 2019-07-30.
 * Desc:侧边栏指标菜单项
 */

public class McTargetMenuItem implements MultiItemEntity {
    private String name;

    public McTargetMenuItem(String name) {
        this.name = name;
    }

    public McTargetMenuItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return TYPE_MENU_TARGET;
    }
}
