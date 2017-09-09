package net.osplay.ui.fragment.sub;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import net.osplay.app.SetOnClickListen;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import net.osplay.olacos.R;
import net.osplay.ui.activity.sub.JoinCommunityDetailsActivity;
import net.osplay.service.entity.LeagueBean;
import net.osplay.service.entity.RecommendBean;
import net.osplay.ui.adapter.sub.HeatRankAdapter;
import net.osplay.ui.fragment.base.BaseBussFragment;

import java.util.List;

/**
 * 社团-热门排行
 */
public class HeatRankFragment extends BaseBussFragment {

    private Gson mGson = new Gson();
    private HeatRankAdapter hAdapter;
    private RecyclerView heat_recy;
    private HeatRankAdapter hAdapter;
    private List<LeagueBean.TrailersBean> trailers;

    @SuppressLint("ValidFragment")
    public HeatRankFragment() {
    }

    @SuppressLint("ValidFragment")
    public HeatRankFragment(Context mContext, int resId) {
        super(mContext, resId);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        heat_recy= (RecyclerView) view.findViewById(R.id.heat_recy);
        hAdapter=new HeatRankAdapter(getActivity());
        heat_recy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        heat_recy.setAdapter(hAdapter);
        setOnClickListen();
        heat_recy = (RecyclerView) view.findViewById(R.id.heat_recy);
        heat_recy.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {

    }

    private void setOnClickListen() {
        SetOnClickListen setOnClickListen=new SetOnClickListen() {
            @Override
            public void setOnClick(int position) {
                startActivity(new Intent(context,JoinCommunityDetailsActivity.class));
            }
        };
        hAdapter.onClick(setOnClickListen);

            @Override
            public void onSucceed(int what, Response<String> response) {
                String json = response.get();//得到请求数据
                Log.e("TAG", json);
                RecommendBean recommendBean = mGson.fromJson(json, RecommendBean.class);
                trailers = recommendBean.getTrailers();
                hAdapter = new HeatRankAdapter(getActivity(), trailers, R.layout.item_heat_community);
                heat_recy.setAdapter(hAdapter);

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
