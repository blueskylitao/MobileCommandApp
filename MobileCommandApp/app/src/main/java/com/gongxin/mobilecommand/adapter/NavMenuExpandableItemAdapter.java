package com.gongxin.mobilecommand.adapter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.McTargetMenuItem;
import com.gongxin.mobilecommand.domain.NavMenuLevel0Item;
import com.gongxin.mobilecommand.domain.NavMenuLevel1Item;

import java.util.List;

/**
 */
public class NavMenuExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = NavMenuExpandableItemAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_MENU_TARGET = 2;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NavMenuExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_nav_menu_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_nav_menu_expandable_lv1);
        addItemType(TYPE_MENU_TARGET, R.layout.item_nav_menu_expandable_lv2);
    }


    @Override
    protected void convert(@NonNull final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final NavMenuLevel0Item lv0 = (NavMenuLevel0Item) item;
                holder.setText(R.id.title, lv0.getName())
                        .setImageResource(R.id.iv, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    Log.d(TAG, "Level 0 item pos: " + pos);
                    if (lv0.isExpanded()) {
                        collapse(pos);
                    } else {
//                            if (pos % 3 == 0) {
//                                expandAll(pos, false);
//                            } else {
                        expand(pos);
//                            }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final NavMenuLevel1Item lv1 = (NavMenuLevel1Item) item;
                holder.setText(R.id.title, lv1.getName())
                        .setImageResource(R.id.iv, lv1.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    Log.d(TAG, "Level 1 item pos: " + pos);
                    if (lv1.isExpanded()) {
                        collapse(pos, false);
                    } else {
                        expand(pos, false);
                    }
                });

                holder.itemView.setOnLongClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    // 先获取到当前 item 的父 positon，再移除自己
                    int positionAtAll = getParentPositionInAll(pos);
                    remove(pos);
                    if (positionAtAll != -1) {
                        IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
                        if (!hasSubItems(multiItemEntity)) {
                            remove(positionAtAll);
                        }
                    }
                    return true;
                });
                break;
            case TYPE_MENU_TARGET:
                final McTargetMenuItem person = (McTargetMenuItem) item;
                holder.setText(R.id.title, person.getName());
                holder.itemView.setOnClickListener(view -> {
                    //int pos = holder.getAdapterPosition();
                    // 先获取到当前 item 的父 positon，再移除自己
//                        int positionAtAll = getParentPositionInAll(pos);
//                        remove(pos);
//                        if (positionAtAll != -1) {
//                            IExpandable multiItemEntity = (IExpandable) getData().get(positionAtAll);
//                            if (!hasSubItems(multiItemEntity)) {
//                                remove(positionAtAll);
//                            }
//                        }
                });
                break;
            default:
                break;
        }
    }
}
