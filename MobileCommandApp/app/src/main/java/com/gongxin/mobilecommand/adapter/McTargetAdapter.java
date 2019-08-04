package com.gongxin.mobilecommand.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.domain.McTargetMenuItem;

import java.util.List;

/**
 * Created by jiazhen on 2019-08-01.
 * Desc:
 */
public class McTargetAdapter extends BaseQuickAdapter<McTargetMenuItem, BaseViewHolder> {

    private OnTargetItemClickListener onTargetItemClickListener;
    public McTargetAdapter(int layoutResId, @Nullable List<McTargetMenuItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, McTargetMenuItem item) {
        helper.setText(R.id.title,item.getName());
        helper.getView(R.id.title).setOnClickListener(v -> {
            if (onTargetItemClickListener!=null) {
                onTargetItemClickListener.onTitleClick(item);
            }
        });
        if(item.getChild()!=null && item.getChild().size()>0){
            helper.setImageResource(R.id.iv,R.mipmap.nav_menu_expand);
            helper.getView(R.id.iv).setOnClickListener(v -> {
                if (onTargetItemClickListener!=null) {
                    onTargetItemClickListener.onArrowClick(item);
                }
            });
        }else {
            helper.setImageResource(R.id.iv,R.mipmap.nav_menu_arrow_right);
            helper.getView(R.id.iv).setOnClickListener(v -> {
                if (onTargetItemClickListener!=null) {
                    onTargetItemClickListener.onTitleClick(item);
                }
            });
        }


    }

    public void setOnTargetItemClickListener(OnTargetItemClickListener onTargetItemClickListener) {
        this.onTargetItemClickListener = onTargetItemClickListener;
    }

    public interface OnTargetItemClickListener{
        void onTitleClick(McTargetMenuItem mcTargetMenuItem);
        void onArrowClick(McTargetMenuItem mcTargetMenuItem);
    }


}
