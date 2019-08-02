package com.gongxin.mobilecommand.domain;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gongxin.mobilecommand.adapter.NavMenuExpandableItemAdapter.TYPE_LEVEL_1;

/**
 * Created by jiazhen on 2019-07-30.
 * Desc:
 */
public class NavMenuLevel1Item extends AbstractExpandableItem<McTargetMenuItem> implements MultiItemEntity {
    private String name;
    private int id;//1
    private int parentId;//0
    private String url;
    private String path;//0-1
    public NavMenuLevel1Item(String name) {
        this.name = name;
    }

    public NavMenuLevel1Item() {
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
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return TYPE_LEVEL_1;
    }
}
