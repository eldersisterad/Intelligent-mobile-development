package com.work.checkwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.work.checkwork.R;
import com.work.checkwork.activity.UserHomeActivity;
import com.work.checkwork.adapter.holder.BaseRecyclerHolder;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.utils.AppUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuanZhuAdapter extends BaseRecyclerAdapter<UserBean, GuanZhuAdapter.GuanZhuHolder> {

    public GuanZhuAdapter(Context context) {
        super(context);
    }

    @Override
    protected GuanZhuHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.item_guanzhu, parent, false);
        return new GuanZhuHolder(view);
    }

    @Override
    protected void onBindBaseViewHolder(GuanZhuHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemView.setTag(position);
    }

    public class GuanZhuHolder extends BaseRecyclerHolder<UserBean> implements View.OnClickListener {

        private CircleImageView user_photo_view;
        private TextView teacher_name, user_id;
        private UserBean mUserBean;

        public GuanZhuHolder(View itemView) {
            super(itemView);
            user_photo_view = itemView.findViewById(R.id.user_photo_view);
            teacher_name = itemView.findViewById(R.id.teacher_name);
            user_id = itemView.findViewById(R.id.user_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(UserBean data) {
            mUserBean = data;
            AppUtil.loadImage(getContext(), data.userPic, user_photo_view);
            teacher_name.setText(data.userName);
            user_id.setText(data.userPhone);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), UserHomeActivity.class);
            intent.putExtra("UserBean", mUserBean);
            getContext().startActivity(intent);
        }
    }
}