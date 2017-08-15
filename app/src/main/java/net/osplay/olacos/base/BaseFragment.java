package net.osplay.olacos.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;

import net.osplay.olacos.R;
import net.osplay.olacos.activity.LoginActivity;

/**
 * Fragment 基类
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;

    /**
     * 当该类被系统创建的时候回调
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        // 加上这句话，menu才会显示出来
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 抽象类，由子类实现，实现不同的效果
     */
    public abstract View initView();

    /**
     * 当子类需要联网请求数据的时候，可以重写该方法，该方法中联网请求
     */
    public void initData() {
    }

    /**
     * 设置 Toolbar
     *
     * @param toolbarId       menu_toolbar ID
     * @param title           标题
     * @param titleVisibility 标题控件是否显示
     * @param cityVisibility  城市选择控件是否显示
     * @param isHomeBottom    侧滑控件是否显示（true 为显示 false 为隐藏）
     * @return menu_toolbar
     */
    public Toolbar setToolbar(int toolbarId, int title, int titleVisibility, int cityVisibility, boolean isHomeBottom) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(toolbarId);
        appCompatActivity.setSupportActionBar(toolbar);

        //设置标题
        TextView textTitle = (TextView) getActivity().findViewById(R.id.title_toolbar);
        textTitle.setVisibility(titleVisibility);
        textTitle.setText(title);

        //城市选择
        RelativeLayout cityLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_city);
        cityLayout.setVisibility(cityVisibility);
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPicker cityPicker = new CityPicker.Builder(getContext()).textSize(20)
                        .title("地址选择")
                        .backgroundPop(0xa0000000)
                        .titleBackgroundColor("#f7c936")
                        .titleTextColor("#ffffff")
                        .confirTextColor("#ffffff")
                        .cancelTextColor("#ffffff")
                        .province("北京市")
                        .city("北京市")
                        .district("昌平区")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .onlyShowProvinceAndCity(false)
                        .build();
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        //省份
                        String province = citySelected[0];
                        //城市
                        String city = citySelected[1];
                        //区县（如果设定了两级联动，那么该项返回空）
                        String district = citySelected[2];
                        //邮编
                        String code = citySelected[3];

                        TextView textCity = (TextView) getActivity().findViewById(R.id.text_city);
                        textCity.setText(district);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getContext(), "已取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            //隐藏左上角图标（true 为显示）
            actionBar.setDisplayHomeAsUpEnabled(isHomeBottom);
            //设置侧滑导航按钮图标
            actionBar.setHomeAsUpIndicator(R.drawable.title_mi);
            //隐藏 Toolbar 自带标题栏
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    /**
     * 判断用户是否已经登录
     */
    public void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息
            startActivity(new Intent(getContext(), LoginActivity.class));
        } else {
            //已经登录过，则直接加载用户的信息并显示
        }
    }

    /**
     * 保存用户信息
     * 基类中写读取用户信息的代码，其中参数在有接口的情况，可传入user实体类，存入的值是user.getName
     *
     * @param name
     * @param password
     */
    public void saveUser(String name, String password) {
        SharedPreferences sp = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.commit();
    }

}
