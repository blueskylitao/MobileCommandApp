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

    public NavMenuLevel1Item(String name) {
        this.name = name;
    }

    public NavMenuLevel1Item() {
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
