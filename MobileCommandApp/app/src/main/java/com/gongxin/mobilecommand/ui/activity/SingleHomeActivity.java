package com.gongxin.mobilecommand.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gongxin.mobilecommand.R;
import com.gongxin.mobilecommand.adapter.NavMenuExpandableItemAdapter;
import com.gongxin.mobilecommand.base.BaseActivity;
import com.gongxin.mobilecommand.domain.McTargetMenuItem;
import com.gongxin.mobilecommand.domain.NavMenuLevel0Item;
import com.gongxin.mobilecommand.domain.NavMenuLevel1Item;
import com.gongxin.mobilecommand.ui.fragment.DashboardFragment;
import com.gongxin.mobilecommand.utils.DensityUtil;
import com.gongxin.mobilecommand.utils.ToastUtil;
import com.gongxin.mobilecommand.view.popup.McTargetSelectPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class SingleHomeActivity extends BaseActivity implements NavMenuExpandableItemAdapter.OnLevel1ItemClickListener, McTargetSelectPopup.OnTargetItemClickListener, View.OnKeyListener, View.OnClickListener {

    private static final String TAG = SingleHomeActivity.class.getSimpleName();
    private static final int REQUEST_TYPE_TARGET_CATEGORY = 1;
    private static final int REQUEST_TYPE_TARGET_TARGET = 2;
    private static final int REQUEST_TYPE_TARGET_SEARCH = 3;
    private static final int REQUEST_TYPE_TARGET_USUAL = 4;

    private Fragment mDashboardFragment;
    private NavMenuExpandableItemAdapter mNavMenuExpandableItemAdapter;

    private McTargetSelectPopup mcTargetSelectPopup;

    private BasePopupView mcTargetSelectPopupView;
    private EditText mNavEtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_home);
        initActionBarAndNavView();
        initContent();
        loadDataFromServe();
    }

    private void initTargetPopup() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mcTargetSelectPopup = new McTargetSelectPopup(context);
        mcTargetSelectPopup.setOnTargetItemClickListener(this);
        mcTargetSelectPopupView =  new XPopup.Builder(context)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .offsetY(displayMetrics.heightPixels- DensityUtil.dip2px(this,634))
                .offsetX(findViewById(R.id.nav_view).getMeasuredWidth()+20)
                .hasShadowBg(false)
                .moveUpToKeyboard(false)
                .asCustom(mcTargetSelectPopup);
    }

    private void loadDataFromServe() {
        showProgressDialog(getString(R.string.dialog_loading));
        loadMenuCategoryData(0,REQUEST_TYPE_TARGET_CATEGORY);
    }

    /**
     * 加载菜单栏指标分类数据
     */
    private void loadMenuCategoryData(int parentId,int requestId) {

        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("parentId",parentId);
            httpRequestByGet("/command/targetTree", httpParams, requestId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchTarget(String targetName){
        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("targetName",targetName);
            httpRequestByGet("/command/getTargetByName", httpParams, REQUEST_TYPE_TARGET_SEARCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsualTarget(){
        try {
            HttpParams httpParams = new HttpParams();
            httpParams.put("count",20);
            httpRequestByGet("/command/usual", httpParams, REQUEST_TYPE_TARGET_USUAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initContent() {
        mDashboardFragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mDashboardFragment, DashboardFragment.TAG)        //.addToBackStack("fname")
                .commit();
    }


    private void initActionBarAndNavView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mcTargetSelectPopupView==null){
                    initTargetPopup();
                }
            }
        });
        toggle.syncState();
        //侧边导航
        RecyclerView navRvMenu = findViewById(R.id.nav_rv_menu);
        navRvMenu.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MultiItemEntity> list = new ArrayList<>();
        mNavMenuExpandableItemAdapter = new NavMenuExpandableItemAdapter(list,this);
        navRvMenu.setAdapter(mNavMenuExpandableItemAdapter);
        mNavMenuExpandableItemAdapter.setLevel1ItemClickListener(this);

        mNavEtSearch = findViewById(R.id.nav_et_search);
        mNavEtSearch.setOnKeyListener(this);

        findViewById(R.id.nav_tv_usual).setOnClickListener(this);
    }

    @Override
    protected void onHttpRequestResult(Response<String> response, int requestId) {
        super.onHttpRequestResult(response, requestId);
        Log.e(TAG, "onHttpRequestResult: "+response.body() );
        if (requestId == REQUEST_TYPE_TARGET_CATEGORY) handMenuCategoryData(response);
        if (requestId == REQUEST_TYPE_TARGET_TARGET) handMenuTargetData(response);
        if (requestId == REQUEST_TYPE_TARGET_SEARCH) handTargetSearch(response);

    }

    @Override
    protected void onHttpRequestErr(Response<String> response, int requestId) {
        super.onHttpRequestErr(response, requestId);
    }

    private void handMenuCategoryData(Response<String> response) {
        List<NavMenuLevel0Item> navMenuLevel0Items = JSON.parseArray(response.body(), NavMenuLevel0Item.class);
        for (NavMenuLevel0Item navMenuLevel0Item : navMenuLevel0Items) {
            navMenuLevel0Item.setSubItems(navMenuLevel0Item.getChild());
        }
        mNavMenuExpandableItemAdapter.addData(navMenuLevel0Items);
    }

    private void handMenuTargetData(Response<String> response) {
        List<McTargetMenuItem> mcTargetMenuItemList = JSON.parseArray(response.body(), McTargetMenuItem.class);
        mcTargetSelectPopup.toggleData(mcTargetMenuItemList);
    }

    private void handTargetSearch(Response<String> response){
        List<McTargetMenuItem> mcTargetMenuItemList = JSON.parseArray(response.body(), McTargetMenuItem.class);
        mcTargetSelectPopup.toggleData(mcTargetMenuItemList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void loadViewLayout() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    public void onLevel1ItemClick(NavMenuLevel1Item item) {
        McTargetMenuItem mcTargetMenuItem = new McTargetMenuItem();
        mcTargetMenuItem.setName(item.getName());
        mcTargetMenuItem.setId(item.getId());
        mcTargetSelectPopup.pushParentId(mcTargetMenuItem);
        mcTargetSelectPopupView.toggle();
        loadMenuCategoryData(item.getId(),REQUEST_TYPE_TARGET_TARGET);
    }

    @Override
    public void onTargetItemClick(McTargetMenuItem item) {
        loadMenuCategoryData(item.getId(),REQUEST_TYPE_TARGET_TARGET);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                KeyboardUtils.hideSoftInput(v);
                String targetName = mNavEtSearch.getText().toString();
                if (TextUtils.isEmpty(targetName)){
                    ToastUtil.shortToast(this,"请输入指标名称");
                    return false;
                }
                mcTargetSelectPopupView.toggle();
                searchTarget(targetName);
                return true;
            }
            return false;

    }

    @Override
    public void onClick(View v) {
        mcTargetSelectPopupView.toggle();
        loadUsualTarget();
    }
}
