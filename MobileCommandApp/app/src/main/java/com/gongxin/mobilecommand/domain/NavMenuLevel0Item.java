package com.gongxin.mobilecommand.domain;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.gongxin.mobilecommand.adapter.NavMenuExpandableItemAdapter.TYPE_LEVEL_0;

/**
 * Created by jiazhen on 2019-07-30.
 * Desc:
 */
public class NavMenuLevel0Item extends AbstractExpandableItem<NavMenuLevel1Item> implements MultiItemEntity {

    private String name;
    private int id;//1
    private int parentId;//0
    private String url;
    private String path;//0-1

    public NavMenuLevel0Item(String name) {
        this.name = name;
    }

    public NavMenuLevel0Item() {
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return TYPE_LEVEL_0;
    }
}
