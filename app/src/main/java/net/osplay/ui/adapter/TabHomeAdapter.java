package net.osplay.ui.adapter;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import net.osplay.app.MyApplication;
import net.osplay.data.bean.HomeData;
import net.osplay.olacos.R;
import net.osplay.service.entity.HomeBannerBean;
import net.osplay.service.entity.VideoBean;
import net.osplay.ui.fragment.HomeTabFragment;
import net.osplay.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页适配器
 */

public class TabHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TabHomeAdapter";
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_CATE = 2;
    public static final int TYPE_TABLE = 3;

    private Activity mContext;
    private LayoutInflater mInflater;

    private List<HomeData> mDataList;
    private List<VideoBean> newGoodsList;
    private List<VideoBean> hotTopicList;

    public TabHomeAdapter(Activity context, List<HomeData> data, List<VideoBean> newGoodsList, List<VideoBean> hotTopicList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList = new ArrayList<>();
        mDataList.addAll(data);
        this.newGoodsList = new ArrayList<>();
        this.newGoodsList.addAll(newGoodsList);
        this.hotTopicList = new ArrayList<>();
        this.hotTopicList.addAll(hotTopicList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new BannerViewHolder(mInflater.inflate(R.layout.layout_home_banner, parent, false));
            case TYPE_CATE:
                return new CateViewHolder(mInflater.inflate(R.layout.layout_home_cate, parent, false));
            case TYPE_TABLE:
                return new TableViewHolder(mInflater.inflate(R.layout.layout_home_table, parent, false));
            default:
                Log.e(TAG, "onCreateViewHolder: is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mDataList.get(position).getItemType()) {
            case TYPE_BANNER:
                ((BannerViewHolder) holder).bindData((List<HomeBannerBean>) mDataList.get(position).getData());
                break;
            case TYPE_CATE:
                ((CateViewHolder) holder).bindData((List<HomeBannerBean>) mDataList.get(position).getData());
                break;
            case TYPE_TABLE:
                ((TableViewHolder) holder).bindData((List<String>) mDataList.get(position).getData(), newGoodsList, hotTopicList);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();
    }

    //////////////////// view holder ////////////////////

    private static class BannerViewHolder extends RecyclerView.ViewHolder {
        Banner banner;

        BannerViewHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner_home);
        }

        void bindData(List<HomeBannerBean> data) {
            if (data != null && !data.isEmpty()) {
                List<String> images = new ArrayList<>();
                for (HomeBannerBean bean : data) {
                    images.add(bean.getImgUrl());
                }
                bindBanner(images);
            }
        }

        private void bindBanner(List<String> images) {
//            banner.setBannerTitles(titles);//设置标题集合（当 banner 样式有显示 title 时）
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置 banner 样式
            banner.setImageLoader(new GlideImageLoader());//设置图片加载器
            banner.setImages(images);//设置图片集合
            banner.setBannerAnimation(Transformer.DepthPage);//设置 banner 动画效果
            banner.isAutoPlay(true);//设置自动轮播，默认为 true
            banner.setDelayTime(3000);//设置轮播时间
            banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当 banner 模式中有指示器时）
            banner.start();//banner 设置方法全部调用完毕时最后调用
        }
    }

    private static class CateViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rvHomeCate;
        private RecyclerView.LayoutManager layoutManager;
        private List<HomeBannerBean> datas;
        private HomeCateAdapter adapter;

        CateViewHolder(View itemView) {
            super(itemView);
            rvHomeCate = (RecyclerView) itemView.findViewById(R.id.recycler_home_cate);
        }

        void bindData(List<HomeBannerBean> beanList) {
            if (beanList != null && !beanList.isEmpty()) {
                datas = new ArrayList<>();
                datas.addAll(beanList);
                bindCate();
            }
        }

        private void bindCate() {
            layoutManager = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvHomeCate.setLayoutManager(layoutManager);
            adapter = new HomeCateAdapter(datas);
            rvHomeCate.setAdapter(adapter);
        }

        private class HomeCateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private LayoutInflater inflater;
            private List<HomeBannerBean> datas;

            HomeCateAdapter(List<HomeBannerBean> datas) {
                inflater = LayoutInflater.from(MyApplication.getContext());
                this.datas = new ArrayList<>();
                this.datas.addAll(datas);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CateItemViewHolder(inflater.inflate(R.layout.item_img_tv, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((CateItemViewHolder) holder).bindData(datas.get(position));
            }

            @Override
            public int getItemCount() {
                return datas == null ? 0 : datas.size();
            }

            class CateItemViewHolder extends RecyclerView.ViewHolder {
                LinearLayout layout;
                ImageView imgHomeCate;
                TextView tvHomeCate;
                HomeBannerBean cateItemBean;

                CateItemViewHolder(View itemView) {
                    super(itemView);
                    layout = (LinearLayout) itemView.findViewById(R.id.layout_img_tv);
                    imgHomeCate = (ImageView) itemView.findViewById(R.id.img_card_view);
                    tvHomeCate = (TextView) itemView.findViewById(R.id.text_card_view);
                }

                void bindData(HomeBannerBean itemBean) {
                    cateItemBean = itemBean;
                    if (itemBean != null) {//如果有网络数据，加载网络数据
                        Glide.with(MyApplication.getContext()).load(cateItemBean.getImgUrl()).into(imgHomeCate);
                        tvHomeCate.setText(cateItemBean.getName());
                    } else {//否则，加载本地数据
                        Glide.with(MyApplication.getContext()).load(R.mipmap.ic_launcher_round).into(imgHomeCate);
                    }
                }
            }

        }

    }

    private class TableViewHolder extends RecyclerView.ViewHolder {
        private static final int FRAGMENT_NEW_GOODS = 0;
        private static final int FRAGMENT_HOT_TOPIC = 1;

        private RelativeLayout layout;
        private TabLayout tabLayout;
        private FrameLayout content;

        private List<VideoBean> newGoodsList;
        private List<VideoBean> hotTopicList;

        private HomeTabFragment newGoodsFragment;
        private HomeTabFragment hotTopicFragment;
        private HomeTabFragment currentFragment;

        TableViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_home_table);
            tabLayout = (TabLayout) itemView.findViewById(R.id.tab_home_table);
            content = (FrameLayout) itemView.findViewById(R.id.tab_home_content);
        }

        void bindData(final List<String> beanList, List<VideoBean> newGoodsList, List<VideoBean> hotTopicList) {
            this.newGoodsList = new ArrayList<>();
            this.newGoodsList.addAll(newGoodsList);
            this.hotTopicList = new ArrayList<>();
            this.hotTopicList.addAll(hotTopicList);

            createFragment();

            tabLayout.addTab(tabLayout.newTab().setText(beanList.get(0)), true);//设置默认选中
            tabLayout.addTab(tabLayout.newTab().setText(beanList.get(1)));

            //init load
            addFragment(R.id.tab_home_content, newGoodsFragment);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {//选中 tab 的逻辑

                    switch (tab.getPosition()) {
                        case FRAGMENT_NEW_GOODS:
                            onClickNewGoodsFragment();
                            break;
                        case FRAGMENT_HOT_TOPIC:
                            onClickHotTopicFragment();
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {//未选中 tab 的逻辑
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {//重复选中 tab 的逻辑
                }

            });

        }

        private void createFragment() {
            newGoodsFragment = HomeTabFragment.newInstance(newGoodsList);
            hotTopicFragment = HomeTabFragment.newInstance(hotTopicList);
            currentFragment = newGoodsFragment;
        }

        private void addFragment(int containerViewId, Fragment fragment) {
            final FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }

        private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
            if (currentFragment == fragment)
                return;

            if (!fragment.isAdded()) {//如果当前 fragment 未被添加，则添加到 Fragment 管理器中
                transaction.hide(currentFragment).add(R.id.tab_home_content, fragment).commit();
            } else {//如果当前 fragment 已添加，则显示该 Fragment
                transaction.hide(currentFragment).show(fragment).commit();
            }
            currentFragment = (HomeTabFragment) fragment;
        }

        private void onClickNewGoodsFragment() {
            addOrShowFragment(((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction(), newGoodsFragment);
        }

        private void onClickHotTopicFragment() {
            addOrShowFragment(((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction(), hotTopicFragment);
        }
    }

}
