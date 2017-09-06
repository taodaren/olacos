package net.osplay.ui.fragment.sub;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import net.osplay.app.I;
import net.osplay.data.bean.HomeData;
import net.osplay.olacos.R;
import net.osplay.service.entity.HomeBannerBean;
import net.osplay.ui.activity.sub.LoginActivity;
import net.osplay.ui.adapter.TabHomeAdapter;
import net.osplay.ui.fragment.base.BaseFragment;
import net.osplay.utils.HomeDataMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 首页模块
 */

public class TabHomeFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "TabHomeFragment";
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private DrawerLayout mDrawerLayout;//侧滑菜单
    private TabHomeAdapter mHomeAdapter;
    private RecyclerView mRvHome;
    private LinearLayoutManager mLayoutManager;
    private Gson mGson = new Gson();
    private List<HomeBannerBean> bannerBeanList;

    @Override
    public View initView() {
        View inflate = View.inflate(getContext(), R.layout.fragment_tab_home, null);
        //注意 getActivity()若使用 view 会报错，此处有大坑
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mRvHome = (RecyclerView) inflate.findViewById(R.id.recycler_home);

        initDrawerLayout();
        return inflate;
    }

    @Override
    public void initData() {
        super.initData();
        // TODO: 2017/9/6 处理网络数据
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(I.HOME_BANNER, RequestMethod.GET);
        requestQueue.add(0, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String json = response.get();//得到请求数据
                Log.e(TAG, "onSucceed: " + json);

                //数据解析（集合）
                Type type = new TypeToken<List<HomeBannerBean>>() {
                }.getType();
                bannerBeanList = mGson.fromJson(json, type);

                initRecyclerView();

                //数据解析(解析对象)
//                HomeBannerBean bannerBean = mGson.fromJson(json, HomeBannerBean.class);
//                String imgUrl = bannerBean.getImgUrl();
//                Log.e(TAG, "onSucceed: " + imgUrl);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        mRvHome.setLayoutManager(mLayoutManager);
        mRvHome.setHasFixedSize(true);

        List<HomeData> list = new ArrayList<>();
        list.add(HomeDataMapper.transformBannerDatas(bannerBeanList, TabHomeAdapter.TYPE_BANNER, false));
        mHomeAdapter = new TabHomeAdapter(getContext(), list);

        mRvHome.setAdapter(mHomeAdapter);
    }

    /**
     * 在 onActivityCreated 方法中初始化 Toolbar
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbar(R.id.toolbar_home, R.string.home_name, View.GONE, View.VISIBLE, true);
        requestCodeQRCodePermissions();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //显示菜单
        inflater.inflate(R.menu.menu_toolbar, menu);
        //显示需要菜单项，隐藏多余菜单项
        menu.findItem(R.id.menu_code).setVisible(false);
        menu.findItem(R.id.menu_msg).setVisible(true);
        menu.findItem(R.id.menu_category).setVisible(false);
        menu.findItem(R.id.menu_register).setVisible(false);
        menu.findItem(R.id.menu_set).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://导航按钮固定 id
                //展示滑动菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.menu_msg:
//                Toast.makeText(mContext, "menu_msg", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

}
