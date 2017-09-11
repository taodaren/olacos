package net.osplay.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.osplay.app.MyApplication;
import net.osplay.olacos.R;
import net.osplay.service.entity.VideoBean;
import net.osplay.service.entity.base.HomeData;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区 → 我的适配器
 */

public class WordMineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "WordMineAdapter";
    public static final int TYPE_ADD_WORD = 0;
    public static final int TYPE_RECOM_WORD = 1;

    private Activity mContext;
    private LayoutInflater mInflater;

    private List<HomeData> mDataList;
    private List<VideoBean> mAddWordList;
    private List<VideoBean> mRecomList;

    public WordMineAdapter(Activity context, List<HomeData> data, List<VideoBean> addWordList, List<VideoBean> recommendList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);

        this.mDataList = new ArrayList<>();
        this.mAddWordList = new ArrayList<>();
        this.mRecomList = new ArrayList<>();

        this.mDataList.addAll(data);
        this.mAddWordList.addAll(addWordList);
        this.mRecomList.addAll(recommendList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADD_WORD:
                return new AddViewHolder(mInflater.inflate(R.layout.layout_word_add, parent, false));
            case TYPE_RECOM_WORD:
                return new RecomViewHolder(mInflater.inflate(R.layout.layout_word_recommend, parent, false));
            default:
                Log.e(TAG, "onCreateViewHolder: is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mDataList.get(position).getItemType()) {
            case TYPE_ADD_WORD:
                ((AddViewHolder) holder).bindData((List<VideoBean>) mDataList.get(position).getData());
                break;
            case TYPE_RECOM_WORD:
                ((RecomViewHolder) holder).bindData((List<VideoBean>) mDataList.get(position).getData());
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

    private static class AddViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rvAddWord;
        private List<VideoBean> addWordList;
        private StaggeredGridLayoutManager layoutManager;
        private MineAddAdapter adapter;

        private AddViewHolder(View itemView) {
            super(itemView);
            rvAddWord = (RecyclerView) itemView.findViewById(R.id.recycler_add_word);
        }

        private void bindData(List<VideoBean> beanList) {
            if (beanList != null && !beanList.isEmpty()) {
                this.addWordList = new ArrayList<>();
                this.addWordList.addAll(beanList);
                bindRecom();
            }
        }

        private void bindRecom() {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rvAddWord.setLayoutManager(layoutManager);
            adapter = new MineAddAdapter(MyApplication.getContext(), addWordList);
            rvAddWord.setAdapter(adapter);
        }

        private class MineAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private Context context;
            private LayoutInflater inflater;
            private List<VideoBean> datas;

            private MineAddAdapter(Context context, List<VideoBean> datas) {
                this.context = context;
                this.inflater = LayoutInflater.from(MyApplication.getContext());
                this.datas = new ArrayList<>();
                this.datas.addAll(datas);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = inflater.inflate(R.layout.item_add_word, parent, false);
                AddItemViewHolder holder = new AddItemViewHolder(inflate);
                holder.setClickListener();
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((AddItemViewHolder) holder).bindData(datas.get(position));
            }

            @Override
            public int getItemCount() {
                return datas == null ? 0 : datas.size();
            }

            private class AddItemViewHolder extends RecyclerView.ViewHolder {
                private View outView;//保存子项最外层布局的实例
                private LinearLayout layout;
                private ImageView imgAvatar;
                private TextView textNick;

                private AddItemViewHolder(View itemView) {
                    super(itemView);
                    outView = itemView;
                    layout = (LinearLayout) itemView.findViewById(R.id.layout_add_word);
                    imgAvatar = (ImageView) itemView.findViewById(R.id.add_word_avatar);
                    textNick = (TextView) itemView.findViewById(R.id.add_word_type);
                }

                private void bindData(VideoBean videoBean) {
                    Glide.with(MyApplication.getContext()).load(videoBean.getCoverImg()).into(imgAvatar);
                    textNick.setText(videoBean.getVideoTitle().substring(0, 3));
                }

                private void setClickListener() {
                    outView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            VideoBean videoBean = datas.get(position);
                            Toast.makeText(context, "点击" + videoBean.getMovieName() + "布局", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imgAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            VideoBean videoBean = datas.get(position);
                            Toast.makeText(context, "跳转到" + videoBean.getMovieName() + "个人界面", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }

    }

    private static class RecomViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rvRecomWord;
        private List<VideoBean> recomWordList;
        private RecyclerView.LayoutManager layoutManager;
        private MineRecomAdapter adapter;

        private RecomViewHolder(View itemView) {
            super(itemView);
            rvRecomWord = (RecyclerView) itemView.findViewById(R.id.recycler_recommend_word);
        }

        private void bindData(List<VideoBean> beanList) {
            if (beanList != null && !beanList.isEmpty()) {
                this.recomWordList = new ArrayList<>();
                this.recomWordList.addAll(beanList);
                bindRecommend();
            }
        }

        private void bindRecommend() {
            layoutManager = new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);
            rvRecomWord.setLayoutManager(layoutManager);
            adapter = new MineRecomAdapter(MyApplication.getContext(), recomWordList);
            rvRecomWord.setAdapter(adapter);
        }

        private class MineRecomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private Context context;
            private LayoutInflater inflater;
            private List<VideoBean> datas;

            private MineRecomAdapter(Context context, List<VideoBean> datas) {
                this.context = context;
                this.inflater = LayoutInflater.from(MyApplication.getContext());
                this.datas = new ArrayList<>();
                this.datas.addAll(datas);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = inflater.inflate(R.layout.item_recommend_word, parent, false);
                RecomItemViewHolder holder = new RecomItemViewHolder(inflate);
                holder.setClickListener();
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((RecomItemViewHolder) holder).bindData(datas.get(position));
            }

            @Override
            public int getItemCount() {
                return datas == null ? 0 : datas.size();
            }

            private class RecomItemViewHolder extends RecyclerView.ViewHolder {
                private View outView;//保存子项最外层布局的实例
                private LinearLayout layout;
                private ImageView imgAvatar;
                private Button btnAdd;
                private TextView textNick, textMember, textPosts, textInfo;

                private RecomItemViewHolder(View itemView) {
                    super(itemView);
                    outView = itemView;
                    layout = (LinearLayout) itemView.findViewById(R.id.layout_recommend_word);
                    imgAvatar = (ImageView) itemView.findViewById(R.id.recommend_word_avatar);
                    btnAdd = (Button) itemView.findViewById(R.id.recommend_word_add);
                    textNick = (TextView) itemView.findViewById(R.id.recommend_word_type);
                    textMember = (TextView) itemView.findViewById(R.id.recommend_word_member);
                    textPosts = (TextView) itemView.findViewById(R.id.recommend_word_posts);
                    textInfo = (TextView) itemView.findViewById(R.id.recommend_word_info);
                }

                private void bindData(VideoBean videoBean) {
                    Glide.with(MyApplication.getContext()).load(videoBean.getCoverImg()).into(imgAvatar);
                    textNick.setText(videoBean.getVideoTitle().substring(0, 2));
                    textMember.setText("成员:" + videoBean.getId());
                    textPosts.setText("帖子:" + videoBean.getMovieId());
                    textInfo.setText(videoBean.getSummary());
                }

                private void setClickListener() {
                    outView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            VideoBean videoBean = datas.get(position);
                            Toast.makeText(context, "点击" + videoBean.getMovieName() + "布局", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imgAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            VideoBean videoBean = datas.get(position);
                            Toast.makeText(context, "跳转到" + videoBean.getMovieName() + "个人界面", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }

    }

}