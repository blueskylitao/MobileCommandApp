package com.gongxin.mobilecommand.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gongxin.mobilecommand.adapter.NavMenuExpandableItemAdapter.TYPE_MENU_TARGET;

/**
 * Created by jiazhen on 2019-07-30.
 * Desc:侧边栏指标菜单项
 */

public class McTargetMenuItem implements MultiItemEntity {
    private String name;
    private int id;//1
    private int parentId;//0
    private String url;
    private String path;//0-1

    public McTargetMenuItem(String name, int id, int parentId, String url, String path) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.url = url;
        this.path = path;
    }

    public McTargetMenuItem(String name) {
        this.name = name;
    }

    public McTargetMenuItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
