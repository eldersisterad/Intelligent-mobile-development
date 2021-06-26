package com.work.checkwork.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.work.checkwork.R;
import com.work.checkwork.activity.UserHomeActivity;
import com.work.checkwork.activity.XiangActivity;
import com.work.checkwork.adapter.holder.BaseRecyclerHolder;
import com.work.checkwork.dao.DBService;
import com.work.checkwork.model.DataBean;
import com.work.checkwork.model.LikeBean;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.AppUtil;
import com.work.checkwork.utils.GlideEngine;

import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;


public class FashionAdapter extends BaseRecyclerAdapter<DataBean, FashionAdapter.FashionHolder>
        implements View.OnClickListener {

    private Context mContext;

    public FashionAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected FashionHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.item_fashion, parent, false);
        view.setOnClickListener(this);
        return new FashionHolder(view);
    }

    @Override
    protected void onBindBaseViewHolder(FashionHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(this, v, (Integer) v.getTag());
        }
    }

    public class FashionHolder extends BaseRecyclerHolder<DataBean> implements View.OnClickListener {

        private ImageView imgItem;
        private TextView tvDescribe, userName, likeNum;
        private CircleImageView mUserImg;
        private CheckBox cbLike;
        private UserBean mUserBean;
        private DataBean mDataBean;

        public FashionHolder(View itemView) {
            super(itemView);
            mUserBean = AppPreferences.getParcelableEntity(mContext, AppConstant.EXTRA_USERINFO, UserBean.class);
            imgItem = itemView.findViewById(R.id.img_item);
            tvDescribe = itemView.findViewById(R.id.tv_describe);
            userName = itemView.findViewById(R.id.user_name);
            likeNum = itemView.findViewById(R.id.like_num);
            mUserImg = itemView.findViewById(R.id.user_photo_view);
            cbLike = itemView.findViewById(R.id.cb_like);
            cbLike.setOnClickListener(this);
            mUserImg.setOnClickListener(this);
            imgItem.setOnClickListener(this);
        }

        @Override
        public void bindData(DataBean data) {
            mDataBean = data;
            UserBean dataUser = data.userBean;
            GlideEngine.createGlideEngine().loadImage(mContext, data.imgUrl, imgItem);
            AppUtil.loadImage(mContext, dataUser.userPic, mUserImg);
            tvDescribe.setText(data.content);
            userName.setText(dataUser.userName);
            likeNum.setText(data.likeNum + "");
            cbLike.setChecked(data.isLike);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_photo_view:
                    Intent intent = new Intent(mContext, UserHomeActivity.class);
                    intent.putExtra("UserBean", mDataBean.userBean);
                    mContext.startActivity(intent);
                    break;
                case R.id.img_item:
                    Intent xiangIntent = new Intent(mContext, XiangActivity.class);
                    xiangIntent.putExtra("DataBean", mDataBean);
                    mContext.startActivity(xiangIntent);
                    break;
                case R.id.cb_like:
                    LikeBean likeBean = new LikeBean(mDataBean.id, mUserBean.id);
                    addLikeData(likeBean);
                    break;
                default:
                    break;
            }

        }

        private void addLikeData(LikeBean likeBean) {
            new Thread(() -> {
                LikeBean mLikeBean = DBService.getDbService().getLikeData(likeBean.userId, likeBean.dataId);
                if (mLikeBean == null) {
                    int likeInsert = DBService.getDbService().insertLike(likeBean);
                    if (likeInsert == 1) {
                        mDataBean.likeNum = (mDataBean.likeNum + 1);
                        if (DBService.getDbService().updateData(mDataBean.likeNum, mDataBean.id) == 1) {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.obj = mDataBean.likeNum;
                            handler.sendMessage(msg);
                            cbLike.setChecked(true);
                        }
                    }
                } else {
                    int likeDelete = DBService.getDbService().delLikeData(mDataBean.id, mUserBean.id);
                    if (likeDelete == 1) {
                        mDataBean.likeNum = (mDataBean.likeNum - 1);
                        if (DBService.getDbService().updateData(mDataBean.likeNum, mDataBean.id) == 1) {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.obj = mDataBean.likeNum;
                            handler.sendMessage(msg);
                            cbLike.setChecked(false);
                        }
                    }
                }
            }).start();
        }


        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        likeNum.setText(msg.obj + "");
                        break;
                    default:
                        break;
                }
            }
        };

    }
} 