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
    private TextView mTvBackLevel;

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

        mTvBackLevel = findViewById(R.id.tv_last_level);
        mTvBackLevel.setOnClickListener(this);

        RecyclerView mRvTargetList = findViewById(R.id.popup_rv_target);
        mRvTargetList.setLayoutManager(new LinearLayoutManager(getContext()));
        mcTargetAdapter = new McTargetAdapter(R.layout.item_mc_target_layout,new ArrayList<>());
        mcTargetAdapter.setOnItemClickListener(this);
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
        if (mOnTargetItemClickListener!=null){
            McTargetMenuItem item = (McTargetMenuItem) adapter.getItem(position);
            if (item != null){
                setTitle(item.getName());
                ids.push(item);
                mOnTargetItemClickListener.onTargetItemClick(item);
            }
        }
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
        ids.clear();
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

    private void setTitle(String title){
        TextView titleView = findViewById(R.id.tv_title);
        titleView.setText(title);
    }
}
