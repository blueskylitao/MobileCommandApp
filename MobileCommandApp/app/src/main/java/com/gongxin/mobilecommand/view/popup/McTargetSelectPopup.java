package com.gongxin.mobilecommand.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.adapter.McTargetAdapter;
import com.gongxin.mobilecommand.domain.McTargetMenuItem;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.lxj.xpopup.core.PositionPopupView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by jiazhen on 2019-08-01.
 * Desc:
 */
public class McTargetSelectPopup extends PositionPopupView implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = McTargetMenuItem.class.getSimpleName();

    private Stack<McTargetMenuItem> ids = new Stack<>();

    private McTargetAdapter mcTargetAdapter;

    private OnTargetItemClickListener mOnTargetItemClickListener;

    public McTargetSelectPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_target_select_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        Button btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        TextView mTvBackLevel = findViewById(R.id.tv_last_level);
        mTvBackLevel.setOnClickListener(this);

        RecyclerView mRvTargetList = findViewById(R.id.popup_rv_target);
        mRvTargetList.setLayoutManager(new LinearLayoutManager(getContext()));
        mcTargetAdapter = new McTargetAdapter(R.layout.item_mc_target_layout,new ArrayList<>());
        //mcTargetAdapter.setOnItemClickListener(this);
        mcTargetAdapter.setOnTargetItemClickListener(new McTargetAdapter.OnTargetItemClickListener() {
            @Override
            public void onTitleClick(McTargetMenuItem mcTargetMenuItem) {
                ToastUtil.shortToast(getContext(),"跳转"+mcTargetMenuItem.getUrl());
            }

            @Override
            public void onArrowClick(McTargetMenuItem mcTargetMenuItem) {
                if (mOnTargetItemClickListener!=null){
                    if (mcTargetMenuItem != null){
                        setTitle(mcTargetMenuItem.getName());
                        ids.push(mcTargetMenuItem);
                        mOnTargetItemClickListener.onTargetItemClick(mcTargetMenuItem);
                    }
                }
            }
        });
        mRvTargetList.setAdapter(mcTargetAdapter);

    }

    public void toggleData(List<McTargetMenuItem> list){
        mcTargetAdapter.replaceData(list);
    }

    public void setOnTargetItemClickListener(OnTargetItemClickListener mOnTargetItemClickListener) {
        this.mOnTargetItemClickListener = mOnTargetItemClickListener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_last_level){
            if (ids.size() <= 1){
                toggle();
                return;
            }
            if (mOnTargetItemClickListener!=null){
                ids.pop();
                McTargetMenuItem item = ids.peek();
                setTitle(item.getName());
                mOnTargetItemClickListener.onTargetItemClick(item);
            }
        }else if(v.getId() == R.id.btn_close){
            toggle();
        }

    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        setTitle("");
        ids.clear();
        mcTargetAdapter.replaceData(new ArrayList<>());
    }


    public interface OnTargetItemClickListener{
        void onTargetItemClick(McTargetMenuItem id);
    }

    public void pushParentId(McTargetMenuItem item){
        if (ids.isEmpty()){
            ids.push(item);
            setTitle(item.getName());
        }
    }

    public void setTitle(String title){
        TextView titleView = findViewById(R.id.tv_title);
        titleView.setText(title);
    }
}
