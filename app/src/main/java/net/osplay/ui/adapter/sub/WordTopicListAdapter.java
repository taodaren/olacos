package net.osplay.ui.adapter.sub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.osplay.app.AppHelper;
import net.osplay.app.I;
import net.osplay.app.MFGT;
import net.osplay.olacos.R;
import net.osplay.service.entity.WordTopicListBean;
import net.osplay.ui.activity.sub.DetailsPostsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区帖子详情 → 全部/精品/同城
 */

public class WordTopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<WordTopicListBean.RowsBean> mRowsBeanList;

    public WordTopicListAdapter(Context mContext, List<WordTopicListBean.RowsBean> rowsBeanList) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRowsBeanList = new ArrayList<>();
        this.mRowsBeanList.addAll(rowsBeanList);
    }

    public void setData(List<WordTopicListBean.RowsBean> data) {
        if (mRowsBeanList != null) {
            mRowsBeanList.clear();
        } else {
            mRowsBeanList = new ArrayList<>();
        }
        mRowsBeanList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public PostsInfoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PostsInfoListHolder holder = new PostsInfoListHolder(mInflater.inflate(R.layout.item_word_topic_list, parent, false));
        holder.setClickListener();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PostsInfoListHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return mRowsBeanList == null ? 0 : mRowsBeanList.size();
    }

    private class PostsInfoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "PostsInfoListHolder";
        private int flag;
        private View outView;
        private LinearLayout llZan, llCollect, llComment;
        private TextView tvNick, tvTime, tvTitle, tvZan, tvCollect, tvComment;
        private ImageView imgAvatar, imgBg, imgZan, imgCollect;
        private String postsId;//帖子 ID

        private PostsInfoListHolder(View itemView) {
            super(itemView);
            outView = itemView;
            tvNick = (TextView) itemView.findViewById(R.id.tv_topic_list_nick);
            tvTime = (TextView) itemView.findViewById(R.id.tv_topic_list_time);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_topic_list_title);
            tvZan = (TextView) itemView.findViewById(R.id.tv_topic_list_zan);
            tvCollect = (TextView) itemView.findViewById(R.id.tv_topic_list_collect);
            tvComment = (TextView) itemView.findViewById(R.id.tv_topic_list_comment);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_topic_list_avatar);
            imgBg = (ImageView) itemView.findViewById(R.id.img_topic_list_icon);
            imgZan = (ImageView) itemView.findViewById(R.id.img_topic_list_zan);
            imgCollect = (ImageView) itemView.findViewById(R.id.img_topic_list_collect);
            llZan = (LinearLayout) itemView.findViewById(R.id.ll_topic_list_zan);
            llCollect = (LinearLayout) itemView.findViewById(R.id.ll_topic_list_collect);
            llComment = (LinearLayout) itemView.findViewById(R.id.ll_topic_list_comment);
        }

        public void bindData(int position) {
            WordTopicListBean.RowsBean rowsBean = mRowsBeanList.get(position);
            if (rowsBean.getHEAD_PATH() != null) {
                Glide.with(mContext).load(I.BASE_URL + rowsBean.getHEAD_PATH()).into(imgAvatar);
            } else {
                Glide.with(mContext).load(R.drawable.avatar_default).into(imgAvatar);
            }
            Glide.with(mContext).load(I.BASE_URL + rowsBean.getCOVERIMG()).into(imgBg);
            tvNick.setText(rowsBean.getNICK_NAME());
            tvTime.setText(rowsBean.getCREATEDATE());
            tvTitle.setText(rowsBean.getTITLE());
            tvZan.setText(rowsBean.getZAN_COUNT());
            tvCollect.setText(rowsBean.getCOLLECT_COUNT());
            tvComment.setText(rowsBean.getPINGLUN_COUNT());
            postsId = rowsBean.getID();
        }

        public void setClickListener() {
            outView.setOnClickListener(this);
            llZan.setOnClickListener(this);
            llCollect.setOnClickListener(this);
            llComment.setOnClickListener(this);
            imgAvatar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            WordTopicListBean.RowsBean bean = mRowsBeanList.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.img_topic_list_avatar:
                    Toast.makeText(mContext, "click Avatar", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_topic_list_zan://点赞
                    if (!(AppHelper.getInstance().isLogined())) {
                        MFGT.gotoLogin(mContext, "loginHeck");
                    } else {
                        if (flag == 0) {
                            imgZan.setImageResource(R.drawable.ic_sugar_selected);
                            tvZan.setText(String.valueOf(Integer.parseInt(bean.getZAN_COUNT()) + 1));
                        } else if (flag == 1) {
                            imgZan.setImageResource(R.drawable.ic_sugar_unselected);
                            tvZan.setText(String.valueOf(Integer.parseInt(bean.getZAN_COUNT()) - 1));
                        }
                        flag = (flag + 1) % 2;
                    }
                    break;
                case R.id.ll_topic_list_collect://收藏
                    if (!(AppHelper.getInstance().isLogined())) {
                        MFGT.gotoLogin(mContext, "loginHeck");
                    } else {
                        if (flag == 0) {
                            imgCollect.setImageResource(R.drawable.ic_collect_selected);
                            Toast.makeText(mContext, "collect+1", Toast.LENGTH_SHORT).show();
                        } else if (flag == 1) {
                            imgCollect.setImageResource(R.drawable.ic_collect_unselected);
                            Toast.makeText(mContext, "collect-1", Toast.LENGTH_SHORT).show();
                        }
                        flag = (flag + 1) % 2;
                    }
                    break;
                case R.id.ll_topic_list_comment://评论
                    Toast.makeText(mContext, "click comment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "postsId=================================" + postsId);
                    Intent intent = new Intent(mContext, DetailsPostsActivity.class);
                    intent.putExtra(I.Posts.POSTS_ID, postsId);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

}